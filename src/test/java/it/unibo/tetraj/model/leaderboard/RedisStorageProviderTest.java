package it.unibo.tetraj.model.leaderboard;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Unit tests for RedisStorageProvider using Mockito. */
class RedisStorageProviderTest {

  private static final String REDIS_DEFAULT_HOSTNAME = "localhost";
  private static final String REDIS_IP_ADDRESS = "10.255.255.1"; // NOPMD - AvoidUsingHardCodedIP
  private static final int REDIS_DEFAULT_PORT = 6379;
  private static final int REDIS_SECURE_PORT = 6380;
  private static final String LEADERBOARD_ENTRY_P1_ID = "player1";
  private static final String LEADERBOARD_ENTRY_P1_NICKNAME = "Alice";
  private static final int LEADERBOARD_ENTRY_P1_SCORE = 1000;
  private static final int LEADERBOARD_ENTRY_P1_LEVEL = 5;
  private static final int LEADERBOARD_ENTRY_P1_LINES = 20;
  private static final int LEADERBOARD_ENTRY_P1_DURATION = 10;
  private RedisStorageProvider provider;

  @BeforeEach
  void setUp() {
    // Arrange - create provider with invalid hostname to prevent real connections
    provider =
        new RedisStorageProvider(
            false,
            "invalid-host-for-testing",
            REDIS_DEFAULT_PORT,
            Optional.empty(),
            Optional.empty());
  }

  @Test
  @DisplayName("should not be available when DNS resolution fails")
  void shouldNotBeAvailableWhenDnsResolutionFails() {
    // Act
    provider.initialize();

    // Assert
    assertFalse(provider.isAvailable(), "Provider should not be available when DNS fails");
  }

  @Test
  @DisplayName("should include connection string in provider name")
  void shouldIncludeConnectionStringInProviderName() {
    // Act
    final String name = provider.getName();

    // Assert
    assertTrue(name.contains("Redis"), "Name should contain 'Redis'");
    assertTrue(name.contains("invalid-host-for-testing"), "Name should contain hostname");
  }

  @Test
  @DisplayName("should mask password in provider name")
  void shouldMaskPasswordInProviderName() {
    // Arrange
    final RedisStorageProvider secureProvider =
        new RedisStorageProvider(
            true,
            REDIS_DEFAULT_HOSTNAME,
            REDIS_DEFAULT_PORT,
            Optional.of("user"),
            Optional.of("secret123"));

    // Act
    final String name = secureProvider.getName();

    // Assert
    assertTrue(name.contains("***"), "Password should be masked");
    assertFalse(name.contains("secret123"), "Password should not be visible");
  }

  @Test
  @DisplayName("should return empty list when not available")
  void shouldReturnEmptyListWhenNotAvailable() {
    // Arrange
    provider.initialize();

    // Act
    final List<LeaderboardEntry> entries = provider.getTop();

    // Assert
    assertTrue(entries.isEmpty(), "Should return empty list when not available");
  }

  @Test
  @DisplayName("should not save when not available")
  void shouldNotSaveWhenNotAvailable() {
    // Arrange
    final LeaderboardEntry entry =
        new LeaderboardEntry(
            LEADERBOARD_ENTRY_P1_ID,
            LEADERBOARD_ENTRY_P1_NICKNAME,
            LEADERBOARD_ENTRY_P1_SCORE,
            Instant.now(),
            LEADERBOARD_ENTRY_P1_LEVEL,
            LEADERBOARD_ENTRY_P1_LINES,
            Duration.ofMinutes(LEADERBOARD_ENTRY_P1_DURATION));
    provider.initialize();

    // Act
    final boolean saved = provider.save(entry);

    // Assert
    assertFalse(saved, "Should not save when provider is not available");
  }

  @Test
  @DisplayName("should use SSL scheme for secure connections")
  void shouldUseSslSchemeForSecureConnections() {
    // Arrange
    final RedisStorageProvider secureProvider =
        new RedisStorageProvider(
            true, "secure-host", REDIS_SECURE_PORT, Optional.empty(), Optional.empty());

    // Act
    final String name = secureProvider.getName();

    // Assert
    assertTrue(name.contains("rediss://"), "Should use rediss:// scheme for SSL connections");
  }

  @Test
  @DisplayName("should use plain scheme for non-SSL connections")
  void shouldUsePlainSchemeForNonSslConnections() {
    // Arrange
    final RedisStorageProvider plainProvider =
        new RedisStorageProvider(
            false, "plain-host", REDIS_DEFAULT_PORT, Optional.empty(), Optional.empty());

    // Act
    final String name = plainProvider.getName();

    // Assert
    assertTrue(name.contains("redis://"), "Should use redis:// scheme for plain connections");
  }

  @Test
  @DisplayName("should handle connection timeout during initialize")
  void shouldHandleConnectionTimeoutDuringInitialize() {
    // Arrange
    final RedisStorageProvider timeoutProvider =
        new RedisStorageProvider(
            false, REDIS_IP_ADDRESS, REDIS_DEFAULT_PORT, Optional.empty(), Optional.empty());

    // Act
    timeoutProvider.initialize();

    // Assert
    assertFalse(timeoutProvider.isAvailable(), "Should not be available after connection timeout");
  }

  @Test
  @DisplayName("should create provider with username and password")
  void shouldCreateProviderWithUsernameAndPassword() {
    // Arrange & Act
    final RedisStorageProvider authProvider =
        new RedisStorageProvider(
            false,
            REDIS_DEFAULT_HOSTNAME,
            REDIS_DEFAULT_PORT,
            Optional.of("testuser"),
            Optional.of("testpass"));
    final String name = authProvider.getName();

    // Assert
    assertTrue(name.contains("testuser"), "Username should be in connection string");
  }

  @Test
  @DisplayName("should create provider without authentication")
  void shouldCreateProviderWithoutAuthentication() {
    // Arrange & Act
    final RedisStorageProvider noAuthProvider =
        new RedisStorageProvider(
            false, REDIS_DEFAULT_HOSTNAME, REDIS_DEFAULT_PORT, Optional.empty(), Optional.empty());
    final String name = noAuthProvider.getName();

    // Assert
    assertTrue(name.contains("default"), "Should use default username when none provided");
  }
}
