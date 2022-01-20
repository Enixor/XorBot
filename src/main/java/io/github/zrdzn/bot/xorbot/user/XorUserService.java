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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class XorUserService implements UserService {

    private final List<User> users;
    private final UserRepository userRepository;

    public XorUserService(UserRepository userRepository) {
        this.users = new ArrayList<>();
        this.userRepository = userRepository;
    }

    public void loadUsers() {
        this.users.addAll(this.userRepository.list());
    }

    @Override
    public CompletableFuture<Optional<User>> createUser(long discordId, String username, long balance) {
        return CompletableFuture.supplyAsync(() -> {
            if (this.userRepository.save(discordId, username, balance)) {
                return Optional.empty();
            }

            this.userRepository.findByDiscordId(discordId).ifPresent(this.users::add);

            return this.users.stream()
                .filter(user -> user.getId() == discordId)
                .findAny();
        });
    }

    @Override
    public CompletableFuture<Void> removeUser(long discordId) {
        return CompletableFuture.runAsync(() -> {
            if (this.userRepository.deleteByDiscordId(discordId)) {
                return;
            }

            this.users.removeIf(user -> user.getId() == discordId);
        });
    }

    @Override
    public CompletableFuture<Optional<User>> getUser(long discordId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<User> userMaybe = this.users.stream()
                .filter(user -> user.getId() == discordId)
                .findAny();
            if (userMaybe.isPresent()) {
                return userMaybe;
            }

            return this.userRepository.findByDiscordId(discordId);
        });
    }

    @Override
    public CompletableFuture<Boolean> userExists(long discordId) {
        return CompletableFuture.supplyAsync(() -> {
            if (this.users.stream().noneMatch(user -> user.getId() == discordId)) {
                Optional<User> userMaybe = this.userRepository.findByDiscordId(discordId);
                if (userMaybe.isPresent()) {
                    this.users.add(userMaybe.get());
                    return true;
                }
            }

            return false;
        });
    }

    public List<User> getCachedUsers() {
        return Collections.unmodifiableList(this.users);
    }

}
