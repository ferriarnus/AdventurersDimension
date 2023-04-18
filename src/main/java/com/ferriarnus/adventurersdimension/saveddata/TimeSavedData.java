package com.ferriarnus.adventurersdimension.saveddata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class TimeSavedData extends SavedData {

    private long time;

    public TimeSavedData(Long time, ServerLevel level){
        this.time = level.getGameTime() + time;
        this.setDirty();
    }

    public TimeSavedData(Long time){
        this.time = time;
        this.setDirty();
    }

    public static TimeSavedData load(CompoundTag pCompoundTag) {
        return new TimeSavedData(pCompoundTag.getLong("time"));
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("time", time);
        return tag;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        this.setDirty();
    }
}
