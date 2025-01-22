package org.plazmamc.plazma.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public interface Commands {

    static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        PlazmaCommand.register(dispatcher);
    }

}
