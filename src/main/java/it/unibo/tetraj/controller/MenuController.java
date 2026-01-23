package it.unibo.tetraj.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.tetraj.ApplicationContext;
import it.unibo.tetraj.GameSession;
import it.unibo.tetraj.GameState;
import it.unibo.tetraj.InputHandler;
import it.unibo.tetraj.command.QuitCommand;
import it.unibo.tetraj.command.StateTransitionCommand;
import it.unibo.tetraj.model.MenuModel;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import it.unibo.tetraj.util.ResourceManager;
import it.unibo.tetraj.view.MenuView;
import java.awt.Canvas;
import java.awt.event.KeyEvent;

/** Controller for the menu state. Handles menu logic and input. */
public class MenuController implements Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
  private static final float MUSIC_VOLUME = 0.1f;
  private final ApplicationContext applicationContext;
  private final ResourceManager resources;
  private final MenuModel model;
  private final MenuView view;
  private final InputHandler inputHandler;

  /**
   * Creates a new menu controller.
   *
   * @param applicationContext The application context
   */
  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP2",
      justification = "ApplicationContext is a shared singleton service")
  public MenuController(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    resources = ResourceManager.getInstance();
    model = new MenuModel();
    view = new MenuView();
    inputHandler = new InputHandler();
  }

  /** {@inheritDoc} */
  @Override
  public void enter(final GameSession gameSession) {
    resources.playBackgroundMusic("menuLoop.wav", MUSIC_VOLUME);
    setupKeyBindings();
    LOGGER.info("Entering menu state");
  }

  /** {@inheritDoc} */
  @Override
  public GameSession exit() {
    final GameSession gameSession = GameSession.empty();
    inputHandler.clearBindings();
    LOGGER.info(String.format("Exiting menu state with %s", gameSession));
    return gameSession;
  }

  /** {@inheritDoc} */
  @Override
  public void update(final float deltaTime) {
    // Menu doesn't need updates in this simple version
  }

  /** {@inheritDoc} */
  @Override
  public void render() {
    view.render(model);
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

  /** Sets up the key bindings for menu state. */
  private void setupKeyBindings() {
    // ENTER to start playing
    inputHandler.bindKey(KeyEvent.VK_ENTER, () -> transitionTo(GameState.PLAYING));
    // L to view leaderboard
    inputHandler.bindKey(KeyEvent.VK_L, () -> transitionTo(GameState.LEADERBOARD));
    // ESC to quit
    inputHandler.bindKey(KeyEvent.VK_ESCAPE, new QuitCommand(applicationContext));
  }

  /**
   * Plays menu selection sound and transitions to the specified state.
   *
   * @param state The target game state
   */
  private void transitionTo(final GameState state) {
    resources.playSound("menuSelect.wav");
    new StateTransitionCommand(applicationContext.getStateManager(), state).execute();
  }
}
