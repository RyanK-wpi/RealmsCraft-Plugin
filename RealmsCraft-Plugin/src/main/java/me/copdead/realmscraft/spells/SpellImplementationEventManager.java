package me.copdead.realmscraft.spells;

import me.copdead.realmscraft.RealmsCraft;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SpellImplementationEventManager implements Listener {
    private final RealmsCraft plugin;

    public SpellImplementationEventManager(RealmsCraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPoisoned(EntityPotionEffectEvent event) {
        if(event.getNewEffect() == null) return;
        event.getEntity().sendMessage(event.getNewEffect().getType().toString());
        if(!event.getNewEffect().getType().equals(PotionEffectType.POISON)) return;
        if(!(event.getEntity() instanceof Player)) return;

        Inventory inv = ((Player) event.getEntity()).getInventory();
        for(int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if(item == null || !item.getType().equals(Material.WOODEN_SWORD)) continue;
            if(item.getItemMeta() == null || item.getItemMeta().getLore() == null) continue;
            if(!item.getItemMeta().getLore().toString().contains("Spell Focus")) continue;
            if(!item.getItemMeta().getDisplayName().contains("Immunity to Poison")) continue;

            NamespacedKey key = new NamespacedKey(plugin, "spellcircle");
            if(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER) != 0) continue;
            event.getEntity().sendMessage("have a key");

            event.setCancelled(true);
            inv.setItem(i, null);
            return;
        }
    }

    @EventHandler
    public void onMobPas(EntityTargetEvent event) {
        if(!(event.getTarget() instanceof Player)) return;

        NamespacedKey key = new NamespacedKey(plugin, "pas");
        if(!event.getEntity().getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;

        String name = event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if(event.getTarget().getName().equals(name)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPas(EntityDamageByEntityEvent event) {
        NamespacedKey key = new NamespacedKey(plugin, "pas");
        if(!event.getDamager().getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;

        String name = event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if(event.getEntity().getName().equals(name)) event.setCancelled(true);
    }

    @EventHandler
    public void onHitPas(EntityDamageByEntityEvent event) {
        NamespacedKey key = new NamespacedKey(plugin, "pas");

        //if the damaged entity has pas casted on it, remove the cast
        if(!event.getEntity().getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;
        event.getEntity().getPersistentDataContainer().remove(key);
    }

    private static Map<Player, BiConsumer<Player, String>> handlers = new HashMap<>();

    public static void registerHandler(Player player, BiConsumer<Player, String> consumer) {
        handlers.put(player, consumer);
    }

    @EventHandler
    public void onSpeakWithDead(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(handlers.containsKey(player)) {
            handlers.get(player).accept(player, event.getMessage());
            handlers.remove(player);
            event.setCancelled(true);
        }
    }
}
