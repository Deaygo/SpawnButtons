package net.deaygo.SpawnButtons.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

public class SBPlayer  {

    private Server server;
    private boolean abilityEnabled;
    private String name;
    private File playerFile;
    private Location spawn;

    public void setAbilityEnabled(boolean hasStick) {
        this.abilityEnabled = hasStick;
    }

    public boolean isAbilityEnabled() {
        return abilityEnabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public boolean isPlayer(final String name) {
        return name.equalsIgnoreCase(getName());
    }

    public void setPlayerFile(File playerFile) {
        this.playerFile = playerFile;
    }

    public File getPlayerFile() {
        return playerFile;
    }
    
    public void load() {
        try
        {
            FileReader f = new FileReader(getPlayerFile());
            BufferedReader reader = new BufferedReader(f);
            String line = "";
            
            while ( (line = reader.readLine()) != null ) {
                if (line.startsWith("spawn=")) {
                    
                    String[] loc = line.replace("spawn=", "").split("::");
                    if ( loc.length != 4 )
                        continue;
                    
                    World w = getServer().getWorld(loc[0]);
                    double x = Double.parseDouble(loc[1]);
                    double y = Double.parseDouble(loc[2]);
                    double z = Double.parseDouble(loc[3]);
                
                    setSpawn(new Location(w, x, y, z));
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        try
        {
            FileWriter f = new FileWriter(getPlayerFile());
            BufferedWriter writer = new BufferedWriter(f);
            String line = "spawn=" + getSpawn().getWorld().getName() + "::" + getSpawn().getX() + "::" + getSpawn().getY() + "::" + getSpawn().getZ() + "\n";
            
            writer.write(line);
            writer.flush();
            f.close();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return server;
    }
    
}
