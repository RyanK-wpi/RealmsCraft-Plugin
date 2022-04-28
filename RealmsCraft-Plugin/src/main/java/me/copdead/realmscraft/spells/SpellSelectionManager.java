package me.copdead.realmscraft.spells;

import me.copdead.realmscraft.death.Body;
import me.copdead.realmscraft.spells.circle1spells.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SpellSelectionManager {
    private static HashMap<String, Spell> circle0spells = new HashMap<>(); //This makes the indexes line up with real life. Also a good place to keep NPC spells
    private static HashMap<String, Spell> circle1spells = new HashMap<>();
    private static HashMap<String, Spell> circle2spells = new HashMap<>();
    private static HashMap<String, Spell> circle3spells = new HashMap<>();
    private static HashMap<String, Spell> circle4spells = new HashMap<>();
    private static HashMap<String, Spell> circle5spells = new HashMap<>();
    private static HashMap<String, Spell> circle6spells = new HashMap<>();
    private static ArrayList<HashMap<String, Spell>> spells = new ArrayList<>(Arrays.asList(
            circle0spells, circle1spells, circle2spells, circle3spells, circle4spells, circle5spells, circle6spells));

    private static HashMap<Player, Body> bodySpellList = new HashMap<>();

    private static Scoreboard spellCounter;

    public SpellSelectionManager() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        spellCounter = manager.getNewScoreboard();

        initializeSpells();
    }

    public static ArrayList<HashMap<String, Spell>> getSpells() {
        return spells;
    }

    public static HashMap<Player, Body> getBodySpellList() {
        return bodySpellList;
    }

    public static Scoreboard getSpellCounter() {
        return spellCounter;
    }

    private void initializeSpells() {
        //first circle
        new CureDisease();
        new DetectMagic();
        new DisruptLight();
        new FightersIntuition_Caster();
        new FightersIntuition_Fighter(); //0th level spell
        new GhostBlade();
        new Heartiness();
        new Identify();
        new ImmunityToPoison_MC(); //0th level spell
        new ImmunityToPoison();
        new Implement();
        new Light();
        new Pas();
        new ProtectionFromBoulder();
        new ProtectItem();
        new RepairArmor();
        new Speak();
        new SpeakWithDead();

        //second circle

        //third circle

        //fourth circle

        //fifth circle

        //sixth circle
    }
}
