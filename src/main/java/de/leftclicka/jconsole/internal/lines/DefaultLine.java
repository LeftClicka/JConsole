package de.leftclicka.jconsole.internal.lines;

public class DefaultLine extends ConsoleLine {

    private final String content;

    public DefaultLine(String content) {
        this.content = content;
    }

    @Override
    public String getDefaultText() {
        return content;
    }
}
