package eu.bebendorf.spigotgui;

import eu.bebendorf.spigotgui.api.GUI;
import eu.bebendorf.spigotgui.api.GUIItem;
import eu.bebendorf.spigotgui.api.SpigotGUI;
import eu.bebendorf.spigotgui.listener.InventoryListener;
import eu.bebendorf.spigotgui.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SpigotGUIPlugin extends JavaPlugin implements SpigotGUI {

    @Getter
    final Map<Player, GUI> playerGUIs = new HashMap<>();

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        Bukkit.getServicesManager().register(SpigotGUI.class, this, this, ServicePriority.Highest);
    }

    public void onDisable() {
        playerGUIs.keySet().forEach(HumanEntity::closeInventory);
    }

    public GUIItem makeItem(String name, Material material) {
        return new GUIItemImpl(this, name, material);
    }

    public GUI makeGUI(String name, String title, int height) {
        return new GUIImpl(this, name, title, height);
    }

}
