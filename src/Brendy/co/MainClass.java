package Brendy.co;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MainClass
  extends JavaPlugin
  implements Listener
{
  String JoinMessage;
  String QuitMessage;
  boolean StaffJoinMessage;
  String StaffMessage;
  String perm;
  
  public void onEnable()
  {
    System.out.print("Plugin Enabled");
    checkConfig();
    loadMessages();
    Bukkit.getPluginManager().registerEvents(this, this);
  }
  
  public void onDisable()
  {
    System.out.print("Plugin Disabled");
  }
  
  public void loadMessages()
  {
    this.JoinMessage = getConfig().getString("JoinMessage");
    this.QuitMessage = getConfig().getString("QuitMessage");
    this.StaffJoinMessage = getConfig().getBoolean("StaffJoinMessage");
    if (this.StaffJoinMessage)
    {
      this.StaffMessage = getConfig().getString("StaffMessage");
      this.perm = getConfig().getString("StaffMessagePerm");
    }
  }
  
  public void checkConfig()
  {
    File file = new File("/CustomMessages/config.yml");
    if ((file.exists()) && (!file.isDirectory())) {
      return;
    }
    saveDefaultConfig();
  }
  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e)
  {
    e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', this.JoinMessage.replace("%player%", e.getPlayer().getName())));
    if ((this.StaffJoinMessage) && (e.getPlayer().hasPermission(this.perm))) {
      Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.StaffMessage).replace("%player%", e.getPlayer().getName()));
    }
  }
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e)
  {
    e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', this.QuitMessage).replace("%player%", e.getPlayer().getName()));
  }
  
  public boolean onCommand(CommandSender s, Command cmd, String label, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("cm"))
    {
      if (args.length == 0)
      {
        s.sendMessage(ChatColor.RED + "Not enough arguments");
        return true;
      }
      if ((args.length == 1) && 
        (args[0].equalsIgnoreCase("reload")))
      {
        if (!(s instanceof Player))
        {
          reloadConfig();
          s.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cConfig Reloaded"));
          return true;
        }
        Player p = (Player)s;
        if (!p.hasPermission("cm.reload"))
        {
          p.sendMessage(ChatColor.RED + "You don't have access to this command!");
          return true;
        }
        reloadConfig();
        p.sendMessage(ChatColor.YELLOW + "Config reloaded!");
        return true;
      }
    }
    return true;
  }
  
  @EventHandler
  public void onDeath(PlayerDeathEvent e) {
	  e.setDeathMessage("");
  }
}