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
package io.github.zrdzn.bot.xorbot.log;

public enum LogAction {

    MEMBER_JOIN("Member joined"),
    MEMBER_LEAVE("Member leaved"),
    MESSAGE_DELETE("Message deleted"),
    MESSAGE_EDIT("Message edited"),
    MEMBER_WARN_ADD("Member warned"),
    MEMBER_WARN_REMOVE("Member unwarned"),
    MEMBER_MUTE("Member muted"),
    MEMBER_UNMUTE("Member unmuted"),
    MEMBER_KICK("Member kicked"),
    MEMBER_BAN("Member banned"),
    MEMBER_UNBAN("Member unbanned");

    private final String description;

    LogAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}