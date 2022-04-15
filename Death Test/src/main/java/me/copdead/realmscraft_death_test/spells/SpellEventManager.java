package me.copdead.realmscraft_death_test.spells;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import me.copdead.realmscraft_death_test.death.DeathBodyManager;
import me.copdead.realmscraft_death_test.events.ClickBodyEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class SpellEventManager implements Listener {
    private final Realmscraft_death_test plugin;
    private final DeathBodyManager bodyManager;

    public SpellEventManager(Realmscraft_death_test plugin) {
        this.plugin = plugin;
        this.bodyManager = plugin.getBodyManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //if(!SpellSelectionManager.getSpellcasters().containsKey(event.getPlayer())) return;
        //give spellbook selection
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        //only run once
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;

        //SPELLBOOK BAD - MOVE THIS TO A DIFFERENT EVENT MANAGER
        if(event.getItem() != null && event.getItem().isSimilar(new SpellBook().getBook())) {
            new SpellBook().useBook(event.getPlayer());
        }

        //Check if spell
        ItemStack focus = event.getItem();
        if(focus == null || !focus.getType().equals(Material.STICK)) return;
        if(focus.getItemMeta() == null || focus.getItemMeta().getLore() == null) return;
        if(!focus.getItemMeta().getLore().toString().contains("Spell Focus")) return;

        //Cast the spell
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            int circle = focus.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "spellcircle"), PersistentDataType.INTEGER);
            String name = focus.getItemMeta().getDisplayName();
            SpellSelectionManager.getSpells().get(circle).get(name).castSpell(event.getPlayer());
        }
    }

    @EventHandler
    public void onClickBody(ClickBodyEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR) {
            //check for spell
            ItemStack focus = event.getPlayer().getInventory().getItemInMainHand();
            if(!focus.getType().equals(Material.STICK)) return;
            if(focus.getItemMeta() == null || focus.getItemMeta().getLore() == null) return;
            if(!focus.getItemMeta().getLore().toString().contains("Spell Focus")) return;

            //make sure in range
            if(event.getPlayer().getLocation().distanceSquared(event.getBody().getCorpse().getBukkitEntity().getLocation()) > 4) return;

            //add body to list
            SpellSelectionManager.getBodySpellList().put(event.getPlayer(), event.getBody());

            //if not dealt with by spell, remove from list
            new BukkitRunnable() {
                @Override
                public void run() {
                    SpellSelectionManager.getBodySpellList().remove(event.getPlayer());
                }
            }.runTaskLater(plugin, 1);
        }
    }

    @EventHandler
    public void onDropSpell(InventoryClickEvent event) {
        //check if spell
        ItemStack focus = event.getCurrentItem();
        if(focus == null || !focus.getType().equals(Material.STICK)) return;
        if(focus.getItemMeta() == null || focus.getItemMeta().getLore() == null) return;
        if(!focus.getItemMeta().getLore().toString().contains("Spell Focus")) return;

        event.getWhoClicked().sendMessage(event.getAction().toString());
    }

    /*
    @EventHandler
    public void onClickSpell(InventoryClickEvent event) {
        ItemStack focus = event.getCurrentItem();
        if(focus == null || !focus.getType().equals(Material.STICK)) return;
        if(focus.getItemMeta() == null || focus.getItemMeta().getLore() == null) return;
        if(!focus.getItemMeta().getLore().toString().equalsIgnoreCase("spell focus")) return;

        //Dont let players break up stacks of the spell
        InventoryAction a = event.getAction();
        if(a == InventoryAction.DROP_ONE_CURSOR || a == InventoryAction.DROP_ONE_SLOT || a == InventoryAction.MOVE_TO_OTHER_INVENTORY
                || a == InventoryAction.PICKUP_HALF || a == InventoryAction.PICKUP_ONE || a == InventoryAction.PICKUP_SOME
                || a == InventoryAction.PLACE_ONE || a == InventoryAction.PLACE_SOME) {
            event.setCancelled(true);
        }
    }*/
}
