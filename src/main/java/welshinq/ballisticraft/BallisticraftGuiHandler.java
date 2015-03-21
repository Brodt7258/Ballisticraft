package welshinq.ballisticraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import welshinq.ballisticraft.alloyfurnace.ContainerAlloyFurnace;
import welshinq.ballisticraft.alloyfurnace.GuiAlloyFurnace;
import welshinq.ballisticraft.alloyfurnace.TileEntityAlloyFurnace;
import cpw.mods.fml.common.network.IGuiHandler;

public class BallisticraftGuiHandler implements IGuiHandler {
	
	public BallisticraftGuiHandler() {
		
	}
	
	/** Returns an instance of container created previously */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof TileEntityAlloyFurnace){
        	return new ContainerAlloyFurnace(player.inventory, (TileEntityAlloyFurnace) tileEntity);
        }
        return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof TileEntityAlloyFurnace){
        	return new GuiAlloyFurnace(player.inventory, (TileEntityAlloyFurnace) tileEntity);
        }
        return null;
	}
	
}
