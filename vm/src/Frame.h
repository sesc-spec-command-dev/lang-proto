#pragma once

#include "main.h"

static Bytecode *BYTECODE;	

class Frame{
	private:
		int programCounter;
		Function *function;
	public:
		void doFunc();
		int *iargs;
		float *fargs;
		Frame(Function &function);
		~Frame();
	
	protected: 

	void _IADD(); 
	void _ISUB(); 
	void _IMUL(); 
	void _IDIV(); 
	void _IMOD(); 
	
	void _FADD(); 
	void _FSUB(); 
	void _FMUL(); 
	void _FDIV(); 
	
	void _ICMPEQ(); 
	void _ICMPNE(); 
	void _ICMPBG(); 
	void _ICMPLS(); 
	void _ICMPBE(); 
	void _ICMPGE(); 
	
	void _FCMPEQ(); 
	void _FCMPNE(); 
	void _FCMPBG(); 
	void _FCMPLS(); 
	void _FCMPBE(); 
	void _FCMPGE(); 
	
	void _ILOAD(); 
	void _FLOAD(); 
	
	void __LAND(); 
	void _LOR(); 
	void _LNOT(); 
	
	
	void _GOTO(); 
	void _GOTO(int line); 
	void _IF(); 
	void _WRITE_INT(); 
	void _WRITE_FLOAT(); 
	void _READ_INT(); 
	void _READ_FLOAT(); 
	
	void _ICALL(); 
	void _FCALL(); 
	
	void execution(Command &command);
	
	
};
