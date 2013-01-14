package net.deaygo.SpawnButtons.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.deaygo.SpawnButtons.SpawnButtons;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class SBSettings {

    File dataFolder;
    File playerFolder;
    File settingsFile;
    Server server;
    SpawnButtons plugin;
    List<SBPlayer> players = new ArrayList<SBPlayer>();
    List<SpawnBlock> blocks = new ArrayList<SpawnBlock>();

    public void addBlock(SpawnBlock b) {
        blocks.add(b);
        saveBlocks();
    }

    public void removeBlock(Block b) {
        for (int i = 0; i < blocks.size(); i++) {
            SpawnBlock s = blocks.get(i);
            if ((s.getLocation().getWorld().getName().equalsIgnoreCase(b.getWorld().getName()))
                    && (s.getLocation().getBlockX() == b.getX())
                    && (s.getLocation().getBlockY() == b.getY())
                    && (s.getLocation().getBlockZ() == b.getZ())) {
                blocks.remove(i);
                break;
            }
        }
    }

    public boolean isSpawnBlock(Block b) {
        for (SpawnBlock s : blocks) {
            if ((s.getLocation().getWorld().getName().equalsIgnoreCase(b.getWorld().getName()))
                    && (s.getLocation().getBlockX() == b.getX())
                    && (s.getLocation().getBlockY() == b.getY())
                    && (s.getLocation().getBlockZ() == b.getZ())) {
                return true;
            }
        }
        return false;
    }

    public void init(final File dataFolder) {
        this.dataFolder = dataFolder;
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.playerFolder = new File(dataFolder.getAbsoluteFile() + "/players");
        if (!this.playerFolder.exists()) {
            this.playerFolder.mkdirs();
        }

        settingsFile = new File(dataFolder, "Config.yml");
        try {
            loadData();
        } catch (Exception ex) {
            plugin.getLogger().log(Level.INFO, "Failed to load config file, disabling", ex);
            server.getPluginManager().disablePlugin(plugin);
        }
    }

    public SBSettings(Server server, SpawnButtons plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    public void addPlayer(final SBPlayer p) {
        if (getPlayer(p.getName()) == null) {
            players.add(p);
        }
    }

    public SBPlayer addNewPlayer(String name) {
        SBPlayer p = new SBPlayer();
        p.setName(name);
        p.setPlayerFile(new File(playerFolder.getAbsoluteFile() + "/" + name + ".dat"));
        players.add(p);

        return p;
    }

    public SBPlayer getPlayer(final String name) {
        for (final SBPlayer p : players) {
            if (p.isPlayer(name)) {
                return p;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void loadData() throws FileNotFoundException, IOException, InvalidConfigurationException {
        YamlConfiguration config = new YamlConfiguration();

        if (settingsFile.exists()) {
            config.load(settingsFile);
        }

        loadBlocks();

        FileFilter fileFilter = new FileFilter() {

            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(".dat");
            }
        };

        File[] playerFiles = playerFolder.listFiles(fileFilter);

        for (File pl : playerFiles) {
            String name = pl.getName().replace(".dat", "");

            SBPlayer p = getPlayer(name);

            if (p == null) {
                p = new SBPlayer();
                players.add(p);
            }
            p.setServer(getServer());
            p.setPlayerFile(pl);
            p.setName(name);
            p.load();
        }
    }

    public void loadBlocks() {
        File blocksFile = new File(dataFolder.getAbsoluteFile() + "/blocks.dat");

        if (blocksFile.exists()) {
            try {
                FileReader f = new FileReader(blocksFile);
                BufferedReader reader = new BufferedReader(f);
                String line = "";
                while ((line = reader.readLine()) != null) {
                    String[] loc = line.split("::");
                    if (loc.length != 4) {
                        continue;
                    }

                    World w = getServer().getWorld(loc[0]);
                    int x = Integer.parseInt(loc[1]);
                    int y = Integer.parseInt(loc[2]);
                    int z = Integer.parseInt(loc[3]);

                    SpawnBlock b = new SpawnBlock();
                    b.setLocation(new Location(w, x, y, z));

                    blocks.add(b);

                }

                f.close();

            } catch (FileNotFoundException e) {
                plugin.getLogger().log(Level.INFO, "Blocks file: Not found", e);
            } catch (IOException e) {
                plugin.getLogger().log(Level.INFO, "Blocks file: IO Exception", e);
            }
        }
    }

    public void saveBlocks() {
        File blocksFile = new File(dataFolder.getAbsoluteFile() + "/blocks.dat");
        try {
            FileWriter f = new FileWriter(blocksFile);
            BufferedWriter writer = new BufferedWriter(f);
            String line = "";
            for (SpawnBlock s : blocks) {
                line = s.getLocation().getWorld().getName() + "::" + s.getLocation().getBlockX() + "::" + s.getLocation().getBlockY() + "::" + s.getLocation().getBlockZ() + "\n";
                writer.write(line);
                writer.flush();
            }

            f.close();

        } catch (FileNotFoundException e) {
            plugin.getLogger().log(Level.INFO, "Configuration file not found", e);
        } catch (IOException e) {
            plugin.getLogger().log(Level.INFO, "IO Error with configuration file", e);
        }
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
