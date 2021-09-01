/*
 * Copyright (c) 2021 Enixor
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
