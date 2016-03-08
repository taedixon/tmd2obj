import com.sun.javafx.geom.Vec3f;

/**
 * Created by Noxid on 08-Mar-16.
 */
public class Material {
    Vec3f ambient;
    Vec3f diffuse;
    Vec3f specular;
    float dissolve;
    String tx_map;
    String ID;

    public Material(short red, short green, short blue, boolean transparent) {
        ambient = new Vec3f((red&0xff)/255f,
                (green&0xff)/255f,
                (blue&0xff)/255f);
        diffuse = new Vec3f((red&0xff)/255f,
                (green&0xff)/255f,
                (blue&0xff)/255f);
        specular = new Vec3f(0f,0f,0f);
        dissolve = transparent ? 0.5f : 1f;
        tx_map = null;
        ID=String.format("%4x.%4x.%4x.%f", red, green, blue, dissolve);
    }

    public Material(short CLUT, short TXB) {
        dissolve = 1f;
        ambient = new Vec3f(1,1,1);
        diffuse = new Vec3f(1,1,1);
        specular = new Vec3f(0,0,0);
        tx_map = String.format("_t%d_t%d.png", CLUT, TXB);
        ID=tx_map;
    }

    public String toString() {
        String result = String.format("Ka %f %f %f\n", ambient.x, ambient.y, ambient.z);
        result += String.format("Kd %f %f %f\n", diffuse.x, diffuse.y, diffuse.z);
        result += String.format("Ks %f %f %f\n", specular.x, specular.y, specular.z);
        result += String.format("d %f\n", dissolve);
        if (tx_map != null) {
            result += String.format("map_Ka %s\n", tx_map);
            result += String.format("map_Kd %s\n", tx_map);
        }
        return  result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Material) {
            Material om = (Material) obj;
            return om.ID.equals(ID);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
