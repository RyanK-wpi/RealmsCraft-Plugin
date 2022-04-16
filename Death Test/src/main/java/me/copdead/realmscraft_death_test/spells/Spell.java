package me.copdead.realmscraft_death_test.spells;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Collections;
import java.util.UUID;

public abstract class Spell {
    private static final Realmscraft_death_test plugin = (Realmscraft_death_test) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");
    private String name;
    protected ItemStack spell = null;
    private ItemStack outOfSpells = null;
    private int model;
    private int circle;
    private int uses = -1;

    public Spell(String name, int circle, int model) {
        this.name = name;
        this.circle = circle;
        this.model = model;

        registerSpell();
    }

    public Spell(String name, int circle, int uses, int model) {
        this.name = name;
        this.circle = circle;
        this.uses = uses;
        this.model = model;

        registerSpell();
    }

    public String getName() {
        return name;
    }

    public int getUses() {
        return uses;
    }

    public ItemStack getSpell() {
        if(spell ==  null) createSpell();

        return spell;
    }

    public ItemStack getOutOfSpells() {
        if(outOfSpells == null) createOutOfSpells();

        return outOfSpells;
    }

    private void createSpell() {
        ItemStack focus = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = focus.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Collections.singletonList("Spell Focus"));
        meta.setCustomModelData(model);
        meta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, "spellcircle"), PersistentDataType.INTEGER, circle);

        //made sword act like a hand
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),"generic.attack_damage", -3, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(),"generic.attack_speed", 2.4, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        focus.setItemMeta(meta);

        spell = focus;
    }

    private void createOutOfSpells() {
        ItemStack focus = new ItemStack(Material.BARRIER);
        ItemMeta meta = focus.getItemMeta();

        meta.setDisplayName("Out of " + name);
        meta.setLore(Collections.singletonList("Spell Focus"));
        meta.setCustomModelData(1);
        focus.setItemMeta(meta);

        outOfSpells = focus;
    }

    public abstract void spellEffect(Player caster);

    public abstract void getDescription(Player caster);

    void castSpell(Player caster) {
        new BukkitRunnable() {
            @Override
            public void run() {
                spellEffect(caster);
            }
        }.runTaskLater(plugin, 0);
    }

    private void registerSpell() {
        //add spell to the list
        SpellSelectionManager.getSpells().get(circle).put(name, this);

        Scoreboard spellCounter = SpellSelectionManager.getSpellCounter();
        spellCounter.registerNewObjective(name, "dummy", name);
    }
}
