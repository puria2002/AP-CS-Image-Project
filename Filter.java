import java.nio.file.*;
import java.io.*;
import java.util.Scanner;
public class Filter {
    public static void main(String[] args) throws IOException
    {   
        //ensure proper command line usage
        if (args.length != 3)
        {
            System.out.println("Usage: java Filter [-b, -e, -g, or -r] InputRelativePath OutputRelativePath");
            System.exit(1);
        }

        //check for allowable flags
        String filter = args[0].substring(1);

        if (!filter.equals("b") && !filter.equals("e") && !filter.equals("g") && !filter.equals("r"))
        {
            System.out.println("Filter must be flagged by either -b, -e, -g, or -r");
            System.exit(2);
        }

        //read input as byte array, handling exceptions as needed
        Path inputpath = Paths.get(args[1]);
        byte[] rawInput = null;

        try
        {
            rawInput = Files.readAllBytes(inputpath);
        }

        catch(IOException e)
        {
            System.out.println("File could not be read. Ensure it exists.");
            System.exit(3);
        }

        //check for BMP signature assuming little endian RAM 
        if (rawInput[0] != 66 || rawInput[1] != 77 || rawInput[14] != 40 || rawInput[15] != 0 || rawInput[16] != 0 || rawInput[17] != 0 || rawInput[28] != 24 || rawInput[29] != 0 || rawInput[30] != 0 || rawInput[31] != 0 || rawInput[32] != 0 || rawInput[33] != 0 )
        {
            System.out.println("File format not supported!");
            System.exit(4);
        }

        //read the rawinput for photo dimensions, converting bytes to int as needed
        int height;
        int width;

        width = (int) Math.abs(BinaryTools.toUnsignedInt(rawInput[18]) + 256 * BinaryTools.toUnsignedInt(rawInput[19]) + 256 * 256 * BinaryTools.toUnsignedInt(rawInput[20]) + 256 * 256 * 256 * BinaryTools.toUnsignedInt(rawInput[21]));

        height = (int) Math.abs(BinaryTools.toUnsignedInt(rawInput[22]) + 256 * BinaryTools.toUnsignedInt(rawInput[23]) + 256 * 256 * BinaryTools.toUnsignedInt(rawInput[24]) + 256 * 256 * 256 * BinaryTools.toUnsignedInt(rawInput[25]));

        //allocate (8/11 of) the memory for the image as an array of pixels
        Pixel[][] image = new Pixel[height][width];

        //calculate the length of skippable bytes within the rawinput (BMP convention)
        int padding = (4 - (width * 3) % 4) % 4;

        //initailize counter for array position in the rawinput based on file header
        int c = BinaryTools.toUnsignedInt(rawInput[10]) + 256 * BinaryTools.toUnsignedInt(rawInput[11]) + 256 * 256 * BinaryTools.toUnsignedInt(rawInput[12]) + 256 * 256 * 256 * BinaryTools.toUnsignedInt(rawInput[13]);

        //loop through rawinput to assign null pointers in array to pixel references
        byte[] tempbytes= new byte[3];

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                tempbytes[0] = rawInput[c];
                c++;
                tempbytes[1] = rawInput[c];
                c++;
                tempbytes[2] = rawInput[c];
                c++;
                image[i][j] = new Pixel(tempbytes[0], tempbytes[1], tempbytes[2]);
            }
            //increment counter by the padding
            c += padding;
        }
        
        //create our image object
        Image photo = new Image(image);
    
        //now create a switch statement for our filter cases!
        switch(filter.charAt(0))
        {
            // Blur
            case 'b':
                photo.blur();
                break;
    
            // Edges
            case 'e':
                photo.edgeDetect();
                break;
    
            // Grayscale
            case 'g':
                photo.toGrayscale();
                break;
    
            // Reflect
            case 'r':
                photo.reflect();
                break;
        }

        //write edited array to output file
        File outputpath = new File(args[2]);
        FileOutputStream output = null;

        if (outputpath.exists())
        {   Scanner keyboard = new Scanner(System.in);
            String s;

            do 
            {
                System.out.println("Are you sure you want to overrwrite the existing file? (y or n)");
                s = keyboard.nextLine();
            }
            while(!s.equals("y") && !s.equals("n"));

            keyboard.close();

            if (s.equals("y"))
            {
                try 
                {
                    output = new FileOutputStream(outputpath, false);
                    output.write(rawInput, 0, 54);
                }

                catch(Exception e) 
                {
                    System.out.println("File oddly could not be created");
                    System.exit(5);
                }
            }

            else
            {
                System.out.println("Aborting");
                System.exit(5);
            }

        }
        
        else
        {
            output = new FileOutputStream(args[2]);
            output.write(rawInput, 0, 54);
        }
        //create the padding array
        byte[] pad = new byte[padding];

        //finally, fill the output
        for (int i = 0; i < height; i++)
        {
           for (int j = 0; j < width; j++)
           {
               output.write(image[i][j].colors);
           }
        
           output.write(pad);
        }

        output.close();

    }
} 