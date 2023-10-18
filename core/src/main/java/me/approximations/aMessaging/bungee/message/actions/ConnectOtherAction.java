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
import me.approximations.aMessaging.bungee.message.MessageAction;

import java.io.DataOutput;
import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConnectOtherAction extends MessageAction {
    public static final String SUB_CHANNEL = "ConnectOther";
    private final String playerName;
    private final String serverName;

    @Override
    public void writeHead(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(SUB_CHANNEL);
        dataOutput.writeUTF(playerName);
        dataOutput.writeUTF(serverName);
    }
}
