package Util;

import Mains.MiniEvents;
import Mains.SettingsManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.IOException;

public class Commands implements CommandExecutor {
    SettingsManager settings = SettingsManager.getInstance();
    public MiniEvents plugin;

    public Commands(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public void save(Player player, String ss) {
        FileConfiguration fc = plugin.customFile("loadout");
        int slot = 0;
        for (ItemStack stack : player.getInventory().getContents()) {
            fc.set("setup." + ss + ".inv." + slot, stack);
            slot++;
        }
        int sslot = 100;
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            fc.set("setup." + ss + ".armor." + sslot, stack);
            sslot++;
        }
        int l = 0;
        for (PotionEffect p : player.getActivePotionEffects()) {
            fc.set("setup." + ss + ".potion.name." + l + ".type", p.getType().getName());
            fc.set("setup." + ss + ".potion.name." + l + ".level", p.getAmplifier());
            fc.set("setup." + ss + ".potion.name." + l + ".duration", p.getDuration());
            l++;
        }
        try {
            fc.save(plugin.customData("loadout"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setSpec(Player player, String s) {
        Location l = player.getLocation();
        settings.getData().set(s + ".spec.world", l.getWorld().getName());
        settings.getData().set(s + ".spec.x", Double.valueOf(l.getX()));
        settings.getData().set(s + ".spec.y", Double.valueOf(l.getY()));
        settings.getData().set(s + ".spec.z", Double.valueOf(l.getZ()));
        settings.getData().set(s + ".spec.yaw", Float.floatToIntBits(l.getYaw()));
        settings.getData().set(s + ".spec.pitch", Float.floatToIntBits(l.getPitch()));
        plugin.send(player, "spec-set", plugin.getFormalName(s));
        settings.saveData();
    }
    public void setSpawn(Player player, String s) {
        Location l = player.getLocation();
        settings.getData().set(s + ".world", l.getWorld().getName());
        settings.getData().set(s + ".x", Double.valueOf(l.getX()));
        settings.getData().set(s + ".y", Double.valueOf(l.getY()));
        settings.getData().set(s + ".z", Double.valueOf(l.getZ()));
        settings.getData().set(s + ".yaw", Float.floatToIntBits(l.getYaw()));
        settings.getData().set(s + ".pitch", Float.floatToIntBits(l.getPitch()));
        plugin.send(player, "set", plugin.getFormalName(s));
        settings.saveData();
    }

    public void a(Player player, String s, String[] args, boolean top) {
        if (!top) {
            if (args[0].equalsIgnoreCase("setspawn")) {
                setSpawn(player, s);
            } else if (args[0].equalsIgnoreCase("setinv")) {
                save(player, s);
                plugin.send(player, "saved-inv", plugin.getFormalName(s));
            } else if (args[0].equalsIgnoreCase("setspec")) {
                setSpec(player, s);
            } else if (args[0].equalsIgnoreCase("delinv")) {
                settings.getData().set("setup." + s, null);
                plugin.send(player, "deleted-inv", plugin.getFormalName(s));
            } else if (args[0].equalsIgnoreCase("delspec")) {
                settings.getData().set("setup." + s + ".spec", null);
                plugin.send(player, "deleted-spec", plugin.getFormalName(s));
            } else {
                plugin.send(player, s + "-usage");
            }
            settings.saveData();
        } else {
            if (args[0].equalsIgnoreCase("setspawn")) {
                setSpawn(player, s);
            } else if (args[0].equalsIgnoreCase("setinv")) {
                save(player, s);
                plugin.send(player, "saved-inv", plugin.getFormalName(s));
            } else if (args[0].equalsIgnoreCase("setspec")) {
                setSpec(player, s);
            } else if (args[0].equalsIgnoreCase("delinv")) {
                settings.getData().set("setup." + s, null);
                plugin.send(player, "deleted-inv", plugin.getFormalName(s));
            } else if (args[0].equalsIgnoreCase("delspec")) {
                settings.getData().set("setup." + s + ".spec", null);
                plugin.send(player, "deleted-spec", plugin.getFormalName(s));
            } else if (args[0].equalsIgnoreCase("settop")) {
                Location l = player.getLocation();
                settings.getData().set(s + ".top.world", l.getWorld().getName());
                settings.getData().set(s + ".top.x", Double.valueOf(l.getX()));
                settings.getData().set(s + ".top.y", Double.valueOf(l.getY()));
                settings.getData().set(s + ".top.z", Double.valueOf(l.getZ()));
                settings.getData().set(s + ".top.yaw", Float.floatToIntBits(l.getYaw()));
                settings.getData().set(s + ".top.pitch", Float.floatToIntBits(l.getPitch()));
                plugin.send(player, s + "-set-top");
                settings.saveData();
            } else {
                plugin.send(player, s + "-usage");
            }
            settings.saveData();
        }
    }

    public void b(CommandSender sender, String[] args, String s, boolean b) {
        Player player = (Player) sender;
        if (!(player.hasPermission("event.set") || player.hasPermission("event.*") || player.isOp())) {
            plugin.send(player, "no-permission");
        } else {
            if (args.length == 0 || args.length >= 2) {
                plugin.send(player, s + "-usage");
                return;
            }
            if (args.length == 1) {
                a(player, s, args, b);
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equals("lms")) {
                b(sender, args, "lms", false);
            }
            if (cmd.getName().equals("tntrun")) {
                b(sender, args, "tnt", false);
            }
            if (cmd.getName().equals("ko")) {
                b(sender, args, "ko", false);
            }
            if (cmd.getName().equals("spleef")) {
                b(sender, args, "spleef", false);
            }
            if (cmd.getName().equals("paint")) {
                b(sender, args, "paint", false);
            }
            if (cmd.getName().equals("horse")) {
                b(sender, args, "horse", false);
            }
            if (cmd.getName().equals("parkour")) {
                b(sender, args, "parkour", true);
            }
            if (cmd.getName().equals("koth")) {
                b(sender, args, "koth", true);
            }
            if (cmd.getName().equals("tdm")) {
                Player player = (Player) sender;
                if (!(player.hasPermission("event.set") || player.hasPermission("event.*") || player.isOp())) {
                    plugin.send(player, "no-permission");
                    return true;
                } else {
                    if (args.length <= 1 || args.length >= 3) {
                        for (String s : plugin.getLangYAML().getLang().getStringList("tdm-usage")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                        }
                        return true;
                    }
                    if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("setspawn")) {
                            switch (args[1].toLowerCase()) {
                                case "red":
                                    Location l = player.getLocation();
                                    settings.getData().set("tdm.red.world", l.getWorld().getName());
                                    settings.getData().set("tdm.red.x", Double.valueOf(l.getX()));
                                    settings.getData().set("tdm.red.y", Double.valueOf(l.getY()));
                                    settings.getData().set("tdm.red.z", Double.valueOf(l.getZ()));
                                    settings.getData().set("tdm.red.yaw", Float.floatToIntBits(l.getYaw()));
                                    settings.getData().set("tdm.red.pitch", Float.floatToIntBits(l.getPitch()));
                                    plugin.send(player, "tdm-red-set");
                                    settings.saveData();
                                    break;
                                case "blue":
                                    Location ll = player.getLocation();
                                    settings.getData().set("tdm.blue.world", ll.getWorld().getName());
                                    settings.getData().set("tdm.blue.x", Double.valueOf(ll.getX()));
                                    settings.getData().set("tdm.blue.y", Double.valueOf(ll.getY()));
                                    settings.getData().set("tdm.blue.z", Double.valueOf(ll.getZ()));
                                    settings.getData().set("tdm.blue.yaw", Float.floatToIntBits(ll.getYaw()));
                                    settings.getData().set("tdm.blue.pitch", Float.floatToIntBits(ll.getPitch()));
                                    plugin.send(player, "tdm-blue-set");
                                    settings.saveData();
                                    break;
                                default:
                                    for (String s : plugin.getLangYAML().getLang().getStringList("tdm-usage")) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                                    }
                                    break;
                            }
                            return true;
                        } else if (args[0].equalsIgnoreCase("setinv")) {
                            switch (args[1].toLowerCase()) {
                                case "red":
                                    save(player, "tdm.red");
                                    plugin.send(player, "saved-inv", "TDM (Red)");
                                    break;
                                case "blue":
                                    save(player, "tdm.blue");
                                    plugin.send(player, "saved-inv", "TDM (Blue)");
                                    break;
                                default:
                                    for (String s : plugin.getLangYAML().getLang().getStringList("tdm-usage")) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                                    }
                                    break;
                            }
                            return true;
                        } else if (args[0].equalsIgnoreCase("setspec")) {
                            setSpec(player, "tdm");
                        } else if (args[0].equalsIgnoreCase("delspec")) {
                            settings.getData().set("setup.tdm.spec", null);
                            plugin.send(player, "deleted-spec", plugin.getFormalName("tdm"));
                        } else if (args[0].equalsIgnoreCase("delinv")) {
                            switch (args[1].toLowerCase()) {
                                case "red":
                                    settings.getData().set("setup.tdm.red", null);
                                    plugin.send(player, "deleted-inv", "TDM (Red Team)");
                                    break;
                                case "blue":
                                    settings.getData().set("setup.tdm.blue", null);
                                    plugin.send(player, "deleted-inv", "TDM (Blue Team)");
                                    break;
                                default:
                                    for (String s : plugin.getLangYAML().getLang().getStringList("tdm-usage")) {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                                    }
                                    break;
                            }
                            return true;
                        } else {
                            plugin.send(player, "tdm-usage");
                            return true;
                        }
                        settings.saveData();
                    }
                }
            }
            if (cmd.getName().equals("oitc")) {
                Player player = (Player) sender;
                if (!(player.hasPermission("event.set") || player.hasPermission("event.*") || player.isOp())) {
                    plugin.send(player, "no-permission");
                    return true;
                } else {
                    if (args.length == 0 || args.length >= 3) {
                        plugin.send(player, "oitc-usage");
                        return true;
                    }
                    if (args.length == 1) {
                        a(player, "oitc", args, false);
                    }
                    if (args.length == 2) {
                        String argg = args[0].toLowerCase();
                        switch (argg) {
                            case "delspawn":
                                if (settings.getData().getConfigurationSection("oitc.rspawn." + args[1]) == null) {
                                    plugin.send(player, "oitc-null-spawn", args[1]);
                                    StringBuilder sb = new StringBuilder();
                                    for (String s : settings.getData().getConfigurationSection("oitc.rspawn.").getKeys(false)) {
                                        sb.append(StringUtils.capitalize(s.toLowerCase()));
                                        sb.append(ChatColor.RED);
                                        sb.append(", ");
                                        sb.append(ChatColor.RED);
                                    }
                                    player.sendMessage(ChatColor.RED + "Available spawns: " + sb.substring(0, sb.length() - 4));
                                } else {
                                    settings.getData().set("oitc.rspawn." + args[1], null);
                                    plugin.send(player, "oitc-deleted-spawn", args[1]);
                                }
                                break;
                            case "addspawn":
                                try {
                                    int ll = Integer.parseInt(args[1]);
                                    String ii = Integer.toString(ll);
                                    Location l = player.getLocation();
                                    this.settings.getData().set("oitc.rspawn." + ii + ".world", l.getWorld().getName());
                                    settings.getData().set("oitc.rspawn." + ii + ".x", Double.valueOf(l.getX()));
                                    settings.getData().set("oitc.rspawn." + ii + ".y", Double.valueOf(l.getY()));
                                    settings.getData().set("oitc.rspawn." + ii + ".z", Double.valueOf(l.getZ()));
                                    settings.getData().set("oitc.rspawn." + ii + ".yaw", Float.floatToIntBits(l.getYaw()));
                                    settings.getData().set("oitc.rspawn." + ii + ".pitch", Float.floatToIntBits(l.getPitch()));
                                    plugin.send(player, "oitc-added-spawn", args[1]);
                                    settings.saveData();
                                    return true;
                                } catch (NumberFormatException e) {
                                    plugin.send(player, "oitc-usage");
                                }
                                break;
                            default:
                                plugin.send(player, "oitc-usage");
                        }
                    }
                }
            }
        }
        return true;
    }
}
