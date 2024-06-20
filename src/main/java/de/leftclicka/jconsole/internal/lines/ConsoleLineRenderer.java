package de.leftclicka.jconsole.internal.lines;

import java.awt.*;

public interface ConsoleLineRenderer {

    void render(ConsoleLine line, int yLevel, boolean isHovered, Color defaultColor, Graphics2D graphics);

}
