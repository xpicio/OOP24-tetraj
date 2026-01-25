package it.unibo.tetraj.model.leaderboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;

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
  private static final String REDIS_PING_RESPONSE = "PONG";
  private static final int ADDITIONAL_ENTRIES = 5;
  private RedisStorageProvider provider;
  private RedisStorageProvider mockProvider;

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

    final JedisPooled mockJedis = mock(JedisPooled.class);
    final AtomicReference<String> storedData = new AtomicReference<>(null);
    when(mockJedis.ping()).thenReturn(REDIS_PING_RESPONSE);
    when(mockJedis.get(anyString())).thenAnswer(invocation -> storedData.get());
    doAnswer(
            invocation -> {
              storedData.set(invocation.getArgument(1));
              return null;
            })
        .when(mockJedis)
        .set(eq("tetraj:leaderboard"), anyString());
    mockProvider =
        new RedisStorageProvider(
            false,
            REDIS_DEFAULT_HOSTNAME,
            REDIS_DEFAULT_PORT,
            Optional.empty(),
            Optional.empty(),
            mockJedis);
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
    // Arrange
    final RedisStorageProvider authProvider =
        new RedisStorageProvider(
            false,
            REDIS_DEFAULT_HOSTNAME,
            REDIS_DEFAULT_PORT,
            Optional.of("testuser"),
            Optional.of("testpass"));

    // Act
    final String name = authProvider.getName();

    // Assert
    assertTrue(name.contains("testuser"), "Username should be in connection string");
  }

  @Test
  @DisplayName("should create provider without authentication")
  void shouldCreateProviderWithoutAuthentication() {
    // Arrange
    final RedisStorageProvider noAuthProvider =
        new RedisStorageProvider(
            false, REDIS_DEFAULT_HOSTNAME, REDIS_DEFAULT_PORT, Optional.empty(), Optional.empty());

    // Act
    final String name = noAuthProvider.getName();

    // Assert
    assertTrue(name.contains("default"), "Should use default username when none provided");
  }

  @Test
  @DisplayName("should be available after successful ping")
  void shouldBeAvailableAfterSuccessfulPing() {
    // Act
    mockProvider.initialize();

    // Assert
    assertTrue(mockProvider.isAvailable(), "Provider should be available after successful ping");
  }

  @Test
  @DisplayName("should save and retrieve multiple entries sorted by score")
  void shouldSaveAndRetrieveMultipleEntriesSortedByScore() {
    // Arrange
    final LeaderboardEntry lowScoreEntry =
        new LeaderboardEntry(
            "player1",
            "LowScorer",
            500,
            Instant.now(),
            LEADERBOARD_ENTRY_P1_LEVEL,
            LEADERBOARD_ENTRY_P1_LINES,
            Duration.ofMinutes(LEADERBOARD_ENTRY_P1_DURATION));
    final LeaderboardEntry highScoreEntry =
        new LeaderboardEntry(
            "player2",
            "HighScorer",
            2000,
            Instant.now(),
            LEADERBOARD_ENTRY_P1_LEVEL,
            LEADERBOARD_ENTRY_P1_LINES,
            Duration.ofMinutes(LEADERBOARD_ENTRY_P1_DURATION));

    mockProvider.initialize();

    // Act
    mockProvider.save(lowScoreEntry);
    mockProvider.save(highScoreEntry);

    final List<LeaderboardEntry> entries = mockProvider.getTop();

    // Assert
    assertEquals(2, entries.size(), "Should have two entries");
    assertEquals(
        highScoreEntry.nickname(),
        entries.getFirst().nickname(),
        "player2 should be first (highest score)");
    assertEquals(
        lowScoreEntry.nickname(), entries.getLast().nickname(), "player1 should be second");
  }

  @Test
  @DisplayName("should limit entries to MAX_ENTRIES")
  void shouldLimitEntriesToMaxEntries() {
    // Arrange
    mockProvider.initialize();

    // Act - add more than MAX_ENTRIES entries
    for (int i = 0; i < StorageProvider.MAX_ENTRIES + ADDITIONAL_ENTRIES; i++) {
      final LeaderboardEntry entry =
          new LeaderboardEntry(
              "player" + i,
              "Player" + i,
              (long) i * 100,
              Instant.now(),
              i,
              i * 2,
              Duration.ofMinutes(i));
      mockProvider.save(entry);
    }

    final List<LeaderboardEntry> entries = mockProvider.getTop();

    // Assert
    assertEquals(
        StorageProvider.MAX_ENTRIES, entries.size(), "Should only keep MAX_ENTRIES top scores");
  }
}
