package eu.bebendorf.spigotgui.api.event;

import eu.bebendorf.spigotgui.api.GUI;
import eu.bebendorf.spigotgui.api.GUIItem;
import eu.bebendorf.spigotgui.api.MouseButton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
@Getter
public class GUIClickEvent extends Event {

    final Player player;
    final GUI gui;
    final GUIItem item;
    final MouseButton button;
    final boolean shift;

    public static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
