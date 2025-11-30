package it.unibo.tetraj.model.pieces;

import java.awt.Color;

public interface Tetromino {
  void move(int dx, int dy);

  void rotateClockwise();

  void rotateCounterClockwise();

  int[][] getShape();

  int getWidth();

  int getHeight();

  int getX();

  int getY();

  void setPosition(int x, int y);

  Color getColor();

  Tetromino copy();
}
