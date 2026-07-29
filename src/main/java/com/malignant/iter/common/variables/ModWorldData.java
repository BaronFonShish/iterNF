package com.malignant.iter.common.variables;

import com.malignant.iter.IterMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class ModWorldData extends SavedData {
    private static final String DATA_KEY = IterMod.MOD_ID + "_world_data";

    private long goblinPatrolNextTick = 0;
    private boolean celestialsArrived = false;
    private boolean demonsActive = false;
    private boolean monolithUnlocked = false;

    public ModWorldData() { }

    public ModWorldData(CompoundTag tag, HolderLookup.Provider lookup) {
        this.goblinPatrolNextTick = tag.getLong("GoblinPatrolNextTick");
        this.celestialsArrived = tag.getBoolean("CelestialsArrived");
        this.demonsActive = tag.getBoolean("DemonsActive");
        this.monolithUnlocked = tag.getBoolean("MonolithUnlocked");
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putLong("GoblinPatrolNextTick", this.goblinPatrolNextTick);
        tag.putBoolean("CelestialsArrived", this.celestialsArrived);
        tag.putBoolean("DemonsActive", this.demonsActive);
        tag.putBoolean("MonolithUnlocked", this.monolithUnlocked);
        return tag;
    }

    public static ModWorldData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new Factory<>(ModWorldData::new, ModWorldData::new),
                DATA_KEY
        );
    }

    public long getgoblinPatrolNextTick() { return goblinPatrolNextTick; }
    public void setgoblinPatrolNextTick(long tick) { this.goblinPatrolNextTick = tick; setDirty(); }
    public boolean getCelestials() { return celestialsArrived; }
    public void setCelestials(boolean flag) { this.celestialsArrived = flag; setDirty(); }
    public boolean getDemons() { return demonsActive; }
    public void setDemons(boolean flag) { this.demonsActive = flag; setDirty(); }
    public boolean getMonolith() { return monolithUnlocked; }
    public void setMonolith(boolean flag) { this.monolithUnlocked = flag; setDirty(); }
}
