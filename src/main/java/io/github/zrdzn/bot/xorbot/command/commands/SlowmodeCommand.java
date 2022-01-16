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

import io.github.zrdzn.bot.xorbot.XorBot;
import io.github.zrdzn.bot.xorbot.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.Optional;

public class SlowmodeCommand implements Command {

    @Override
    public String getName() {
        return "slowmode";
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Set custom slow mode on current channel. If no argument specified it will disable slow mode completely.");
    }

    @Override
    public Optional<String> getUsage() {
        return Optional.of(String.format("!%s [time]", this.getName()));
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> optionList) {
        TextChannel channel = event.getTextChannel();

        if (!event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            channel.sendMessageEmbeds(XorBot.NO_PERMISSIONS_EMBED).queue();
            return;
        }

        if (optionList.isEmpty()) {
            this.disableSlowMode(channel);
            return;
        }

        int time;
        try {
            time = Integer.parseInt(optionList.get(0));
        } catch (NumberFormatException exception) {
            channel.sendMessage("You need to provide valid number in seconds.").queue();
            return;
        }

        if (time <= 0) {
            this.disableSlowMode(channel);
            return;
        }

        if (time > TextChannel.MAX_SLOWMODE) {
            time = TextChannel.MAX_SLOWMODE;
        }

        channel.getManager().setSlowmode(time)
            .and(channel.sendMessage(String.format("Slow mode has been set to %d seconds.", time)))
            .queue();
    }

    private void disableSlowMode(TextChannel channel) {
        channel.getManager().setSlowmode(0).queue();
        channel.sendMessage("Slow mode has been disabled.").queue();
    }

}
