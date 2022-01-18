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

public class XorEconomyService implements EconomyService {

    private final EconomyRepository economyRepository;

    public XorEconomyService(EconomyRepository economyRepository) {
        this.economyRepository = economyRepository;
    }

    @Override
    public CompletableFuture<Long> getMoney(long discordId) {
        return CompletableFuture.supplyAsync(() -> this.economyRepository.getMoneyByDiscordId(discordId));
    }

    @Override
    public CompletableFuture<Long> setMoney(long discordId, long amount) {
        return CompletableFuture.supplyAsync(() -> this.economyRepository.setMoneyByDiscordId(discordId, amount, EconomyRepository.MoneyOperation.SET));
    }

    @Override
    public CompletableFuture<Long> addMoney(long discordId, long amount) {
        return CompletableFuture.supplyAsync(() -> this.economyRepository.setMoneyByDiscordId(discordId, amount, EconomyRepository.MoneyOperation.ADD));
    }

    @Override
    public CompletableFuture<Long> subtractMoney(long discordId, long amount) {
        return CompletableFuture.supplyAsync(() -> this.economyRepository.setMoneyByDiscordId(discordId, amount, EconomyRepository.MoneyOperation.SUBTRACT));
    }

}
