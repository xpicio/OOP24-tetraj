package it.unibo.tetraj.controller;

import it.unibo.tetraj.ApplicationContext;
import it.unibo.tetraj.GameSession;
import it.unibo.tetraj.InputHandler;
import it.unibo.tetraj.model.leaderboard.PlayerProfile;
import it.unibo.tetraj.model.leaderboard.PlayerProfileManager;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import it.unibo.tetraj.util.ResourceManager;
import java.awt.Canvas;

public class LeaderboardController implements Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(LeaderboardController.class);
  private final ApplicationContext applicationContext;
  private final ResourceManager resources;
  private final PlayerProfile playerProfile;
  private final InputHandler inputHandler;

  public LeaderboardController(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    playerProfile = PlayerProfileManager.getInstance().getProfile();
    resources = ResourceManager.getInstance();
    inputHandler = new InputHandler();
  }

  @Override
  public void enter(GameSession gameSession) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'enter'");
  }

  @Override
  public GameSession exit() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'exit'");
  }

  @Override
  public void update(float deltaTime) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public void render() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'render'");
  }

  @Override
  public void handleInput(int keyCode) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'handleInput'");
  }

  @Override
  public Canvas getCanvas() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getCanvas'");
  }
}
