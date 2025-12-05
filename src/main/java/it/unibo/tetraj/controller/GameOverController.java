package it.unibo.tetraj.controller;

import it.unibo.tetraj.ApplicationContext;
import it.unibo.tetraj.GameState;
import it.unibo.tetraj.InputHandler;
import it.unibo.tetraj.command.StateTransitionCommand;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import it.unibo.tetraj.view.GameOverView;
import java.awt.Canvas;
import java.awt.event.KeyEvent;

/** Controller for the game over state. Handles game over logic and input. */
public class GameOverController implements Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(GameOverController.class);
  private final GameOverView view;
  private final InputHandler inputHandler;
  private final ApplicationContext context;

  /**
   * Creates a new game over controller.
   *
   * @param context The application context
   */
  public GameOverController(final ApplicationContext context) {
    this.context = context;
    this.view = new GameOverView();
    this.inputHandler = new InputHandler();

    setupKeyBindings();
  }

  /** Sets up the key bindings for game over state. */
  private void setupKeyBindings() {
    // ENTER to restart (play again)
    inputHandler.bindKey(
        KeyEvent.VK_ENTER,
        new StateTransitionCommand(context.getStateManager(), GameState.PLAYING));

    // ESC to return to menu
    inputHandler.bindKey(
        KeyEvent.VK_ESCAPE, new StateTransitionCommand(context.getStateManager(), GameState.MENU));
  }

  /** {@inheritDoc} */
  @Override
  public void enter() {
    LOGGER.info("Entering game over state");
    // Don't initialize here, let render() handle it lazily
  }

  /** {@inheritDoc} */
  @Override
  public void exit() {
    LOGGER.info("Exiting game over state");
  }

  /** {@inheritDoc} */
  @Override
  public void update(final float deltaTime) {
    // Game over doesn't need updates
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
