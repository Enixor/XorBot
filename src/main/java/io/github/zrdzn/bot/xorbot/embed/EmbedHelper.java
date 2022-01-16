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
