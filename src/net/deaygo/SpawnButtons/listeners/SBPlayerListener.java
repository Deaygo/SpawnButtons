package net.deaygo.SpawnButtons.listeners;

import net.deaygo.SpawnButtons.SpawnButtons;
import net.deaygo.SpawnButtons.model.SBPlayer;
import net.deaygo.SpawnButtons.model.SBSettings;
import net.deaygo.SpawnButtons.model.SpawnBlock;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SBPlayerListener implements Listener {

    private static final long serialVersionUID = 4308676690438696095L;
    SBSettings settings;

    public SBPlayerListener(SBSettings settings) {
        this.settings = settings;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        SBPlayer s = settings.getPlayer(event.getPlayer().getName());
        if (s != null) {
            if (event.getPlayer().hasPermission("spawnblocks.use")) {
                event.setRespawnLocation(new Location(s.getSpawn().getWorld(), s.getSpawn().getX(), s.getSpawn().getY(), s.getSpawn().getZ()));
            }
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        SBPlayer p = settings.getPlayer(event.getPlayer().getName());
        Block b = event.getBed();
        if (p == null) {
            p = settings.addNewPlayer(event.getPlayer().getName());
        }
        if (event.getPlayer().hasPermission("spawnblocks.use")) {
            p.setSpawn(new Location(b.getWorld(), b.getX(), b.getY(), b.getZ() + 1));
            event.getPlayer().sendMessage(ChatColor.GREEN + "This is now your new spawn point.");
            p.save();
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();

        if (b != null) {

            if (b.getType() == Material.STONE_BUTTON || b.getType() == Material.STONE_PLATE || b.getType() == Material.WOOD_PLATE) {
                if (event.getPlayer().getItemInHand().getType() == Material.GLOWSTONE && event.getAction() != Action.PHYSICAL) {
                    if (event.getPlayer().hasPermission("spawnblocks.create")) {
                        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            if (settings.isSpawnBlock(b)) {
                                event.getPlayer().sendMessage(ChatColor.RED + "This block is already a registered way point.");
                                event.setCancelled(true);
                            } else {
                                SpawnBlock sb = new SpawnBlock();
                                sb.setLocation(new Location(b.getWorld(), b.getX(), b.getY(), b.getZ()));
                                settings.addBlock(sb);
                                event.getPlayer().sendMessage(ChatColor.GREEN + "This is now registered as a way point block.");
                                event.setCancelled(true);
                            }
                        }
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (!settings.isSpawnBlock(b)) {
                                event.getPlayer().sendMessage(ChatColor.RED + "This is not a registered way point block");
                                event.setUseItemInHand(Result.DENY);
                                event.setUseInteractedBlock(Result.DENY);
                                event.setCancelled(true);
                            } else {
                                settings.removeBlock(b);
                                event.getPlayer().sendMessage(ChatColor.GREEN + "This way point block has been unregistered.");
                                event.setUseItemInHand(Result.DENY);
                                event.setUseInteractedBlock(Result.DENY);
                                event.setCancelled(true);
                                settings.saveBlocks();
                            }
                        }
                    } else {
                        event.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                    }
                } else {
                    if (settings.isSpawnBlock(b)) {
                        if (event.getPlayer().hasPermission("spawnblocks.use")) {
                            SBPlayer p = settings.getPlayer(event.getPlayer().getName());

                            if (p == null) {
                                p = settings.addNewPlayer(event.getPlayer().getName());
                            }
                            p.setSpawn(new Location(b.getWorld(), b.getX(), b.getY(), b.getZ() + 1));
                            event.getPlayer().sendMessage(ChatColor.GREEN + "This is now your new spawn point.");
                            p.save();
                        }
                    }
                }
            }
        }
    }
}
