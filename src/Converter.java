import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Objects;

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

        String extension = tmdfile.getName().substring(tmdfile.getName().lastIndexOf('.'));

        TmdFile tmd;
        // yea this is clunky
        if (extension.toLowerCase().equals(".tzp")) {
            tmd = new TzpFile(dataBuf);
        } else {
            tmd = new TmdFile(dataBuf);
        }
        tmd.convert(new File(tmdfile.getParent()),
                tmdfile.getName().replace(extension,""));
    }


    public static void main(String[] args) {
        Converter c = new Converter();
        String current = null;
        try {
            if (args.length > 0){
                for (String s: args) {
                    if (s.toLowerCase().equals("-h")) {
                        System.out.println("tmd2obj : Convert a PSX tmd file to a Wavefront obj/mlt.\n" +
                                            "Usage:\n\t" +
                                            "tmd2obj.jar file1.tmd file2.tmd ...\n" +
                                            "The resulting file(s) will be in the same folder as the original.\n" +
                                            "If no argument is provided, the program will look for tmd files in the 'models' folder.");
                        break;
                    }
                    File f = new File(s);
                    c.convert(f);
                }
            } else {
                File rootdir = new File("./models");
                for (File f : Objects.requireNonNull(rootdir.listFiles())) {
                    current = f.getName().toLowerCase();
                    if (current.endsWith(".tmd") || current.endsWith(".tzp")) {
                        c.convert(f);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("File not found ! Make sure to run this program in the same directory as the models one.");
        }
    }
}
