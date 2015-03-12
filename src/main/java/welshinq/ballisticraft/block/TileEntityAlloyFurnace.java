package welshinq.ballisticraft.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
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

public class TileEntityAlloyFurnace extends TileEntity implements ISidedInventory {
	private static final int slotsTop[] = new int[] {0};
	private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};
    /** ItemStack that holds items being used */
    private ItemStack[] alloyFurnaceItemStacks = new ItemStack[5];
    /** Ticks furnace will keep burning */
    public int furnaceBurnTime;
    /** Ticks worth of burn time that a fuel has */
    public int currentItemBurnTime;
    /** How long the furnace has been cooking */
    public int furnaceCookTime;
    private String name = "Alloy Furnace";

    @Override
	public void writeToNBT(NBTTagCompound par1)
	{
    	super.writeToNBT(par1);
        par1.setShort("BurnTime", (short)this.furnaceBurnTime);
        par1.setShort("CookTime", (short)this.furnaceCookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.alloyFurnaceItemStacks.length; ++i)
        {
            if (this.alloyFurnaceItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.alloyFurnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName())
        {
            par1.setString("CustomName", this.name);
        }
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
        NBTTagList nbttaglist = par1.getTagList("Items", 10);
        this.alloyFurnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.alloyFurnaceItemStacks.length)
            {
                this.alloyFurnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.furnaceBurnTime = par1.getShort("BurnTime");
        this.furnaceCookTime = par1.getShort("CookTime");
        this.currentItemBurnTime = getItemBurnTime(this.alloyFurnaceItemStacks[1]);

        if (par1.hasKey("CustomName", 8))
        {
            this.name = par1.getString("CustomName");
        }
	}
    
    /** Returns int between 0 and num based on how close item is to being fully cooked */
    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int num)
    {
        return this.furnaceCookTime * num / 200;
    }

    /** Returns int between 0 and num based on how much burn time fuel has left */
    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int num)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 200;
        }

        return this.furnaceBurnTime * num / this.currentItemBurnTime;
    }

    public boolean isBurning()
    {
        return this.furnaceBurnTime > 0;
    }

    public void updateEntity()
    {
        boolean isBurning = this.furnaceBurnTime > 0;
        boolean flag = false;

        if (this.furnaceBurnTime > 0)
        {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.furnaceBurnTime != 0 || this.alloyFurnaceItemStacks[1] != null && this.alloyFurnaceItemStacks[0] != null)
            {
                if (this.furnaceBurnTime == 0 && this.canSmelt())
                {
                    this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.alloyFurnaceItemStacks[1]);

                    if (this.furnaceBurnTime > 0)
                    {
                        flag = true;

                        if (this.alloyFurnaceItemStacks[1] != null)
                        {
                            --this.alloyFurnaceItemStacks[1].stackSize;

                            if (this.alloyFurnaceItemStacks[1].stackSize == 0)
                            {
                                this.alloyFurnaceItemStacks[1] = alloyFurnaceItemStacks[1].getItem().getContainerItem(alloyFurnaceItemStacks[1]);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    ++this.furnaceCookTime;

                    if (this.furnaceCookTime == 200)
                    {
                        this.furnaceCookTime = 0;
                        this.smeltItem();
                        flag = true;
                    }
                }
                else
                {
                    this.furnaceCookTime = 0;
                }
            }

            if (isBurning != this.furnaceBurnTime > 0)
            {
                flag = true;
                BlockFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (flag)
        {
            this.markDirty();
        }
    }
//TODO Make custom alloy Furnace recipes
    private boolean canSmelt()
    {
        if (this.alloyFurnaceItemStacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.alloyFurnaceItemStacks[0]);
            if (itemstack == null) return false;
            if (this.alloyFurnaceItemStacks[2] == null) return true;
            if (!this.alloyFurnaceItemStacks[2].isItemEqual(itemstack)) return false;
            int result = alloyFurnaceItemStacks[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.alloyFurnaceItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.alloyFurnaceItemStacks[0]);
            
            /** If the output slot is empty, generate a new item */
            if (this.alloyFurnaceItemStacks[2] == null)
            {
                this.alloyFurnaceItemStacks[2] = itemstack.copy();
            }
            else if (this.alloyFurnaceItemStacks[2].getItem() == itemstack.getItem())
            {
                this.alloyFurnaceItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.alloyFurnaceItemStacks[0].stackSize;

            if (this.alloyFurnaceItemStacks[0].stackSize <= 0)
            {
                this.alloyFurnaceItemStacks[0] = null;
            }
        }
    }

    public static int getItemBurnTime(ItemStack itemFuel)
    {
        if (itemFuel == null)
        {
            return 0;
        }
        else
        {
            Item item = itemFuel.getItem();

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

            /** 200 is one full item */
            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(itemFuel);
        }
    }

    public static boolean isItemFuel(ItemStack item)
    {
        return getItemBurnTime(item) > 0;
    }
    
	@Override
	public int getSizeInventory() {
		return this.alloyFurnaceItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.alloyFurnaceItemStacks[slot];
	}

	/**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
	@Override
    public ItemStack decrStackSize(int slot, int num)
    {
        if (this.alloyFurnaceItemStacks[slot] != null)
        {
            ItemStack itemstack;

            if (this.alloyFurnaceItemStacks[slot].stackSize <= num)
            {
                itemstack = this.alloyFurnaceItemStacks[slot];
                this.alloyFurnaceItemStacks[slot] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.alloyFurnaceItemStacks[slot].splitStack(num);

                if (this.alloyFurnaceItemStacks[slot].stackSize == 0)
                {
                    this.alloyFurnaceItemStacks[slot] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }
	
	/** Crap code drops items on closing GUI on some TileEntities. GODDAMN THE WORKBENCH */
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.alloyFurnaceItemStacks[slot] != null)
        {
            ItemStack itemstack = this.alloyFurnaceItemStacks[slot];
            this.alloyFurnaceItemStacks[slot] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.alloyFurnaceItemStacks[slot] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.name : "container.furnace";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.name != null && this.name.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != 
				this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 
				0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}
	
	/** Can automation insert given item into given slot? */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		return slot == 2 ? false : (slot == 1 ? isItemFuel(item) : true);
	}
	
	/** Returns array containing indices of slots accessible by automation on given side */
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSides);
	}
	
	/** Can automation insert items into given slot on given side? */
	@Override
	public boolean canInsertItem(int slot, ItemStack item, int side) {
		return this.isItemValidForSlot(slot, item);
	}
	
	/** Can automation extract given items from given slot on given side? */
	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side) {
		return side != 0 || slot != 1 || item.getItem() == Items.bucket;
	}
}
