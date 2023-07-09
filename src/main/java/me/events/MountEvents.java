package me.events;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.Objects;

import static me.relxis.HorseMastery.messages;
import static me.utils.GUI.color;

public class MountEvents implements Listener {

    @EventHandler
    public void onPlayerMount(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getMount() instanceof AbstractHorse horse)) return;
        ItemStack saddle = horse.getInventory().getSaddle();
        if(saddle == null) return;
        ItemMeta itemMeta = saddle.getItemMeta();
        if(itemMeta == null) return;
        if (!itemMeta.getDisplayName().equals("§6Saddle")) return;
        String message = color(messages.getString("Mount disallowed message"));
        if(horse.getOwner() == null) {
            event.setCancelled(true);
            player.sendMessage(message);
            return;
        }
        if (!Objects.equals(horse.getOwner().getName(), player.getName())) {
            event.setCancelled(true);
            player.sendMessage(message);
        }

    }

}
