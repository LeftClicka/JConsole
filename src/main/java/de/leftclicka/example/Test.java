package de.leftclicka.example;

import de.leftclicka.jconsole.JConsole;
import de.leftclicka.jconsole.internal.lines.*;

import java.awt.*;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        JConsole frame = new JConsole(1000, 600);
        frame.setVisible(true);
        frame.addInputListener(in -> frame.write("You typed "+in));
        frame.write(new ColorCodedLine("Hello World!").addColorCode(0, 4, Color.BLUE).addColorCode(5, 11, Color.GREEN));
        frame.write(new ClickableLine("Click me", "Click this", true, () -> frame.write("You clicked something")));
        frame.write(new AnimatedLine(3, "Loading", "Loading.", "Loading..", "Loading..."));
        frame.write(new TooltippedLine("Hover me", "This line has a tooltip"));
        frame.write("Try typing something...");
        frame.write(new ClickableLine("Click to disband", frame::dispose));
    }

}
