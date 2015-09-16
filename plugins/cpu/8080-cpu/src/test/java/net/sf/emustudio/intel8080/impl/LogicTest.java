package net.sf.emustudio.intel8080.impl;

import net.sf.emustudio.cpu.testsuite.Generator;
import net.sf.emustudio.cpu.testsuite.runners.RunnerContext;
import net.sf.emustudio.intel8080.impl.suite.ByteTestBuilder;
import net.sf.emustudio.intel8080.impl.suite.FlagsBuilderImpl;
import org.junit.Test;

import java.util.function.Function;

import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_AC;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_C;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_P;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_S;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_Z;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.REG_A;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.REG_B;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.REG_C;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.REG_D;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.REG_E;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.REG_H;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.REG_L;

public class LogicTest extends InstructionsTest {

    private ByteTestBuilder logicTest(Function<RunnerContext<Byte>, Integer> operation) {
        return new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsRegister(REG_A)
                .verifyRegister(REG_A, operation)
                .verifyFlagsOfLastOp(new FlagsBuilderImpl().sign().zero().parity().carryIsReset().auxCarryIsReset())
                .keepCurrentInjectorsAfterRun();
    }

    @Test
    public void testANA() throws Exception {
        ByteTestBuilder test = logicTest(context -> context.first & context.second);

        Generator.forSome8bitBinaryWhichEqual(
                test.run(0xA7)
        );
        Generator.forSome8bitBinary(
                test.secondIsRegister(REG_B).run(0xA0),
                test.secondIsRegister(REG_C).run(0xA1),
                test.secondIsRegister(REG_D).run(0xA2),
                test.secondIsRegister(REG_E).run(0xA3),
                test.secondIsRegister(REG_H).run(0xA4),
                test.secondIsRegister(REG_L).run(0xA5),
                test.setPair(REG_PAIR_HL, 1).secondIsMemoryByteAt(1).run(0xA6)
        );
    }

    @Test
    public void testANI() throws Exception {
        ByteTestBuilder test = logicTest(context -> context.first & context.second);

        Generator.forSome8bitBinary(
                test.runWithSecondOperand(0xE6)
        );
    }

    @Test
    public void testXRA() throws Exception {
        ByteTestBuilder test = logicTest(context -> context.first ^ context.second);

        Generator.forSome8bitBinaryWhichEqual(
                test.run(0xAF)
        );
        Generator.forSome8bitBinary(
                test.secondIsRegister(REG_B).run(0xA8),
                test.secondIsRegister(REG_C).run(0xA9),
                test.secondIsRegister(REG_D).run(0xAA),
                test.secondIsRegister(REG_E).run(0xAB),
                test.secondIsRegister(REG_H).run(0xAC),
                test.secondIsRegister(REG_L).run(0xAD),
                test.setPair(REG_PAIR_HL, 1).secondIsMemoryByteAt(1).run(0xAE)
        );
    }

    @Test
    public void testXRI() throws Exception {
        ByteTestBuilder test = logicTest(context -> context.first ^ context.second);

        Generator.forSome8bitBinary(
                test.runWithSecondOperand(0xEE)
        );
    }

    @Test
    public void testORA() throws Exception {
        ByteTestBuilder test = logicTest(context -> context.first | context.second);

        Generator.forSome8bitBinaryWhichEqual(
                test.run(0xB7)
        );
        Generator.forSome8bitBinary(
                test.secondIsRegister(REG_B).run(0xB0),
                test.secondIsRegister(REG_C).run(0xB1),
                test.secondIsRegister(REG_D).run(0xB2),
                test.secondIsRegister(REG_E).run(0xB3),
                test.secondIsRegister(REG_H).run(0xB4),
                test.secondIsRegister(REG_L).run(0xB5),
                test.setPair(REG_PAIR_HL, 1).secondIsMemoryByteAt(1).run(0xB6)
        );
    }

    @Test
    public void testORI() throws Exception {
        Function<RunnerContext<Byte>, Integer> op = context -> context.first | context.second;

        Generator.forSome8bitBinary(
                logicTest(op).runWithSecondOperand(0xF6)
        );
    }

    @Test
    public void testDAA() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .verifyRegister(REG_A, context -> {
                    int result = ((int)context.first)& 0xFF;
                    if (((context.flags & FLAG_AC) == FLAG_AC) || (result & 0x0F) > 9) {
                        result += 6;
                    }
                    if ((context.flags & FLAG_C) == FLAG_C || (result & 0xF0) > 0x90) {
                        result += 0x60;
                    }
                    return result;
                })
                .verifyFlagsOfLastOp(new FlagsBuilderImpl().sign().zero().parity().carry()
                        .expectFlagOnlyWhen(FLAG_AC, (context, result) -> {
                            int firstInt = ((RunnerContext)context).first.intValue();
                            int diff = (((Number)result).intValue() - firstInt) & 0x0F;

                            return ((diff == 6) && FlagsBuilderImpl.isAuxCarry(firstInt, 6));
                        }))
                .firstIsRegister(REG_A);

        Generator.forSome8bitUnary(
                test.run(0x27)
        );
    }

    @Test
    public void testCMA() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .verifyRegister(REG_A, context -> (~context.first) & 0xFF)
                .firstIsRegister(REG_A);

        Generator.forSome8bitUnary(
                test.run(0x2F)
        );
    }

    @Test
    public void testSTC() throws Exception {
        cpuRunnerImpl.setProgram(0x37);
        cpuRunnerImpl.reset();
        cpuRunnerImpl.step();

        cpuVerifierImpl.checkFlags(FLAG_C);
        cpuVerifierImpl.checkNotFlags(FLAG_S | FLAG_Z | FLAG_AC | FLAG_P);
    }

    @Test
    public void testCMC() throws Exception {
        cpuRunnerImpl.setProgram(0x3F);
        cpuRunnerImpl.reset();
        cpuRunnerImpl.setFlags(FLAG_C);
        cpuRunnerImpl.step();

        cpuVerifierImpl.checkNotFlags(FLAG_C | FLAG_S | FLAG_Z | FLAG_AC | FLAG_P);
    }

    @Test
    public void testRLC() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsRegister(REG_A)
                .verifyRegister(REG_A, context -> (context.first << 1) | ((context.first >>> 7) & 1))
                .verifyFlagsOfLastOp(new FlagsBuilderImpl().carryIsFirstOperandMSB());

        Generator.forSome8bitUnary(
                test.run(0x07)
        );
    }

    @Test
    public void testRRC() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsRegister(REG_A)
                .verifyRegister(REG_A, context -> (((context.first &0xFF) >>> 1) | ((context.first & 1) << 7)) & 0xFF)
                .verifyFlagsOfLastOp(new FlagsBuilderImpl().carryIsFirstOperandLSB());

        Generator.forSome8bitUnary(
                test.run(0x0F)
        );
    }

    @Test
    public void testRAL() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsRegister(REG_A)
                .verifyRegister(REG_A, context -> (context.first << 1) | (context.flags & 1))
                .verifyFlagsOfLastOp(new FlagsBuilderImpl().carryIsFirstOperandMSB());

        Generator.forSome8bitUnary(
                test.run(0x17)
        );
    }

    @Test
    public void testRAR() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsRegister(REG_A)
                .verifyRegister(REG_A, context -> (((context.first &0xFF) >>> 1) | ((context.flags & 1) << 7)) & 0xFF)
                .verifyFlagsOfLastOp(new FlagsBuilderImpl().carryIsFirstOperandLSB());

        Generator.forSome8bitUnary(
                test.run(0x1F)
        );
    }

    @Test
    public void testCMP() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsRegister(REG_A)
                .verifyRegister(REG_A, context -> context.first.intValue())
                .verifyFlags(
                        new FlagsBuilderImpl().sign().zero().carry().auxCarry().parity(),
                        context -> (context.first & 0xFF) - (context.second & 0xFF))
                .keepCurrentInjectorsAfterRun();

        Generator.forSome8bitBinaryWhichEqual(
                test.run(0xBF)
        );
        Generator.forSome8bitBinary(
                test.secondIsRegister(REG_B).run(0xB8),
                test.secondIsRegister(REG_C).run(0xB9),
                test.secondIsRegister(REG_D).run(0xBA),
                test.secondIsRegister(REG_E).run(0xBB),
                test.secondIsRegister(REG_H).run(0xBC),
                test.secondIsRegister(REG_L).run(0xBD),
                test.setPair(REG_PAIR_HL, 1).secondIsMemoryByteAt(1).run(0xBE)
        );
    }

    @Test
    public void testCPI() throws Exception {
        FlagsBuilderImpl flagsToCheck = new FlagsBuilderImpl().sign().zero().carry().auxCarry().parity();
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsRegister(REG_A)
                .verifyRegister(REG_A, context -> context.first.intValue())
                .verifyFlags(flagsToCheck, context -> (context.first & 0xFF) - (context.second & 0xFF));

        Generator.forSome8bitBinary(
                test.runWithSecondOperand(0xFE)
        );
    }


}
