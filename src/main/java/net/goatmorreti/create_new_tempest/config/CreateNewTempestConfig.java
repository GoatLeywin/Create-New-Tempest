package net.goatmorreti.create_new_tempest.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CreateNewTempestConfig {
    public static final CreateNewTempestConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;
    public final CreateNewTempestRacesConfig racesConfig;

    private CreateNewTempestConfig(ForgeConfigSpec.Builder builder) {
        builder.push("races");
        this.racesConfig = new CreateNewTempestRacesConfig(builder);
        builder.pop();
    }

    static {
        Pair<CreateNewTempestConfig, ForgeConfigSpec> pair = (new ForgeConfigSpec.Builder()).configure(CreateNewTempestConfig::new);
        INSTANCE = (CreateNewTempestConfig)pair.getKey();
        SPEC = (ForgeConfigSpec)pair.getValue();
    }
}
