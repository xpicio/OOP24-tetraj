package it.unibo.tetraj.model.pieces;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public final class TetrominoRegistry {
  private static final TetrominoRegistry INSTANCE = new TetrominoRegistry();
  private final Map<
          Class<? extends AbstractTetromino<?>>, BiFunction<Integer, Integer, AbstractTetromino<?>>>
      factories;
  private final List<Class<? extends AbstractTetromino<?>>> availableTypes;

  private TetrominoRegistry() {
    this.factories =
        Map.of(
            ITetromino.class, (x, y) -> new ITetromino(x, y),
            OTetromino.class, (x, y) -> new OTetromino(x, y),
            TTetromino.class, (x, y) -> new TTetromino(x, y),
            STetromino.class, (x, y) -> new STetromino(x, y),
            ZTetromino.class, (x, y) -> new ZTetromino(x, y),
            JTetromino.class, (x, y) -> new JTetromino(x, y),
            LTetromino.class, (x, y) -> new LTetromino(x, y));
    this.availableTypes = List.copyOf(factories.keySet());
  }

  public static TetrominoRegistry getInstance() {
    return INSTANCE;
  }

  public List<Class<? extends AbstractTetromino<?>>> getAvailableTypes() {
    return availableTypes;
  }

  public AbstractTetromino<?> create(final Class<? extends AbstractTetromino<?>> type, final int x, final int y) {
    return factories.get(type).apply(x, y);
  }
}
