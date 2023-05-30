import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Jonathan_Mathew_Morphology {

    int numImgRows;
    int numImgCols;
    int imgMin;
    int imgMax;
    int numStructRows;
    int numStructCols;
    int structMin;
    int structMax;
    int rowOrigin;
    int colOrigin;
    int rowFrameSize;
    int colFrameSize;
    int extraRows;
    int extraCols;
    int rowSize;
    int colSize;
    public int[][] zeroFramedAry;
    public int[][] morphAry;
    public int[][] tempAry;
    public int[][] structAry;


    public Jonathan_Mathew_Morphology(int numImgRows, int numImgCols, int imgMin, int imgMax, int numStructRows, int numStructCols, int structMin, int structMax, int rowOrigin, int colOrigin){

        this.numImgRows = numImgRows;
        this.numImgCols = numImgCols;
        this.imgMin = imgMin;
        this.imgMax = imgMax;
        this.numStructRows = numStructRows;
        this.numStructCols = numStructCols;
        this.structMin = structMin;
        this.structMax = structMax;
        this.rowOrigin = rowOrigin;
        this.colOrigin = colOrigin;

        this.rowFrameSize = numStructRows /2;
        this.colFrameSize = numStructCols /2;

        this.extraRows = rowFrameSize*2;
        this.extraCols = colFrameSize*2;
        this.rowSize = numImgRows + extraRows;
        this.colSize = numImgCols + extraCols;
//
//        System.out.println("numimg rows:" + numImgRows);
//        System.out.println("numimg col:" + numImgCols);
//        System.out.println("extraRows rows:" + extraRows);
//        System.out.println("extraCols rows:" + extraCols);
//        System.out.println("rowSize rows:" + rowSize);
//        System.out.println("colSize rows:" + colSize);

        this.zeroFramedAry = new int[rowSize][colSize];
        this.morphAry = new int[rowSize][colSize];
        this.tempAry = new int[rowSize][colSize];
        this.structAry = new int[numStructRows][numStructCols];

        zero2DAry(zeroFramedAry);
        zero2DAry(morphAry);
        zero2DAry(tempAry);
        zero2DAry(structAry);
    }

    public void zero2DAry (int[][] arr){

        int r = arr.length, c = arr[0].length;
        for (int i=0; i<r;i++){
            for (int j=0; j<c;j++){
                arr[i][j]=0;
            }
        }
    }

    public void onePixelDilation(int i, int j, int[][] inArr, int[][]outArr) {
        int iOffset = i - rowOrigin;
        int jOffset = j - colOrigin;


        int rIndex = 0;

        while (rIndex < numStructRows) {
            //step 2
            int cIndex = 0;
            //step 5
            while (cIndex < numStructCols) {
                //step 3
                if (structAry[rIndex][cIndex] > 0) {
//                    System.out.println(iOffset + rIndex);
//                    System.out.println(jOffset + cIndex);
                    outArr[iOffset + rIndex][jOffset + cIndex] = 1;
                }

                //step 4
                cIndex++;
            }
            //step 6
            rIndex++;
        }

    }

    public void onePixelErosion(int i, int j, int[][] inArr, int[][]outArr){
        int iOffset = i - rowOrigin;
        int jOffset = j - colOrigin;
        boolean match = true;

        //step1
        int rIndex = 0;


        //step 7
        while ((match) && (rIndex < numStructRows)) {
            //step 2
            int cIndex = 0;
            //step 5
            while ( (match) && (cIndex < numStructCols )) {
                //step 3
                if (structAry[rIndex][cIndex] > 0 &&
                        (inArr[iOffset + rIndex][jOffset + cIndex] ) <= 0) {
                    match = false;
                }

                //step 4
                cIndex++;
            }
            //step 6
            rIndex++;
        }

        //step 8
        if(match){
            outArr[i][j] = 1;
        }else{
            outArr[i][j] = 0;
        }

    }

    public void computeDilation (int[][] inArr, int[][]outArr){
        //step 1
        int i = rowFrameSize;

        //step 7
        while (i<rowSize) {

            //step 2
            int j = colFrameSize;

            //step 5
            while (j < colSize) {

                //step 3
                if (inArr[i][j] > 0) {
                    onePixelDilation(i, j, inArr, outArr);
                }

                //step 4
                j++;
            }

            //step 6;
            i++;
        }
    }

    public void computeErosion (int[][] inArr, int[][]outArr){
        //step1
        int i = rowFrameSize;

        //step 7
        while (i<rowSize) {
            //step2
            int j = colFrameSize;

            //step5

            while (j < colSize) {
                //step 3
                if (inArr[i][j] > 0) {
                    onePixelErosion(i, j, inArr, outArr);
                }

                //step 4
                j++;
            }

            //step6
            i++;
        }
    }

    public void computeClosing(){
        computeDilation(zeroFramedAry,tempAry);
        computeErosion(tempAry, morphAry);
        zero2DAry(tempAry);
    }

    public void computeOpening(){
        computeErosion(zeroFramedAry, tempAry);
        computeDilation(tempAry,morphAry);
        zero2DAry(tempAry);
    }

    public void loadImg(FileReader input) throws IOException {
        BufferedReader br = new BufferedReader(input);
        String line = br.readLine();
        int i=1;
        while((line=br.readLine())!=null){
            String[] c = line.split(" ");
            for(int j=1; j<c.length; j++){
                zeroFramedAry[i][j] = Integer.parseInt(c[j-1]);
            }
            i++;
        }
    }

    public void loadStruct(FileReader input) throws IOException {
        BufferedReader br = new BufferedReader(input);
        String line = br.readLine();
        line = br.readLine();
        int i=0;
        while((line=br.readLine())!=null){
            String[] c = line.split(" ");
            for(int j=0; j<c.length; j++){
                structAry[i][j] = Integer.parseInt(c[j]);
//                System.out.print(c[j]);
            }
//            System.out.println();
            i++;
        }
    }

    public void imgReformat(FileWriter output) throws IOException {
        output.write("\n" + zeroFramedAry.length + ", " + zeroFramedAry[0].length + ", 0, 1\n");

        int width = 1;

        int r = 0;

        while (r<zeroFramedAry.length){
            int c = 0;
            while (c<zeroFramedAry[0].length){

                output.write(zeroFramedAry[r][c] + " ");
                c++;
            }
            output.write("\n");
            r++;
        }

    }

    public void prettyPrint(int[][] arr, FileWriter output) throws IOException {

        output.write("\n\n" + zeroFramedAry.length + ", " + zeroFramedAry[0].length + ", 0, 1\n\n");
        for (int i = 0; i < arr.length; i++)
        {
            for (int j = 0; j < arr[0].length; j++)
            {
                if(arr[i][j]>0){
                    output.write(arr[i][j] + " ");
                }else{
                    output.write( "  ");
                }
            }
            output.write("\n");

        }
    }

    public void basicOP(FileWriter output)throws IOException{

        //step 0
        output.write("\nentering basicOperations method \n");
        //step 1
        zero2DAry(morphAry);
        computeDilation(zeroFramedAry, morphAry);
        output.write("\n Printing result of ComputeDilation. \n");
        prettyPrint(morphAry,output);
        //step 2
        zero2DAry(morphAry);
        computeErosion(zeroFramedAry, morphAry);
        output.write("\n Printing result of ComputeErosion. \n");
        prettyPrint(morphAry,output);
        //step 3
        zero2DAry(morphAry);
        computeOpening();
        output.write("\n Printing result of ComputeOpening. \n");
        prettyPrint(morphAry,output);
        //step 4
        zero2DAry(morphAry);
        computeClosing();
        output.write("\n Printing result of ComputeClosing. \n");
        prettyPrint(morphAry,output);

        //step 5
        output.write("\nexit basicOperations method\n");
    }

    public void complexOP(FileWriter output)throws IOException{
        //step 0
        output.write("entering complexOperations method \n");

        //step 1
        zero2DAry(morphAry);
        computeOpening();
        output.write("\n Printing result of Opening. \n");
        prettyPrint(morphAry,output);
        copyArr();

        //step 2
        zero2DAry(morphAry);
        computeClosing();
        output.write("\n Pretty print the result of Opening follow by Closing. \n");
        prettyPrint(morphAry,output);
        copyArr();

        //step 3
        zero2DAry(morphAry);
        computeClosing();
        output.write("\n  Pretty print the result of Closing. \n");
        prettyPrint(morphAry,output);
        copyArr();

        //step 4
        zero2DAry(morphAry);
        computeOpening();
        output.write("\n  Pretty print the result of Closing follow by Opening. \n");
        prettyPrint(morphAry, output);


        output.write("\n Exit complexOperations method” \n");
    }

    void copyArr(){
        for (int i=0; i<zeroFramedAry.length;i++){
            System.arraycopy(morphAry[i], 0, zeroFramedAry[i], 0, zeroFramedAry[0].length);
        }
    }

}
