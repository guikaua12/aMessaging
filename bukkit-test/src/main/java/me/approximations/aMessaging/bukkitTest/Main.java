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

package me.approximations.aMessaging.bukkitTest;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.approximations.aMessaging.bungee.channel.BungeeChannel;
import me.approximations.aMessaging.bungee.input.args.BungeeInputArgs;
import me.approximations.aMessaging.bungee.message.actions.ConnectOtherAction;
import me.approximations.aMessaging.bungee.message.actions.ForwardAction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Arrays;

public class Main extends JavaPlugin implements CommandExecutor {
    public static Main INSTANCE;
    private BungeeChannel bungeeChannel;

    @Override
    public void onEnable() {
        INSTANCE = this;

        getCommand("aMessagingTest").setExecutor(this);

        bungeeChannel = new BungeeChannel(this);
        bungeeChannel.init();

        bungeeChannel.subscribe("someChannel", args -> {
            final ByteArrayDataInput dataInput = args.getDataInput();

            // TODO: implement a better api for this
            final short len = dataInput.readShort();
            final byte[] msgbytes = new byte[len];
            dataInput.readFully(msgbytes);

            final DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));

            try {
                System.out.println(msgin.readInt());
                System.out.println(msgin.readInt());
                System.out.println(msgin.readUTF());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void onDisable() {
        bungeeChannel.unregisterChannel();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("aMessagingTest")) return false;

        /*bungeeChannel.sendMessage(
                BungeeInputArgs.builder()
                        .messageAction(new ConnectOtherAction(playerName, serverName))
                        .build()
        );*/

        bungeeChannel.sendMessage(
                BungeeInputArgs.builder()
                        .messageAction(new ForwardAction(ForwardAction.SERVER_ALL, "someChannel", Arrays.asList(1337, 69, "pica")))
                        .build()
        );

        return true;
    }
}
