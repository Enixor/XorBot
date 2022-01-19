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
