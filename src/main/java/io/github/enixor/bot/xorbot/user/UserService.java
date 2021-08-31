package io.github.enixor.bot.xorbot.user;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    /**
     * Creates new User and adds him to the database.
     *
     * @param discordId an discord id of the user
     * @param username an username of the user
     * @param balance an initial balance of the user
     *
     * @return newly created user
     */
    CompletableFuture<User> createUser(long discordId, String username, long balance);

    /**
     * Removes User by discord id from the database.
     *
     * @param discordId an discord id of the existing user
     */
    CompletableFuture<Void> removeUser(long discordId);

    /**
     * Gets the optional User by discord id from the database.
     *
     * @param discordId an discord id of the searched user
     *
     * @return optional user
     */
    CompletableFuture<Optional<User>> getUser(long discordId);

    /**
     * Gets the boolean if User exists by discord id in the database or not.
     *
     * @param discordId an discord id of the searched user
     *
     * @return true if user exists
     */
    CompletableFuture<Boolean> userExists(long discordId);

    /**
     * Gets user money by discord id in the database.
     *
     * @param discordId an discord id of the searched user
     *
     * @return current account balance
     */
    CompletableFuture<Long> getMoney(long discordId);

    /**
     * Sets money for user by discord id in the database.
     *
     * @param discordId an discord id of the searched user
     * @param amount an amount of money that should be added to the user
     *
     * @return new account balance, if -1 it means something went wrong
     */
    CompletableFuture<Long> setMoney(long discordId, long amount);

    /**
     * Adds money to user by discord id in the database.
     *
     * @param discordId an discord id of the searched user
     * @param amount an amount of money that should be added to the user
     *
     * @return new account balance, if -1 it means something went wrong
     */
    CompletableFuture<Long> addMoney(long discordId, long amount);

    /**
     * Subtracts money from user by discord id in the database.
     *
     * @param discordId an discord id of the searched user
     * @param amount an amount of money that should be subtract from the user
     *
     * @return new account balance, if -1 it means something went wrong
     */
    CompletableFuture<Long> subtractMoney(long discordId, long amount);

}
