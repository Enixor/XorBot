package io.github.enixor.bot.xorbot.user;

public class UserCreationException extends RuntimeException {

    public UserCreationException(long discordId, String discordUsername, String message) {
        super("[" + discordUsername + "-" + discordId + "] Could not create the user: " + message);
    }

}
