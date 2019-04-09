#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <stdlib.h>
#include "Bytecode.h"
#include "Frame.h"

/* run this program using the console pauser or add your own getch, system("pause") or input loop */
using namespace std;
void ips();

Bytecode *_BYTECODE;


int main(int argc, char** argv) {    
    if (argc != 2) {
        std::cout << "bad arguments" << std::endl;
        return 0;
    }else{
        _BYTECODE = read_bytecode(argv[1]);
    }
    ips();
    system("pause");
    return 0;
}

void ips(){
    for(int i = 0 ; i < _BYTECODE -> functionsNumber; ++i){

    //    cout << i << endl;

        if(!strcmp("main",_BYTECODE -> functions[i].name)) {

     //       cout << "main" << endl;
            Frame firstFrame(_BYTECODE -> functions[i]);
            firstFrame.execute();
            break;

        }
    }
    
}