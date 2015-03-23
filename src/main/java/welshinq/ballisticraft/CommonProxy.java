package welshinq.ballisticraft;

import welshinq.ballisticraft.alloyfurnace.TileAlloyFurnace;
import welshinq.ballisticraft.handler.GuiHandler;
import welshinq.ballisticraft.world.WorldGen;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public static void mainRegistry() {
		GameRegistry.registerTileEntity(TileAlloyFurnace.class, MainRegistry.MODID + ":TileEntityAlloyFurnace");
		
		GameRegistry.registerWorldGenerator(new WorldGen(), 2);
	}

	public void registerNetworkItems() {
		NetworkRegistry.INSTANCE.registerGuiHandler(MainRegistry.instance, new GuiHandler());
	}
	
}
