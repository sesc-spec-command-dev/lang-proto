#include <fstream>
#include "Bytecode.h"
#include <string>
#include <stdlib.h>
#include <iostream>
#include <vector>

void add_args(std::vector<std::string> s, Command &com) {
    com.argsCount = s.size();
    com.args = new int[com.argsCount];
    for (int i = 0; i <  com.argsCount; i++) {
        com.args[i] = std::stoi(s[i + 1]);
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
            add_args(s, com[j]);
        }
        func_list[i].commands = com;
    }
    bytecode->functions = func_list;
	return bytecode;
}
