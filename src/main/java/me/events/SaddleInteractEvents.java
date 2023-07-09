package me.events;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static me.relxis.HorseMastery.expCache;
import static me.relxis.HorseMastery.connectedItemStack;
import static me.relxis.HorseMastery.spawnedHorse;
import static me.relxis.HorseMastery.plugin;
import static me.utils.GUI.color;
import static me.utils.HorseUpdate.checkSaddleInInventory;

public class SaddleInteractEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!spawnedHorse.containsKey(player)) return;

        AbstractHorse horse = spawnedHorse.get(player);
        ItemStack saddle = connectedItemStack.get(horse);
        InventoryAction action = event.getAction();
        if(Objects.equals(event.getCurrentItem(), saddle)) {
            String message = color(plugin.getMessages().getString("Interaction disabled message"));
            spawnedHorse.remove(player);
            connectedItemStack.remove(horse);
            expCache.remove(horse);
            horse.remove();
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!spawnedHorse.containsKey(player)) return;
        checkSaddleInInventory(player);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!spawnedHorse.containsKey(player)) return;
        checkSaddleInInventory(player);
    }



}
