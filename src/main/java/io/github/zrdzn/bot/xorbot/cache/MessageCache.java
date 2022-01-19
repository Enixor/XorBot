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
package io.github.zrdzn.bot.xorbot.cache;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MessageCache {

    private final CircularQueue<Message> messages;

    public MessageCache(int capacity) {
        this.messages = new CircularQueue<>(capacity);
    }

    public void store(Message message) {
        this.messages.addElement(message);
    }

    public boolean contains(Message message) {
        return this.find(storedMessage -> storedMessage.getId().equals(message.getId())).isPresent();
    }

    public Optional<Message> find(Predicate<Message> filter) {
        return Optional.ofNullable(this.messages.findElement(filter));
    }

    public List<? extends Message> getMessages() {
        return this.messages.getElements();
    }

}
