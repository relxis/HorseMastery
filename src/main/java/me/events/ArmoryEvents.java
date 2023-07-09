package me.events;

import com.jeff_media.morepersistentdatatypes.DataType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.relxis.HorseMastery.*;

public class ArmoryEvents implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        Inventory topInventory = event.getView().getTopInventory();
        Inventory bottomInventory = event.getView().getBottomInventory();
        if (!(inventoryHashMap.containsValue(topInventory) || inventoryHashMap.containsValue(bottomInventory))) return;

        Player player = inventoryHashMap.inverse().get(topInventory);
        ItemStack item = topInventory.getItem(2);
        if (item == null) {
            item = new ItemStack(Material.AIR);
        }
        player.getPersistentDataContainer().set(key, DataType.ITEM_STACK, item);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        Inventory bottomInventory = event.getView().getBottomInventory();
        if (!(inventoryHashMap.containsValue(topInventory) || inventoryHashMap.containsValue(bottomInventory))) return;

        if (event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().equals(topInventory)) {
            int slot = event.getSlot();
            if (slot != 2) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryItemMove(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        Inventory bottomInventory = event.getView().getBottomInventory();
        if (!(inventoryHashMap.containsValue(topInventory) || inventoryHashMap.containsValue(bottomInventory))) return;

        List<InventoryAction> actions = List.of(InventoryAction.HOTBAR_SWAP, InventoryAction.HOTBAR_MOVE_AND_READD, InventoryAction.COLLECT_TO_CURSOR);

        if (actions.contains(event.getAction())) {
            event.setCancelled(true);
            return;
        }

        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        List<Material> materials = List.of(Material.LEATHER_HORSE_ARMOR, Material.IRON_HORSE_ARMOR, Material.GOLDEN_HORSE_ARMOR, Material.DIAMOND_HORSE_ARMOR, Material.AIR);

        if (currentItem == null) return;
        if (!materials.contains(currentItem.getType())) {
            event.setCancelled(true);
            return;
        }

    }
}
