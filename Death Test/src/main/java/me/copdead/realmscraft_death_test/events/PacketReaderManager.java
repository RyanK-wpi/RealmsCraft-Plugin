package me.copdead.realmscraft_death_test.events;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.copdead.realmscraft_death_test.Realmscraft_death_test;
import me.copdead.realmscraft_death_test.death.Body;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketReaderManager {

    private Realmscraft_death_test plugin;
    private static Map<UUID, Channel> channels = new HashMap<>();

    public PacketReaderManager(Realmscraft_death_test plugin) {
        this.plugin = plugin;
    }

    public static Map<UUID, Channel> getChannels() {
        return channels;
    }

    public void inject(Player p) {
        CraftPlayer nmsPlayer = (CraftPlayer) p;
        Channel channel = nmsPlayer.getHandle().b.a.k;                                                                  //PlayerConnection.netWorkManager.channel
        channels.put(p.getUniqueId(), channel);

        if(channel.pipeline().get("PacketInjector") != null)        //already injected!
            return;

        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {
            @Override
            protected void decode(ChannelHandlerContext channel, PacketPlayInUseEntity packet, List<Object> list) throws Exception {
                list.add(packet);
                readPacket(p, packet);
            }
        });
    }

    public void uninject(Player p) {
        Channel channel = channels.get(p.getUniqueId());

        if(channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
            channels.remove(p.getUniqueId());
        }
    }

    private void readPacket(Player player, Packet<?> packet) {
        //PACKET: a = entityID, b = action, c = sneaking, d = hand
        //ACTION: 0 = interact, 1 = attack, 2 = interact_at
        //HAND: 0 = main_hand, 1 = off_hand

        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            int id = (int) getValue(packet, "a");

            if(getValue(packet, "b").toString().split("\\$")[1].charAt(0) == '1') {                         //ATTACK
                for(Body body : plugin.getBodyManager().getBodies()) {
                    if(body.getID() == id) {
                        Bukkit.getScheduler().runTask(plugin, () ->
                                Bukkit.getPluginManager().callEvent(new
                                        ClickBodyEvent(player, body, Action.LEFT_CLICK_AIR)));
                    }
                }
                return;
            }

            if(getValue(getValue(packet, "b"), "a").toString().equalsIgnoreCase("OFF_HAND"))
                return;
            if(getValue(packet, "b").toString().split("\\$")[1].charAt(0) == 'e')                           //INTERACT_AT
                return;

            if(getValue(packet, "b").toString().split("\\$")[1].charAt(0) == 'd') {                         //INTERACT
                for(Body body : plugin.getBodyManager().getBodies()) {
                    if(body.getID() == id) {
                        Bukkit.getScheduler().runTask(plugin, () ->
                                Bukkit.getPluginManager().callEvent(new
                                        ClickBodyEvent(player, body, Action.RIGHT_CLICK_AIR)));
                    }
                }
            }
        }
    }

    //get private fields through reflection
    private Object getValue(Object instance, String name) {
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);

            field.setAccessible(true);
            result = field.get(instance);
            //field.setAccessible(false);

        }catch (Exception e) {
            e.getStackTrace();
        }

        return result;
    }
}
