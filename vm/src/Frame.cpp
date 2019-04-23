#include <iostream>
#include <stdlib.h>
#include "Bytecode.h"
#include "Frame.h"

using namespace std;

Frame::Frame(Function &function){
    
    this->function = &function;
    this->iRegs = new int[function.intRegsNumber];
    this->fRegs = new float[function.floatRegsNumber];
	this->pRegs = new void*[function.pointersNumber];
}

Frame::~Frame(){
    
}

void Frame::execute() 
{ 
    while (true) { 
        switch (function->commands[programCounter].operation) { 
            case IADD:	_IADD();  break; 
            case ISUB:	_ISUB();  break; 
            case IMUL:	_IMUL();  break; 
            case IDIV:	_IDIV();  break; 
            case IMOD:	_IMOD();  break; 
            case FADD:	_FADD();  break; 
            case FSUB:	_FSUB();  break;
            case FMUL:	_FMUL();  break; 
            case FDIV:	_FDIV();  break;
            case LAND:	_LAND();  break; 
            case LOR:	_LOR();   break; 
            case LNOT:	_LNOT();  break; 
            case IMOV:	_IMOVE(); break; 
            case FMOV:	_FMOVE(); break; 
            case ILOAD: _ILOAD(); break; 
            case FLOAD: _FLOAD(); break; 
            case ICMPEQ: _ICMPEQ(); break; 
            case ICMPNE: _ICMPNE(); break; 
            case ICMPBG: _ICMPBG(); break; 
            case ICMPLS: _ICMPLS(); break; 
            case ICMPBE: _ICMPBE(); break; 
            case ICMPGE: _ICMPGE(); break; 
            case FCMPEQ: _FCMPEQ(); break;
            case FCMPNE: _FCMPNE(); break; 
            case FCMPBG: _FCMPBG(); break; 
            case FCMPLS: _FCMPLS(); break; 
            case FCMPBE: _FCMPBE(); break; 
            case FCMPGE: _FCMPGE(); break; 
            case GOTO: _GOTO();   break; 
            case IF: _IF();       break; 
            case ICALL: _ICALL(); break;
            case WRITE_INT: _WRITE_INT();     break;
            case WRITE_FLOAT: _WRITE_FLOAT(); break; 
            case WRITE_STR: _WRITE_STR(); break;
            case READ_INT: _READ_INT();		  break;
            case READ_FLOAT: _READ_FLOAT();	  break;
			case NEW: _NEW(); break;
			case SETFIELD: _SETFIELD(); break;
			case GETFIELD: _GETFIELD(); break;

            case RET: return;
        }
        programCounter++; 

        if (programCounter == this->function->commandsNumber) {
            throw exception("No ret func");
        }
    }
}

int find_offset(char* class_name, char* field_name) {
	Bytecode *bytecode = _BYTECODE;
	int offset = -1;

	for (int i = 0; i < bytecode->classNumber; i++) {
		if (!strcmp(bytecode->classes[i].name, class_name)) {
			for (int j = 0; j < bytecode->classes[i].fieldsCounter; j++) {
				if (!strcmp(bytecode->classes[i].fields[j].name, field_name)) {
					offset = bytecode->classes[i].fields[j].offset;
					break;
				}
			}
			break;
		}
	}

	return offset;
}

Function findFunction(Command *command) {
	Function f;
	f.name = NULL;
	for (int i = 0; i < _BYTECODE->functionsNumber; ++i) {
		if (!strcmp(_BYTECODE->functions[i].name,command->funcName)) {
			return _BYTECODE->functions[i];
		}
	}
	return f;
}

void Frame::_ICALL() {
	Command *command = &function->commands[programCounter];
	Function func = (findFunction(command));
	if (func.name == NULL)	
	{
		throw exception("Can't find that func"); 
		return;
	}
    Frame nextFrame(func);
	for (int i = 0; i < func.intRegsNumber; ++i) {
		nextFrame.iRegs[i] = this->iRegs[i];
	}
    nextFrame.execute();
	this->iRegs[command->result] = nextFrame.iRegs[0];
}

void Frame::_NEW()
{
	Command *command = &function->commands[programCounter];
	Bytecode *bytecode = _BYTECODE;
	void *p = NULL;
	for (int i = 0; i < bytecode->classNumber; i++) {
		if (!strcmp(bytecode->classes[i].name, command->strConst1)) {
			p = malloc(sizeof(int) * bytecode->classes[i].fieldsCounter);
		}
	}
	pRegs[command->result] = p;
}

void Frame::_SETFIELD()
{
	Command *command = &function->commands[programCounter];
	char* class_name = command->strConst1;
	char* field_name = command->strConst2;
	int offset = find_offset(class_name, field_name);
	if (offset == -1) throw ("SETFIELD%OffsetNullError");
	int *p = (int*)(pRegs[command->args[0]]);
	p[(int)(offset/sizeof(int))] = iRegs[command->result];	// поменять на args[1]

}

void Frame::_GETFIELD()
{
	Command *command = &function->commands[programCounter];
	char *class_name = command->strConst1;
	char* field_name = command->strConst2;
	int offset = find_offset(class_name, field_name);
	if (offset == -1) throw ("GETFIELD%OffsetNullError");
	int *p = (int*)(pRegs[command->args[0]]);
	this->iRegs[(int)(command->result)] = (int)(p[(offset / sizeof(int))]);	// поменять на args[1]
}


void Frame::_IADD(){ 
    Command *command = &function->commands[programCounter]; 
    int *args = command->args; 
    iRegs[command->result] = iRegs[args[0]] + iRegs[args[1]]; 
} 

void Frame::_ISUB(){ 
    Command *command = &function->commands[programCounter]; 
    int *args = command->args; 
    iRegs[command->result] = iRegs[args[0]] - iRegs[args[1]]; 
} 

void Frame::_IMUL(){ 
    Command *command = &function->commands[programCounter]; 
    int *args = command->args; 
    iRegs[command->result] = iRegs[args[0]] * iRegs[args[1]]; 
} 

void Frame::_IDIV(){ 
    Command *command = &function->commands[programCounter]; 
    int *args = command->args; 
    iRegs[command->result] = iRegs[args[0]] / iRegs[args[1]]; 
} 

void Frame::_IMOD(){ 
    Command *command = &function->commands[programCounter]; 
    int *args = command->args; 
    iRegs[command->result] = iRegs[args[0]] % iRegs[args[1]]; 
} 

void Frame::_FADD(){ 
    Command *command = &function->commands[programCounter]; 
    int *args = command->args; 
    fRegs[command->result] = fRegs[args[0]] + fRegs[args[1]]; 
} 

void Frame::_FSUB(){ 
    Command *command = &function->commands[programCounter]; 
    int *args = command->args; 
    fRegs[command->result] = fRegs[args[0]] - fRegs[args[1]]; 
} 

void Frame::_FMUL(){ 
    Command *command = &function->commands[programCounter]; 
    int *args = command->args; 
    fRegs[command->result] = fRegs[args[0]] * fRegs[args[1]]; 
} 

void Frame::_FDIV(){ 
    Command *command = &function->commands[programCounter]; 
    int *args = command->args; 
    fRegs[command->result] = fRegs[args[0]] / fRegs[args[1]]; 
} 

void Frame::_ICMPEQ(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (iRegs[command->args[0]] == iRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_ICMPNE(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (iRegs[command->args[0]] != iRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_ICMPBG(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (iRegs[command->args[0]] > iRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_ICMPLS(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (iRegs[command->args[0]] < iRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_ICMPBE(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (iRegs[command->args[0]] >= iRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_ICMPGE(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (iRegs[command->args[0]] <= iRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_FCMPGE(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (fRegs[command->args[0]] == fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_FCMPBE(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (fRegs[command->args[0]] >= fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_FCMPLS(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (fRegs[command->args[0]] < fRegs[command->args[1]]) ? 1 : 0;

} 

void Frame::_FCMPBG(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (fRegs[command->args[0]] > fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_FCMPNE(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (fRegs[command->args[0]] != fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_FCMPEQ(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = (fRegs[command->args[0]] == fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_GOTO(){ 
    Command *command = &function->commands[programCounter]; 
    programCounter = (command -> intConst) - 1; 
} 

void Frame::_GOTO(int line){
    programCounter = line - 1; // - 1 обусловленно тем, что при выполнении execute() после вызова _GOTO мы прибавим 1 к programCounter 
} 

void Frame::_IF(){ 
    Command *command = &function->commands[programCounter]; 
    if(iRegs[command -> args[0]]) _GOTO(command->args[1]); 
} 

void Frame::_WRITE_INT(){ 
    Command *command = &(function->commands[programCounter]);
    std::cout << iRegs[command -> intConst] << std::flush; 
} 

void Frame::_WRITE_FLOAT() {
    Command *command = &function->commands[programCounter];
    std::cout << fRegs[command -> intConst] << std::flush;
}

void Frame::_WRITE_STR() {
    Command *command = &function->commands[programCounter];
    std::cout << command->strConst1 << std::flush;
}

void Frame::_READ_INT(){
    Command *command = &function->commands[programCounter]; 
    std::cin >> iRegs[command -> result]; 
} 

void Frame::_READ_FLOAT(){ 
    Command *command = &function->commands[programCounter]; 
    std::cin >> fRegs[command -> result]; 
} 

void Frame::_ILOAD(){ 
    Command *command = &function->commands[programCounter]; 
    iRegs[command->result] = command->intConst; 
} 

void Frame::_FLOAD(){ 
    Command *command = &function->commands[programCounter]; 
    fRegs[command->result] = command->floatConst; 
}

void Frame::_IMOVE() {
    Command *command = &function->commands[programCounter];
    iRegs[command->result] = iRegs[command->args[0]];
}

void Frame::_FMOVE() {
    Command *command = &function->commands[programCounter];
    fRegs[command->result] = fRegs[command->args[0]];
}

void Frame::_LNOT() {
    Command *command = &function->commands[programCounter];
    iRegs[command->result] = (!iRegs[command->args[0]]) ? 255 : 0;
}

void Frame::_LAND() {
    Command *command = &function->commands[programCounter];
    iRegs[command->result] = (iRegs[command->args[0]] && iRegs[command->args[1]]) ? 1 : 0;
}

void Frame::_LOR() {
    Command *command = &function->commands[programCounter];
    iRegs[command->result] = (iRegs[command->args[0]] || iRegs[command->args[1]]) ? 1 : 0;
}
