package net.deaygo.SpawnButtons;

import net.deaygo.SpawnButtons.commands.SpawnBlocksCommand;
import net.deaygo.SpawnButtons.listeners.SBBlockListener;
import net.deaygo.SpawnButtons.listeners.SBPlayerListener;
import net.deaygo.SpawnButtons.model.SBSettings;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnButtons extends JavaPlugin {

    SBPlayerListener playerListener;
    SBBlockListener blockListener;
    SBSettings settings;
    PluginDescriptionFile descriptor;
    SpawnBlocksCommand spawnBlockCommand;

    public void onDisable() {
        settings.saveBlocks();
    }

    public void onEnable() {
        settings = new SBSettings(getServer(), this);
        settings.init(getDataFolder());
        descriptor = getDescription();
        spawnBlockCommand = new SpawnBlocksCommand();

        playerListener = new SBPlayerListener(settings);
        blockListener = new SBBlockListener(settings);

        getCommand("spawnblocks").setExecutor(spawnBlockCommand);

        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);

    }
}
