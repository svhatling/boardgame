package view.ui;

import controller.MainViewController;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
  private final MainViewController controller;

  public MainView(MainViewController controller) {
    this.controller = controller;

    setTitle("Choose Game");
    setSize(300, 200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    JButton laddersButton = new JButton("Ladders & Snakes");
    JButton ludoButton = new JButton("Ludo");

    laddersButton.addActionListener(e -> controller.selectGame("Ladders"));
    ludoButton.addActionListener(e -> controller.selectGame("Ludo"));

    JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
    panel.add(new JLabel("Welcome! Choose a game:", JLabel.CENTER));
    panel.add(laddersButton);
    panel.add(ludoButton);

    add(panel);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainViewController controller = new MainViewController();
      MainView mainView = new MainView(controller);
      mainView.setVisible(true);
    });
  }
}
