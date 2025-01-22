package org.plazmamc.plazma.configurations;

import io.papermc.paper.configuration.Configuration;
import io.papermc.paper.configuration.ConfigurationPart;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.configurate.objectmapping.meta.Setting;

public class WorldConfigurations extends ConfigurationPart {

    static final int VERSION = 2;

    private transient final ResourceLocation worldKey;
    WorldConfigurations(ResourceLocation worldKey) {
        this.worldKey = worldKey;
    }

    @Setting(Configuration.VERSION_FIELD)
    int version = VERSION;

}
