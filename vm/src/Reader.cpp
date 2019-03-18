#include <fstream>
#include "Bytecode.h"
#include <string>
#include <stdlib.h>
#include <iostream>
#include <vector>
#include <assert.h>

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
    if (str == "FRET")       return FRET;
    if (str == "IRET")       return IRET;
    if (str == "WRITE_INT")  return WRITE_INT;
    if (str == "WRITE_FLOAT")return WRITE_FLOAT;
    if (str == "READ_INT")   return READ_INT;
    if (str == "READ_FLOAT") return READ_FLOAT;
    if (str == "ICALL")      return ICALL;
    if (str == "FCALL")      return FCALL;
    if (str == "WRITE_STR")  return WRITE_STR;
    
    assert(false);
    
}

int specialArgsType(Operation op) {
	switch (op) {
	case ILOAD:
	case WRITE_INT: 
    case READ_INT:
    case GOTO:
    case IF:
        return 1;
    case FLOAD:
    case WRITE_FLOAT:
    case READ_FLOAT:
        return 2;
	case ICALL:
	case FCALL:
		return 3;
	case WRITE_STR:
		return 4;
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
    switch (specialArgsType(com.operation)){
    case 1:
        std::cout << "1";
        com.intConst = std::stoi(s[s.size() - 1]);
        break;
    case 2:
        std::cout << "2";
        com.floatConst = std::stof(s[s.size() - 1]);
        break;
    case 3:
        std::cout << "3";
        com.strConst = s[1];
        com.argsCount = s.size() - 3;
        delete[] com.args;
        com.args = new int[com.argsCount];
        for (int i = 0; i < com.argsCount; i++) {
            com.args[i] = std::stoi(s[i + 2]);
        }
        com.result = std::stoi(s[s.size() - 1]);
        break;
    case 4:
        std::cout << "4";
        com.strConst = s[s.size() - 1];
        break;
    }

}

std::vector<std::string> split(std::string str){
    std::vector<std::string> vec;
    int i,n;
    n = 0;
    for (i = 0; i <= (int)str.length(); i++){
        if (( i == str.length()) || (str[i] == ' ')){
            vec.push_back(str.substr(n, i-n));
            n = i + 1;
        }
    }
    return vec;
}    

Bytecode* readBytecode(std::string name) {
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
        func_list[i].name = list[curr++].c_str();
        func_list[i].intRegsNumber = std::stoi(list[curr++].c_str());
        func_list[i].floatRegsNumber = std::stoi(list[curr++].c_str());
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
    return bytecode;
}

void bytecode_writer(Bytecode & B) {
	std::ofstream out("debug_byte.txt");
	out << "BYTECODE:" << std::endl << "function count = " << B.functionsNumber << std::endl;
	out << "FUNCTIONS:" << std::endl;

	for (int i = 0; i < B.functionsNumber; i++) {
        out << "Имя функции " << B.functions[i].name << std::endl;
		out << "########################"<<std::endl;
		out << "Целочисленные: " << B.functions[i].intRegsNumber << std::endl;
		out << "Float " << B.functions[i].floatRegsNumber << std::endl;
		out << "Commands_count " << B.functions[i].commandsNumber << std::endl;
		
		for (int j = 0; j < B.functions[i].commandsNumber; j++) {
			out  << std::endl;
            out << "Command " << B.functions[i].commands[j].operation << std::endl;
			out << "Кол-во обычных аргументов " << B.functions[i].commands[j].argsCount << "" << std::endl;

			for (int k = 0; k < B.functions[i].commands[j].argsCount; k++) {
	
				out<<k<<": " << B.functions[i].commands[j].args[k] << std::endl;

			}

			out << "Результат " << B.functions[i].commands[j].result << std::endl;
			out << "Целая константа " << B.functions[i].commands[j].intConst << std::endl;
			out << "Вещ-ая константа " << B.functions[i].commands[j].floatConst << std::endl;
			out << "Строковая константа " << B.functions[i].commands[j].strConst << std::endl;
		}
		out << "########################"<<std::endl;
	}

	out.close();
}

int main(int argc, char** argv) {

    std::cout << argc << std::endl;

    for (int i = 0; i < argc; i++) {
        std::cout << argv[i] << std::endl;
    }

	Bytecode* B = readBytecode("byte.txt");
    
   // bytecode_writer(*B);
	return 0;
}
