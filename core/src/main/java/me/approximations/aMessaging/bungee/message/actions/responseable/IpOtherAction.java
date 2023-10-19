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

package me.approximations.aMessaging.bungee.message.actions.responseable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.approximations.aMessaging.bungee.message.actions.ResponseableMessageAction;
import me.approximations.aMessaging.bungee.message.response.handler.MessageResponseHandler;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

@EqualsAndHashCode(callSuper = true)
@Data
public class IpOtherAction extends ResponseableMessageAction<String, InetSocketAddress> {
    public static final String SUB_CHANNEL = "IPOther";
    private final String playerName;

    @Override
    public @NotNull String getSubChannel() {
        return SUB_CHANNEL;
    }

    @Override
    public void writeHead(@NotNull DataOutput out) throws IOException {
        out.writeUTF(SUB_CHANNEL);
        out.writeUTF(playerName);
    }

    @Override
    public @NotNull CompletableFuture<InetSocketAddress> addFuture(MessageResponseHandler<String, InetSocketAddress> responseHandler) {
        final CompletableFuture<InetSocketAddress> future = new CompletableFuture<>();

        responseHandler.addFuture(playerName, future);

        return future;
    }
}
