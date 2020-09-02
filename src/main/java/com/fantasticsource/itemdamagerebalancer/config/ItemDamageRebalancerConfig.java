package com.fantasticsource.itemdamagerebalancer.config;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.itemdamagerebalancer.ItemDamageRebalancer.MODID;

@Config(modid = MODID)
public class ItemDamageRebalancerConfig
{
    @Config.Name("Server Settings")
    @Config.LangKey(MODID + ".config.serverSettings")
    public static ServerConfig serverSettings = new ServerConfig();
}
