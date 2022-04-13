package me.copdead.realmscraft.death;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Body {

    private EntityPlayer corpse;
    private UUID whoDied;
    private boolean isDiseased = false;
    private boolean isRegenerating = false;
    private ItemStack[] stealables;
    private long whenRegen;

    public Body() {
    }

    public EntityPlayer getCorpse() {
        return corpse;
    }

    public void setCorpse(EntityPlayer corpse) {
        this.corpse = corpse;
    }

    public UUID getWhoDied() {
        return whoDied;
    }

    public void setWhoDied(UUID whoDied) {
        this.whoDied = whoDied;
    }
    public boolean isDiseased() {
        return isDiseased;
    }

    public void setDiseased(boolean diseased) {
        isDiseased = diseased;
    }

    public boolean isRegenerating() {
        return isRegenerating;
    }

    public void setRegenerating(boolean regenerating) {
        isRegenerating = regenerating;
    }

    public ItemStack[] getStealables() {
        return stealables;
    }

    public void setStealables(ItemStack[] stealables) {
        this.stealables = stealables;
    }

    public long getWhenRegen() {
        return whenRegen;
    }

    public void setWhenRegen(long whenRegen) {
        this.whenRegen = whenRegen;
    }
}
