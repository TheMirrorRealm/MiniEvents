package Util.Methods;

import API.CustomEvents.MiniEventsEndEvent;
import API.CustomEvents.MiniEventsLeaveEvent;
import API.Info.ApiDo;
import Mains.MiniEvents;
import Mains.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

public class Do implements ApiDo {
    SettingsManager settings = SettingsManager.getInstance();

    public MiniEvents plugin;

    public Do(MiniEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public void endEvent(boolean removeAll) {
        Bukkit.getServer().getPluginManager().callEvent(new MiniEventsEndEvent(plugin.getEventName(), removeAll));
    }

    @Override
    public void loadPlayerInventory(final Player p) throws NullPointerException {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                p.getInventory().clear();
                p.getInventory().setArmorContents(null);
                FileConfiguration fc = plugin.playerFile(p.getName().toLowerCase());
                Set<String> a = fc.getConfigurationSection(p.getName().toLowerCase() + ".inv.").getKeys(false);

                for (String s : a) {
                    p.getInventory().setItem(Integer.parseInt(s), fc.getItemStack(p.getName().toLowerCase() + ".inv." + s));
                }
                p.getInventory().setHelmet(fc.getItemStack(p.getName().toLowerCase() + ".armor.103"));
                p.getInventory().setChestplate(fc.getItemStack(p.getName().toLowerCase() + ".armor.102"));
                p.getInventory().setLeggings(fc.getItemStack(p.getName().toLowerCase() + ".armor.101"));
                p.getInventory().setBoots(fc.getItemStack(p.getName().toLowerCase() + ".armor.100"));

                if (fc.getConfigurationSection(p.getName().toLowerCase() + ".potion") != null) {
                    for (String m : fc.getConfigurationSection(p.getName().toLowerCase() + ".potion.name.").getKeys(false)) {
                        String s = fc.getString(p.getName().toLowerCase() + ".potion.name." + m + ".type");
                        int n = fc.getInt(p.getName().toLowerCase() + ".potion.name." + m + ".level");
                        int i = fc.getInt(p.getName().toLowerCase() + ".potion.name." + m + ".duration");
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(s), i, n));
                    }
                }
            }
        }, 20L);
    }
    @Override
    public void putPlayerInSpectate(final Player player) {
        plugin.getSpectateMode().inspec.add(player.getName());
        if (settings.getData().getConfigurationSection(plugin.getEventName() + ".spec.") != null) {
            World w = Bukkit.getServer().getWorld(settings.getData().getString(plugin.getEventName() + ".spec.world"));
            double x = settings.getData().getDouble(plugin.getEventName() + ".spec.x");
            double y = settings.getData().getDouble(plugin.getEventName() + ".spec.y");
            double z = settings.getData().getDouble(plugin.getEventName() + ".spec.z");
            float yaw = Float.intBitsToFloat(settings.getData().getInt(plugin.getEventName() + ".spec.yaw"));
            float pitch = Float.intBitsToFloat(settings.getData().getInt(plugin.getEventName() + ".spec.pitch"));
            final Location l = new Location(w, x, y, z, yaw, pitch);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);
                    player.teleport(l);
                    plugin.send(player, "now-in-spectate");
                }
            }, 3L);
        } else {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);
                    removePlayerFromEvent(player, true, true);
                }
            }, 3L);
        }
    }

    @Override
    public void removePlayerFromEvent(Player player, boolean spawn, boolean loadInventory) {
        Bukkit.getServer().getPluginManager().callEvent(new MiniEventsLeaveEvent(player, plugin.getEventName(),
                plugin.getInfo().inevent.size(), plugin.getTimerMain().getTimeLeft(), plugin.getInfo().inevent));
        if (spawn) {
            teleportPlayerToQuitPoint(player);
        }
        if (loadInventory) {
            loadPlayerInventory(player);
        }
    }

    @Override
    public void savePlayerInventory(Player p) {
        FileConfiguration fc = plugin.playerFile(p.getName().toLowerCase());
        ItemStack[] i = p.getInventory().getContents();
        ItemStack[] a = p.getInventory().getArmorContents();
        int slot = 0;
        for (ItemStack stack : i) {
            fc.set(p.getName().toLowerCase() + ".inv." + slot, stack);
            slot++;
        }
        int sslot = 100;
        for (ItemStack stack : a) {
            fc.set(p.getName().toLowerCase() + ".armor." + sslot, stack);
            sslot++;
        }
        int ii = 0;
        fc.set(p.getName() + ".potion", null);
        for (PotionEffect pp : p.getActivePotionEffects()) {
            String ll = Integer.toString(ii);
            fc.set(p.getName().toLowerCase() + ".potion.name." + ll + ".type", pp.getType().getName());
            fc.set(p.getName().toLowerCase() + ".potion.name." + ll + ".level", pp.getAmplifier());
            fc.set(p.getName().toLowerCase() + ".potion.name." + ll + ".duration", pp.getDuration());
            ii++;
        }
        try {
            fc.save(plugin.playerData(p.getName().toLowerCase()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startCountDown(String eventName) {
        plugin.getCountDown().EventWait(eventName);
    }

    @Override
    public void startTimer(String eventName) {
        plugin.getTimerMain().setTime(eventName);
    }

    @Override
    public void stopTimer() {
        plugin.getTimerMain().stop = true;
    }

    @Override
    public void teleportPlayerToQuitPoint(final Player player) throws NullPointerException {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                World w = Bukkit.getServer().getWorld(settings.getData().getString("quit.world"));
                double x = settings.getData().getDouble("quit.x");
                double y = settings.getData().getDouble("quit.y");
                double z = settings.getData().getDouble("quit.z");
                float yaw = Float.intBitsToFloat(settings.getData().getInt("quit.yaw"));
                float pitch = Float.intBitsToFloat(settings.getData().getInt("quit.pitch"));
                final Location spawn = new Location(w, x, y, z, yaw, pitch);
                player.teleport(spawn);
            }
        }, 20L);
    }

    @Override
    public void updateEventScoreboard(Player player, Integer time, boolean setOrUpdate) {

    }
}
