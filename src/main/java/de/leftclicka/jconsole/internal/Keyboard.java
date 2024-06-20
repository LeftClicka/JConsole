package de.leftclicka.jconsole.internal;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Keyboard {
    private Keyboard() {}
    private static final Map<Integer, Boolean> pressedKeys = new HashMap<>();
    private static final ArrayList<KeyListener> listeners = new ArrayList<>();

    static {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            synchronized (Keyboard.class) {
                if (event.getID() == KeyEvent.KEY_PRESSED) {
                    listeners.forEach(listener -> listener.onKey(event.getKeyCode(), event.getKeyChar()));
                    pressedKeys.put(event.getKeyCode(), true);
                } else if (event.getID() == KeyEvent.KEY_RELEASED) {
                    pressedKeys.put(event.getKeyCode(), false);
                }
                return true;
            }
        });
    }
    public static boolean isKeyPressed(int keyCode) {
        return pressedKeys.getOrDefault(keyCode, false);
    }

    public static void addListener(KeyListener listener) {
        listeners.add(listener);
    }
}
