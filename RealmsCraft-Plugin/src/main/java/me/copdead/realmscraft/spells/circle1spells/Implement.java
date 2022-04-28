package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.menu.MenuManager;
import me.copdead.realmscraft.menu.menus.ImplementAbilityMenu;
import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.SpellSelectionManager;
import org.bukkit.entity.Player;

public class Implement extends Spell {
    public Implement() {
        super("Implement", 1, 1, 109);
    }

    @Override
    public void spellEffect(Player caster) {
        int uses = SpellSelectionManager.getSpellCounter().getObjective(getName()).getScore(caster.getName()).getScore();
        if(uses > 5) uses = 5;
        new ImplementAbilityMenu(MenuManager.getPlayerMenuUtility(caster), uses).open();
        //on first use, open a menu that gives options based on spellList and "uses"; (5 USE MAX)
        //store effected spell info in item? Other spells have "if implement in offHand, obey implement rules"
        //cooldown = disenchant (probably handled in disenchant)
        //Change it to a visible item??? (Staff, wand, orb, book)
    }

    @Override
    public String getDescription() {
        return "The spellcaster is able to create a staff, wand, orb, or book (hereafter called “implement”) that " +
                "enhances their own spells. Each time the spellcaster learns this spell, they gain 1 point into a pool " +
                "from which they may purchase special abilities from the choices below. A spellcaster may only have 5 " +
                "points worth of implements per event. At the magic check-in of an event, the spellcaster may choose how " +
                "the points in their pool are spent.\n" +
                "\n" +
                "Unless otherwise stated, abilities gained that augment or alter spells require that the spellcaster " +
                "already knows that spell, otherwise there is no effect. In order to use the gained ability, the " +
                "spellcaster must be holding the implement in one hand.\n" +
                "\n" +
                "The implement is a magical manifestation and cannot be broken. Any blow that strikes the implement must " +
                "be taken as if the implement is not there. You can not actively parry with an implement.\n" +
                "\n" +
                "An implement may be disenchanted causing any effects or castings to be lost until the implement is " +
                "restored. If disenchanted it takes 120 seconds of holding the implement with both hands and nothing " +
                "else to restore.\n" +
                "\n" +
                "Gain one additional casting of one of the following spells for 1 point each: Find the Path, Fortune " +
                "Tell, Guidance, Precognition, Skew Divination, Raise Dead, Deep Pockets, Enfeeble Being, Beckon Corpse, " +
                "Disenchant, or Disrupt.\n" +
                "\n" +
                "Gain the following abilities for 1 point each:\n" +
                "\n" +
                "When using a Circle spell you may double the length of the rope. This may only be done once.\n" +
                "The AC for Death Watch is changed to “Spellcaster must kneel on one knee holding their Implement with " +
                "both hands for 60 seconds before being killed.” Additionally, the spell no longer ends when the " +
                "spellcaster is raised.\n" +
                "The spell Death Watch allows the spellcaster to move their head while dead. They still may not speak, " +
                "try to communicate, or move in any other way while dead.\n" +
                "Gain one additional casting of one of the following spells for 2 points each: Call the Soul, Group " +
                "Healing, Resist Magic, Animate Undead, or Soul Bane.\n" +
                "\n" +
                "Gain the following abilities for 2 points each:\n" +
                "\n" +
                "The uses for Speak become unlimited.\n" +
                "Gain 1 point to spend on your familiar. This ability may not be taken more than once. When this " +
                "augmentation is first used through Implement, you shall list an alternate build for the familiar. This " +
                "alternate build can only be changed by learning or unlearning a use of Familiar. From now on, when you " +
                "use your Familiar and it is augmented by Implement, it uses that new build. If you are using the " +
                "augmented build, and you do not have your implement on you, you may not use your Familiar abilities.\n" +
                "Gain one additional casting of one of the following spells for 3 points each: Vision or Séance.\n" +
                "\n" +
                "Gain the following abilities for 3 points each:\n" +
                "\n" +
                "Gain one use of Regeneration. You are not required to know the spell to use this ability.\n" +
                "Gain one use of Regeneration. Upon completion, the spellcaster will be raised as a free-willed undead. " +
                "You are not required to know the spell to use this ability.\n" +
                "Gain one additional Magic Missile prop.";
    }
}
