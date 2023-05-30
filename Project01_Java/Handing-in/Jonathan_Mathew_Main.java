import java.io.*;
import java.util.Scanner;

public class Jonathan_Mathew_Main {
    public static void main(String[] args) throws IOException {
        File inFile = new File(args[0]);
        FileReader input = new FileReader(inFile);

        File outFile = new File(args[1]);
        FileWriter output = new FileWriter(outFile);

        BufferedReader br=new BufferedReader(input);
        String[] line = br.readLine().split(" ");
        int numRows = Integer.parseInt(line[0]);
        int numCol = Integer.parseInt(line[1]);
        int minVal = Integer.parseInt(line[2]);
        int MaxVal = Integer.parseInt(line[3]);

        Scanner scanner = new Scanner(System.in);
        System.out.println("please insert user input");
        int thrVal =  scanner.nextInt();

        output.write(numRows + " " + numCol + " 0 1 \n" );
        processing(br, output, thrVal);
        output.close();
        input.close();
    }



    public static void processing(BufferedReader input, FileWriter output, int thrVal) throws IOException {

        int pixelVal;
//        BufferedReader br = new BufferedReader(input);
        String line = input.readLine();

        while((line=input.readLine())!=null){
//            System.out.println(line);
            String[] data = line.split(" ");
            for(String s: data){
                if(Integer.parseInt(s)>=thrVal){
                    pixelVal = 1;

                }else{
                    pixelVal = 0;
                }

                output.write(pixelVal + " ");
            }
            output.write("\n");
        }

    }
}
