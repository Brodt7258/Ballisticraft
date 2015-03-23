package welshinq.ballisticraft;

import net.minecraft.block.Block;
import welshinq.ballisticraft.alloyfurnace.BlockAlloyFurnace;
import welshinq.ballisticraft.ore.OreCopper;
import welshinq.ballisticraft.ore.OreTin;
import cpw.mods.fml.common.registry.GameRegistry;

public class BCBlock {
	
	public static void mainRegistry() {
		initializeBlocks();
		registerBlocks();
	}
	
	public static Block oreCopper;
	public static Block oreTin;
	public static Block alloyFurnace;
    public static Block alloyFurnaceActive;
	
    public static void initializeBlocks() {
    	oreCopper = new OreCopper().setBlockName("oreCopper").setCreativeTab(MainRegistry.tabBallisticraft).setBlockTextureName(MainRegistry.MODID + ":oreCopper");
    	oreTin = new OreTin().setBlockName("oreTin").setCreativeTab(MainRegistry.tabBallisticraft).setBlockTextureName(MainRegistry.MODID + ":oreTin");
    	alloyFurnace = new BlockAlloyFurnace(false).setBlockName("alloyFurnace").setCreativeTab(MainRegistry.tabBallisticraft);
    	alloyFurnaceActive = new BlockAlloyFurnace(true).setBlockName("alloyFurnaceActive").setLightLevel(0.875f);
    }
    
    public static void registerBlocks() {
    	GameRegistry.registerBlock(oreCopper, oreCopper.getUnlocalizedName());
    	GameRegistry.registerBlock(oreTin, oreTin.getUnlocalizedName());
    	GameRegistry.registerBlock(alloyFurnace, alloyFurnace.getUnlocalizedName());
		GameRegistry.registerBlock(alloyFurnaceActive, alloyFurnaceActive.getUnlocalizedName());
    }
    
}
