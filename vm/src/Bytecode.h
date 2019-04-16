#pragma once

enum Operation {
    IADD, 
	ISUB, 
	IMUL, 
	IDIV, 
	IMOD, 
	FADD, 
	FSUB, 
	FMUL, 
	FDIV, 
	LAND, 
	LOR, 
	LNOT, 
	
	FMOV, 
	IMOV, 
	ILOAD, 
	FLOAD, 
	
	ICMPEQ, 
	ICMPNE,
	ICMPBG,
	ICMPLS,
	ICMPBE,
	ICMPGE,
	
	FCMPEQ,
	FCMPNE,
	FCMPBG,
	FCMPLS,
	FCMPBE, 
	FCMPGE, 
	
	GOTO, // 
	IF, 
    RET, 

	WRITE_INT, 
	WRITE_FLOAT, 
	READ_INT, 
	READ_FLOAT, 
	ICALL, 
	FCALL,
	WRITE_STR,

    NEW,
    GETFIELD,
    SETFIELD
};


struct Command {
    Operation operation;

    int argsCount;
    int* args;

    int result;

    int intConst;
    float floatConst;
    char* strConst1 = NULL;
    char* strConst2 = NULL;
    char* funcName;
};

struct Function {
    char* name;
    int pointersNumber;
    int intRegsNumber;
    int floatRegsNumber;
    int commandsNumber;
    Command* commands;
};

struct Field {
	char *name;
	char *className;
	char *type;
	int offset;
};

struct Class {
	char *name;
	int fieldsCounter;
	Field *fields;
};

struct Bytecode {
    int functionsNumber;
    Function* functions;
    int classNumber;
    Class *classes;
};

Bytecode* read_bytecode(std::string name);
void bytecode_writer(Bytecode & B);