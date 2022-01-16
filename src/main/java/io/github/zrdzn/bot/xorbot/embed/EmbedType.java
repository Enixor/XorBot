package io.github.zrdzn.bot.xorbot.embed;

import java.awt.Color;

public enum EmbedType {

    INFORMATION(Color.BLUE),
    MODERATION(Color.GREEN),
    ERROR(Color.RED),
    LOG(Color.BLUE);

    private final Color color;

    EmbedType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

}