package net.deaygo.SpawnButtons.commands;

import net.deaygo.SpawnButtons.SpawnButtons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnBlocksCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] params) {
        Player p;
        if (sender instanceof Player) {
            p = (Player) sender;
        } else {
            return false;
        }

        if (p.hasPermission("spawnblocks.create")) {
            p.getInventory().addItem(new ItemStack(Material.GLOWSTONE, 1));
            p.sendMessage(ChatColor.GREEN + "Here you go!");
            return true;
        } else {
            p.sendMessage(ChatColor.RED + "You do not have permissions to use this command.");
            return false;
        }
    }
}
