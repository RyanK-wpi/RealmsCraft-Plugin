package me.copdead.realmscraft_death_test.menu.menus;

import me.copdead.realmscraft_death_test.menu.Menu;
import me.copdead.realmscraft_death_test.menu.PlayerMenuUtility;
import me.copdead.realmscraft_death_test.spells.Spell;
import me.copdead.realmscraft_death_test.spells.SpellSelectionManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class CircleSpellSelectMenu extends Menu {
    private int itemSlotToReplace;
    private int spellsLevel;
    private int spellsSelect;

    CircleSpellSelectMenu(PlayerMenuUtility pmu, int itemSlotToReplace, int spellsLevel, int spellsSelect) {
        super(pmu);
        this.itemSlotToReplace = itemSlotToReplace;
        this.spellsLevel = spellsLevel;
        this.spellsSelect = spellsSelect;
    }

    @Override
    public String getMenuName() {
        return "Select Spell";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        //selected a spell
        if(e.getSlot() > 8) {
            if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

            pmu.getSpellsList().getInventory().setItem(itemSlotToReplace, e.getCurrentItem());
            pmu.getSpellsList().open();
        }

        //selected the menu bar
        switch (e.getCurrentItem().getType()) {
            case ENCHANTED_BOOK: case GRAY_STAINED_GLASS_PANE:
                break;
            case BARRIER:
                pmu.getSpellsList().open();
                break;
            case BOOK:
                new CircleSpellSelectMenu(pmu, itemSlotToReplace, spellsLevel, e.getSlot()).open();
        }

    }

    @Override
    public void setMenuItems() {
        //top boarder
        inventory.setItem(0, FILLER_GLASS);
        inventory.setItem(2, FILLER_GLASS);
        inventory.setItem(3, FILLER_GLASS);
        inventory.setItem(4, FILLER_GLASS);
        inventory.setItem(5, FILLER_GLASS);
        inventory.setItem(6, FILLER_GLASS);
        inventory.setItem(8, FILLER_GLASS);

        //back button
        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Back to spell list");
        ArrayList<String> backLore = new ArrayList<>();
        backLore.add(ChatColor.AQUA + "Returns you to");
        backLore.add(ChatColor.AQUA + "your spell list");
        backMeta.setLore(backLore);
        back.setItemMeta(backMeta);

        inventory.setItem(7, back);

        //Spell circle selectors
        ItemStack circleAvailable = new ItemStack(Material.BOOK, 6);
        ItemMeta circleMeta = circleAvailable.getItemMeta();
        circleMeta.setDisplayName(ChatColor.DARK_PURPLE + "Select a Sixth Circle Spell");
        circleAvailable.setItemMeta(circleMeta);

        switch (spellsLevel) {
            case 6:
                inventory.setItem(6, circleAvailable);
            case 5:
                circleAvailable.setAmount(5);
                circleMeta.setDisplayName(ChatColor.DARK_PURPLE + "Select a Fifth Circle Spell");
                circleAvailable.setItemMeta(circleMeta);

                inventory.setItem(5, circleAvailable);
            case 4:
                circleAvailable.setAmount(4);
                circleMeta.setDisplayName(ChatColor.DARK_PURPLE + "Select a Fourth Circle Spell");
                circleAvailable.setItemMeta(circleMeta);

                inventory.setItem(4, circleAvailable);
            case 3:
                circleAvailable.setAmount(3);
                circleMeta.setDisplayName(ChatColor.DARK_PURPLE + "Select a Third Circle Spell");
                circleAvailable.setItemMeta(circleMeta);

                inventory.setItem(3, circleAvailable);
            case 2:
                circleAvailable.setAmount(2);
                circleMeta.setDisplayName(ChatColor.DARK_PURPLE + "Select a Second Circle Spell");
                circleAvailable.setItemMeta(circleMeta);

                inventory.setItem(2, circleAvailable);
            case 1:
                circleAvailable.setAmount(1);
                circleMeta.setDisplayName(ChatColor.DARK_PURPLE + "Select a First Circle Spell");
                circleAvailable.setItemMeta(circleMeta);

                inventory.setItem(1, circleAvailable);
                break;
        }

        //current circle selector
        inventory.getItem(spellsSelect).setType(Material.ENCHANTED_BOOK);

        //filter in spells
        HashMap<String, Spell> spellList = SpellSelectionManager.getSpells().get(spellsSelect);
        int i = 0;
        for(Spell spell : spellList.values()) {
            inventory.setItem(9 + i, spell.getSpell());
            i++;
        }
    }
}
