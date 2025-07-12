package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PhotoConverter implements TextGraphicsConverter {
    int callingWidth = 0;
    int callingHeight = 0;
    int callingRatio = 0;
    int maxWidth = 20000;
    int maxHeight = 20000;
    double maxRatio = 100.00;
    TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int width = img.getWidth();
        int height = img.getHeight();

        if (callingRatio > 0) {
            double ratioParameters = (double) width / height;
            if (ratioParameters > maxRatio) {
                throw new BadImageSizeException(ratioParameters, maxRatio);
            }
        }

        if ((callingWidth > 0 || callingHeight > 0) && (img.getWidth() > maxWidth ||
        img.getHeight() > maxHeight)) {
            double proportions = (double) width / height;

            while (width > maxWidth || height > maxHeight) {
                width /= proportions;
                height /= proportions;
            }
        }

        int newWidth = width;
        int newHeight = height;

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        graphics.dispose();

        WritableRaster bwRaster = bwImg.getRaster();
        schema = new ColorScheme();
        Character[][] chars = new Character[newHeight][newWidth];

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                int color = bwRaster.getPixel(j, i, new int[3])[0];
                char c = schema.convert(color);
                chars[i][j] = c;
            }
        }

        StringBuilder string = new StringBuilder();
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                string.append(chars[i][j]);
                string.append(chars[i][j]);
            }
            string.append('\n');
        }

        return string.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        callingWidth = 1;

        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        callingHeight = 1;

        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        callingRatio = 1;

        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
