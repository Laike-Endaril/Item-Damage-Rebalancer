package com.fantasticsource.itemdamagerebalancer.config;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.itemdamagerebalancer.ItemDamageRebalancer.MODID;

public class ServerConfig
{
    @Config.Name("Item Damage Multipliers")
    @Config.LangKey(MODID + ".config.itemDamageMultipliers")
    @Config.Comment(
            {
                    "Items and attack multipliers for them",
                    "Syntax is...",
                    "",
                    "domain:item:meta > nbtkey1 = nbtvalue1 & nbtkey2 = nbtvalue2, multiplier",
                    "",
                    "eg...",
                    "",
                    "diamond_axe, 0.5",
                    "",
                    "tetra:duplex_tool_modular > duplex/sickle_left_material & duplex/butt_right_material, 3"
            })
    public String[] itemDamageMultipliers = new String[0];
}
