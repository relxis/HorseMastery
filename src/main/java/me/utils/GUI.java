package me.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GUI {
    public static ItemStack createItemStack(Material material, String name, Integer amount, List<String> lore) {
        material = (material != null) ? material : Material.STONE;

        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(amount != null ? amount : 1);

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            if (name != null) {
                itemMeta.setDisplayName(name);
            }
            if (lore != null) {
                itemMeta.setLore(lore);
            }
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }
    public static String color(String string) {
        if(string == null) return "invalid string format";
        return string.replace("&","§");
    }
}
