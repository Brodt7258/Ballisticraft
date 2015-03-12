package welshinq.ballisticraft.block;

import java.util.Random;

import welshinq.ballisticraft.Ballisticraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BallisticraftBlock extends Block {

	protected BallisticraftBlock(Material material, CreativeTabs tab, String unLocName, String texName) {
		super(material);
		setCreativeTab(tab);
		setBlockName(unLocName);
		setBlockTextureName(Ballisticraft.MODID+":"+texName);
	}
	
}
