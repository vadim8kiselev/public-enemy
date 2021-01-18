package com.kiselev.enemy.utils.image;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Color {

    private static final List<Color> SHADES = Lists.newArrayList(
            Color.of("White", RGB.of(255, 255, 255)),
            Color.of("Silver", RGB.of(192, 192, 192)),
            Color.of("Gray", RGB.of(128, 128, 128)),
            Color.of("Black", RGB.of(0, 0, 0)),
            Color.of("Red", RGB.of(255, 0, 0)),
            Color.of("Maroon", RGB.of(128, 0, 0)),
            Color.of("Yellow", RGB.of(255, 255, 0)),
            Color.of("Olive", RGB.of(128, 128, 0)),
            Color.of("Lime", RGB.of(0, 255, 0)),
            Color.of("Green", RGB.of(0, 128, 0)),
            Color.of("Aqua", RGB.of(0, 255, 255)),
            Color.of("Teal", RGB.of(0, 128, 128)),
            Color.of("Blue", RGB.of(0, 0, 255)),
            Color.of("Navy", RGB.of(0, 0, 128)),
            Color.of("Fuchsia", RGB.of(255, 0, 255)),
            Color.of("Purple", RGB.of(128, 0, 128))
    );

    @EqualsAndHashCode.Include
    private String name;

    private RGB rgb;

    public static Color of(int code) {
        RGB rgb = RGB.of(code);
        return of(
                shade(rgb),
                rgb
        );
    }

    public static Color of(String name, RGB rgb) {
        Color color = new Color();
        color.name = name;
        color.rgb = rgb;
        return color;
    }

    private static String shade(RGB color) {
        return SHADES.stream()
                .min(Comparator.comparingDouble(
                        shade -> RGB.distance(shade.getRgb(), color)
                ))
                .map(Color::getName)
                .orElse(null);
    }
}
