package de.leftclicka.jconsole.internal.lines;

import javax.swing.*;
import java.io.Closeable;
import java.io.IOException;

public class AnimatedLine extends ConsoleLine implements Closeable {

    private final String[] strings;

    private final Timer updater;
    private int framePtr;
    private String stopReplacement = null;

    public AnimatedLine(int fps, String... strings) {
        this.strings = strings;
        updater = new Timer(1000/fps, e -> {
            //use temp local var to avoid race conditions where framePtr might be out of bounds
            int next = framePtr + 1;
            if (next >= strings.length)
                next = 0;
            framePtr = next;
        });
        updater.start();
    }

    @Override
    public String getDefaultText() {
        if (stopReplacement != null)
            return stopReplacement;
        return strings[framePtr];
    }

    /**
     * Stops (freezes) the animation permanently
     */
    @Override
    public void close() {
        updater.stop();
    }

    /**
     * After this method is invoked the animation will stop and instead the parameter "replacement" will be shown
     */
    public void stop(String replacement) {
        close();
        stopReplacement = replacement;
    }
}
