package org.plazmamc.plazma.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.plazmamc.plazma.NullConstants;
import java.util.Objects;

public class PlazmaCommand {

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        final Command versionCommand = Objects.requireNonNull(MinecraftServer.getServer().server.getCommandMap().getCommand("version"));

        dispatcher.register(Commands.literal("plazma")
            .requires(src -> src.hasPermission(4, "bukkit.command.plazma"))
            .then(Commands.literal("reload")
                .requires(src -> src.hasPermission(4, "bukkit.command.plazma.reload"))
                .executes(ctx -> reload(ctx.getSource().getSender()))
            )
            .then(Commands.literal("version")
                .executes(ctx -> versionCommand.execute(ctx.getSource().getSender(), "plazma", NullConstants.STRINGS) ? 1 : 0)
            )
        );
    }

    private static int reload(final CommandSender sender) {
        Command.broadcastCommandMessage(sender, Component.text("Please note that this command is not supported and may cause issues.", NamedTextColor.RED));
        Command.broadcastCommandMessage(sender, Component.text("If you encounter any issues please use the /stop command to restart your server.", NamedTextColor.RED));

        final MinecraftServer server = ((CraftServer) sender.getServer()).getServer();
        server.plazmaConfigurations.reloadConfigs(server);
        server.server.reloadCount++;

        Command.broadcastCommandMessage(sender, Component.text("Successfully reloaded Plazma configuration files.", NamedTextColor.GREEN));
        return 1;
    }

}
