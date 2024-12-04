package it.unibo.tetraj.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

/** View for the main menu state. Simple implementation showing "MENU STATE". */
public class MenuView {

  /** Canvas width. */
  private static final int WIDTH = 800;

  /** Canvas height. */
  private static final int HEIGHT = 600;

  /** Background color. */
  private static final Color BACKGROUND = new Color(30, 30, 40);

  /** Text color. */
  private static final Color TEXT_COLOR = Color.WHITE;

  /** The canvas for rendering. */
  private final Canvas canvas;

  /** Buffer strategy for smooth rendering. */
  private BufferStrategy bufferStrategy;

  /** Creates a new menu view. */
  public MenuView() {
    this.canvas = new Canvas();
    canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    canvas.setBackground(Color.BLACK);
    canvas.setFocusable(true);
  }

  /** Initializes the buffer strategy. Must be called after canvas is added to window. */
  public void initialize() {
    if (bufferStrategy == null) {
      canvas.createBufferStrategy(3);
      bufferStrategy = canvas.getBufferStrategy();
    }
  }

  /** Renders the menu view. */
  public void render() {
    if (bufferStrategy == null) {
      initialize();
      if (bufferStrategy == null) {
        return;
      }
    }

    Graphics2D g = null;
    try {
      g = (Graphics2D) bufferStrategy.getDrawGraphics();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // Clear screen
      g.setColor(BACKGROUND);
      g.fillRect(0, 0, WIDTH, HEIGHT);

      // Draw state text
      g.setColor(TEXT_COLOR);
      g.setFont(new Font("Arial", Font.BOLD, 48));
      final String text = "MENU STATE";
      final int textWidth = g.getFontMetrics().stringWidth(text);
      g.drawString(text, (WIDTH - textWidth) / 2, HEIGHT / 2);

      // Draw instructions
      g.setFont(new Font("Arial", Font.PLAIN, 20));
      final String inst = "Press ENTER to play, ESC to quit";
      final int instWidth = g.getFontMetrics().stringWidth(inst);
      g.drawString(inst, (WIDTH - instWidth) / 2, HEIGHT / 2 + 50);

      bufferStrategy.show();
    } finally {
      if (g != null) {
        g.dispose();
      }
    }
  }

  /**
   * Gets the canvas.
   *
   * @return The canvas component
   */
  public Canvas getCanvas() {
    return canvas;
  }
}
