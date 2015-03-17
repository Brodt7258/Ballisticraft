package welshinq.ballisticraft.alloyfurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerAlloyFurnace extends Container {
	protected TileEntityAlloyFurnace tileFurnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
	
    public ContainerAlloyFurnace(InventoryPlayer invPlayer, TileEntityAlloyFurnace alloyFurnace) {
        this.tileFurnace = alloyFurnace;
        
      //the Slot constructor takes the IInventory and the slot number in that it binds to
        //and the x-y coordinates it resides on-screen
        for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                        addSlotToContainer(new Slot(alloyFurnace, j + i * 3, 62 + j * 18, 17 + i * 18));
                }
        }

        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(invPlayer);
    }
    
    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
        for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                        addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9,
                                        8 + j * 18, 84 + i * 18));
                }
        }

        for (int i = 0; i < 9; i++) {
                addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
        }
    }
    
    //TODO
    public void addCraftingToCrafters(ICrafting p_75132_1_)
    {
        super.addCraftingToCrafters(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.tileFurnace.furnaceCookTime);
        p_75132_1_.sendProgressBarUpdate(this, 1, this.tileFurnace.furnaceBurnTime);
        p_75132_1_.sendProgressBarUpdate(this, 2, this.tileFurnace.currentBurnTime);
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

            if (this.lastItemBurnTime != this.tileFurnace.currentBurnTime)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.tileFurnace.currentBurnTime);
            }
        }

        this.lastCookTime = this.tileFurnace.furnaceCookTime;
        this.lastBurnTime = this.tileFurnace.furnaceBurnTime;
        this.lastItemBurnTime = this.tileFurnace.currentBurnTime;
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
            this.tileFurnace.currentBurnTime = num2;
        }
    }
    
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileFurnace.isUseableByPlayer(player);
	}

	/** MAKE SURE YOU OVERRIDE THIS OR IT WILL CRASH 
	 * This is the Shift-Click stack transfer method */
	//TODO
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
	    ItemStack stack = null;
	    Slot slotObj = (Slot) this.inventorySlots.get(slot);
	
	    if (slotObj != null && slotObj.getHasStack()) {
	        ItemStack stackInSlot = slotObj.getStack();
	        stack = stackInSlot.copy();
	
	        if (slot == 2) {
	            if (!this.mergeItemStack(stackInSlot, 3, 39, true))
	            {
	                return null;
	            }
	
	            slotObj.onSlotChange(stackInSlot, stack);
	        }
	        else if (slot != 1 && slot != 0) {
	            if (FurnaceRecipes.smelting().getSmeltingResult(stackInSlot) != null) {
	                if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
	                    return null;
	                }
	            }
	            else if (TileEntityAlloyFurnace.isItemFuel(stackInSlot)) {
	                if (!this.mergeItemStack(stackInSlot, 1, 2, false)) {
	                    return null;
	                }
	            }
	            else if (slot >= 3 && slot < 30) {
	                if (!this.mergeItemStack(stackInSlot, 30, 39, false)) {
	                    return null;
	                }
	            }
	            else if (slot >= 30 && slot < 39 && !this.mergeItemStack(stackInSlot, 3, 30, false)) {
	                return null;
	            }
	        }
	        else if (!this.mergeItemStack(stackInSlot, 3, 39, false)) {
	            return null;
	        }
	
	        if (stackInSlot.stackSize == 0) {
	            slotObj.putStack((ItemStack)null);
	        }
	        else {
	            slotObj.onSlotChanged();
	        }
	
	        if (stackInSlot.stackSize == stack.stackSize) {
	            return null;
	        }
	
	        slotObj.onPickupFromSlot(player, stackInSlot);
	    }
	
	    return stack;
	}
}
