package de.leftclicka.jconsole;

import de.leftclicka.jconsole.internal.DragManager;
import de.leftclicka.jconsole.internal.Keyboard;
import de.leftclicka.jconsole.internal.lines.*;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

import static java.awt.event.KeyEvent.CHAR_UNDEFINED;

public class JConsole extends JFrame {

    private final LinkedList<ConsoleLine> lines = new LinkedList<>();
    private final Map<Class<? extends ConsoleLine>, ConsoleLineRenderer> specialLineHandlers = new HashMap<>();
    private final PendingInputLine pendingInput = new PendingInputLine();
    private final ArrayList<Consumer<String>> permanentListeners = new ArrayList<>();
    private final ArrayList<Consumer<String>> singleListeners = new ArrayList<>();
    private final Timer repaintTimer;
    private Color defaultColor = Color.WHITE;
    private FontMetrics cachedFontMetrics;
    private int scrolledLines = 0;
    private int cachedFontHeight = -1;
    private int currentlyHoveredIndex = -1;
    private boolean printInput = true;

    /**
     * Creates a new console object with width and height passed as parameters
     * After creation, the console will not be visible! Use the setVisible(boolean flag) method to show/hide the console
     */
    public JConsole(int width, int height) {
        //setup special line renderers
        specialLineHandlers.put(ColorCodedLine.class, new ColorCodedLine.Renderer());

        //setup draw routine
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics;
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                int y = g.getFontMetrics().getHeight();
                for (int i = scrolledLines; i < lines.size(); i++) {
                    ConsoleLine line = lines.get(i);
                    ConsoleLineRenderer renderer = specialLineHandlers.getOrDefault(line.getClass(), ConsoleLine.DefaultRenderer.INSTANCE);
                    renderer.render(line, y, i == currentlyHoveredIndex, defaultColor, g);
                    y += g.getFontMetrics().getHeight();
                }
                cachedFontHeight = g.getFontMetrics().getHeight();
                cachedFontMetrics = g.getFontMetrics();
            }
        };

        //setup some other stuff
        panel.setSize(width, height);
        add(panel);
        setLocationRelativeTo(null);
        setSize(width, height);
        setResizable(false);
        setUndecorated(true);

        //add mouse and keyboard listeners
        DragManager dragManager = new DragManager(this);
        addMouseMotionListener(dragManager);
        addMouseListener(dragManager);
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {
                Point location = e.getLocationOnScreen();
                Point topLeft = getLocationOnScreen();
                int width = getWidth(), height = getHeight();
                boolean inside =
                        location.x >= topLeft.x && location.x <= topLeft.x + width
                                && location.y >= topLeft.y && location.y <= topLeft.y + height;
                if (!inside) {
                    currentlyHoveredIndex = -1;
                    return;
                }
                int y = location.y-topLeft.y;
                int index = y/cachedFontHeight;
                if (index >= lines.size() || cachedFontMetrics == null) {
                    currentlyHoveredIndex = -1;
                    return;
                }
                ConsoleLine line = lines.get(index);
                int textWidth = 5 + cachedFontMetrics.stringWidth(line.getDefaultText());
                if (location.x <= topLeft.x + textWidth)
                    currentlyHoveredIndex = index;
                else currentlyHoveredIndex = -1;
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentlyHoveredIndex < 0 || currentlyHoveredIndex >=lines.size())
                    return;
                ConsoleLine hoveredLine = lines.get(currentlyHoveredIndex);
                if (hoveredLine instanceof ClickableLine clickableLine) {
                    clickableLine.getTask().run();
                    clickableLine.setUsed(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        addMouseWheelListener(event -> {
            int amount = event.getUnitsToScroll();
            int newScrollAmount = scrolledLines;
            if (amount < 0)
                newScrollAmount--;
            else if (amount > 0)
                newScrollAmount++;
            if (newScrollAmount < 0)
                newScrollAmount = 0;
            scrolledLines = newScrollAmount;
        });
        Keyboard.addListener((int id, char character) -> {
            if (id == KeyEvent.VK_ENTER) {
                String input = pendingInput.confirm();
                write(new DefaultLine(input));
                permanentListeners.forEach(c -> c.accept(input));
                singleListeners.forEach(c -> c.accept(input));
                singleListeners.clear();
            } else if (id == 8) {
                pendingInput.delete();
            } else {
                if (character == CHAR_UNDEFINED)
                    return;
                pendingInput.addChar(character);
            }
        });

        lines.add(pendingInput);
        repaintTimer = new Timer(1000/25, e -> repaint());
        repaintTimer.start();
        repaint();
    }

    /**
     * Adds any ConsoleLine implementation to the console
     * Use this if you want more than just plain text, for example clickable lines or color coded ones
     */
    public void write(ConsoleLine line) {
        lines.add(lines.size()-1, line);
    }

    /**
     * Writes a plain string into the console as a single line
     */
    public void write(String text) {
        write(new DefaultLine(text));
    }

    /**
     * Set the default color for text in the console (this will be white by default)
     * This color will be used as long as the lines are not color coded themselves or something similar
     */
    public void setDefaultTextColor(Color color) {
        this.defaultColor = color;
    }

    /**
     * Will clear all the lines in the console
     */
    public void clear() {
        for (ConsoleLine line : lines) {
            if (line instanceof Closeable closeable) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        lines.clear();
    }

    /**
     * Adds a permanent input listener to this console
     */
    public void addInputListener(Consumer<String> listener) {
        permanentListeners.add(listener);
    }

    /**
     * Adds a single timer input listener. After the next input, this listener will fire and then be removed
     */
    public void addSingleTimeListener(Consumer<String> listener) {
        singleListeners.add(listener);
    }

    /**
     * Clears all permanent input listeners for this console. Will not clear single use listeners
     */
    public void clearListeners() {
        permanentListeners.clear();
    }

    /**
     * Disband this console
     */
    @Override
    public void dispose() {
        repaintTimer.stop();
        clear();
        super.dispose();
    }

    /**
     * Register your own renderer for special ConsoleLine implementations
     */
    public void addSpecialRenderer(Class<? extends ConsoleLine> type, ConsoleLineRenderer renderer) {
        specialLineHandlers.put(type, renderer);
    }

    /**
     * Toggles whether text input given by the user should be printed to the console or simply be 'absorbed'
     */
    public void setPrintInput(boolean flag) {
        printInput = flag;
    }

}
