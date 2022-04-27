package me.copdead.realmscraft.menu.menus;

import me.copdead.realmscraft.RealmsCraft;
import me.copdead.realmscraft.menu.Menu;
import me.copdead.realmscraft.menu.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class ImplementItemMenu extends Menu {
    private static final RealmsCraft plugin = (RealmsCraft) Bukkit.getPluginManager().getPlugin("Realmscraft_death_test");
    private ArrayList<ItemStack> choices;

    public ImplementItemMenu(PlayerMenuUtility pmu, ArrayList<ItemStack> choices) {
        super(pmu);
        this.choices = choices;
    }

    @Override
    public String getMenuName() {
        return "Choose Implement Type";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;
        giveImplement(((Player) e.getWhoClicked()), e.getCurrentItem().getType());
    }

    private void giveImplement(Player caster, Material material) {
        ItemStack implement = new ItemStack(material);
        ItemMeta iMeta = implement.getItemMeta();
        iMeta.setDisplayName("Implement");
        ArrayList<String> iLore = new ArrayList<>();
        PersistentDataContainer container = iMeta.getPersistentDataContainer();
        for(ItemStack spell : choices) {
            if(!spell.getItemMeta().getLore().toString().contains("Spell Focus")) {
                NamespacedKey key = new NamespacedKey(plugin, spell.getItemMeta().getDisplayName());
                container.set(key, PersistentDataType.INTEGER, spell.getAmount());

                iLore.add("Extra " + spell.getItemMeta().getDisplayName()+": "+spell.getAmount());
            } else {
                iLore.add(spell.getItemMeta().getDisplayName());
            }
        }
        iMeta.setLore(iLore);
        implement.setItemMeta(iMeta);

        caster.getInventory().addItem(implement);
    }

    @Override
    public void setMenuItems() {
        //(Staff, wand, orb, book)
        ItemStack focus = new ItemStack(Material.STICK);
        ItemMeta fMeta = focus.getItemMeta();
        fMeta.setDisplayName(ChatColor.GOLD + "Staff");
        fMeta.setCustomModelData(1);
        ArrayList<String> fLore = new ArrayList<>();
        fLore.add("Implement type has no");
        fLore.add("effect on gameplay");
        fMeta.setLore(fLore);
        focus.setItemMeta(fMeta);

        inventory.setItem(2, focus);

        fMeta.setDisplayName(ChatColor.GOLD + "Wand");
        fMeta.setCustomModelData(2);
        focus.setItemMeta(fMeta);

        inventory.setItem(3, focus);

        fMeta.setDisplayName(ChatColor.GOLD + "Orb");
        fMeta.setCustomModelData(3);
        focus.setItemMeta(fMeta);

        inventory.setItem(5, focus);

        fMeta.setDisplayName(ChatColor.GOLD + "Book");
        fMeta.setCustomModelData(4);
        focus.setItemMeta(fMeta);

        inventory.setItem(6, focus);
    }
}
