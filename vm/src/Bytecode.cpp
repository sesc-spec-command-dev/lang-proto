#include "main.h"
void Bytecode::findFunction(Command *command) {
    for (int i = 0; i < BYTECODE->functionsNumber; ++i) {
        if (BYTECODE->functions[i].name == command->funcName) {
            Frame nextFrame(BYTECODE->functions[i]);
            nextFrame.execute();
            break;
        }
    }
}