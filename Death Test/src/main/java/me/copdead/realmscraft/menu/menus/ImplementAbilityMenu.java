package me.copdead.realmscraft.menu.menus;

import me.copdead.realmscraft.menu.Menu;
import me.copdead.realmscraft.menu.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImplementAbilityMenu extends Menu {
    private int points;

    private ItemStack pts;
    private ItemStack confirm;
    private ItemStack back;
    private ItemStack blocked;

    public ImplementAbilityMenu(PlayerMenuUtility pmu, int points) {
        super(pmu);
        this.points = points;

        pts = new ItemStack(Material.NETHER_STAR, points);
        ItemMeta ptsMeta = pts.getItemMeta();
        ptsMeta.setDisplayName(ChatColor.GOLD + "Points Remaining:");
        ArrayList<String> ptsLore = new ArrayList<>();
        ptsLore.add(ChatColor.AQUA + "Use Points to choose");
        ptsLore.add(ChatColor.AQUA + "abilities");
        ptsMeta.setLore(ptsLore);
        pts.setItemMeta(ptsMeta);

        confirm = new ItemStack(Material.EMERALD);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "Confirm Implement");
        ArrayList<String> confirmLore = new ArrayList<>();
        confirmLore.add(ChatColor.YELLOW + "Warning: You will not be able");
        confirmLore.add(ChatColor.YELLOW + "to change your abilities after");
        confirmLore.add(ChatColor.YELLOW + "confirming");
        confirmMeta.setLore(confirmLore);
        confirm.setItemMeta(confirmMeta);

        back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.DARK_RED + "Back");
        ArrayList<String> backLore = new ArrayList<>();
        backLore.add(ChatColor.YELLOW + "Warning: Your selected");
        backLore.add(ChatColor.YELLOW + "abilities will not be saved!");
        backMeta.setLore(backLore);
        back.setItemMeta(backMeta);

        blocked = new ItemStack(Material.FIREWORK_STAR);
        ItemMeta blockedMeta = blocked.getItemMeta();
        blockedMeta.setDisplayName(ChatColor.DARK_RED + "Ability Unavailable");
        ArrayList<String> blockedLore = new ArrayList<>();
        blockedLore.add(ChatColor.RED + "Get more points to");
        blockedLore.add(ChatColor.RED + "use this ability");
        blockedMeta.setLore(blockedLore);
        blocked.setItemMeta(blockedMeta);
    }

    @Override
    public String getMenuName() {
        return "Implement Selection";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        ItemMeta meta = item.getItemMeta();

        switch (item.getType()) {
            case BARRIER:
                pmu.getOwner().closeInventory();
                break;
            case WOODEN_SWORD:
                if(item.getItemMeta().hasEnchant(Enchantment.MENDING)) {
                    item.setAmount(item.getAmount() + 1);
                    pointUpdater(e.getSlot());
                    break;
                }
                meta.addEnchant(Enchantment.MENDING, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
                pointUpdater(e.getSlot());
                break;
            case PLAYER_HEAD:
                if(item.getItemMeta().hasEnchant(Enchantment.MENDING)) break;
                meta.addEnchant(Enchantment.MENDING, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
                pointUpdater(e.getSlot());
                break;
            case EMERALD:
                //check for points? eh.
                ArrayList<ItemStack> choices = new ArrayList<>();
                for(ItemStack selectedItem : inventory) {
                    if(selectedItem == null) continue;
                    if(selectedItem.getType().equals(FILLER_GLASS.getType())) continue;
                    if(selectedItem.getType().equals(blocked.getType())) continue;

                    if(!selectedItem.getItemMeta().hasEnchant(Enchantment.MENDING)) continue;
                    choices.add(selectedItem);
                }
                new ImplementItemMenu(pmu, choices).open();
                break;
        }
        //click to enchant (signals use)
        //click multiple times to buy again (SPELLS ONLY)

        //confirm: Send choices to another menu to pick item shape. Put info into the persistent data storage and give item.

    }

    private void pointUpdater(int slot) {
        if(slot > 45) points = points - 1;
        if(slot > 26) points = points - 1;
        points = points - 1;

        pts.setAmount(points);

        if(points < 3) {
            for(int i = 46; i <= 53; i++) {
                if(inventory.getItem(i) == null) continue;
                if(inventory.getItem(i).getItemMeta().hasEnchant(Enchantment.MENDING)) continue;

                inventory.setItem(i, blocked);
            }
        }
        if(points < 2) {
            for(int i = 27; i <= 35; i++) {
                if(inventory.getItem(i) == null) continue;
                if(inventory.getItem(i).getItemMeta().hasEnchant(Enchantment.MENDING)) continue;

                inventory.setItem(i, blocked);
            }
        }
        if(points < 1) {
            for(int i = 9; i <= 22; i++) {
                if(inventory.getItem(i) == null) continue;
                if(inventory.getItem(i).getType().equals(FILLER_GLASS.getType())) continue;
                if(inventory.getItem(i).getItemMeta().hasEnchant(Enchantment.MENDING)) continue;

                inventory.setItem(i, blocked);
            }
        }
    }

    @Override
    public void setMenuItems() {
        ArrayList<List<ItemStack>> spells = getUpgradableSpells();

        ItemStack p11 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta p11Meta = p11.getItemMeta();
        p11Meta.setDisplayName(ChatColor.BLUE + "Enhanced Circle");
        ArrayList<String> p11Lore = new ArrayList<>();
        p11Lore.add(ChatColor.YELLOW + "When using a Circle spell you may");
        p11Lore.add(ChatColor.YELLOW + "double the length of the rope.");
        p11Lore.add(ChatColor.YELLOW + "This may only be done once.");
        p11Lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Costs 1 Point");
        p11Meta.setLore(p11Lore);
        p11.setItemMeta(p11Meta);

        ItemStack p12 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta p12Meta = p12.getItemMeta();
        p11Meta.setDisplayName(ChatColor.BLUE + "EZ Deathwatch");
        ArrayList<String> p12Lore = new ArrayList<>();
        p12Lore.add(ChatColor.YELLOW + "Deathwatch no longer ends");
        p12Lore.add(ChatColor.YELLOW + "when you are raised.");
        p12Lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Costs 1 Point");
        p12Meta.setLore(p12Lore);
        p12.setItemMeta(p12Meta);

        ItemStack p13 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta p13Meta = p13.getItemMeta();
        p13Meta.setDisplayName(ChatColor.BLUE + "Deathwatch Head Turn");
        ArrayList<String> p13Lore = new ArrayList<>();
        p13Lore.add(ChatColor.YELLOW + "Death Watch allows you to move");
        p13Lore.add(ChatColor.YELLOW + "your head while dead.");
        p13Lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Costs 1 Point");
        p13Meta.setLore(p13Lore);
        p13.setItemMeta(p13Meta);

        ItemStack p21 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta p21Meta = p21.getItemMeta();
        p21Meta.setDisplayName(ChatColor.BLUE + "Enhanced Speak");
        ArrayList<String> p21Lore = new ArrayList<>();
        p21Lore.add(ChatColor.YELLOW + "The uses for Speak");
        p21Lore.add(ChatColor.YELLOW + "become unlimited.");
        p21Lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Costs 2 Points");
        p21Meta.setLore(p21Lore);
        p21.setItemMeta(p21Meta);

        ItemStack p22 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta p22Meta = p22.getItemMeta();
        p22Meta.setDisplayName(ChatColor.BLUE + "Enhanced Familiar");
        ArrayList<String> p22Lore = new ArrayList<>();
        p22Lore.add(ChatColor.YELLOW + "Gain 1 point to spend");
        p22Lore.add(ChatColor.YELLOW + "on your familiar.");
        p22Lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Costs 2 Points");
        p22Meta.setLore(p22Lore);
        p22.setItemMeta(p22Meta);

        ItemStack p31 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta p31Meta = p31.getItemMeta();
        p31Meta.setDisplayName(ChatColor.BLUE + "Regeneration");
        ArrayList<String> p31Lore = new ArrayList<>();
        p31Lore.add(ChatColor.YELLOW + "Gain one use of Regeneration.");
        p31Lore.add(ChatColor.YELLOW + "You do not need to know the");
        p31Lore.add(ChatColor.YELLOW + "spell to use this ability.");
        p31Lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Costs 3 Points");
        p31Meta.setLore(p31Lore);
        p31.setItemMeta(p31Meta);

        ItemStack p32 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta p32Meta = p32.getItemMeta();
        p32Meta.setDisplayName(ChatColor.BLUE + "Regenerate Undeath");
        ArrayList<String> p32Lore = new ArrayList<>();
        p32Lore.add(ChatColor.YELLOW + "Gain one use of Regeneration.");
        p32Lore.add(ChatColor.YELLOW + "You will be raised as a free-");
        p32Lore.add(ChatColor.YELLOW + "willed undead. You do not need to");
        p32Lore.add(ChatColor.YELLOW + "know the spell to use this ability.");
        p32Lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Costs 3 Points");
        p32Meta.setLore(p32Lore);
        p32.setItemMeta(p32Meta);

        ItemStack p33 = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta p33Meta = p33.getItemMeta();
        p33Meta.setDisplayName(ChatColor.BLUE + "Enhanced Magic Missile");
        ArrayList<String> p33Lore = new ArrayList<>();
        p33Lore.add(ChatColor.YELLOW + "Gain one additional");
        p33Lore.add(ChatColor.YELLOW + "Magic Missile.");
        p33Lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Costs 3 Points");
        p33Meta.setLore(p33Lore);
        p33.setItemMeta(p33Meta);

        //First Row
        inventory.setItem(0, FILLER_GLASS); inventory.setItem(1, back);     inventory.setItem(2, FILLER_GLASS);
        inventory.setItem(3, FILLER_GLASS); inventory.setItem(4, pts);      inventory.setItem(5, FILLER_GLASS);
        inventory.setItem(6, FILLER_GLASS); inventory.setItem(7, confirm);  inventory.setItem(8, FILLER_GLASS);

        //Second Row
        //1 point spells (11)
        //1 point abilities (3)
        //filter in spells from inventory to slots 9 - 14. If they spill over, filter into 18 - 22
        int index = 9;
        for(ItemStack spell : spells.get(0)) {
            inventory.setItem(index, spell);
            index++;
            if(index >= 15) index = 18;
        }

        inventory.setItem(15, p11); inventory.setItem(16, p12); inventory.setItem(17, p13);

        //Third Row
        inventory.setItem(18, FILLER_GLASS); inventory.setItem(19, FILLER_GLASS); inventory.setItem(20, FILLER_GLASS);
        inventory.setItem(21, FILLER_GLASS); inventory.setItem(22, FILLER_GLASS); inventory.setItem(23, FILLER_GLASS);
        inventory.setItem(24, FILLER_GLASS); inventory.setItem(25, FILLER_GLASS); inventory.setItem(26, FILLER_GLASS);

        //Fourth Row
        //2 point spells (5)
        //2 point abilities (2)
        inventory.setItem(33, blocked); inventory.setItem(34, blocked); inventory.setItem(35, blocked);
        if(points >= 2) {
            index = 27;
            for(ItemStack spell : spells.get(1)) {
                inventory.setItem(index, spell);
                index++;
            }

            inventory.setItem(33, p11); inventory.setItem(34, p12); inventory.setItem(35, p13);
        }

        //Fifth Row
        inventory.setItem(36, FILLER_GLASS); inventory.setItem(37, FILLER_GLASS); inventory.setItem(38, FILLER_GLASS);
        inventory.setItem(39, FILLER_GLASS); inventory.setItem(40, FILLER_GLASS); inventory.setItem(41, FILLER_GLASS);
        inventory.setItem(42, FILLER_GLASS); inventory.setItem(43, FILLER_GLASS); inventory.setItem(44, FILLER_GLASS);

        //Sixth Row
        //3 point spells (2)
        //3 point abilities (3)
        inventory.setItem(51, blocked); inventory.setItem(52, blocked); inventory.setItem(53, blocked);
        if(points >= 3) {
            index = 45;
            for(ItemStack spell : spells.get(2)) {
                inventory.setItem(index, spell);
                index++;
            }

            inventory.setItem(51, p31); inventory.setItem(52, p32); inventory.setItem(53, p33);
        }

        //Ability Items (if have not enough points, make firework core)
    }

    private ArrayList<List<ItemStack>> getUpgradableSpells() {
        List<ItemStack> onePointSpell = new ArrayList<>();
        List<ItemStack> twoPointSpell = new ArrayList<>();
        List<ItemStack> threePointSpell = new ArrayList<>();

        Inventory inv = pmu.getOwner().getInventory();
        for(int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if(item == null || !item.getType().equals(Material.WOODEN_SWORD)) continue;
            if(item.getItemMeta() == null || item.getItemMeta().getLore() == null) continue;
            if(!item.getItemMeta().getLore().toString().contains("Spell Focus")) continue;

            String name = item.getItemMeta().getDisplayName();
            switch (name) {
                case "Find the Path": case "Fortune Tell": case "Guidance": case "Precognition": case "Skew Divination":
                case "Raise Dead": case "Deep Pockets": case "Enfeeble Being": case "Beckon Corpse": case "Disenchant":
                case "Disrupt":
                    onePointSpell.add(item);
                    break;
                case "Call the Soul": case "Group Healing": case "Resist Magic": case "Animate Undead": case "Soul Bane":
                    twoPointSpell.add(item);
                    break;
                case "Vision": case "Seance":
                    threePointSpell.add(item);
                    break;
            }
        }

        return new ArrayList<>(Arrays.asList(onePointSpell, twoPointSpell, threePointSpell));
    }
}
