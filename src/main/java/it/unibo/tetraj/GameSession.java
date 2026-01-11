package it.unibo.tetraj;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable game session data that is passed between game states. Contains all relevant information
 * about a game session including score, level, timing, and visual state.
 */
public final class GameSession {

  private final Integer score;
  private final Integer level;
  private final Integer linesCleared;
  private final BufferedImage lastFrame;
  private final Instant gameStartTime;
  private final Instant gameEndTime;

  /**
   * Creates a new game session with the specified values. Validates input and creates defensive
   * copies where needed.
   *
   * @param score The score achieved
   * @param level The level reached
   * @param linesCleared Number of lines cleared
   * @param lastFrame The last rendered frame (will be copied)
   * @param gameStartTime When the game started
   * @param gameEndTime When the game ended
   */
  public GameSession(
      final Integer score,
      final Integer level,
      final Integer linesCleared,
      final BufferedImage lastFrame,
      final Instant gameStartTime,
      final Instant gameEndTime) {
    // Validation
    if (score != null && score < 0) {
      throw new IllegalArgumentException("Score cannot be negative");
    }
    if (level != null && level < 0) {
      throw new IllegalArgumentException("Level cannot be negative");
    }
    if (linesCleared != null && linesCleared < 0) {
      throw new IllegalArgumentException("Lines cleared cannot be negative");
    }

    this.score = score;
    this.level = level;
    this.linesCleared = linesCleared;
    this.lastFrame = copyImage(lastFrame);
    this.gameStartTime = gameStartTime;
    this.gameEndTime = gameEndTime;
  }

  /**
   * Creates an empty game session with all fields set to null. Used as initial state or when
   * transitioning to non-game states.
   *
   * @return An empty GameSession instance
   */
  public static GameSession empty() {
    return new GameSession(null, null, null, null, null, null);
  }

  /**
   * Checks if this session contains game data.
   *
   * @return true if the session has no game data, false otherwise
   */
  public boolean isEmpty() {
    return score == null && level == null && linesCleared == null;
  }

  /**
   * Checks if this session has game data.
   *
   * @return true if the session contains game data, false otherwise
   */
  public boolean hasData() {
    return !isEmpty();
  }

  /**
   * Calculates the duration of the game session.
   *
   * @return The duration between start and end, or Duration.ZERO if times are not set
   */
  public Duration getDuration() {
    if (gameStartTime != null && gameEndTime != null) {
      return Duration.between(gameStartTime, gameEndTime);
    }
    return Duration.ZERO;
  }

  /**
   * Gets the score.
   *
   * @return The score, may be null
   */
  public Integer score() {
    return score;
  }

  /**
   * Gets the level.
   *
   * @return The level, may be null
   */
  public Integer level() {
    return level;
  }

  /**
   * Gets the lines cleared.
   *
   * @return The lines cleared, may be null
   */
  public Integer linesCleared() {
    return linesCleared;
  }

  /**
   * Gets the last frame. Returns a defensive copy to maintain immutability.
   *
   * @return A copy of the last frame, may be null
   */
  public BufferedImage lastFrame() {
    return copyImage(lastFrame);
  }

  /**
   * Gets the game start time.
   *
   * @return The start time, may be null
   */
  public Instant gameStartTime() {
    return gameStartTime;
  }

  /**
   * Gets the game end time.
   *
   * @return The end time, may be null
   */
  public Instant gameEndTime() {
    return gameEndTime;
  }

  /**
   * Gets the score as an Optional.
   *
   * @return Optional containing the score, or empty if not set
   */
  public Optional<Integer> getScore() {
    return Optional.ofNullable(score);
  }

  /**
   * Gets the level as an Optional.
   *
   * @return Optional containing the level, or empty if not set
   */
  public Optional<Integer> getLevel() {
    return Optional.ofNullable(level);
  }

  /**
   * Gets the lines cleared as an Optional.
   *
   * @return Optional containing the lines cleared, or empty if not set
   */
  public Optional<Integer> getLinesCleared() {
    return Optional.ofNullable(linesCleared);
  }

  /**
   * Gets the last frame as an Optional. Returns a defensive copy to maintain immutability.
   *
   * @return Optional containing a copy of the last frame, or empty if not set
   */
  public Optional<BufferedImage> getLastFrame() {
    return Optional.ofNullable(copyImage(lastFrame));
  }

  /**
   * Gets the game start time as an Optional.
   *
   * @return Optional containing the start time, or empty if not set
   */
  public Optional<Instant> getGameStartTime() {
    return Optional.ofNullable(gameStartTime);
  }

  /**
   * Gets the game end time as an Optional.
   *
   * @return Optional containing the end time, or empty if not set
   */
  public Optional<Instant> getGameEndTime() {
    return Optional.ofNullable(gameEndTime);
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final GameSession other = (GameSession) obj;
    return Objects.equals(score, other.score)
        && Objects.equals(level, other.level)
        && Objects.equals(linesCleared, other.linesCleared)
        && bufferedImagesEqual(lastFrame, other.lastFrame)
        && Objects.equals(gameStartTime, other.gameStartTime)
        && Objects.equals(gameEndTime, other.gameEndTime);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(
        score,
        level,
        linesCleared,
        lastFrame != null ? lastFrame.getWidth() * lastFrame.getHeight() : 0,
        gameStartTime,
        gameEndTime);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return String.format(
        "GameSession[score=%d, level=%d, linesCleared=%d, lastFrame=%s, gameStartTime=%s, gameEndTime=%s]",
        score,
        level,
        linesCleared,
        lastFrame != null ? "present" : "null",
        gameStartTime,
        gameEndTime);
  }

  /**
   * Creates a new Builder for constructing GameSession instances.
   *
   * @return A new Builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a defensive copy of a BufferedImage.
   *
   * @param source The image to copy
   * @return A new BufferedImage with the same content, or null if source is null
   */
  private static BufferedImage copyImage(final BufferedImage source) {
    if (source == null) {
      return null;
    }
    final BufferedImage copy =
        new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
    final Graphics2D g = copy.createGraphics();
    g.drawImage(source, 0, 0, null);
    g.dispose();
    return copy;
  }

  /**
   * Compares two BufferedImages for equality based on dimensions and type.
   *
   * @param img1 The first image to compare
   * @param img2 The second image to compare
   * @return true if the images have the same dimensions and type, false otherwise
   */
  private static boolean bufferedImagesEqual(final BufferedImage img1, final BufferedImage img2) {
    if (img1 == null || img2 == null) {
      return false;
    }

    return Objects.equals(img1, img2)
        && img1.getWidth() == img2.getWidth()
        && img1.getHeight() == img2.getHeight()
        && img1.getType() == img2.getType();
  }

  /**
   * Builder class for constructing GameSession instances with a fluent API. Allows selective
   * setting of fields without requiring all parameters.
   */
  public static final class Builder {
    private Integer score;
    private Integer level;
    private Integer linesCleared;
    private BufferedImage lastFrame;
    private Instant gameStartTime;
    private Instant gameEndTime;

    /** Private constructor to enforce builder pattern. */
    private Builder() {
      // Empty constructor
    }

    /**
     * Sets the score value.
     *
     * @param scoreValue The score to set
     * @return This builder for chaining
     */
    public Builder withScore(final int scoreValue) {
      this.score = scoreValue;
      return this;
    }

    /**
     * Sets the level value.
     *
     * @param levelValue The level to set
     * @return This builder for chaining
     */
    public Builder withLevel(final int levelValue) {
      this.level = levelValue;
      return this;
    }

    /**
     * Sets the lines cleared value.
     *
     * @param lines The number of lines cleared
     * @return This builder for chaining
     */
    public Builder withLinesCleared(final int lines) {
      this.linesCleared = lines;
      return this;
    }

    /**
     * Sets the last frame image. Creates a defensive copy to ensure immutability.
     *
     * @param frame The BufferedImage of the last frame
     * @return This builder for chaining
     */
    public Builder withLastFrame(final BufferedImage frame) {
      this.lastFrame = copyImage(frame);
      return this;
    }

    /**
     * Sets the game start time.
     *
     * @param start The instant when the game started
     * @return This builder for chaining
     */
    public Builder withGameStart(final Instant start) {
      this.gameStartTime = start;
      return this;
    }

    /**
     * Sets the game end time.
     *
     * @param end The instant when the game ended
     * @return This builder for chaining
     */
    public Builder withGameEnd(final Instant end) {
      this.gameEndTime = end;
      return this;
    }

    /**
     * Marks the current time as the game start time. Convenience method equivalent to
     * withGameStart(Instant.now()).
     *
     * @return This builder for chaining
     */
    public Builder markGameStart() {
      this.gameStartTime = Instant.now();
      return this;
    }

    /**
     * Marks the current time as the game end time. Convenience method equivalent to
     * withGameEnd(Instant.now()).
     *
     * @return This builder for chaining
     */
    public Builder markGameEnd() {
      this.gameEndTime = Instant.now();
      return this;
    }

    /**
     * Builds and returns the GameSession instance. Creates an immutable GameSession with the
     * configured values.
     *
     * @return A new GameSession instance with the builder's values
     */
    public GameSession build() {
      return new GameSession(score, level, linesCleared, lastFrame, gameStartTime, gameEndTime);
    }
  }
}
