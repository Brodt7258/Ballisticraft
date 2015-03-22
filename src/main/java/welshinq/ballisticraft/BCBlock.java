package welshinq.ballisticraft;

import net.minecraft.block.Block;
import welshinq.ballisticraft.alloyfurnace.BlockAlloyFurnace;
import cpw.mods.fml.common.registry.GameRegistry;

public class BCBlock {
	
	public static void mainRegistry() {
		initializeBlocks();
		registerBlocks();
	}

	public static Block alloyFurnace_Idle;
    public static Block alloyFurnace_Active;
	
    public static void initializeBlocks() {
    	alloyFurnace_Idle = new BlockAlloyFurnace(false).setBlockName("alloyFurnace_Idle").setCreativeTab(MainRegistry.tabBallisticraft);
    	alloyFurnace_Active = new BlockAlloyFurnace(true).setBlockName("alloyFurnace_Active");
    }
    
    public static void registerBlocks() {
    	GameRegistry.registerBlock(alloyFurnace_Idle, alloyFurnace_Idle.getUnlocalizedName());
		GameRegistry.registerBlock(alloyFurnace_Active, alloyFurnace_Active.getUnlocalizedName());
    }
    
}
