package welshinq.ballisticraft;

import welshinq.ballisticraft.alloyfurnace.TileEntityAlloyFurnace;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, BallisticraftMain.MODID + ":TileEntityAlloyFurnace");
	}

	public void registerNetworkItems() {
		NetworkRegistry.INSTANCE.registerGuiHandler(BallisticraftMain.instance, new BallisticraftGuiHandler());
	}
	
}
