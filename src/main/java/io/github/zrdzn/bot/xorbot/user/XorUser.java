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

public class XorUser implements User {

    private long id;
    private long discordId;
    private String username;
    private long balance;

    private XorUser() {
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public long getDiscordId() {
        return this.discordId;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public long getBalance() {
        return this.balance;
    }

    public static class Builder {

        private long id;
        private long discordId;
        private String username;
        private long balance;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder discordId(long discordId) {
            this.discordId = discordId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder balance(long balance) {
            this.balance = balance;
            return this;
        }

        public XorUser build() {
            if (this.id == 0L) {
                throw new IllegalStateException("User id must be above 0.");
            }

            if (String.valueOf(this.discordId).length() < 17) {
                throw new IllegalStateException("Discord id must be above 17 digit length.");
            }

            if (this.username == null) {
                throw new IllegalStateException("Username cannot be null.");
            }

            if (this.balance < 0L) {
                throw new IllegalStateException("Balance must be above 0.");
            }

            XorUser user = new XorUser();

            user.id = this.id;
            user.discordId = this.discordId;
            user.username = this.username;
            user.balance = this.balance;

            return user;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

}
