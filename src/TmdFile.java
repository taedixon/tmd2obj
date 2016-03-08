import java.io.*;
import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Created by Noxid on 08-Mar-16.
 */
public class TmdFile {
    int id; //always 0x41
    int flags; //Indicates when addresses are relative or explicit
    int nObj;

    LinkedList<Model> objList = new LinkedList<Model>();

    TmdFile(ByteBuffer data) {
        id = data.getInt();
        flags = data.getInt();
        nObj = data.getInt();

        for (int i = 0; i < nObj; i++) {
            objList.add(new Model(data, flags));
        }
        for (Model m : objList) {
            m.populate(data);
        }
    }

    public void convert(File directory, String name) throws IOException {
        int modelCount = 1;
        for (Model m : objList) {
            File outfile = new File(directory + "/" + name + "." + modelCount++ + ".obj");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outfile));
            m.writeVerts(writer);
            m.writeNormals(writer);
            m.writeFaces(writer);
            writer.close();
        }
    }


}
