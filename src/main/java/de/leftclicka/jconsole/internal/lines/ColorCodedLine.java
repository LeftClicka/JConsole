package de.leftclicka.jconsole.internal.lines;

import java.awt.*;
import java.util.ArrayList;

public class ColorCodedLine extends DefaultLine {

    private final Color[] colors;

    public ColorCodedLine(String content) {
        super(content);
        colors = new Color[content.length()];
    }

    public ColorCodedLine addColorCode(int from, int to, Color color) {
        for (int i = from; i <= to; i++)
            colors[i] = color;
        return this;
    }

    public Color colorForIndex(int i) {
        if (i < 0 || i >= colors.length)
            return Color.WHITE;
        Color color = colors[i];
        return color == null ? Color.WHITE : color;
    }

    public static class Renderer implements ConsoleLineRenderer {

        @Override
        public void render(ConsoleLine line, int yLevel, boolean isHovered, Color defaultColor, Graphics2D g) {
            assert line instanceof ColorCodedLine;
            ColorCodedLine colorCodedLine = (ColorCodedLine) line;
            char[] chars = line.getDefaultText().toCharArray();
            int x = 5;
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                g.setColor(colorCodedLine.colorForIndex(j));
                g.drawString(String.valueOf(c), x, yLevel);
                x += g.getFontMetrics().charWidth(c);
            }
        }
    }
}
