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
import io.github.zrdzn.bot.xorbot.economy.EconomyService;
import io.github.zrdzn.bot.xorbot.embed.EmbedHelper;
import io.github.zrdzn.bot.xorbot.user.UserService;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MoneyCommand implements Command {

    private final UserService userService;
    private final EconomyService economyService;

    public MoneyCommand(UserService userService, EconomyService economyService) {
        this.userService = userService;
        this.economyService = economyService;
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

        Member member = event.getMember();
        if (optionList.isEmpty()) {
            this.economyService.getMoney(member.getIdLong()).thenAcceptAsync(money ->
                channel.sendMessageEmbeds(EmbedHelper.info()
                    .addField("Account balance", String.valueOf(money), false)
                    .build()).queue());

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
                channel.sendMessageEmbeds(EmbedHelper.NO_MENTIONED_USER).queue();
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

        member = event.getMessage().getMentionedMembers().get(0);
        if (member == null) {
            channel.sendMessageEmbeds(EmbedHelper.NO_MENTIONED_USER).queue();
            return;
        }

        long userId = member.getIdLong();

        this.userService.createUser(userId, member.getUser().getName(), 0L);

        CompletableFuture<Long> updatedAccountBalance = switch (optionList.get(0).toLowerCase(Locale.ROOT)) {
            case "get" -> this.economyService.getMoney(userId);
            case "set" -> this.economyService.setMoney(userId, amount);
            case "add" -> this.economyService.addMoney(userId, amount);
            case "subtract" -> this.economyService.subtractMoney(userId, amount);
            default -> this.economyService.getMoney(event.getAuthor().getIdLong());
        };

        updatedAccountBalance.thenAccept(money ->
            channel.sendMessageEmbeds(EmbedHelper.info(event.getAuthor())
                .addField("Account balance", String.valueOf(money < 0 ? 0L : money), false)
                .build()).queue());
    }

}
