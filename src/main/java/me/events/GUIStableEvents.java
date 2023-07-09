package me.events;

import me.classes.HorseInformation;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static me.relxis.HorseMastery.stable;
import static me.utils.GUI.color;
import static me.utils.GUIStable.*;
import static me.utils.HorseUpdate.updateLore;

public class GUIStableEvents implements Listener {

    @EventHandler
    public void GUI(InventoryClickEvent event) {
        if(!(event.getInventory().equals(stable))) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        updateHorseNames();
        if(slot == 0) {
            HorseInformation info = new HorseInformation(1,1,10,0.0, Horse.Color.BROWN, Horse.Style.NONE);
            giveHorse(brown,player,info);
        }else if(slot == 1) {
            HorseInformation info = new HorseInformation(2,1,20,0.0, Horse.Color.BLACK, Horse.Style.NONE);
            giveHorse(black,player,info);
        }else if(slot == 2) {
            HorseInformation info = new HorseInformation(3,1,30,0.0, Horse.Color.CHESTNUT, Horse.Style.NONE);
            giveHorse(chestnut,player,info);
        }else if(slot == 3) {
            HorseInformation info = new HorseInformation(4,1,40,0.0, Horse.Color.WHITE, Horse.Style.NONE);
            giveHorse(white,player,info);
        }
    }

    public void giveHorse(String name, Player player, HorseInformation info) {
        ItemStack item = createHorse(name,info);
        updateLore(item);
        player.getInventory().addItem(item);
        player.sendMessage(color("&6[HorseMastery] &aYou're given a " + name + "&a."));
    }

}
