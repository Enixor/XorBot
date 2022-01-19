package io.github.zrdzn.bot.xorbot.economy;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EconomyRepository {

    private final HikariDataSource dataSource;
    private final Logger logger;

    public EconomyRepository(HikariDataSource dataSource, Logger logger) {
        this.dataSource = dataSource;
        this.logger = logger;
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
            this.logger.error("Could not select user's balance from database.", exception);
            return -1L;
        }
    }

    public long setMoneyByDiscordId(long discordId, long amount, MoneyOperation operation) {
        if (amount < 0) {
            return -1L;
        }

        String updateQuery;
        switch (operation) {
            case SET -> updateQuery = "UPDATE users SET balance = ? WHERE discord_id = ?;";
            case ADD -> updateQuery = "UPDATE users SET balance = balance + ? WHERE discord_id = ?;";
            case SUBTRACT -> updateQuery = "UPDATE users SET balance = balance - ? WHERE discord_id = ?;";
            default -> updateQuery = null;
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
            this.logger.error("Could not update user in database.", exception);
            return -1L;
        }
    }

    enum MoneyOperation {

        SET,
        ADD,
        SUBTRACT

    }

}
