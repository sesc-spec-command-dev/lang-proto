#pragma once
#include <fstream>
#include <string>
#include <stdlib.h>
#include <iostream>
#include <vector>
#include <assert.h>

#include "Bytecode.h"

Bytecode *read_bytecode(std::string name);