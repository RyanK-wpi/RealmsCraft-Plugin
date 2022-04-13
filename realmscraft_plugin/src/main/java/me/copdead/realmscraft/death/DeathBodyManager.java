package me.copdead.realmscraft.death;

import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeathBodyManager {
    private List<Body> bodies;

    public DeathBodyManager() {
        this.bodies = new ArrayList<>();
    }

    public List<Body> getBodies() {
        return bodies;
    }

    void addJoinPacket(Player player) {
        for(Body npc : getBodies()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutNamedEntitySpawn(npc.getCorpse()));
        }
    }
}
