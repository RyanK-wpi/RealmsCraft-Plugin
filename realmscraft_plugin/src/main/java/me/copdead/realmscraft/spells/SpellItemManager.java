package me.copdead.realmscraft.spells;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class SpellItemManager {

    //*********************
    //       SPELLS
    //*********************
    //First Circle Spells
    public static ItemStack cure_disease_focus;
    public static ItemStack detect_magic_focus;
    public static ItemStack disrupt_light_focus;
    public static ItemStack fighters_intuition_caster_focus;
    public static ItemStack fighters_intuition_fighter_focus;
    public static ItemStack ghost_blade_focus;
    public static ItemStack heartiness_focus;
    public static ItemStack identify_focus;
    public static ItemStack immunity_to_poison_caster_focus;
    public static ItemStack immunity_to_poison_user_focus;

    public static ItemStack light_focus;
    public static ItemStack death_watch_focus;

    public static ItemStack regeneration_focus;

    //*********************
    //      COOLDOWNS
    //*********************
    //First Circle Spells
    static ItemStack cure_disease_cd;
    static ItemStack detect_magic_cd;
    static ItemStack disrupt_light_cd;
    static ItemStack fighters_intuition_cd;
    static ItemStack ghost_blade_cd;
    //heartiness has no cooldown
    static ItemStack identify_cd;

    static ItemStack light_focus_cd;

    public static void init() {
        //*********************
        //       SPELLS
        //*********************
        //First Circle Spells
        cure_disease_focus = createSpellFocus("Cure Disease", 5, 101);
        detect_magic_focus = createSpellFocus("Detect Magic", 5, 102);
        disrupt_light_focus = createSpellFocus("Disrupt Light", 5, 103);
        fighters_intuition_caster_focus = createSpellFocus("Fighter's Intuition", 3, 104);
        fighters_intuition_fighter_focus = createSpellFocus("Fighter's Intuition", 1, 104);
        ghost_blade_focus = createSpellFocus("Ghost Blade", 2, 105);
        heartiness_focus = createSpellFocus("Heartiness", 1, 106);
        identify_focus = createSpellFocus("Identify", 3, 107);

        regeneration_focus = createSpellFocus("Regeneration", 1, 500);

        //*********************
        //      COOLDOWNS
        //*********************
        //First Circle Spells
        cure_disease_cd = createCooldown("Cure Disease on Cooldown");
        detect_magic_cd = createCooldown("Detect Magic on Cooldown");


    }

    private static ItemStack createSpellFocus(String name, int amount, int customModelData) {
        ItemStack spellFocus = new ItemStack(Material.STICK, amount);
        ItemMeta meta = spellFocus.getItemMeta();
        meta.setCustomModelData(customModelData);
        meta.setDisplayName(name);
        meta.setLore(Collections.singletonList("Spell Focus"));
        meta.addItemFlags(ItemFlag.valueOf("circle_1"));
        spellFocus.setItemMeta(meta);

        return spellFocus;
    }

    private static ItemStack createCooldown(String name) {
        ItemStack cooldown = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = cooldown.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName(name);
        meta.setLore(Collections.singletonList("Spell Focus"));
        cooldown.setItemMeta(meta);

        return cooldown;
    }

}
