package welshinq.ballisticraft.alloyfurnace;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import welshinq.ballisticraft.BallisticraftMain;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AlloyFurnaceRecipes {
	private static final AlloyFurnaceRecipes ALLOYBASE = new AlloyFurnaceRecipes();
	private Map smeltingList = new HashMap();
	private Map experienceList = new HashMap();
	
	public static AlloyFurnaceRecipes smelting() {
		return ALLOYBASE;
	}
	
	private AlloyFurnaceRecipes() {
		
	}
	
	/** Adds a recipe to alloyfurnace. 
	 * Parameters: ItemStack input 1, 2, and 3
	 */
	public static ItemStack isRecipe(ItemStack input, ItemStack input1, ItemStack input2) {
		Item in1 = null;
		Item in2 = null;
		Item in3 = null; 
		if (input != null) in1 = input.getItem();
		if (input1 != null) in2 = input1.getItem();
		if (input2 != null) in3 = input2.getItem();
		Item[] in = new Item[] {in1, in2, in3};
		ItemStack output = null;
		
		Item[] brassRecipe = new Item[] {BallisticraftMain.ingotCopper, BallisticraftMain.ingotTin, null};
		
		if (in[0] == brassRecipe[0] && in[1] == brassRecipe[1] && in[2] == brassRecipe[2]) {
			output = new ItemStack(BallisticraftMain.ingotBrass, 2, 32767);
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
			if (stack.getItem() == BallisticraftMain.ingotBrass) return 0.8f;
			if (stack.getItem() == BallisticraftMain.ingotSteel) return 0.8f;
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
