/*
 * Copyright (c) 2022 zrdzn
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
package io.github.zrdzn.bot.xorbot;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.zrdzn.bot.xorbot.command.CommandRegistry;
import io.github.zrdzn.bot.xorbot.command.commands.BotInformationCommand;
import io.github.zrdzn.bot.xorbot.command.commands.HelpCommand;
import io.github.zrdzn.bot.xorbot.command.commands.MoneyCommand;
import io.github.zrdzn.bot.xorbot.command.commands.SlowmodeCommand;
import io.github.zrdzn.bot.xorbot.listener.CommandListener;
import io.github.zrdzn.bot.xorbot.user.UserRepository;
import io.github.zrdzn.bot.xorbot.user.UserService;
import io.github.zrdzn.bot.xorbot.user.XorUserService;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;

import javax.security.auth.login.LoginException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class XorBot {

    public static void main(String[] args) throws LoginException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Token was not provided.");
        }

        boolean testBuild = args.length == 2 && args[1].equalsIgnoreCase("dev");

        JDABuilder jdaBuilder = JDABuilder.createDefault(args[0]);

        XorBot app = new XorBot();
        app.run(jdaBuilder, testBuild);
    }

    public void run(JDABuilder jdaBuilder, boolean testBuild) throws LoginException {
        BasicConfigurator.configure();
        Logger logger = JDALogger.getLog("DISCORD-BOT");

        String databaseConfig = testBuild ? "test_database" : "database";
        HikariDataSource dataSource = new HikariDataSource(new HikariConfig("/" + databaseConfig + ".properties"));

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
            logger.error("Could not create-if-not-exists table 'users'. Something went wrong.", exception);
            return;
        }

        CommandRegistry commandRegistry = new CommandRegistry();

        UserService userService = new XorUserService(new UserRepository(dataSource, logger));

        logger.info("Registering default commands...");
        commandRegistry.register(new HelpCommand(commandRegistry));
        commandRegistry.register(new MoneyCommand(userService));
        commandRegistry.register(new SlowmodeCommand());
        commandRegistry.register(new BotInformationCommand(commandRegistry));
        logger.info("Registered all default commands.");

        logger.info("Registering listeners...");
        jdaBuilder.addEventListeners(new CommandListener(commandRegistry, testBuild)).build();
        logger.info("Registered all listeners. JDA Built, ready to go.");
    }

}
