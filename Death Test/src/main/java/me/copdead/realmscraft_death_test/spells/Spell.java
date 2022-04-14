package me.copdead.realmscraft_death_test.spells;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;

public abstract class Spell {
    private static final Realmscraft_death_test plugin = (Realmscraft_death_test) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");
    protected String name;
    //protected Player caster;
    protected ItemStack spell = null;
    protected int model;
    protected int circle;
    //store circle?
    //cooldowns?
    //useful things: casted spell, marshal request,

    public ItemStack getSpell() {
        if(spell ==  null) createSpell();

        return spell;
    }

    private void createSpell() {
        ItemStack focus = new ItemStack(Material.STICK);
        ItemMeta meta = focus.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Collections.singletonList("Spell Focus"));
        meta.setCustomModelData(model);
        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "spellcircle"), PersistentDataType.INTEGER, circle);
        focus.setItemMeta(meta);

        spell = focus;
    }

    public abstract void castSpell(Player caster);

    public abstract void getDescription(Player caster);

    protected void registerSpell() {
        //add spell to the list
        SpellSelectionManager.getSpells().get(circle).put(getSpell(), this);
    }


    //get Cooldown
    //create cooldown
}
