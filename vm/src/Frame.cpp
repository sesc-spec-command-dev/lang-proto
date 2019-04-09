#include "main.h"

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
    while (function -> commands[programCounter].operation != RET) { 
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
            case RET: break;      break;
            case ICALL: _ICALL(); break;
            case WRITE_INT: _WRITE_INT();     break;
            case WRITE_FLOAT: _WRITE_FLOAT(); break; 
            case READ_INT: _READ_INT();		  break;
            case READ_FLOAT: _READ_FLOAT();	  break;
			case NEW: _NEW(); break;
			case SETFIELD: _SETFIELD(); break;
			case GETFIELD: _GETFIELD(); break;
             
            } 
        programCounter++; 

        if (programCounter == this->function->commandsNumber) {
            throw exception("No ret func");
        }
    
}
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

}

void Frame::_GETFIELD()
{
	Command *command = &function->commands[programCounter];
	int *args = command->args;
	Field *field = NULL;
	const char *class_name = command->strConst1;
	const char* field_name = command->strConst2;

	for (int i = 0; i < _BYTECODE->classNumber; ++i) {
		Class *curr_class = &_BYTECODE->classes[i];
		const char *curr_class_name = curr_class->name;
		if (!strcmp(curr_class_name, class_name)) {
			for (int j = 0; j < curr_class->fieldsCounter; ++j) {
				Field *curr_field = &curr_class->fields[j];
				const char *curr_field_name = curr_field->name;

				if (!strcmp(field_name, curr_field_name)) {
					field = curr_field;
					break;
				}
			}
			break;
		}
	}

	if(field != NULL){
		
	}
	else throw("");
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
    fRegs[command->result] = (fRegs[command->args[0]] == fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_FCMPBE(){ 
    Command *command = &function->commands[programCounter]; 
    fRegs[command->result] = (fRegs[command->args[0]] >= fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_FCMPLS(){ 
    Command *command = &function->commands[programCounter]; 
    fRegs[command->result] = (fRegs[command->args[0]] < fRegs[command->args[1]]) ? 1 : 0;

} 

void Frame::_FCMPBG(){ 
    Command *command = &function->commands[programCounter]; 
    fRegs[command->result] = (fRegs[command->args[0]] > fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_FCMPNE(){ 
    Command *command = &function->commands[programCounter]; 
    fRegs[command->result] = (fRegs[command->args[0]] != fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_FCMPEQ(){ 
    Command *command = &function->commands[programCounter]; 
    fRegs[command->result] = (fRegs[command->args[0]] == fRegs[command->args[1]]) ? 1 : 0;
} 

void Frame::_GOTO(){ 
    Command *command = &function->commands[programCounter]; 
    programCounter = command -> intConst; 
} 

void Frame::_GOTO(int line){
    programCounter = line; 
} 

void Frame::_IF(){ 
    Command *command = &function->commands[programCounter]; 
    if(iRegs[command -> args[0]]) _GOTO(command->args[1]); 
} 

void Frame::_WRITE_INT(){ 
    Command *command = &(function->commands[programCounter]);
    std::cout << iRegs[command -> args[0]] << std::flush; 
} 

void Frame::_WRITE_FLOAT(){ 
    Command *command = &function->commands[programCounter]; 
    std::cout << fRegs[command -> args[0]] << std::flush; 
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