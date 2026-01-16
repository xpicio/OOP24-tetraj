package it.unibo.tetraj.model.leaderboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Random;
import java.util.UUID;

/**
 * Player profile with unique ID and randomly generated nickname. Persisted to user home directory
 * as JSON.
 *
 * @param id Unique player identifier (UUID)
 * @param nickname Player's display name
 */
public record PlayerProfile(
    @JsonProperty("id") String id, @JsonProperty("nickname") String nickname) {

  private static final Random RANDOM = new Random();
  private static final String[] ADJECTIVES = {
    "Swift", "Brave", "Clever", "Happy", "Lucky", "Mighty",
    "Quick", "Silent", "Fierce", "Gentle", "Bold", "Wise",
    "Agile", "Cosmic", "Electric", "Mystic", "Noble", "Rapid",
    "Stealth", "Turbo", "Ultra", "Vivid", "Wild", "Epic",
    "Mega", "Super", "Hyper", "Cyber", "Neon", "Pixel",
  };
  private static final String[] ANIMALS = {
    "Panda", "Eagle", "Fox", "Tiger", "Wolf", "Bear",
    "Hawk", "Shark", "Dragon", "Phoenix", "Falcon", "Lion",
    "Cobra", "Viper", "Raven", "Owl", "Cat", "Dog",
    "Monkey", "Rabbit", "Turtle", "Dolphin", "Octopus", "Squid",
    "Mantis", "Spider", "Scorpion", "Rhino", "Hippo", "Giraffe",
  };

  /**
   * Creates a new player profile with generated UUID and random nickname.
   *
   * @return A new PlayerProfile with unique ID and random nickname
   */
  public static PlayerProfile generateNew() {
    final String id = UUID.randomUUID().toString();
    final String nickname = generateRandomNickname();

    return new PlayerProfile(id, nickname);
  }

  /**
   * Generates a random nickname from adjective + animal combination.
   *
   * @return A nickname like "SwiftPanda" or "BraveEagle"
   */
  private static String generateRandomNickname() {
    final String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
    final String animal = ANIMALS[RANDOM.nextInt(ANIMALS.length)];

    return adjective + animal;
  }
}
