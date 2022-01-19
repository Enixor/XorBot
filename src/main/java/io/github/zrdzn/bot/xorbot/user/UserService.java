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
package io.github.zrdzn.bot.xorbot.user;

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

}
