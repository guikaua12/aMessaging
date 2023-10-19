/*
 * MIT License
 *
 * Copyright (c) 2023 Guilherme Kauã (Approximations)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.approximations.aMessaging.bungee.message.response.handler;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class GetServersHandler implements MessageResponseHandler<String, List<String>> {
    public static final String SUB_CHANNEL = "GetServers";
    private static final List<String> EMPTY_LIST = Collections.emptyList();

    private final Queue<CompletableFuture<List<String>>> queue = new ConcurrentLinkedQueue<>();

    @Override
    public void handle(@NotNull DataInput in) {
        try {
            final String[] serverList = in.readUTF().split(", ");

            final CompletableFuture<List<String>> future = queue.poll();

            if (future == null) return;

            future.complete(Arrays.asList(serverList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFuture(@Nullable String key, @NotNull CompletableFuture<List<String>> future) {
        queue.add(future);
    }

    @Override
    public @NotNull Class<String> getInputClass() {
        return String.class;
    }

    @Override
    public @NotNull Class<List<String>> getOutputClass() {
        return (Class<List<String>>) EMPTY_LIST.getClass();
    }

}
