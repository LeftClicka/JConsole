package de.leftclicka.jconsole.internal.lines;

import javax.swing.*;

public class ManuallyAnimatedLine extends ConsoleLine {

    private final String[] strings;

    private int framePtr = 0;

    public ManuallyAnimatedLine(String... strings) {
        this.strings = strings;

    }

    @Override
    public String getDefaultText() {
        return strings[framePtr];
    }

    public void nextFrame() {
        int next = framePtr+1;
        if (next >= strings.length)
            next = 0;
        framePtr = next;
    }
}
