#include <fstream>
#include "Bytecode.h"
#include <string>
#include <stdlib.h>
#include <iostream>
#include <vector>
Bytecode* readBytecode(const char* name) {
	std::string line;
	std::vector<std::string> list;
	std::ifstream in(name);
		while (getline(in, line)){
			list.push_back(line);
		}
	Bytecode *bytecode = new Bytecode; //? in dynamic memory
	int curr = 1;
	int count = atoi(list[0].c_str());
	bytecode.functionsNumber = count;
	for (int i = 0; i < count; i++) {
		Function func; //? создайте массив функций до цикла, здесь заполн€йте элемент массива
		func.name = list[curr++].c_str();
		func.intRegsNumber = atoi(list[curr++].c_str());
		func.floatRegsNumber = atoi(list[curr++].c_str());
		func.commandsNumber = atoi(list[curr++].c_str());
		for (int j = 0; j < func.commandsNumber; j++) {
			
			std::string S;
			std::vector<int> a;
			std::vector<std::string>> c;
			int m, k, b;
			list[j] += " ";
			int len = list[j].length();
			for (m = 0; m < len; m++) {
				if (list[j][m] == ' ')
					break;
			}
			S = list[j].substr(0, m);
			k = m + 1;
			if (S == 'CALL' && S == 'FLOAD' && S == 'WRITE_STR'){
				for (b = m + 1; b < len; b++) {
					if (S[b] == ' ') {
						c.push_back(list[j].substr(k, b - 1));
						k = b + 1;
					}
				}
			}
			//? special cases: CALL, FLOAD, WRITE_STR
			else{
				for (b = m + 1; b < len; b++) {
						a.push_back(stoi(list[j].substr(k, b - 1)));
						k = b + 1;
					}
				}
			}

			if (S.compare("IADD")) {
			
			}
			if (S.compare("ISUB")) {

			}
			if (S.compare("IMUL")) {

			}
			if (S.compare("IDIV")) {

			}
			if (S.compare("IMOD")) {

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

			}
			if (S.compare("ILOR")) {

			}
			if (S.compare("ILNOT")) {

			}
			if (S.compare("IMOV")) {

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
			if (S.compare("GOTO")) {

			}
			if (S.compare("IF")) {

			}
			if (S.compare("RET")) {

			}
			if (S.compare("WRITE_INT")) {

			}
			if (S.compare("WRITE_FLOAT")) {

			}
			if (S.compare("READ_INT")) {

			}
			if (S.compare("READ_FLOAT")) {

			}
			if (S.compare("CALL")) {

			}


			}
		
		
		
		bytecode.functions[i]=func;
	}
	return &bytecode;
}
