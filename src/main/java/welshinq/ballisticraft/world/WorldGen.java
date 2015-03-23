package welshinq.ballisticraft.world;

import java.util.Random;

import welshinq.ballisticraft.BCBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.dimensionId){
		case -1:
			generateNether(world, random, chunkX * 16, chunkZ * 16);
			break;
		case 0:
			generateSurface(world, random, chunkX * 16, chunkZ * 16);
			break;
		}
	}
	
	private void generateNether(World world, Random random, int i, int j) {
		
	}
	
	private void generateSurface(World world, Random random, int i, int j) {
		for (int k = 0; k < 10; k++) { // k where k < number of veins per chunk
			int copperXCoord = i + random.nextInt(16);
			int h = random.nextInt(80);
			int copperYCoord = (h < 35) ? h = 35: h; // Copper ore spawns from y = 80 to y = 35
			int copperZCoord = j + random.nextInt(16);
			
			h = random.nextInt(8);
			(new WorldGenMinable(BCBlock.oreCopper, (h < 3) ? h = 3: h)) // Spawn between 3 and 8 blocks
				.generate(world, random, copperXCoord, copperYCoord, copperZCoord);
		}
		
		for (int k = 0; k < 10; k++) { // k where k < number of veins per chunk
			int copperXCoord = i + random.nextInt(16);
			int h = random.nextInt(56);
			int copperYCoord = (h < 28) ? h = 28: h; // Tin ore spawns from y = 56 to y = 28
			int copperZCoord = j + random.nextInt(16);
			
			h = random.nextInt(10);
			(new WorldGenMinable(BCBlock.oreTin, (h < 3) ? h = 3: h)) // Spawn between 3 and 10 blocks
				.generate(world, random, copperXCoord, copperYCoord, copperZCoord);
		}
	}
	
}
