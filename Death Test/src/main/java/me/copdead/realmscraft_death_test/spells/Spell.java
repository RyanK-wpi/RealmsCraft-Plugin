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
    private String name;
    protected ItemStack spell = null;
    private int model;
    private int circle;

    public Spell(String name, int circle, int model) {
        this.name = name;
        this.circle = circle;
        this.model = model;

        registerSpell();
    }

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

    public abstract void spellEffect(Player caster);

    public abstract void getDescription(Player caster);

    void castSpell(Player caster) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> spellEffect(caster), 0);
    }

    private void registerSpell() {
        //add spell to the list
        SpellSelectionManager.getSpells().get(circle).put(name, this);
    }

    //get Cooldown
    //create cooldown
}
