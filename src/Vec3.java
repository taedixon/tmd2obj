/**
 * Created by Noxid on 08-Mar-16.
 */
public class Vec3 {
    short vx, vy, vz;
    Vec3(short x, short y, short z) {
        vx = x;
        vy = y;
        vz = z;
    }
    Vec3 add(Vec3 other) {
        return new Vec3(
                // explicit downcasting because operator + promotes shorts to ints
                (short)(vx + other.vx),
                (short)(vy + other.vy),
                (short)(vz + other.vz));
    }
}
