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
package io.github.zrdzn.bot.xorbot.command.commands;

import io.github.zrdzn.bot.xorbot.command.Command;
import io.github.zrdzn.bot.xorbot.user.UserService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MoneyCommand implements Command {

    private final UserService userService;

    public MoneyCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return "money";
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Get the account balance of the specified user.");
    }

    @Override
    public Optional<String> getUsage() {
        return Optional.of(String.format("!%s [get/set/add/subtract] [<amount>] [<mention>]", this.getName()));
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> optionList) {
        TextChannel channel = event.getTextChannel();

        User user;
        if (optionList.isEmpty()) {
            user = event.getAuthor();

            this.userService.getMoney(user.getIdLong()).thenAcceptAsync(money ->
                    channel.sendMessageEmbeds(new EmbedBuilder().addField("Account balance", String.valueOf(money), false).build()).queue());

            return;
        }

        String usage = this.getUsage().orElse("No usage specified");

        if (optionList.size() == 1) {
            channel.sendMessage(usage).queue();
            return;
        }

        long amount = 0L;
        if (!optionList.get(0).equalsIgnoreCase("get")) {
            if (optionList.size() == 2) {
                channel.sendMessage("You need to mention someone.").queue();
                return;
            }

            try {
                amount = Long.parseLong(optionList.get(1));
                if (amount < 0) {
                    channel.sendMessage("Amount must be above 0.").queue();
                    return;
                }
            } catch (NumberFormatException exception) {
                channel.sendMessage("You need to provide valid amount.").queue();
                return;
            }
        }

        user = event.getMessage().getMentionedUsers().get(0);
        if (user == null) {
            channel.sendMessage("You need to mention someone that is on this server.").queue();
            return;
        }

        long userId = user.getIdLong();

        this.userService.createUser(userId, user.getName(), 0L);

        CompletableFuture<Long> updatedAccountBalance = switch (optionList.get(0).toLowerCase(Locale.ROOT)) {
            case "get" -> this.userService.getMoney(userId);
            case "set" -> this.userService.setMoney(userId, amount);
            case "add" -> this.userService.addMoney(userId, amount);
            case "subtract" -> this.userService.subtractMoney(userId, amount);
            default -> this.userService.getMoney(event.getAuthor().getIdLong());
        };

        updatedAccountBalance.thenAccept(money ->
                channel.sendMessageEmbeds(new EmbedBuilder().addField("Account balance", String.valueOf(money < 0 ? 0L : money), false).build()).queue());
    }

}
