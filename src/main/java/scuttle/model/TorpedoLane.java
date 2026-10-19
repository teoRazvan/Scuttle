package scuttle.model;

public class TorpedoLane extends Lane {

    public static final int LAUNCHER_COLUMN = 0;
    private final int idleTicks;
    private final int warningTicks;
    private final int firingTicks;
    private int phaseTimer;
    private Phase phase;

    private enum Phase {
        Idle,
        Warning,
        Firing;
    }

    public TorpedoLane(int idleTicks, int warningTicks, int firingTicks, int initialDelay) {
        super(LaneType.TORPEDO);
        this.phase = Phase.Idle;
        this.idleTicks = idleTicks;
        this.firingTicks = firingTicks;
        this.warningTicks = warningTicks;
        this.phaseTimer = initialDelay;
    }

    public boolean isWarning() {
        return phase == Phase.Warning;
    }

    public boolean isFiring() {
        return phase == Phase.Firing;
    }

    @Override

    public boolean canStepOn(int column) {
        if (column == LAUNCHER_COLUMN) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isDeadly(int column) {
        if (phase == Phase.Firing) {
            return true;
        }
        return false;
    }

    @Override
    public void update(int tick) {
        phaseTimer--;
        if (phaseTimer == 0) {
            switch (phase) {
                case Idle:
                    phase = Phase.Warning;
                    phaseTimer = warningTicks;
                    break;
                case Warning:
                    phase = Phase.Firing;
                    phaseTimer = firingTicks;
                    break;
                case Firing:
                    phase = Phase.Idle;
                    phaseTimer = idleTicks;
                    break;
            }
        }

    }

}
