package de.leftclicka.jconsole.internal.lines;

import java.util.function.Consumer;

public class ClickableLineGroup {

    private String chosen = null;
    private final ClickableLine[] lines;

    public ClickableLineGroup(String[] lines, String[] tooltips) {
        this.lines = new ClickableLine[lines.length];
        for (int i = 0; i < lines.length; i++) {
            final String option = lines[i];
            final String tooltip = tooltips[i];
            this.lines[i] = new ClickableLine(option, tooltip, true, () ->
                    chosen = option
            );
        }
    }

    public ClickableLineGroup(String... lines) {
        this(lines, lines);
    }

    /**
     * Blocks the calling thread until a choice is made
     */
    public String getChoice() {
        while (chosen == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (ClickableLine line : lines)
            line.setUsed(true);
        return chosen;
    }

    public void getChoiceAsync(Consumer<String> action) {
        new Thread(() -> action.accept(getChoice())).start();
    }

}
