package me.copdead.realmscraft_death_test.menu.menus;

import me.copdead.realmscraft_death_test.menu.Menu;
import me.copdead.realmscraft_death_test.menu.PlayerMenuUtility;
import me.copdead.realmscraft_death_test.player_class.player_classes.Caster1Path;
import me.copdead.realmscraft_death_test.player_class.player_classes.Caster2Path;
import me.copdead.realmscraft_death_test.player_class.player_classes.Caster3Path;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PathSpellSelectMenu extends Menu {
    private int path;
    private ItemStack spell;
    private ItemStack closed;
    private ItemStack confirm;
    private ItemStack back;
    private ItemStack armor;
    private boolean armored = false;

    PathSpellSelectMenu(PlayerMenuUtility pmu, int path) {
        super(pmu);
        this.path = path;
        pmu.setSpellsList(this);

        spell = new ItemStack(Material.STICK);
        ItemMeta spellMeta = spell.getItemMeta();
        spellMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Choose a Spell");
        spell.setItemMeta(spellMeta);

        closed = new ItemStack(Material.BARRIER);
        ItemMeta closedMeta = closed.getItemMeta();
        closedMeta.setDisplayName(ChatColor.DARK_RED + "SPELL UNAVAILABLE");
        ArrayList<String> closedLore = new ArrayList<>();
        closedLore.add(ChatColor.RED + "Choose a higher path");
        closedLore.add(ChatColor.RED + "or remove your armor");
        closedLore.add(ChatColor.RED + "to use this spell");
        closedMeta.setLore(closedLore);
        closed.setItemMeta(closedMeta);

        confirm = new ItemStack(Material.EMERALD);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "Confirm Spell List");
        ArrayList<String> confirmLore = new ArrayList<>();
        confirmLore.add(ChatColor.YELLOW + "Warning: You will not be able");
        confirmLore.add(ChatColor.YELLOW + "to change your spells after");
        confirmLore.add(ChatColor.YELLOW + "confirming");
        confirmMeta.setLore(confirmLore);
        confirm.setItemMeta(confirmMeta);

        back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.DARK_RED + "Back to Class Selection");
        ArrayList<String> backLore = new ArrayList<>();
        backLore.add(ChatColor.YELLOW + "Warning: Your selected spells");
        backLore.add(ChatColor.YELLOW + "will not be saved!");
        backMeta.setLore(backLore);
        back.setItemMeta(backMeta);

        armor = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta armorMeta = armor.getItemMeta();
        armorMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Add Armor");
        ArrayList<String> armorLore = new ArrayList<>();
        armorLore.add(ChatColor.AQUA + "Adds one point of armor");
        armorLore.add(ChatColor.AQUA + "But removes your top spell");
        armorLore.add(ChatColor.AQUA + "in every path.");
        armorMeta.setLore(armorLore);
        armor.setItemMeta(armorMeta);
    }

    @Override
    public String getMenuName() {
        return "Select Spells";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if(e.getCurrentItem() == null
                || e.getCurrentItem().getType() == Material.AIR
                || e.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        switch (e.getCurrentItem().getType()) {
            case BARRIER:
                if(!e.getCurrentItem().getItemMeta().getDisplayName().contains("Back")) return;

                new ClassSelectMenu(pmu).open();
                pmu.setSpellsList(null);
                break;

            case LEATHER_CHESTPLATE:
                if(e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.MENDING)) {
                    armored = false;
                    ItemMeta armorMeta = armor.getItemMeta();
                    armorMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Add Armor");
                    armorMeta.removeEnchant(Enchantment.MENDING);
                    armor.setItemMeta(armorMeta);
                    inventory.setItem(7, armor);

                    switch (path) {
                        case 3:
                            inventory.setItem(41, spell);
                        case 2:
                            inventory.setItem(49, spell);
                            inventory.setItem(39, spell);
                            break;
                    }
                    break;
                }
                armored = true;
                ItemMeta armorMeta = armor.getItemMeta();
                armorMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Remove Armor");
                armorMeta.addEnchant(Enchantment.MENDING, 1, true);
                armorMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                armor.setItemMeta(armorMeta);
                inventory.setItem(7, armor);

                switch (path) {
                    case 3:
                        inventory.setItem(41, closed);
                    case 2:
                        inventory.setItem(39, closed);
                        inventory.setItem(49, closed);
                        break;
                }
                break;

            case EMERALD:
                for(ItemStack item : inventory.getContents()) {
                    if(item == null) continue;
                    if(item.isSimilar(spell)) {
                        pmu.getOwner().sendMessage("You have unselected spells!");
                        return;
                    }
                }
                e.getWhoClicked().closeInventory();
                pmu.setSpellsList(new SpellListMenu(pmu, inventory));
                switch (path) {
                    case 1:
                        Caster1Path caster1 = new Caster1Path();
                        caster1.giveClassItems(pmu.getOwner());
                        break;
                    case 2:
                        Caster2Path caster2 = new Caster2Path();
                        caster2.setArmored(armored);
                        caster2.giveClassItems(pmu.getOwner());
                        break;
                    case 3:
                        Caster3Path caster3 = new Caster3Path();
                        caster3.setArmored(armored);
                        caster3.giveClassItems(pmu.getOwner());
                        break;
                }
                break;

            default:
                new CircleSpellSelectMenu(pmu, e.getSlot(), (int) Math.ceil(e.getSlot()/9.0), (int) Math.ceil(e.getSlot()/9.0)).open();
                break;
        }
    }

    @Override
    public void setMenuItems() {
        switch (path) {
            case 3:
                inventory.setItem(5, spell);
                inventory.setItem(14, spell);
                inventory.setItem(23, spell);
                inventory.setItem(32, spell);
                inventory.setItem(41, spell);
            case 2:
                inventory.setItem(4, spell);
                inventory.setItem(13, spell);
                inventory.setItem(22, spell);
                inventory.setItem(31, spell);
                inventory.setItem(40, spell);
                inventory.setItem(49, spell);
            case 1:
                inventory.setItem(3, spell);
                inventory.setItem(12, spell);
                inventory.setItem(21, spell);
                inventory.setItem(30, spell);
                inventory.setItem(39, spell);
        }

        if(armored) {
            switch (path) {
                case 3:
                    inventory.setItem(39, closed);
                case 2:
                    inventory.setItem(49, closed);
                    inventory.setItem(41, closed);
                    break;
            }
        }

        //set the Filler Glass
        inventory.setItem(0, FILLER_GLASS);
        inventory.setItem(1, FILLER_GLASS);
        inventory.setItem(2, FILLER_GLASS);
        //SPELLS
        inventory.setItem(6, FILLER_GLASS);
        inventory.setItem(7, FILLER_GLASS);
        inventory.setItem(8, FILLER_GLASS);

        inventory.setItem(9, FILLER_GLASS);
        inventory.setItem(10, FILLER_GLASS);
        inventory.setItem(11, FILLER_GLASS);
        //SPELLS
        inventory.setItem(15, FILLER_GLASS);
        inventory.setItem(16, FILLER_GLASS);
        inventory.setItem(17, FILLER_GLASS);

        inventory.setItem(18, FILLER_GLASS);
        inventory.setItem(19, FILLER_GLASS);
        inventory.setItem(20, FILLER_GLASS);
        //SPELLS
        inventory.setItem(24, FILLER_GLASS);
        inventory.setItem(25, FILLER_GLASS);
        inventory.setItem(26, FILLER_GLASS);

        inventory.setItem(27, FILLER_GLASS);
        inventory.setItem(28, FILLER_GLASS);
        inventory.setItem(29, FILLER_GLASS);
        //SPELLS
        inventory.setItem(33, FILLER_GLASS);
        inventory.setItem(34, FILLER_GLASS);
        inventory.setItem(35, FILLER_GLASS);

        inventory.setItem(36, FILLER_GLASS);
        inventory.setItem(37, FILLER_GLASS);
        inventory.setItem(38, FILLER_GLASS);
        //SPELLS
        inventory.setItem(42, FILLER_GLASS);
        inventory.setItem(43, FILLER_GLASS);
        inventory.setItem(44, FILLER_GLASS);

        inventory.setItem(45, FILLER_GLASS);
        inventory.setItem(46, back);
        inventory.setItem(47, FILLER_GLASS);
        //SPELLS
        inventory.setItem(51, FILLER_GLASS);
        inventory.setItem(52, confirm);
        inventory.setItem(53, FILLER_GLASS);

        if(path > 1) {
            inventory.setItem(7, armor);
        }
    }
}
