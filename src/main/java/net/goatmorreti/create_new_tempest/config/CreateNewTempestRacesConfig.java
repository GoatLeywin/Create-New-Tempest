package net.goatmorreti.create_new_tempest.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CreateNewTempestRacesConfig {
    public ForgeConfigSpec.DoubleValue epToMechanicalColossus = null;

    public void CreateNewTempestRacesConfig(ForgeConfigSpec.Builder builder) {
        builder.push("evolutionEPRequirements");
        this.epToMechanicalColossus = builder.comment("The amount of EP needed to evolve into a Mechanical Colossus").defineInRange("epToMechanicalColossus", (double) 4000.0F, (double) 0.0F, (double) 1.0E9F);
    }
}