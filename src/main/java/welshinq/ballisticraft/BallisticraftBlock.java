package welshinq.ballisticraft;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BallisticraftBlock extends Block {

	protected BallisticraftBlock(Material material, CreativeTabs tab, String unLocName, String texName) {
		super(material);
		setCreativeTab(tab);
		setBlockName(unLocName);
		setBlockTextureName(BallisticraftMain.MODID+":"+texName);
	}
	
}
