package ru.netology.graphics.image;

public class ColorScheme implements TextColorSchema {
    @Override
    public char convert(int color) {
        return (color < 33) ? '#': (color < 65) ? '$' : (color < 97) ? '@' :
                (color < 129) ? '%' : (color < 161) ? '*' :
                        (color < 193) ? '+' : (color < 225) ? '-' :
                                (color < 257) ? '\'' : ' ';
    }
}
