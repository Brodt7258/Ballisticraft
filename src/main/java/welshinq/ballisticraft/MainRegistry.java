package welshinq.ballisticraft;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import welshinq.ballisticraft.handler.CraftingManager;
import welshinq.ballisticraft.handler.GuiHandler;
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

@Mod(modid = MainRegistry.MODID, name = MainRegistry.MODID, version = MainRegistry.VERSION)

public class MainRegistry {
	
    public static final String MODID = "Ballisticraft"; 
    public static final String VERSION = "0.1.0";
     
    @Instance(value = MODID)
    public static MainRegistry instance;
    
    @SidedProxy(clientSide = "welshinq.ballisticraft.client.ClientProxy", serverSide = "welshinq.ballisticraft.CommonProxy")
    public static CommonProxy proxy;
    
    public static GuiHandler guiHandler = new GuiHandler();
    
    public static CreativeTabs tabBallisticraft = new CreativeTabs("Ballisticraft") {
	    @Override
	    @SideOnly(Side.CLIENT)
	    public Item getTabIconItem() {
	    	return BCItem.gunBarrel;
	    }
    };
    
    @EventHandler
    public void PreLoad(FMLPreInitializationEvent event)
    {
    	BCItem.mainRegistry();
    	BCBlock.mainRegistry();
    	CraftingManager.mainRegistry();
    	
    	proxy.mainRegistry();
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event)
    {
    	proxy.registerNetworkItems();
    }
    
}
