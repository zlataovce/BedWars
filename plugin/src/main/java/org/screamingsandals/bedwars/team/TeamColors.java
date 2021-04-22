package org.screamingsandals.bedwars.team;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.Map;

@UtilityClass
public class TeamColors {
    public final TeamColor BLACK = new TeamColor(NamedTextColor.BLACK, NamedTextColor.BLACK, "BLACK");
    public final TeamColor BLUE = new TeamColor(NamedTextColor.DARK_BLUE, NamedTextColor.DARK_BLUE, "BLUE");
    public final TeamColor GREEN = new TeamColor(NamedTextColor.DARK_GREEN, NamedTextColor.DARK_GREEN, "GREEN");
    public final TeamColor RED = new TeamColor(NamedTextColor.RED, NamedTextColor.RED, "RED");
    public final TeamColor MAGENTA = new TeamColor(NamedTextColor.DARK_PURPLE, NamedTextColor.DARK_PURPLE, "MAGENTA");
    public final TeamColor ORANGE = new TeamColor(NamedTextColor.GOLD, NamedTextColor.GOLD, "ORANGE");
    public final TeamColor LIGHT_GRAY = new TeamColor(NamedTextColor.GRAY, NamedTextColor.GRAY, "LIGHT_GRAY");
    public final TeamColor GRAY = new TeamColor(NamedTextColor.DARK_GRAY, NamedTextColor.DARK_GRAY, "GRAY");
    public final TeamColor LIGHT_BLUE = new TeamColor(NamedTextColor.BLUE, NamedTextColor.BLUE, "LIGHT_BLUE");
    public final TeamColor LIME = new TeamColor(NamedTextColor.GREEN, NamedTextColor.GREEN, "LIME");
    public final TeamColor CYAN = new TeamColor(NamedTextColor.AQUA, NamedTextColor.AQUA, "CYAN");
    public final TeamColor PINK = new TeamColor(NamedTextColor.LIGHT_PURPLE, NamedTextColor.LIGHT_PURPLE, "PINK");
    public final TeamColor YELLOW = new TeamColor(NamedTextColor.YELLOW, NamedTextColor.YELLOW, "YELLOW");
    public final TeamColor WHITE = new TeamColor(NamedTextColor.WHITE, NamedTextColor.WHITE, "WHITE");
    public final TeamColor BROWN = new TeamColor(TextColor.color(139, 69, 19), NamedTextColor.DARK_RED, "BROWN");

    public static final Map<String, TeamColor> VALUES = Map.ofEntries(
            Map.entry("BLACK", TeamColors.BLACK),
            Map.entry("BLUE", TeamColors.BLUE),
            Map.entry("GREEN", TeamColors.GREEN),
            Map.entry("RED", TeamColors.RED),
            Map.entry("MAGENTA", TeamColors.MAGENTA),
            Map.entry("ORANGE", TeamColors.ORANGE),
            Map.entry("LIGHT_GRAY", TeamColors.LIGHT_GRAY),
            Map.entry("GRAY", TeamColors.GRAY),
            Map.entry("LIGHT_BLUE", TeamColors.LIGHT_BLUE),
            Map.entry("LIME", TeamColors.LIME),
            Map.entry("CYAN", TeamColors.CYAN),
            Map.entry("PINK", TeamColors.PINK),
            Map.entry("YELLOW", TeamColors.YELLOW),
            Map.entry("WHITE", TeamColors.WHITE),
            Map.entry("BROWN", TeamColors.BROWN)
    );

    public TeamColor valueOf(String name) {
        return VALUES.get(name.toUpperCase());
    }

}
