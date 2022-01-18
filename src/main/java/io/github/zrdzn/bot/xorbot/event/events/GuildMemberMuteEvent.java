package io.github.zrdzn.bot.xorbot.event.events;

import io.github.zrdzn.bot.xorbot.event.PunishmentEvent;
import net.dv8tion.jda.api.entities.Member;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

public class GuildMemberMuteEvent implements PunishmentEvent {

    private final Member target;
    private final Member executor;
    private final String reason;
    private final Duration duration;

    public GuildMemberMuteEvent(Member target, Member executor, String reason, Duration duration) {
        this.target = Objects.requireNonNull(target, "target");
        this.executor = Objects.requireNonNull(executor, "executor");
        this.reason = reason;
        this.duration = duration;
    }

    @Override
    public Member getTarget() {
        return this.target;
    }

    @Override
    public Member getExecutor() {
        return this.executor;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public Optional<Duration> getDuration() {
        return Optional.ofNullable(this.duration);
    }

}
