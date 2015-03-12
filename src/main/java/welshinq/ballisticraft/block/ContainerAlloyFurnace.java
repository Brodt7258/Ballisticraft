package welshinq.ballisticraft.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerAlloyFurnace extends Container {
	
	private TileEntityAlloyFurnace tileFurnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private static final String __OBFID = "CL_00001748";
	
    public ContainerAlloyFurnace(InventoryPlayer playerInv, TileEntityAlloyFurnace alloyFurnace)
    {
        this.tileFurnace = alloyFurnace;
        this.addSlotToContainer(new Slot(alloyFurnace, 0, 56, 17));
        this.addSlotToContainer(new Slot(alloyFurnace, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnace(playerInv.player, alloyFurnace, 2, 116, 35));
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting p_75132_1_)
    {
        super.addCraftingToCrafters(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.tileFurnace.furnaceCookTime);
        p_75132_1_.sendProgressBarUpdate(this, 1, this.tileFurnace.furnaceBurnTime);
        p_75132_1_.sendProgressBarUpdate(this, 2, this.tileFurnace.currentItemBurnTime);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastCookTime != this.tileFurnace.furnaceCookTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileFurnace.furnaceCookTime);
            }

            if (this.lastBurnTime != this.tileFurnace.furnaceBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileFurnace.furnaceBurnTime);
            }

            if (this.lastItemBurnTime != this.tileFurnace.currentItemBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.tileFurnace.currentItemBurnTime);
            }
        }

        this.lastCookTime = this.tileFurnace.furnaceCookTime;
        this.lastBurnTime = this.tileFurnace.furnaceBurnTime;
        this.lastItemBurnTime = this.tileFurnace.currentItemBurnTime;
    }
// TODO Understand this shit
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int num1, int num2)
    {
        if (num1 == 0)
        {
            this.tileFurnace.furnaceCookTime = num2;
        }

        if (num1 == 1)
        {
            this.tileFurnace.furnaceBurnTime = num2;
        }

        if (num1 == 2)
        {
            this.tileFurnace.currentItemBurnTime = num2;
        }
    }
    
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileFurnace.isUseableByPlayer(player);
	}

	/** MAKE SURE YOU OVERRIDE THIS OR IT WILL CRASH 
	 * This is the Shift-Click stack transfer method */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
	{
	    ItemStack itemstack = null;
	    Slot slot = (Slot)this.inventorySlots.get(slotNum);
	
	    if (slot != null && slot.getHasStack())
	    {
	        ItemStack itemstack1 = slot.getStack();
	        itemstack = itemstack1.copy();
	
	        if (slotNum == 2)
	        {
	            if (!this.mergeItemStack(itemstack1, 3, 39, true))
	            {
	                return null;
	            }
	
	            slot.onSlotChange(itemstack1, itemstack);
	        }
	        else if (slotNum != 1 && slotNum != 0)
	        {
	            if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
	            {
	                if (!this.mergeItemStack(itemstack1, 0, 1, false))
	                {
	                    return null;
	                }
	            }
	            else if (TileEntityAlloyFurnace.isItemFuel(itemstack1))
	            {
	                if (!this.mergeItemStack(itemstack1, 1, 2, false))
	                {
	                    return null;
	                }
	            }
	            else if (slotNum >= 3 && slotNum < 30)
	            {
	                if (!this.mergeItemStack(itemstack1, 30, 39, false))
	                {
	                    return null;
	                }
	            }
	            else if (slotNum >= 30 && slotNum < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
	            {
	                return null;
	            }
	        }
	        else if (!this.mergeItemStack(itemstack1, 3, 39, false))
	        {
	            return null;
	        }
	
	        if (itemstack1.stackSize == 0)
	        {
	            slot.putStack((ItemStack)null);
	        }
	        else
	        {
	            slot.onSlotChanged();
	        }
	
	        if (itemstack1.stackSize == itemstack.stackSize)
	        {
	            return null;
	        }
	
	        slot.onPickupFromSlot(player, itemstack1);
	    }
	
	    return itemstack;
	}
}
