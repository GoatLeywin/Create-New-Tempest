package net.goatmorreti.create_new_tempest;

import com.mojang.logging.LogUtils;
import net.goatmorreti.create_new_tempest.config.CreateNewTempestConfig;
import net.goatmorreti.create_new_tempest.item.ModItems;
import net.goatmorreti.create_new_tempest.registry.skill.AllSkills;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.*;
import java.util.Arrays;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateNewTempest.MOD_ID)
public class CreateNewTempest {
    public static final String MOD_ID = "create_new_tempest";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateNewTempest() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        AllSkills.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CreateNewTempestConfig.SPEC, this.getConfigFileName("create-new-tempest-common"));
    }

    public static org.apache.logging.log4j.Logger getLogger() {
        return (org.apache.logging.log4j.Logger) LOGGER;
    }

    private String getConfigFileName(String name) {
        return String.format("%s/%s.toml", "tensura-reincarnated", name);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        if (this.isFirstLaunch()) {
            this.editTOMLFile();
            this.markAsEdited();
            LOGGER.info("Common setup works and TOML file was edited.");
        } else {
            LOGGER.info("Common setup works. TOML file already edited.");
        }

    }

    private boolean isFirstLaunch() {
        File markerFile = new File("defaultconfigs/tensura-reincarnated/tempest_first_launch_marker");
        return !markerFile.exists();
    }

    private void markAsEdited() {
        File markerFile = new File("defaultconfigs/tensura-reincarnated/tempest_first_launch_marker");

        try {
            if (markerFile.createNewFile()) {
                System.out.println("Marker file created: " + markerFile.getAbsolutePath());
            } else {
                System.out.println("Marker file already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating marker file: " + e.getMessage());
        }

    }

    public void editTOMLFile() {
        File tomlFile = new File("defaultconfigs/tensura-reincarnated/common.toml");
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(tomlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading the TOML file: " + e.getMessage());
            return;
        }

        String content = contentBuilder.toString();
        String[] startingRaces = {"create_new_tempest:mechanical_construct"};
        String[] randomRaces = {"create_new_tempest:mechanical_construct"};
        // Uncomment and use this array if you want to add new skills
        // String[] newSkills = {"create_new_tempest:some_skill"};

        String startingRacesKey = "startingRaces = [";
        String randomRacesKey = "possibleRandomRaces = [";
        // String reincarnationSkillsKey = "reincarnationSkills = [";

        // Add the items to the corresponding lists in the TOML file content
        content = this.addItemsToTOMLList(content, startingRacesKey, startingRaces);
        content = this.addItemsToTOMLList(content, randomRacesKey, randomRaces);
        // content = this.addItemsToTOMLList(content, reincarnationSkillsKey, newSkills);

        // Write the updated content back to the TOML file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tomlFile))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to the TOML file: " + e.getMessage());
        }

        System.out.println("Items added to TOML lists successfully.");
    }


    private String addItemsToTOMLList(String content, String listKey, String[] newItems) {
        int index = content.indexOf(listKey);
        if (index == -1) {
            System.out.println("List identifier '" + listKey + "' not found.");
            return content;
        } else {
            int endIndex = content.indexOf("]", index) + 1;
            if (endIndex == 0) {
                System.out.println("Closing bracket not found for list: " + listKey);
                return content;
            } else {
                String listContent = content.substring(index, endIndex);

                for (String newItem : newItems) {
                    if (!listContent.contains(newItem)) {
                        listContent = listContent.replace("]", ", \"" + newItem + "\"]");
                    }
                }

                return content.replace(content.substring(index, endIndex), listContent);
            }
        }
    }
}