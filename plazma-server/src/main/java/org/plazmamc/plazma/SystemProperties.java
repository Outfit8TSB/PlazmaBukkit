package org.plazmamc.plazma;

import static java.lang.Boolean.getBoolean;

public interface SystemProperties {

    boolean NO_OPTIMIZE = getBoolean("Plazma.disableConfigOptimization");

}
