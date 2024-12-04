package it.unibo.tetraj.controller;

import it.unibo.tetraj.ApplicationContext;
import it.unibo.tetraj.GameState;
import it.unibo.tetraj.InputHandler;
import it.unibo.tetraj.command.StateTransitionCommand;
import it.unibo.tetraj.utils.Logger;
import it.unibo.tetraj.utils.LoggerFactory;
import it.unibo.tetraj.view.PlayView;
import java.awt.Canvas;
import java.awt.event.KeyEvent;

/** Controller for the playing state. Handles game logic and input during gameplay. */
public class PlayController implements Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayController.class);
  private final PlayView view;
  private final InputHandler inputHandler;
  private final ApplicationContext context;
  private float elapsedTime;

  /**
   * Creates a new play controller.
   *
   * @param context The application context
   */
  public PlayController(final ApplicationContext context) {
    this.context = context;
    this.view = new PlayView();
    this.inputHandler = new InputHandler();
    this.elapsedTime = 0;

    setupKeyBindings();
  }

  /** Sets up the key bindings for play state. */
  private void setupKeyBindings() {
    // P to pause
    inputHandler.bindKey(
        KeyEvent.VK_P, new StateTransitionCommand(context.getStateManager(), GameState.PAUSED));

    // ESC to return to menu
    inputHandler.bindKey(
        KeyEvent.VK_ESCAPE, new StateTransitionCommand(context.getStateManager(), GameState.MENU));
  }

  /** {@inheritDoc} */
  @Override
  public void enter() {
    LOGGER.info("Entering play state");
    // Don't initialize here, let render() handle it lazily
    elapsedTime = 0;
  }

  /** {@inheritDoc} */
  @Override
  public void exit() {
    LOGGER.info("Exiting play state");
  }

  /** {@inheritDoc} */
  @Override
  public void update(final float deltaTime) {
    // Simulate game logic
    elapsedTime += deltaTime;

    // Demo: automatically trigger game over after 10 seconds
    if (elapsedTime > 10) {
      LOGGER.info("Simulated game over!");
      context.getStateManager().switchTo(GameState.GAME_OVER);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void render() {
    view.render();
  }

  /** {@inheritDoc} */
  @Override
  public void handleInput(final int keyCode) {
    inputHandler.handleKeyPress(keyCode);
  }

  /** {@inheritDoc} */
  @Override
  public Canvas getCanvas() {
    return view.getCanvas();
  }
}
