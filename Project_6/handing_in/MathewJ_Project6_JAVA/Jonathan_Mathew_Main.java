import java.io.*;

public class Jonathan_Mathew_Main {

    public static void main(String[] args) throws IOException {

        File infile = new File(args[0]),
                outFile = new File(args[1]);

        FileReader in = new FileReader(infile);
        FileWriter out = new FileWriter(outFile);

        BufferedReader br = new BufferedReader(in);
        String line = br.readLine();
        String [] header = line.split(" ");
        int numRow = Integer.parseInt(header[0]),
                numCol = Integer.parseInt(header[1]),
                minVal = Integer.parseInt(header[2]),
                maxVal  = Integer.parseInt(header[3]);

        Jonathan_Mathew_HoughTransform proj = new Jonathan_Mathew_HoughTransform(numRow, numCol, minVal, maxVal);


        in.close();
        in = new FileReader(infile);
        proj.loadImg(in);
        out.write("Here is Original input\n\n");
        proj.reformatPrettyPrint(proj.imgArr, out);
        proj.buildHSpace();

        out.write("\nHere is the CARTESIAN HOUGH SPACE\n\n");
        proj.reformatPrettyPrint(proj.cartHoughArr, out);
        out.write("\nHere is the POLAR HOUGH SPACE\n\n");
        proj.reformatPrettyPrint(proj.polarHoughArr, out);


        in.close();
        out.close();
    }



}
