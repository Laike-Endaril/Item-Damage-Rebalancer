package com.fantasticsource.itemdamagerebalancer;

import com.fantasticsource.mctools.MCTools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ItemDamageRebalancer.MODID, name = ItemDamageRebalancer.NAME, version = ItemDamageRebalancer.VERSION, dependencies = "required-after:fantasticlib@[1.12.2.036,)")
public class ItemDamageRebalancer
{
    public static final String MODID = "itemdamagerebalancer";
    public static final String NAME = "Item Damage Rebalancer";
    public static final String VERSION = "1.12.2.003";

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        Network.init();
        MinecraftForge.EVENT_BUS.register(ItemDamageRebalancer.class);
        MinecraftForge.EVENT_BUS.register(ItemDamageMultiplierData.class);

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            MinecraftForge.EVENT_BUS.register(TooltipRenderer.class);
        }
    }

    @Mod.EventHandler
    public static void serverstart(FMLServerAboutToStartEvent event)
    {
        ItemDamageMultiplierData.serverstart(event);
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void syncConfig(ConfigChangedEvent.PostConfigChangedEvent event)
    {
        if (MCTools.hosting())
        {
            for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
            {
                Network.WRAPPER.sendTo(new Network.ConfigPacket(), player);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttack(LivingHurtEvent event)
    {
        DamageSource dmgSource = event.getSource();
        Entity source = dmgSource.getTrueSource();
        Entity immediate = dmgSource.getImmediateSource();

        boolean isMelee = source != null && source == immediate;
        if (!isMelee) return;

        Entity attacker = event.getSource().getImmediateSource();
        if (!(attacker instanceof EntityLivingBase)) attacker = event.getSource().getTrueSource();
        if (attacker instanceof EntityLivingBase)
        {
            event.setAmount((float) (event.getAmount() * ItemDamageMultiplierData.getMultiplier(((EntityLivingBase) attacker).getHeldItemMainhand())));
        }
    }
}
