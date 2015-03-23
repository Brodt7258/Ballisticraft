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
import welshinq.ballisticraft.BCBlock;
import welshinq.ballisticraft.MainRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
//TODO Annotate and clean up the code
public class BlockAlloyFurnace extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon top;
	@SideOnly(Side.CLIENT)
	private IIcon front;
	
	private final boolean isBurning;
	private static boolean isBurning1;
	private final Random random = new Random();
	
	public BlockAlloyFurnace(boolean isActive) {
		super(Material.iron);
		setHardness(3.5f);
		setResistance(5.0f);
		setHarvestLevel("pickaxe", 1);
		setStepSound(Block.soundTypeMetal);
		isBurning = isActive;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(MainRegistry.MODID + ":alloyFurnaceSide");
		this.front = iconRegister.registerIcon(this.isBurning ? MainRegistry.MODID + ":alloyFurnaceActive" :
			MainRegistry.MODID + ":alloyFurnace");
		this.top = iconRegister.registerIcon(MainRegistry.MODID + ":alloyFurnaceTop");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? this.top : (side == 0 ? this.top : (side != meta ? this.blockIcon : this.front));
	}
	
	@SideOnly(Side.CLIENT)
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.direction(world, x, y, z);
	}
	
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (isBurning) {
			int direction = world.getBlockMetadata(x, y, z);
			
			float xx = (float) x + 0.5f;
			float yy = (float) y + random.nextFloat() * 6.0f / 16.0f;
			float zz = (float) z + 0.5f;
			float xx1 = 0.52f;
			float zz1 = random.nextFloat() * 0.6f - 0.3f;
			
			if (direction == 4) {
				world.spawnParticle("smoke", (double) (xx - xx1), (double) yy, (double) (zz + zz1), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx - xx1), (double) yy, (double) (zz + zz1), 0.0F, 0.0F, 0.0F);
			} else if (direction == 5) {
				world.spawnParticle("smoke", (double) (xx + xx1), (double) yy, (double) (zz + zz1), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx + xx1), (double) yy, (double) (zz + zz1), 0.0F, 0.0F, 0.0F);
			} else if (direction == 2) {
				world.spawnParticle("smoke", (double) (xx + zz1), (double) yy, (double) (zz - xx1), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx + zz1), (double) yy, (double) (zz - xx1), 0.0F, 0.0F, 0.0F);
			} else if (direction == 3) {
				world.spawnParticle("smoke", (double) (xx + zz1), (double) yy, (double) (zz + xx1), 0.0F, 0.0F, 0.0F);
				world.spawnParticle("flame", (double) (xx + zz1), (double) yy, (double) (zz + xx1), 0.0F, 0.0F, 0.0F);
			}
		}
	}
	
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileAlloyFurnace();
	}
	
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
			((TileAlloyFurnace) world.getTileEntity(x, y, z)).setFurnaceName(stack.getDisplayName());
		}
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
			int metadata, float par1, float par2, float par3) {
		TileAlloyFurnace tileEntityFurnace = (TileAlloyFurnace) world.getTileEntity(x, y, z);
		
		if (tileEntityFurnace == null || player.isSneaking()) {
			return false;
		}
		
		player.openGui(MainRegistry.instance, 0, world, x, y, z);
		return true;
	}
	
	public Item getItemDropped(int par1, Random random, int par2) {
		return Item.getItemFromBlock(BCBlock.alloyFurnace);
	}
	
	public Item getItem(World world, int x, int y, int z) {
		return Item.getItemFromBlock(BCBlock.alloyFurnace);
	}
	
	public static void updateBlockState(boolean burning, World world, int x, int y, int z) {
		int direction = world.getBlockMetadata(x, y, z);
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		isBurning1 = true;
		
		if (burning) {
			world.setBlock(x, y, z, BCBlock.alloyFurnaceActive);
		} else {
			world.setBlock(x, y, z, BCBlock.alloyFurnace);
		}
		
		isBurning1 = false;
		world.setBlockMetadataWithNotify(x, y, z, direction, 2);
		
		if(tileEntity != null) {
			tileEntity.validate();
			world.setTileEntity(x, y, z, tileEntity);
		}
	}
	
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (!isBurning1) {
			TileAlloyFurnace tileEntity = (TileAlloyFurnace) world.getTileEntity(x, y, z);

			if (tileEntity != null) {
				for (int i = 0; i < tileEntity.getSizeInventory(); ++i) {
					ItemStack itemstack = tileEntity.getStackInSlot(i);
	
					if (itemstack != null) {
						float f = this.random.nextFloat() * 0.6F + 0.1F;
						float f1 = this.random.nextFloat() * 0.6F + 0.1F;
						float f2 = this.random.nextFloat() * 0.6F + 0.1F;
	
						while (itemstack.stackSize > 0) {
							int j = this.random.nextInt(21) + 10;
	
							if (j > itemstack.stackSize) {
								j = itemstack.stackSize;
							}
	
							itemstack.stackSize -= j;
							EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));
	
							if (itemstack.hasTagCompound()) {
								entityitem.getEntityItem().setTagCompound(((NBTTagCompound) itemstack.getTagCompound().copy()));
							}
	
							float f3 = 0.025F;
							entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
							entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.1F);
							entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
							world.spawnEntityInWorld(entityitem);
						}
					}
				}
				world.func_147453_f(x, y, z, block);
			}
		super.breakBlock(world, x, y, z, block, meta);
		}
	}
}