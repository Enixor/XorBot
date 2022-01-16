package io.github.zrdzn.bot.xorbot.embed;

import java.awt.Color;

public enum EmbedType {

    INFORMATION(Color.decode("#4169e1")),
    MODERATION(Color.decode("#32cd32")),
    ERROR(Color.decode("#ff6347")),
    LOG(Color.decode("#4169e1"));

    private final Color color;

    EmbedType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

}