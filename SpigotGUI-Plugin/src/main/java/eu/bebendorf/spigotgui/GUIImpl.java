package eu.bebendorf.spigotgui;

import eu.bebendorf.spigotgui.api.GUI;
import eu.bebendorf.spigotgui.api.GUIClickListener;
import eu.bebendorf.spigotgui.api.GUIItem;
import eu.bebendorf.spigotgui.api.event.GUIClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GUIImpl implements GUI {

    final SpigotGUIPlugin plugin;
    final String name;
    String title;
    final int height;
    GUIItem background;
    final GUIItem[] items;
    final List<GUIClickListener> listeners = new ArrayList<>();
    final Map<String, Object> attributes = new HashMap<>();

    public GUIImpl(SpigotGUIPlugin plugin, String name, String title, int height) {
        this.plugin = plugin;
        this.name = name;
        this.title = title;
        this.height = height;
        this.items = new GUIItem[height()*width()];
    }

    public String name() {
        return name;
    }

    public <T> T attr(String key) {
        return (T) attributes.get(key);
    }

    public GUI attr(String key, Object value) {
        attributes.put(key, value);
        return this;
    }

    public String title() {
        return title;
    }

    public int width() {
        return 9;
    }

    public int height() {
        return height;
    }

    public GUI show(Player player) {
        Inventory inventory = Bukkit.createInventory(null, items.length, title);
        for(int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, toStack(items[i]));
        player.openInventory(inventory);
        plugin.getPlayerGUIs().put(player, this);
        return this;
    }

    public List<Player> getViewers() {
        return plugin.getPlayerGUIs().entrySet().stream().filter(e -> e.getValue().equals(this)).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private Map<Player, Inventory> getViewerInventories() {
        Map<Player, Inventory> inventories = new HashMap<>();
        getViewers().forEach(p -> inventories.put(p, p.getOpenInventory().getTopInventory()));
        return inventories;
    }

    public GUIItem item(int i) {
        if(i < 0 || i >= items.length)
            return null;
        return items[i];
    }

    public GUI background(GUIItem background) {
        this.background = background;
        return this;
    }

    private ItemStack toStack(GUIItem item) {
        ItemStack stack = item == null ? null : ((GUIItemImpl) item).toItem();
        if(stack == null && background != null)
            stack = ((GUIItemImpl) background).toItem();
        return stack;
    }

    public GUI item(int i, GUIItem item) {
        if(i < 0 || i >= items.length)
            return this;
        items[i] = item;
        getViewerInventories().forEach((p, inv) -> inv.setItem(i, toStack(item)));
        return this;
    }

    public GUI title(String title) {
        this.title = title;
        return this;
    }

    public GUI click(GUIClickListener listener) {
        listeners.add(listener);
        return this;
    }

    public void call(GUIClickEvent event) {
        listeners.forEach(l -> l.onClick(event));
    }

}
