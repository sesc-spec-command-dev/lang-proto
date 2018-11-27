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
            s = func_list[i]



            

            if (S.compare("IADD")) {
                com.argsCount = 3;
                com.args[0] = a[0];
                com.args[1] = a[1];
                com.args[2] = a[2];
            }
            if (S.compare("ISUB")) {
                com.argsCount = 3;
                com.args[0] = a[0];
                com.args[1] = a[1];
                com.args[2] = a[2];
            }
            if (S.compare("IMUL")) {
                com.argsCount = 3;
                com.args[0] = a[0];
                com.args[1] = a[1];
                com.args[2] = a[2];
            }
            if (S.compare("IDIV")) {
                com.argsCount = 3;
                com.args[0] = a[0];
                com.args[1] = a[1];
                com.args[2] = a[2];
            }
            if (S.compare("IMOD")) {
                com.argsCount = 3;
                com.args[0] = a[0];
                com.args[1] = a[1];
                com.args[2] = a[2];
            }
            if (S.compare("FADD")) {

            }
            if (S.compare("FSUB")) {

            }
            if (S.compare("FMUL")) {

            }
            if (S.compare("FDIV")) {

            }
            if (S.compare("ILAND")) {
                com.argsCount = 3;
                com.args[0] = a[0];
                com.args[1] = a[1];
                com.args[2] = a[2];
            }
            if (S.compare("ILOR")) {
                com.argsCount = 3;
                com.args[0] = a[0];
                com.args[1] = a[1];
                com.args[2] = a[2];
            }
            if (S.compare("ILNOT")) {
                com.argsCount = 3;
                com.args[0] = a[0];
                com.args[1] = a[1];
                com.args[2] = a[2];
            }
            if (S.compare("IMOV")) {
                com.argsCount = 2;
                com.args[0] = a[0];
                com.args[1] = a[1];
            }
            if (S.compare("FLAND")) {

            }
            if (S.compare("FLOR")) {

            }
            if (S.compare("FLNOT")) {

            }
            if (S.compare("FMOV")) {

            }
            if (S.compare("ILOAD")) {
                com.argsCount = 1;
                com.args[0] = a[0];
            }
            if (S.compare("FLOAD")) {

            }
            if (S.compare("ICMPEQ")) {

            }
            if (S.compare("ICMPNE")) {

            }
            if (S.compare("ICMPBG")) {

            }
            if (S.compare("ICMPLS")) {

            }
            if (S.compare("ICMPBE")) {

            }
            if (S.compare("ICMPGE")) {

            }
            if (S.compare("FCMPEQ")) {

            }
            if (S.compare("FCMPNE")) {

            }
            if (S.compare("FCMPBG")) {

            }
            if (S.compare("FCMPLS")) {

            }
            if (S.compare("FCMPBE")) {

            }
            if (S.compare("FCMPGE")) {

            }
        }
    }

	return bytecode;
}
