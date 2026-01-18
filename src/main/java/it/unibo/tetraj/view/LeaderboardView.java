package it.unibo.tetraj.view;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.tetraj.model.LeaderboardModel;
import it.unibo.tetraj.model.LeaderboardModel.LeaderboardDisplayEntry;
import it.unibo.tetraj.util.ApplicationProperties;
import it.unibo.tetraj.util.ResourceManager;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.List;
import java.util.Locale;

/** View for the leaderboard state. Displays top scores with player information. */
public class LeaderboardView {

  private static final Color BACKGROUND_COLOR = new Color(20, 20, 30);
  private static final float BACKGROUND_OVERLAY_ALPHA = 0.90f;
  private static final Color TITLE_TEXT_COLOR = new Color(255, 220, 100);
  private static final Color HEADER_TEXT_COLOR = new Color(180, 180, 180);
  private static final Color FOOTER_TEXT_COLOR = Color.WHITE;
  private static final Color HIGHLIGHT_TEXT_COLOR = new Color(100, 255, 100);
  private static final float TITLE_FONT_SIZE = 32f;
  private static final float HEADER_FONT_SIZE = 15f;
  private static final float ENTRY_FONT_SIZE = 12f;
  private static final float FOOTER_FONT_SIZE = 18f;
  private static final int TITLE_Y_OFFSET = 120;
  private static final int HEADER_Y_OFFSET = 180;
  private static final int ENTRY_START_Y = 240;
  private static final int ENTRY_LINE_HEIGHT = 30;
  private static final int FOOTER_BOTTOM_OFFSET = 40;
  private static final int NICKNAME_MAX_LENGHT = 16;
  private static final float TABLE_WIDTH_PERCENT = 0.80f;
  private static final float[] COLUMN_WIDTHS = {0.08f, 0.30f, 0.22f, 0.12f, 0.12f, 0.16f};
  private final ApplicationProperties applicationProperties;
  private final Canvas canvas;
  private final int windowWidth;
  private final int windowHeight;
  private BufferStrategy bufferStrategy;
  private Image backgroundImage;
  private Font titleFont;
  private Font headerFont;
  private Font entryFont;
  private Font footerFont;

  /** Creates a new leaderboard view. */
  public LeaderboardView() {
    applicationProperties = ApplicationProperties.getInstance();
    windowWidth = applicationProperties.getWindowWidth();
    windowHeight = applicationProperties.getWindowHeight();
    canvas = new Canvas();
    canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));
    canvas.setBackground(BACKGROUND_COLOR);
    canvas.setFocusable(true);
    preloadResources();
  }

  /** Initializes the buffer strategy. */
  public void initialize() {
    if (bufferStrategy == null) {
      canvas.createBufferStrategy(3);
      bufferStrategy = canvas.getBufferStrategy();
    }
  }

  /**
   * Renders the leaderboard view.
   *
   * @param model The leaderboard model containing entries and player info
   */
  public void render(final LeaderboardModel model) {
    if (bufferStrategy == null) {
      initialize();
      if (bufferStrategy == null) {
        return;
      }
    }

    RenderUtils.renderWithGraphics(
        bufferStrategy,
        BACKGROUND_COLOR,
        windowWidth,
        windowHeight,
        g -> {
          // Draw background image with overlay
          RenderUtils.drawBackgroundWithOverlay(
              g, backgroundImage, windowWidth, windowHeight, BACKGROUND_OVERLAY_ALPHA);
          // Draw title
          drawTitle(g);
          // Draw header
          drawHeader(g);
          // Draw entries
          drawEntries(g, model.getEntries(), model.getCurrentPlayerProfileId());
          // Draw footer
          drawFooter(g);
        });
  }

  /**
   * Gets the canvas.
   *
   * @return The canvas component
   */
  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification = "Canvas must be exposed for GameEngine to mount current view")
  public Canvas getCanvas() {
    return canvas;
  }

  /** Preloads all resources needed for the view. */
  private void preloadResources() {
    final ResourceManager resources = ResourceManager.getInstance();

    // Load background image
    backgroundImage = resources.loadImage("splashScreenBackground.png");
    // Load fonts
    titleFont = resources.getPressStart2PFont(TITLE_FONT_SIZE);
    headerFont = resources.getPressStart2PFont(HEADER_FONT_SIZE);
    entryFont = resources.getPressStart2PFont(ENTRY_FONT_SIZE);
    footerFont = resources.getPressStart2PFont(FOOTER_FONT_SIZE);
  }

  /**
   * Draws the title "Block Legends" at the top.
   *
   * @param g The graphics context
   */
  private void drawTitle(final Graphics2D g) {
    final String title = "Block Legends";

    g.setColor(TITLE_TEXT_COLOR);
    g.setFont(titleFont);
    RenderUtils.drawCenteredString(g, windowWidth, TITLE_Y_OFFSET, title.toUpperCase(Locale.ROOT));
  }

  /**
   * Draws the header row with column names.
   *
   * @param g The graphics context
   */
  private void drawHeader(final Graphics2D g) {
    final String[] headers = {"RANK", "PLAYER", "SCORE", "LEVEL", "LINES", "DATE"};

    g.setFont(headerFont);
    g.setColor(HEADER_TEXT_COLOR);
    drawTableRow(g, HEADER_Y_OFFSET, headers);
  }

  /**
   * Draws all leaderboard entries.
   *
   * @param g The graphics context
   * @param entries The list of display entries
   * @param currentPlayerId The current player's ID for highlighting
   */
  private void drawEntries(
      final Graphics2D g,
      final List<LeaderboardDisplayEntry> entries,
      final String currentPlayerId) {
    int yPosition = ENTRY_START_Y;

    g.setFont(entryFont);
    for (final LeaderboardDisplayEntry entry : entries) {
      final String[] row = {
        String.valueOf(entry.rank()),
        truncate(entry.nickname(), NICKNAME_MAX_LENGHT),
        String.valueOf(entry.score()),
        String.valueOf(entry.level()),
        String.valueOf(entry.lines()),
        entry.date(),
      };

      // Highlight current player's entries
      if (entry.playerId().equals(currentPlayerId)) {
        g.setColor(HIGHLIGHT_TEXT_COLOR);
      } else {
        g.setColor(FOOTER_TEXT_COLOR);
      }
      drawTableRow(g, yPosition, row);
      yPosition += ENTRY_LINE_HEIGHT;
    }
  }

  /**
   * Draws a table row with centered text in each column.
   *
   * @param g The graphics context
   * @param y The Y position for the row baseline
   * @param values Array of strings to draw in each column
   */
  private void drawTableRow(final Graphics2D g, final int y, final String[] values) {
    final int tableWidth = (int) (windowWidth * TABLE_WIDTH_PERCENT);
    final int tableStartX = (windowWidth - tableWidth) / 2;
    final java.awt.FontMetrics fontMetrics = g.getFontMetrics();
    int currentX = tableStartX;

    for (int i = 0; i < values.length && i < COLUMN_WIDTHS.length; i++) {
      final int colWidth = (int) (tableWidth * COLUMN_WIDTHS[i]);
      final int textWidth = fontMetrics.stringWidth(values[i]);
      final int textX = currentX + (colWidth - textWidth) / 2;

      g.drawString(values[i], textX, y);
      currentX += colWidth;
    }
  }

  /**
   * Draws the footer with instructions.
   *
   * @param g The graphics context
   */
  private void drawFooter(final Graphics2D g) {
    final String instruction = "Press ESC to return to menu";

    g.setColor(FOOTER_TEXT_COLOR);
    g.setFont(footerFont);
    RenderUtils.drawCenteredString(
        g, windowWidth, windowHeight - FOOTER_BOTTOM_OFFSET, instruction.toUpperCase(Locale.ROOT));
  }

  /**
   * Truncates a string to a maximum length.
   *
   * @param text The string to truncate
   * @param maxLength The maximum length
   * @return The truncated string
   */
  private String truncate(final String text, final int maxLength) {
    if (text.length() <= maxLength) {
      return text;
    }

    return text.substring(0, maxLength - 3) + "...";
  }
}
