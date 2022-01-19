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

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

class CircularQueue<T> {

    private final Object[] bufferArray;

    private int current = -1;
    private int size;

    public CircularQueue(int bufferSize) {
        this.bufferArray = new Object[bufferSize];
    }

    public synchronized CircularQueue<T> addElement(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }

        this.current = (this.current + 1) % this.bufferArray.length;
        this.bufferArray[this.current] = item;

        if (this.size + 1 > Integer.MAX_VALUE - 1) {
            throw new IllegalArgumentException("Argument cannot be bigger than max value of an integer.");
        }

        this.size = Math.min(this.size + 1, this.bufferArray.length);

        return this;
    }

    @SuppressWarnings("unchecked")
    public synchronized List<T> getElements() {
        Object[] result = new Object[this.getSize()];

        if (!this.isFull()) {
            System.arraycopy(this.bufferArray, 0, result, 0, this.size);
        } else {
            int rightLength = this.bufferArray.length - (this.current + 1);
            int leftLength = this.bufferArray.length - rightLength;

            System.arraycopy(this.bufferArray, this.current + 1, result, 0, rightLength);
            System.arraycopy(this.bufferArray, 0, result, rightLength, leftLength);
        }

        return (List<T>) Arrays.asList(result);
    }

    @SuppressWarnings("unchecked")
    public synchronized T findElement(Predicate<T> filter) {
        for (Object element : this.bufferArray) {
            if (element == null) {
                break;
            }

            if (filter.test((T) element)) {
                return (T) element;
            }
        }

        return null;
    }

    public synchronized int getSize() {
        return this.size;
    }

    public synchronized boolean isFull() {
        return this.bufferArray[Math.min(this.current + 1, this.bufferArray.length - 1)] != null;
    }

}