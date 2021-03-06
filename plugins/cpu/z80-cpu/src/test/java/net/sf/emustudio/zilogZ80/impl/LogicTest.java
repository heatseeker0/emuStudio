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
package net.sf.emustudio.zilogZ80.impl;

import net.sf.emustudio.cpu.testsuite.Generator;
import net.sf.emustudio.cpu.testsuite.RunnerContext;
import net.sf.emustudio.zilogZ80.impl.suite.ByteTestBuilder;
import net.sf.emustudio.zilogZ80.impl.suite.FlagsCheckImpl;
import net.sf.emustudio.zilogZ80.impl.suite.IntegerTestBuilder;
import org.junit.Test;

import java.util.function.Function;

import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.*;
import static net.sf.emustudio.zilogZ80.impl.suite.Utils.get8MSBplus8LSB;
import static net.sf.emustudio.zilogZ80.impl.suite.Utils.predicate8MSBplus8LSB;

@SuppressWarnings("unchecked")
public class LogicTest extends InstructionsTest {

    private ByteTestBuilder getLogicTestBuilder(Function<RunnerContext<Byte>, Integer> operator) {
        return new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .verifyRegister(REG_A, operator)
            .verifyFlagsOfLastOp(new FlagsCheckImpl()
                .carryIsReset().subtractionIsReset().halfCarryIsSet().parity().sign().zero())
            .keepCurrentInjectorsAfterRun();
    }

    @Test
    public void testAND__r() throws Exception {
        ByteTestBuilder test = getLogicTestBuilder(context -> context.first & context.second);

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
            test.secondIsMemoryByteAt(0x0320).setPair(REG_PAIR_HL, 0x0320).run(0xA6)
        );
    }

    @Test
    public void testAND__n() throws Exception {
        ByteTestBuilder test = getLogicTestBuilder(context -> context.first & context.second);

        Generator.forSome8bitBinary(
            test.runWithSecondOperand(0xE6)
        );
    }

    @Test
    public void testOR__r() throws Exception {
        ByteTestBuilder test = getLogicTestBuilder(context -> context.first | context.second);

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
            test.secondIsMemoryByteAt(0x0320).setPair(REG_PAIR_HL, 0x0320).run(0xB6)
        );
    }

    @Test
    public void testOR__n() throws Exception {
        ByteTestBuilder test = getLogicTestBuilder(context -> context.first | context.second);

        Generator.forSome8bitBinary(
            test.runWithSecondOperand(0xF6)
        );
    }

    @Test
    public void testXOR__r() throws Exception {
        ByteTestBuilder test = getLogicTestBuilder(context -> context.first ^ context.second);

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
            test.secondIsMemoryByteAt(0x0320).setPair(REG_PAIR_HL, 0x0320).run(0xAE)
        );
    }

    @Test
    public void testXOR__n() throws Exception {
        ByteTestBuilder test = getLogicTestBuilder(context -> context.first ^ context.second);

        Generator.forSome8bitBinary(
            test.runWithSecondOperand(0xEE)
        );
    }

    @Test
    public void testCP__r() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .verifyRegister(REG_A, context -> context.first & 0xFF)
            .verifyFlags(new FlagsCheckImpl().sign().zero().carry().halfCarry().overflow().subtractionIsSet(),
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
            test.setPair(REG_PAIR_HL, 0x0320).secondIsMemoryByteAt(0x0320).run(0xBE)
        );
    }

    @Test
    public void testCP__n() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .verifyRegister(REG_A, context -> context.first & 0xFF)
            .verifyFlags(new FlagsCheckImpl().sign().zero().carry().halfCarry().overflow().subtractionIsSet(),
                context -> (context.first & 0xFF) - (context.second & 0xFF));

        Generator.forSome8bitBinary(
            test.runWithSecondOperand(0xFE)
        );
    }

    @Test
    public void testDAA() throws Exception {
        Function<RunnerContext<Byte>, Integer> daaFunc = context -> FlagsTableGeneratorTest.daa(
            ((context.flags & FLAG_C) == FLAG_C),
            ((context.flags & FLAG_H) == FLAG_H),
            (byte) (((int) context.first) & 0xFF)
        );

        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .verifyRegister(REG_A, context -> daaFunc.apply(context) & 0xFF)
            .verifyFlagsOfLastOp(new FlagsCheckImpl<Byte>().sign().zero().parity()
                .expectFlagOnlyWhen(FLAG_H, (context, result) ->
                    FlagsTableGeneratorTest.daa_hf(
                        (context.flags & FLAG_N) == FLAG_N,
                        (context.flags & FLAG_H) == FLAG_H,
                        (byte) (((int) context.first) & 0xFF)
                    ) == FLAG_H
                )
                .expectFlagOnlyWhen(FLAG_C, ((context, result) ->
                    (daaFunc.apply(context) >>> 8) == FLAG_C))
            );

        Generator.forAll8bitUnary(
            test.run(0x27)
        );
    }

    @Test
    public void testCPL() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .verifyRegister(REG_A, context -> ~context.first)
            .verifyFlagsOfLastOp(new FlagsCheckImpl<>().halfCarryIsSet().subtractionIsSet());

        Generator.forSome8bitUnary(
            test.run(0x2F)
        );
    }

    @Test
    public void testSCF() throws Exception {
        cpuRunnerImpl.setProgram(0x37);
        cpuRunnerImpl.reset();

        cpuRunnerImpl.step();

        cpuVerifierImpl.checkFlags(FLAG_C);
    }

    @Test
    public void testCCF() throws Exception {
        cpuRunnerImpl.setProgram(0x3F);
        cpuRunnerImpl.reset();
        cpuRunnerImpl.setFlags(FLAG_C);

        cpuRunnerImpl.step();

        cpuVerifierImpl.checkNotFlags(FLAG_C);
    }

    @Test
    public void testRLCA() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .setFlags(FLAG_H | FLAG_H)
            .verifyRegister(REG_A, context -> ((context.first << 1) & 0xFF) | (context.first >>> 7) & 1)
            .verifyFlagsOfLastOp(new FlagsCheckImpl<>()
                .carryIsFirstOperandMSB().halfCarryIsReset().subtractionIsReset());

        Generator.forSome8bitUnary(
            test.run(0x07)
        );
    }

    @Test
    public void testRRCA() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .setFlags(FLAG_H | FLAG_H)
            .verifyRegister(REG_A, context -> ((context.first >> 1) & 0x7F) | ((context.first & 1) << 7))
            .verifyFlagsOfLastOp(new FlagsCheckImpl<>()
                .carryIsFirstOperandLSB().halfCarryIsReset().subtractionIsReset());

        Generator.forSome8bitUnary(
            test.run(0x0F)
        );
    }

    @Test
    public void testRLA() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .setFlags(FLAG_H | FLAG_H)
            .verifyRegister(REG_A, context -> ((context.first << 1) & 0xFE) | (context.flags & 1))
            .verifyFlagsOfLastOp(new FlagsCheckImpl<>()
                .carryIsFirstOperandMSB().halfCarryIsReset().subtractionIsReset());

        Generator.forSome8bitUnary(
            test.run(0x17)
        );
    }

    @Test
    public void testRRA() throws Exception {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .setFlags(FLAG_H | FLAG_H)
            .verifyRegister(REG_A, context -> ((context.first >> 1) & 0x7F) | ((context.flags & 1) << 7))
            .verifyFlagsOfLastOp(new FlagsCheckImpl<>()
                .carryIsFirstOperandLSB().halfCarryIsReset().subtractionIsReset());

        Generator.forSome8bitUnary(
            test.run(0x1F)
        );
    }

    private IntegerTestBuilder prepareCPxTest(Function<RunnerContext<Integer>, Integer> hlOperation) {
        return new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsAddressAndSecondIsMemoryByte()
            .firstIsPair(REG_PAIR_HL)
            .first8LSBisRegister(REG_A)
            .registerIsRandom(REG_B, 0xFF)
            .registerIsRandom(REG_C, 0xFF)
            .verifyPair(REG_PAIR_HL, hlOperation)
            .verifyPair(REG_PAIR_BC, context ->
                ((context.getRegister(REG_B) << 8 | context.getRegister(REG_C)) - 1)
            )
            .verifyFlags(new FlagsCheckImpl<>()
                .sign().zero().subtractionIsSet().halfCarry()
                .expectFlagOnlyWhen(FLAG_PV, (context, result) ->
                    ((context.getRegister(REG_B) << 8 | context.getRegister(REG_C)) - 1) != 0
                ), context -> context.registers.get(REG_A) - (context.second & 0xFF));
    }

    @Test
    public void testCPI() {
        IntegerTestBuilder test = prepareCPxTest(context ->
            ((context.getRegister(REG_H) << 8 | context.getRegister(REG_L)) + 1));

        Generator.forSome16bitBinary(3,
            test.run(0xED, 0xA1)
        );
    }

    @Test
    public void testCPIR() {
        IntegerTestBuilder test = prepareCPxTest(context ->
            ((context.getRegister(REG_H) << 8 | context.getRegister(REG_L)) + 1))
            .verifyPC(context -> {
                boolean regAzero = (context.registers.get(REG_A) == (context.second & 0xFF));
                boolean BCzero = ((context.getRegister(REG_B) << 8 | context.getRegister(REG_C)) - 1) == 0;
                if (regAzero || BCzero) {
                    return context.PC + 2;
                }
                return context.PC;
            });

        Generator.forSome16bitBinary(3,
            test.run(0xED, 0xB1)
        );
    }

    @Test
    public void testCPD() {
        IntegerTestBuilder test = prepareCPxTest(context ->
            ((context.getRegister(REG_H) << 8 | context.getRegister(REG_L)) - 1));

        Generator.forSome16bitBinary(3,
            test.run(0xED, 0xA9)
        );
    }

    @Test
    public void testCPDR() {
        IntegerTestBuilder test = prepareCPxTest(context ->
            ((context.getRegister(REG_H) << 8 | context.getRegister(REG_L)) - 1))
            .verifyPC(context -> {
                boolean regAzero = (context.registers.get(REG_A) == (context.second & 0xFF));
                boolean BCzero = ((context.getRegister(REG_B) << 8 | context.getRegister(REG_C)) - 1) == 0;
                if (regAzero || BCzero) {
                    return context.PC + 2;
                }
                return context.PC;
            });

        Generator.forSome16bitBinary(3,
            test.run(0xED, 0xB9)
        );
    }

    private IntegerTestBuilder prepareLogicIXYtest(Function<RunnerContext<Integer>, Integer> operation) {
        return new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .first8MSBplus8LSBisMemoryAddressAndSecondIsMemoryByte()
            .first8LSBisRegister(REG_A)
            .verifyRegister(REG_A, operation)
            .verifyFlagsOfLastOp(new FlagsCheckImpl()
                .sign().zero().carryIsReset().halfCarryIsSet().parity().subtractionIsReset());
    }


    @Test
    public void testAND__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareLogicIXYtest(context -> (context.first & 0xFF) & (context.second & 0xFF))
            .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(3),
            test.first8MSBisIX().runWithFirst8bitOperand(0xDD, 0xA6),
            test.first8MSBisIY().runWithFirst8bitOperand(0xFD, 0xA6)
        );
    }

    @Test
    public void testOR__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareLogicIXYtest(context -> (context.first & 0xFF) | (context.second & 0xFF))
            .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(3),
            test.first8MSBisIX().runWithFirst8bitOperand(0xDD, 0xB6),
            test.first8MSBisIY().runWithFirst8bitOperand(0xFD, 0xB6)
        );
    }

    @Test
    public void testXOR__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareLogicIXYtest(context -> (context.first & 0xFF) ^ (context.second & 0xFF))
            .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(3),
            test.first8MSBisIX().runWithFirst8bitOperand(0xDD, 0xAE),
            test.first8MSBisIY().runWithFirst8bitOperand(0xFD, 0xAE)
        );
    }

    @Test
    public void testCP__IX_IY_plus_d() {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .first8MSBplus8LSBisMemoryAddressAndSecondIsMemoryByte()
            .first8LSBisRegister(REG_A)
            .verifyRegister(REG_A, context -> context.first & 0xFF)
            .verifyFlags(new FlagsCheckImpl().sign().zero().carry().halfCarry().overflow().subtractionIsSet(),
                context -> (context.first & 0xFF) - (context.second & 0xFF)
            )
            .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(3),
            test.first8MSBisIX().runWithFirst8bitOperand(0xDD, 0xBE),
            test.first8MSBisIY().runWithFirst8bitOperand(0xFD, 0xBE)
        );
    }

    @Test
    public void testNEG() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsRegister(REG_A)
            .verifyRegister(REG_A, context -> (0 - context.first) & 0xFF)
            .verifyFlagsOfLastOp(new FlagsCheckImpl<Byte>()
                    .switchFirstAndSecond().sign().zero().halfCarry().subtractionIsSet()
                    .expectFlagOnlyWhen(FLAG_C, (context, result) -> context.second.byteValue() != 0)
                // .expectFlagOnlyWhen(FLAG_PV, (context, result) -> (context.second & 0xFF) == 0x80)
            )
            .keepCurrentInjectorsAfterRun();

        Generator.forSome8bitUnary(
            test.run(0xED, 0x44),
            test.run(0xED, 0x4C),
            test.run(0xED, 0x54),
            test.run(0xED, 0x5C),
            test.run(0xED, 0x64),
            test.run(0xED, 0x6C),
            test.run(0xED, 0x74),
            test.run(0xED, 0x7C)
        );
    }

    @Test
    public void testRLC__r() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .setFlags(FLAG_H | FLAG_N)
            .keepCurrentInjectorsAfterRun()
            .clearOtherVerifiersAfterRun();

        Function<RunnerContext<Byte>, Integer> operator = context ->
            ((context.first << 1) & 0xFF) | (context.first >>> 7) & 1;
        FlagsCheckImpl flags = new FlagsCheckImpl<>()
            .carryIsFirstOperandMSB().sign().zero().parity().halfCarryIsReset().subtractionIsReset();

        Generator.forSome8bitUnary(
            test.firstIsRegister(REG_A).verifyRegister(REG_A, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x07),
            test.firstIsRegister(REG_B).verifyRegister(REG_B, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x00),
            test.firstIsRegister(REG_C).verifyRegister(REG_C, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x01),
            test.firstIsRegister(REG_D).verifyRegister(REG_D, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x02),
            test.firstIsRegister(REG_E).verifyRegister(REG_E, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x03),
            test.firstIsRegister(REG_H).verifyRegister(REG_H, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x04),
            test.firstIsRegister(REG_L).verifyRegister(REG_L, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x05),
            test.setPair(REG_PAIR_HL, 0x301).firstIsMemoryByteAt(0x301).verifyByte(0x301, operator)
                .verifyFlagsOfLastOp(flags).run(0xCB, 0x06)
        );
    }

    private IntegerTestBuilder prepareIXIYRotationMSBTest(Function<RunnerContext<Integer>, Integer> operator) {
        return new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .first8MSBplus8LSBisMemoryAddressAndSecondIsMemoryByte()
            .verifyByte(context -> get8MSBplus8LSB(context.first), operator)
            .verifyFlagsOfLastOp(new FlagsCheckImpl<>()
                .switchFirstAndSecond().carryIsFirstOperandMSB().sign().zero().halfCarryIsReset().parity()
                .subtractionIsReset())
            .setFlags(FLAG_H | FLAG_N);
    }

    private IntegerTestBuilder prepareIXIYRotationLSBTest(Function<RunnerContext<Integer>, Integer> operator) {
        return new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .first8MSBplus8LSBisMemoryAddressAndSecondIsMemoryByte()
            .verifyByte(context -> get8MSBplus8LSB(context.first), operator)
            .verifyFlagsOfLastOp(new FlagsCheckImpl<>()
                .switchFirstAndSecond().carryIsFirstOperandLSB().sign().zero().halfCarryIsReset().parity()
                .subtractionIsReset())
            .setFlags(FLAG_H | FLAG_N);
    }

    @Test
    public void testRLC__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareIXIYRotationMSBTest(
            context -> ((context.second << 1) & 0xFF) | (context.second >>> 7) & 1
        ).keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(4),
            test.first8MSBisIX().runWithFirst8bitOperandWithOpcodeAfter(06, 0xDD, 0xCB),
            test.first8MSBisIY().runWithFirst8bitOperandWithOpcodeAfter(06, 0xFD, 0xCB)
        );
    }

    @Test
    public void testRLC__IX_IY_plus_d_undocumented() {
        Function<RunnerContext<Integer>, Integer> operation =
            context -> ((context.second << 1) & 0xFF) | (context.second >>> 7) & 1;

        IntegerTestBuilder test = prepareIXIYRotationMSBTest(operation)
            .verifyRegister(REG_B, operation)
            .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(4),
            test.first8MSBisIX().runWithFirst8bitOperandWithOpcodeAfter(00, 0xDD, 0xCB),
            test.first8MSBisIY().runWithFirst8bitOperandWithOpcodeAfter(00, 0xFD, 0xCB)
        );
    }


    @Test
    public void testRL__r() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .setFlags(FLAG_H | FLAG_N)
            .keepCurrentInjectorsAfterRun()
            .clearOtherVerifiersAfterRun();

        Function<RunnerContext<Byte>, Integer> operator = context ->
            ((context.first << 1) & 0xFF) | (context.flags & FLAG_C);
        FlagsCheckImpl flags = new FlagsCheckImpl<>()
            .carryIsFirstOperandMSB().sign().zero().parity().halfCarryIsReset().subtractionIsReset();

        Generator.forSome8bitUnary(
            test.firstIsRegister(REG_A).verifyRegister(REG_A, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x17),
            test.firstIsRegister(REG_B).verifyRegister(REG_B, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x10),
            test.firstIsRegister(REG_C).verifyRegister(REG_C, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x11),
            test.firstIsRegister(REG_D).verifyRegister(REG_D, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x12),
            test.firstIsRegister(REG_E).verifyRegister(REG_E, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x13),
            test.firstIsRegister(REG_H).verifyRegister(REG_H, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x14),
            test.firstIsRegister(REG_L).verifyRegister(REG_L, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x15),
            test.setPair(REG_PAIR_HL, 0x301).firstIsMemoryByteAt(0x301).verifyByte(0x301, operator)
                .verifyFlagsOfLastOp(flags).run(0xCB, 0x16)
        );
    }

    @Test
    public void testRL__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareIXIYRotationMSBTest(
            context -> ((context.second << 1) & 0xFF) | (context.flags & FLAG_C)
        ).keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(4),
            test.first8MSBisIX().runWithFirst8bitOperandWithOpcodeAfter(0x16, 0xDD, 0xCB),
            test.first8MSBisIY().runWithFirst8bitOperandWithOpcodeAfter(0x16, 0xFD, 0xCB)
        );
    }

    @Test
    public void testRRC__r() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .setFlags(FLAG_H | FLAG_N)
            .keepCurrentInjectorsAfterRun()
            .clearOtherVerifiersAfterRun();

        Function<RunnerContext<Byte>, Integer> operator = context ->
            ((context.first >>> 1) & 0x7F) | (((context.first & 1) << 7));
        FlagsCheckImpl flags = new FlagsCheckImpl<>()
            .carryIsFirstOperandLSB().sign().zero().parity().halfCarryIsReset().subtractionIsReset();

        Generator.forSome8bitUnary(
            test.firstIsRegister(REG_A).verifyRegister(REG_A, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x0F),
            test.firstIsRegister(REG_B).verifyRegister(REG_B, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x08),
            test.firstIsRegister(REG_C).verifyRegister(REG_C, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x09),
            test.firstIsRegister(REG_D).verifyRegister(REG_D, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x0A),
            test.firstIsRegister(REG_E).verifyRegister(REG_E, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x0B),
            test.firstIsRegister(REG_H).verifyRegister(REG_H, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x0C),
            test.firstIsRegister(REG_L).verifyRegister(REG_L, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x0D),
            test.setPair(REG_PAIR_HL, 0x301).firstIsMemoryByteAt(0x301).verifyByte(0x301, operator)
                .verifyFlagsOfLastOp(flags).run(0xCB, 0x0E)
        );
    }

    @Test
    public void testRRC__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareIXIYRotationLSBTest(
            context -> ((context.second >>> 1) & 0x7F) | (((context.second & 1) << 7))
        ).keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(4),
            test.first8MSBisIX().runWithFirst8bitOperandWithOpcodeAfter(0x0E, 0xDD, 0xCB),
            test.first8MSBisIY().runWithFirst8bitOperandWithOpcodeAfter(0x0E, 0xFD, 0xCB)
        );
    }

    @Test
    public void testRR__r() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .setFlags(FLAG_H | FLAG_N)
            .keepCurrentInjectorsAfterRun()
            .clearOtherVerifiersAfterRun();

        Function<RunnerContext<Byte>, Integer> operator = context ->
            ((context.first >> 1) & 0x7F) | ((context.flags & FLAG_C) << 7);
        FlagsCheckImpl flags = new FlagsCheckImpl<>()
            .carryIsFirstOperandLSB().sign().zero().parity().halfCarryIsReset().subtractionIsReset();

        Generator.forSome8bitUnary(
            test.firstIsRegister(REG_A).verifyRegister(REG_A, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x1F),
            test.firstIsRegister(REG_B).verifyRegister(REG_B, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x18),
            test.firstIsRegister(REG_C).verifyRegister(REG_C, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x19),
            test.firstIsRegister(REG_D).verifyRegister(REG_D, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x1A),
            test.firstIsRegister(REG_E).verifyRegister(REG_E, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x1B),
            test.firstIsRegister(REG_H).verifyRegister(REG_H, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x1C),
            test.firstIsRegister(REG_L).verifyRegister(REG_L, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x1D),
            test.setPair(REG_PAIR_HL, 0x301).firstIsMemoryByteAt(0x301).verifyByte(0x301, operator)
                .verifyFlagsOfLastOp(flags).run(0xCB, 0x1E)
        );
    }

    @Test
    public void testRR__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareIXIYRotationLSBTest(
            context -> ((context.second >> 1) & 0x7F) | ((context.flags & FLAG_C) << 7)
        ).keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(4),
            test.first8MSBisIX().runWithFirst8bitOperandWithOpcodeAfter(0x1E, 0xDD, 0xCB),
            test.first8MSBisIY().runWithFirst8bitOperandWithOpcodeAfter(0x1E, 0xFD, 0xCB)
        );
    }

    @Test
    public void testSLA__r() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .setFlags(FLAG_H | FLAG_N)
            .keepCurrentInjectorsAfterRun()
            .clearOtherVerifiersAfterRun();

        Function<RunnerContext<Byte>, Integer> operator = context -> (context.first << 1) & 0xFE;
        FlagsCheckImpl flags = new FlagsCheckImpl<>()
            .carryIsFirstOperandMSB().sign().zero().parity().halfCarryIsReset().subtractionIsReset();

        Generator.forSome8bitUnary(
            test.firstIsRegister(REG_A).verifyRegister(REG_A, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x27),
            test.firstIsRegister(REG_B).verifyRegister(REG_B, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x20),
            test.firstIsRegister(REG_C).verifyRegister(REG_C, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x21),
            test.firstIsRegister(REG_D).verifyRegister(REG_D, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x22),
            test.firstIsRegister(REG_E).verifyRegister(REG_E, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x23),
            test.firstIsRegister(REG_H).verifyRegister(REG_H, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x24),
            test.firstIsRegister(REG_L).verifyRegister(REG_L, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x25),
            test.setPair(REG_PAIR_HL, 0x301).firstIsMemoryByteAt(0x301).verifyByte(0x301, operator)
                .verifyFlagsOfLastOp(flags).run(0xCB, 0x26)
        );
    }

    @Test
    public void testSLA__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareIXIYRotationMSBTest(context -> (context.second << 1) & 0xFE)
            .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(4),
            test.first8MSBisIX().runWithFirst8bitOperandWithOpcodeAfter(0x26, 0xDD, 0xCB),
            test.first8MSBisIY().runWithFirst8bitOperandWithOpcodeAfter(0x26, 0xFD, 0xCB)
        );
    }

    @Test
    public void testSRA__r() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .setFlags(FLAG_H | FLAG_N)
            .keepCurrentInjectorsAfterRun()
            .clearOtherVerifiersAfterRun();

        Function<RunnerContext<Byte>, Integer> operator = context -> (context.first >> 1) & 0xFF | (context.first & 0x80);
        FlagsCheckImpl flags = new FlagsCheckImpl<>()
            .carryIsFirstOperandLSB().sign().zero().parity().halfCarryIsReset().subtractionIsReset();

        Generator.forSome8bitUnary(
            test.firstIsRegister(REG_A).verifyRegister(REG_A, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x2F),
            test.firstIsRegister(REG_B).verifyRegister(REG_B, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x28),
            test.firstIsRegister(REG_C).verifyRegister(REG_C, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x29),
            test.firstIsRegister(REG_D).verifyRegister(REG_D, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x2A),
            test.firstIsRegister(REG_E).verifyRegister(REG_E, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x2B),
            test.firstIsRegister(REG_H).verifyRegister(REG_H, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x2C),
            test.firstIsRegister(REG_L).verifyRegister(REG_L, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x2D),
            test.setPair(REG_PAIR_HL, 0x301).firstIsMemoryByteAt(0x301).verifyByte(0x301, operator)
                .verifyFlagsOfLastOp(flags).run(0xCB, 0x2E)
        );
    }

    @Test
    public void testSRA__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareIXIYRotationLSBTest(
            context -> ((context.second & 0xFF) >>> 1) & 0xFF | (context.second & 0x80)
        ).keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(4),
            test.first8MSBisIX().runWithFirst8bitOperandWithOpcodeAfter(0x2E, 0xDD, 0xCB),
            test.first8MSBisIY().runWithFirst8bitOperandWithOpcodeAfter(0x2E, 0xFD, 0xCB)
        );
    }

    @Test
    public void testSRL__r() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .setFlags(FLAG_H | FLAG_N)
            .keepCurrentInjectorsAfterRun()
            .clearOtherVerifiersAfterRun();

        Function<RunnerContext<Byte>, Integer> operator = context -> (context.first >>> 1) & 0x7F;
        FlagsCheckImpl flags = new FlagsCheckImpl<>()
            .carryIsFirstOperandLSB().sign().zero().parity().halfCarryIsReset().subtractionIsReset();

        Generator.forSome8bitUnary(
            test.firstIsRegister(REG_A).verifyRegister(REG_A, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x3F),
            test.firstIsRegister(REG_B).verifyRegister(REG_B, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x38),
            test.firstIsRegister(REG_C).verifyRegister(REG_C, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x39),
            test.firstIsRegister(REG_D).verifyRegister(REG_D, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x3A),
            test.firstIsRegister(REG_E).verifyRegister(REG_E, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x3B),
            test.firstIsRegister(REG_H).verifyRegister(REG_H, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x3C),
            test.firstIsRegister(REG_L).verifyRegister(REG_L, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x3D),
            test.setPair(REG_PAIR_HL, 0x301).firstIsMemoryByteAt(0x301).verifyByte(0x301, operator)
                .verifyFlagsOfLastOp(flags).run(0xCB, 0x3E)
        );
    }

    @Test
    public void testSRL__IX_IY_plus_d() {
        IntegerTestBuilder test = prepareIXIYRotationLSBTest(context -> ((context.second & 0xFF) >>> 1) & 0x7F)
            .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(4),
            test.first8MSBisIX().runWithFirst8bitOperandWithOpcodeAfter(0x3E, 0xDD, 0xCB),
            test.first8MSBisIY().runWithFirst8bitOperandWithOpcodeAfter(0x3E, 0xFD, 0xCB)
        );
    }

    @Test
    public void testSLL__r() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .setFlags(FLAG_H | FLAG_N)
            .keepCurrentInjectorsAfterRun()
            .clearOtherVerifiersAfterRun();

        Function<RunnerContext<Byte>, Integer> operator = context -> (context.first << 1) & 0xFF | context.first & 1;
        FlagsCheckImpl flags = new FlagsCheckImpl<>()
            .carryIsFirstOperandMSB().sign().zero().parity().halfCarryIsReset().subtractionIsReset();

        Generator.forSome8bitUnary(
            test.firstIsRegister(REG_A).verifyRegister(REG_A, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x37),
            test.firstIsRegister(REG_B).verifyRegister(REG_B, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x30),
            test.firstIsRegister(REG_C).verifyRegister(REG_C, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x31),
            test.firstIsRegister(REG_D).verifyRegister(REG_D, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x32),
            test.firstIsRegister(REG_E).verifyRegister(REG_E, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x33),
            test.firstIsRegister(REG_H).verifyRegister(REG_H, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x34),
            test.firstIsRegister(REG_L).verifyRegister(REG_L, operator).verifyFlagsOfLastOp(flags).run(0xCB, 0x35),
            test.setPair(REG_PAIR_HL, 0x301).firstIsMemoryByteAt(0x301).verifyByte(0x301, operator)
                .verifyFlagsOfLastOp(flags).run(0xCB, 0x36)
        );
    }

    @Test
    public void testSLL_IX_IY_plus_d() throws Exception {
        IntegerTestBuilder test = prepareIXIYRotationMSBTest(context -> ((context.second << 1) & 0xFF) | context.second & 1)
            .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinaryFirstSatisfying(predicate8MSBplus8LSB(4),
            test.first8MSBisIX().runWithFirst8bitOperandWithOpcodeAfter(0x36, 0xDD, 0xCB),
            test.first8MSBisIY().runWithFirst8bitOperandWithOpcodeAfter(0x36, 0xFD, 0xCB)
        );
    }

    @Test
    public void testRLD() {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsAddressAndSecondIsMemoryByte()
            .firstIsPair(REG_PAIR_HL)
            .first8LSBisRegister(REG_A)
            .verifyByte(context -> context.first, context -> (context.second << 4) & 0xF0 | context.first & 0x0F)
            .verifyRegister(REG_A, context -> (context.first & 0xF0) | (context.second >>> 4) & 0x0F)
            .verifyFlagsOfLastOp(new FlagsCheckImpl<>().sign().zero().parity().subtractionIsReset().halfCarryIsReset());

        Generator.forSome16bitBinary(
            test.run(0xED, 0x6F)
        );
    }

    @Test
    public void testRRD() {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
            .firstIsAddressAndSecondIsMemoryByte()
            .firstIsPair(REG_PAIR_HL)
            .first8LSBisRegister(REG_A)
            .verifyByte(context -> context.first, context -> (context.first << 4) & 0xF0 | (context.second >>> 4) & 0x0F)
            .verifyRegister(REG_A, context -> (context.first & 0xF0) | context.second & 0x0F)
            .verifyFlagsOfLastOp(new FlagsCheckImpl<>().sign().zero().parity().subtractionIsReset().halfCarryIsReset());

        Generator.forSome16bitBinary(
            test.run(0xED, 0x67)
        );
    }

}
