#!/bin/bash

java -cp "compilers/as-8080.jar:lib/*" net.sf.emustudio.intel8080.assembler.impl.Main "$@"
