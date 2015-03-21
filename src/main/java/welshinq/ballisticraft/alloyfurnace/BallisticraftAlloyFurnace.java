package welshinq.ballisticraft.alloyfurnace;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import welshinq.ballisticraft.BallisticraftMain;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
//TODO Annotate and clean up the code
public class BallisticraftAlloyFurnace extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon top;
	@SideOnly(Side.CLIENT)
	private IIcon front;
	
	private static boolean isBurning;
	private final boolean isBurning1;
	private final Random random = new Random();
	
	public BallisticraftAlloyFurnace(boolean isActive) {
		super(Material.iron);
		setHardness(2.0f);
		setResistance(5.0f);
		setBlockName("ballisticraftAlloyFurnace");
		isBurning1 = isActive;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(BallisticraftMain.MODID + ":alloyFurnace_Side");
		this.front = iconRegister.registerIcon(this.isBurning1 ? BallisticraftMain.MODID + ":alloyFurnace_Active" :
			BallisticraftMain.MODID + ":alloyFurnace_Idle");
		this.top = iconRegister.registerIcon(BallisticraftMain.MODID + ":alloyFurnace_Top");
	}
	
	@SideOnly(Side.CLIENT)
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.direction(world, x, y, z);
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? this.top : (side == 0 ? this.top : (side != meta ? this.blockIcon : this.front));
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (isBurning1) {
			int direction = world.getBlockMetadata(x, y, z);
			
			float xx = (float) x + 0.5f;
			float xx1 = random.nextFloat() * 0.3f - 0.2f;
			float yy = (float) random.nextFloat() * 6.0f / 16.0f;
			float zz = (float) z + 0.5f;
			float zz1 = 0.5f;
			
			if (direction == 4) {
				world.spawnParticle("smoke", (double) (xx - zz1), (double) yy, (double) (zz + xx1), 0.0f, 0.0f, 0.0f);
				world.spawnParticle("flame", (double) (xx - zz1), (double) yy, (double) (zz + xx1), 0.0f, 0.0f, 0.0f);
			} else if (direction == 5) {
				world.spawnParticle("smoke", (double) (xx - zz1), (double) yy, (double) (zz + xx1), 0.0f, 0.0f, 0.0f);
				world.spawnParticle("flame", (double) (xx - zz1), (double) yy, (double) (zz + xx1), 0.0f, 0.0f, 0.0f);
			} else if (direction == 3) {
				world.spawnParticle("smoke", (double) (xx - zz1), (double) yy, (double) (zz + xx1), 0.0f, 0.0f, 0.0f);
				world.spawnParticle("flame", (double) (xx - zz1), (double) yy, (double) (zz + xx1), 0.0f, 0.0f, 0.0f);
			} else if (direction == 2) {
				world.spawnParticle("smoke", (double) (xx - zz1), (double) yy, (double) (zz + xx1), 0.0f, 0.0f, 0.0f);
				world.spawnParticle("flame", (double) (xx - zz1), (double) yy, (double) (zz + xx1), 0.0f, 0.0f, 0.0f);
			}
		}
	}
	
	//TODO Understand this V
	public void direction(World world, int x, int y, int z) {
		if (!world.isRemote) {
			Block direction = world.getBlock(x, y, z - 1);
			Block direction1 = world.getBlock(x, y, z + 1);
			Block direction2 = world.getBlock(x - 1, y, z);
			Block direction3 = world.getBlock(x + 1, y, z);
			byte b0 = 3;
			
			if (direction.func_149730_j() && direction.func_149730_j()) {
				b0 = 3;
			}
			
			if (direction1.func_149730_j() && direction1.func_149730_j()) {
				b0 = 2;
			}
			
			if (direction2.func_149730_j() && direction2.func_149730_j()) {
				b0 = 5;
			}
			
			if (direction3.func_149730_j() && direction3.func_149730_j()) {
				b0 = 4;
			}
			
			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		int direction = MathHelper.floor_double((double) (entity.rotationYaw * 4.0f / 360.0f) + 0.5d) & 3;
		
		if (direction == 0) world.setBlockMetadataWithNotify(x,  y,  z,  2,  2);
		if (direction == 1) world.setBlockMetadataWithNotify(x,  y,  z,  5,  2);
		if (direction == 2) world.setBlockMetadataWithNotify(x,  y,  z,  3,  2);
		if (direction == 3) world.setBlockMetadataWithNotify(x,  y,  z,  4,  2);
		
		if (stack.hasDisplayName()) {
			((TileEntityAlloyFurnace) world.getTileEntity(x, y, z)).setFurnaceName(stack.getDisplayName());
		}
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
			int metadata, float par1, float par2, float par3) {
		TileEntityAlloyFurnace tileEntityFurnace = (TileEntityAlloyFurnace) world.getTileEntity(x, y, z);
		
		if (tileEntityFurnace == null || player.isSneaking()) {
			return false;
		}
		
		player.openGui(BallisticraftMain.instance, 0, world, x, y, z);
		return true;
	}
	
	public Item getItemDropped(int par1, Random random, int par2) {
		return Item.getItemFromBlock(BallisticraftMain.alloyFurnace_Idle);
	}
	
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(BallisticraftMain.alloyFurnace_Idle);
	}
	
	public static void updateBlockState(boolean burning, World world, int x, int y, int z) {
		int direction = world.getBlockMetadata(x, y, z);
		TileEntity tileEntity = world.getTileEntity(x,  y,  z);
		isBurning = true;
		
		if (burning) {
			world.setBlock(x, y, z, BallisticraftMain.alloyFurnace_Active);
		} else {
			world.setBlock(x,  y,  z, BallisticraftMain.alloyFurnace_Idle);
		}
		
		isBurning = false;
		world.setBlockMetadataWithNotify(x, y, z, direction, 2);
		
		if(tileEntity != null) {
			tileEntity.validate();
			world.setTileEntity(x, y, z, tileEntity);
		}
	}
	
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		Random random = new Random();
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inv = (IInventory) tileEntity;
		
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			
			if (stack != null && stack.stackSize > 0) {
				float rx = random.nextFloat() * 0.6f + 0.1f;
				float ry = random.nextFloat() * 0.6f + 0.1f;
				float rz = random.nextFloat() * 0.6f + 0.1f;
				
				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz,
                        new ItemStack(stack.getItem(), stack.stackSize, stack.getItemDamage()));
					
				if (stack.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound(((NBTTagCompound) stack.getTagCompound().copy()));
				}
				
				float factor = 0.025f;
				entityItem.motionX = (double) ((float) random.nextGaussian() * factor);
				entityItem.motionY = (double) ((float) random.nextGaussian() * factor + 0.1f);
				entityItem.motionZ = (double) ((float) random.nextGaussian() * factor);
				world.spawnEntityInWorld(entityItem);
				stack.stackSize = 0;
			}
		}
		
		super.breakBlock(world, x, y, z, block, meta);
	}

	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEntityAlloyFurnace();
	}
}
