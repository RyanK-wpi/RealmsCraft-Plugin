package me.copdead.realmscraft_death_test.events;

import me.copdead.realmscraft_death_test.death.Body;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

public class ClickBodyEvent extends Event implements Cancellable {

    private final Player player;
    private final Body body;
    private final Action action;
    private boolean isCancelled;
    private static final HandlerList HANDLERS = new HandlerList();

    ClickBodyEvent(Player player, Body body, Action action) {
        this.player = player;
        this.body = body;
        this.action = action;
    }

    public Player getPlayer() {
        return player;
    }

    public Body getBody() {
        return body;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }
}
