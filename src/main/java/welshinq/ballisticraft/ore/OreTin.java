package welshinq.ballisticraft.ore;

import java.util.Random;

import welshinq.ballisticraft.BCBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class OreTin extends Block {
	
	public OreTin() {
		super(Material.rock);
		setHardness(1.5f);
		setResistance(5.0f);
		setHarvestLevel("pickaxe", 1);
		setStepSound(Block.soundTypePiston);
	}
	
	@Override
	public Item getItemDropped(int metadata, Random random, int fortune) {
		return Item.getItemFromBlock(BCBlock.oreTin);
	}
}
