package eu.bebendorf.spigotgui.api;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public interface SpigotGUI {

    GUIItem makeItem(String name, Material material);
    GUI makeGUI(String name, String title, int height);

    static GUIItem make(String name, Material material) {
        return get().makeItem(name, material);
    }

    static SpigotGUI get() {
        return Bukkit.getServicesManager().load(SpigotGUI.class);
    }

}
