/*
 * Copyright (c) 2021 Enixor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.enixor.bot.xorbot.user;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.Optional;

public class UserRepository {

    private final HikariDataSource dataSource;

    public UserRepository(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User save(long discordId, String username, long balance) throws UserCreationException {
        if (this.existsByDiscordId(discordId)) {
            return this.findByDiscordId(discordId).orElseThrow(() -> new IllegalStateException("Cannot find user that exists in the database."));
        }

        long id;
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (discord_id, username, balance) VALUES (?, ?, ?);",
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, discordId);
            statement.setString(2, username);
            statement.setLong(3, balance);

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new UserCreationException(discordId, username, "Affected " + affectedRows + " rows but should affect only 1.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new UserCreationException(discordId, username, "Something went wrong while retrieving user id from database.");
                }
                id = generatedKeys.getLong(1);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new UserCreationException(discordId, username, "Something went wrong while querying the database.");
        }

        return XorUser.builder()
                .id(id)
                .discordId(discordId)
                .username(username)
                .balance(balance)
                .build();
    }

    public void deleteByDiscordId(long discordId) {
        if (!this.existsByDiscordId(discordId)) {
            return;
        }

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE discord_id = ?;")) {
            statement.setLong(1, discordId);

            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Optional<User> findByDiscordId(long discordId) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE discord_id = ?;")) {
            statement.setLong(1, discordId);

            ResultSet result = statement.executeQuery();
            if (result == null || !result.next()) {
                return Optional.empty();
            }

            return Optional.of(XorUser.builder()
                    .id(result.getLong("id"))
                    .discordId(discordId)
                    .username(result.getString("username"))
                    .balance(result.getLong("balance"))
                    .build());
        } catch (SQLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean existsByDiscordId(long discordId) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id FROM users WHERE discord_id = ?;")){
            statement.setLong(1, discordId);

            return statement.executeQuery().next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public long getMoneyByDiscordId(long discordId) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT balance FROM users WHERE discord_id = ?;")) {
            statement.setLong(1, discordId);

            ResultSet result = statement.executeQuery();
            if (result == null || !result.next()) {
                return 0L;
            }

            return result.getLong("balance");
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1L;
        }
    }

    public long setMoneyByDiscordId(long discordId, long amount, MoneyOperation operation) {
        if (amount < 0) {
            return -1L;
        }

        String updateQuery = null;
        switch (operation) {
            case SET -> updateQuery = "UPDATE users SET balance = ? WHERE discord_id = ?;";
            case ADD -> updateQuery = "UPDATE users SET balance = balance + ? WHERE discord_id = ?;";
            case SUBTRACT -> updateQuery = "UPDATE users SET balance = balance - ? WHERE discord_id = ?;";
        }

        if (updateQuery == null) {
            throw new IllegalArgumentException("Money operation cannot be null.");
        }

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
             PreparedStatement resultStatement = connection.prepareStatement("SELECT balance FROM users WHERE discord_id = ?;")) {
            connection.setAutoCommit(false);

            updateStatement.setLong(1, amount);
            updateStatement.setLong(2, discordId);
            updateStatement.executeUpdate();

            resultStatement.setLong(1, discordId);
            ResultSet result = resultStatement.executeQuery();

            connection.commit();

            if (result == null || !result.next()) {
                return 0L;
            }

            return result.getLong("balance");
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1L;
        }
    }

    enum MoneyOperation {

        SET,
        ADD,
        SUBTRACT

    }

}
