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
package io.github.zrdzn.bot.xorbot.economy;

import java.util.concurrent.CompletableFuture;

public interface EconomyService {

    /**
     * Gets user money by discord id in the database.
     *
     * @param discordId a discord id of the searched user
     *
     * @return current account balance
     */
    CompletableFuture<Long> getMoney(long discordId);

    /**
     * Sets money for user by discord id in the database.
     *
     * @param discordId a discord id of the searched user
     * @param amount an amount of money that should be added to the user
     *
     * @return new account balance, if -1 it means something went wrong
     */
    CompletableFuture<Long> setMoney(long discordId, long amount);

    /**
     * Adds money to user by discord id in the database.
     *
     * @param discordId a discord id of the searched user
     * @param amount an amount of money that should be added to the user
     *
     * @return new account balance, if -1 it means something went wrong
     */
    CompletableFuture<Long> addMoney(long discordId, long amount);

    /**
     * Subtracts money from user by discord id in the database.
     *
     * @param discordId a discord id of the searched user
     * @param amount an amount of money that should be subtracted from the user
     *
     * @return new account balance, if -1 it means something went wrong
     */
    CompletableFuture<Long> subtractMoney(long discordId, long amount);

}
