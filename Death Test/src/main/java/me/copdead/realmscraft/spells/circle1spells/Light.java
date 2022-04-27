package me.copdead.realmscraft.spells.circle1spells;

import me.copdead.realmscraft.spells.Spell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Light extends Spell {
    public Light() {
        super("Light", 1, 110);
    }

    //Only let a player activate once?
    @Override
    public void spellEffect(Player caster) {
        final Location[] lastLight = {new Location(caster.getWorld(), 0, 319, 0, 0, 0)};

        new BukkitRunnable() {
            @Override
            public void run() {
                //player not online
                if(!caster.isOnline()) {
                    lastLight[0].getBlock().setType(Material.AIR);
                    cancel();
                }

                //check if holding light source
                if(!(caster.getInventory().getItemInMainHand().equals(getSpell())
                        || caster.getInventory().getItemInOffHand().equals(getSpell()))) {
                    lastLight[0].getBlock().setType(Material.AIR);
                    cancel();
                }

                //search locations and put new light
                Location newLight = caster.getLocation().getBlock().getLocation();
                if(tryLight(newLight.add(0, 1, 0), lastLight[0]) ||            //Head pos
                        tryLight(newLight.add(0, 1,0), lastLight[0]) ||        //Above player
                        tryLight(newLight.add(0, -2,0), lastLight[0])) {       //Feet pos

                    lastLight[0] = newLight;
                }
                //no valid location, no light
                else lastLight[0].getBlock().setType(Material.AIR);
            }
        }.runTaskTimer(plugin,0, 1);
    }

    private boolean tryLight(Location tryLightLoc, Location lastLight) {
        if(tryLightLoc.equals(lastLight)) return true;

        Material material = tryLightLoc.getBlock().getType();
        switch (material) {
            case AIR: case CAVE_AIR: case VOID_AIR:
                tryLightLoc.getBlock().setType(Material.LIGHT);

                Levelled level = (Levelled) tryLightLoc.getBlock();
                level.setLevel(9);
                tryLightLoc.getBlock().setBlockData(level, true);

            case LIGHT:
                lastLight.getBlock().setType(Material.AIR);
                return true;

            default:
                return false;
        }
    }

    @Override
    public String getDescription() {
        return "This spell creates light. The spellcaster may use as many Light props as desired. Electronic light " +
                "sources must be checked in before use and may be pulled if they may be too bright or unsafe for the " +
                "event. If using chemical light sticks, the spellcaster must also carry a bag large enough to hold all " +
                "of the glow sticks they will use and thick enough to prevent any light from escaping. The bag is to be " +
                "used if they are affected by a Disrupt Light spell. The spellcaster may not give a Light prop to anyone " +
                "who is going to travel beyond easy speaking distance. It is possible for this spell to be disrupted. It " +
                "is the spellcaster’s responsibility to know what the Disrupt Light spell is, how to recognize it, and " +
                "how to respond to it.";
    }
}
