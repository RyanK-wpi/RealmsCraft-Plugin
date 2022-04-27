package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import me.copdead.realmscraft.spells.spell_support.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GhostBlade extends Spell implements Cooldown {
    public GhostBlade() {
        super("Ghost Blade", 1, 2, 105);
    }

    @Override
    public void spellEffect(Player caster) {
        ItemStack mainHand = caster.getInventory().getItemInMainHand();
        ItemStack offHand = caster.getInventory().getItemInOffHand();
        ItemStack hand;

        //get hand holding the item to ghost blade
        if(mainHand.equals(getSpell())) hand = offHand;
        else hand = mainHand;

        //check for weapon
        if(!(hand.getType().toString().toLowerCase().contains("sword") || hand.getType().toString().toLowerCase().contains("axe"))) {
            caster.sendMessage("You can only cast Ghost Blade on a weapon!");
            return;
        }

        //get item meta
        ItemMeta meta = hand.getItemMeta();
        assert meta != null : caster.getName() + " cast Ghost Blade: cannot retrieve meta of item "+ hand;

        //get item lore, or create lore
        List<String> lore;
        if(meta.hasLore()) lore = meta.getLore();
        else lore = new ArrayList<>();
        assert lore != null : caster.getName() + " cast Ghost Blade: cannot retrieve lore of item"+ hand;

        //check if already has ghost blade
        if(lore.contains("Ghost Blade")) {
            caster.sendMessage("Ghost Blade is already cast on this weapon!");
            return;
        }

        //add ghost blade
        lore.add("Ghost Blade");
        meta.setLore(lore);
        hand.setItemMeta(meta);

        Cooldown.cooldownLimited(caster, 5, this);
    }

    @Override
    public String getDescription() {
        return "This spell enchants a single weapon to no longer affect the casting of the spells Raise Dead or " +
                "Regeneration or the breaking of Circle of Healing. Upon casting this spell, the spellcaster must tie " +
                "the MC onto the enchanted weapon.";
    }
}
