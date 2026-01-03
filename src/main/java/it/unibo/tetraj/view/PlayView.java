package it.unibo.tetraj.view;

import it.unibo.tetraj.model.Board;
import it.unibo.tetraj.model.PlayModel;
import it.unibo.tetraj.model.piece.AbstractTetromino;
import it.unibo.tetraj.util.ResourceManager;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

/** View for the playing state. Renders the Tetris game. */
public final class PlayView {

  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private static final int CELL_SIZE = 30;
  // Board dimensions
  private static final int BOARD_WIDTH_CELLS = 10;
  private static final int BOARD_HEIGHT_CELLS = 20;
  private static final int BOARD_PIXEL_WIDTH = BOARD_WIDTH_CELLS * CELL_SIZE;
  private static final int BOARD_PIXEL_HEIGHT = BOARD_HEIGHT_CELLS * CELL_SIZE;
  // Layout spacing
  private static final int PADDING = 50;
  private static final int UI_PANEL_WIDTH = 200;
  private static final int TOTAL_CONTENT_WIDTH = BOARD_PIXEL_WIDTH + PADDING + UI_PANEL_WIDTH;
  // Calculated centered positions
  private static final int CONTENT_START_X = (WIDTH - TOTAL_CONTENT_WIDTH) / 2;
  private static final int BOARD_X = CONTENT_START_X;
  private static final int BOARD_Y = (HEIGHT - BOARD_PIXEL_HEIGHT) / 2;
  // Game info panel positions (right of board)
  private static final int TEXT_OFFSET = 20;
  private static final int SCORE_OFFSET_Y = 25;
  private static final int GAME_INFO_PANEL_X = BOARD_X + BOARD_PIXEL_WIDTH + PADDING;
  // Next/Hold block aligned with board top
  private static final int NEXT_Y = BOARD_Y + TEXT_OFFSET;
  private static final int NEXT_BOX_Y = NEXT_Y + 10;
  private static final int HOLD_Y = NEXT_Y + 115;
  private static final int HOLD_BOX_Y = HOLD_Y + 10;
  // Score/Level/Lines block aligned with board bottom
  private static final int INFO_BLOCK_SPACING = 35;
  private static final int LINES_Y = BOARD_Y + BOARD_PIXEL_HEIGHT - TEXT_OFFSET;
  private static final int LEVEL_Y = LINES_Y - INFO_BLOCK_SPACING * 2;
  private static final int SCORE_Y = LEVEL_Y - INFO_BLOCK_SPACING * 2;
  // Colors
  private static final Color BACKGROUND_COLOR = new Color(20, 20, 30);
  private static final Color BOARD_BG = new Color(10, 10, 15);
  private static final Color GRID_COLOR = new Color(40, 40, 50);
  private static final Color GHOST_ALPHA = new Color(255, 255, 255, 33);
  private static final Color TEXT_COLOR = Color.WHITE;
  // Fonts
  private static final int FONT_SIZE = 16;

  private final Canvas canvas;
  private final ResourceManager resources;
  private BufferStrategy bufferStrategy;
  private Font gameFont;

  /** Creates a new play view. */
  public PlayView() {
    this.canvas = new Canvas();
    this.resources = ResourceManager.getInstance();
    canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    canvas.setBackground(BACKGROUND_COLOR);
    canvas.setFocusable(true);
    loadFonts();
  }

  private void loadFonts() {
    gameFont = resources.getPressStart2PFont(FONT_SIZE);
  }

  private void initialize() {
    if (bufferStrategy == null) {
      canvas.createBufferStrategy(3);
      bufferStrategy = canvas.getBufferStrategy();
    }
  }

  /**
   * Renders the play view with the game model.
   *
   * @param model The play model to render
   */
  public void render(final PlayModel model) {
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

      g.setColor(BACKGROUND_COLOR);
      g.fillRect(0, 0, WIDTH, HEIGHT);

      drawBoard(g, model.getBoard());
      drawGhostPiece(g, model.getGhostPiece());
      drawCurrentPiece(g, model.getCurrentPiece());
      drawNextPiece(g, model.getNextPiece());
      drawHeldPiece(g, model.getHeldPiece());
      drawGameInfo(g, model);

      bufferStrategy.show();
    } finally {
      if (g != null) {
        g.dispose();
      }
    }
  }

  private void drawBoard(final Graphics2D g, final Board board) {
    g.setColor(BOARD_BG);
    g.fillRect(BOARD_X, BOARD_Y, BOARD_PIXEL_WIDTH, BOARD_PIXEL_HEIGHT);

    g.setColor(GRID_COLOR);
    for (int row = 0; row <= BOARD_HEIGHT_CELLS; row++) {
      g.drawLine(
          BOARD_X,
          BOARD_Y + row * CELL_SIZE,
          BOARD_X + BOARD_PIXEL_WIDTH,
          BOARD_Y + row * CELL_SIZE);
    }
    for (int col = 0; col <= BOARD_WIDTH_CELLS; col++) {
      g.drawLine(
          BOARD_X + col * CELL_SIZE,
          BOARD_Y,
          BOARD_X + col * CELL_SIZE,
          BOARD_Y + BOARD_PIXEL_HEIGHT);
    }

    for (int row = 0; row < board.getHeight(); row++) {
      for (int col = 0; col < board.getWidth(); col++) {
        final Color cellColor = board.getCellColor(row, col);
        if (cellColor != null) {
          drawCell(g, BOARD_X + col * CELL_SIZE, BOARD_Y + row * CELL_SIZE, cellColor);
        }
      }
    }
  }

  private void drawCurrentPiece(final Graphics2D g, final AbstractTetromino<?> piece) {
    if (piece == null) {
      return;
    }
    drawTetromino(g, piece, BOARD_X, BOARD_Y, piece.getColor());
  }

  private void drawGhostPiece(final Graphics2D g, final AbstractTetromino<?> ghost) {
    if (ghost == null) {
      return;
    }
    drawTetromino(g, ghost, BOARD_X, BOARD_Y, GHOST_ALPHA);
  }

  private void drawNextPiece(final Graphics2D g, final AbstractTetromino<?> next) {
    g.setColor(TEXT_COLOR);
    g.setFont(gameFont);
    g.drawString("NEXT", GAME_INFO_PANEL_X, NEXT_Y);

    if (next != null) {
      drawTetrominoPreview(g, next, GAME_INFO_PANEL_X, NEXT_BOX_Y);
    }
  }

  private void drawHeldPiece(final Graphics2D g, final AbstractTetromino<?> held) {
    g.setColor(TEXT_COLOR);
    g.setFont(gameFont);
    g.drawString("HOLD", GAME_INFO_PANEL_X, HOLD_Y);

    if (held != null) {
      drawTetrominoPreview(g, held, GAME_INFO_PANEL_X, HOLD_BOX_Y);
    }
  }

  private void drawTetromino(
      final Graphics2D g,
      final AbstractTetromino<?> piece,
      final int boardX,
      final int boardY,
      final Color color) {
    final int[][] shape = piece.getShape();
    final int pieceX = piece.getX();
    final int pieceY = piece.getY();

    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] != 0) {
          final int x = boardX + (pieceX + col) * CELL_SIZE;
          final int y = boardY + (pieceY + row) * CELL_SIZE;
          drawCell(g, x, y, color);
        }
      }
    }
  }

  private void drawTetrominoPreview(
      final Graphics2D g, final AbstractTetromino<?> piece, final int x, final int y) {
    final int[][] shape = piece.getShape();
    for (int row = 0; row < shape.length; row++) {
      for (int col = 0; col < shape[row].length; col++) {
        if (shape[row][col] != 0) {
          drawCell(g, x + col * CELL_SIZE, y + row * CELL_SIZE, piece.getColor());
        }
      }
    }
  }

  private void drawCell(final Graphics2D g, final int x, final int y, final Color color) {
    g.setColor(color);
    g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);

    g.setColor(color.brighter());
    g.drawLine(x + 1, y + 1, x + CELL_SIZE - 2, y + 1);
    g.drawLine(x + 1, y + 1, x + 1, y + CELL_SIZE - 2);

    g.setColor(color.darker());
    g.drawLine(x + CELL_SIZE - 1, y + 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1);
    g.drawLine(x + 1, y + CELL_SIZE - 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1);
  }

  private void drawGameInfo(final Graphics2D g, final PlayModel model) {
    g.setColor(TEXT_COLOR);
    g.setFont(gameFont);

    // Score block
    g.drawString("SCORE", GAME_INFO_PANEL_X, SCORE_Y);
    g.drawString(String.valueOf(model.getScore()), GAME_INFO_PANEL_X, SCORE_Y + SCORE_OFFSET_Y);

    // Level block
    g.drawString("LEVEL", GAME_INFO_PANEL_X, LEVEL_Y);
    g.drawString(String.valueOf(model.getLevel()), GAME_INFO_PANEL_X, LEVEL_Y + SCORE_OFFSET_Y);

    // Lines block (aligned with board bottom)
    g.drawString("LINES", GAME_INFO_PANEL_X, LINES_Y);
    g.drawString(
        String.valueOf(model.getLinesCleared()), GAME_INFO_PANEL_X, LINES_Y + SCORE_OFFSET_Y);
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
