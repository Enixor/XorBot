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
package io.github.zrdzn.bot.xorbot.log;

import io.github.zrdzn.bot.xorbot.embed.EmbedHelper;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LogListener extends ListenerAdapter {

    private final long logChannelId;

    public LogListener(long logChannelId) {
        this.logChannelId = logChannelId;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        MessageChannel logChannel = event.getGuild().getTextChannelById(this.logChannelId);
        if (logChannel == null) {
            return;
        }

        logChannel.sendMessageEmbeds(EmbedHelper.log(LogAction.MEMBER_JOIN)
            .addField("Member", EmbedHelper.formatUser(event.getUser()), false)
            .build()).queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        MessageChannel logChannel = event.getGuild().getTextChannelById(this.logChannelId);
        if (logChannel == null) {
            return;
        }

        // TODO Add cancel if not caused by user itself to leave guild (should check user id in audit log)

        logChannel.sendMessageEmbeds(EmbedHelper.log(LogAction.MEMBER_LEAVE)
            .addField("Member", EmbedHelper.formatUser(event.getUser()), false)
            .build()).queue();
    }

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
        MessageChannel logChannel = event.getGuild().getTextChannelById(this.logChannelId);
        if (logChannel == null) {
            return;
        }

        // TODO Need to create cache with user reference and message content
        logChannel.sendMessageEmbeds(EmbedHelper.log(LogAction.MESSAGE_DELETE)
            //.addField("Member", EmbedHelper.formatUser(event.getUser()), false)
            //.addField("Message", message, false)
            .build()).queue();
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        MessageChannel logChannel = event.getGuild().getTextChannelById(this.logChannelId);
        if (logChannel == null) {
            return;
        }

        // TODO Need to create cache with user reference and message content
        logChannel.sendMessageEmbeds(EmbedHelper.log(LogAction.MESSAGE_EDIT)
            .addField("Member", EmbedHelper.formatUser(event.getAuthor()), false)
            //.addField("Old message", oldMessage, false)
            .addField("New message", event.getMessage().getContentRaw(), false)
            .build()).queue();
    }

    // TODO Add custom events and maybe punishment service
    /*
    public void onGuildMemberWarnAdd(@NotNull GuildMemberWarnAddEvent event) {

    }

    public void onGuildMemberWarnRemove(@NotNull GuildMemberWarnAddEvent event) {

    }

    public void onGuildMemberMute(@NotNull GuildMemberMuteEvent event) {

    }

    public void onGuildMemberUnmute(@NotNull GuildMemberUnmuteEvent event) {

    }
    */

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        MessageChannel logChannel = event.getGuild().getTextChannelById(this.logChannelId);
        if (logChannel == null) {
            return;
        }

        event.getGuild().retrieveAuditLogs()
            .type(ActionType.BAN)
            .limit(1)
            .queue(entries -> {
                Optional<AuditLogEntry> entryMaybe = entries.stream()
                    .filter(entry -> entry.getTargetId().equals(event.getUser().getId()))
                    .findFirst();
                if (entryMaybe.isEmpty()) {
                    return;
                }

                AuditLogEntry entry = entryMaybe.get();

                logChannel.sendMessageEmbeds(EmbedHelper.log(LogAction.MEMBER_BAN)
                    .addField("Member", EmbedHelper.formatUser(event.getUser()), false)
                    .addField("Executor", EmbedHelper.formatUser(entry.getUser()), false)
                    .addField("Reason", entry.getReason(), false)
                    .build()).queue();
            });
    }

    @Override
    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        MessageChannel logChannel = event.getGuild().getTextChannelById(this.logChannelId);
        if (logChannel == null) {
            return;
        }

        event.getGuild().retrieveAuditLogs()
            .type(ActionType.UNBAN)
            .limit(1)
            .queue(entries -> {
                Optional<AuditLogEntry> entryMaybe = entries.stream()
                    .filter(entry -> entry.getTargetId().equals(event.getUser().getId()))
                    .findFirst();
                if (entryMaybe.isEmpty()) {
                    return;
                }

                AuditLogEntry entry = entryMaybe.get();

                logChannel.sendMessageEmbeds(EmbedHelper.log(LogAction.MEMBER_UNBAN)
                    .addField("Member", EmbedHelper.formatUser(event.getUser()), false)
                    .addField("Executor", EmbedHelper.formatUser(entry.getUser()), false)
                    .build()).queue();
            });
    }

}
