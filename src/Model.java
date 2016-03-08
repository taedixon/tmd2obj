import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Noxid on 08-Mar-16.
 */
public class Model {
    private ArrayList<Vec3> verts = new ArrayList<Vec3>();
    private ArrayList<Vec3> normals = new ArrayList<Vec3>();
    private ArrayList<Primitive> primitives = new ArrayList<Primitive>();

    int flags;

    int vertAddress;
    int nVert;
    int normalAddress;
    int nNorm;
    int primitiveAddress;
    int nPrimitive;
    int scale; //unused?


    Model(ByteBuffer data, int flags) {
        vertAddress = data.getInt() + 12; // add 12 to account for header
        nVert = data.getInt();
        normalAddress = data.getInt() + 12;
        nNorm = data.getInt();
        primitiveAddress = data.getInt() + 12;
        nPrimitive = data.getInt();
        scale = data.getInt(); //unused?
        this.flags = flags;
    }

    void populate(ByteBuffer data) {
        if (flags != 0) {
            System.err.println("Unrecognized flags: " + flags);
            return;
        }

        data.position(vertAddress);
        for (int i = 0; i < nVert; i++) {
            verts.add(new Vec3(data.getShort(), data.getShort(), data.getShort()));
            data.getShort(); //pad
        }
        data.position(normalAddress);
        for (int i = 0; i < nNorm; i++) {
            normals.add(new Vec3(data.getShort(), data.getShort(), data.getShort()));
            data.getShort(); //pad
        }
        data.position(primitiveAddress);
        for (int i = 0; i < nPrimitive; i++) {
            primitives.add(new Primitive(data));
        }
    }

    public void writeVerts(Writer writer) throws IOException {
        for (Vec3 v : verts) {
            writer.write(String.format("v %f %f %f\n",
                    v.vx*-500f/Short.MAX_VALUE,
                    v.vy*-500f/Short.MAX_VALUE,
                    v.vz*-500f/Short.MAX_VALUE));
        }
    }

    public void writeNormals(Writer writer) throws IOException {
        for (Vec3 v : normals) {
            writer.write(String.format("vn %f %f %f\n",
                    v.vx*-500f/Short.MAX_VALUE,
                    v.vz*-500f/Short.MAX_VALUE,
                    v.vy*-500f/Short.MAX_VALUE));
        }
    }

    public void writeFaces(Writer writer) throws IOException {
        for (Primitive p : primitives) {
            writer.write(p.getFace() + "\n");
        }
    }
}
