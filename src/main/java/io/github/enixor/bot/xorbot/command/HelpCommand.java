package io.github.enixor.bot.xorbot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Optional;

public class HelpCommand implements Command {

    private final CommandRegistry commandRegistry;

    public HelpCommand(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("List all available commands.");
    }

    @Override
    public Optional<String> getUsage() {
        return Optional.empty();
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> optionList) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        this.commandRegistry.getCommandMap().values()
                .forEach(command -> embedBuilder.addField(command.getName(), command.getDescription().orElse("<None>"), false));

        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

}