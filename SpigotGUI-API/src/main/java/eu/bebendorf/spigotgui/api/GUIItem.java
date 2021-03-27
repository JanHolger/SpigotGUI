package eu.bebendorf.spigotgui.api;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Map;

public interface GUIItem {

    static GUIItem make(String name, Material material) {
        return SpigotGUI.get().makeItem(name, material);
    }

    String name();
    <T> T attr(String key);
    GUIItem attr(String key, Object value);
    GUIItem data(int data);
    short data();
    GUIItem title(String title);
    String title();
    GUIItem amount(int amount);
    int amount();
    GUIItem lore(String... lore);
    String[] lore();
    GUIItem enchant();
    GUIItem enchant(Enchantment enchantment, int level);
    Map<Enchantment, Integer> enchants();
    GUIItem flags(ItemFlag... flags);
    List<ItemFlag> flags();
    GUIItem color(Color color);
    Color color();
    GUIItem texture(String url);
    GUIItem owner(String owner);
    GUIItem meta(MetaModifier... modifiers);
    GUIItem click(GUIClickListener listener);

}
