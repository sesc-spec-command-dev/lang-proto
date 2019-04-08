#include <iostream>
#include <stdlib.h>
#include "Bytecode.h"
#include "Frame.h"

/* run this program using the console pauser or add your own getch, system("pause") or input loop */
using namespace std;
void ips();

Bytecode *_BYTECODE;
Bytecode *test() {
	Bytecode *bytecode = new Bytecode;
	Function *functions = new Function[2];
	Command *c1 = new Command[3];
	Command *c2 = new Command[4];

	c1[0].operation = IADD;
	c1[0].argsCount = 2;
	c1[0].args = new int[2];
	c1[0].args[0] = 0;
	c1[0].args[1] = 1;
	c1[0].result = 0;

	c1[1].operation = WRITE_INT;
	c1[1].args = new int[1];
	c1[1].args[0] = 0;

	c1[2].operation = IRET;

	functions[1].name = "lol";
	functions[1].commands = c1;
	functions[1].commandsNumber = 3;
	functions[1].intRegsNumber = 2;
	functions[1].floatRegsNumber = 0;

	c2[0].operation = READ_INT;
	c2[0].result = 0;

	c2[1].operation = READ_INT;
	c2[1].result = 1;

	c2[2].operation = ICALL;
	c2[2].argsCount = 2;
	c2[2].args = new int[2];
	c2[2].args[0] = 0;
	c2[2].args[1] = 1;
	c2[2].funcName = "lol";

	c2[3].operation = IRET;

	functions[0].name = "main";
	functions[0].commands = c2;
	functions[0].intRegsNumber = 2;
	functions[0].floatRegsNumber = 0;
	functions[0].commandsNumber = 4;


	bytecode->functions = functions;
	bytecode->functionsNumber = 2;
	return bytecode;
}
/*
int main(int argc, char** argv) {    
    _BYTECODE = test();
    ips();
    system("pause");
    return 0;
}*/

void ips(){
    for(int i = 0 ; i < _BYTECODE -> functionsNumber; ++i){

        cout << i << endl;

        if(!strcmp("main",_BYTECODE -> functions[i].name)) {

            cout << "main" << endl;
            Frame firstFrame(_BYTECODE -> functions[i]);
            firstFrame.execute();
            break;

        }
    }
    
}