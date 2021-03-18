package eu.bebendorf.spigotgui.listener;

import eu.bebendorf.spigotgui.SpigotGUIPlugin;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerListener implements Listener {

    final SpigotGUIPlugin plugin;

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        plugin.getPlayerGUIs().remove(e.getPlayer());
    }

}
