package net.goatmorreti.create_new_tempest.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CreateNewTempestRacesConfig {
    public ForgeConfigSpec.DoubleValue epToMechanicalGiant;
    public ForgeConfigSpec.DoubleValue epToMechanicalColossus;
    public ForgeConfigSpec.DoubleValue epToMechanicalTitan;
    public ForgeConfigSpec.DoubleValue epToMechanicalWarrior;
    public ForgeConfigSpec.DoubleValue epToMechanicalSoldier;
    public ForgeConfigSpec.DoubleValue epToMechanicalCombatant;
    public ForgeConfigSpec.DoubleValue epToMechanicalExpert;
    public ForgeConfigSpec.DoubleValue epToMechanicalNovice;
    public ForgeConfigSpec.DoubleValue epToMechanicalScholar;


    public CreateNewTempestRacesConfig(ForgeConfigSpec.Builder builder) {
        builder.push("evolutionEPRequirements");
        this.epToMechanicalGiant = builder.comment("The amount of EP needed to evolve into a Mechanical Giant").defineInRange("epToMechanicalGiant", (double) 10000.0F, (double) 0.0F, (double) 1.0E9F);
        this.epToMechanicalColossus = builder.comment("The amount of EP needed to evolve into a Mechanical Colossus").defineInRange("epToMechanicalColossus", (double) 20000.0F, (double) 0.0F, (double) 1.0E9F);
        this.epToMechanicalTitan = builder.comment("The amount of EP needed to evolve into a Mechanical Titan").defineInRange("epToMechanicalTitan", (double) 50000.0F, (double) 0.0F, (double) 1.0E9F);
        this.epToMechanicalWarrior = builder.comment("The amount of EP needed to evolve into a Mechanical Warrior").defineInRange("epToMechanicalWarrior", (double) 50000.0F, (double) 0.0F, (double) 1.0E9F);
        this.epToMechanicalSoldier = builder.comment("The amount of EP needed to evolve into a Mechanical Soldier").defineInRange("epToMechanicalSoldier", (double) 20000.0F, (double) 0.0F, (double) 1.0E9F);
        this.epToMechanicalCombatant = builder.comment("The amount of EP needed to evolve into a Mechanical Combatant").defineInRange("epToMechanicalCombatant", (double) 10000.0F, (double) 0.0F, (double) 1.0E9F);
        this.epToMechanicalExpert = builder.comment("The amount of EP needed to evolve into a Mechanical Expert").defineInRange("epToMechanicalExpert", (double) 10000.0F, (double) 0.0F, (double) 1.0E9F);
        this.epToMechanicalNovice = builder.comment("The amount of EP needed to evolve into a Mechanical Novice").defineInRange("epToMechanicalNovice", (double) 20000.0F, (double) 0.0F, (double) 1.0E9F);
        this.epToMechanicalScholar = builder.comment("The amount of EP needed to evolve into a Mechanical Scholar").defineInRange("epToMechanicalScholar", (double) 50000.0F, (double) 0.0F, (double) 1.0E9F);
    }
}