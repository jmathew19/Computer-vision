import java.io.*;

public class Jonathan_Mathew_Main {

    public static void main(String[] args) throws IOException {
        File img = new File(args[0]);
        File elm = new File(args[1]);
        File output1 = new File(args[2]);
        File output2 = new File(args[3]);

        FileReader imgFile = new FileReader(img);
        FileReader elmFile = new FileReader(elm);

        FileWriter out1 = new FileWriter(output1);
        FileWriter out2 = new FileWriter(output2);

        //step1
        BufferedReader br = new BufferedReader(imgFile);
        String line = br.readLine();
        String[] values = line.split(" ");
        int numImgRows = Integer.parseInt(values[0]),
                numImgCols = Integer.parseInt(values[1]),
                imgMin = Integer.parseInt(values[2]),
                imgMax = Integer.parseInt(values[3]);

        br = new BufferedReader(elmFile);
        line = br.readLine();
        values = line.split(" ");
        int numStructRows = Integer.parseInt(values[0]),
                numStructCols = Integer.parseInt(values[1]),
                structMin = Integer.parseInt(values[2]),
                structMax = Integer.parseInt(values[3]);

        line = br.readLine();
        values = line.split(" ");
        int rowOrigin = Integer.parseInt(values[0]),
                colOrigin = Integer.parseInt(values[1]);


//        for (String s: values)
//            System.out.println(s);
//        System.out.println();

        //step 2
        Jonathan_Mathew_Morphology proj = new Jonathan_Mathew_Morphology(numImgRows, numImgCols, imgMin, imgMax, numStructRows, numStructCols, structMin, structMax, rowOrigin, colOrigin);

        //step 3
//        done in constructor

        //step 4
        imgFile.close();
        imgFile = new FileReader(img);
        proj.loadImg(imgFile);
        //step 5
        proj.imgReformat(out1);
        proj.prettyPrint(proj.zeroFramedAry, out1);

        //step 6
        elmFile.close();
        elmFile = new FileReader(elm);
        proj.loadStruct(elmFile);
        proj.prettyPrint(proj.structAry, out1);

        //step 7
        proj.basicOP(out1);

        //step8
        proj.complexOP(out2);

        //step 9
        imgFile.close();
        elmFile.close();
        out1.close();
        out2.close();



    }
}
