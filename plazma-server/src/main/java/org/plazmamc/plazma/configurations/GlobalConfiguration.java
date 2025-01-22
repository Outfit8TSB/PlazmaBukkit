package org.plazmamc.plazma.configurations;

import io.papermc.paper.configuration.Configuration;
import io.papermc.paper.configuration.ConfigurationPart;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import java.util.Objects;

public class GlobalConfiguration extends ConfigurationPart {

    private static @Nullable GlobalConfiguration INSTANCE;
    static final int VERSION = 2;

    @Contract(pure = true)
    public static @NonNull GlobalConfiguration get() {
        return Objects.requireNonNull(INSTANCE);
    }

    static void set(final @NonNull GlobalConfiguration instance) {
        if (INSTANCE != null) throw new IllegalStateException("GlobalConfiguration already initialized!");
        INSTANCE = instance;
    }

    @Setting(Configuration.VERSION_FIELD)
    int version = VERSION;

}
