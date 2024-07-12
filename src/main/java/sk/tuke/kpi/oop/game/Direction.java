package sk.tuke.kpi.oop.game;


public enum Direction {
    NORTH(0, 1, 0),
    EAST(1, 0, 270),
    SOUTH(0, -1, 180),
    WEST(-1, 0, 90),
    NONE(0, 0, 0),
    NORTHEAST(1, 1, 315),
    SOUTHEAST(1, -1, 225),
    NORTHWEST(-1, 1, 45),
    SOUTHWEST(-1, -1, 135);


    private final int dx;
    private final int dy;
    private final int angle;

    private Direction(int dx, int dy, int angle) {
        this.dx = dx;
        this.dy = dy;
        this.angle = angle;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public float getAngle() {
        return angle;
    }

    public Direction combine(Direction other) {
        int dx = Math.min(Math.max(this.dx + other.dx, -1), 1);
        int dy = Math.min(Math.max(this.dy + other.dy, -1), 1);

        for (Direction direction : Direction.values()) {
            if (direction.dx == dx && direction.dy == dy) {
                return direction;
            }
        }
        return Direction.NONE;
    }

    public static Direction fromAngle(float angle) {
        float normalized_angle = (angle % 360 + 360) % 360;

        if (normalized_angle >= 337.5 || normalized_angle < 22.5) {
            return NORTH;
        }
        if (normalized_angle < 67.5) {
            return NORTHWEST;
        }
        if (normalized_angle < 112.5) {
            return WEST;
        }
        if (normalized_angle < 157.5) {
            return SOUTHWEST;
        }
        if (normalized_angle < 202.5) {
            return SOUTH;
        }
        if (normalized_angle < 247.5) {
            return SOUTHEAST;
        }
        if (normalized_angle < 292.5) {
            return EAST;
        }
        return NORTHEAST;
    }
}
