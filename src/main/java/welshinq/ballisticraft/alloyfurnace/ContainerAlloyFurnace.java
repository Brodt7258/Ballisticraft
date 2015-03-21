package welshinq.ballisticraft.alloyfurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerAlloyFurnace extends Container {
	protected TileEntityAlloyFurnace tileFurnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
	
    public ContainerAlloyFurnace(InventoryPlayer invPlayer, TileEntityAlloyFurnace alloyFurnace) {
        this.tileFurnace = alloyFurnace;
        
        addGuiSlots(alloyFurnace, invPlayer);

        bindPlayerInventory(invPlayer);
    }
    
    protected void addGuiSlots(TileEntityAlloyFurnace alloyFurnace, InventoryPlayer invPlayer) {
    	/** Input */
    	for (int i = 0; i < 3; i++) {
    		addSlotToContainer(new Slot(alloyFurnace, i, 38 + i * 18, 17));
    	}
    	/** Output */
    	for (int i = 0; i < 2; i++) {
    		addSlotToContainer(new SlotFurnace(invPlayer.player, alloyFurnace, i + 3, 116 + i * 18, 35));
    	}
    	/** Fuel */
    	addSlotToContainer(new Slot(alloyFurnace, 5, 56, 53));
    }
    
    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
    	int i;
    	
    	/** Player Inventory */
    	for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    	
    	/** Hotbar */
    	for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
        }
    }
    
    //TODO
    public void addCraftingToCrafters(ICrafting icrafting)
    {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate(this, 0, this.tileFurnace.furnaceCookTime);
        icrafting.sendProgressBarUpdate(this, 1, this.tileFurnace.furnaceBurnTime);
        icrafting.sendProgressBarUpdate(this, 2, this.tileFurnace.currentBurnTime);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

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
    
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileFurnace.isUseableByPlayer(player);
	}

	/** MAKE SURE YOU OVERRIDE THIS OR IT WILL CRASH 
	 * This is the Shift-Click stack transfer method */
	//TODO
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        ItemStack itemstack = null;
        Slot slotObj = (Slot) this.inventorySlots.get(slot);
        
        if (slotObj != null && slotObj.getHasStack())
        {
            ItemStack itemstack1 = slotObj.getStack();
            itemstack = itemstack1.copy();

            if (slot == 4 || slot == 5)
            {
                if (!this.mergeItemStack(itemstack1, 6, 42, true))
                {
                    return null;
                }

                slotObj.onSlotChange(itemstack1, itemstack);
            }
            else if (slot != 1 && slot != 0)
            {
                if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 5, 6, false))
                    {
                        return null;
                    }
                }
                else if (slot >= 3 && slot < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 33, 42, false))
                    {
                        return null;
                    }
                }
                else if (slot >= 30 && slot < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 6, 42, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slotObj.putStack((ItemStack)null);
            }
            else
            {
                slotObj.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slotObj.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}
