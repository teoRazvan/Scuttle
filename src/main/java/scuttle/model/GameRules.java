package scuttle.model;

public final class GameRules {

    // Fixed rules
    static final int MAX_DANGER_STREAK = 4;
    static final int SAFE_START_LANES = 4;
    static final int MAX_GROUND_ATTEMPTS = 10;
    static final int COIN_GROUND_CHANCE = 50;
    // Max difficulty from this lane onward
    static final int DIFFICULTY_RAMP_LANES = 300;

    // Easiest ruleset
    static final int CORAL_CHANCE_EASY = 30;

    static final int GROUND_WEIGHT_EASY = 40;
    static final int FISH_WEIGHT_EASY = 30;
    static final int RAFT_WEIGHT_EASY = 20;
    static final int TORPEDO_WEIGHT_EASY = 10;

    static final int MIN_FISH_COUNT_EASY = 2;
    static final int MAX_FISH_COUNT_EASY = 5;
    static final int MIN_FISH_LENGTH_EASY = 1;
    static final int MAX_FISH_LENGTH_EASY = 3;
    // The number of ticks between two steps lower => faster
    static final int MAX_FISH_TICKS_EASY = 30;
    static final int MIN_FISH_TICKS_EASY = 10;

    static final int MIN_RAFT_COUNT_EASY = 3;
    static final int MAX_RAFT_COUNT_EASY = 5;
    static final int MIN_RAFT_LENGTH_EASY = 2;
    static final int MAX_RAFT_LENGTH_EASY = 4;
    static final int MAX_RAFT_TICKS_EASY = 30;
    static final int MIN_RAFT_TICKS_EASY = 15;

    static final int MIN_TORPEDO_IDLE_TICKS_EASY = 180;
    static final int MAX_TORPEDO_IDLE_TICKS_EASY = 360;
    static final int TORPEDO_WARNING_TICKS = 60;
    static final int TORPEDO_FIRING_TICKS = 20;

    // Hardest ruleset
    static final int GROUND_WEIGHT_HARD = 25;
    static final int FISH_WEIGHT_HARD = 35;
    static final int RAFT_WEIGHT_HARD = 25;
    static final int TORPEDO_WEIGHT_HARD = 15;

    static final int CORAL_CHANCE_HARD = 40;

    static final int MIN_FISH_COUNT_HARD = 3;
    static final int MAX_FISH_COUNT_HARD = 6;
    static final int MIN_FISH_LENGTH_HARD = 2;
    static final int MAX_FISH_LENGTH_HARD = 3;
    static final int MAX_FISH_TICKS_HARD = 18;
    static final int MIN_FISH_TICKS_HARD = 6;

    static final int MIN_RAFT_COUNT_HARD = 2;
    static final int MAX_RAFT_COUNT_HARD = 4;
    static final int MIN_RAFT_LENGTH_HARD = 2;
    static final int MAX_RAFT_LENGTH_HARD = 3;
    static final int MAX_RAFT_TICKS_HARD = 22;
    static final int MIN_RAFT_TICKS_HARD = 10;

    static final int MIN_TORPEDO_IDLE_TICKS_HARD = 100;
    static final int MAX_TORPEDO_IDLE_TICKS_HARD = 200;

    private GameRules() {

    }

}
