package io.github.zrdzn.bot.xorbot.embed;

import io.github.zrdzn.bot.xorbot.log.LogAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.Date;

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

    public static EmbedBuilder log(LogAction logAction) {
        return getEmbed(EmbedType.LOG)
            .addField("Date", new Date(System.currentTimeMillis()).toString(), false)
            .setFooter("Action: " + logAction.getDescription());
    }

    public static EmbedBuilder getEmbed(EmbedType type) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setColor(type.getColor());

        return embedBuilder;
    }

    public static String formatUser(User user) {
        return String.format("%s#%s (%s)", user.getName(), user.getDiscriminator(), user.getId());
    }

}
