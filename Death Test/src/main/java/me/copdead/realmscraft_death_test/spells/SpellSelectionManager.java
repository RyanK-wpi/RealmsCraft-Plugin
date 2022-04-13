package me.copdead.realmscraft_death_test.spells;

import me.copdead.realmscraft_death_test.spells.circle1spells.CureDisease;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SpellSelectionManager {
    private static HashMap<ItemStack, Spell> circle0spells = new HashMap<>(); //This makes the indexes line up with real life. Also a good place to keep NPC spells
    private static HashMap<ItemStack, Spell> circle1spells = new HashMap<>();
    private static HashMap<ItemStack, Spell> circle2spells = new HashMap<>();
    private static HashMap<ItemStack, Spell> circle3spells = new HashMap<>();
    private static HashMap<ItemStack, Spell> circle4spells = new HashMap<>();
    private static HashMap<ItemStack, Spell> circle5spells = new HashMap<>();
    private static HashMap<ItemStack, Spell> circle6spells = new HashMap<>();
    private static ArrayList<HashMap<ItemStack, Spell>> spells = new ArrayList<>(Arrays.asList(
            circle0spells, circle1spells, circle2spells, circle3spells, circle4spells, circle5spells, circle6spells));

    public SpellSelectionManager() {
        initializeSpells();
    }

    public static ArrayList<HashMap<ItemStack, Spell>> getSpells() {
        return spells;
    }

    private void initializeSpells() {
        //first circle
        new CureDisease();

        //second circle

        //third circle

        //fourth circle

        //fifth circle

        //sixth circle
    }
}
