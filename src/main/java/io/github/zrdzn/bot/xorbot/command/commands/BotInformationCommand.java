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
import io.github.zrdzn.bot.xorbot.command.CommandRegistry;
import io.github.zrdzn.bot.xorbot.embed.EmbedHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Optional;

public class BotInformationCommand implements Command {

    private final CommandRegistry commandRegistry;

    public BotInformationCommand(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public String getName() {
        return "botinfo";
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Shows information about bot.");
    }

    @Override
    public Optional<String> getUsage() {
        return Optional.empty();
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> optionList) {
        EmbedBuilder embedBuilder = EmbedHelper.info(event.getAuthor());

        embedBuilder.addField("Commands amount", String.valueOf(this.commandRegistry.getCommands().size()), false);

        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
    }

}