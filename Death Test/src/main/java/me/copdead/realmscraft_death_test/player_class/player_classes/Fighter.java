package me.copdead.realmscraft_death_test.player_class.player_classes;

import me.copdead.realmscraft_death_test.player_class.PlayerClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Fighter extends PlayerClass {
    @Override
    public void giveClassItems(Player player) {
        setPlayerClass(player, "Fighter");

        PlayerInventory inv = player.getInventory();
        //remove Items
        inv.clear(); //removeClassItems method somewhere...
        player.removePotionEffect(PotionEffectType.ABSORPTION);

        //give Armor
        ItemStack armor = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta armorMeta = armor.getItemMeta();
        armorMeta.setUnbreakable(true);
        armor.setItemMeta(armorMeta);

        inv.setBoots(armor);

        armor.setType(Material.CHAINMAIL_LEGGINGS);
        inv.setLeggings(armor);

        armor.setType(Material.CHAINMAIL_CHESTPLATE);
        inv.setChestplate(armor);

        armor.setType(Material.CHAINMAIL_HELMET);
        inv.setHelmet(armor);

        //give Weapons
        ItemStack weapon = new ItemStack(Material.IRON_SWORD);
        ItemMeta weaponMeta = weapon.getItemMeta();
        weaponMeta.setUnbreakable(true);
        weapon.setItemMeta(weaponMeta);

        inv.setItem(0, weapon);

        weapon.setType(Material.IRON_AXE);
        inv.setItem(1, weapon);

        weapon.setType(Material.SHIELD);
        inv.setItemInOffHand(weapon);

        //give Effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 9, false, false, false));

        //Inform the Player
        player.sendMessage("You have selected Fighter, have fun!");
    }
}
