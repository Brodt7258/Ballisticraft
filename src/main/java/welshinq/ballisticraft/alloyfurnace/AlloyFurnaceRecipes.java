package welshinq.ballisticraft.alloyfurnace;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import welshinq.ballisticraft.BCItem;

public class AlloyFurnaceRecipes {
	private Map smeltingList = new HashMap();
	private Map experienceList = new HashMap();
	
	/** Checks a hard-coded recipe to alloyfurnace. 
	 * Parameters: ItemStack input 1, 2, and 3
	 */
	public static ItemStack isRecipe(ItemStack input, ItemStack input1, ItemStack input2) {
		Item in = null, in1 = null, in2 = null;
		ItemStack output = null;
		if (input != null) in = input.getItem();
		if (input1 != null) in1 = input1.getItem();
		if (input2 != null) in2 = input2.getItem();
		
		Item[] brassRecipe = new Item[] {BCItem.ingotCopper, BCItem.ingotTin, null, BCItem.ingotBrass};
		
		if (in == brassRecipe[0] || in == brassRecipe[1] || in == brassRecipe[2]) {
			if (in1 == brassRecipe[0] || in1 == brassRecipe[1] || in1 == brassRecipe[2]) {
				if (in2 == brassRecipe[0] || in2 == brassRecipe[1] || in2 == brassRecipe[2]) {
					output = new ItemStack(brassRecipe[3], 2, 32767);
				}
			}
		}
		
		
		return output;
	}
	
	private boolean canAlloy(ItemStack stack, ItemStack stack1) {
		return stack1.getItem() == stack.getItem() &&
				(stack1.getItemDamage() == 32767 || stack1.getItemDamage() == stack.getItemDamage());
	}
	
	/** Compares the ItemStack's item to a list of products, returns exp value */
	public float getAlloyingExperience(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() == BCItem.ingotBrass) return 0.8f;
			if (stack.getItem() == BCItem.ingotSteel) return 0.8f;
			return -1.0f;
		} else return -1.0f;
	}
	
	public float giveExp(ItemStack stack) {
		Iterator itr = this.experienceList.entrySet().iterator();
		Entry entry;
		
		do {
			if (!itr.hasNext()) {
				return 0.0f;
			}
			entry = (Entry) itr.next();
		} while (!canAlloy(stack, (ItemStack) entry.getKey()));
		
		if (getAlloyingExperience(stack) != -1) {
			return getAlloyingExperience(stack);
		}
		
		return ((Float) entry.getValue()).floatValue();
	}
}
