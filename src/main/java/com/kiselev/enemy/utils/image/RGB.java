package com.kiselev.enemy.utils.image;

import lombok.Data;

@Data
public class RGB {

    private int red;

    private int green;

    private int blue;

    public static RGB of(int code) {
        return of(
                (code & 0x00ff0000) >> 16,
                (code & 0x0000ff00) >> 8,
                (code & 0x000000ff)
        );
    }

    public static RGB of(int red, int green, int blue) {
        RGB rgb = new RGB();
        rgb.setRed(red);
        rgb.setGreen(green);
        rgb.setBlue(blue);
        return rgb;
    }

    public static double distance(RGB a, RGB b) {
        return Math.sqrt(
                (a.getRed() - b.getRed()) * (a.getRed() - b.getRed())
                        + (a.getGreen() - b.getGreen()) * (a.getGreen() - b.getGreen())
                        + (a.getBlue() - b.getBlue()) * (a.getBlue() - b.getBlue())
        );
    }
}
