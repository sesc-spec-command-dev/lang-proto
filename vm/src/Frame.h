#pragma once

extern Bytecode *_BYTECODE;

Function findFunction(Command *command);
class Frame{
    private:
        int programCounter;
        Function *function;
    public:
		int *pRegs;
        int *iRegs;
        float *fRegs;
        Frame(Function &function);
        ~Frame();
        void execute();
    
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

    void _IMOVE();
    void _FMOVE();
    
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
    
    void _LAND(); 
    void _LOR(); 
    void _LNOT(); 
    
    
    void _GOTO(); 
    void _GOTO(int line); 
    void _IF(); 
    void _WRITE_INT(); 
    void _WRITE_FLOAT();
    void _WRITE_STR();
    void _READ_INT();
    void _READ_FLOAT(); 
    
    void _ICALL();

	void _NEW();
	void _SETFIELD();
	void _GETFIELD();

};
