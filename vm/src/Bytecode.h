
#pragma once
#include "main.h"
enum Operation{
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
    
    GOTO,  
    IF, 
    RET, 
    WRITE_INT, 
    WRITE_FLOAT, 
    READ_INT, 
    READ_FLOAT, 
    ICALL, 
    FCALL 
};

struct Command {
    Operation operation;

    int argsCount;
    int* args;

    int result;

    int intConst;
    float floatConst;
    const char* strConst;
    const char* funcName;
};

struct Function {
    const char* name;
    int intRegsNumber;
    int floatRegsNumber;
    int commandsNumber;
    Command* commands;
};

struct Bytecode {
    int functionsNumber;
    Function* functions;
    public: 
        void findFunction(Command *command);
};

