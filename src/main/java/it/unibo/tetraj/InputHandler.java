package it.unibo.tetraj;

import it.unibo.tetraj.command.Command;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles input mapping and command execution. Maps key codes to commands for decoupled input
 * handling.
 */
public class InputHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(InputHandler.class);
  private final Map<Integer, Command> keyBindings;

  /** Creates a new input handler. */
  public InputHandler() {
    this.keyBindings = new HashMap<>();
  }

  /**
   * Binds a key to a command.
   *
   * @param keyCode The key code
   * @param command The command to execute
   */
  public void bindKey(final int keyCode, final Command command) {
    keyBindings.put(keyCode, command);
    LOGGER.info("Bound key {} to command {}", keyCode, command.getClass().getSimpleName());
  }

  /**
   * Unbinds a key.
   *
   * @param keyCode The key code to unbind
   */
  public void unbindKey(final int keyCode) {
    keyBindings.remove(keyCode);
    LOGGER.info("Unbound key {}", keyCode);
  }

  /** Clears all key bindings. */
  public void clearBindings() {
    keyBindings.clear();
    LOGGER.info("Cleared all key bindings");
  }

  /**
   * Handles a key press by executing the bound command.
   *
   * @param keyCode The key code of the pressed key
   * @return true if a command was executed
   */
  public boolean handleKeyPress(final int keyCode) {
    final Command command = keyBindings.get(keyCode);
    if (command != null) {
      command.execute();
      LOGGER.info("Executed command for key {}", keyCode);
      return true;
    }
    return false;
  }

  /**
   * Checks if a key has a binding.
   *
   * @param keyCode The key code
   * @return true if the key has a binding
   */
  public boolean hasBinding(final int keyCode) {
    return keyBindings.containsKey(keyCode);
  }
}
