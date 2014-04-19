package Mains;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class SettingsManager {
    private SettingsManager() { }

    static SettingsManager instance = new SettingsManager();

    public static SettingsManager getInstance() {
        return instance;
    }
    Plugin p;
    FileConfiguration data;
    File dfile;
    public void setup(Plugin p) {
        dfile = new File(p.getDataFolder(), "data.yml");
        data = YamlConfiguration.loadConfiguration(dfile);
    }
    public FileConfiguration getData() {
        return data;
    }
    public void saveData(){
        try {
            data.save(dfile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(dfile);
    }
    public PluginDescriptionFile getDesc() {
        return p.getDescription();
    }
}