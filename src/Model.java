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

    int filesize;

    Model(ByteBuffer data, int flags, int headerOffset) {
        vertAddress = data.getInt() + headerOffset; // add 12 to account for header
        nVert = data.getInt();
        normalAddress = data.getInt() + headerOffset;
        nNorm = data.getInt();
        primitiveAddress = data.getInt() + headerOffset;
        nPrimitive = data.getInt();
        scale = data.getInt(); //unused?
        this.flags = flags;
    }

    // for bubsy poses
    public Model(Model other, ArrayList<Vec3> verts, ArrayList<Vec3> normals) {
        this.verts = verts;
        nVert = verts.size();
        this.normals = normals;
        nNorm = normals.size();
        primitives = other.primitives;
    }

    private void applyDelta(ArrayList<Vec3> output,
                            ArrayList<Vec3> points,
                            ArrayList<Vec3> delta) {
        for (int i = 0; i < points.size(); i++) {
            // remembering that Java is copy-by-ref for all nonprimitive types
            output.add(points.get(i).add(delta.get(i)));
        }
    }

    void populate(ByteBuffer data) {
        if (flags != 0) {
            System.err.println("Unrecognized flags: " + flags);
            return;
        }
        filesize = 28; // header size

        data.position(vertAddress);
        System.out.printf("Begin vertex data 0x%X\n", vertAddress);
        for (int i = 0; i < nVert; i++) {
            verts.add(new Vec3(data.getShort(), data.getShort(), data.getShort()));
            data.getShort(); //pad
            filesize += 8;
        }
        data.position(normalAddress);
        System.out.printf("Begin normal data 0x%X\n", normalAddress);
        for (int i = 0; i < nNorm; i++) {
            normals.add(new Vec3(data.getShort(), data.getShort(), data.getShort()));
            data.getShort(); //pad
            filesize += 8;
        }
        data.position(primitiveAddress);
        System.out.printf("Begin face data 0x%X\n", primitiveAddress);
        for (int i = 0; i < nPrimitive; i++) {
            Primitive p = new Primitive(data);
            Material m = p.getMaterial();
            if (!primitives.containsKey(m)) {
                primitives.put(m,new ArrayList<>());
            }
            primitives.get(m).add(p);
            filesize += p.filesize;
        }
        System.out.printf("Finished reading object 0x%X\n", data.position());
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
                    v.vy*-500f/Short.MAX_VALUE,
                    v.vz*-500f/Short.MAX_VALUE));
        }
    }

    public void writeUVs(Writer writer) throws  IOException {
        int vt_index = 1;
        for (ArrayList<Primitive> plist : primitives.values()) {
            for (Primitive p : plist) {
                int num_coords = p.numTexcoords();
                if (num_coords > 0) {
                    p.vt_index = vt_index;
                    writer.write(p.getTexcoords());
                    vt_index += num_coords;
                }
            }
        }
    }

    public void writeFaces(Writer writer, String mtlbasename) throws IOException {
        int mtlcount = 0;
        writer.write("s off\n");
        for (Material m : primitives.keySet()) {
            writer.write("usemtl " + mtlbasename + "m" + mtlcount++ + "\n");
            for (Primitive p : primitives.get(m)) {
                writer.write(p.getFace());
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
