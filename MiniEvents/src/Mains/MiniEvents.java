package Mains;

import Handlers.CheckCustomEvents.*;
import Handlers.EquipArmor;
import Handlers.MainHandle;
import Handlers.Handle;
import Misc.S1;
import Misc.StatCommands;
import Util.Methods.MainMethods;
import Misc.SignMethods;
import Util.TimerMain;
import Util.*;
import Util.Methods.*;
import Misc.MainSigns;
import Handlers.EventHandlers.ComplexEvents.HorseEvent;
import Handlers.EventHandlers.SimpleEvents.KO;
import Handlers.EventHandlers.ComplexEvents.KOTH;
import Handlers.EventHandlers.SimpleEvents.LMS;
import Handlers.EventHandlers.ComplexEvents.OITC;
import Handlers.EventHandlers.ComplexEvents.PaintBall;
import Handlers.EventHandlers.SimpleEvents.Parkour;
import Handlers.EventHandlers.SimpleEvents.Spleef;
import Handlers.EventHandlers.ComplexEvents.TDM;
import Handlers.EventHandlers.SimpleEvents.TNTRun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MiniEvents extends JavaPlugin {
    SettingsManager settings = SettingsManager.getInstance();
    LMS lms = new LMS(this);
    Spleef spleef = new Spleef(this);
    EventsMain eventsmain = new EventsMain(this);
    KO ko = new KO(this);
    SetQuit setQuit = new SetQuit(this);
    Handle handle = new Handle(this);
    Commands command = new Commands(this);
    TNTRun tntrun = new TNTRun(this);
    PaintBall paintball = new PaintBall(this);
    KOTH koth = new KOTH(this);
    Parkour parkour = new Parkour(this);
    LangYAML langYAML = new LangYAML(this);
    CountDown method = new CountDown(this);
    OITC oitc = new OITC(this);
    public String eventName = null;
    MainSigns mainSigns = new MainSigns(this);
    SpectateMode spectateMode = new SpectateMode(this);
    JoinEventMethod joinEventMethod = new JoinEventMethod(this);
    SignMethods signMethods = new SignMethods(this);
    MainHandle mainHandle = new MainHandle(this);
    NoDeathScreen noDeathScreen = new NoDeathScreen(this);
    SendRawCommand sendRawCommand = new SendRawCommand(this);
    StatCommands statCommands = new StatCommands(this);
    TimerMain timerMain = new TimerMain(this);
    HorseEvent horseEvent = new HorseEvent(this);
    TDM tdm = new TDM(this);
    EndEvent endEvent = new EndEvent(this);
    RemoveEvent removeEvent = new RemoveEvent(this);
    StartEvent startEvent = new StartEvent(this);
    JoinEvent joinEvent = new JoinEvent(this);
    EquipArmor equipArmor = new EquipArmor(this);
    WinEvent winEvent = new WinEvent(this);
    Do ado = new Do(this);
    MainMethods mainMethods = new MainMethods(this);
    Info info = new Info(this);
    S1 s1 = new S1(this);
    public void onEnable() {
        settings.setup(this);
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        langYAML.getLang().options().copyDefaults(true);
        langYAML.saveDefaultLang();
        Bukkit.getServer().getLogger().info("MiniEvents has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(winEvent, this);
        Bukkit.getServer().getPluginManager().registerEvents(joinEvent, this);
        Bukkit.getServer().getPluginManager().registerEvents(startEvent, this);
        Bukkit.getServer().getPluginManager().registerEvents(removeEvent, this);
        Bukkit.getServer().getPluginManager().registerEvents(endEvent, this);
        Bukkit.getServer().getPluginManager().registerEvents(mainHandle, this);
        Bukkit.getServer().getPluginManager().registerEvents(tntrun, this);
        Bukkit.getServer().getPluginManager().registerEvents(parkour, this);
        Bukkit.getServer().getPluginManager().registerEvents(lms, this);
        Bukkit.getServer().getPluginManager().registerEvents(spleef, this);
        Bukkit.getServer().getPluginManager().registerEvents(method, this);
        Bukkit.getServer().getPluginManager().registerEvents(oitc, this);
        Bukkit.getServer().getPluginManager().registerEvents(koth, this);
        Bukkit.getServer().getPluginManager().registerEvents(ko, this);
        Bukkit.getServer().getPluginManager().registerEvents(paintball, this);
        Bukkit.getServer().getPluginManager().registerEvents(handle, this);
        Bukkit.getServer().getPluginManager().registerEvents(mainSigns, this);
        Bukkit.getServer().getPluginManager().registerEvents(noDeathScreen, this);
        Bukkit.getServer().getPluginManager().registerEvents(spectateMode, this);
        Bukkit.getServer().getPluginManager().registerEvents(timerMain, this);
        Bukkit.getServer().getPluginManager().registerEvents(horseEvent, this);
        Bukkit.getServer().getPluginManager().registerEvents(tdm, this);
        getCommand("leave").setExecutor(spectateMode);
        getCommand("eventstats").setExecutor(statCommands);
        getCommand("sendraw").setExecutor(sendRawCommand);
        getCommand("event").setExecutor(eventsmain);
        getCommand("join").setExecutor(eventsmain);
        getCommand("lms").setExecutor(command);
        getCommand("spleef").setExecutor(command);
        getCommand("tdm").setExecutor(command);
        getCommand("ko").setExecutor(command);
        getCommand("oitc").setExecutor(command);
        getCommand("koth").setExecutor(command);
        getCommand("tntrun").setExecutor(command);
        getCommand("parkour").setExecutor(command);
        getCommand("setquit").setExecutor(setQuit);
        getCommand("paint").setExecutor(command);
        getCommand("horse").setExecutor(command);
    }
    public TNTRun getTntrun(){
        return this.tntrun;
    }
    public SpectateMode getSpectateMode() {
        return this.spectateMode;
    }
    public SignMethods getSignMethods() {
        return this.signMethods;
    }
    public Spleef getSpleef(){
        return this.spleef;
    }
    public JoinEventMethod getJoinEventMethod() {
        return this.joinEventMethod;
    }

    public S1 getS1() {
        return this.s1;
    }

    public TimerMain getTimerMain() {
        return this.timerMain;
    }

    public String getFormalName(){
        return getConfig().getString("event-names." + getEventName());
    }
    public String getFormalName(String s){
        return getConfig().getString("event-names." + s);
    }
    public FileConfiguration customFile(String s) {
        File playerDir = new File(this.getDataFolder() + File.separator + s.toLowerCase() + ".yml");
        return YamlConfiguration.loadConfiguration(playerDir);
    }

    public File customData(String s) {
        return new File(this.getDataFolder() + File.separator + s.toLowerCase() + ".yml");
    }
    public EquipArmor getEquipArmor(){
        return this.equipArmor;
    }
    public void onDisable() {
        this.saveConfig();
        settings.saveData();
    }
    public Do getDo(){
        return this.ado;
    }
    public Info getInfo() { return this.info; }
    public void send(Player player, String string) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string)));
    }

    public void send(Player player, String string1, String string2) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string1).replace("{0}", string2)));
    }

    public void send(Player player, String string1, String string2, String string3) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string1).replace("{0}", string2).replace("{1}", string3)));
    }

    public void send(CommandSender sender, String string) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string)));
    }
    public void send(CommandSender sender, String string1, String string2) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string1).replace("{0}",
                string2)));
    }
    public void send(CommandSender sender, String string1, String string2, String string3) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string1).replace("{0}",
                string2).replace("{1}", string3)));
    }
    public void send(CommandSender sender, String string1, String string2, String string3, String string4) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string1).replace("{0}",
                string2).replace("{1}", string3).replace("{2}", string4)));
    }

    public void send(CommandSender sender, String string1, String string2, String string3, String string4, String string5) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string1).replace("{0}",
                string2).replace("{1}", string3).replace("{2}", string4).replace("{3}", string5)));
    }

    public void send(CommandSender sender, String string1, String string2, String string3, String string4, String string5, String string6) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string1).replace("{0}",
                string2).replace("{1}", string3).replace("{2}", string4).replace("{3}", string5).replace("{4}", string6)));
    }

    public void send(CommandSender sender, String string1, String string2, String string3, String string4, String string5, String string6, String string7) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string1).replace("{0}",
                string2).replace("{1}", string3).replace("{2}", string4).replace("{3}", string5).replace("{4}", string6).replace("{5}", string7)));
    }

    public void send(CommandSender sender, String string1, String string2, String string3, String string4, String string5, String string6, String string7, String string8) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string1).replace("{0}",
                string2).replace("{1}", string3).replace("{2}", string4).replace("{3}", string5).replace("{4}", string6).replace("{5}", string7).replace("{6}", string8)));
    }
    public void broadcast(String string) {
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string)));
    }

    public void broadcast(String string, String string2) {
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string).replace("{0}", string2)));
    }

    public void broadcast(String string, String string2, String string3) {
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', langYAML.getLang().getString(string).replace("{0}", string2).replace("{1}", string3)));
    }
    public HorseEvent getHorseEvent(){
        return this.horseEvent;
    }
    public String getPlayerEvent(Player player){
        if (getInfo().inevent.containsKey(player.getName())){
            return getInfo().inevent.get(player.getName());
        }else{
            return "null";
        }
    }

    public LangYAML getLangYAML() {
        return this.langYAML;
    }

    public String getEventName(){
        if (eventName != null){
        return eventName;
        }else{
            return "none";
        }
    }
    public MainMethods getMethods(){
        return this.mainMethods;
    }
    public OITC getOitc() {
        return this.oitc;
    }

    public CountDown getCountDown() {
        return this.method;
    }

    public KO getKO() {
        return this.ko;
    }

    public KOTH getKOTH() {
        return this.koth;
    }

    public Handle getHandle() {
        return this.handle;
    }

    public FileConfiguration playerFile(String s) {
        File playerDir = new File(this.getDataFolder() + File.separator + "userdata" + File.separator + s.toLowerCase() + ".yml");
        return YamlConfiguration.loadConfiguration(playerDir);
    }

    public File playerData(String s) {
        return new File(this.getDataFolder() + File.separator + "userdata" + File.separator + s.toLowerCase() + ".yml");
    }
}
