import java.nio.ByteBuffer;

/**
 * Created by Noxid on 08-Mar-16.
 */
public class Primitive {
    byte olen;
    byte ilen;
    byte flag;
    byte mode;

    TmdPacket packet;
    // what index are our texture coordinates in the obj
    int vt_index = -1;

    Primitive(ByteBuffer data) {
        olen = data.get();
        ilen = data.get();
        flag = data.get();
        mode = data.get();

        packet = TmdPacket.build(data, flag, mode, ilen);
    }

    public String getFace() {
        return packet.getFace(vt_index);
    }

    public String getTexcoords() {
        StringBuilder sb = new StringBuilder();
        byte[] coords = packet.getTexcoords();
        for (int i = 0; (i+1) < coords.length; i += 2) {
            // TMD texture coordinates seem to be pixel coordinates for
            // the texture that'll be applied. The model doesn't really
            // have information about the texture size, so I'm just going to
            // assume 64x64 and you'll need to fix it in blender sorry.
            sb.append(String.format("vt %f %f\n",
                    (coords[i] / 64.f),
                    // flip the vertical axis only
                    1. - (coords[i+1] / 64.f)));
        };
        return sb.toString();
    }

    public int numTexcoords() {
        return packet.getTexcoords().length / 2;
    }

    public Material getMaterial() {
        return packet.getMaterial();
    }
}
