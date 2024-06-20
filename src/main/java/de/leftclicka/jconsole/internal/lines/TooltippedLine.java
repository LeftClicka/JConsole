package de.leftclicka.jconsole.internal.lines;

public class TooltippedLine extends ConsoleLine {

    private final String text, tooltip;

    public TooltippedLine(String text, String tooltip) {
        this.text = text;
        this.tooltip = tooltip;
    }

    @Override
    public String getDefaultText() {
        return text;
    }

    @Override
    public String getHoveredText() {
        return tooltip;
    }
}
