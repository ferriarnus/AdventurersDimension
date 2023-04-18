package com.ferriarnus.adventurersdimension.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AdventureConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.ConfigValue<Integer> PERMISSION;

    static {
        PERMISSION = BUILDER.define("Permission", 4);
        SPEC = BUILDER.build();
    }
}
