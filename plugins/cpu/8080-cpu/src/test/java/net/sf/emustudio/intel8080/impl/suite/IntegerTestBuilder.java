/*
 * KISS, YAGNI, DRY
 *
 * (c) Copyright 2006-2017, Peter Jakubčo
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.sf.emustudio.intel8080.impl.suite;

import net.sf.emustudio.cpu.testsuite.RunnerContext;
import net.sf.emustudio.cpu.testsuite.TestBuilder;
import net.sf.emustudio.cpu.testsuite.injectors.MemoryExpand;
import net.sf.emustudio.intel8080.impl.suite.injectors.RegisterPair;
import net.sf.emustudio.intel8080.impl.suite.injectors.RegisterPairPSW;

import java.util.function.Function;

public class IntegerTestBuilder extends TestBuilder<Integer, IntegerTestBuilder, CpuRunnerImpl, CpuVerifierImpl> {

    public IntegerTestBuilder(CpuRunnerImpl cpuRunner, CpuVerifierImpl cpuVerifier) {
        super(cpuRunner, cpuVerifier);
    }

    public IntegerTestBuilder firstIsPair(int registerPair) {
        runner.injectFirst(new MemoryExpand(), new RegisterPair(registerPair));
        return this;
    }

    public IntegerTestBuilder secondIsPair(int registerPair) {
        runner.injectSecond(new MemoryExpand(), new RegisterPair(registerPair));
        return this;
    }

    public IntegerTestBuilder secondIsRegisterPairPSW(int registerPairPSW) {
        runner.injectSecond(new MemoryExpand(), new RegisterPairPSW(registerPairPSW));
        return this;
    }

    public IntegerTestBuilder setRegister(int register, int value) {
        runner.injectFirst((tmpRunner, argument) -> cpuRunner.setRegister(register, value));
        return this;
    }

    public IntegerTestBuilder setPair(int registerPair, int value) {
        runner.injectFirst((tmpRunner, argument) -> cpuRunner.setRegisterPair(registerPair, value));
        return this;
    }

    public IntegerTestBuilder verifyPairAndPSW(int registerPair, Function<RunnerContext<Integer>, Integer> operation) {
        lastOperation = operation;
        runner.verifyAfterTest(context -> cpuVerifier.checkRegisterPairPSW(registerPair, operation.apply(context)));
        return this;
    }

    public IntegerTestBuilder verifyRegister(int register, Function<RunnerContext<Integer>, Integer> operator) {
        lastOperation = operator;
        return verifyRegister(register);
    }

    public IntegerTestBuilder verifyRegister(int register) {
        if (lastOperation == null) {
            throw new IllegalStateException("Last operation is not set!");
        }
        Function<RunnerContext<Integer>, Integer> operation = lastOperation;
        runner.verifyAfterTest(context -> cpuVerifier.checkRegister(register, operation.apply(context)));
        return this;
    }

    public IntegerTestBuilder verifyPair(int registerPair, Function<RunnerContext<Integer>, Integer> operator) {
        lastOperation = operator;
        runner.verifyAfterTest(context -> cpuVerifier.checkRegisterPair(registerPair, operator.apply(context)));
        return this;
    }

    public IntegerTestBuilder verifyPC(Function<RunnerContext<Integer>, Integer> operator) {
        lastOperation = operator;
        runner.verifyAfterTest(context -> cpuVerifier.checkPC(operator.apply(context)));
        return this;
    }

}
