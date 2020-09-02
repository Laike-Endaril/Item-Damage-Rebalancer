package com.fantasticsource.itemdamagerebalancer;

import com.fantasticsource.itemdamagerebalancer.config.ItemDamageRebalancerConfig;
import com.fantasticsource.mctools.items.ItemFilter;
import com.fantasticsource.tools.Tools;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class ItemDamageMultiplierData
{
    protected static LinkedHashMap<ItemFilter, Double> multipliers = new LinkedHashMap<>();

    public static void serverstart(FMLServerAboutToStartEvent event)
    {
        set(ItemDamageRebalancerConfig.serverSettings.itemDamageMultipliers);
    }

    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        Network.WRAPPER.sendTo(new Network.ConfigPacket(), (EntityPlayerMP) event.player);
    }

    public static double getMultiplier(ItemStack stack)
    {
        for (Map.Entry<ItemFilter, Double> entry : multipliers.entrySet())
        {
            if (entry.getKey().matches(stack)) return entry.getValue();
        }
        return 1;
    }

    public static void set(String... data)
    {
        multipliers.clear();
        for (String s : data)
        {
            String[] tokens = Tools.fixedSplit(s, ",");
            if (tokens.length != 2)
            {
                System.err.println(TextFormatting.RED + "Invalid config entry: " + s);
                continue;
            }

            ItemFilter itemFilter = ItemFilter.getInstance(tokens[0].trim());
            if (itemFilter == null)
            {
                System.err.println(TextFormatting.RED + "Item not found: " + tokens[0].trim());
                System.err.println(TextFormatting.RED + "For config entry: " + s);
                continue;
            }

            try
            {
                double mult = Double.parseDouble(tokens[1].trim());
                multipliers.put(itemFilter, mult);
            }
            catch (NumberFormatException e)
            {
                System.err.println(TextFormatting.RED + "Invalid config entry: " + s);
                continue;
            }
        }
    }
}
