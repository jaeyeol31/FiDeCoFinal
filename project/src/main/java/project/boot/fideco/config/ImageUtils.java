package project.boot.fideco.config;

import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;

public class ImageUtils {

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }
}