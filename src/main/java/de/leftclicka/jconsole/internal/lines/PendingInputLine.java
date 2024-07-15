package de.leftclicka.jconsole.internal.lines;

import java.io.Closeable;
import javax.swing.Timer;
import java.io.IOException;

public class PendingInputLine extends ConsoleLine implements Closeable {

    private final StringBuilder stringBuilder = new StringBuilder();
    private boolean state = false;
    private final Timer updater;

    public PendingInputLine() {
        updater = new Timer(500, e -> state = !state);
        updater.start();
    }

    @Override
    public String getDefaultText() {
        String str = "> " + stringBuilder;
        if (state)
            str += "|";
        return str;
    }

    public void addChar(char c) {
        stringBuilder.append(c);
    }

    public void addString(String str) {
        stringBuilder.append(str);
    }

    public void delete() {
        if (stringBuilder.length() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
    }
    public String confirm() {
        String str = stringBuilder.toString();
        if (!str.isEmpty())
            stringBuilder.delete(0, stringBuilder.length());
        return str;
    }

    @Override
    public void close() throws IOException {
        updater.stop();
    }
}
