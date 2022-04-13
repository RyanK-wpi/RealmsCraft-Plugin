package me.copdead.realmscraft.tasks;

import me.copdead.realmscraft.death.Body;
import me.copdead.realmscraft.death.DeathBodyManager;
import me.copdead.realmscraft.spells.Spells;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class RegenTask extends BukkitRunnable {

    private final DeathBodyManager bodyManager;

    public RegenTask(DeathBodyManager bodyManager) {
        this.bodyManager = bodyManager;
    }

    @Override
    public void run() {
        Iterator<Body> bodyIterator = bodyManager.getBodies().iterator();                                               //Use iterators to avoid concurrency exceptions
        while(bodyIterator.hasNext()) {
            Body body = bodyIterator.next();
            CraftPlayer corpse = body.getCorpse().getBukkitEntity();

            if(body.isRegenerating()) {
                corpse.getWorld().spawnParticle(Particle.HEART, corpse.getLocation(), 1);

                long now = System.currentTimeMillis();
                if((!body.isDiseased() && now - body.getWhenRegen() >= 60*1000) ||
                        (body.isDiseased() && now - body.getWhenRegen() >= 120*1000)) {
                    bodyIterator.remove();
                    Spells.reviveCorpse(body);
                }
            }
        }
    }
}
