#include <fstream>
#include "Bytecode.h"
#include <string>
#include <stdlib.h>
#include <iostream>
#include <vector>
//#include <assert>

Operation operationByName(std::string str) {
    if (str == "IADD") {
        return IADD ;
    }
    if (str == "ISUB" ) {
        return ISUB;
    }
    if (str == "IMUL" ) {
        return IMUL;
    }
    if (str == "IDIV") {
        return IDIV;
    }
    if (str == "IMOD") {
        return IMOD;
    }
    if (str == "FADD") {
        return FADD;
    }
    if (str == "FSUB") {
        return FSUB;
    }
    if (str == "FMUL") {
        return FMUL;
    }
    if (str == "FDIV") {
        return FDIV;
    }
    if (str == "LAND") {
        return LAND;
    }
    if (str == "LOR") {
        return LOR;
    }                                   
    if (str == "LNOT") {                
        return LNOT;                    
    }                                   
    if (str == "IMOV") {
        return IMOV;                                         
    }                                                        
    if (str == "ILOAD") {                                    
        return ILOAD;                                        
    }                                                        
    if (str == "FMOV") {
        return FMOV;
    }
    if (str == "FLOAD") {
        return FLOAD;
    }
    if (str == "ICMPEQ") {
        return ICMPEQ;
    }
    if(str == "ICMPNE"){
        return ICMPNE;
    }
    if(str == "ICMPBG"){
        return ICMPBG;
    }
    if(str == "ICMPLS"){
        return ICMPLS;
    }
    if(str == "ICMPBE"){
        return ICMPBE;
    }
    if(str == "ICMPGE"){
        return ICMPGE;
    }
    if(str == "FCMPEQ"){
        return FCMPEQ;
    }
    if(str == "FCMPNE"){
        return FCMPNE;
    }
    if(str == "FCMPBG"){
        return FCMPBG;
    }
    if(str == "FCMPLS"){
        return FCMPLS;
    }
    if(str == "FCMPBE"){
        return FCMPBE;
    }
    if(str == "FCMPGE"){
        return FCMPGE;
    }
	if (str == "GOTO"){
		return GOTO;
	}
	if(str == "IF") {
		return IF;
	}
	if(str == "FRET") {
		return FRET;
	}
	if(str == "IRET") {
		return IRET;
	}
	if(str == "WRITE_INT") {
		return WRITE_INT;
	}
	if(str == "WRITE_FLOAT") {
		return WRITE_FLOAT;
	}
	if(str == "READ_INT") {
		return READ_INT;
	}
	if(str == "READ_FLOAT") {
		return READ_FLOAT;
	}
	if(str == "ICALL") {
		return ICALL;
	}
	if(str == "FCALL") {
		return FCALL;
	}
	return FCALL;
}

int specialArgsCount(Operation op) {
	switch (op) {
	case ILOAD:
	case FLOAD:
	case WRITE_INT: 
	case WRITE_FLOAT:
	case READ_INT:
	case READ_FLOAT:
	case ICALL:
	case FCALL:
	case GOTO:
	case IF:
		return 1;
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
    // s -> "ISUB", "47", "92", "13"
    // "ISUB" -> Operation.ISUB
    //      Operation.ISUB -> количество специальных аргументов = SN, количество обычных аргументов = N, количество результатов = R
    //      SN + N + R = s.size - 1
    // Например, для ISUB
    //      SN = 0, N = 2, R = 1
    // SN != 0 - assert
	com.operation = operationByName(s[0]);
	int sn = 0;
	int n = 0;
	int r = 0;
	sn = specialArgsCount(com.operation);
	//assert(SN == 1);
	n = defaultArgsCount(com.operation);
	r = resultCount(com.operation);

	com.argsCount = n;
    com.args = new int[com.argsCount];
    for (int i = 0; i < n; i++) {
        com.args[i] = std::stoi(s[i + 1]);
    }
	if (r == 1) {
		com.result = std::stoi(s[s.size() - 1]);
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
int main() {
	Bytecode B = *readBytecode("C:\\Users\\kadoc\\Desktop\\project\\Project1\\byte.txt");

	return 0;
}
