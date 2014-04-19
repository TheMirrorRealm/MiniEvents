package Mains;

import API.CustomEvents.MiniEventsInitiateEvent;
import API.CustomEvents.MiniEventsJoinEvent;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.*;

public class EventsMain implements CommandExecutor {
    SettingsManager settings = SettingsManager.getInstance();
    public MiniEvents plugin;

    public EventsMain(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public boolean passed(Player player, String s) {
        boolean passed = true;
        if (settings.getData().getConfigurationSection(s) == null) {
            plugin.send(player, s + "-spawn-not-set");
            passed = false;
        } else if (settings.getData().getConfigurationSection("quit") == null) {
            plugin.send(player, "quit-not-set");
            passed = false;
        } else if (plugin.getInfo().eventstarting || plugin.getInfo().eventstarted) {
            plugin.send(player, "event-in-progress");
            passed = false;
        }
        if (passed) {
            MiniEventsInitiateEvent event = new MiniEventsInitiateEvent(player, s, plugin.getTimerMain().getTimeLeft());
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                passed = true;
                plugin.getInfo().eventstarting = true;
                plugin.getInfo().eventstarted = false;
                plugin.getInfo().cancelled = false;
                plugin.getCountDown().EventWait(s);
            } else {
                passed = false;
            }
        }
        return passed;
    }

    public void announce(Player player) {
        List<String> event = plugin.getConfig().getStringList("events.starting");
        for (String e : event) {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', e)
                    .replace("{EVENT}", plugin.getFormalName()).replace("{PLAYER}", player.getName()));
        }
    }

    public void checkPassed(String s, Player player) {
        if (passed(player, s)) {
            plugin.eventName = s;
            announce(player);
        }
    }

    int count = 1;

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equals("event")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!(player.hasPermission("event.start") || player.hasPermission("event.*") || player.isOp())) {
                    plugin.send(player, "no-permission");
                    return true;
                } else {
                    if (args.length == 0 || args.length >= 2) {
                        for (String s : plugin.getConfig().getStringList("events.help-menu")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                        }
                        return true;
                    } else {
                        if (args.length == 1) {
                            String arg = args[0].toLowerCase();
                            switch (arg) {
                                case "lms":
                                    checkPassed("lms", player);
                                    break;
                                case "oitc":
                                    checkPassed("oitc", player);
                                    break;
                                case "tnt":
                                    checkPassed("tnt", player);
                                    break;
                                case "paint":
                                    checkPassed("paint", player);
                                    break;
                                case "spleef":
                                    checkPassed("spleef", player);
                                    break;
                                case "horse":
                                    checkPassed("horse", player);
                                    break;
                                case "ko":
                                    checkPassed("ko", player);
                                    break;
                                case "parkour":
                                    if (settings.getData().getConfigurationSection("parkour.top") == null) {
                                        plugin.send(player, "parkour-top-not-set");
                                        return true;
                                    }
                                    if (settings.getData().getString("parkour.world") == null) {
                                        plugin.send(player, "parkour-spawn-not-set");
                                        return true;
                                    }
                                    checkPassed("parkour", player);
                                    break;
                                case "koth":
                                    if (settings.getData().getString("koth.world") == null) {
                                        plugin.send(player, "koth-top-not-set");
                                        return true;
                                    }
                                    if (settings.getData().getConfigurationSection("koth.top") == null) {
                                        plugin.send(player, "koth-spawn-not-set");
                                        return true;
                                    }
                                    checkPassed("koth", player);
                                    break;
                                case "tdm":
                                    if (settings.getData().getConfigurationSection("tdm.red") == null) {
                                        plugin.send(player, "tdm-red-spawn-not-set");
                                        return true;
                                    } else if (settings.getData().getConfigurationSection("tdm.blue") == null) {
                                        plugin.send(player, "tdm-blue-spawn-not-set");
                                        return true;
                                    } else if (plugin.getInfo().eventstarting || plugin.getInfo().eventstarted) {
                                        plugin.send(player, "event-in-progress");
                                        return true;
                                    } else {
                                        plugin.eventName = "tdm";
                                        announce(player);
                                        plugin.getInfo().eventstarting = true;
                                        plugin.getInfo().eventstarted = false;
                                        plugin.getInfo().cancelled = false;
                                        plugin.getCountDown().EventWait("tdm");
                                    }
                                    break;
                                case "reload":
                                    if (!(player.hasPermission("event.reload") || player.hasPermission("event.*") || player.isOp())) {
                                        plugin.send(player, "no-permission");
                                        return true;
                                    } else {
                                        plugin.reloadConfig();
                                        plugin.getLangYAML().reloadLang();
                                        plugin.send(player, "reloaded-config");
                                    }
                                    break;
                                case "end":
                                    if (player.hasPermission("event.end") || player.hasPermission("event.*") || player.isOp()) {
                                        if (plugin.getInfo().eventstarted) {
                                            for (final Player o : Bukkit.getServer().getOnlinePlayers()) {
                                                if (plugin.getInfo().inevent.containsKey(o.getName())) {
                                                    if (o.isInsideVehicle()) {
                                                        if (o.getVehicle() instanceof Horse) {
                                                            o.getVehicle().remove();
                                                        }
                                                    }
                                                    plugin.getDo().removePlayerFromEvent(o, true, true);
                                                }
                                            }
                                            plugin.getDo().endEvent(true);
                                            plugin.broadcast("ended-event");
                                            return true;
                                        } else if (plugin.getInfo().eventstarting) {
                                            for (final Player o : Bukkit.getServer().getOnlinePlayers()) {
                                                if (plugin.getInfo().inevent.containsKey(o.getName())) {
                                                    plugin.getInfo().inevent.remove(player.getName());
                                                }
                                            }
                                            plugin.getDo().endEvent(false);
                                            plugin.broadcast("ended-event");
                                        } else {
                                            plugin.send(player, "no-events-to-end");
                                        }
                                    } else {
                                        plugin.send(player, "no-permission");
                                    }
                                    break;
                                default:
                                    for (String s : plugin.getConfig().getStringList("events.help-menu")) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                                    }
                            }
                        }
                    }
                }
            }
        }
        if (cmd.getName().equalsIgnoreCase("join")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!(player.hasPermission("event.join") || player.hasPermission("event.*") || player.isOp())) {
                    plugin.send(player, "no-permission");
                    return true;
                } else {
                    if (args.length >= 0) {
                        if (plugin.getInfo().inevent.containsKey(player.getName())) {
                            plugin.send(player, "already-in-event");
                            return true;
                        }
                        if (!plugin.getInfo().eventstarting) {
                            plugin.send(player, "no-events");
                            return true;
                        } else {
                            plugin.getInfo().inevent.put(player.getName(), plugin.getEventName());
                            plugin.send(player, "joined-event", Integer.toString(plugin.getInfo().inevent.size()));
                            if (plugin.getEventName().equals("tdm")) {
                                if (count % 2 == 0) {
                                    plugin.getInfo().sblue.add(player.getName());
                                } else {
                                    plugin.getInfo().sred.add(player.getName());
                                }
                                count++;
                            }
                            Bukkit.getServer().getPluginManager().callEvent(new MiniEventsJoinEvent(player, plugin.getEventName(),
                                    plugin.getInfo().inevent.size(), plugin.getTimerMain().getTimeLeft(), plugin.getInfo().inevent));
                        }
                    }
                }
            }
        }
        return true;
    }
}
