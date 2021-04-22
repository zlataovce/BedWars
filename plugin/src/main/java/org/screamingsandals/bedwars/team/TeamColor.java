package org.screamingsandals.bedwars.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.screamingsandals.bedwars.game.SynchronizableGameElement;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamColor implements SynchronizableGameElement, org.screamingsandals.bedwars.api.team.TeamColor {
    private RGBLike leatherColor = NamedTextColor.WHITE;
    private RGBLike chatColor = NamedTextColor.WHITE;
    private String materialColor = "WHITE";

    @Override
    public void deserialize(ConfigurationNode node) {
        var leatherColor = node.node("leatherColor");
        if (!leatherColor.empty()) {
            var c = TextColor.fromCSSHexString(leatherColor.getString(""));
            if (c != null) {
                this.leatherColor = c;
            } else {
                var c2 = NamedTextColor.NAMES.value(leatherColor.getString("").trim().toLowerCase());
                if (c2 != null) {
                    this.leatherColor = c2;
                }
            }
        }

        var chatColor = node.node("chatColor");
        if (!chatColor.empty()) {
            var c = TextColor.fromCSSHexString(chatColor.getString(""));
            if (c != null) {
                this.chatColor = c;
            } else {
                var c2 = NamedTextColor.NAMES.value(chatColor.getString("").trim().toLowerCase());
                if (c2 != null) {
                    this.chatColor = c2;
                }
            }
        }

        this.materialColor = node.node("materialColor").getString("WHITE");
    }

    @Override
    public void serialize(ConfigurationNode node) throws ConfigurateException {
        node.node("leatherColor").set("#" + Integer.toHexString(this.leatherColor.red()) + Integer.toHexString(this.leatherColor.green()) + Integer.toHexString(this.leatherColor.blue()));
        node.node("chatColor").set("#" + Integer.toHexString(this.chatColor.red()) + Integer.toHexString(this.chatColor.green()) + Integer.toHexString(this.chatColor.blue()));
        node.node("materialColor").set(this.materialColor);
    }

    @Override
    public void synchronize(String globalKey) {
        var color = TeamColors.valueOf(globalKey);
        if (color != null) {
            this.setLeatherColor(color.getLeatherColor());
            this.setChatColor(color.getChatColor());
            this.setMaterialColor(color.getMaterialColor());
        }
    }
}
