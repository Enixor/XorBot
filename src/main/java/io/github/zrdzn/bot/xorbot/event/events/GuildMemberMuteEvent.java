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
