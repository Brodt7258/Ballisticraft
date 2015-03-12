package welshinq.ballisticraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import welshinq.ballisticraft.Ballisticraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BallisticraftAlloyFurnace extends Block {
	
	private final Random rand = new Random();
	private static boolean bool;

	public BallisticraftAlloyFurnace(Material material, CreativeTabs tab, String unLocName, String texName) {
		super(material);
		setCreativeTab(tab);
		setBlockName(unLocName);
		setBlockTextureName(Ballisticraft.MODID+":"+texName);
	}
	
	@SideOnly(Side.CLIENT)
    public Item getItem(World world, int par1, int par2, int par3)
    {
        return Item.getItemFromBlock(Ballisticraft.alloyFurnace);
    }
	
	/** Bind TileEntity to block */
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityAlloyFurnace();
	}
	
	public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        this.func_149930_e(world, x, y, z);
    }
	
	private void func_149930_e(World world, int x, int y, int z)
    {
        if (!world.isRemote)
        {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            byte b0 = 3;

            if (block.func_149730_j() && !block1.func_149730_j())
            {
                b0 = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j())
            {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j())
            {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j())
            {
                b0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, b0, 2);
        }
    }
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par1, float par2, float par3, float par4)
    {
        if (world.isRemote)
        {
        	System.out.println("ALLOY FURNACE OPENED 1");
            return true;
        }
        else
        {
            TileEntityAlloyFurnace tileEntityAlloyFurnace = (TileEntityAlloyFurnace)world.getTileEntity(x, y, z);

            if (tileEntityAlloyFurnace != null)
            {
                //player.func_146101_a(tileEntityAlloyFurnace);
            	System.out.println("ALLOY FURNACE OPENED 2");
            }
            System.out.println("ALLOY FURNACE OPENED 3");
            return true;
        }
    }
	
	public void breakBlock(World world, int x, int y, int z, Block block, int par)
    {
        if (!bool)
        {
            TileEntityAlloyFurnace tileEntityAlloyFurnace = (TileEntityAlloyFurnace)world.getTileEntity(x, y, z);

            if (tileEntityAlloyFurnace != null)
            {
                for (int i1 = 0; i1 < tileEntityAlloyFurnace.getSizeInventory(); ++i1)
                {
                    ItemStack itemstack = tileEntityAlloyFurnace.getStackInSlot(i1);

                    if (itemstack != null)
                    {
                        float f = this.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0)
                        {
                            int j1 = this.rand.nextInt(21) + 10;

                            if (j1 > itemstack.stackSize)
                            {
                                j1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= j1;
                            EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound())
                            {
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                            }

                            float f3 = 0.05F;
                            entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                            entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                            entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                            world.spawnEntityInWorld(entityitem);
                        }
                    }
                }

                world.func_147453_f(x, y, z, block);
            }
        }

        super.breakBlock(world, x, y, z, block, par);
    }
	
}
