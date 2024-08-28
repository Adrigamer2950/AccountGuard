package me.adrigamer2950.accountguard.common.totp;

import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.adrigamer2950.accountguard.common.config.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TOTP {

    private final String secret;
    private final QrData qr;

    public BufferedImage getBufferedImage() {
        try {
            return ImageIO.read(new ByteArrayInputStream(new ZxingPngQrGenerator().generate(qr)));
        } catch (IOException | QrGenerationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateSecret() {
        return new AGSecretGenerator().generate();
    }

    public static QrData generateQR(String secret, Config config, String playerName) {
        return new QrData.Builder()
                .label(config.totp().label().replaceAll("%player%", playerName))
                .secret(secret)
                .issuer(config.totp().server())
                .algorithm(config.totp().algorithm())
                .digits(config.totp().digits())
                .period(config.totp().interval())
                .build();
    }

    /*public static void genImageFromQr(QrData data, Path path) {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(path.toAbsolutePath().toString()))) {
            out.write(new ZxingPngQrGenerator().generate(data));
        } catch (IOException | QrGenerationException e) {
            throw new RuntimeException(e);
        }
    }*/

    public static TOTP genTOTP(Config config, String playerName) {
        String secret = generateSecret();
        QrData qr = generateQR(secret, config, playerName);

        return new TOTP(secret, qr);
    }
}
