package eu.bebendorf.spigotgui.listener;

import eu.bebendorf.spigotgui.GUIImpl;
import eu.bebendorf.spigotgui.GUIItemImpl;
import eu.bebendorf.spigotgui.SpigotGUIPlugin;
import eu.bebendorf.spigotgui.api.MouseButton;
import eu.bebendorf.spigotgui.api.GUI;
import eu.bebendorf.spigotgui.api.GUIItem;
import eu.bebendorf.spigotgui.api.event.GUIClickEvent;
import eu.bebendorf.spigotgui.api.event.GUICloseEvent;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

@AllArgsConstructor
public class InventoryListener implements Listener {

    final SpigotGUIPlugin plugin;

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) e.getWhoClicked();
        GUI gui = plugin.getPlayerGUIs().get(player);
        if(gui == null)
            return;
        e.setResult(Event.Result.DENY);
        e.setCancelled(true);
        if(shouldCancel(e.getAction()))
            return;
        if(e.getClickedInventory() == null || e.getClickedInventory().getType() != InventoryType.CHEST)
            return;
        GUIItem item = gui.item(e.getSlot());
        MouseButton button = e.getClick() == ClickType.MIDDLE ? MouseButton.MIDDLE : (e.isRightClick() ? MouseButton.RIGHT : MouseButton.LEFT);
        GUIClickEvent event = new GUIClickEvent(player, gui, item, button, e.isShiftClick());
        plugin.getServer().getPluginManager().callEvent(event);
        ((GUIImpl) gui).call(event);
        if(item == null)
            return;
        ((GUIItemImpl) item).call(event);
    }

    private static boolean shouldCancel(InventoryAction action) {
        if(action == null)
            return true;
        switch (action) {
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
                return true;
            default:
                return false;
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(!(e.getPlayer() instanceof Player))
            return;
        Player player = (Player) e.getPlayer();
        GUI gui = plugin.getPlayerGUIs().get(player);
        if(gui == null)
            return;
        plugin.getServer().getPluginManager().callEvent(new GUICloseEvent(player, gui));
        plugin.getPlayerGUIs().remove(player);
    }

}
