/**
 * This file is part of CommandLogger, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 Helion3 http://helion3.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.helion3.commandlogger;

import java.util.Optional;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.inject.Inject;

@Plugin(id = "commandlogger", name = "commandlogger", version = "1.0")
final public class CommandLogger {
    @Inject
    private Game game;

    @Listener
    public void onSendCommand(final SendCommandEvent event) {
        StringBuilder builder = new StringBuilder();

        Optional<Player> optionalPlayer = event.getCause().first(Player.class);
        if (optionalPlayer.isPresent()) {
            builder.append(optionalPlayer.get().getName());

            Location<World> loc = optionalPlayer.get().getLocation();
            builder.append(String.format(" (%s @ %d %d %d) ", loc.getExtent().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        }
        else if(event.getCause().first(ConsoleSource.class).isPresent()) {
            builder.append("console");
        }
        else if(event.getCause().first(CommandBlockSource.class).isPresent()) {
            builder.append("command block");
        }

        builder.append(": /").append(event.getCommand()).append(" ").append(event.getArguments());

        game.getServer().getConsole().sendMessage(Text.of(builder.toString()));
    }
}
