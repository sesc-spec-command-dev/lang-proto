enum Operation {
    IADD,
    ...
    CALL
};

struct Command {
    Operation operation;

    int argsCount;
    int* args;

    int result;

    // special fields for some commands
    int intConst;      // ILOAD
    float floatConst;  // FLOAD
    const char* strConst;    // WRITE_STR
    const char* funcName;    // CALL
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
};
