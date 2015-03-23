package welshinq.ballisticraft.handler;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import welshinq.ballisticraft.BCBlock;
import welshinq.ballisticraft.BCItem;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingManager {

	public static void mainRegistry() {
		registerCrafting();
		registerSmelting();
		registerAlloying();
		registerGunbench();
	}
	
	private static void registerCrafting() {
		GameRegistry.addRecipe(new ItemStack(BCBlock.alloyFurnace, 1), new Object[] {
			"SSS", "SFS", "CCC", 'S', BCItem.ingotSteel, 'F', Blocks.furnace, 'C', BCItem.ingotCopper
		});
	}
	
	private static void registerSmelting() {
		GameRegistry.addSmelting(new ItemStack(Items.iron_ingot), new ItemStack(BCItem.ingotSteel, 1), 0.1f);
		GameRegistry.addSmelting(new ItemStack(BCBlock.oreCopper), new ItemStack(BCItem.ingotCopper), 0.1f);
		GameRegistry.addSmelting(new ItemStack(BCBlock.oreTin), new ItemStack(BCItem.ingotTin), 0.1f);
	}
	
	private static void registerAlloying() {
		
	}
	
	private static void registerGunbench() {
		
	}
	
}
