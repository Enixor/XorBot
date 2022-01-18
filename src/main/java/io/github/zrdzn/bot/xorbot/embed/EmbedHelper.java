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
package io.github.zrdzn.bot.xorbot.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.Instant;

public class EmbedHelper {

    public static final MessageEmbed NO_PERMISSIONS_EMBED = error().setDescription("No permissions.").build();
    public static final MessageEmbed NO_MENTIONED_USER = error().setDescription("You need to mention someone that is on this server.").build();

    public static EmbedBuilder info() {
        return getEmbed(EmbedType.INFORMATION);
    }

    public static EmbedBuilder moderation() {
        return getEmbed(EmbedType.MODERATION);
    }

    public static EmbedBuilder error() {
        return getEmbed(EmbedType.ERROR);
    }

    public static EmbedBuilder log() {
        return getEmbed(EmbedType.LOG);
    }

    public static EmbedBuilder getEmbed(EmbedType type) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setColor(type.getColor());

        return embedBuilder;
    }

}
