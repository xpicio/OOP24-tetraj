package it.unibo.tetraj.view;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.tetraj.util.ApplicationProperties;
import it.unibo.tetraj.util.ResourceManager;
import it.unibo.tetraj.util.ResourceManager.FontSize;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferStrategy;

/**
 * Abstract base class for all game views. Handles common setup for canvas, buffer strategy, and
 * window dimensions.
 *
 * @param <M> The type of model this view renders
 */
public abstract class AbstractView<M> {

  private static final Color DEFAULT_BACKGROUND_COLOR = new Color(20, 20, 30);
  private static final int BUFFER_STRATEGY_BUFFERS = 3;
  private final Canvas canvas;
  private final int windowWidth;
  private final int windowHeight;
  private final Color backgroundColor;
  private final Font displayFont;
  private final Font h1Font;
  private final Font h2Font;
  private final Font bodyFont;
  private final Font captionFont;
  private BufferStrategy bufferStrategy;

  /** Creates a new view with the default background color. */
  protected AbstractView() {
    this(DEFAULT_BACKGROUND_COLOR);
  }

  /**
   * Creates a new view with the specified background color.
   *
   * @param backgroundColor The background color for the canvas
   */
  protected AbstractView(final Color backgroundColor) {
    final ApplicationProperties properties = ApplicationProperties.getInstance();
    final ResourceManager resources = ResourceManager.getInstance();

    windowWidth = properties.getWindowWidth();
    windowHeight = properties.getWindowHeight();
    this.backgroundColor = backgroundColor;
    canvas = new Canvas();
    canvas.setPreferredSize(new Dimension(windowWidth, windowHeight));
    canvas.setBackground(backgroundColor);
    canvas.setFocusable(true);
    displayFont = resources.getPressStart2PFont(FontSize.DISPLAY);
    h1Font = resources.getPressStart2PFont(FontSize.H1);
    h2Font = resources.getPressStart2PFont(FontSize.H2);
    bodyFont = resources.getPressStart2PFont(FontSize.BODY);
    captionFont = resources.getPressStart2PFont(FontSize.CAPTION);
  }

  /**
   * Renders the view with the given model. Template method that initializes the buffer strategy and
   * then delegates to renderContent.
   *
   * @param model The model containing data to render
   */
  public void render(final M model) {
    if (bufferStrategy == null) {
      canvas.createBufferStrategy(BUFFER_STRATEGY_BUFFERS);
      bufferStrategy = canvas.getBufferStrategy();
      if (bufferStrategy == null) {
        return;
      }
    }
    renderContent(model);
  }

  /**
   * Renders the view content. Subclasses implement this to define their specific rendering logic.
   *
   * @param model The model containing data to render
   */
  protected abstract void renderContent(M model);

  /**
   * Gets the canvas component used for rendering.
   *
   * @return The canvas component
   */
  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification = "Canvas must be exposed for GameEngine to mount current view")
  public Canvas getCanvas() {
    return canvas;
  }

  /**
   * Gets the window width.
   *
   * @return The window width in pixels
   */
  protected final int getWindowWidth() {
    return windowWidth;
  }

  /**
   * Gets the window height.
   *
   * @return The window height in pixels
   */
  protected final int getWindowHeight() {
    return windowHeight;
  }

  /**
   * Gets the background color.
   *
   * @return The background color
   */
  protected final Color getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * Gets the buffer strategy for rendering.
   *
   * @return The buffer strategy
   */
  protected final BufferStrategy getBufferStrategy() {
    return bufferStrategy;
  }

  /**
   * Gets the display font (largest size, for titles).
   *
   * @return The display font
   */
  protected final Font getDisplayFont() {
    return displayFont;
  }

  /**
   * Gets the H1 font.
   *
   * @return The H1 font
   */
  protected final Font getH1Font() {
    return h1Font;
  }

  /**
   * Gets the H2 font.
   *
   * @return The H2 font
   */
  protected final Font getH2Font() {
    return h2Font;
  }

  /**
   * Gets the body font.
   *
   * @return The body font
   */
  protected final Font getBodyFont() {
    return bodyFont;
  }

  /**
   * Gets the caption font (smallest size).
   *
   * @return The caption font
   */
  protected final Font getCaptionFont() {
    return captionFont;
  }
}
