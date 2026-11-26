package de.pizzamanager;
import de.pizzamanager.gui.PizzaFrame;

import javax.swing.*;
public class Main { public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new PizzaFrame().setVisible(true)); }
}
