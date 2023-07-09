package me.events;

import com.jeff_media.morepersistentdatatypes.DataType;
import me.classes.HorseInformation;
import me.classes.HorseInformationType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import static me.relxis.HorseMastery.*;
import static me.utils.HorseUpdate.*;

public class SummonEvents implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        AbstractHorse horse;
        Action action = event.getAction();
        Player player = event.getPlayer();

        if(event.getItem() == null) return;
        if(!isHorse(event.getItem())) return;
        if(!(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) return;

        HorseInformation info = event.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin,"Horse"),new HorseInformationType());
        if(info == null) return;

        duplicateCheck(event);

        if(spawnedHorse.containsKey(player)) {
            AbstractHorse abstractHorse = spawnedHorse.get(player);
            abstractHorse.remove();
            spawnedHorse.remove(player); // remove connection
            connectedItemStack.remove(spawnedHorse.get(player));
            expCache.remove(spawnedHorse.get(player));
        } else {
            horse = spawnHorse(player,info);
            spawnedHorse.put(player, horse);
            connectedItemStack.put(horse,event.getItem());
            expCache.put(horse,0.0);
            updateAttributes(horse, info.getLevel(), info.getLevelCap());
            updateLore(event.getItem());
        }

    }

    @EventHandler
    public void removeGlitchedHorse(EntitiesLoadEvent event) {
        List<AbstractHorse> abstractHorses = event.getEntities().stream().filter(entity -> entity instanceof AbstractHorse)
                .map(entity -> (AbstractHorse) entity).toList();
        for (AbstractHorse horse : abstractHorses) {
            if (!horse.isEmpty()) continue;
            if (horse.getOwner() == null) continue;
            ItemStack saddle = horse.getInventory().getItem(0);
            if (saddle == null) continue;
            if (saddle.getItemMeta() == null) continue;
            if (!saddle.getItemMeta().getDisplayName().equals("§6Saddle")) continue;
            horse.remove();
            String ownerName = horse.getOwner().getName();
            if(ownerName == null) continue;
            if (Bukkit.getPlayer(ownerName) == null) continue;
            spawnedHorse.remove(Bukkit.getPlayer(ownerName));
            connectedItemStack.remove(horse);
            expCache.remove(horse);
        }
    }

    @EventHandler
    public void distanceCheck(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(!spawnedHorse.containsKey(player)) return;
        AbstractHorse horse = spawnedHorse.get(player);
        if(player.getLocation().distance(horse.getLocation()) > 20) {
            horse.remove();
            spawnedHorse.remove(player);
            connectedItemStack.remove(horse);
            expCache.remove(horse);
        }
    }

    @EventHandler
    public void cancelAbstractHorseInventoryInteraction(InventoryClickEvent event) {
        if(!(event.getView().getTopInventory() instanceof AbstractHorseInventory)) return;

        if(event.getAction() == InventoryAction.COLLECT_TO_CURSOR){
            event.setCancelled(true);
            return;
        }
        ItemStack saddle = ((AbstractHorseInventory) event.getClickedInventory()).getSaddle();
        if (saddle == null) return;
        if (saddle.getItemMeta() == null) return;
        if (!saddle.getItemMeta().getDisplayName().equals("§6Saddle")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void removeGlitchedHorseWhenUnloadingChunks(ChunkUnloadEvent event) {
        List<AbstractHorse> abstractHorses = Arrays.stream(event.getChunk().getEntities())
                .filter(entity -> entity instanceof AbstractHorse)
                .map(entity -> (AbstractHorse) entity).toList();

        for (AbstractHorse horse : abstractHorses) {
            if (!horse.isEmpty()) continue;
            if (horse.getOwner() == null) continue;
            ItemStack saddle = horse.getInventory().getItem(0);
            if (saddle == null) continue;
            if (saddle.getItemMeta() == null) continue;
            if (!saddle.getItemMeta().getDisplayName().equals("§6Saddle")) continue;
            horse.remove();
            String ownerName = horse.getOwner().getName();
            if(ownerName == null) continue;
            if (Bukkit.getPlayer(ownerName) == null) continue;
            spawnedHorse.remove(Bukkit.getPlayer(ownerName));
            connectedItemStack.remove(horse);
            expCache.remove(horse);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!spawnedHorse.containsKey(player)) return;
        AbstractHorse horse = spawnedHorse.get(player);
        connectedItemStack.remove(horse);
        expCache.remove(horse);
        spawnedHorse.remove(player);
        horse.remove();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!spawnedHorse.containsKey(player)) return;
        AbstractHorse horse = spawnedHorse.get(player);
        connectedItemStack.remove(horse);
        expCache.remove(horse);
        spawnedHorse.remove(player);
        horse.remove();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(!(event.getEntity() instanceof AbstractHorse horse)) return;
        if (horse.getInventory().getSaddle() == null) return;
        if (horse.getInventory().getSaddle().getItemMeta() == null) return;
        if (!horse.getInventory().getSaddle().getItemMeta().getDisplayName().equals("§6Saddle")) return;
        event.getDrops().clear();
    }
    public void duplicateCheck(PlayerInteractEvent event) {
        List<AbstractHorse> horses = event.getPlayer().getNearbyEntities(20,20,20).stream().filter(entity -> entity instanceof AbstractHorse)
                .map(entity -> (AbstractHorse) entity).toList();
        for (AbstractHorse abstractHorse : horses) {
            if (!abstractHorse.isEmpty()) continue;
            if (abstractHorse.getOwner() == null) continue;
            if (abstractHorse.getOwner().getName() == null) continue;
            if (!abstractHorse.getOwner().getName().equals(event.getPlayer().getName())) continue;
            if (abstractHorse.getInventory().getSaddle() == null) continue;
            if (abstractHorse.getInventory().getSaddle().getItemMeta() == null) continue;
            if (!abstractHorse.getInventory().getSaddle().getItemMeta().getDisplayName().equals("§6Saddle")) continue;
            if (abstractHorse == spawnedHorse.get(event.getPlayer())) continue;
            abstractHorse.remove();
        }
    }

}
