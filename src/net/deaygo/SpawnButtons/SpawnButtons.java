package net.deaygo.SpawnButtons;

import net.deaygo.SpawnButtons.commands.SpawnBlocksCommand;
import net.deaygo.SpawnButtons.listeners.SBPlayerListener;
import net.deaygo.SpawnButtons.model.SBSettings;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class SpawnButtons extends JavaPlugin {

    SBPlayerListener playerListener;
    SBSettings settings;
    public static PermissionHandler         Permissions = null;
    PluginDescriptionFile descriptor;
    SpawnBlocksCommand spawnBlockCommand;
    
    public void onDisable() {
        settings.saveBlocks();
        System.out.println(descriptor.getName() + " version " + descriptor.getVersion() + " unloaded.");
    }

    public void onEnable() {
        settings = new SBSettings(getServer());
        settings.init(getDataFolder());
        descriptor = getDescription();
        spawnBlockCommand = new SpawnBlocksCommand();
        
        setupPermissions();
        
        playerListener = new SBPlayerListener(settings);
        
        getCommand("spawnblocks").setExecutor(spawnBlockCommand);
        
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_BED_ENTER, playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.High, this);
        
        System.out.println(descriptor.getName() + " version " + descriptor.getVersion() + " loaded.");
    }
    
    public void setupPermissions()
    {
        final Plugin test = getServer().getPluginManager().getPlugin("Permissions");

        if (SpawnButtons.Permissions == null)
        {
            if (test != null)
            {
                SpawnButtons.Permissions = ((Permissions) test).getHandler();
            }
            else
            {
                System.out.println("Permissions is missing, this plugin can't be used.");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

}
