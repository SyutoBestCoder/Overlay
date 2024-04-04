package com.unloged.overlay;

import com.google.gson.*;
import com.unloged.overlay.config.Config;
import com.unloged.overlay.mods.impl.BedwarsOverlay;
import com.unloged.overlay.utilities.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(modid = "2better_zoom2", version = "2.0")
public class Main
{
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    public static final Logger LOG = LogManager.getLogger("BWU");
    private final static File modDirectory = new File(Minecraft.func_71410_x().field_71412_D + File.separator + "bedwars utils");
    private static File configFile = new File(modDirectory, "bwu.json");
    public static File snipersFile = new File(modDirectory, "snipers.txt");
    public static File cheatersFile = new File(modDirectory, "cheaters.txt");
    public static List<String> snipers = new ArrayList<>();
    public static List<String> cheaters = new ArrayList<>();
    private static JsonObject config;
    public static boolean gui;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (!modDirectory.exists()) {
            modDirectory.mkdir();
        }
        FMLCommonHandler.instance().bus().register(new EventListener());
        ClientCommandHandler.instance.func_71560_a(new BedwarsCommand());
        ClientCommandHandler.instance.func_71560_a(new BlacklistCommand());
        if (!configFile.exists())
            try {
                configFile.createNewFile();
                saveConfig();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        else {
            final JsonParser jsonParser = new JsonParser();
            try (FileReader reader = new FileReader(configFile)) {
                final Object obj = jsonParser.parse(reader);
                config = (JsonObject) obj;
            } catch (JsonSyntaxException | ClassCastException | IOException e) {
                e.printStackTrace();
            }
        }
        loadConfig(config);
        generateFile(snipersFile);
        generateFile(cheatersFile);
        Main.getExecutor().execute(() -> {
            loadBlacklists();
        });
    }

    public static void saveConfig() {
        try {
            Path path = PlayerUtils.mc.field_71412_D.toPath().resolve("bedwars utils").resolve("bwu.json");
            JsonObject jo = new JsonObject();
            jo.addProperty("Bedwars Overlay enabled", Config.overlayEnabled);
            jo.addProperty("Held down", Config.heldDown);
            jo.addProperty("Hypixel API Key", Config.hypixelAPI_KEY != null ? Config.hypixelAPI_KEY : "");
            jo.addProperty("Hypixel API Key 2", Config.hypixelAPI_KEY2 != null ? Config.hypixelAPI_KEY2 : "");
            jo.addProperty("Hypixel API Key 3", Config.hypixelAPI_KEY3 != null ? Config.hypixelAPI_KEY3 : "");
            jo.addProperty("Hypixel API Key 4", Config.hypixelAPI_KEY4 != null ? Config.hypixelAPI_KEY4 : "");
            jo.addProperty("Show bind", Config.showBind);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonData = gson.toJson(jo);
            Files.write(path, jsonData.getBytes(StandardCharsets.UTF_8));
        } catch (Throwable t) {
            LOG.error("Unable to save config.", t);
        }
    }

    private void loadConfig(JsonObject data) {
        try {
            Config.overlayEnabled = data.get("Bedwars Overlay enabled").getAsBoolean();
            Config.heldDown = data.get("Held down").getAsBoolean();
            Config.hypixelAPI_KEY = !data.has("Hypixel API Key") ? "" : data.get("Hypixel API Key").getAsString();
            Config.hypixelAPI_KEY2 = !data.has("Hypixel API Key 2") ? "" : data.get("Hypixel API Key 2").getAsString();
            Config.hypixelAPI_KEY3 = !data.has("Hypixel API Key 3") ? "" : data.get("Hypixel API Key 3").getAsString();
            Config.hypixelAPI_KEY4 = !data.has("Hypixel API Key 4") ? "" : data.get("Hypixel API Key 4").getAsString();
            Config.showBind = data.get("Show bind").getAsInt();
            BedwarsOverlay.apiKeys.clear();
            if (Config.hypixelAPI_KEY != null && !Config.hypixelAPI_KEY.isEmpty()) {
                BedwarsOverlay.apiKeys.add(Config.hypixelAPI_KEY);
            }
            if (Config.hypixelAPI_KEY2 != null && !Config.hypixelAPI_KEY2.isEmpty()) {
                BedwarsOverlay.apiKeys.add(Config.hypixelAPI_KEY2);
            }
            if (Config.hypixelAPI_KEY3 != null && !Config.hypixelAPI_KEY3.isEmpty()) {
                BedwarsOverlay.apiKeys.add(Config.hypixelAPI_KEY3);
            }
            if (Config.hypixelAPI_KEY4 != null && !Config.hypixelAPI_KEY4.isEmpty()) {
                BedwarsOverlay.apiKeys.add(Config.hypixelAPI_KEY4);
            }
        } catch(Throwable t){
            LOG.error("Unable to load config.", t);
        }
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void loadBlacklists() {
        snipers.clear();
        cheaters.clear();
        getBlacklists(snipersFile, snipers, "snipers");
        getBlacklists(cheatersFile, cheaters, "cheaters");
    }

    private void generateFile(File file) {
        if (file.exists()) {
            return;
        }
        try {
            file.createNewFile();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void getBlacklists(File file, List<String> blacklist, String type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                blacklist.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            PlayerUtils.sendMessage("§cError getting " + type + " blacklist.");
        }
    }
}
