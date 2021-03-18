# SpigotGUI
A simple to use graphical interface system for spigot

## Maven
```xml
<repositories>
    <repository>
    <id>bebendorf</id>
    <url>https://repo.bebendorf.eu</url>
  </repository>
</repositories>
```
```xml
<dependency>
  <groupId>eu.bebendorf</groupId>
  <artifactId>SpigotGUI-API</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

## Usage
### Example GUI
```java
SpigotGUI.get().makeGUI("test", "Test", 3)
  .background(SpigotGUI.make(null, Material.STAINED_GLASS_PANE).title("§r"))
  .item(2, 1, SpigotGUI.make("a", Material.DIAMOND).title("A"))
  .item(4, 1, SpigotGUI.make("b", Material.DIAMOND).title("B"))
  .item(6, 1, SpigotGUI.make("c", Material.DIAMOND).title("C"))
  .click(e -> e.getPlayer().sendMessage(e.getItem().getName()))
  .show(player);
```
### Events
There are multiple ways to react to clicks.
You can register listeners on either the gui or individual items by using their `.click(GUIClickListener)` method or use the bukkit event system to listen for the `GUIClickEvent`.

## Attributes
Both the `GUI` and `GUIItem` have an attribute system to attach attributes to those items. You can get and set attributes using the `attr` methods.
