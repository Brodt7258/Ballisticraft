package welshinq.ballisticraft;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import welshinq.ballisticraft.alloyfurnace.BallisticraftAlloyFurnace;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
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
    
    public static BallisticraftGuiHandler guiHandler = new BallisticraftGuiHandler();
    
    
    
    /** Item and Block Variables */
    public static Item gunBarrel;
    public static Item ingotCopper;
    public static Item ingotTin;
    public static Item ingotSteel;
    public static Item ingotBrass;
    
    public static Block alloyFurnace_Idle;
    public static Block alloyFurnace_Active;
    
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
    	
    	alloyFurnace_Idle = new BallisticraftAlloyFurnace(false).setBlockName("alloyFurnace_Idle").setCreativeTab(this.tabBallisticraft).setBlockTextureName(this.MODID + ":alloyFurnace_Idle");
    	alloyFurnace_Active = new BallisticraftAlloyFurnace(true).setBlockName("alloyFurnace_Active").setBlockTextureName(this.MODID + ":alloyFurnace_Active");
    	
    	
    	/** Register game objects */
    	guiHandler.registerTileEntities();
    	
    	GameRegistry.addSmelting(new ItemStack(Items.iron_ingot, 4), new ItemStack(ingotSteel, 1), 0.1f);
    	
		GameRegistry.registerItem(gunBarrel, "gunBarrel");
		GameRegistry.registerItem(ingotCopper, "ingotCopper");
		GameRegistry.registerItem(ingotTin, "ingotTin");
		GameRegistry.registerItem(ingotSteel, "ingotSteel");
		GameRegistry.registerItem(ingotBrass, "ingotBrass");
		
		GameRegistry.registerBlock(alloyFurnace_Idle, "alloyFurnace_Idle");
		GameRegistry.registerBlock(alloyFurnace_Active, "alloyFurnace_Active");
		
		/** "This" is an instance of @Mod */
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
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
