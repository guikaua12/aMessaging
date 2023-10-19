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

package me.approximations.aMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface Channel<I extends MessageInputArgs, C extends MessageCallbackArgs> {
    /**
     * Initializes the channel.
     */
    void init();

    /**
     * Retrieves the name.
     *
     * @return the name
     */

    @NotNull String getName();

    /**
     * Subscribes to a subChannel and registers a listener for receiving messages.
     *
     * @param subChannel the name of the subChannel to subscribe to
     * @param listener   the callback function to be invoked when a message is received
     */

    void subscribe(@NotNull String subChannel, @NotNull MessageCallback<C> listener);

    /**
     * Sends a message with the given arguments.
     *
     * @param args the arguments for the message
     */

    void sendMessage(@NotNull I args);

    /**
     * Sends a request-response message.
     *
     * @param args          the arguments for the message
     * @param inputClazz    the class of the input type
     * @param responseClazz the class of the response type
     * @return a CompletableFuture representing the response
     */
    <K, R> CompletableFuture<R> sendReqRespMessage(@NotNull I args, @NotNull Class<? extends K> inputClazz, @NotNull Class<? extends R> responseClazz);
}
