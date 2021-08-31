package io.github.enixor.bot.xorbot.command;

import io.github.enixor.bot.xorbot.user.UserService;
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
        return Optional.of("!money [get/set/add/subtract] [<amount>] [<mention>]");
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
