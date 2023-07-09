package me.utils;

import com.jeff_media.morepersistentdatatypes.DataType;
import me.classes.HorseInformation;
import me.classes.HorseInformationType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static me.relxis.HorseMastery.*;
import static me.utils.GUI.*;

public class HorseUpdate {

    public static HorseInformation getHorseInformation(ItemStack item) {
        HorseInformation info = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin,"horse"),new HorseInformationType());
        info.updateDivisor();
        return info;
    }

    public static void saveHorseInformation(ItemStack item, HorseInformation info) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin,"horse"),new HorseInformationType(),info);
        item.setItemMeta(itemMeta);
    }

    public static void updateLore(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        HorseInformation info = itemMeta.getPersistentDataContainer().get(new NamespacedKey(plugin,"horse"),new HorseInformationType());
        int level = info.getLevel();
        double expPercentage = info.getExpPercentage();
        if(level >= info.getLevelCap()) {
            level = info.getLevelCap();
            expPercentage = 0.0;
        }

        List<String> lore = plugin.getConfig().getStringList("Saddle lore");
        replaceStrings(lore,"{tier}",String.valueOf(info.getTier()));
        replaceStrings(lore,"{level}",String.valueOf(level));
        replaceStrings(lore,"{levelCap}",String.valueOf(info.getLevelCap()));
        replaceStrings(lore,"{armour}","None");
        replaceStrings(lore,"{exp%}", String.valueOf(expPercentage));
        replaceStrings(lore,"&","§");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }
    public static boolean isHorse(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return false;
        return itemMeta.getPersistentDataContainer().has(new NamespacedKey(plugin,"horse"),new HorseInformationType());
    };

    public static void updateAttributes(AbstractHorse horse, int level, int levelCap) {
        double baseBPS = plugin.getConfig().getDouble("Blocks per second.Base value");
        double baseJumpHeight = plugin.getConfig().getDouble("Jump height.Base value");
        double increasedBPS = plugin.getConfig().getDouble("Blocks per second.Value increased per level");
        double increasedJumpHeight = plugin.getConfig().getDouble("Jump height.Value increased per level");

        if(level > levelCap) {
            level = levelCap;
        }

        baseBPS = baseBPS / 43.17;
        increasedBPS = increasedBPS / 43.17;

        double jumpStrength = LinearInterpolation(baseJumpHeight + level * increasedJumpHeight);
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(baseBPS + level * increasedBPS);
        horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(jumpStrength);
    }

    public static AbstractHorse spawnHorse(Player player, HorseInformation info) {
        AbstractHorse horse = null;
        ItemStack saddle = createItemStack(Material.SADDLE,"§6Saddle",1,null);

        ItemStack armor = null;

        if (player.getPersistentDataContainer().has(key, DataType.ITEM_STACK)) {
            if(player.getPersistentDataContainer().get(key,DataType.ITEM_STACK) != null) {
                armor = player.getPersistentDataContainer().get(key, DataType.ITEM_STACK);
            }
        }

        if(armor == null || armor.getType().equals(Material.AIR)) {
             armor = new ItemStack(Material.BARRIER);
        }

        ItemMeta itemMeta = armor.getItemMeta();
        if (itemMeta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            String armorType = itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (armorType.equals("Zombie")) {
                horse = (ZombieHorse) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE_HORSE);
            } else {
                horse = (SkeletonHorse) player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON_HORSE);
            }
        } else {
            horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
            horse.getInventory().setItem(1, armor);
        }
        
        horse.getInventory().setSaddle(saddle);

        horse.setOwner(player);
        horse.setInvulnerable(true);
        String format = color(plugin.getConfig().getString("Horse name format"));
        String name = format.replace("{player}",player.getName());

        if(horse instanceof Horse) {
            ((Horse) horse).setColor(info.getColor());
            ((Horse) horse).setStyle(info.getStyle());
        }

        horse.setCustomName(name);
        horse.setCustomNameVisible(true);
        return horse;
    }

    public static double LinearInterpolation(double blocks) {
        double[][] table = {
                {1, 0.4},
                {1.5, 0.4783896632803},
                {2, 0.57628879677},
                {2.5, 0.6452555494763},
                {3, 0.7178228209413},
                {3.5, 0.7830656075598},
                {4, 0.8487416781119},
                {4.5, 0.9115636843716},
                {5.125,1.0}
        };

        if (blocks <= table[0][0]) {
            return table[0][1];
        } else if (blocks >= table[table.length - 1][0]) {
            return table[table.length - 1][1];
        } else {
            for (int i = 0; i < table.length - 1; i++) {
                if (blocks >= table[i][0] && blocks < table[i + 1][0]) {
                    double x0 = table[i][0];
                    double y0 = table[i][1];
                    double x1 = table[i + 1][0];
                    double y1 = table[i + 1][1];

                    double slope = (y1 - y0) / (x1 - x0);
                    return y0 + slope * (blocks - x0);
                }
            }
        }
        return 1.0;
    }

    public static void checkSaddleInInventory(Player player) {
        if(!spawnedHorse.containsKey(player)) return;
        AbstractHorse horse = spawnedHorse.get(player);
        ItemStack saddle = connectedItemStack.get(horse);
        Inventory inventory = player.getInventory();
        if(inventory.first(saddle) == -1) {
            String message = color(plugin.getMessages().getString("Interaction disabled message"));
            spawnedHorse.remove(player);
            connectedItemStack.remove(horse);
            expCache.remove(horse);
            horse.remove();
            player.sendMessage(message);
        }
    }
    public static void replaceStrings(List<String> list, String target, String replacement) {
        list.replaceAll(s -> s.replace(target, replacement));
    }

}

