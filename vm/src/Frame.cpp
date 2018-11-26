#include "main.h"

using namespace std;

//void Frame::execution(Command &command){
//	
//	Operation op = command.operation;
//	
//	switch(op){
//		
//		case CALL:{
//			_ICALL();
//			break;
//		}
//		
//		default:{
//			cout << "This command doesn't exist: " << op << endl;
//			break;
//		}
//		
//	}
//	
//}

Frame::Frame(Function &function){
	
	this -> function = &function;
	this -> iargs = (int*)malloc(function.intRegsNumber * sizeof(int));
	
	
}

Frame::~Frame(){
	
}

void Frame::doFunc() 
{ 
	while (function -> commands[programCounter].operation != IRET) { 
		switch (function->commands[programCounter].operation) { 
			case IADD: 
			_IADD(); 
			break; 
			
			case ISUB: 
			_ISUB(); 
			break; 
			
			case IMUL: 
			_IMUL(); 
			break; 
			
			case IDIV: 
			_IDIV(); 
			break; 
			
			case IMOD: 
			_IMOD(); 
			break; 
			
			case FADD: 
			_FADD(); 
			break; 
			
			case FSUB: 
			_FSUB(); 
			break; 
			
			case FMUL: 
			_FMUL(); 
			break; 
			
			case FDIV: 
			_FDIV(); 
			break; 
			
			case LAND: 
				_LAND();
			break; 
			
			case LOR: 
				_LOR();
				break; 
			
			case LNOT:
				_LNOT();
				break; 
			
			case IMOV:
				_IMOVE();
				break; 
			
			case FMOV: 
				_FMOVE();
				break; 
			
			case ILOAD: 
				_ILOAD(); 
				break; 
			
			case FLOAD: 
				_FLOAD(); 
				break; 
			
			case ICMPEQ: 
			_ICMPEQ(); 
			break; 
			
			case ICMPNE: 
			_ICMPNE(); 
			break; 
			
			case ICMPBG: 
			_ICMPBG(); 
			break; 
			
			case ICMPLS: 
			_ICMPLS(); 
			break; 
			
			case ICMPBE: 
			_ICMPBE(); 
			break; 
			
			case ICMPGE: 
			_ICMPGE(); 
			break; 
			
			// 
			case FCMPEQ: 
			_FCMPEQ(); 
			break; 
			
			case FCMPNE: 
			_FCMPNE(); 
			break; 
			
			case FCMPBG: 
			_FCMPBG(); 
			break; 
			
			case FCMPLS: 
			_FCMPLS(); 
			break; 
			
			case FCMPBE: 
			_FCMPBE(); 
			break; 
			
			case FCMPGE: 
			_FCMPGE(); 
			break; 
			
			case GOTO: 
			_GOTO(); 
			break; 
			
			case IF: 
			_IF(); 
			break; 
			
			case IRET: 
			break; 
			
			case FRET: 
			break; 
			
			case WRITE_INT: 
			_WRITE_INT(); 
			break; 
			
			case WRITE_FLOAT: 
			_WRITE_FLOAT(); 
			break; 
			
			case READ_INT: 
			_READ_INT(); 
			break; 
			
			case READ_FLOAT: 
			_READ_FLOAT(); 
			break; 
			
			case ICALL: 
			_ICALL(); 
			break; 
			} 
		
		programCounter++; 

		if (programCounter == this->function->commandsNumber) break; 
	
}
}

void Frame::_ICALL(){
	Command *command = &function->commands[programCounter]; 

	for(int i = 0 ; i < BYTECODE -> functionsNumber; ++i){

		if(!strcmp(BYTECODE -> functions[i].name,command -> funcName)){

			Frame nextFrame(BYTECODE -> functions[i]);

			for(int j = 0; j < command -> argsCount; ++j){

				nextFrame.iargs[j] = iargs[command -> args[j]];

			}

			nextFrame.doFunc();

		}
	}
}

void Frame::_IADD(){ 
	Command *command = &function->commands[programCounter]; 
	int *args = command->args; 
	iargs[command->result] = iargs[args[0]] + iargs[args[1]]; 
} 

void Frame::_ISUB(){ 
	Command *command = &function->commands[programCounter]; 
	int *args = command->args; 
	iargs[command->result] = iargs[args[0]] - iargs[args[1]]; 
} 

void Frame::_IMUL(){ 
	Command *command = &function->commands[programCounter]; 
	int *args = command->args; 
	iargs[command->result] = iargs[args[0]] * iargs[args[1]]; 
} 

void Frame::_IDIV(){ 
	Command *command = &function->commands[programCounter]; 
	int *args = command->args; 
	iargs[command->result] = iargs[args[0]] / iargs[args[1]]; 
} 

void Frame::_IMOD(){ 
	Command *command = &function->commands[programCounter]; 
	int *args = command->args; 
	iargs[command->result] = iargs[args[0]] % iargs[args[1]]; 
} 

void Frame::_FADD(){ 
	Command *command = &function->commands[programCounter]; 
	int *args = command->args; 
	fargs[command->result] = fargs[args[0]] + fargs[args[1]]; 
} 

void Frame::_FSUB(){ 
	Command *command = &function->commands[programCounter]; 
	int *args = command->args; 
	fargs[command->result] = fargs[args[0]] - fargs[args[1]]; 
} 

void Frame::_FMUL(){ 
	Command *command = &function->commands[programCounter]; 
	int *args = command->args; 
	fargs[command->result] = fargs[args[0]] * fargs[args[1]]; 
} 

void Frame::_FDIV(){ 
	Command *command = &function->commands[programCounter]; 
	int *args = command->args; 
	fargs[command->result] = fargs[args[0]] / fargs[args[1]]; 
} 

void Frame::_ICMPEQ(){ 
	Command *command = &function->commands[programCounter]; 
	if(iargs[command -> args[0]] == iargs[command -> args[1]]){ 
		iargs[command -> result] = 1; 
	} else { 
		iargs[command -> result] = 0; 
	} 
} 

void Frame::_ICMPNE(){ 
	Command *command = &function->commands[programCounter]; 
	if(iargs[command -> args[0]] != iargs[command -> args[1]]){ 
		iargs[command -> result] = 1; 
	} else { 
		iargs[command -> result] = 0; 
	} 
} 

void Frame::_ICMPBG(){ 
	Command *command = &function->commands[programCounter]; 
	if(iargs[command -> args[0]] > iargs[command -> args[1]]){ 
		iargs[command -> result] = 1; 
	} else { 
		iargs[command -> result] = 0; 
	} 
} 

void Frame::_ICMPLS(){ 
	Command *command = &function->commands[programCounter]; 
	if(iargs[command -> args[0]] < iargs[command -> args[1]]){ 
		iargs[command -> result] = 1; 
	} else { 
		iargs[command -> result] = 0; 
	} 
} 

void Frame::_ICMPBE(){ 
	Command *command = &function->commands[programCounter]; 
	if(iargs[command -> args[0]] >= iargs[command -> args[1]]){ 
		iargs[command -> result] = 1; 
	} else { 
		iargs[command -> result] = 0; 
	} 
} 

void Frame::_ICMPGE(){ 
	Command *command = &function->commands[programCounter]; 
	if(iargs[command -> args[0]] <= iargs[command -> args[1]]){ 
		iargs[command -> result] = 1; 
	} else { 
		iargs[command -> result] = 0; 
	} 
} 

void Frame::_FCMPGE(){ 
	Command *command = &function->commands[programCounter]; 
	if(fargs[command -> args[0]] <= fargs[command -> args[1]]){ 
		fargs[command -> result] = 1; 
	} else { 
		fargs[command -> result] = 0; 
	} 
} 

void Frame::_FCMPBE(){ 
	Command *command = &function->commands[programCounter]; 
	if(fargs[command -> args[0]] >= fargs[command -> args[1]]){ 
		fargs[command -> result] = 1; 
	} else { 
		fargs[command -> result] = 0; 
	} 
} 

void Frame::_FCMPLS(){ 
	Command *command = &function->commands[programCounter]; 
	if(fargs[command -> args[0]] < fargs[command -> args[1]]){ 
	fargs[command -> result] = 1; 
	} else { 
	fargs[command -> result] = 0; 
	} 
} 

void Frame::_FCMPBG(){ 
	Command *command = &function->commands[programCounter]; 
	if(fargs[command -> args[0]] > fargs[command -> args[1]]){ 
		fargs[command -> result] = 1; 
	} else { 
		fargs[command -> result] = 0; 
	} 
} 

void Frame::_FCMPNE(){ 
	Command *command = &function->commands[programCounter]; 
	if(fargs[command -> args[0]] != fargs[command -> args[1]]){
		fargs[command -> result] = 1; 
	} else { 
		fargs[command -> result] = 0; 
	} 
} 

void Frame::_FCMPEQ(){ 
	Command *command = &function->commands[programCounter]; 
	if(fargs[command -> args[0]] == fargs[command -> args[1]]){ 
		fargs[command -> result] = 1; 
	} else { 
		fargs[command -> result] = 0; 
	} 
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
	if(iargs[command -> args[0]]) _GOTO(command->args[1]); 
} 

void Frame::_WRITE_INT(){ 
	Command *command = &(function->commands[programCounter]);
	std::cout << iargs[command -> args[0]] << std::flush; 
} 

void Frame::_WRITE_FLOAT(){ 
	Command *command = &function->commands[programCounter]; 
	std::cout << fargs[command -> args[0]] << std::flush; 
} 

void Frame::_READ_INT(){ 
	Command *command = &function->commands[programCounter]; 
	std::cin >> iargs[command -> result]; 
} 

void Frame::_READ_FLOAT(){ 
	Command *command = &function->commands[programCounter]; 
	std::cin >> fargs[command -> result]; 
} 

void Frame::_ILOAD(){ 
	Command *command = &function->commands[programCounter]; 
	iargs[command->result] = command->intConst; 
} 

void Frame::_FLOAD(){ 
	Command *command = &function->commands[programCounter]; 
	fargs[command->result] = command->floatConst; 
}

void Frame::_IMOVE() {
	Command *command = &function->commands[programCounter];
	iargs[command->result] = iargs[command->args[0]];
}

void Frame::_FMOVE() {
	Command *command = &function->commands[programCounter];
	fargs[command->result] = fargs[command->args[0]];
}

void Frame::_LNOT() {
	Command *command = &function->commands[programCounter];
	if (!iargs[command->args[0]]) iargs[command->result] = 1;
	else iargs[command->result] = 0;
}

void Frame::_LAND() {
	Command *command = &function->commands[programCounter];
	if (iargs[command->args[0]] && iargs[command->args[1]]) iargs[command->result] = 1;
	else iargs[command->result] = 0;
}

void Frame::_LOR() {
	Command *command = &function->commands[programCounter];
	if (iargs[command->args[0]] || iargs[command->args[1]]) iargs[command->result] = 1;
	else iargs[command->result] = 0;
}
