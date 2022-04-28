package me.copdead.realmscraft.spells;

import me.copdead.realmscraft.RealmsCraft;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.UUID;

public abstract class Spell {
    protected static final RealmsCraft plugin = JavaPlugin.getPlugin(RealmsCraft.class);//(Realmscraft_death_test) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");

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

        assert meta != null;
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

        assert meta != null;
        meta.setDisplayName("Out of " + name);
        meta.setLore(Collections.singletonList("Spell Focus"));
        meta.setCustomModelData(1);
        focus.setItemMeta(meta);

        outOfSpells = focus;
    }

    public abstract void spellEffect(Player caster);

    public abstract String getDescription();

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

        //only create scores for spells with uses
        if(uses < 0) return;

        String scoreName = name.substring(0, Math.min(name.length(), 16));      //Scoreboard Ojectives must be <16 char
        Scoreboard spellCounter = SpellSelectionManager.getSpellCounter();
        spellCounter.registerNewObjective(scoreName, "dummy", name);
    }
}
