
public class Pixel
{
    public byte[] colors = new byte[3];

    public Pixel(byte blue, byte green, byte red)
    {
        colors[0] = blue;
        colors[1] = green;
        colors[2] = red;
    }
}