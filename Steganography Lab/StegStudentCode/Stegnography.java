// I got help from James Overholt fixing a bug
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Point;

public class Stegnography {
    public static void main(String[] args) {
        Picture flower1 = new Picture("flower1.jpg");
        /* ACTIVITY 1
        mark.explore();
        Picture copy = testSetLow(beach, Color.PINK);
        Picture copy2 = revealPicture(copy);
        copy2.explore();
        */

        /*ACTIVITY 2
        Picture beach = new Picture("beach.jpg");
        Picture mark = new Picture("blue-mark.jpg");
        Picture moon = new Picture("moon-surface.jpg");
        Picture robot = new Picture("robot.jpg");

        beach.explore();
        // these lines hide 2 pictures
        Picture hidden1 = hidePictureTwo(beach, mark, 0, 0);
        if(canHide(beach, mark) && (canHide(beach, flower1)))
        {
            System.out.println("Can hide");
        }
        Picture unhidden = revealPicture(hidden1);
        unhidden.explore();
        */

        /* ACTIVITY THREE

        */
        Picture hall = new Picture("femaleLionAndHall.jpg");
        Picture robot2 = new Picture("robot.jpg");
        Picture flower2 = new Picture("flower1.jpg");

        Picture hall2 = hidePictureTwo(hall, robot2, 50, 300);
        Picture hall3 = hidePictureTwo(hall2, flower2, 115, 275);
        hall3.explore();
        if(!isSame(hall, hall3))
        {
            Picture hall4 = showDifferentArea(hall, findDifferences(hall, hall3));
            hall4.show();
            Picture unhiddenHall3 = revealPicture(hall3);
            unhiddenHall3.show();
        }

        /* ACTIVITY FOUR

         */
        Picture beach = new Picture("beach.jpg");
        hideText(beach, "HELLO WORLD");
        String revealed = revealText(beach);
        beach.show();
        System.out.println("Hidden message: " + revealed);

    }
    /*
    Clear the lower (rightmost) two bits in a pixel.
     */
    public static void clearLow(Pixel pix)
    {
        int red = ((pix.getRed() / 4) * 4);
        int green = ((pix.getGreen() / 4) * 4);
        int blue = ((pix.getBlue() / 4) * 4);
        pix.setColor(new Color(red, green, blue));
    }

    public static Picture testClearLow(Picture pic)
    {
        Picture pict = new Picture(pic);
        Pixel[][] pixels = pict.getPixels2D();
        for (int ron = 0; ron < pixels.length; ron++)
        {
            for (int cochran = 0; cochran < pixels[ron].length; cochran++)
            {
                clearLow(pixels[ron][cochran]);
            }
        }
        return pict;
    }

    /*
    Set the lower two bits in a pixel to the highest 2 pits in clr
     */
    public static void setLow(Pixel pix, Color clr)
    {
        clearLow(pix);
        int red = (pix.getRed() + (clr.getRed() / 64));
        int blue = (pix.getBlue() + (clr.getBlue() / 64));
        int green = (pix.getGreen() + (clr.getGreen() / 64));
        pix.setColor(new Color(red, green, blue));
    }

    public static Picture testSetLow(Picture pic, Color clr)
    {
        Picture copy = new Picture(pic);
        Pixel[][] pixels = copy.getPixels2D();
        for (int ron = 0; ron < pixels.length; ron++)
        {
            for (int cochran = 0; cochran < pixels[ron].length; cochran++)
            {
                setLow(pixels[ron][cochran], clr);
            }
        }
        return copy;
    }

    public static Picture revealPicture(Picture hidden)
    {
        Picture copy = new Picture(hidden);
        Pixel[][] pixels = copy.getPixels2D();
        Pixel[][] source = hidden.getPixels2D();
        for (int ron = 0; ron < pixels.length; ron++) {
            for (int cochran = 0; cochran < pixels[0].length; cochran++)
            {
                Color sourceColor = source[ron][cochran].getColor();
                // Finds color the matching pixel in the hidden guy
                Color pixelColor = new Color(
                        (pixels[ron][cochran].getRed() % 64) + (sourceColor.getRed() % 4 * 64),
                        (pixels[ron][cochran].getGreen() % 64) + (sourceColor.getGreen() % 4 * 64),
                        (pixels[ron][cochran].getBlue() % 64) + (sourceColor.getBlue() % 4 * 64)
                );
                // Applies it
                pixels[ron][cochran].setColor(pixelColor);
            }
        }
        return copy;
    }

    public static boolean canHide(Picture source, Picture secret)
    {
        return source.getHeight() >= secret.getHeight() && source.getWidth() >= secret.getWidth();
    }
    public static Picture hidePicture(Picture source, Picture secret)
    {
        // Checks if the source picture is the same size or greater than the hidden pic
        if(!canHide(source, secret))
        {
            System.out.println("Cannot hide: " + source + " & " + secret);
            return null;
        }
        else
        {
            System.out.println("Can hide: " + source + " & " + secret);
            Picture pic = new Picture(source);
            Pixel[][] sourcePixels = pic.getPixels2D();
            Pixel[][] secretPixels = secret.getPixels2D();
            for (int ron = 0; ron < sourcePixels.length; ron++)
            {
                for (int cochran = 0; cochran < sourcePixels.length; cochran++)
                {
                    // setLows the color of the source pixels with the color of the secret pixels
                    setLow(sourcePixels[ron][cochran], secretPixels[ron][cochran].getColor());
                }
            }
            return pic;
        }
    }
    public static Picture hidePictureTwo(Picture source, Picture secret, int startRow, int startColumn)
    {
        Picture result = new Picture(source);
        Pixel[][] resultPixels = result.getPixels2D();
        Pixel[][] secretPixels = secret.getPixels2D();
        for (int ron = startRow, secretRon = 0; ron < resultPixels.length && secretRon < secretPixels.length; ron++, secretRon++)
        {
            for (int cochran = startColumn, secretCochran = 0; cochran < resultPixels[ron].length && secretCochran < secretPixels[secretRon].length; cochran++, secretCochran++)
            {
                setLow(resultPixels[ron][cochran], secretPixels[secretRon][secretCochran].getColor());
            }
        }
        return result;
    }

    public static boolean isSame(Picture pic1, Picture pic2)
    {
        Pixel[][] p1Pixels = pic1.getPixels2D();
        Pixel[][] p2Pixels = pic2.getPixels2D();
        Pixel p1Pixel;
        Pixel p2Pixel;
        for (int ron = 0; ron < p1Pixels.length; ron++)
        {
            for (int cochran = 0; cochran < p1Pixels[0].length; cochran++)
            {
                p1Pixel = p1Pixels[ron][cochran];
                p2Pixel = p2Pixels[ron][cochran];
                if (p1Pixel.getRed() != p2Pixel.getRed() || p1Pixel.getBlue() != p2Pixel.getBlue() || p1Pixel.getGreen() != p2Pixel.getGreen())
                {
                    return false;
                }
            }
        }
        return true;
    }
    public static ArrayList<Point> findDifferences(Picture source, Picture secret)
    {
        ArrayList<Point> differences = new ArrayList<>();
        Pixel[][] sourcePixels = source.getPixels2D();
        Pixel[][] secretPixels = secret.getPixels2D();
        for(int ron = 0; ron < sourcePixels.length; ron++)
        {
            for(int cochran = 0; cochran < sourcePixels[ron].length; cochran++)
            {
                if(!secretPixels[ron][cochran].getColor().equals(sourcePixels[ron][cochran].getColor()))
                {
                    differences.add(new Point(ron, cochran));
                }
            }
        }
        return differences;
    }
    public static Picture showDifferentArea(Picture source, ArrayList<Point> differences)
    {
        Pixel[][] sourcePixels = source.getPixels2D();
        int xMin = (int) differences.getFirst().getX();
        int yMin = (int) differences.getFirst().getY();

        int xMax = (int) differences.getFirst().getX();
        int yMax = (int) differences.getFirst().getY();

        for (int i = 0; i < differences.size(); i++)
        {
            Point difference = differences.get(i);
            int x = (int) difference.getX();
            int y = (int) difference.getY();
            //System.out.println(x + " " + y);
            // Copious ifs
            if (x > xMax)
            {
                xMax = x;
            }
            if (y > yMax)
            {
                yMax = y;
            }
            if (x < xMin)
            {
                xMin = x;
            }
            if (y < yMin)
            {
                yMin = y;
            }
        }
        //System.out.println(xMax + " " + xMin + " / " + yMax + " " + yMin);
        // Coloring the border
        for(int i = xMin; i <= xMax; i++)
        {
            sourcePixels[i][yMin].setColor(Color.red);
            sourcePixels[i][yMax].setColor(Color.red);
        }
        for(int i = yMin; i <= yMax; i++)
        {
            sourcePixels[xMin][i].setColor(Color.red);
            sourcePixels[xMax][i].setColor(Color.red);
        }
        return source;
    }
    public static ArrayList<Integer> encodeString(String str)
    {
        str = str.toUpperCase();
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < str.length(); i++)
        {
            if (str.substring(i,i + 1).equals(" "))
            {
                result.add(27);
            }
            else
            {
                result.add(alpha.indexOf(str.substring(i,i+1))+1);
            }
        }
        result.add(0);
        return result;
    }
    public static String decodeString(ArrayList<Integer> codes)
    {
        String result = "";
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < codes.size(); i++)
        {
            if (codes.get(i) == 27)
            {
                result = result + " ";
            }
            else
            {
                result = result + alpha.substring(codes.get(i) - 1,codes.get(i));
            }
        }
        return result;
    }
    private static int[] getBitPairs(int num)
    {
        int[] bits = new int[3];
        int code = num;
        for (int i = 0; i < 3; i++)
        {
            bits[i] = code % 4;
            code = code / 4;
        }
        return bits;
    }
    public static void hideText(Picture source, String str)
    {
        ArrayList<Integer> encodedMsg = encodeString(str);
        Pixel[][] pixels = source.getPixels2D();
        int index = 0;
        for(int ron = 0; ron < pixels.length; ron++)
        {
            for(int cochran = 0; cochran < pixels[ron].length; cochran++)
            {
                // Keeps index from going too far
                if(index >= encodedMsg.size()) return;
                // Gets the bit pairs representing the current encoded message integer
                int[] bitPairs = getBitPairs(encodedMsg.get(index));
                // Applies each color value of the pixel to the message bits
                pixels[ron][cochran].setRed((pixels[ron][cochran].getRed() / 4) * 4 + bitPairs[2]);
                pixels[ron][cochran].setGreen((pixels[ron][cochran].getGreen() / 4 * 4) + bitPairs[1]);
                pixels[ron][cochran].setBlue((pixels[ron][cochran].getBlue() / 4 * 4) + bitPairs[0]);
                index++;
            }
        }
    }
    public static String revealText(Picture source)
    {
        ArrayList<Integer> encodedMsg = new ArrayList<>();
        Pixel[][] pixels = source.getPixels2D();
        for(int ron = 0; ron < pixels.length; ron++)
        {
            for(int cochran = 0; cochran < pixels[ron].length; cochran++)
            {
                // Assigns the significant bits from each color value to the variable
                int redBits = (pixels[ron][cochran].getRed() % 4);
                int blueBits = (pixels[ron][cochran].getBlue() % 4);
                int greenBits = (pixels[ron][cochran].getGreen() % 4);
                int bit = (redBits * 16) + (greenBits * 4) + blueBits;
                if(bit == 0) return decodeString(encodedMsg);
                encodedMsg.add(bit); // Adds it to the thingy
            }
        }
        // Returns the thingi
        return decodeString(encodedMsg);
    }
}