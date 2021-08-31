package io.github.enixor.bot.xorbot.user;

public interface User {

    /**
     * Gets the database primary key id, could be used as
     * unique identifier of the User.
     *
     * @return id of the user
     */
    long getId();

    /**
     * Gets the discord id of the User.
     *
     * @return discord id of the user
     */
    long getDiscordId();

    /**
     * Gets the username of the User.
     *
     * @return username of the user
     */
    String getUsername();

    /**
     * Gets the account balance of the User.
     *
     * @return account balance of the user
     */
    long getBalance();

}
