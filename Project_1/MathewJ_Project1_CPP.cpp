#include <iostream>
#include <fstream>
#include <cmath>
using namespace std;

//class
class thresholdSelection{
public:
    int numRows, numCols, minVal, maxVal, x1, y1, x2, y2, *histAry, deepestThrVal, BiGaussThrVal, *GaussAry;

    thresholdSelection(int r, int c, int min, int max, int *arr){
        this->numRows = r;
        this->numCols = c;
        this->minVal = min;
        this->maxVal = max;
        this->histAry = arr;
        this->GaussAry = new int[maxVal +1];
    }


    int loadHist(ifstream &input){

        int index, val, max = 0;

        while(!input.eof()){
            input >> index >> val;
            this->histAry[index] = val;
            if(max<histAry[index]){
                max = histAry[index];
            }
        }

        return max;
    }

    void dispHist(ofstream *output){
  
        for (int i = 0; i < maxVal +1 ; i++){

            *output<<i<<" "<< histAry[i]<<": ";
           for (int j = 0; j < histAry[i]; j++){
                *output << "+";
           }

           *output << endl;
           
        }
        
    }

    void setZero(int arr[], int size){
        // cout<<size;
        for(int i = 0; i < size; i++){
            arr[i] = 0;
        }
    }

    int deepestConcavity(int x1, int y1, int x2, int y2, ofstream *output){
        *output << "++++Entering deepestConcavity Method++++"<<endl;
        double m = (double) (y2-y1) / (double) (x2-x1);
        double b = (double) (y1) -(m*(double) x1);

        int maxGap = 0;
        int first = x1;
        int second = x2;

        int x = first;
        int thr = first;

        while (x<=second){
            //step 1
            int y = (int) (m*x +b);
            //step 3 she misnumbered
            int gap = (abs) (histAry[x]-y);
            //step 4
            if (gap > maxGap){
                maxGap = gap;
                thr = x;
            }
            //step 5
            x++;
        }

       
        *output <<"\tmaxGap: "<<maxGap<<endl;
        *output <<"\tthr: "<<thr<<endl;
         *output << "++++leaving deepestConcavity method++++"<<endl;

        return thr;

    }

    double computeMean(int leftIndex, int rightIndex, int maxHeight, ofstream *output){

        *output<<"\t\t%%%%Entering computeMean method%%%%"<<endl;

        // maxHeight = 0;
        int sum = 0,
            numPixels = 0;


        //Step1
        int index = leftIndex;
        
        //step 5
        while (index < rightIndex){
            //step2
            sum +=(histAry[index] *index);
            numPixels +=histAry[index];
            //step 3
            if (histAry[index] > maxHeight){
                maxHeight = histAry[index];
            }
            //step 4
            index++;
        }

        //step 6
        double result = (double) sum / (double) numPixels;

        // step 7
        
        *output <<"\t\t\tmaxHeight: "<<maxHeight<<endl;
        *output <<"\t\t\tresult: " << result <<endl; 
        *output << "\t\t%%%%Leaving computeMean method maxHeight is an result%%%%%"<<endl;
        return result;

    }

    double computeVar(int leftIndex, int rightIndex, double mean, ofstream *output){
        *output << "\t\t_______Entering computeVar method_______"<<endl;

        int sum = 0, 
            numPixels = 0;

        int index = leftIndex;

        //step 4
        while (index <rightIndex){
            //step 2
            sum+=(double) histAry[index] * (double)pow((double) index - mean,2);

                //((double) index - mean)
                //pow(x-mean,2)

            numPixels +=histAry[index];

            //step 3
            index++;
        }

        //step 5
        double result = sum / (double) numPixels;
        *output << "\t\t\tresult: " << result << endl;
        *output << "\t\t_______Leaving computeVar method returning result_______" << endl;
        return result;

    }


    double modifiedGauss(int x, double mean, double var, int maxHeight){

        // return (double)(maxHeight * exp)
        //g(x) = a* exp (- ((x-b)^2)/(2*c2)))
        /*
            a = max height
            b = mean 
            c2 = var
        **/
       return (double) maxHeight * exp( - ((pow(x-mean,2)/(2*var))));


        // return 2.0;
    }

    double fitGauss(int leftIndex, int rightIndex, int maxHeight, ofstream *output){
        *output << "\t####Entering fitGauss method####"<<endl;

        double mean,
            var, 
            sum = 0.0,
            Gval,
            maxGval;

        //step 1
        mean = computeMean(leftIndex, rightIndex, maxHeight, output);
        var = computeVar(leftIndex, rightIndex, mean, output);

        //step 2
        int index = leftIndex;
        
        while (index<=rightIndex){
            // step 3
            Gval = modifiedGauss(index, mean, var, maxHeight);
            // step 4
            sum += abs(Gval - (double)histAry[index]);
            //step 5
            GaussAry[index] = (int) Gval;
            //step 6
            index ++;
        }
        
        *output << "\t\tsum is: " << sum << endl;
        *output << "\t####leaving fitGauss method####"<<endl;

        return sum;
    }

    int biGauss(int maxHeight, int minVal, int maxVal, ofstream *output){
        
        //step 0
        *output << "^^^^Entering deepestConcavity Method^^^^"<<endl;
        double sum1, sum2, total, minSumDiff;
        int offSet = (int) (maxVal - minVal)/10, 
            dividePt = offSet, 
            bestThr = dividePt;
            minSumDiff = 999999.0;

        
        
        while (dividePt < maxVal - offSet){
    
            // step 1
            setZero(GaussAry, maxVal);
            // step 2
            sum1 = fitGauss(1, dividePt, maxHeight, output);
            //step 3
            sum2 = fitGauss(dividePt, maxVal, maxHeight, output);
            //step 4
            total = sum1 + sum2;
            //step 5
            if (total < minSumDiff){
            minSumDiff = total;
            bestThr = dividePt;
            }
            //step 6
            *output << "\tdividePt is: " << dividePt << endl;
            *output << "\tsum1 is: " << sum1 << endl;
            *output << "\t sum2 is: " << sum2 << endl;
            *output << "\t total is: " << total << endl;
            *output << "\t minSumDiff is: " << minSumDiff << endl;
            *output << "\t bestThr is: " << bestThr << endl;

            //STEP 7
            dividePt ++;
        }


        *output << "minSumDiff: " << minSumDiff<< endl;
        *output << "bestThr" <<bestThr << endl;
        *output << "^^^^leaving biGaussian method, minSumDiff = bestThr is^^^^"<<endl;

        return bestThr;
 

    }

};  

int main(int argc, char *argv[]){


    ifstream input1, input2;
    input1.open(argv[1]);
    input2.open(argv[2]);

    ofstream output, debug;
    output.open(argv[3]);
    debug.open(argv[4]);

    int numRows, numCols, minVal, maxVal;
    input1 >> numRows >> numCols>>minVal>>maxVal;

    int x1, y1, x2, y2;
    input2 >> x1>> y1>> x2>> y2;

    output << numRows<< " " << numCols<< " "<<minVal<< " "<<maxVal << endl;

    int *histAry = new int[maxVal+1];

    for (int i=0;i<maxVal+1; i++){
       histAry[i]=0;
    }

    thresholdSelection *proj1 = new thresholdSelection(numRows, numCols, minVal, maxVal, histAry);

    int maxHeight = proj1->loadHist(input1);
    
    //step 2
    proj1->dispHist(&output);

    output << endl << "the FIRST peak is " << x1 << ", " << y1 <<endl; 
    output << "the SECOND peak is " << x2 << ", " << y2 <<endl <<endl;

    //step 3
    int deepestThrVal = proj1->deepestConcavity(x1, y1, x2, y2, &debug);
    output << "deepestThrVal: " << deepestThrVal <<endl;

    //step 4
    int biGaussVal = proj1->biGauss(maxHeight, minVal, maxVal, &debug);

    output << "biGaussVal: " << biGaussVal << endl;
   
    


    input1.close();
    input2.close();
    output.close();
    debug.close();

    

    


    return 0;
}