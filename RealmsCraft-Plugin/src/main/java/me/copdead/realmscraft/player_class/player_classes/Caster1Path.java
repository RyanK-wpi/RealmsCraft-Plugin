package me.copdead.realmscraft.player_class.player_classes;

import me.copdead.realmscraft.player_class.PlayerCasterClass;
import me.copdead.realmscraft.spells.SpellBook;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class Caster1Path extends PlayerCasterClass {
    @Override
    public void giveClassItems(Player player) {
        setPlayerClass(player, "1Path");

        PlayerInventory inv = player.getInventory();
        //remove Items
        inv.clear(); //removeClassItems method somewhere...
        player.removePotionEffect(PotionEffectType.ABSORPTION);
        inv.setItem(8, new SpellBook().getBook());

        //give Armor
        ItemStack armor = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta armorMeta = armor.getItemMeta();
        armorMeta.setUnbreakable(true);
        armor.setItemMeta(armorMeta);

        inv.setBoots(armor);

        armor.setType(Material.LEATHER_LEGGINGS);
        inv.setLeggings(armor);

        armor.setType(Material.LEATHER_CHESTPLATE);
        inv.setChestplate(armor);

        armor.setType(Material.LEATHER_HELMET);
        inv.setHelmet(armor);

        //give Weapons
        ItemStack weapon = new ItemStack(Material.STONE_SWORD);
        ItemMeta weaponMeta = weapon.getItemMeta();
        weaponMeta.setUnbreakable(true);
        weapon.setItemMeta(weaponMeta);

        inv.setItem(0, weapon);

        weapon.setType(Material.STONE_AXE);
        inv.setItem(1, weapon);

        //give Effects
        player.setAbsorptionAmount(20);

        //give Spells
        giveSpells(player);

        //Inform the Player
        player.sendMessage("You have selected One Path, have fun!");
    }
}
