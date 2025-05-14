package com.istanbul.eminonurehber.utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

public class Utils {

    public static byte[] compressImage(byte[] originalImageBytes, String format, float initialScale, long maxSizeBytes) throws IOException {
        if (originalImageBytes == null || originalImageBytes.length == 0) {
            throw new IllegalArgumentException("Image bytes cannot be null or empty");
        }
        if (initialScale <= 0 || initialScale > 1) {
            throw new IllegalArgumentException("Scale must be between 0 and 1");
        }
        if (!Arrays.asList("jpg", "jpeg", "png").contains(format.toLowerCase())) {
            throw new IllegalArgumentException("Unsupported image format: " + format);
        }

        try (InputStream in = new ByteArrayInputStream(originalImageBytes)) {
            BufferedImage originalImage = ImageIO.read(in);
            if (originalImage == null) {
                throw new IOException("Could not read image from bytes");
            }

            float currentScale = initialScale;
            byte[] result;
            int attempts = 0;
            final int MAX_ATTEMPTS = 5;

            do {
                // Boyutları yeniden hesapla
                int newWidth = (int) (originalImage.getWidth() * currentScale);
                int newHeight = (int) (originalImage.getHeight() * currentScale);

                // Alpha kanalını koruyarak yeni resim oluştur
                BufferedImage resizedImage = new BufferedImage(
                        newWidth,
                        newHeight,
                        originalImage.getTransparency() == Transparency.OPAQUE ?
                                BufferedImage.TYPE_INT_RGB :
                                BufferedImage.TYPE_INT_ARGB
                );

                // Daha kaliteli ölçeklendirme için BICUBIC interpolasyon
                Graphics2D g = resizedImage.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setRenderingHint(RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                g.dispose();

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                // JPEG için kalite ayarı (boyuta göre dinamik kalite)
                float quality = calculateQuality(currentScale, initialScale);

                if ("jpg".equalsIgnoreCase(format) || "jpeg".equalsIgnoreCase(format)) {
                    writeJpegWithQuality(resizedImage, out, quality);
                } else {
                    // PNG için
                    ImageIO.write(resizedImage, "png", out);
                }

                result = out.toByteArray();
                currentScale *= 0.8f; // Her denemede %20 daha küçült
                attempts++;

            } while (result.length > maxSizeBytes && attempts < MAX_ATTEMPTS);

            if (result.length > maxSizeBytes) {
                throw new IOException("Image could not be compressed below " + maxSizeBytes + " bytes");
            }

            return result;
        }
    }

    private static float calculateQuality(float currentScale, float initialScale) {
        // Ölçek küçüldükçe kaliteyi düşür (0.7 - 0.9 arası)
        float minQuality = 0.7f;
        float maxQuality = 0.9f;

        return maxQuality - (maxQuality - minQuality) *
                ((initialScale - currentScale) / initialScale);
    }

    private static void writeJpegWithQuality(BufferedImage image, OutputStream out, float quality)
            throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            ImageIO.write(image, "jpeg", out);
            return;
        }

        ImageWriter writer = writers.next();
        try {
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            try (ImageOutputStream ios = ImageIO.createImageOutputStream(out)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(image, null, null), param);
            }
        } finally {
            writer.dispose();
        }
    }

}
