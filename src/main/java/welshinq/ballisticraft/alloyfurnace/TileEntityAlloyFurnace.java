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
public class TileEntityAlloyFurnace extends TileEntity implements ISidedInventory {
	private static final int slotsTop[] = new int[] {0};
	private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};
    /** ItemStack that holds items being used */
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    /** Ticks furnace will keep burning */
    public int furnaceBurnTime;
    /** Ticks worth of burn time that a fuel has */
    public int currentBurnTime;
    /** How long the furnace has been cooking */
    public int furnaceCookTime;
    /** GUI Title */
    private String furnaceName = "Alloy Furnace";
    
    public TileEntityAlloyFurnace() {
    	
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
        /**
         * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
         * fuel
         */
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
            /** ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if (itemstack == null) return false;
            if (this.furnaceItemStacks[2] == null) return true;
            if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;
            int result = this.furnaceItemStacks[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
            */
        	return false;
        }
    }
	
	public boolean isBurning() {
		return furnaceBurnTime > 0;
	}
	
	public void smeltItem()
    {
        if (canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if (this.furnaceItemStacks[2] == null)
            {
                this.furnaceItemStacks[2] = itemstack.copy();
            }
            else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem())
            {
                this.furnaceItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            this.furnaceItemStacks[0].stackSize--;

            if (this.furnaceItemStacks[0].stackSize <= 0)
            {
                this.furnaceItemStacks[0] = null;
            }
        }
    }
	
	//TODO Understand this shit V
	/** Updates block state, progress, and cooking information */
	public void updateEntity() {
		boolean flag = furnaceBurnTime > 0;
		boolean flag1 = false;
		
		if (furnaceBurnTime > 0) {
			furnaceBurnTime--;
		}
		
		/** If worldObj is server world */
		if (!worldObj.isRemote) {
			if (furnaceBurnTime == 0 && canSmelt()) {
				this.currentBurnTime = furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
				
				if (furnaceBurnTime > 0) {
					flag1 = true;
					if (this.furnaceItemStacks[5] != null) {
						this.furnaceItemStacks[5].stackSize--;
						
						if (this.furnaceItemStacks[5].stackSize == 0) {
							this.furnaceItemStacks[5] = this.furnaceItemStacks[5].getItem().getContainerItem(this.furnaceItemStacks[5]);
						}
					}
				}
			}
			
			if (isBurning() && canSmelt()) {
				this.furnaceCookTime++;
				
				if (this.furnaceCookTime >= 200) {
					this.furnaceCookTime = 0;
					smeltItem();
					flag1 = true;
				}
			} else {
				this.furnaceCookTime = 0;
			}
		}
		
		if (flag != furnaceBurnTime > 0) {
			flag1 = true;
			BallisticraftAlloyFurnace.updateBlockState(furnaceBurnTime > 0, worldObj, xCoord, yCoord, zCoord);
		}
		
		if(flag1) {
			markDirty();
		}
	}
	
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
    
    @Override
	public int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		/**ItemStack stack = null;
		if (slot <= this.furnaceItemStacks.length) {
			stack = this.furnaceItemStacks[slot];
		}
		return stack;*/
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
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.furnaceItemStacks[slot] = stack;
		if (stack != null && isItemValidForSlot(slot, stack)) {
			if (stack.stackSize > getInventoryStackLimit()) {
				stack.stackSize = getInventoryStackLimit();
			}
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
	
	//TODO UNDERSTAND THE NBT CODE HERE V
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

  