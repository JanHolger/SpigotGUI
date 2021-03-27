package eu.bebendorf.spigotgui;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import eu.bebendorf.spigotgui.api.GUIClickListener;
import eu.bebendorf.spigotgui.api.GUIItem;
import eu.bebendorf.spigotgui.api.MetaModifier;
import eu.bebendorf.spigotgui.api.event.GUIClickEvent;
import eu.bebendorf.spigotgui.util.Reflection;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class GUIItemImpl implements GUIItem {

    final SpigotGUIPlugin plugin;
    final String name;
    final Material material;
    int amount = 1;
    short data = 0;
    String title;
    String[] lore;
    final List<ItemFlag> flags = new ArrayList<>();
    final Map<Enchantment, Integer> enchants = new HashMap<>();
    Color color;
    String texture;
    String owner;
    final List<MetaModifier> metaModifiers = new ArrayList<>();
    final List<GUIClickListener> listeners = new ArrayList<>();
    final Map<String, Object> attributes = new HashMap<>();

    public GUIItemImpl(SpigotGUIPlugin plugin, String name, Material material) {
        this.plugin = plugin;
        this.name = name;
        this.material = material;
    }

    public String name() {
        return name;
    }

    public <T> T attr(String key) {
        return (T) attributes.get(key);
    }

    public GUIItem attr(String key, Object value) {
        attributes.put(key, value);
        return this;
    }

    public GUIItem title(String title) {
        this.title = title;
        return this;
    }

    public String title() {
        return title;
    }

    public GUIItem amount(int amount) {
        this.amount = amount;
        return this;
    }

    public int amount() {
        return amount;
    }

    public GUIItem data(int data) {
        this.data = (short) data;
        return this;
    }

    public short data() {
        return data;
    }

    public GUIItem lore(String... lore) {
        this.lore = lore;
        return this;
    }

    public String[] lore() {
        return lore;
    }

    public GUIItem enchant() {
        return enchant(Enchantment.DURABILITY, 1).flags(ItemFlag.HIDE_ENCHANTS);
    }

    public GUIItem flags(ItemFlag... flags) {
        for(ItemFlag flag : flags) {
            if(!this.flags.contains(flag))
                this.flags.add(flag);
        }
        return this;
    }

    public List<ItemFlag> flags() {
        return flags;
    }

    public GUIItem enchant(Enchantment enchantment, int level) {
        enchants.put(enchantment, level);
        return this;
    }

    public Map<Enchantment, Integer> enchants() {
        return enchants;
    }

    public GUIItem color(Color color) {
        this.color = color;
        return this;
    }

    public Color color() {
        return color;
    }

    public GUIItem texture(String url) {
        this.texture = url;
        return this;
    }

    public GUIItem owner(String owner) {
        this.owner = owner;
        return this;
    }

    public GUIItem meta(MetaModifier... modifiers) {
        Collections.addAll(metaModifiers, modifiers);
        return this;
    }

    public GUIItem click(GUIClickListener listener) {
        listeners.add(listener);
        return this;
    }

    public void call(GUIClickEvent event) {
        listeners.forEach(l -> l.onClick(event));
    }

    public ItemStack toItem() {
        ItemStack stack = new ItemStack(material, amount, data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(title);
        if(lore != null)
            meta.setLore(Arrays.asList(lore));
        enchants.forEach((e,l) -> meta.addEnchant(e, l, true));
        flags.forEach(meta::addItemFlags);
        if(color != null && meta instanceof LeatherArmorMeta)
            ((LeatherArmorMeta) meta).setColor(color);
        if(meta instanceof SkullMeta) {
            if(owner != null)
                ((SkullMeta) meta).setOwner(owner);
            if(texture != null) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), "Skull");
                JsonObject object = new JsonObject();
                object.addProperty("timestamp", System.currentTimeMillis());
                object.addProperty("profileId", profile.getId().toString());
                object.addProperty("profileName", profile.getName());
                JsonObject textures = new JsonObject();
                JsonObject skin = new JsonObject();
                skin.addProperty("url", texture);
                textures.add("SKIN", skin);
                object.add("textures", textures);
                profile.getProperties().put("textures", new Property("textures", new String(Base64.getEncoder().encode(new GsonBuilder().disableHtmlEscaping().create().toJson(object).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)));
                Reflection.getCBClass("inventory.CraftMetaSkull").getField("profile").setValue(meta, profile);
            }
        }
        metaModifiers.forEach(m -> m.modify(meta));
        stack.setItemMeta(meta);
        return stack;
    }

}
