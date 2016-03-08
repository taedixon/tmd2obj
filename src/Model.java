import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Noxid on 08-Mar-16.
 */
public class Model {
    private ArrayList<Vec3> verts = new ArrayList<>();
    private ArrayList<Vec3> normals = new ArrayList<>();
    private HashMap<Material, ArrayList<Primitive>> primitives = new HashMap<>();

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
            Primitive p = new Primitive(data);
            Material m = p.getMaterial();
            if (!primitives.containsKey(m)) {
                primitives.put(m,new ArrayList<>());
            }
            primitives.get(m).add(p);
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

    public void writeFaces(Writer writer, String mtlbasename) throws IOException {
        int mtlcount = 0;
        writer.write("s off\n");
        for (Material m : primitives.keySet()) {
            writer.write("usemtl " + mtlbasename + "m" + mtlcount++ + "\n");
            for (Primitive p : primitives.get(m)) {
                writer.write(p.getFace() + "\n");
            }

        }
    }

    public void writeMaterials(Writer writer, String mtlbasename) throws IOException{
        int mtlcount = 0;
        for (Material m : primitives.keySet()) {
            writer.write("newmtl " + mtlbasename + "m" + mtlcount++ + "\n");
            writer.write(m.toString());

        }
    }
}
