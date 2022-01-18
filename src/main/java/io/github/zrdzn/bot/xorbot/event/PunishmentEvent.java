package io.github.zrdzn.bot.xorbot.event;

import net.dv8tion.jda.api.entities.Member;

import java.time.Duration;
import java.util.Optional;

public interface PunishmentEvent {

    Member getTarget();

    Member getExecutor();

    String getReason();

    Optional<Duration> getDuration();

    default String getDurationString() {
        Optional<Duration> durationMaybe = this.getDuration();
        if (durationMaybe.isPresent()) {
            Duration duration = durationMaybe.get();
            return String.format("%d days, %d hours, %d minutes and %d seconds",
                duration.toDays(), duration.toHours(), duration.toMinutes(), duration.getSeconds());
        }

        return "Permanent";
    }

}
