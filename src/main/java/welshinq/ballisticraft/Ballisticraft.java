package welshinq.ballisticraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import welshinq.ballisticraft.item.BallisticraftItem;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Ballisticraft.MODID, version = Ballisticraft.VERSION)
public class Ballisticraft
{
    public static final String MODID = "Ballisticraft";
    public static final String VERSION = "v0.1.0";
     
    @Instance(value = MODID)
    public static Ballisticraft instance;
    
    @SidedProxy(clientSide = "welshinq.ballisticraft.client.ClientProxy",
    		serverSide = "welshinq.ballisticraft.CommonProxy")
    public static CommonProxy proxy;
    
    
    
    public static Item gunBarrel;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	gunBarrel = new BallisticraftItem(16, CreativeTabs.tabMisc, "gunBarrel", "gunBarrel");
    	
    	
    	
		GameRegistry.registerItem(gunBarrel, "gunBarrel");
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
