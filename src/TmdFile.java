import java.io.*;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by Noxid on 08-Mar-16.
 */
public class TmdFile {
    int id; //always 0x41
    int flags; //Indicates when addresses are relative or explicit
    int nObj;
    int headerSize = 12;

    LinkedList<Model> objList = new LinkedList<Model>();

    TmdFile() {}

    TmdFile(ByteBuffer data) {
        readHeader(data);
        readModels(data);
    }

    void readHeader(ByteBuffer data) {
        id = data.getInt();
        flags = data.getInt();
        nObj = data.getInt();
    }

    void readModels(ByteBuffer data) {
        for (int i = 0; i < nObj; i++) {
            Model m = new Model(data, flags, headerSize);
            m.populate(data);
            objList.add(m);
        }

    }

    public void convert(File directory, String name) throws IOException {
        int modelCount = 1;
        for (Model m : objList) {
            String objname = name + "." + modelCount++;
            File outfile = new File(directory + "/" + objname + ".obj");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
            writer.write("# Converted with TMD2OBJ by Noxid\n");
            writer.write("# Converted on:" + Calendar.getInstance().getTime() + "\n");
            writer.write("mtllib " + objname + ".mtl\n");
            writer.write("o " + objname + "\n");
            m.writeVerts(writer);
            m.writeNormals(writer);
            m.writeUVs(writer);
            m.writeFaces(writer, objname);
            writer.close();

            File mtlFile = new File(directory + "/" + objname + ".mtl");
            BufferedWriter mtlWriter = new BufferedWriter(new FileWriter(mtlFile));
            m.writeMaterials(mtlWriter, objname);
            mtlWriter.close();
        }
    }


}
