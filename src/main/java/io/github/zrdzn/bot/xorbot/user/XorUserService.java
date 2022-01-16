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

public class XorUserService implements UserService {

    private final UserRepository userRepository;

    public XorUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CompletableFuture<User> createUser(long discordId, String username, long balance) {
        return CompletableFuture.supplyAsync(() -> this.userRepository.save(discordId, username, balance));
    }

    @Override
    public CompletableFuture<Void> removeUser(long discordId) {
        return CompletableFuture.runAsync(() -> this.userRepository.deleteByDiscordId(discordId));
    }

    @Override
    public CompletableFuture<Optional<User>> getUser(long discordId) {
        return CompletableFuture.supplyAsync(() -> this.userRepository.findByDiscordId(discordId));
    }

    @Override
    public CompletableFuture<Boolean> userExists(long discordId) {
        return CompletableFuture.supplyAsync(() -> this.userRepository.existsByDiscordId(discordId));
    }

    @Override
    public CompletableFuture<Long> getMoney(long discordId) {
        return CompletableFuture.supplyAsync(() -> this.userRepository.getMoneyByDiscordId(discordId));
    }

    @Override
    public CompletableFuture<Long> setMoney(long discordId, long amount) {
        return CompletableFuture.supplyAsync(() -> this.userRepository.setMoneyByDiscordId(discordId, amount, UserRepository.MoneyOperation.SET));
    }

    @Override
    public CompletableFuture<Long> addMoney(long discordId, long amount) {
        return CompletableFuture.supplyAsync(() -> this.userRepository.setMoneyByDiscordId(discordId, amount, UserRepository.MoneyOperation.ADD));
    }

    @Override
    public CompletableFuture<Long> subtractMoney(long discordId, long amount) {
        return CompletableFuture.supplyAsync(() -> this.userRepository.setMoneyByDiscordId(discordId, amount, UserRepository.MoneyOperation.SUBTRACT));
    }

}
