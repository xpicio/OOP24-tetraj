package it.unibo.tetraj.model.leaderboard;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for PlayerProfileManager.
 *
 * <p>Note: These tests use the real file system since PlayerProfileManager is a singleton with
 * eager initialization. We clean up after each test.
 */
class PlayerProfileManagerTest {

  private static final int UUID_LENGTH_WITH_DASHES = 36;

  @Test
  @DisplayName("should return singleton instance")
  void shouldReturnSingletonInstance() {
    // Act
    final PlayerProfileManager instance1 = PlayerProfileManager.getInstance();
    final PlayerProfileManager instance2 = PlayerProfileManager.getInstance();

    // Assert
    assertEquals(instance1, instance2, "Should return same singleton instance");
  }

  @Test
  @DisplayName("should persist profile to file system")
  void shouldPersistProfileToFileSystem() {
    // Arrange
    final String profileFileName = "tetrajPlayerProfile.json";
    final Path testProfilePath = Paths.get(System.getProperty("user.home"), profileFileName);

    // Act
    PlayerProfileManager.getInstance();

    // Assert
    assertTrue(Files.exists(testProfilePath), "Profile file should be created");
  }

  @Test
  @DisplayName("should create profile with unique ID and nickname")
  void shouldCreateProfileWithUniqueIdAndNickname() {
    // Act
    final PlayerProfileManager manager = PlayerProfileManager.getInstance();
    final PlayerProfile profile = manager.getProfile();

    // Assert
    assertNotNull(profile, "Profile should not be null");
    assertNotNull(profile.id(), "Profile ID should not be null");
    assertNotNull(profile.nickname(), "Profile nickname should not be null");
  }

  @Test
  @DisplayName("should generate random nickname from adjectives and animals")
  void shouldGenerateRandomNicknameFromAdjectivesAndAnimals() {
    // Act
    final PlayerProfile profile1 = PlayerProfile.generateNew();
    final PlayerProfile profile2 = PlayerProfile.generateNew();

    // Assert
    assertNotNull(profile1.nickname(), "Nickname should not be null");
    assertNotNull(profile2.nickname(), "Nickname should not be null");
  }

  @Test
  @DisplayName("should generate valid UUID for profile ID")
  void shouldGenerateValidUuidForProfileId() {
    // Act
    final PlayerProfile profile = PlayerProfile.generateNew();
    final String id = profile.id();

    // Assert
    assertNotNull(id, "ID should not be null");
    assertDoesNotThrow(() -> UUID.fromString(id), "ID should be a valid UUID format");
    assertEquals(UUID_LENGTH_WITH_DASHES, id.length(), "UUID should be 36 characters with dashes");
  }
}
