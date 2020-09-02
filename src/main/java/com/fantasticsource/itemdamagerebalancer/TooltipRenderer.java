package com.fantasticsource.itemdamagerebalancer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class TooltipRenderer
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void tooltips(ItemTooltipEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (player == null) return;

        String attackDamageString = I18n.translateToLocalFormatted("attribute.name.generic.attackDamage");

        ItemStack stack = event.getItemStack();
        if (stack.getItem().getHarvestLevel(stack, "axe", null, null) >= 0)
        {
            List<String> tooltip = event.getToolTip();
            for (int i = 0; i < tooltip.size(); i++)
            {
                String line = tooltip.get(i);
                if (line.contains(attackDamageString))
                {
                    try
                    {
                        double damage = Double.parseDouble(line.replace(attackDamageString, "").trim()) * ItemDamageMultiplierData.getMultiplier(stack);
                        tooltip.set(i, line.replaceFirst("[0-9.]+", "" + damage));
                    }
                    catch (NumberFormatException e)
                    {
                        continue;
                    }
                }
            }
        }
    }
}
