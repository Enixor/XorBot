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
package io.github.enixor.bot.xorbot;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.enixor.bot.xorbot.command.*;
import io.github.enixor.bot.xorbot.listener.MessageReceivedListener;
import io.github.enixor.bot.xorbot.user.UserRepository;
import io.github.enixor.bot.xorbot.user.UserService;
import io.github.enixor.bot.xorbot.user.XorUserService;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class XorBot {

    public void run(JDABuilder jdaBuilder) throws LoginException {
        BasicConfigurator.configure();
        Logger logger = JDALogger.getLog("DISCORD-BOT");

        HikariDataSource dataSource = new HikariDataSource(new HikariConfig("/database.properties"));

        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                "discord_id VARCHAR(20) NOT NULL UNIQUE KEY," +
                "username VARCHAR(32) NOT NULL," +
                "balance BIGINT UNSIGNED DEFAULT 0);";
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            logger.info("Checking if table 'users' exist...");
            if (statement.executeUpdate() == 0) {
                logger.info("Table 'users' exists, skipping...");
            } else {
                logger.info("Created new table 'users'.");
            }
        } catch (SQLException exception) {
            logger.error("Could not create-if-not-exists table 'users'. Something went wrong.");
            exception.printStackTrace();
            return;
        }

        CommandRegistry commandRegistry = new CommandRegistry();

        UserService userService = new XorUserService(new UserRepository(dataSource));

        logger.info("Registering default commands...");
        commandRegistry.register(new HelpCommand(commandRegistry));
        commandRegistry.register(new MoneyCommand(userService));
        logger.info("Registered all default commands.");

        logger.info("Registering listeners...");
        jdaBuilder.addEventListeners(new MessageReceivedListener(commandRegistry)).build();
        logger.info("Registered all listeners. JDA Built, ready to go.");
    }

}
