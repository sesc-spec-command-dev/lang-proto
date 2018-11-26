#include "main.h"
/* run this program using the console pauser or add your own getch, system("pause") or input loop */
using namespace std;
void ips();
int main(int argc, char** argv) {
	
	Bytecode *bytecode = (Bytecode*)malloc(sizeof(Bytecode));
	Function *functions = (Function*)malloc(sizeof(Function) * 2);
	Command *c1 = (Command*)malloc(sizeof(Command) * 2);
	Command *c2 = (Command*)malloc(sizeof(Command) * 3);
	/*commands[0].operation = READ_INT;
	commands[0].result = 0;
	
	commands[1].operation = READ_INT;
	commands[1].result = 1;*/
	
	c1[0].operation = IADD;
	c1[0].args = (int*)malloc(sizeof(int) * 2);
	c1[0].args[0] = 0;		
	c1[0].args[1] = 1;
	c1[0].result = 0;
	
	c1[1].operation = WRITE_INT;
	c1[1].args = (int*)malloc(sizeof(int) * 1);
	c1[1].args[0] = 0;
	
	functions[1].name = "lol";
	functions[1].commands = c1;
	functions[1].commandsNumber = 2;
	functions[1].intRegsNumber = 2;
	functions[1].floatRegsNumber = 0;
	
	//
	
	c2[0].operation = READ_INT;
	c2[0].result = 0;

	c2[1].operation = READ_INT;
	c2[1].result = 1;

	c2[2].operation = ICALL;
	c2[2].argsCount = 2;
	c2[2].args = (int*)malloc(sizeof(int) * 2);
	c2[2].args[0] = 0;
	c2[2].args[1] = 1;
	c2[2].funcName = "lol";

	functions[0].name = "main";
	functions[0].commands = c2;
	functions[0].intRegsNumber = 2;
	functions[0].floatRegsNumber = 0;
	functions[0].commandsNumber = 3; 
	

	bytecode->functions = functions;
	bytecode->functionsNumber = 2;
	
	
	BYTECODE = bytecode;
	ips();
	system("pause");
	return 0;
}

void ips(){
	for(int i = 0 ; i < BYTECODE -> functionsNumber; ++i){

		cout << i << endl;

		if(!strcmp("main",BYTECODE -> functions[i].name)) {

			cout << "main" << endl;
			Frame firstFrame(BYTECODE -> functions[i]);
			firstFrame.doFunc();
			break;

		}
	}
	
}