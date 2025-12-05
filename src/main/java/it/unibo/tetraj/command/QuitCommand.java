package it.unibo.tetraj.command;

import it.unibo.tetraj.ApplicationContext;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;

/** Command to quit the game gracefully. */
public class QuitCommand implements Command {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuitCommand.class);
  private final ApplicationContext context;

  /**
   * Creates a new quit command.
   *
   * @param context The application context
   */
  public QuitCommand(final ApplicationContext context) {
    this.context = context;
  }

  /** {@inheritDoc} */
  @Override
  public void execute() {
    LOGGER.info("Quit command executed, requesting application shutdown");
    context.shutdown();
  }
}
