#include "Bytecode.h"
#include "Frame.h"

/* run this program using the console pauser or add your own getch, system("pause") or input loop */
using namespace std;
void ips();

Bytecode *_BYTECODE;
Bytecode *test2() {
	Bytecode *bytecode = new Bytecode;
	Function *functions = new Function[1];
	Command *c1 = new Command[6];
	
	// Описание класса
	Class *classes = new Class[1];
	Field *fields = new Field[1];
	fields[0].name = "a";
	fields[0].className = "Foo";
	fields[0].type = "int";
	fields[0].offset = 0;
	classes[0].name = "Foo";
	classes[0].fieldsCounter = 1;
	classes[0].fields = fields;
	
	//Описание функции: присвоить константу полю, из поля вынести в третий регистр, вывести на экран
	functions[0].pointersNumber = 1;
	functions[0].intRegsNumber = 2;
	functions[0].floatRegsNumber = 0;
	functions[0].name = (char*)"main";

	c1[0].operation = ILOAD;
	c1[0].intConst = 2703;
	c1[0].result = 0;

	c1[1].operation = NEW;
	c1[2].argsCount = 1;
	c1[1].args = new int[1];
	c1[1].args[0] = 0;
	c1[1].result = 0;
	c1[1].strConst1 = (char*)"Foo";

	c1[2].operation = SETFIELD;
	c1[2].argsCount = 2;
	c1[2].args = new int[2];
	c1[2].args[0] = 0;
	c1[2].args[1] = 0;
	c1[2].strConst1 = (char*)"Foo";
	c1[2].strConst2 = (char*)"a";

	c1[3].operation = GETFIELD;
	c1[3].argsCount = 2;
	c1[3].args = new int[2];
	c1[3].args[0] = 0;
	c1[3].args[1] = 1;
	c1[3].strConst1 = (char*)"Foo";
	c1[3].strConst2 = (char*)"a";

	c1[4].operation = WRITE_INT;
	c1[4].argsCount = 1;
	c1[4].args = new int[1];
	c1[4].args[0] = 1;

	c1[5].operation = RET;

	functions[0].commandsNumber = 6;
	functions[0].commands = c1;
	bytecode->functions = functions;
	bytecode->functionsNumber = 1;
	bytecode->classes = classes;
	bytecode->classNumber = 1;

	return bytecode;
}
Bytecode *test1() {
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

	c1[2].operation = RET;

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
	c2[2].funcName = (char*)"lol";

	c2[3].operation = RET;

	functions[0].name = "main";
	functions[0].commands = c2;
	functions[0].intRegsNumber = 2;
	functions[0].floatRegsNumber = 0;
	functions[0].commandsNumber = 4;


	bytecode->functions = functions;
	bytecode->functionsNumber = 2;
	return bytecode;
}

int main(int argc, char** argv) {    
    _BYTECODE = test2();
    ips();
    system("pause");
    return 0;
}

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