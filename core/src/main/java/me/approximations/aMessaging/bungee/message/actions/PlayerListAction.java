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

package me.approximations.aMessaging.bungee.message.actions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.approximations.aMessaging.bungee.message.response.handler.MessageResponseHandler;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlayerListAction extends ResponseableMessageAction<String, List<String>> {
    public static final String SUB_CHANNEL = "PlayerList";
    private final String server;

    @Override
    public @NotNull String getSubChannel() {
        return SUB_CHANNEL;
    }

    @Override
    public void writeHead(@NotNull DataOutput out) throws IOException {
        out.writeUTF(SUB_CHANNEL);
        out.writeUTF(server);
    }

    @Override
    public @NotNull CompletableFuture<List<String>> addFuture(MessageResponseHandler<String, List<String>> responseHandler) {
        final CompletableFuture<List<String>> future = new CompletableFuture<>();

        responseHandler.addFuture(server, future);

        return future;
    }
}
