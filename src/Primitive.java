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

    Primitive(ByteBuffer data) {
        olen = data.get();
        ilen = data.get();
        flag = data.get();
        mode = data.get();

        packet = TmdPacket.build(data, flag, mode, ilen);
    }

    public String getFace() {
        return packet.getFace();
    }
}
