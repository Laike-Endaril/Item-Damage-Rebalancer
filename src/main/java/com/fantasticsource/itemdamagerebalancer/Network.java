package com.fantasticsource.itemdamagerebalancer;

import com.fantasticsource.itemdamagerebalancer.config.ItemDamageRebalancerConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

import static com.fantasticsource.itemdamagerebalancer.ItemDamageRebalancer.MODID;

public class Network
{
    public static final SimpleNetworkWrapper WRAPPER = new SimpleNetworkWrapper(MODID);
    private static int discriminator = 0;

    public static void init()
    {
        WRAPPER.registerMessage(ConfigPacketHandler.class, ConfigPacket.class, discriminator++, Side.CLIENT);
    }


    public static class ConfigPacket implements IMessage
    {
        public ArrayList<String> configStrings = new ArrayList<>();

        public ConfigPacket()
        {
            //Required
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            buf.writeInt(ItemDamageRebalancerConfig.serverSettings.itemDamageMultipliers.length);
            for (String s : ItemDamageRebalancerConfig.serverSettings.itemDamageMultipliers)
            {
                ByteBufUtils.writeUTF8String(buf, s);
            }
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            for (int i = buf.readInt(); i > 0; i--) configStrings.add(ByteBufUtils.readUTF8String(buf));
        }
    }

    public static class ConfigPacketHandler implements IMessageHandler<ConfigPacket, IMessage>
    {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(ConfigPacket packet, MessageContext ctx)
        {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> ItemDamageMultiplierData.set(packet.configStrings.toArray(new String[0])));
            return null;
        }
    }
}
