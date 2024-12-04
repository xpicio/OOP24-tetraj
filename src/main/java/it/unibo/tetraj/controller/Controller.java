package it.unibo.tetraj.controller;

import java.awt.Canvas;

/** Interface for controllers. Each game state has its own implementation handling its logic. */
public interface Controller {

  /** Called when entering this state. Initialize or reset state-specific resources. */
  void enter();

  /** Called when exiting this state. Cleanup state-specific resources. */
  void exit();

  /**
   * Updates the state logic.
   *
   * @param deltaTime Time elapsed since last update in seconds
   */
  void update(float deltaTime);

  /** Renders the state visuals. */
  void render();

  /**
   * Handles input for this state.
   *
   * @param keyCode The key code of the pressed key
   */
  void handleInput(int keyCode);

  /**
   * Gets the canvas for this state's view.
   *
   * @return The canvas component
   */
  Canvas getCanvas();
}
