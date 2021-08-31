package io.github.enixor.bot.xorbot.listener;

import io.github.enixor.bot.xorbot.command.Command;
import io.github.enixor.bot.xorbot.command.CommandRegistry;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MessageReceivedListener extends ListenerAdapter {

    private final CommandRegistry commandRegistry;

    public MessageReceivedListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String rawContent = event.getMessage().getContentRaw();

        if (!rawContent.startsWith("!")) {
            return;
        }

        List<String> optionList = new ArrayList<>(Arrays.asList(rawContent.split(" ")));

        String commandName = optionList.get(0).substring(1);
        if (commandName.isBlank()) {
            return;
        }

        Map<String, Command> commandMap = this.commandRegistry.getCommandMap();
        if (!commandMap.containsKey(commandName)) {
            event.getChannel().sendMessage("Provided command does not exist in the command registry. Check !help for the command list.").queue();
            return;
        }

        optionList.remove(0);

        CompletableFuture.runAsync(() -> commandMap.get(commandName).execute(event, optionList));
    }
}
