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
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.approximations.aMessaging.bungee.channel.BungeeChannel;
import me.approximations.aMessaging.bungee.input.args.BungeeInputArgs;
import me.approximations.aMessaging.bungee.message.actions.ConnectOtherAction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Main extends JavaPlugin implements CommandExecutor {
    public static Main INSTANCE;
    private BungeeChannel bungeeChannel;

    @Override
    public void onEnable() {
        INSTANCE = this;

        getCommand("aMessagingTest").setExecutor(this);

        bungeeChannel = new BungeeChannel(this);
        bungeeChannel.init();

//        bungeeChannel.subscribe("someChannel", args -> {
//            System.out.println(args.getDataInput().readUTF());
//        });

        bungeeChannel.sendMessage(
                BungeeInputArgs.builder()
                        .messageAction(new ConnectOtherAction("Approximations", "server1"))
                        .build()
        );

//        Bukkit.getScheduler().runTaskLater(this, () -> {
//            ByteArrayDataOutput out = ByteStreams.newDataOutput();
//            out.writeUTF("Forward");
//            out.writeUTF("ALL");
//            out.writeUTF("someChannel");
//
//            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
//            DataOutputStream msgout = new DataOutputStream(msgbytes);
//            try {
//                msgout.writeUTF("Hello world");
//            } catch (IOException exception) {
//                exception.printStackTrace();
//            }
//
//            out.writeShort(msgbytes.toByteArray().length);
//            out.write(msgbytes.toByteArray());
//
//            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
//
//            player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
//
//        }, 40);
    }

    @Override
    public void onDisable() {
        bungeeChannel.unregisterChannel();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("aMessagingTest")) return false;

        if (args.length != 2) return false;

        final String playerName = args[0];
        final String serverName = args[1];

        bungeeChannel.sendMessage(
                BungeeInputArgs.builder()
                        .messageAction(new ConnectOtherAction(playerName, serverName))
                        .build()
        );

        return true;
    }
}
