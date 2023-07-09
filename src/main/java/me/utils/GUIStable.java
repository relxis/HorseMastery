package me.utils;

import me.classes.HorseInformation;
import me.classes.HorseInformationType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

import static me.relxis.HorseMastery.*;
import static me.utils.GUI.color;
import static me.utils.GUI.createItemStack;

public class GUIStable {

    public static String brown = color(plugin.getConfig().getString("Horses.Brown.Name"));
    public static String black = color(plugin.getConfig().getString("Horses.Black.Name"));
    public static String chestnut = color(plugin.getConfig().getString("Horses.Chestnut.Name"));
    public static String white = color(plugin.getConfig().getString("Horses.White.Name"));

    public static void updateHorseNames() {
        brown = color(plugin.getConfig().getString("Horses.Brown.Name"));
        black = color(plugin.getConfig().getString("Horses.Black.Name"));
        chestnut = color(plugin.getConfig().getString("Horses.Chestnut.Name"));
        white = color(plugin.getConfig().getString("Horses.White.Name"));
    }

    public Inventory createStable() {
        String title = color(plugin.getConfig().getString("Stable title"));
        Inventory stable = Bukkit.createInventory(null,9,title);
        ItemStack brownHorse = createItemStack(Material.SADDLE,brown,1, List.of("§eClick to obtain the horse."));
        ItemStack blackHorse = createItemStack(Material.SADDLE,black,1,List.of("§eClick to obtain the horse."));
        ItemStack chestnutHorse = createItemStack(Material.SADDLE,chestnut,1,List.of("§eClick to obtain the horse."));
        ItemStack whiteHorse = createItemStack(Material.SADDLE,white,1,List.of("§eClick to obtain the horse."));
        stable.setItem(0,brownHorse);
        stable.setItem(1,blackHorse);
        stable.setItem(2,chestnutHorse);
        stable.setItem(3,whiteHorse);

        return stable;
    }

    public static ItemStack createHorse(String name, HorseInformation information) {
        information.updateDivisor();
        int tier = information.getTier();
        int level= information.getLevel();
        int levelCap = information.getLevelCap();
        double xp = information.getExp();
        ItemStack item = new ItemStack(Material.SADDLE);
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return null;
        itemMeta.setDisplayName(name);
        itemMeta.setLore(List.of("§7Tier " + tier, "§6Speed: " + level + "/" + levelCap, "§6Jump: " + level + "/" + levelCap, "§5Armour: None", "§bXp: " + xp + "/100"));
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin,"horse");
        data.set(key,new HorseInformationType(),information);
        item.setItemMeta(itemMeta);
        return item;
    }

}
