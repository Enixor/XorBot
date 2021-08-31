package io.github.enixor.bot.xorbot.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Optional;

// TODO Create AbstractCommand that will implement this interface.
public interface Command {

    /**
     * Gets the name of the Command.
     * Whatever is the name, it will trigger associated Command
     * if typed right after the global command prefix.
     *
     * @return name of the command
     */
    String getName();

    /**
     * Gets the optional description of the Command.
     *
     * @return optional description of the command
     */
    Optional<String> getDescription();

    /**
     * Gets the optional usage of the Command.
     *
     * @return optional usage of the command
     */
    Optional<String> getUsage();

    /**
     * Main logic of the command. Here all things will be done
     * after triggering command in the chat. Options in the
     * signature method are everything after the prefix and
     * the Command name.
     *
     * @param event event in which the command was triggered
     * @param optionList optional options of the command
     */
    void execute(MessageReceivedEvent event, List<String> optionList);

}