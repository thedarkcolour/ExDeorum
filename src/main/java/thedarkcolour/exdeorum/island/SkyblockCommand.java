/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.island;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import static net.minecraft.commands.Commands.argument;

public class SkyblockCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("exdeorum_islands")
                .then(admin("create"))
                .then(admin("delete"))
                .then(admin("list"))
                .then(admin("config"))
                .then(admin("info"))
                .then(admin("tp"))
                .then(player("tpr"))
                .then(player("rename")
                        .then(argument("new-name", StringArgumentType.string())
                                // admins can rename other islands, players can only rename their own island
                                .then(argument("old-name", StringArgumentType.string())
                                        .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                                )
                        )
                )
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> admin(String name) {
        return Commands.literal(name).requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> player(String name) {
        return Commands.literal(name);
    }
}
