package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.menu.MenuManager;
import me.copdead.realmscraft.player_class.PlayerClass;
import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.spell_support.Cast;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairArmor extends Spell implements Cast, Cooldown {
    public RepairArmor() {
        super("Repair Armor", 1, 5, 114);
    }

    @Override
    public void spellEffect(Player caster) {
        Entity target = Cast.cast_include_player(caster);

        if(!(target instanceof Player)) return;
        Player player = (Player) target;

        String playerClass = PlayerClass.getPlayerClass(player);
        switch (playerClass) {
            case "Fighter":
                if(player.getAbsorptionAmount() >= 40) return;
                player.setAbsorptionAmount(40);
                break;

            case "1Path":
                if(player.getAbsorptionAmount() >= 20) return;
                player.setAbsorptionAmount(20);
                break;

            case "2Path": case "3Path":
                //check if chose armor
                ItemStack sixthCircleSpell = MenuManager.getPlayerMenuUtility(player).getSpellsList().getInventory().getItem(49);
                assert sixthCircleSpell != null : caster.getName() + " Repair Armor on " +player.getName()+ " cannot find armor value!";
                if(!sixthCircleSpell.getType().equals(Material.BARRIER)) return;

                if(player.getAbsorptionAmount() >= 20) return;
                player.setAbsorptionAmount(20);
                break;

            case "none":
                return;
        }

        Cooldown.cooldownLimited(caster, 15, this);
    }

    @Override
    public String getDescription() {
        return "This spell will repair one hit location of armor. You are encouraged to simulate physically repairing " +
                "the armor, such as tapping it with a focus, like a boff-hammer.";
    }
}
