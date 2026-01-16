package it.unibo.tetraj.model.leaderboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.tetraj.util.Logger;
import it.unibo.tetraj.util.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Manages player profile persistence to JSON file in user home directory. Singleton with eager
 * initialization for thread-safety and simplicity.
 */
public final class PlayerProfileManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(PlayerProfileManager.class);
  private static final String PROFILE_FILENAME = "tetrajPlayerProfile.json";
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final PlayerProfileManager INSTANCE = new PlayerProfileManager();
  private PlayerProfile currentProfile;

  /**
   * Private constructor loads or creates profile once. Called only once when class is first loaded.
   */
  private PlayerProfileManager() {
    loadOrCreateProfile();
  }

  /**
   * Gets the singleton instance.
   *
   * @return The PlayerProfileManager instance
   */
  public static PlayerProfileManager getInstance() {
    return INSTANCE;
  }

  /**
   * Gets the current player profile.
   *
   * @return The current player profile
   */
  public PlayerProfile getProfile() {
    return currentProfile;
  }

  /**
   * Gets the profile file path in user home directory.
   *
   * @return File reference to the profile JSON file
   */
  private static File getProfileFile() {
    final String userHome = System.getProperty("user.home");

    return Paths.get(userHome, PROFILE_FILENAME).toFile();
  }

  /**
   * Loads existing profile from disk or creates a new one if not found. Called once during
   * singleton initialization.
   */
  private void loadOrCreateProfile() {
    final File profileFile = getProfileFile();

    if (profileFile.exists()) {
      try {
        currentProfile = MAPPER.readValue(profileFile, PlayerProfile.class);
        LOGGER.info(
            "Loaded profile ID: {}, Nickname: {}", currentProfile.id(), currentProfile.nickname());
        return;
      } catch (final IOException e) {
        LOGGER.error(
            "Failed to load profile from {}: {}", profileFile.getAbsolutePath(), e.getMessage());
        LOGGER.info("Creating new profile due to load failure");
      }
    }
    // Create new profile with generated nickname
    currentProfile = PlayerProfile.generateNew();
    save(currentProfile);
    LOGGER.info(
        "Created new profile ID: {}, Nickname: {}", currentProfile.id(), currentProfile.nickname());
  }

  /**
   * Saves the profile to JSON file in user home.
   *
   * @param profile The profile to save
   */
  private void save(final PlayerProfile profile) {
    final File profileFile = getProfileFile();

    currentProfile = profile;
    try {
      MAPPER.writerWithDefaultPrettyPrinter().writeValue(profileFile, profile);
      LOGGER.debug("Profile saved to: {}", profileFile.getAbsolutePath());
    } catch (final IOException e) {
      LOGGER.error(
          "Failed to save profile to {}: {}", profileFile.getAbsolutePath(), e.getMessage());
    }
  }
}
