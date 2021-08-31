package io.github.enixor.bot.xorbot.user;

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
