package me.copdead.realmscraft.spells;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class eSpellEventManager implements Listener {

    @EventHandler
    public static void onUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        ItemStack focus = event.getItem();
        if(focus != null && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)) {
            String focusData = focus.getItemMeta().toString().toLowerCase();
            if(focusData.contains("circle_1")) {
                if(focus.isSimilar(SpellItemManager.cure_disease_focus)) Spells.cure_disease(p, focus);
                else if(focus.isSimilar(SpellItemManager.detect_magic_focus)) Spells.detect_magic(p, focus);
                else if(focus.isSimilar(SpellItemManager.disrupt_light_focus)) Spells.disrupt_light(p, focus);
                else if(focus.isSimilar(SpellItemManager.fighters_intuition_caster_focus)) Spells.fighter_intuition_caster(p, focus);
                else if(focus.isSimilar(SpellItemManager.fighters_intuition_fighter_focus)) Spells.fighter_intuition_fighter(p, focus);
                else if(focus.isSimilar(SpellItemManager.ghost_blade_focus)) Spells.ghost_blade(p, focus);
                else if(focus.isSimilar(SpellItemManager.identify_focus)) Spells.identify(p, focus);
                else if(focus.isSimilar(SpellItemManager.immunity_to_poison_caster_focus)) Spells.immuneToPoisonCaster(p, focus);
            } else if(focusData.contains("circle_2")) {
                //Second Circle Spells
            } else if(focusData.contains("circle_3")) {
                //Third Circle Spells
            } else if(focusData.contains("circle_4")) {
                //Fourth Circle Spells
            } else if(focusData.contains("circle_5")) {
                //Fifth Circle Spells
            } else if(focusData.contains("circle_6")) {
                //Sixth Circle Spells
            }
        }
    }

    @EventHandler
    public static void onPoisoned(EntityPotionEffectEvent event) {
        if(event.getNewEffect().getType() != PotionEffectType.POISON) return;
        if(!(event.getEntity() instanceof Player)) return;

        Inventory inv = ((Player) event.getEntity()).getInventory();
        for(int i=0; i < inv.getSize(); i++) {
            if(inv.getItem(i) != null) {
                if(inv.getItem(i).isSimilar(SpellItemManager.immunity_to_poison_user_focus)) {
                    event.setCancelled(true);
                    inv.remove(SpellItemManager.immunity_to_poison_user_focus);
                    return;
                }
            }
        }
    }
}
