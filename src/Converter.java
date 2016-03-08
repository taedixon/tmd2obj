import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

/**
 * Created by Noxid on 07-Mar-16.
 */
public class Converter {

    public void convert(File tmdfile) throws IOException {
        FileChannel tmdChannel = new FileInputStream(tmdfile).getChannel();
        ByteBuffer dataBuf = ByteBuffer.allocate((int) tmdChannel.size());
        tmdChannel.read(dataBuf);
        tmdChannel.close();

        dataBuf.flip();
        dataBuf.order(ByteOrder.LITTLE_ENDIAN);

        TmdFile tmd = new TmdFile(dataBuf);
        tmd.convert(new File(tmdfile.getParent()),
                tmdfile.getName().replace(".tmd",""));
    }


    public static void main(String[] args) {
        Converter c = new Converter();
        File current = null;
        try {
            File rootdir = new File("./models");
            for (File f : rootdir.listFiles()) {
                current = f;
                if (f.getName().endsWith(".tmd")) {
                    c.convert(f);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error in " + current);
            e.printStackTrace();
        }
    }
}
