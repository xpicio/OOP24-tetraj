package it.unibo.tetraj.model.pieces;

import java.awt.Color;

/**
 * Interface for all tetromino pieces in the game. Defines the common behavior for all tetromino
 * shapes.
 */
public interface Tetromino {

  /**
   * Moves the tetromino by the specified delta.
   *
   * @param dx the horizontal displacement
   * @param dy the vertical displacement
   */
  void move(int dx, int dy);

  /** Rotates the tetromino 90 degrees clockwise. */
  void rotateClockwise();

  /** Rotates the tetromino 90 degrees counter-clockwise. */
  void rotateCounterClockwise();

  /**
   * Returns the current shape matrix of the tetromino.
   *
   * @return a 2D array representing the tetromino shape
   */
  int[][] getShape();

  /**
   * Returns the width of the tetromino in its current rotation.
   *
   * @return the width in cells
   */
  int getWidth();

  /**
   * Returns the height of the tetromino in its current rotation.
   *
   * @return the height in cells
   */
  int getHeight();

  /**
   * Returns the current X position of the tetromino.
   *
   * @return the X coordinate
   */
  int getX();

  /**
   * Returns the current Y position of the tetromino.
   *
   * @return the Y coordinate
   */
  int getY();

  /**
   * Sets the position of the tetromino.
   *
   * @param x the new X coordinate
   * @param y the new Y coordinate
   */
  void setPosition(int x, int y);

  /**
   * Returns the color of this tetromino.
   *
   * @return the tetromino color
   */
  Color getColor();

  /**
   * Creates a copy of this tetromino.
   *
   * @return a new tetromino instance with the same state
   */
  Tetromino copy();
}
