package org.plazmamc.plazma.configurations;

import io.papermc.paper.configuration.Configurations;
import joptsimple.OptionSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.File;

import static io.papermc.paper.configuration.PaperConfigurations.CONFIG_DIR;

@DefaultQualifier(NonNull.class)
public class PlazmaConfigurations extends Configurations<GlobalConfiguration, WorldConfigurations> {

    private static final String GLOBAL = "plazma-global.yml";
    private static final String WORLD_DEFAULTS = "plazma-world-defaults.yml";
    private static final String WORLD_SPECIFIC = "plazma-world.yml";
    public static final String CONFIG_DIR_OPTION = "plazma-settings-directory";

    private static final String HEADER_START = """
            #### ENGLISH ####
            This is the %s configuration file for Plazma.
            As you can see, there's a lot to configure. Some options may impact
            gameplay, so use with caution, and make sure you know what each option
            does before configuring.
            
            A description of the options can be found on the official wiki,
            If you need help with the configuration or have any questions related to Plazma,
            join us in our Discord for Plazma, or create issues on our GitHub repository.
            
            %s
            
            #### 한국어 ####
            본 파일은 Plazma의 %s 구성 파일입니다.
            보시다시피, 굉장히 많은 설정이 있습니다. 일부 설정은 게임 플레이에 영향을 줄 수 있습니다.
            따라서 주의해서 사용하시고, 각 설정이 서버에 어떠한 작용을 하는지 숙지하고 사용하시기 바랍니다.
            
            구성에 대한 설명은 공식 위키에서 찾을 수 있으며,
            만약 설정에 도움이 필요하거나, Plazma에 대해 질문이 있으시다면,
            공식 Discord 서버에 접속하거나, GitHub 저장소에 이슈를 생성해주시기 바랍니다.
            
            %s
            
            GitHub: https://github.com/PlazmaMC/Plazma
            Wiki: https://docs.plazmamc.org/
            Discord: https://plazmamc.org/discord
            """;

    private static final String GLOBAL_HEADER = HEADER_START.formatted(
        "global", "World options can be set in the %s file.".formatted(WORLD_DEFAULTS),
        "전역", "월드별 설정은 %s 파일에서 설정할 수 있습니다.".formatted(WORLD_DEFAULTS)
    );

    public PlazmaConfigurations(final OptionSet options) {
        super(((File) options.valueOf(CONFIG_DIR_OPTION)).toPath());
    }

    @Override
    protected Class<GlobalConfiguration> globalConfigClass() {
        return GlobalConfiguration.class;
    }

    @Override
    protected Class<WorldConfigurations> worldConfigClass() {
        return WorldConfigurations.class;
    }

    @Override
    protected int globalConfigVersion() {
        return GlobalConfiguration.VERSION;
    }

    @Override
    protected int worldConfigVersion() {
        return WorldConfigurations.VERSION;
    }

    @Override
    protected String globalConfigFileName() {
        return GLOBAL;
    }

    @Override
    protected String worldDefaultsFileName() {
        return WORLD_DEFAULTS;
    }

    @Override
    protected String worldConfigFileName() {
        return WORLD_SPECIFIC;
    }

    @Override
    protected String worldDefaultHeader() {
        return HEADER_START.formatted(
            "world default", """
            World-specific settings can be set in the %FILENAME%
            file within each world folder, and the same settings apply to all
            worlds unless they are overwritten through the world-specific settings file.
            """,
            "월드 기본", """
            월드별 설정은 각 월드 폴더 내 %FILENAME% 파일에서 설정할 수
            있으며, 월드별 설정을 통해 값을 덮어쓰지 않는 한, 모든 월드에 동일한 설정이 적용됩니다.
            """
        ).replace("%FILENAME%", WORLD_SPECIFIC);
    }

    @Override
    protected String buildWorldHeader(final ContextMap contextMap) {
        return """
            #### ENGLISH ####
            This is world-specific Plazma configuration file for the world %WORLD% (%KEY%).
            This file may start empty, but can be filled with options to override
            world default configuration.
            Some options may impact gameplay, so use with caution,
            and make sure you know what each option does before configuring.
            
            A description of the options can be found on the official wiki,
            If you need help with the configuration or have any questions related to Plazma,
            join us in our Discord for Plazma, or create issues on our GitHub repository.
            
            #### 한국어 ####
            본 파일은 %WORLD% (%KEY%) 월드 전용 Plazma 구성 파일입니다.
            이 파일은 초기에는 비어있지만, 월드 기본 설정을 덮어쓰기 위해 설정을 추가할 수 있습니다.
            일부 설정은 게임 플레이에 영향을 줄 수 있으므로, 주의해서 사용하시고,
            각 설정이 서버에 어떠한 작용을 하는지 숙지하고 사용하시기 바랍니다.
            
            구성에 대한 설명은 공식 위키에서 찾을 수 있으며,
            만약 설정에 도움이 필요하거나, Plazma에 대해 질문이 있으시다면,
            공식 Discord 서버에 접속하거나, GitHub 레포지토리에 이슈를 생성해주시기 바랍니다.
            
            World Default Config Directory: %s/%s
            GitHub: https://github.com/PlazmaMC/Plazma
            Document: https://docs.plazmamc.org/
            Discord: https://plazmamc.org/discord
            """
            .replace("WORLD", contextMap.require(WORLD_NAME))
            .replace("KEY", contextMap.require(WORLD_KEY).toString())
            .formatted(CONFIG_DIR, WORLD_DEFAULTS);
    }

    @Override
    protected NodePath[] removedGlobalPaths() {
        return RemovedConfigurations.GLOBAL_PATHS;
    }

    @Override
    protected NodePath[] removedWorldPaths() {
        return RemovedConfigurations.WORLD_PATHS;
    }

    @Override
    protected GlobalConfiguration getGlobalConfiguration() {
        return GlobalConfiguration.get();
    }

    @Override
    protected YamlConfigurationLoader.Builder createGlobalLoaderBuilder(final RegistryAccess access) {
        return super.createGlobalLoaderBuilder(access).defaultOptions(options -> options
            .header(GLOBAL_HEADER)
        );
    }

    @Override
    protected WorldConfigurations getWorldConfiguration(final ServerLevel level) {
        return level.plazma;
    }

    @Override
    protected WorldConfigurations createWorldConfigInstance(final ContextMap contextMap) {
        return new WorldConfigurations(contextMap.require(WORLD_KEY));
    }

}
