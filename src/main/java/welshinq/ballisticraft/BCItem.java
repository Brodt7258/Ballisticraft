package welshinq.ballisticraft;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BCItem extends Item {

	public static void mainRegistry() {
		initializeItems();
		registerItems();
	}

	public static Item gunBarrel;
	public static Item ingotCopper;
	public static Item ingotTin;
	public static Item ingotSteel;
	public static Item ingotBrass;
	
	public static void initializeItems() {
		gunBarrel = new Item().setUnlocalizedName("gunBarrel").setCreativeTab(MainRegistry.tabBallisticraft).setTextureName(MainRegistry.MODID + ":gunBarrel");
    	ingotCopper = new Item().setUnlocalizedName("ingotCopper").setCreativeTab(CreativeTabs.tabMaterials).setTextureName(MainRegistry.MODID + ":ingotCopper");
    	ingotTin = new Item().setUnlocalizedName("ingotTin").setCreativeTab(CreativeTabs.tabMaterials).setTextureName(MainRegistry.MODID + ":ingotTin");
    	ingotSteel = new Item().setUnlocalizedName("ingotSteel").setCreativeTab(CreativeTabs.tabMaterials).setTextureName(MainRegistry.MODID + ":ingotSteel");
    	ingotBrass = new Item().setUnlocalizedName("ingotBrass").setCreativeTab(CreativeTabs.tabMaterials).setTextureName(MainRegistry.MODID + ":ingotBrass");
	}
	
	public static void registerItems() {
		GameRegistry.registerItem(gunBarrel, "gunBarrel");
		GameRegistry.registerItem(ingotCopper, "ingotCopper");
		GameRegistry.registerItem(ingotTin, "ingotTin");
		GameRegistry.registerItem(ingotSteel, "ingotSteel");
		GameRegistry.registerItem(ingotBrass, "ingotBrass");
	}
}
