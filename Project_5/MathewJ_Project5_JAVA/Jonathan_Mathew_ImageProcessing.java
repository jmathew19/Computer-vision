import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Jonathan_Mathew_ImageProcessing {

    public int numRows,
        numCols,
        minVal,
        maxVal,
        newMinVal,
        newMaxVal;

    public int[][] zeroFramedAry,
        skeletonAry;


    public Jonathan_Mathew_ImageProcessing(int row, int col, int min, int max){

        numRows = row;
        numCols = col;
        minVal = min;
        maxVal = max;


        zeroFramedAry = new int[numRows+2][numCols+2];
        skeletonAry = new int[numRows+2][numCols+2];
    }


    public void setZero(int[][] arr) {
        // Set all elements of Ary to 0
        for (int[] ints : arr) {
            Arrays.fill(ints, 0);
        }
    }

    public void loadImage(FileReader file, BufferedReader br) throws IOException {
        // Read image data from file and store it in the frame of zeroFramedAry
        // Implementation not shown
        String line;
//        System.out.println(line)

        int count =1;

        while((line=br.readLine()) != null){
            String[] data = line.split(" ");
//            for (int i = count; i < numRows+1; i++){
                for (int j = 1; j< numCols+1; j++){
                    zeroFramedAry[count][j] = Integer.parseInt(data[j-1]);
                }
//            }
            count++;
        }


        printZero();

    }

    public void printZero(){
        for (int i = 0; i < numRows+2; i++){
            for (int j = 1; j< numCols+2; j++){
                if(zeroFramedAry[i][j] == 0){
//                    System.out.print(". ");
                }else{
//                    System.out.print(zeroFramedAry[i][j] + " ");
                }
            }
//            System.out.println();
        }

    }

    public  void Distance8(FileWriter out1, FileWriter debug) throws IOException {
        // Compute the 8-neighbor distance of pixel (i, j) in zeroFramedAry
        // Implementation not shown
        //step 0
        debug.write("Entering Distance8\n");
        //step 1
        out1.write("Printing OG Table\n");
        reformatPrettyPrint(zeroFramedAry, out1);
        distance8pass1(zeroFramedAry);
        //step 2
        out1.write("Printing Table After Dist 8 pass 1\n");
        reformatPrettyPrint(zeroFramedAry, out1);
        reformatPrettyPrint(zeroFramedAry, debug);
        //step 3
        out1.write("Printing Table After Dist 8 pass 2\n");
        distance8pass2(zeroFramedAry);
        //step 4
        reformatPrettyPrint(zeroFramedAry, out1);
        reformatPrettyPrint(zeroFramedAry, debug);
        //step 5
        debug.write("Leaving Distance8\n");
    }

    private void distance8pass2(int[][] arr) {

        for (int r = numRows; r > 0 ; r--) {
            for (int c = numCols; c > 0 ; c--) {

                if(zeroFramedAry[r][c] > 0){
                    int e = zeroFramedAry[r][c + 1];
                    int f = zeroFramedAry[r + 1][c - 1];
                    int g = zeroFramedAry[r + 1][c];
                    int h = zeroFramedAry[r+1][c +1];

                    zeroFramedAry[r][c] = Math.min(Math.min(e+1, Math.min(f+1, Math.min(g+1,h+1))), zeroFramedAry[r][c]);
                    newMinVal = Math.min(newMinVal , zeroFramedAry[r][c]);
                    newMaxVal = Math.max(newMaxVal , zeroFramedAry[r][c]);
                }

            }
        }
    }

    private void reformatPrettyPrint(int[][] arr, FileWriter out1) throws IOException {

        out1.write(numRows + " " + numCols + " " + minVal + " " + maxVal + "\n");
        String str = Integer.toString(newMaxVal);
        int width = str.length();

        int ww;
        int r = 1;

        while (r < numRows+1) {
            int c = 1;
            while (c < numCols+1) {
                if (arr[r][c] == 0) {
                    out1.write(".");
                } else {
                    out1.write(arr[r][c] + "");
                }
                str = Integer.toString(arr[r][c]);
                ww = str.length();
                while (ww <= width) {
                    out1.write(" ");
                    ww++;
                }
                c++;
            }
            r++;
            out1.write("\n");

        }
        out1.write("\n");

    }

    private void distance8pass1(int[][] arr) {

        for (int r = 1; r < numRows + 1; r++) {
            for (int c = 1; c < numCols + 1; c++) {

                if (arr[r][c] > 0) {
                    int a = arr[r - 1][c - 1];
                    int b = arr[r - 1][c];
                    int cl = arr[r - 1][c + 1];
                    int d = arr[r][c - 1];

                    zeroFramedAry[r][c] = Math.min(a, Math.min(b, Math.min(cl,d))) + 1;

                }

            }
        }

    }


    public void skeletonExtract(FileWriter skeletonFile, FileWriter out1, FileWriter debug) throws IOException {
//        Step 0: deBugFile“Entering skeletonExtraction”
        debug.write("Entering skeletonExtraction\n");
//        step 1: localMaxima (zeroFramedAry, skeletonAry)
        localMax();
//        Step 2: reformatPrettyPrint (skeletonAry, outFile1)
        out1.write(" Running Local Maxima ");
        reformatPrettyPrint(skeletonAry, out1);
//        step 3: compression (skeletonAry, skeletonFile)
        compression(skeletonAry, skeletonFile);
//        Step 4: close skeletonFile
        skeletonFile.close();
//        Step 5: deBugFile“Leaving skeletonExtraction”
        debug.write("Leaving skeletonExtraction\n");
    }

    private void compression(int[][] arr, FileWriter skeletonFile) throws IOException {


        for (int r = 1; r < numRows + 1; r++) {
            for (int c = 1; c < numCols + 1; c++) {

                if ( arr[r][c] > 0) {
                    skeletonFile.write(r + " " + c +  " " + arr[r][c] + "\n");
                }

            }
        }

    }

    private void localMax() {
        for (int r = 1 ; r<numRows+1; r++){
            for(int c=1; c <numCols+1;c++){

                //step 2
                if(isLocalMax(r,c)){

                    skeletonAry[r][c] = zeroFramedAry[r][c];
                }else{
                    skeletonAry[r][c] = 0;
                }
            }
        }
    }

    private boolean isLocalMax(int r, int c) {

        int a = zeroFramedAry[r - 1][c - 1],
                b = zeroFramedAry[r - 1][c],
                cl = zeroFramedAry[r - 1][c + 1],
                d = zeroFramedAry[r][c - 1],
                e =  zeroFramedAry[r][c + 1],
                f = zeroFramedAry[r + 1][c - 1],
                g = zeroFramedAry[r +1][c],
                h = zeroFramedAry[r +1][c + 1];

        return zeroFramedAry[r][c] >= a &&
                zeroFramedAry[r][c] >= b &&
                zeroFramedAry[r][c] >= cl &&
                zeroFramedAry[r][c] >= d &&
                zeroFramedAry[r][c] >= e &&
                zeroFramedAry[r][c] >= f &&
                zeroFramedAry[r][c] >= g &&
                zeroFramedAry[r][c] >= h;
    }

    public void deCompression( FileReader skeletonFile, FileWriter out2, FileWriter debug) throws IOException {
//        Step 0: deBugFile“Entering deCompression method”
        debug.write("Entering deCompression method\n");
//        Step 1: setZero (zeroFramedAry)
        setZero(zeroFramedAry);
//        step 2: load (skeletonFile, zeroFramedAry)
        load(skeletonFile);
        out2.write("Zero fram arr\n");
        reformatPrettyPrint(zeroFramedAry, out2);
//        step 3: expension8Pass1 (zeroFramedAry)
        expension8Pass1();
//        step 4: reformatPrettyPrint (zeroFramedAry, outFile2)
        out2.write("After Expansion Pass 1\n");
        reformatPrettyPrint(zeroFramedAry, out2);
//        step 5: expension8Pass2 (zeroFramedAry)
        expension8Pass2();
//          step 6
        out2.write("After Expansion Pass 2\n");
        reformatPrettyPrint(zeroFramedAry, out2);
        //step 7
        debug.write("Leaving deCompression method\n");

    }


    private void load(FileReader skeletonFile) throws IOException {
        int r, c, val;
        String line;
        BufferedReader br  = new BufferedReader(skeletonFile);

        while((line=br.readLine()) != null){
            String[]  comp = line.split( " ");
            r = Integer.parseInt(comp[0]);
            c = Integer.parseInt(comp[1]);
            val = Integer.parseInt(comp[2]);
            zeroFramedAry[r][c] = val;

        }



    }

    private void expension8Pass1() {


        for (int r = 1; r < numRows + 1; r++) {
            for (int c = 1; c < numCols + 1; c++) {

                int a = zeroFramedAry[r - 1][c - 1] -1,
                        b = zeroFramedAry[r - 1][c] -1,
                        cl = zeroFramedAry[r - 1][c + 1] -1 ,
                        d = zeroFramedAry[r][c - 1]-1,
                        e =  zeroFramedAry[r][c + 1]-1,
                        f = zeroFramedAry[r + 1][c - 1]-1,
                        g = zeroFramedAry[r +1][c]-1,
                        h = zeroFramedAry[r +1][c + 1]-1;
                //step 2
                if (zeroFramedAry[r][c] == 0) {

                    zeroFramedAry[r][c] = Math.max( zeroFramedAry[r][c], Math.max(a,Math.max(b, Math.max(cl,Math.max(d,Math.max(e, Math.max(f, Math.max(g, h))))))));

                }

            }
        }

    }

    private void expension8Pass2() {


        for (int r = numRows; r > 0; r--) {
            for (int c = numCols; c > 0; c--) {

                //step 2
                int a = zeroFramedAry[r - 1][c - 1] -1,
                        b = zeroFramedAry[r - 1][c] -1,
                        cl = zeroFramedAry[r - 1][c + 1] -1 ,
                        d = zeroFramedAry[r][c - 1]-1,
                        e =  zeroFramedAry[r][c + 1]-1,
                        f = zeroFramedAry[r + 1][c - 1]-1,
                        g = zeroFramedAry[r +1][c]-1,
                        h = zeroFramedAry[r +1][c + 1]-1,
                        max = zeroFramedAry[r][c];

                max =  zeroFramedAry[r][c] = Math.max( zeroFramedAry[r][c], Math.max(a,Math.max(b, Math.max(cl,Math.max(d,Math.max(e, Math.max(f, Math.max(g, h))))))));

                if (zeroFramedAry[r][c] < max) {
                    zeroFramedAry[r][c] = max;
                }

            }
        }
    }



    public void binThr(int[][] zeroFramedAry) {

        for(int r = 0; r < numRows + 2; r++) {
            for(int c = 0; c < numCols + 2; c++) {

                if(zeroFramedAry[r][c]>0){
                    zeroFramedAry[r][c] = 1;
                }else{
                    zeroFramedAry[r][c] = 0;
                }

            }
        }
    }




}
