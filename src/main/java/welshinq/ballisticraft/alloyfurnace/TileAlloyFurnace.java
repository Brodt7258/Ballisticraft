package welshinq.ballisticraft.alloyfurnace;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
// TODO Annotate and clean up the code
public class TileAlloyFurnace extends TileEntity implements ISidedInventory {
	private static final int slotsTop[] = new int[] {0, 1, 2};
	private static final int[] slotsBottom = new int[] {3, 4};
    private static final int[] slotsSides = new int[] {5};
    /** ItemStack that holds items being used */
    private ItemStack[] furnaceItemStacks = new ItemStack[6];
    /** Ticks furnace will keep burning */
    public int furnaceBurnTime;
    /** Ticks worth of burn time that a fuel has left */
    public int currentBurnTime;
    /** How long the furnace has been cooking */
    public int furnaceCookTime;
    /** GUI Title */
    private String furnaceName = "Alloy Furnace";
    
    @SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int par1) {
		return this.furnaceCookTime * par1 / 200;
	}
	
	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int par1) {
		if (this.currentBurnTime == 0 ) {
			this.currentBurnTime = 200;
		}
		
		return furnaceBurnTime * par1 / this.currentBurnTime;
	}
    
	public void setFurnaceName(String name) {
		this.furnaceName = name;
	}
	
	public String getFurnaceName() {
		return this.furnaceName;
	}
	
	public static int getItemBurnTime(ItemStack stack)
    {
        if (stack == null)
        {
            return 0;
        }
        else
        {
            Item item = stack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }

                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(stack);
        }
    }
	
	public static boolean isItemFuel(ItemStack stack)
    {
        return getItemBurnTime(stack) > 0;
    }
	
	private boolean canSmelt()
    {
        if (this.furnaceItemStacks[0] == null && this.furnaceItemStacks[1] == null && this.furnaceItemStacks[2] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = AlloyFurnaceRecipes.isRecipe(this.furnaceItemStacks[0], this.furnaceItemStacks[1], this.furnaceItemStacks[2]);
            if (itemstack == null) return false;
            if (this.furnaceItemStacks[3] == null && this.furnaceItemStacks[4] == null) return true;
            if (!this.furnaceItemStacks[3].isItemEqual(itemstack) && !this.furnaceItemStacks[4].isItemEqual(itemstack)) return false;
            //TODO setup for two outputs
            int result = this.furnaceItemStacks[3].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[3].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }
	
	public boolean isBurning() {
		return furnaceBurnTime > 0;
	}
	
	public void smeltItem()
    {
        if (canSmelt())
        {
            ItemStack itemstack = AlloyFurnaceRecipes.isRecipe(this.furnaceItemStacks[0], this.furnaceItemStacks[1], this.furnaceItemStacks[2]);

            if (this.furnaceItemStacks[3] == null)
            {
                this.furnaceItemStacks[3] = itemstack.copy();
            }
            else if (this.furnaceItemStacks[3].getItem() == itemstack.getItem())
            {
                this.furnaceItemStacks[3].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            if (this.furnaceItemStacks[0] != null) this.furnaceItemStacks[0].stackSize--;
            if (this.furnaceItemStacks[1] != null) this.furnaceItemStacks[1].stackSize--;
            if (this.furnaceItemStacks[2] != null) this.furnaceItemStacks[2].stackSize--; 

            if (this.furnaceItemStacks[0] != null) {
	            if (this.furnaceItemStacks[0].stackSize <= 0)
	            {
	                this.furnaceItemStacks[0] = null;
	            }
            }
            if (this.furnaceItemStacks[1] != null) {
	            if (this.furnaceItemStacks[1].stackSize <= 0)
	            {
	                this.furnaceItemStacks[1] = null;
	            }
            }
            if (this.furnaceItemStacks[2] != null) {
	            if (this.furnaceItemStacks[2].stackSize <= 0)
	            {
	                this.furnaceItemStacks[2] = null;
	            }
            }
        }
    }
	
	/** Updates block state, progress, and cooking information */
	public void updateEntity() {
		super.updateEntity();
		
		/** If worldObj is server world */
		if (!this.worldObj.isRemote) {
			boolean flag = this.furnaceBurnTime > 0;
			boolean updateInv = false;
			
			if (this.furnaceBurnTime > 0) {
				this.furnaceBurnTime--;
			}
			
			
			if (this.furnaceBurnTime == 0 && this.canSmelt()) {
				this.currentBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[5]);
				
				if (this.furnaceBurnTime > 0) {
					updateInv = true;
					if (this.furnaceItemStacks[5] != null) {
						this.furnaceItemStacks[5].stackSize--;
						
						if (this.furnaceItemStacks[5].stackSize == 0) {
							this.furnaceItemStacks[5] = this.furnaceItemStacks[5].getItem().getContainerItem(this.furnaceItemStacks[5]);
						}
					}
				}
			}
			
			if (this.isBurning() && this.canSmelt()) {
				this.furnaceCookTime++;
				
				if (this.furnaceCookTime >= 200) {
					this.furnaceCookTime = 0;
					smeltItem();
					updateInv = true;
				}
			} else {
				this.furnaceCookTime = 0;
			}
		
			if (flag != furnaceBurnTime > 0) {
				updateInv = true;
				BlockAlloyFurnace.updateBlockState(furnaceBurnTime > 0, worldObj, xCoord, yCoord, zCoord);
			}
			
			if(updateInv) {
				//markDirty();
			}
		}
	}
    
    @Override
	public int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.furnaceItemStacks[slot];
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int decr) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= decr) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(decr);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.furnaceItemStacks[slot] != null) {
			ItemStack itemstack = this.furnaceItemStacks[slot];
			this.furnaceItemStacks[slot] = null;
			return itemstack;
		} else {
			return null;
		}
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.furnaceItemStacks[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
				stack.stackSize = getInventoryStackLimit();
		}
		return;
	}
	
	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? this.furnaceName : "Alloy Furnace";
	}
	
	@Override
	public boolean hasCustomInventoryName() {
		return this.furnaceName != null && this.furnaceName.length() > 0;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
				player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}
	
	@Override
	public void openInventory() {}
	
	@Override
	public void closeInventory() {}
	
	@Override
	public boolean isItemValidForSlot(int slots, ItemStack stack) {
		return (slots == 3 || slots == 4) ? false : (slots == 5 ? isItemFuel(stack) : true);
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSides);
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return side != 0 || slot != 5 || stack.getItem() == Items.bucket;
	}
	
	//TODO UNDERSTAND THE NBT CODE HERE
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		NBTTagList tagList = tagCompound.getTagList("Items", 10);
		this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];
		
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
			byte b0 = tagCompound1.getByte("Slot");
			
			if (b0 >= 0 && b0 < this.furnaceItemStacks.length) {
				this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(tagCompound1);
			}
		}
		
		this.furnaceBurnTime = tagCompound.getShort("BurnTime");
		this.furnaceCookTime = tagCompound.getShort("CookTime");
		this.currentBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
		
		if (tagCompound.hasKey("CustomName", 8)) {
			this.furnaceName = tagCompound.getString("CustomName");
		}
	}
	
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setShort("BurnTime", (short) this.furnaceBurnTime);
		tagCompound.setShort("CoolTime", (short) this.furnaceCookTime);
		
		NBTTagList tagList = new NBTTagList();
		
		for (int i = 0; i < this.furnaceItemStacks.length; i++) {
			if (this.furnaceItemStacks[i] != null) {
				NBTTagCompound tagCompound1 = new NBTTagCompound();
				tagCompound.setByte("Slot", (byte) i);
				this.furnaceItemStacks[i].writeToNBT(tagCompound);
				tagList.appendTag(tagCompound1);
			}
		}
		
		tagCompound.setTag("Items", tagList);
		
		if (hasCustomInventoryName()) {
			tagCompound.setString("CustomName", this.furnaceName);
		}
	}
}