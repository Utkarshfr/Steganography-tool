import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.Math;

class PixelAdd{
    public void Merge(BufferedImage image1,BufferedImage image2,File OutputFile) throws Exception{
        int width = image2.getWidth();int height = image2.getHeight();
        int cov_width = image1.getWidth();int cov_height = image1.getHeight();
        String s = DecToBin(width, 12) + DecToBin(height, 12);
        Color size = new Color(BinToDec(s.substring(0,8)),BinToDec(s.substring(8, 16)),BinToDec(s.substring(16, 24)));

        BufferedImage image3 = new BufferedImage(cov_width,cov_height,BufferedImage.TYPE_INT_ARGB);
        
        try{
            for(int i=0;i<cov_height;i++){
                for(int j=0;j<cov_width;j++){
                    if(i == 0 && j == 0){
                        image3.setRGB(j, i, size.getRGB());
                        continue;
                    }
                    if(i >= height | j >= width){
                        image3.setRGB(j, i, image1.getRGB(j, i));
                        continue;
                    }
                    Color c = new Color(image1.getRGB(j,i));
                    Color d = new Color(image2.getRGB(j,i));
                    String r1 = new String(DecToBin(c.getRed(),8));
                    String r2 = new String(DecToBin(d.getRed(),8));
                    String g1 = new String(DecToBin(c.getGreen(),8));
                    String g2 = new String(DecToBin(d.getGreen(),8));
                    String b1 = new String(DecToBin(c.getBlue(),8));
                    String b2 = new String(DecToBin(d.getBlue(),8));

                    String nr = r1.substring(0,4) + r2.substring(0,4);
                    String ng = g1.substring(0,4) + g2.substring(0,4);
                    String nb = b1.substring(0,4) + b2.substring(0,4);
                    Color b = new Color(BinToDec(nr),BinToDec(ng),BinToDec(nb));
                    image3.setRGB(j, i, b.getRGB());
                }
            }
            ImageIO.write(image3,"png",OutputFile);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void Unmerge(BufferedImage image1,File OutputFile) throws Exception{
        Color a = new Color(image1.getRGB(0, 0));
        String x = new String(DecToBin(a.getRed(),8) + DecToBin(a.getGreen(),8) + DecToBin(a.getBlue(),8));
        int width = BinToDec(x.substring(0,12));
        int height = BinToDec(x.substring(12,24));
        BufferedImage output_img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        try{
            for(int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    Color c = new Color(image1.getRGB(j, i));
                    String r = new String(DecToBin(c.getRed(),8));
                    String g = new String(DecToBin(c.getGreen(),8));
                    String b = new String(DecToBin(c.getBlue(),8));

                    Color out = new Color(BinToDec(r.substring(4,8)),BinToDec(g.substring(4,8)),BinToDec(b.substring(4,8)));
                    output_img.setRGB(j, i, out.getRGB());
                }
            }
            ImageIO.write(output_img,"png",OutputFile);
        }
        catch(Exception e){
            System.out.println("An error Occured");
            System.out.println(e);
        }
    }


    String DecToBin(int x,int pad){
        String q = new String("");
        while(x > 0){   
            q = x%2 + q;
            x = x/2;
        }
        
        int s = q.length();
        for(int i=0;i<pad-s;i++)
            q = "0" + q;
    
        return q;
    }

    int BinToDec(String bin){
        if(bin.length() < 8){
            int size = bin.length();
            for(int i=0;i<8-size;i++)
                bin = bin + "0";
        }
        int num = 0;
        for(int i=bin.length()-1;i>=0;i--){
            num += Math.pow(2,bin.length()-i-1)*(bin.charAt(i) - '0');
        }
        return num;
    }

    public static void main(String[] args) {
        try{
            if(args[0].equals("merge")){
                BufferedImage image1 = ImageIO.read(new File(args[1]));
                BufferedImage image2 = ImageIO.read(new File(args[2]));
                File f3 = new File(args[3]);

                if(image2.getWidth() > image1.getWidth() || image2.getHeight() > image1.getHeight())
                    System.out.println("Use image to be hidden with smaller dimensions than carrier Image");
                else{
                    System.out.println("Merging Files...");
                    new PixelAdd().Merge(image1, image2, f3);
                    System.out.println("File saved as -> " + args[3]);
                }
            }
            else if(args[0].equals("unmerge")){
                System.out.println("Unmerging files...");
                BufferedImage image1 = ImageIO.read(new File(args[1]));
                File f1 = new File(args[2]);
                new PixelAdd().Unmerge(image1, f1);
                System.out.println("File saved as -> " + args[2]);
            }

            else{
                System.out.println("Usage: java PixelAdd <options> <File-1> <File-2> <File-3>");
                System.out.println("\nOptions:\n\tmerge->\n\t\tFile-1 : carrier image\n\t\tFile-2 : image to be hidden\n\t\tFile-3 : output image");
                System.out.println("\n\tunmerge->\n\t\tFile-1 : image containing hidden data\n\t\tFile-2 : output file");
            }
        }
        catch (Exception e){
            System.out.println("Enter a valid filename");
            System.out.println(e);
        }
    }
}