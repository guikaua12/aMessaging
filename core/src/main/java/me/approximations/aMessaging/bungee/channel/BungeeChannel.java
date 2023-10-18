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

package me.approximations.aMessaging.bungee.channel;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.RequiredArgsConstructor;
import me.approximations.aMessaging.Channel;
import me.approximations.aMessaging.MessageCallback;
import me.approximations.aMessaging.MessageListener;
import me.approximations.aMessaging.bungee.callback.args.BungeeCallbackArgs;
import me.approximations.aMessaging.bungee.input.args.BungeeInputArgs;
import me.approximations.aMessaging.bungee.message.actions.MessageAction;
import me.approximations.aMessaging.bungee.message.actions.ResponseableMessageAction;
import me.approximations.aMessaging.bungee.message.response.handler.MessageResponseHandler;
import me.approximations.aMessaging.bungee.message.response.handler.PlayerCountHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class BungeeChannel implements Channel<BungeeInputArgs, BungeeCallbackArgs>, PluginMessageListener {
    public static final String BUNGEE_CHANNEL = "BungeeCord";
    private final List<MessageListener<BungeeCallbackArgs>> listeners = new ArrayList<>();
    private final Map<String, MessageResponseHandler<?, ?>> responseHandlerMap = new HashMap<>();
    private final Plugin plugin;

    @Override
    public void init() {
        registerChannel();

        responseHandlerMap.put(PlayerCountHandler.SUB_CHANNEL, new PlayerCountHandler());
    }

    public void registerChannel() {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, BUNGEE_CHANNEL);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, BUNGEE_CHANNEL, this);
    }

    public void unregisterChannel() {
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, BUNGEE_CHANNEL);
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, BUNGEE_CHANNEL);
    }

    @Override
    public @NotNull String getName() {
        return BUNGEE_CHANNEL;
    }

    @Override
    public void subscribe(String subChannel, MessageCallback<BungeeCallbackArgs> listener) {
        listeners.add(new MessageListener<BungeeCallbackArgs>() {
            @Override
            public String getSubChannel() {
                return subChannel;
            }

            @Override
            public MessageCallback<BungeeCallbackArgs> getCallback() {
                return listener;
            }
        });
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void sendMessage(BungeeInputArgs args) {
        final MessageAction messageAction = args.getMessageAction();

        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        try {
            messageAction.writeHead(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        final DataOutputStream msgout = new DataOutputStream(msgbytes);

        try {
            messageAction.writeBody(msgout);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        Player player = args.getPlayer();
        if (player == null) player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

        if (player != null) {
            player.sendPluginMessage(this.plugin, BUNGEE_CHANNEL, out.toByteArray());
        }
    }

    @Override
    public <K, R> CompletableFuture<R> sendReqRespMessage(BungeeInputArgs args, Class<? extends K> inputClazz, Class<? extends R> responseClazz) {
        if (!(args.getMessageAction() instanceof ResponseableMessageAction))
            throw new IllegalArgumentException("Unsupported message action");

        sendMessage(args);

        final ResponseableMessageAction<K, R> messageAction = (ResponseableMessageAction<K, R>) args.getMessageAction();
        final MessageResponseHandler<K, R> handler = responseHandlerMap.get(messageAction.getSubChannel());

        if (handler == null)
            throw new IllegalStateException("No handler found for subchannel " + args.getMessageAction().getSubChannel());


        return messageAction.addFuture(handler);
    }


    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equals(BUNGEE_CHANNEL)) return;

        final ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        final String subchannel = in.readUTF();

        final MessageResponseHandler<?, ?> handler = responseHandlerMap.get(subchannel);
        if (handler != null) {
            handler.handle(in);
        }

        {
            final BungeeCallbackArgs args = new BungeeCallbackArgs(player, in);

            for (final MessageListener<BungeeCallbackArgs> listener : listeners) {
                if (!listener.getSubChannel().equals(subchannel)) continue;

                final MessageCallback<BungeeCallbackArgs> callback = listener.getCallback();

                callback.handle(args);
            }
        }

    }
}
