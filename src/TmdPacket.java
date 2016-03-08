import java.nio.ByteBuffer;
import java.util.InputMismatchException;

/**
 * Created by Noxid on 08-Mar-16.
 */
public abstract class TmdPacket {
    short normal1;
    short vert1, vert2, vert3;

    public static TmdPacket build(ByteBuffer data, byte flag, byte mode, byte ilen) {
        int flagmode = mode + (flag<<8);
        switch (flagmode){
            case 0x0020:
                return new FFPacket(data);
            case 0x0030:
                return new GFPacket(data);
            case 0x0024:
                return new FTPacket(data);
            case 0x0121:
                return new NFPacket(data);
            case 0x0034:
                return new GTPacket(data);
            case 0x0022:
                return new TFFPacket(data);
            case 0x0032:
                return new TGFPacket(data);
        }
        System.err.println(String.format("Unrecognized flag: 0x%x (%d) @0x%X", flagmode, ilen, data.position()));
        throw new InputMismatchException("Fucked Up format");
        //data.get(new byte[ilen*4]);
        //return null;
    }

    public String getFace() {
        return String.format("f %d//%d %d//%d %d//%d",
                vert1+1, normal1+1,
                vert2+1, normal1+1,
                vert3+1, normal1+1);
    }

    public abstract Material getMaterial();

    /**
     * Flat Shading Flat Pigment
     */
    static class FFPacket extends TmdPacket {
        byte red, green, blue;
        byte mode; //0x20

        public FFPacket(ByteBuffer data) {
            red = data.get();
            green = data.get();
            blue = data.get();
            mode = data.get();
            normal1 = data.getShort();
            vert1 = data.getShort();
            vert2 = data.getShort();
            vert3 = data.getShort();
        }

        @Override
        public Material getMaterial() {
            return new Material(red, green, blue, false);
        }
    }

    /**
     * Gourad Shading Flat Pigment
     */
    static class GFPacket extends TmdPacket {
        byte red, green, blue;
        byte mode; //0x30
        short normal2, normal3;

        public GFPacket(ByteBuffer data) {
            red = data.get();
            green = data.get();
            blue = data.get();
            mode = data.get();
            normal1 = data.getShort();
            vert1 = data.getShort();
            normal2 = data.getShort();
            vert2 = data.getShort();
            normal3 = data.getShort();
            vert3 = data.getShort();
        }
        @Override
        public Material getMaterial() {
            return new Material(red, green, blue, false);
        }
    }

    /**
     * Flat Shading Gradient Pigment
     */
    class FGPacket {
        byte red1, green1, blue1;
        byte red2, green2, blue2;
        byte red3, green3, blue3;
        byte mode; //0x20
        short normal;
        short vert1, vert2, vert3;
    }

    /**
     * Gourad Shading Gradient Pigment
     */
    class GGPacket {
        byte mode; //0x30
        byte red1, green1, blue1;
        byte red2, green2, blue2;
        byte red3, green3, blue3;
        short normal1, normal2, normal3;
        short vert1, vert2, vert3;
    }

    /**
     * Textured Flat Shaded
     */
    static class FTPacket extends TmdPacket {
        byte u1, v1;
        byte u2, v2;
        byte u3, v3;
        short CBA; //position of CLUT for texture in VRAM
        short TSB; //information about texture in VRAM

        public FTPacket(ByteBuffer data) {
            u1 = data.get();
            v1 = data.get();
            CBA = data.getShort();
            u2 = data.get();
            v2 = data.get();
            TSB = data.getShort();
            u3 = data.get();
            v3 = data.get();
            data.getShort(); //padding
            normal1 = data.getShort();
            vert1 = data.getShort();
            vert2 = data.getShort();
            vert3 = data.getShort();
        }
        @Override
        public Material getMaterial() {
            return new Material(CBA, TSB);
        }
    }

    /**
     * Textured Gourad Shaded
     */
    static class GTPacket extends TmdPacket {
        byte u1, v1;
        byte u2, v2;
        byte u3, v3;
        short CBA; //position of CLUT for texture in VRAM
        short TSB; //information about texture in VRAM
        short normal2, normal3;

        public GTPacket(ByteBuffer data) {
            u1 = data.get();
            v1 = data.get();
            CBA = data.getShort();
            u2 = data.get();
            v2 = data.get();
            TSB = data.getShort();
            u3 = data.get();
            v3 = data.get();
            data.getShort(); //padding
            normal1 = data.getShort();
            vert1 = data.getShort();
            normal2 = data.getShort();
            vert2 = data.getShort();
            normal3 = data.getShort();
            vert3 = data.getShort();
        }

        @Override
        public Material getMaterial() {
            return new Material(CBA, TSB);
        }
    }

    /**
     * No Shading Flat Pigment
     */
    static class NFPacket extends TmdPacket {
        byte red, green, blue;
        byte mode; //0x21

        NFPacket(ByteBuffer data) {
            red = data.get();
            green = data.get();
            blue = data.get();
            mode = data.get();
            vert1 = data.getShort();
            vert2 = data.getShort();
            vert3 = data.getShort();
            data.getShort();
        }
        @Override
        public Material getMaterial() {
            return new Material(red, green, blue, false);
        }
    }
    /**
     * Translucent Flat Shaded Flat Coloured
     */
    static class TFFPacket extends TmdPacket {
        byte red, green, blue;
        byte mode; //0x22

        public TFFPacket(ByteBuffer data) {
            red = data.get();
            green = data.get();
            blue = data.get();
            mode = data.get();
            normal1 = data.getShort();
            vert1 = data.getShort();
            vert2 = data.getShort();
            vert3 = data.getShort();
        }
        @Override
        public Material getMaterial() {
            return new Material(red, green, blue, true);
        }
    }


    /**
     * Translucent Gourad Shading Flat Pigment
     */
    static class TGFPacket extends TmdPacket {
        byte red, green, blue;
        byte mode; //0x30
        short normal2, normal3;

        public TGFPacket(ByteBuffer data) {
            red = data.get();
            green = data.get();
            blue = data.get();
            mode = data.get();
            normal1 = data.getShort();
            vert1 = data.getShort();
            normal2 = data.getShort();
            vert2 = data.getShort();
            normal3 = data.getShort();
            vert3 = data.getShort();
        }
        @Override
        public Material getMaterial() {
            return new Material(red, green, blue, true);
        }
    }
}
