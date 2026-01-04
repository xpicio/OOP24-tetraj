package it.unibo.tetraj.controller;

import it.unibo.tetraj.ApplicationContext;
import it.unibo.tetraj.GameState;
import it.unibo.tetraj.InputHandler;
import it.unibo.tetraj.command.StateTransitionCommand;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import it.unibo.tetraj.view.PauseView;
import java.awt.Canvas;
import java.awt.event.KeyEvent;

/** Controller for the pause state. Handles pause menu logic and input. */
public class PauseController implements Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(PauseController.class);
  private final PauseView view;
  private final InputHandler inputHandler;
  private final ApplicationContext applicationContext;

  /**
   * Creates a new pause controller.
   *
   * @param applicationContext The application context
   */
  public PauseController(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    view = new PauseView();
    inputHandler = new InputHandler();
  }

  /** {@inheritDoc} */
  @Override
  public void enter() {
    setupKeyBindings();
    LOGGER.info("Entering pause state");
  }

  /** {@inheritDoc} */
  @Override
  public void exit() {
    inputHandler.clearBindings();
    LOGGER.info("Exiting pause state");
  }

  /** {@inheritDoc} */
  @Override
  public void update(final float deltaTime) {
    // Pause doesn't need updates
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

  /** Sets up the key bindings for pause state. */
  private void setupKeyBindings() {
    // P to resume playing
    inputHandler.bindKey(
        KeyEvent.VK_P,
        new StateTransitionCommand(applicationContext.getStateManager(), GameState.PLAYING));

    // ESC to return to menu
    inputHandler.bindKey(
        KeyEvent.VK_ESCAPE,
        new StateTransitionCommand(applicationContext.getStateManager(), GameState.MENU));
  }
}
