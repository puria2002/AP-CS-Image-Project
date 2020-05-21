
public class BinaryTools {
    public static int toUnsignedInt(byte b)
    {
        if (b<0)
        {
            return b + 256;
        }
        return b;
    }

}