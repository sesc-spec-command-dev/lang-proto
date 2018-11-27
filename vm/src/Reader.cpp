#include <fstream>
#include "Bytecode.h"
#include <string>
#include <stdlib.h>
#include <iostream>
#include <vector>
std::vector<std::string> split(std::string str){
	std::vector<std::string> vec;
	int i,n;
	n = 0;
	for (i = 0; i <= str.length(); i++){
		if (( i == str.length()) || (str[i] == ' ')){
			vec.push_back(str.substr(n, i-n));
			n = i + 1;
		}
	}
	return vec;
}	

Bytecode* readBytecode(const char* name) {
    std::string line;
    std::vector<std::string> list;
    std::ifstream in(name);
    while (getline(in, line)) {
        list.push_back(line);
    }
    int count = atoi(list[0].c_str());
    Bytecode *bytecode = new Bytecode;
    Function *func_list = new Function[count];
    int curr = 1;
    
    bytecode.functionsNumber = count;
    for (int i = 0; i < count; i++) {
        func_list[i].name = list[curr++].c_str();
        func_list[i].intRegsNumber = atoi(list[curr++].c_str());
        func_list[i].floatRegsNumber = atoi(list[curr++].c_str());
        func_list[i].commandsNumber = atoi(list[curr++].c_str());
        curr += 4;
        Command *com = new Command[func_list[i].commandsNumber];
        for (int j = 0; j < func_list[i].commandsNumber; j++) {
            std::vector<string> s;
            s = split(list[curr]);
            curr++;


            

            if (s[0].compare("IADD")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("ISUB")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("IMUL")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("IDIV")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("IMOD")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("FADD")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FSUB")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FMUL")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FDIV")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("ILAND")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("ILOR")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("ILNOT")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("IMOV")) {
                com[j].argsCount = 2;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
            }
            if (s[0].compare("FLAND")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FLOR")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FLNOT")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FMOV")) {
                com[j].argsCount = 2;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
            }
            if (s[0].compare("ILOAD")) {
                com[j].argsCount = 1;
                com[j].args[0] = std::stoi(s[1]);
            }
            if (s[0].compare("FLOAD")) {
                com[j].argsCount = 1;
                com[j].args[0] = std::ftoi(s[1]);
            }
            if (s[0].compare("ICMPEQ")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("ICMPNE")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("ICMPBG")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("ICMPLS")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("ICMPBE")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("ICMPGE")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::stoi(s[1]);
                com[j].args[1] = std::stoi(s[2]);
                com[j].args[2] = std::stoi(s[3]);
            }
            if (s[0].compare("FCMPEQ")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FCMPNE")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FCMPBG")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FCMPLS")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FCMPBE")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
            if (s[0].compare("FCMPGE")) {
                com[j].argsCount = 3;
                com[j].args[0] = std::ftoi(s[1]);
                com[j].args[1] = std::ftoi(s[2]);
                com[j].args[2] = std::ftoi(s[3]);
            }
        }
        func_list[i].commands = com;

    }
    bytecode.functions = func_list;
	return bytecode;
}
