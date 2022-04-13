package me.copdead.realmscraft_death_test.spells;

import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class SpellEventManager implements Listener {
    private final Realmscraft_death_test plugin;

    public SpellEventManager(Realmscraft_death_test plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //if(!SpellSelectionManager.getSpellcasters().containsKey(event.getPlayer())) return;
        //give spellbook selection
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if(event.getItem() != null && event.getItem().isSimilar(new SpellBook().getBook())) {
            new SpellBook().useBook(event.getPlayer());
        }

        if(event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack focus = event.getItem();
        if(focus == null || !focus.getType().equals(Material.STICK)) return;
        if(focus.getItemMeta() == null || focus.getItemMeta().getLore() == null) return;
        if(!focus.getItemMeta().getLore().toString().equalsIgnoreCase("spell focus")) return;

        int circle = focus.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "spellcircle"), PersistentDataType.INTEGER);
        SpellSelectionManager.getSpells().get(circle).get(focus).castSpell(event.getPlayer());
    }

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
    }
}
