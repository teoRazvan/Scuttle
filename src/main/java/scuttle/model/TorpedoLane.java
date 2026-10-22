package scuttle.model;

/**
 * A launcher on the left fires a water jet across the whole lane. It cycles IDLE → WARNING → FIRING
 * → IDLE, and the whole lane is deadly while firing. The launcher cell is solid.
 */
public class TorpedoLane extends Lane {

    // The Launcher sits in this column and blocks movement
    public static final int LAUNCHER_COLUMN = 0;

    private final int idleTicks;
    private final int warningTicks;
    private final int firingTicks;
    private int phaseTimer;
    private Phase phase;

    private enum Phase {
        IDLE,
        WARNING,
        FIRING;
    }

    /**
     * @param idleTicks how long idle phase lasts,
     * @param warningTicks how long warning phase lasts,
     * @param firingTicks how long firing phase lasts,
     * @param initialDelay ticks before the first warning, random so the lanes don’t fire in sync.
     * @throws IllegalArgumentException if any of them is below 1.
     */
    public TorpedoLane(int idleTicks, int warningTicks, int firingTicks, int initialDelay) {
        super(LaneType.TORPEDO);
        if (warningTicks < 1 || firingTicks < 1 || idleTicks < 1) {
            throw new IllegalArgumentException("Tick Counts must be at least 1");
        }
        if (initialDelay < 1) {
            throw new IllegalArgumentException("Initial delay must be at least 1");
        }
        this.phase = Phase.IDLE;
        this.idleTicks = idleTicks;
        this.firingTicks = firingTicks;
        this.warningTicks = warningTicks;
        this.phaseTimer = initialDelay;
    }

    public boolean isWarning() {
        return phase == Phase.WARNING;
    }

    public boolean isFiring() {
        return phase == Phase.FIRING;
    }

    @Override
    public boolean canStepOn(int column) {
        return column != LAUNCHER_COLUMN;
    }

    @Override
    public boolean isDeadly(int column) {
        return phase == Phase.FIRING;
    }

    @Override
    public void update(int tick) {
        phaseTimer--;
        if (phaseTimer == 0) {
            switch (phase) {
                case IDLE -> {
                    phase = Phase.WARNING;
                    phaseTimer = warningTicks;
                }
                case WARNING -> {
                    phase = Phase.FIRING;
                    phaseTimer = firingTicks;
                }
                case FIRING -> {
                    phase = Phase.IDLE;
                    phaseTimer = idleTicks;
                }
            }
        }

    }

}
