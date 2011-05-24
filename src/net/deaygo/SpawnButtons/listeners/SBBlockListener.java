package net.deaygo.SpawnButtons.listeners;

import net.deaygo.SpawnButtons.model.SBSettings;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class SBBlockListener extends BlockListener {
	
	SBSettings settings;
	
	public SBBlockListener(SBSettings settings) {
		this.settings = settings;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		if ( b.getType() == Material.STONE_BUTTON || b.getType() == Material.STONE_PLATE || b.getType() == Material.WOOD_PLATE )
		{
			if ( settings.isSpawnBlock(b) )
			{
				settings.removeBlock(b);
				settings.saveBlocks();
			}
		}
	}
}
