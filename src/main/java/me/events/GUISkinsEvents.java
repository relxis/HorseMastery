package me.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static me.relxis.HorseMastery.*;
import static me.utils.GUI.color;
import static me.utils.GUI.createItemStack;
import static me.utils.HorseUpdate.replaceStrings;

public class GUISkinsEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(skins)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if(slot == 0) {
            String name = plugin.getConfig().getString("Skins.Zombie horse armor.Name");
            List<String> lore = plugin.getConfig().getStringList("Skins.Zombie horse armor.Lore");
            replaceStrings(lore,"&","§");
            giveHorseArmor(player,name,"Zombie",lore);
            player.sendMessage(color("&6[HorseMastery] &aYou're given a " + name + "&a."));
        } else if (slot == 1) {
            String name = plugin.getConfig().getString("Skins.Skeleton horse armor.Name");
            List<String> lore = plugin.getConfig().getStringList("Skins.Skeleton horse armor.Lore");
            replaceStrings(lore,"&","§");
            giveHorseArmor(player,name,"Skeleton",lore);
            player.sendMessage(color("&6[HorseMastery] &aYou're given a " + name + "&a."));
        }

    }

    private void giveHorseArmor(Player player, String name, String armorType, List<String> lore) {
        ItemStack item = createItemStack(Material.LEATHER_HORSE_ARMOR,color(name),1,null);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING,armorType);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        player.getInventory().addItem(item);
    }
}
