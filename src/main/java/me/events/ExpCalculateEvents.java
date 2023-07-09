package me.events;

import me.classes.HorseInformation;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import static me.relxis.HorseMastery.*;
import static me.utils.GUI.color;
import static me.utils.HorseUpdate.*;

public class ExpCalculateEvents implements Listener {

    @EventHandler
    public void ExpCalculation(PlayerMoveEvent event) {
        if(!onHorse(event)) return;
        Player player = event.getPlayer();
        AbstractHorse horse = spawnedHorse.get(player);
        ItemStack connectedSaddle = connectedItemStack.get(horse);
        HorseInformation info = getHorseInformation(connectedSaddle);
        if(info.getLevel() >= info.getLevelCap()) return;

        Location location = horse.getLocation();
        Location previousLocation = event.getFrom();
        //distance calculation
        double dx = location.getX() - previousLocation.getX();
        double dy = location.getZ() - previousLocation.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy);
        //update exp
        expCache.put(horse, expCache.get(horse) + distance);
        //update stuff as currentExp exceeds 20
        double currentExp = expCache.get(horse);
        if(currentExp < 20) return;

        int previousLevel = info.getLevel();
        info.setExp(info.getExp() + currentExp);
        saveHorseInformation(connectedSaddle,info);
        expCache.put(horse,0.0);
        updateLore(connectedItemStack.get(horse));
        updateAttributes(horse,info.getLevel(),info.getLevelCap());
        checkSaddleInInventory(player);
        //stuff to execute when the horse level up
        if(info.getLevel() != previousLevel) {
            if(info.getLevel() == 1) return;
            String message = color(plugin.getMessages().getString("Level up message"));
            player.sendMessage(message);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
        }
    }

    public boolean onHorse(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!spawnedHorse.containsKey(player)) return false;
        if (!player.isInsideVehicle()) return false;
        AbstractHorse horse = spawnedHorse.get(player);
        if (!(player.getVehicle() == horse)) return false;
        ItemStack saddle = horse.getInventory().getSaddle();
        if (saddle == null) return false;
        if (saddle.getItemMeta() == null) return false;
        return saddle.getItemMeta().getDisplayName().equals("§6Saddle");
    }

}
