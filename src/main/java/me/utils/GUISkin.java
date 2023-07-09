package me.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.relxis.HorseMastery.plugin;
import static me.utils.GUI.color;
import static me.utils.GUI.createItemStack;

public class GUISkin {

    public Inventory createSkinMenu() {
        String title = plugin.getConfig().getString("Skin menu title");
        if(title == null) {title = "";}
        Inventory menu = Bukkit.createInventory(null,9,title);
        String ZombieArmorName = plugin.getConfig().getString("Skins.Zombie horse armor.Name");
        String SkeletonArmorName = plugin.getConfig().getString("Skins.Skeleton horse armor.Name");

        ItemStack zombieArmor = createItemStack(Material.LEATHER_HORSE_ARMOR,color(ZombieArmorName),1,
                List.of(color("&eClick to obtain the horse armor")));
        ItemStack skeletonArmor = createItemStack(Material.LEATHER_HORSE_ARMOR,color(SkeletonArmorName),1,
                List.of(color("&eClick to obtain the horse armor")));

        menu.setItem(0,zombieArmor);
        menu.setItem(1,skeletonArmor);


        return menu;
    }

}
