package it.unibo.tetraj.view;

import it.unibo.tetraj.model.GameOverModel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Stream;

/** View for the game over state. Simple implementation showing "GAME OVER" and game statistics. */
public class GameOverView extends AbstractView<GameOverModel> {

  private static final float BACKGROUND_OVERLAY_ALPHA = 0.80f;
  private static final Color H1_TEXT_COLOR = new Color(220, 40, 40);
  private static final Color BODY_TEXT_COLOR = Color.WHITE;
  private static final int FOOTER_BOTTOM_OFFSET = 40;

  /** Creates a new game over view. */
  public GameOverView() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  protected void renderContent(final Graphics2D g, final GameOverModel model) {
    RenderUtils.renderWithGraphics(
        g,
        getBackgroundColor(),
        getWindowWidth(),
        getWindowHeight(),
        () -> {
          // Draw background image if exist
          final BufferedImage backgroundImage = model.getBackgroundImage();
          if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWindowWidth(), getWindowHeight(), null);
            RenderUtils.drawOverlay(
                g, getWindowWidth(), getWindowHeight(), BACKGROUND_OVERLAY_ALPHA);
          }
          // Set defaults
          g.setColor(BODY_TEXT_COLOR);
          g.setFont(getBodyFont());
          // Render text
          RenderUtils.drawCenteredTextBlock(
              g,
              Stream.concat(Stream.of("GAME OVER"), model.getGameOverStats().stream()).toList(),
              getH1Font(),
              H1_TEXT_COLOR,
              getWindowWidth(),
              getWindowHeight());
          // Draw footer
          drawFooter(g);
        });
  }

  /**
   * Draws the footer with instructions.
   *
   * @param g The graphics context
   */
  private void drawFooter(final Graphics2D g) {
    RenderUtils.drawCenteredTextBlockFromBottom(
        g,
        List.of("Press L to view Legends", "Press ESC to return to menu"),
        getBodyFont(),
        BODY_TEXT_COLOR,
        getWindowWidth(),
        getWindowHeight(),
        FOOTER_BOTTOM_OFFSET);
  }
}
