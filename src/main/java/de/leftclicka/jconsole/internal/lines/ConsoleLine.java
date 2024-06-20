package de.leftclicka.jconsole.internal.lines;

import java.awt.*;

public abstract class ConsoleLine {

    private Color color = Color.WHITE;
    private boolean isDefaultColor = true;

    public abstract String getDefaultText();

    public String getHoveredText() {
        return getDefaultText();
    }

    public Color getColor() {
        return color;
    }

    public ConsoleLine setColor(Color color) {
        isDefaultColor = false;
        this.color = color;
        return this;
    }

    public boolean isDefaultColor() {
        return isDefaultColor;
    }

    public static class DefaultRenderer implements ConsoleLineRenderer {

        public static final DefaultRenderer INSTANCE = new DefaultRenderer();

        @Override
        public void render(ConsoleLine line, int yLevel, boolean isHovered, Color defaultColor, Graphics2D g) {
            if (line.isDefaultColor())
                g.setColor(defaultColor);
            else g.setColor(line.getColor());
            String text =  isHovered ? line.getHoveredText() : line.getDefaultText();
            g.drawString(text, 5, yLevel);
        }
    }

}
