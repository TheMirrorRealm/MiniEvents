package Handlers;

import Mains.MiniEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EquipArmor {
    public MiniEvents plugin;

    public EquipArmor(MiniEvents plugin) {
        this.plugin = plugin;
    }

    public void equip(Player player, boolean tdm, String redBlue) {
        String s = plugin.getEventName();
        FileConfiguration fc = plugin.customFile("loadout");
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        if (player.getActivePotionEffects() != null) {
            for (PotionEffect p : player.getActivePotionEffects()) {
                player.removePotionEffect(p.getType());
            }
        }
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20.0F);
        if (!tdm) {
            if (fc.getConfigurationSection("setup." + s) != null) {
                for (String n : fc.getConfigurationSection("setup." + s + ".inv.").getKeys(false)) {
                    player.getInventory().setItem(Integer.parseInt(n), fc.getItemStack("setup." + s + ".inv." + n));
                }
                player.getInventory().setHelmet(fc.getItemStack("setup." + s + ".armor.103"));
                player.getInventory().setChestplate(fc.getItemStack("setup." + s + ".armor.102"));
                player.getInventory().setLeggings(fc.getItemStack("setup." + s + ".armor.101"));
                player.getInventory().setBoots(fc.getItemStack("setup." + s + ".armor.100"));
                if (fc.getConfigurationSection("setup." + s + ".potion") != null) {
                    for (String m : fc.getConfigurationSection("setup." + s + ".potion.name.").getKeys(false)) {
                        String v = fc.getString("setup." + s + ".potion.name." + m + ".type");
                        int n = fc.getInt("setup." + s + ".potion.name." + m + ".level");
                        int l = fc.getInt("setup." + s + ".potion.name." + m + ".duration");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(v), l, n));
                    }
                }
            }
        } else {
            if (fc.getConfigurationSection("setup.tdm." + redBlue) != null) {
                for (String n : fc.getConfigurationSection("setup." + redBlue + ".inv.").getKeys(false)) {
                    player.getInventory().setItem(Integer.parseInt(n), fc.getItemStack("setup." + s + ".inv." + n));
                }
                player.getInventory().setHelmet(fc.getItemStack("setup.tdm." + redBlue + ".armor.103"));
                player.getInventory().setChestplate(fc.getItemStack("setup.tdm." + redBlue + ".armor.102"));
                player.getInventory().setLeggings(fc.getItemStack("setup.tdm." + redBlue + ".armor.101"));
                player.getInventory().setBoots(fc.getItemStack("setup.tdm." + redBlue + ".armor.100"));
                if (fc.getConfigurationSection("setup.tdm." + redBlue + ".potion") != null) {
                    for (String m : fc.getConfigurationSection("setup.tdm." + redBlue + ".potion.name.").getKeys(false)) {
                        String v = fc.getString("setup.tdm." + redBlue + ".potion.name." + m + ".type");
                        int n = fc.getInt("setup.tdm." + redBlue + ".potion.name." + m + ".level");
                        int l = fc.getInt("setup.tdm." + redBlue + ".potion.name." + m + ".duration");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(v), l, n));
                    }
                }
            }
        }
    }
}
