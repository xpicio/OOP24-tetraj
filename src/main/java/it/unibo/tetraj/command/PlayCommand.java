package it.unibo.tetraj.command;

import it.unibo.tetraj.model.PlayModel;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import java.util.function.Consumer;

/**
 * A generic command implementation responsible for executing specific operations on the active game
 * model.
 */
public class PlayCommand implements Command {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayCommand.class);
  private final PlayModel playModel;
  private final Consumer<PlayModel> action;
  private final String actionName;

  /**
   * Constructs a new command instance to apply a specific operation to the game model.
   *
   * @param playModel The target game model upon which the action will be performed.
   * @param action The functional logic (Consumer) defining the state change to apply.
   * @param actionName A descriptive identifier for the action, used primarily for logging and
   *     debugging purposes.
   */
  public PlayCommand(
      final PlayModel playModel, final Consumer<PlayModel> action, final String actionName) {
    this.playModel = playModel;
    this.action = action;
    this.actionName = actionName;
  }

  /** {@inheritDoc} */
  @Override
  public void execute() {
    LOGGER.debug("Move command executed, {}", actionName);
    action.accept(playModel);
  }
}
