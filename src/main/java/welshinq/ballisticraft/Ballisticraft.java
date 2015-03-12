package welshinq.ballisticraft;

import welshinq.ballisticraft.block.BallisticraftAlloyFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Ballisticraft.MODID, name = Ballisticraft.MODID, version = Ballisticraft.VERSION)
public class Ballisticraft
{
    public static final String MODID = "Ballisticraft"; 
    public static final String VERSION = "0.1.0";
     
    @Instance(value = MODID)
    public static Ballisticraft instance;
    
    @SidedProxy(clientSide = "welshinq.ballisticraft.client.ClientProxy",
    		serverSide = "welshinq.ballisticraft.CommonProxy")
    public static CommonProxy proxy;
    
    
    
    /** Item and Block Variables */
    public static Item gunBarrel;
    public static Item ingotCopper;
    public static Item ingotTin;
    public static Item ingotSteel;
    public static Item ingotBrass;
    
    public static Block alloyFurnace;
    
    public static CreativeTabs tabBallisticraft = new CreativeTabs("Ballisticraft") {
	    @Override
	    @SideOnly(Side.CLIENT)
	    public Item getTabIconItem() {
	    	return Ballisticraft.gunBarrel;
	    }
    };
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	/** Initialize game objects */
    	gunBarrel = new BallisticraftItem(16, this.tabBallisticraft, "gunBarrel", "gunBarrel");
    	ingotCopper = new BallisticraftItem(64, CreativeTabs.tabMaterials, "ingotCopper", "ingotCopper");
    	ingotTin = new BallisticraftItem(64, CreativeTabs.tabMaterials, "ingotTin", "ingotTin");
    	ingotSteel = new BallisticraftItem(64, CreativeTabs.tabMaterials, "ingotSteel", "ingotSteel");
    	ingotBrass = new BallisticraftItem(64, CreativeTabs.tabMaterials, "ingotBrass", "ingotBrass");
    	
    	alloyFurnace = new BallisticraftAlloyFurnace(Material.rock, this.tabBallisticraft, "alloyFurnace", "alloyFurnace");
    	
    	
    	
    	/** Register game objects */
    	GameRegistry.addSmelting(new ItemStack(Items.iron_ingot, 4), new ItemStack(ingotSteel, 1), 0.1f);
    	
    	GameRegistry.registerTileEntity(welshinq.ballisticraft.block.TileEntityAlloyFurnace.class, "tileEntityAlloyFurnace");
    	
		GameRegistry.registerItem(gunBarrel, "gunBarrel");
		GameRegistry.registerItem(ingotCopper, "ingotCopper");
		GameRegistry.registerItem(ingotTin, "ingotTin");
		GameRegistry.registerItem(ingotSteel, "ingotSteel");
		GameRegistry.registerItem(ingotBrass, "ingotBrass");
		
		GameRegistry.registerBlock(alloyFurnace, "alloyFurnace");
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    }

}
