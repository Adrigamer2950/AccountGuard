package me.adrigamer2950.accountguard.bukkit.map;

import lombok.Getter;
import me.adrigamer2950.accountguard.bukkit.AGBukkit;
import me.adrigamer2950.accountguard.common.totp.TOTP;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class QrMapRenderer extends MapRenderer {

    @Getter
    private final TOTP totp;
    private final BufferedImage img;

    public QrMapRenderer(AGBukkit plugin, String playerName) {
        this.totp = TOTP.genTOTP(plugin.config, playerName);
        this.img = this.totp.getBufferedImage();
    }

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        canvas.drawImage(0, 0, MapPalette.resizeImage(img));
    }
}
