package welshinq.ballisticraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BallisticraftItem extends Item {

	public BallisticraftItem(int stackSize, CreativeTabs tab, String unlocName, String texName) {
		setMaxStackSize(stackSize);
		setCreativeTab(tab);
		setUnlocalizedName(unlocName);
		setTextureName(Ballisticraft.MODID+":"+texName);
	}
}
