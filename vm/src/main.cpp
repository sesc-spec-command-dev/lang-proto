#include "main.h"
/* run this program using the console pauser or add your own getch, system("pause") or input loop */

int main(int argc, char** argv) {
	Bytecode *bytecode = (Bytecode*)malloc(sizeof(Bytecode));
	
	BYTECODE = bytecode;
	return 0;
}

void ips(){
	
	for(int i = 0 ; i < BYTECODE -> functionsNumber; ++i){
		if(BYTECODE -> functions[i].name == "main") {
			Frame firstFrame(BYTECODE -> functions[i]);
			firstFrame.doFunc();
		}
	}
	
}
