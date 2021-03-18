package eu.bebendorf.spigotgui.api;

import org.bukkit.entity.Player;

import java.util.List;

public interface GUI {

    static GUI make(String name, String title, int height) {
        return SpigotGUI.get().makeGUI(name, title, height);
    }

    String name();
    <T> T attr(String key);
    GUI attr(String key, Object value);
    GUI background(GUIItem background);
    String title();
    int width();
    int height();
    GUI show(Player player);
    List<Player> getViewers();
    default GUIItem item(int x, int y) {
        return item(x+(y*width()));
    }
    GUIItem item(int i);
    default GUI item(int x, int y, GUIItem item) {
        return item(x+(y*width()), item);
    }
    GUI item(int i, GUIItem item);
    GUI title(String title);
    GUI click(GUIClickListener listener);

}
