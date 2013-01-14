package net.deaygo.SpawnButtons.listeners;

import net.deaygo.SpawnButtons.model.SBSettings;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SBBlockListener implements Listener {

    SBSettings settings;

    public SBBlockListener(SBSettings settings) {
        this.settings = settings;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (b.getType() == Material.STONE_BUTTON || b.getType() == Material.STONE_PLATE || b.getType() == Material.WOOD_PLATE) {
            if (settings.isSpawnBlock(b)) {
                settings.removeBlock(b);
                settings.saveBlocks();
            }
        }
    }
}
