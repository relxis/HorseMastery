package me.relxis;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.jeff_media.morepersistentdatatypes.DataType;
import me.commands.Main;
import me.events.*;
import me.utils.GUISkin;
import me.utils.GUIStable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class HorseMastery extends JavaPlugin {

    public static HorseMastery plugin;
    public static Inventory stable;
    public static Inventory skins;
    public static FileConfiguration messages;
    public static HashMap<Player, AbstractHorse> spawnedHorse;
    public static HashMap<AbstractHorse, ItemStack> connectedItemStack;
    public static HashMap<AbstractHorse, Double> expCache;
    public static NamespacedKey key;
    public static BiMap<Player, Inventory> inventoryHashMap;

    public HorseMastery() {
        key = new NamespacedKey(this,"horse");
        spawnedHorse = new HashMap<>();
        connectedItemStack = new HashMap<>();
        expCache = new HashMap<>();
        inventoryHashMap = HashBiMap.create();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().options().copyDefaults();
        getCommand("Horses").setExecutor(new Main());

        createMessagesFile();
        plugin = this;
        stable = new GUIStable().createStable();
        skins = new GUISkin().createSkinMenu();

        getServer().getPluginManager().registerEvents(new GUIStableEvents(),this);
        getServer().getPluginManager().registerEvents(new SummonEvents(),this);
        getServer().getPluginManager().registerEvents(new SaddleInteractEvents(),this);
        getServer().getPluginManager().registerEvents(new ExpCalculateEvents(),this);
        getServer().getPluginManager().registerEvents(new MountEvents(),this);
        getServer().getPluginManager().registerEvents(new ArmoryEvents(),this);
        getServer().getPluginManager().registerEvents(new GUISkinsEvents(),this);

    }

    @Override
    public void onDisable() {
        for (AbstractHorse horse : spawnedHorse.values()) {
            horse.remove();
        }
        for (Map.Entry<Player,Inventory> entry: inventoryHashMap.entrySet()) {
            Player player = entry.getKey();
            Inventory inventory = entry.getValue();
            ItemStack item = inventory.getItem(2);
            if(item == null) {
                item = new ItemStack(Material.AIR);
            }
            player.getPersistentDataContainer().set(key, DataType.ITEM_STACK,item);
        }
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public void createMessagesFile() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messages = new YamlConfiguration();
        try {
            messages.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        };
    }
}
