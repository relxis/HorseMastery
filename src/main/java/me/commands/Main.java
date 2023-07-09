package me.commands;

import com.jeff_media.morepersistentdatatypes.DataType;
import me.classes.HorseInformation;
import me.relxis.HorseMastery;
import me.utils.GUISkin;
import me.utils.GUIStable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;

import static me.relxis.HorseMastery.*;
import static me.utils.GUI.color;
import static me.utils.GUI.createItemStack;
import static me.utils.GUIStable.updateHorseNames;
import static me.utils.HorseUpdate.updateAttributes;
import static me.utils.HorseUpdate.updateLore;
import static me.utils.HorseUpdate.*;

public class Main implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player player)) return false;
        if(args.length == 0) {
            if(!hasPermission(player,"HorseMastery.help")) {return false;}
            help(player);
            return false;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("reload")) {
                if(!hasPermission(player,"HorseMastery.reload")) {return false;}
                plugin.saveDefaultConfig();
                plugin.getConfig().options().copyDefaults();
                plugin.reloadConfig();
                plugin.createMessagesFile();
                updateHorseNames();
                stable = new GUIStable().createStable();
                skins = new GUISkin().createSkinMenu();
                player.sendMessage(color("&6[HorseMastery] &aConfiguration reloaded."));
                return false;
            } else if (args[0].equalsIgnoreCase("exp")) {
                if(!hasPermission(player,"HorseMastery.exp")) {return false;}
                player.sendMessage(color("&6[HorseMastery] &aInvalid arguments, please try again."));
                return false;
            } else if (args[0].equalsIgnoreCase("level")) {
                if(!hasPermission(player,"HorseMastery.level")) {return false;}
                player.sendMessage(color("&6[HorseMastery] &aInvalid arguments, please try again."));
                return false;
            } else if (args[0].equalsIgnoreCase("stable")) {
                if(!hasPermission(player,"HorseMastery.stable")) {return false;}
                player.openInventory(stable);
                return false;
            } else if(args[0].equalsIgnoreCase("armory")) {
                if(!hasPermission(player,"HorseMastery.armory")) {return false;}
                openarmory(player);
                return false;
            } else if (args[0].equalsIgnoreCase("skins")) {
                if(!hasPermission(player,"HorseMastery.skins")) {return false;}
                player.openInventory(skins);
                return false;
            }
        }

        if(args.length == 2) {
            player.sendMessage(color("&6[HorseMastery] &aInvalid arguments, please try again."));
            return false;
        }

        if(args.length == 3) {
            if(!hasPermission(player,"HorseMastery.exp")) {return false;}
            if(args[0].equalsIgnoreCase("exp")) {
                if(args[1].equalsIgnoreCase("set")) {
                    if (!isHorse(player.getInventory().getItemInMainHand())) {
                        player.sendMessage(color("&6[HorseMastery] &aThe item in your hand isn't a valid horse."));
                        return false;
                    }
                    if (!args[2].matches("\\d+")) {
                        player.sendMessage(color("&6[HorseMastery] &aInvalid arguments, please try again."));
                        return false;
                    }
                    HorseInformation info = getHorseInformation(player.getInventory().getItemInMainHand());
                    info.setExp(Integer.parseInt(args[2]));
                    saveHorseInformation(player.getInventory().getItemInMainHand(),info);
                    updateLore(player.getInventory().getItemInMainHand());
                    player.sendMessage(color("&6[HorseMastery] &aHorse exp has been set to &e" + args[2] + "&a."));
                    if(spawnedHorse.containsKey(player)) {
                        AbstractHorse horse = spawnedHorse.get(player);
                        updateAttributes(horse,info.getLevel(),info.getLevelCap());
                    }
                    return false;
                }
            } else if(args[0].equalsIgnoreCase("level")) {
                if(!hasPermission(player,"HorseMastery.level")) {return false;}
                if (args[1].equalsIgnoreCase("set")) {
                    if (!isHorse(player.getInventory().getItemInMainHand())) {
                        player.sendMessage(color("&6[HorseMastery] &aThe item in your hand isn't a valid horse."));
                        return false;
                    }
                    if (!args[2].matches("\\d+")) {
                        player.sendMessage(color("&6[HorseMastery] &aInvalid arguments, please try again."));
                        return false;
                    }
                    if(Integer.parseInt(args[2]) > 40 || Integer.parseInt(args[2]) < 1) {
                        player.sendMessage(color("&6[HorseMastery] &aHorse level must be within the range of &e1-40&a."));
                        return false;
                    }

                    HorseInformation info = getHorseInformation(player.getInventory().getItemInMainHand());
                    info.setLevel(Integer.parseInt(args[2]));
                    saveHorseInformation(player.getInventory().getItemInMainHand(), info);
                    updateLore(player.getInventory().getItemInMainHand());
                    player.sendMessage(color("&6[HorseMastery] &aHorse level has been set to &e" + args[2] + "&a."));
                    if(spawnedHorse.containsKey(player)) {
                        AbstractHorse horse = spawnedHorse.get(player);
                        updateAttributes(horse,info.getLevel(),info.getLevelCap());
                    }
                    return false;
                }
            }
        }
        player.sendMessage(color("&6[HorseMastery] &aInvalid arguments, please try again."));
        return false;
    }

    public void help(Player player) {
        player.sendMessage(color("&6[HorseMastery] &aversion 1.2"));
        player.sendMessage(color("&6- &e/horses &estable &aOpens the stable menu."));
        player.sendMessage(color("&6- &e/horses &eskins &aOpens the skin menu"));
        player.sendMessage(color("&6- &e/horses &eexp &aModifies the exp value of the horse in hand."));
        player.sendMessage(color("&6- &e/horses &elevel &aAdjusts the level of the horse in hand."));
        player.sendMessage(color("&6- &e/horses &ereload &aReloads the configuration file."));
        player.sendMessage(color("&6- &e/horses &earmory &aOpen the armory GUI."));

    }

    public boolean hasPermission(Player player, String permission) {
        if(player.hasPermission("*")) return true;
        if(player.hasPermission("HorseMastery.*")) return  true;

        if(!player.hasPermission(permission)) {
            player.sendMessage(color("&6[HorseMastery] &aYou don't have the required permission to use this command."));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 1) {
            return List.of("stable","exp","level","reload","armory","skins");
        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("exp") || args[0].equalsIgnoreCase("level")) {
                return List.of("set");
            }
        }
        return new ArrayList<>();
    }

    public void openarmory(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        Inventory inventory = createHorsearmory(player);
        player.closeInventory();
        if(inventoryHashMap.containsKey(player)) {
            inventory = inventoryHashMap.get(player);
            player.openInventory(inventory);
        } else if(data.has(key, DataType.ITEM_STACK)) {
            ItemStack itemStack = data.get(key,DataType.ITEM_STACK);
            if(itemStack == null) return;
            inventory.setItem(2,itemStack);
            player.openInventory(inventory);
            inventoryHashMap.put(player,inventory);
        } else {
            ItemStack itemStack = new ItemStack(Material.AIR);
            inventory.setItem(2,itemStack);
            player.openInventory(inventory);
            inventoryHashMap.put(player,inventory);
        }
    }

    public Inventory createHorsearmory(Player player) {
        String title = plugin.getConfig().getString("Horse armory title");
        title = title.replace("{player}",player.getName());
        Inventory GUI = Bukkit.createInventory(player, InventoryType.HOPPER,title);
        ItemStack item = createItemStack(Material.BLACK_STAINED_GLASS_PANE,"",1,null);
        GUI.setItem(0,item);
        GUI.setItem(1,item);
        GUI.setItem(3,item);
        GUI.setItem(4,item);
        return GUI;
    }
}
