#define _CRT_SECURE_NO_WARNINGS

#include <fstream>
#include "Bytecode.h"
#include <string>
#include <stdlib.h>
#include <iostream>
#include <vector>
#include <assert.h>
#include "Reader.h"

Operation operationByName(std::string str) {
    if (str == "IADD")       return IADD;
    if (str == "ISUB")       return ISUB;
    if (str == "IMUL")       return IMUL;
    if (str == "IDIV")       return IDIV;
    if (str == "IMOD")       return IMOD;
    if (str == "FADD")       return FADD;
    if (str == "FSUB")       return FSUB;
    if (str == "FMUL")       return FMUL;
    if (str == "FDIV")       return FDIV;
    if (str == "LAND")       return LAND;
    if (str == "LOR")        return LOR;
    if (str == "LNOT")       return LNOT;
    if (str == "IMOV")       return IMOV;
    if (str == "ILOAD")      return ILOAD;
    if (str == "FMOV")       return FMOV;
    if (str == "FLOAD")      return FLOAD;
    if (str == "ICMPEQ")     return ICMPEQ;
    if (str == "ICMPNE")     return ICMPNE;
    if (str == "ICMPBG")     return ICMPBG;
    if (str == "ICMPLS")     return ICMPLS;
    if (str == "ICMPBE")     return ICMPBE;
    if (str == "ICMPGE")     return ICMPGE;
    if (str == "FCMPEQ")     return FCMPEQ;
    if (str == "FCMPNE")     return FCMPNE;
    if (str == "FCMPBG")     return FCMPBG;
    if (str == "FCMPLS")     return FCMPLS;
    if (str == "FCMPBE")     return FCMPBE;
    if (str == "FCMPGE")     return FCMPGE;
    if (str == "GOTO")       return GOTO;
    if (str == "IF")         return IF;
    if (str == "RET")       return RET;
    if (str == "WRITE_INT")  return WRITE_INT;
    if (str == "WRITE_FLOAT")return WRITE_FLOAT;
    if (str == "READ_INT")   return READ_INT;
    if (str == "READ_FLOAT") return READ_FLOAT;
    if (str == "ICALL")      return ICALL;
    if (str == "FCALL")      return FCALL;
    if (str == "WRITE_STR")  return WRITE_STR;
    if (str == "SETFIELD")   return SETFIELD;
    if (str == "GETFIELD")   return GETFIELD;
    if (str == "NEW")  		 return NEW;
    assert(false);
}

int specialArgsType(Operation op) {
    switch (op) {
    case WRITE_INT:
    case READ_INT:
    case GOTO:
    case IF:
        return 1;
    case WRITE_FLOAT:
    case READ_FLOAT:
        return 2;
    case ICALL:
    case FCALL:
        return 3;
    case WRITE_STR:
        return 4;
    case SETFIELD:
    case GETFIELD:
        return 5;
    case NEW:
        return 6;
    case ILOAD:
        return 7;
    case FLOAD:
        return 8;
    default:
        return 0;
    }
}

int defaultArgsCount(Operation op) {
    switch (op) {
    case IADD:
    case ISUB:
    case IMUL:
    case IDIV:
    case IMOD:
    case FADD:
    case FSUB:
    case FMUL:
    case FDIV:
    case LAND:
    case LOR:
    case LNOT:
    case ICMPEQ:
    case ICMPNE:
    case ICMPBG:
    case ICMPLS:
    case ICMPBE:
    case ICMPGE:
    case FCMPEQ:
    case FCMPNE:
    case FCMPBG:
    case FCMPLS:
    case FCMPBE:
    case FCMPGE:
        return 2;
    case FMOV:
    case IMOV:
    case IF:
        return 1;
    default:
        return 0;
    }
}

int resultCount(Operation op) {
    switch (op) {
    case IADD:
    case ISUB:
    case IMUL:
    case IDIV:
    case IMOD:
    case FADD:
    case FSUB:
    case FMUL:
    case FDIV:
    case LAND:
    case LOR:
    case LNOT:
    case ICALL:
    case FCALL:
    case ICMPEQ:
    case ICMPNE:
    case ICMPBG:
    case ICMPLS:
    case ICMPBE:
    case ICMPGE:
    case FCMPEQ:
    case FCMPNE:
    case FCMPBG:
    case FCMPLS:
    case FCMPBE:
    case FCMPGE:
    case NEW:
    case GETFIELD:
    case SETFIELD:
    case ILOAD:
    case FLOAD:
    case IMOV:
    case FMOV:
        return 1;
    default:
        return 0;
    }
}

void add_args(std::vector<std::string> s, Command &com) {
    com.operation = operationByName(s[0]);
    com.argsCount = defaultArgsCount(com.operation);
    com.args = new int[com.argsCount];
    for (int i = 0; i < com.argsCount; i++) {
        com.args[i] = std::stoi(s[i + 1]);
    }
    if (resultCount(com.operation) == 1) {
        com.result = std::stoi(s[s.size() - 1]);
    }
    int  sn = specialArgsType(com.operation);
    switch (specialArgsType(com.operation)) {

    case 1:
        com.intConst = std::stoi(s[s.size() - 1]);
        break;

    case 2:
        com.intConst = std::stoi(s[s.size() - 1]);
        break;

    case 3:
        com.strConst1 = new char[s[1].size()];
        std::strcpy(com.strConst1, s[1].c_str());
        com.argsCount = s.size() - 3;
        com.args = new int[com.argsCount];
        for (int i = 0; i < com.argsCount; i++) {
            com.args[i] = std::stoi(s[i + 2]);
        }
        break;

    case 4:
        com.strConst1 = new char[s[s.size() - 1].size()];
        std::strcpy(com.strConst1, s[s.size() - 1].c_str());
        break;

    case 5:
        com.strConst1 = new char[s[1].size()];
        std::strcpy(com.strConst1, s[1].c_str());

        com.strConst2 = new char[s[2].size()];
        std::strcpy(com.strConst2, s[2].c_str());

        com.argsCount = 1;
        com.args = new int[com.argsCount];
        com.args[0] = std::stoi(s[3]);
        break;

    case 6:
        com.strConst1 = new char[s[1].size()];
        std::strcpy(com.strConst1, s[1].c_str());
        break;
    case 7:
        com.intConst = std::stoi(s[1]);
        break;
    case 8:
        com.floatConst = std::stof(s[1]);
        break;
    }
}

std::vector<std::string> split(std::string str) {
    std::vector<std::string> vec;
    int i, n;
    n = 0;
    for (i = 0; i <= (int)str.length(); i++) {
        if ((i == str.length()) || (str[i] == ' ')) {
            vec.push_back(str.substr(n, i - n));
            n = i + 1;
        }
    }
    return vec;
}

Bytecode* read_bytecode(std::string name) {
    std::string line;
    std::vector<std::string> list;
    std::ifstream in(name);
    while (getline(in, line)) {
        list.push_back(line);
    }

    int count = std::stoi(list[0].c_str());
    Bytecode *bytecode = new Bytecode;
    Function *func_list = new Function[count];
    int curr = 1;
    bytecode->functionsNumber = count;
    for (int i = 0; i < count; i++) {
        func_list[i].name = new char[list[curr].size()];
        std::strcpy(func_list[i].name, list[curr++].c_str());
        func_list[i].intRegsNumber = std::stoi(list[curr++].c_str());
        func_list[i].floatRegsNumber = std::stoi(list[curr++].c_str());
        func_list[i].pointersNumber = std::stoi(list[curr++].c_str());
        func_list[i].commandsNumber = std::stoi(list[curr++].c_str());
        Command *com = new Command[func_list[i].commandsNumber];
        for (int j = 0; j < func_list[i].commandsNumber; j++) {
            std::vector<std::string> s;
            s = split(list[curr++]);
            add_args(s, com[j]);
        }
        func_list[i].commands = com;
    }
    bytecode->functions = func_list;

    int class_count = std::stoi(list[curr++].c_str());
    bytecode->classNumber = class_count;
    Class *class_list = new Class[class_count];
    for (int i = 0; i < class_count; i++) {
        class_list[i].name = new char[list[curr].size()];
        std::strcpy(class_list[i].name, list[curr++].c_str());
        int fields_count = std::stoi(list[curr++].c_str());
        class_list[i].fieldsCounter = fields_count;
        class_list[i].fields = new Field[fields_count];
        for (int j = 0; j < fields_count; j++) {
            class_list[i].fields[j].name = new char[list[curr].size()];
            std::strcpy(class_list[i].fields[j].name, list[curr++].c_str());

            class_list[i].fields[j].type = new char[list[curr].size()];
            std::strcpy(class_list[i].fields[j].type, list[curr++].c_str());

            class_list[i].fields[j].className = new char[sizeof(class_list[i].name)];
            std::strcpy(class_list[i].fields[j].className, class_list[i].name);

            class_list[i].fields[j].offset = j * 4;
        }

    }
    bytecode->classes = class_list;

    return bytecode;
}

void bytecode_writer(Bytecode & B) {
    std::string operations[] = {
        "IADD",
        "ISUB",
        "IMUL",
        "IDIV",
        "IMOD",
        "FADD",
        "FSUB",
        "FMUL",
        "FDIV",
        "LAND",
        "LOR",
        "LNOT",
        "FMOV",
        "IMOV",
        "ILOAD",
        "FLOAD",
        "ICMPEQ",
        "ICMPNE",
        "ICMPBG",
        "ICMPLS",
        "ICMPBE",
        "ICMPGE",
        "FCMPEQ",
        "FCMPNE",
        "FCMPBG",
        "FCMPLS",
        "FCMPBE",
        "FCMPGE",
        "GOTO", 
        "IF",
        "RET",
        "WRITE_INT",
        "WRITE_FLOAT",
        "READ_INT",
        "READ_FLOAT",
        "ICALL",
        "FCALL",
        "WRITE_STR",
        "NEW",
        "GETFIELD",
        "SETFIELD"
    };

    std::ofstream out("debug_byte.txt");
    out << "BYTECODE:" << std::endl << "function count = " << B.functionsNumber << std::endl;
    out << "FUNCTIONS:" << std::endl;

    for (int i = 0; i < B.functionsNumber; i++) {
        out << " function " << B.functions[i].name << std::endl;
        out << "########################" << std::endl;
        out << "int count " << B.functions[i].intRegsNumber << std::endl;
        out << "float count " << B.functions[i].floatRegsNumber << std::endl;
        out << "commands count " << B.functions[i].commandsNumber << std::endl;

        for (int j = 0; j < B.functions[i].commandsNumber; j++) {
            out << std::endl;
            out << "command " <<operations[B.functions[i].commands[j].operation] << std::endl;
            out << "args count " << B.functions[i].commands[j].argsCount << "" << std::endl;
            for (int k = 0; k < B.functions[i].commands[j].argsCount; k++) {
                out << k << ": " << B.functions[i].commands[j].args[k] << std::endl;
            }
            out << "result " << B.functions[i].commands[j].result << std::endl;
            out << "int const " << B.functions[i].commands[j].intConst << std::endl;
            out << "float const " << B.functions[i].commands[j].floatConst << std::endl;
            if(B.functions[i].commands[j].strConst1 != NULL)
                out << "string1 " << B.functions[i].commands[j].strConst1 << std::endl;
            if (B.functions[i].commands[j].strConst2 != NULL)
                out << "string2 " << B.functions[i].commands[j].strConst2 << std::endl;
        }
        out << "########################" << std::endl;
    }
    out << "class count " << B.classNumber << std::endl;
    for (int i = 0; i < B.classNumber; i++) {
        out << " class " << B.classes[i].name << std::endl;
        out << "************************" << std::endl;
        for (int j = 0; j < B.classes[i].fieldsCounter; j++) {
            out << "name " << B.classes[i].fields[j].name << std::endl;
            out << "type " << B.classes[i].fields[j].type << std::endl;
            }
        out << "************************" << std::endl;
    }
    out.close();
}
