package me.copdead.realmscraft_death_test.death;

import net.minecraft.server.level.EntityPlayer;
//import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Body {
    //private ServerPlayer corpse;
    private EntityPlayer corpse;
    private Player whoDied;
    private Player following = null;
    private boolean isDiseased = false;
    private boolean isRegenerating = false;
    private boolean isRevived = false;
    private boolean isSoulless = false;
    private boolean isHearty = false;
    //private ItemStack[] stealables;
    private Inventory inv = Bukkit.createInventory(null, 27, "Stealable Items");
    private long whenRegen;
    private int soulBlows = 0;
    private BukkitTask carried = null;

    public Body() {
    }

    public EntityPlayer getCorpse() {
        return corpse;
    }

    public int getID() {
        return corpse.ae();
    }

    void setCorpse(EntityPlayer corpse) {
        this.corpse = corpse;
    }

    public Player getWhoDied() {
        return whoDied;
    }

    void setWhoDied(Player whoDied) {
        this.whoDied = whoDied;
    }

    boolean isRevived() {
        return isRevived;
    }

    void setRevived(boolean revived) {
        isRevived = revived;
    }

    public boolean isDiseased() {
        return isDiseased;
    }

    void setDiseased(boolean diseased) {
        isDiseased = diseased;
    }

    public boolean isRegenerating() {
        return isRegenerating;
    }

    public void setRegenerating(boolean regenerating) {
        isRegenerating = regenerating;
    }

    boolean isSoulless() {
        return isSoulless;
    }

    void setSoulless(boolean soulless) {
        isSoulless = soulless;
    }

    int getSoulBlows() {
        return soulBlows;
    }

    void setSoulBlows(int soulBlows) {
        this.soulBlows = soulBlows;
    }

    boolean isHearty() {
        return isHearty;
    }

    void setHearty(boolean hearty) {
        isHearty = hearty;
    }

    Inventory getStealables() {
        return inv;
    }

    void setStealables(ItemStack[] stealables) {
        for(int i = 0; i < stealables.length; i++) {
            inv.setItem(i, stealables[i]);
        }
    }

    public long getWhenRegen() {
        return whenRegen;
    }

    public void setWhenRegen(long whenRegen) {
        this.whenRegen = whenRegen;
    }

    BukkitTask getCarried() {
        return carried;
    }

    void setCarried(BukkitTask carried) {
        this.carried = carried;
    }

    Player getFollowing() {
        return following;
    }

    void setFollowing(Player following) {
        this.following = following;
    }
}
