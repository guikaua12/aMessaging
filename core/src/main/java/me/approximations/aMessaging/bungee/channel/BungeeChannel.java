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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BungeeChannel implements Channel<BungeeInputArgs, BungeeCallbackArgs>, PluginMessageListener {
    public static final String BUNGEE_CHANNEL = "BungeeCord";
    private final List<MessageListener<BungeeCallbackArgs>> listeners = new ArrayList<>();
    private final Plugin plugin;

    @Override
    public void init() {
        registerChannel();
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

    @Override
    public void sendMessage(String subChannel, BungeeInputArgs args) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF(subChannel);

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            // TODO: implement this
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        Player player = args.getPlayer();

        player.sendPluginMessage(this.plugin, BUNGEE_CHANNEL, out.toByteArray());
    }


    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equals(BUNGEE_CHANNEL)) return;

        final ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        final String subchannel = in.readUTF();

        for (final MessageListener<BungeeCallbackArgs> listener : listeners) {
            if (!listener.getSubChannel().equals(subchannel)) continue;

            final MessageCallback<BungeeCallbackArgs> callback = listener.getCallback();

            final BungeeCallbackArgs args = new BungeeCallbackArgs(player, in);
            callback.handle(args);
        }

    }
}
