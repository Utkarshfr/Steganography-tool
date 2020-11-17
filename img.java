import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.Math;

class PixelAdd{

    
    public void Merge(File Image1,File Image2,File OutputFile) throws Exception{
        BufferedImage image1 = ImageIO.read(Image1);
        BufferedImage image2 = ImageIO.read(Image2);
        int width = image1.getWidth();
        int height = image1.getHeight();
        BufferedImage image3 = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        try{
            for(int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    Color c = new Color(image1.getRGB(j,i));
                    Color d = new Color(image2.getRGB(j,i));
                    String r1 = new String(DecToBin(c.getRed()));
                    String r2 = new String(DecToBin(d.getRed()));
                    String g1 = new String(DecToBin(c.getGreen()));
                    String g2 = new String(DecToBin(d.getGreen()));
                    String b1 = new String(DecToBin(c.getBlue()));
                    String b2 = new String(DecToBin(d.getBlue()));

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

    public void Unmerge(File Image,File OutputFile) throws Exception{
        BufferedImage image1 = ImageIO.read(Image);
        int width = image1.getWidth();
        int height = image1.getHeight();
        BufferedImage output_img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        try{
            for(int i=0;i<height;i++){
                for(int j=0;j<width;j++){
                    Color c = new Color(image1.getRGB(j, i));
                    String r = new String(DecToBin(c.getRed()));
                    String g = new String(DecToBin(c.getGreen()));
                    String b = new String(DecToBin(c.getBlue()));

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

    String DecToBin(int x){
        String q = new String("");
        while(x > 0){   
            q = x%2 + q;
            x = x/2;
        }
        
        int s = q.length();
        for(int i=0;i<8-s;i++)
            q = "0" + q;
    
        return q;
    }

    int BinToDec(String bin){
        if(bin.length() != 8){
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

    public static void main(String[] args) throws Exception{
        try{
            if(args[0].equals("merge")){
                File f1 = new File(args[1]);
                File f2 = new File(args[2]);
                File f3 = new File(args[3]);
                System.out.println("Merging Files...");
                new PixelAdd().Merge(f1, f2, f3);
                System.out.println("Saved File as -> " + args[3]);
            }
            else if(args[0].equals("unmerge")){
                System.out.println("Unmerging files...");
                File f1 = new File(args[1]);
                File f2 = new File(args[2]);
                new PixelAdd().Unmerge(f1, f2);
            }

            else{
                System.out.println("Usage: java PixelAdd <options> <File-1> <File-2> <File-3>");
                System.out.println("\nOptions:\n\tmerge->\n\t\tFile-1 : carrier image\n\t\tFile-2 : image to be hidden\n\t\tFile-3 : output image");
                System.out.println("\n\tunmerge->\n\t\tFile-1 : image containing hidden data\n\t\tFile-2 : output file");
            }
        }
        catch (Exception e){
            System.out.println("Enter a valid file");
            System.out.println(e);
        }
    }
}