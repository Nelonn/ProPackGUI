package me.nelonn.propack.bukkitgui;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TextUtils {
    private TextUtils() {
    }

    public static final char COLOR_CHAR = '&';
    private static final String SPLIT_PATTERN = "((?<=&)|(?=&))";

    public static void send(@NotNull CommandSender sender, @NotNull String message) {
        sender.sendMessage(color(message));
    }

    public static String color(String string) {
        if (string == null) return null;

        String[] strings = string.split(String.valueOf(COLOR_CHAR));
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            if (s.isEmpty()) continue;
            if (i == 0) {
                result.append(s);
                continue;
            }
            if (s.charAt(0) == '#') {
                if (s.length() < 7) {
                    result.append(COLOR_CHAR).append(s);
                }
                ChatColor color = ChatColor.of(s.substring(0, 7));
                result.append(color).append(s.substring(7));
            } else {
                ChatColor color = ChatColor.getByChar(s.charAt(0));
                if (color == null) {
                    result.append(COLOR_CHAR).append(s);
                } else {
                    result.append(color).append(s.substring(1));
                }
            }
        }

        return result.toString();
    }

    @Deprecated
    public static String colorOld(String string) {
        if (string == null) return null;

        String[] strings = string.split(SPLIT_PATTERN);
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equalsIgnoreCase("&")) {
                i++;
                if (strings[i].charAt(0) == '#') {
                    String text = strings[i].substring(7);
                    ChatColor color = ChatColor.of(strings[i].substring(0, 7));
                    builder.append(color).append(text);
                } else {
                    String text = strings[i].substring(1);
                    ChatColor color = ChatColor.getByChar(strings[i].charAt(0));
                    if (color != null) builder.append(color);
                    builder.append(text);
                }
            } else {
                builder.append(strings[i]);
            }
        }

        return builder.toString();
    }

    public static BaseComponent[] colorComponents(@NotNull String string) {
        String[] strings = string.split(SPLIT_PATTERN);
        ComponentBuilder builder = new ComponentBuilder();

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equalsIgnoreCase("&")) {
                i++;
                if (strings[i].charAt(0) == '#') {
                    String text = strings[i].substring(7);
                    ChatColor color = ChatColor.of(strings[i].substring(0, 7));

                    ComponentBuilder subComponent = new ComponentBuilder(text);
                    subComponent.color(color);

                    builder.append(subComponent.create());
                } else {
                    String text = strings[i].substring(1);
                    ChatColor color = ChatColor.getByChar(strings[i].charAt(0));

                    ComponentBuilder subComponent = new ComponentBuilder(text);
                    if (color != null) subComponent.color(color);

                    builder.append(subComponent.create());
                }
            } else {
                builder.append(strings[i]);
            }
        }

        return builder.create();
    }

    public static TextComponent colorComponent(String string) {
        return new TextComponent(colorComponents(string));
    }
}