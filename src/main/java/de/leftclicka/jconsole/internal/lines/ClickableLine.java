package de.leftclicka.jconsole.internal.lines;

public class ClickableLine extends TooltippedLine {
    private static final Runnable empty = ()->{};
    private final Runnable onClick;
    private final boolean isSingleUse;
    private boolean isUsed = false;
    public ClickableLine(String text, String tooltip, boolean singleUse, Runnable onClick) {
        super(text, tooltip);
        this.onClick = onClick;
        this.isSingleUse = singleUse;
    }

    public ClickableLine(String text, String tooltip, Runnable onClick) {
        this(text, tooltip, false, onClick);
    }

    public ClickableLine(String text, Runnable onClick) {
        this(text, text, false, onClick);
    }

    public ClickableLine(String text, boolean singleUse, Runnable onClick) {
        this(text, text, singleUse, onClick);
    }

    public Runnable getTask() {
        if (isSingleUse && isUsed)
            return empty;
        return onClick;
    }

    public void setUsed(boolean flag) {
        if (isSingleUse)
            isUsed = flag;
    }
}
