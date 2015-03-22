package welshinq.ballisticraft;

import welshinq.ballisticraft.alloyfurnace.TileEntityAlloyFurnace;
import welshinq.ballisticraft.handler.GuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, MainRegistry.MODID + ":TileEntityAlloyFurnace");
	}

	public void registerNetworkItems() {
		NetworkRegistry.INSTANCE.registerGuiHandler(MainRegistry.instance, new GuiHandler());
	}
	
}
