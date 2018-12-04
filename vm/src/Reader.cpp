#include <fstream>
#include "Bytecode.h"
#include <string>
#include <stdlib.h>
#include <iostream>
#include <vector>

void add_args(std::vector<std::string> s, int j, Command com[]) {
    com[j].args = new int[3];
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
    if (s[0].compare("ILOAD")) {
        com[j].argsCount = 2;
        com[j].args[0] = std::stoi(s[1]);
        com[j].args[1] = std::stoi(s[2]);
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
}

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
        curr += 4;
        Command *com = new Command[func_list[i].commandsNumber];
        for (int j = 0; j < func_list[i].commandsNumber; j++) {
            std::vector<std::string> s;
            s = split(list[curr]);
            curr++;
            add_args(s, j, com);
        }
        func_list[i].commands = com;
    }
    bytecode->functions = func_list;
	return bytecode;
}
