package me.copdead.realmscraft.spells;

import me.copdead.realmscraft.RealmsCraft;
import me.copdead.realmscraft.events.ClickBodyEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;

public class SpellEventManager implements Listener {
    private final RealmsCraft plugin;

    public SpellEventManager(RealmsCraft plugin) {
        this.plugin = plugin;
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
        if(focus == null || !focus.getType().equals(Material.WOODEN_SWORD)) return;
        if(focus.getItemMeta() == null || focus.getItemMeta().getLore() == null) return;
        if(!focus.getItemMeta().getLore().toString().contains("Spell Focus")) return;

        //Check if on cooldown
        if(((Damageable) focus.getItemMeta()).hasDamage()) return;

        //Cast the spell
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            int circle = focus.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "spellcircle"), PersistentDataType.INTEGER);
            String name = focus.getItemMeta().getDisplayName();
            SpellSelectionManager.getSpells().get(circle).get(name).castSpell(event.getPlayer());
        }
    }

    @EventHandler
    public void onSelectSpell(PlayerItemHeldEvent event) {
        //Check if spell
        ItemStack focus = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(focus == null || !focus.getType().equals(Material.WOODEN_SWORD)) return;
        if(focus.getItemMeta() == null || focus.getItemMeta().getLore() == null) return;
        if(!focus.getItemMeta().getLore().toString().contains("Spell Focus")) return;

        //get the spell objective
        String objName = focus.getItemMeta().getDisplayName().substring(0, Math.min(focus.getItemMeta().getDisplayName().length(), 16)); //objectives <16 char
        Objective objective = SpellSelectionManager.getSpellCounter().getObjective(objName);

        //no objective if one-time-use spell (fighters intuition, protection from poison)
        if(objective == null) return;

        //show remaining uses
        int uses = objective.getScore(event.getPlayer().getName()).getScore();
        if(uses < 1) return;
        TextComponent message = new TextComponent("Uses: " + uses);
        message.setItalic(true);
        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
    }

    @EventHandler
    public void onClickBody(ClickBodyEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_AIR) {
            //check for spell
            ItemStack focus = event.getPlayer().getInventory().getItemInMainHand();
            if(!focus.getType().equals(Material.WOODEN_SWORD)) return;
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

/*    @EventHandler
    public void onSpellAction(InventoryClickEvent event) {
        //check if spell
        ItemStack focus = event.getCurrentItem();
        if(focus == null || !focus.getType().equals(Material.WOODEN_SWORD)) return;
        if(focus.getItemMeta() == null || focus.getItemMeta().getLore() == null) return;
        if(!focus.getItemMeta().getLore().toString().contains("Spell Focus")) return;

        event.getWhoClicked().sendMessage(event.getAction().toString());
    }*/

    @EventHandler
    public void onDropSpell(PlayerDropItemEvent event) {
        ItemStack focus = event.getItemDrop().getItemStack();
        if(!focus.getType().equals(Material.WOODEN_SWORD)) return;
        if(focus.getItemMeta() == null || focus.getItemMeta().getLore() == null) return;
        if(!focus.getItemMeta().getLore().toString().contains("Spell Focus")) return;

        event.setCancelled(true);
    }

    //TODO: don't let players put spells in other inventories

    /*
    @EventHandler
    public void onClickSpell(InventoryClickEvent event) {
        ItemStack focus = event.getCurrentItem();
        if(focus == null || !focus.getType().equals(Material.WOODEN_SWORD)) return;
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
