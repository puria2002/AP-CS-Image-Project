
public class Image  
{   
    private Pixel[][] image;
    private int height;
    private int width;

    public Image(Pixel[][] image)
    {
        this.image = image;
        height = image.length;
        width = image[0].length;
    }

    public void reflect()
    {   
        Pixel temp;
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j <= (width-2)/2; j++)
            {   
                //implement a swap (lack of pointers makes this complicated) to provide reflection
                temp = image[i][j];
                image[i][j] = image[i][width-j-1];
                image[i][width-j-1] = temp;
            }
        }

    }


    public void toGrayscale()
    {
        int pixelsum;
        //for each pixel, take the average of the RGB value and make them equal
        //thus making grayscale proportional to brightness
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                pixelsum = BinaryTools.toUnsignedInt(image[i][j].colors[0]) + BinaryTools.toUnsignedInt(image[i][j].colors[1]) + BinaryTools.toUnsignedInt(image[i][j].colors[2]);
                pixelsum = (int) Math.round((double) pixelsum/ 3);
                image[i][j].colors[0] = (byte) pixelsum;
                image[i][j].colors[1] = (byte) pixelsum;
                image[i][j].colors[2] = (byte) pixelsum;
            }
        }
    }

    public void blur()
    {   //initalize array to be used
        Pixel[][] image2 = new Pixel[height][width];
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                image2[i][j] = new Pixel((byte) 0,(byte) 0,(byte) 0);
            }
        }
        int[] intensity = new int[4];
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width ; j++)
            {   //check every pixel for possible neighboring pixels
                //if one exists, add it to the running sum
                //average the neighbors for a blur effect
                intensity[0] = 0;
                intensity[1] = 0;
                intensity[2] = 0;
                intensity[3] = 0;

                if (i-1 >= 0 && j-1 >= 0)
                {
                    intensity[0] += BinaryTools.toUnsignedInt(image[i-1][j-1].colors[0]);
                    intensity[1] += BinaryTools.toUnsignedInt(image[i-1][j-1].colors[1]);
                    intensity[2] += BinaryTools.toUnsignedInt(image[i-1][j-1].colors[2]);
                    intensity[3]++;
                }

                if (i-1 >= 0)
                {
                    intensity[0] += BinaryTools.toUnsignedInt(image[i-1][j].colors[0]);
                    intensity[1] += BinaryTools.toUnsignedInt(image[i-1][j].colors[1]);
                    intensity[2] += BinaryTools.toUnsignedInt(image[i-1][j].colors[2]);
                    intensity[3]++;
                }

                if (i-1 >= 0 && j+1 < width)
                {
                    intensity[0] += BinaryTools.toUnsignedInt(image[i-1][j+1].colors[0]);
                    intensity[1] += BinaryTools.toUnsignedInt(image[i-1][j+1].colors[1]);
                    intensity[2] += BinaryTools.toUnsignedInt(image[i-1][j+1].colors[2]);
                    intensity[3]++;
                }

                if (j-1 >= 0)
                {
                    intensity[0] += BinaryTools.toUnsignedInt(image[i][j-1].colors[0]);
                    intensity[1] += BinaryTools.toUnsignedInt(image[i][j-1].colors[1]);
                    intensity[2] += BinaryTools.toUnsignedInt(image[i][j-1].colors[2]);
                    intensity[3]++;
                }

                if (true)
                {
                    intensity[0] += BinaryTools.toUnsignedInt(image[i][j].colors[0]);
                    intensity[1] += BinaryTools.toUnsignedInt(image[i][j].colors[1]);
                    intensity[2] += BinaryTools.toUnsignedInt(image[i][j].colors[2]);
                    intensity[3]++;
                }
                
                if (j+1 < width)
                {
                    intensity[0] += BinaryTools.toUnsignedInt(image[i][j+1].colors[0]);
                    intensity[1] += BinaryTools.toUnsignedInt(image[i][j+1].colors[1]);
                    intensity[2] += BinaryTools.toUnsignedInt(image[i][j+1].colors[2]);
                    intensity[3]++;
                }

                if (i+1 < height && j-1 >= 0)
                {
                    intensity[0] += BinaryTools.toUnsignedInt(image[i+1][j-1].colors[0]);
                    intensity[1] += BinaryTools.toUnsignedInt(image[i+1][j-1].colors[1]);
                    intensity[2] += BinaryTools.toUnsignedInt(image[i+1][j-1].colors[2]);
                    intensity[3]++;
                }

                if (i+1 < height)
                {
                    intensity[0] += BinaryTools.toUnsignedInt(image[i+1][j].colors[0]);
                    intensity[1] += BinaryTools.toUnsignedInt(image[i+1][j].colors[1]);
                    intensity[2] += BinaryTools.toUnsignedInt(image[i+1][j].colors[2]);
                    intensity[3]++;
                }

                if (i+1 < height && j+1 < width)
                {
                    intensity[0] += BinaryTools.toUnsignedInt(image[i+1][j+1].colors[0]);
                    intensity[1] += BinaryTools.toUnsignedInt(image[i+1][j+1].colors[1]);
                    intensity[2] += BinaryTools.toUnsignedInt(image[i+1][j+1].colors[2]);
                    intensity[3]++;
            }

            image2[i][j].colors[0] = (byte) Math.round( (double) intensity[0] / intensity[3]);
            image2[i][j].colors[1] = (byte) Math.round( (double) intensity[1] / intensity[3]);
            image2[i][j].colors[2] = (byte) Math.round( (double) intensity[2] / intensity[3]);
        }
    }
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                image[i][j] = image2[i][j];
            }
        }   
    }
    
    public void edgeDetect()
    {
        //initalize array to be used
        Pixel[][] image2 = new Pixel[height][width];

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                image2[i][j] = new Pixel((byte) 0,(byte) 0,(byte) 0);
            }
        }

        //gradient array for color differences: 0-2 in x; 3-5 in y
        int[] grads = new int[6]; 

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width ; j++)
            {   //calculate color gradient for each color in each
                //direction and normalize in accordance with Schor's algorithm
                grads[0] = 0;
                grads[1] = 0;
                grads[2] = 0;
                grads[3] = 0;
                grads[4] = 0;
                grads[5] = 0;

                if (i-1 >= 0 && j-1 >= 0)
                {
                    grads[0] += -BinaryTools.toUnsignedInt(image[i-1][j-1].colors[0]);
                    grads[1] += -BinaryTools.toUnsignedInt(image[i-1][j-1].colors[1]);
                    grads[2] += -BinaryTools.toUnsignedInt(image[i-1][j-1].colors[2]);
                    grads[3] += -BinaryTools.toUnsignedInt(image[i-1][j-1].colors[0]);
                    grads[4] += -BinaryTools.toUnsignedInt(image[i-1][j-1].colors[1]);
                    grads[5] += -BinaryTools.toUnsignedInt(image[i-1][j-1].colors[2]);
                }

                if (i-1 >= 0)
                {
                    grads[3] += -2 * BinaryTools.toUnsignedInt(image[i-1][j].colors[0]);
                    grads[4] += -2 * BinaryTools.toUnsignedInt(image[i-1][j].colors[1]);
                    grads[5] += -2 * BinaryTools.toUnsignedInt(image[i-1][j].colors[2]);
                }

                if (i-1 >= 0 && j+1 < width)
                {
                    grads[0] += BinaryTools.toUnsignedInt(image[i-1][j+1].colors[0]);
                    grads[1] += BinaryTools.toUnsignedInt(image[i-1][j+1].colors[1]);
                    grads[2] += BinaryTools.toUnsignedInt(image[i-1][j+1].colors[2]);
                    grads[3] += -BinaryTools.toUnsignedInt(image[i-1][j+1].colors[0]);
                    grads[4] += -BinaryTools.toUnsignedInt(image[i-1][j+1].colors[1]);
                    grads[5] += -BinaryTools.toUnsignedInt(image[i-1][j+1].colors[2]);
                }

                if (j-1 >= 0)
                {
                    grads[0] += -2 * BinaryTools.toUnsignedInt(image[i][j-1].colors[0]);
                    grads[1] += -2 * BinaryTools.toUnsignedInt(image[i][j-1].colors[1]);
                    grads[2] += -2 * BinaryTools.toUnsignedInt(image[i][j-1].colors[2]);
                }

                if (j+1 < width)
                {
                    grads[0] += 2 * BinaryTools.toUnsignedInt(image[i][j+1].colors[0]);
                    grads[1] += 2 * BinaryTools.toUnsignedInt(image[i][j+1].colors[1]);
                    grads[2] += 2 * BinaryTools.toUnsignedInt(image[i][j+1].colors[2]);
                }

                if (i+1 < height && j-1 >= 0)
                {
                    grads[0] += -BinaryTools.toUnsignedInt(image[i+1][j-1].colors[0]);
                    grads[1] += -BinaryTools.toUnsignedInt(image[i+1][j-1].colors[1]);
                    grads[2] += -BinaryTools.toUnsignedInt(image[i+1][j-1].colors[2]);
                    grads[3] += BinaryTools.toUnsignedInt(image[i+1][j-1].colors[0]);
                    grads[4] += BinaryTools.toUnsignedInt(image[i+1][j-1].colors[1]);
                    grads[5] += BinaryTools.toUnsignedInt(image[i+1][j-1].colors[2]);
                }

                if (i+1 < height)
                {
                    grads[3] += 2 * BinaryTools.toUnsignedInt(image[i+1][j].colors[0]);
                    grads[4] += 2 * BinaryTools.toUnsignedInt(image[i+1][j].colors[1]);
                    grads[5] += 2 * BinaryTools.toUnsignedInt(image[i+1][j].colors[2]);
                }

                if (i+1 < height && j+1 < width)
                {
                    grads[0] += BinaryTools.toUnsignedInt(image[i+1][j+1].colors[0]);
                    grads[1] += BinaryTools.toUnsignedInt(image[i+1][j+1].colors[1]);
                    grads[2] += BinaryTools.toUnsignedInt(image[i+1][j+1].colors[2]);
                    grads[3] += BinaryTools.toUnsignedInt(image[i+1][j+1].colors[0]);
                    grads[4] += BinaryTools.toUnsignedInt(image[i+1][j+1].colors[1]);
                    grads[5] += BinaryTools.toUnsignedInt(image[i+1][j+1].colors[2]);
                }

            //combine gradient values via a L2 norm for one pixel value
            int blue = (int) Math.round(Math.sqrt(grads[0] * grads[0] + grads[3] * grads[3]));
            int green = (int) Math.round(Math.sqrt(grads[1] * grads[1] + grads[4] * grads[4]));
            int red = (int) Math.round(Math.sqrt(grads[2] * grads[2] + grads[5] * grads[5]));

            //cap them at 255
            blue = Math.min(255, blue);
            green = Math.min(255, green);
            red = Math.min(255, red);

            image2[i][j].colors[0] = (byte) blue;
            image2[i][j].colors[1] = (byte) green;
            image2[i][j].colors[2] = (byte) red;
        }
    }
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                image[i][j] = image2[i][j];
            }
        }   
    }
}