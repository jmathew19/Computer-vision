import java.io.*;

public class Jonathan_Mathew_Main {

    public static void main(String[] args) throws IOException {

        //step 0
        File in = new File(args[0]),
                outFile1 = new File(args[1]),
                outFile2 = new File(args[2]),
                de =new File(args[3]);

        FileReader inFile = new FileReader(in);

        FileWriter out1 = new FileWriter(outFile1),
                out2 = new FileWriter(outFile2),
                debug = new FileWriter(de);

        BufferedReader br = new BufferedReader(inFile);
        String line = br.readLine();
        String [] header = line.split(" ");
        int numRow = Integer.parseInt(header[0]),
                numCol = Integer.parseInt(header[1]),
                minVal = Integer.parseInt(header[2]),
                maxVal  = Integer.parseInt(header[3]);

        Jonathan_Mathew_ImageProcessing proj = new Jonathan_Mathew_ImageProcessing(numRow, numCol, minVal, maxVal);

        //step 1 + 2
        String fileName = args[0];
        fileName = fileName.substring(0,fileName.length()-4);
        FileWriter skeletonFile = new FileWriter(fileName + "_skeleton.txt");

        //step 3+4
        FileWriter decompressedFile = new FileWriter(fileName + "_decompressed.txt");

        //step 5
        proj.setZero(proj.zeroFramedAry);
        proj.setZero(proj.skeletonAry);

        //step 6
        proj.loadImage(inFile, br);

        //step 7
        proj.Distance8(out1, debug);
//        proj.printZero();

        //step 8
        proj.skeletonExtract(skeletonFile, out1, debug);

        //step 9
        FileReader skeletonFileRead = new FileReader(fileName + "_skeleton.txt");

        //step 10
        proj.deCompression(skeletonFileRead, out2, debug);

        //step 11
        proj.binThr(proj.zeroFramedAry);

        //step 12
//        decompressedFile.write(numRow+" " +numCol +" " + minVal +" " + maxVal);

        //step 13
        decompressedFile.write(numRow + " " + numCol + " " + minVal + " " + maxVal + "\n");
        for (int row = 1; row < numRow + 1; row++) {
            for (int col = 1; col < numCol + 1; col++) {
                decompressedFile.write(proj.zeroFramedAry[row][col] + " ");
            }
            decompressedFile.write("\n");
        }

        //step 14
        inFile.close();
        out1.close();
        out2.close();
        debug.close();
        decompressedFile.close();
        skeletonFileRead.close();






    }
}
