package it.unibo.tetraj.controller;

import it.unibo.tetraj.ApplicationContext;
import it.unibo.tetraj.GameState;
import it.unibo.tetraj.InputHandler;
import it.unibo.tetraj.command.StateTransitionCommand;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import it.unibo.tetraj.view.MenuView;
import java.awt.Canvas;
import java.awt.event.KeyEvent;

/** Controller for the menu state. Handles menu logic and input. */
public class MenuController implements Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
  private final MenuView view;
  private final InputHandler inputHandler;
  private final ApplicationContext context;

  /**
   * Creates a new menu controller.
   *
   * @param context The application context
   */
  public MenuController(final ApplicationContext context) {
    this.context = context;
    this.view = new MenuView();
    this.inputHandler = new InputHandler();

    setupKeyBindings();
  }

  /** Sets up the key bindings for menu state. */
  private void setupKeyBindings() {
    // ENTER to start playing
    inputHandler.bindKey(
        KeyEvent.VK_ENTER,
        new StateTransitionCommand(context.getStateManager(), GameState.PLAYING));

    // ESC to quit - use lambda for simplicity
    inputHandler.bindKey(KeyEvent.VK_ESCAPE, context::shutdown);
  }

  /** {@inheritDoc} */
  @Override
  public void enter() {
    LOGGER.info("Entering menu state");
    // Don't initialize here, let render() handle it lazily
  }

  /** {@inheritDoc} */
  @Override
  public void exit() {
    LOGGER.info("Exiting menu state");
  }

  /** {@inheritDoc} */
  @Override
  public void update(final float deltaTime) {
    // Menu doesn't need updates in this simple version
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
