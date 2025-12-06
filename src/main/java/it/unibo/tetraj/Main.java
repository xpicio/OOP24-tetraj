package it.unibo.tetraj;

/**
 * Application entry point for Tetraj game.
 *
 * <p>This class serves as the main entry point for the Tetraj application, handling initial system
 * configuration before delegating to the ApplicationContext for the actual application bootstrap.
 */
public final class Main {

  /**
   * Private constructor to prevent instantiation. This is a utility class with only static methods.
   */
  private Main() {
    // Prevent instantiation
  }

  /**
   * Main entry point of the application.
   *
   * @param args Command line arguments
   */
  public static void main(final String[] args) {
    configureApplicationProperties();

    final ApplicationContext context = new ApplicationContext();
    context.bootstrap();
  }

  /** Configures application name for all platforms. */
  private static void configureApplicationProperties() {
    final String appName = "Tetraj";

    // Get OS name for platform-specific configurations
    final String osName = System.getProperty("os.name").toLowerCase(java.util.Locale.ROOT);

    // macOS - set app name in menu bar
    if (osName.contains("mac")) {
      System.setProperty("apple.awt.application.name", appName);
    }

    // Cross-platform anti-aliasing for better text rendering
    System.setProperty("awt.useSystemAAFontSettings", "on");
    System.setProperty("swing.aatext", "true");
  }
}
