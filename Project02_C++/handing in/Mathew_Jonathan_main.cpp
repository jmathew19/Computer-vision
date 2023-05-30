#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <cstring>
using namespace std;


int main(int argc, const char* argv[]){


    ifstream inputFile;
    inputFile.open(argv[1]);

    if (!inputFile.is_open()) {
             cout << "Unable to open file" << endl;
             exit(1);
    }

    ofstream outFile;
    outFile.open(argv[2]);

    int numRow, numCol, minVal, maxVal, thrVal;
    
    inputFile>>numRow>>numCol>>minVal>>maxVal;

    outFile<< numRow << " " << numCol << " " << minVal <<  " " << maxVal<<"\n";
    string str;
    getline(inputFile, str);
    // cout <<str;
    string sa;
    cout << "Enter a threshold value: ";
    cin >> thrVal;
    // cout << thrVal;
    while (getline(inputFile, sa)) { 
        char *str= new char[sa.length()+1];
        strcpy(str, sa.c_str());

        const char delim[] = " ";
        char *token = strtok(str, delim);

        while (token != NULL) {
            // cout << token;
            // outFile << token << " ";
            stringstream obj;  
            int pixel;
            obj << token; // insert data into obj  
            obj >> pixel; // fetch integer type data  
            if(thrVal <= pixel){
                outFile << pixel << " ";
            }else{
                outFile << "0 ";
            }
            token = strtok(NULL, delim);
        }
        outFile << "\n";
        delete[] str;
        
    }

}



