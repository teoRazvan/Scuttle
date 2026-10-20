package scuttle.model;

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

    public TorpedoLane(int idleTicks, int warningTicks, int firingTicks, int initialDelay) {
        super(LaneType.TORPEDO);
        if (warningTicks < 1 || firingTicks < 1 || firingTicks < 1) {
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
                    break;
                }
                case WARNING -> {
                    phase = Phase.FIRING;
                    phaseTimer = firingTicks;
                    break;
                }
                case FIRING -> {
                    phase = Phase.IDLE;
                    phaseTimer = idleTicks;
                    break;
                }
            }
        }

    }

}
