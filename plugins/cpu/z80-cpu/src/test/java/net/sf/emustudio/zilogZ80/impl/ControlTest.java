/*
 * Copyright (C) 2015 Peter Jakubčo
 * KISS, YAGNI, DRY
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

import emulib.plugins.cpu.CPU;
import net.sf.emustudio.cpu.testsuite.Generator;
import net.sf.emustudio.zilogZ80.impl.suite.ByteTestBuilder;
import net.sf.emustudio.zilogZ80.impl.suite.IntegerTestBuilder;
import org.junit.Test;

import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.FLAG_C;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.FLAG_H;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.FLAG_N;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.FLAG_PV;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.FLAG_S;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.FLAG_Z;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.REG_A;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.REG_B;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.REG_C;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.REG_D;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.REG_E;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.REG_H;
import static net.sf.emustudio.zilogZ80.impl.EmulatorEngine.REG_L;

public class ControlTest extends InstructionsTest {

    @Test
    public void testEI_DI() throws Exception {
        cpuVerifierImpl.checkInterruptsAreDisabled(0);
        cpuRunnerImpl.setProgram(0xFB, 0xF3);
        cpuRunnerImpl.reset();

        cpuRunnerImpl.step();
        cpuVerifierImpl.checkInterruptsAreEnabled(0);

        cpuRunnerImpl.step();
        cpuVerifierImpl.checkInterruptsAreDisabled(0);
    }

    @Test
    public void testJP__nn__AND__JP_cc__nn() throws Exception {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsMemoryAddressWord(0)
                .verifyPC(context -> context.first)
                .verifyPair(REG_SP, context -> 0)
                .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitUnary(3,
                test.runWithFirstOperand(0xC3),
                test.runWithFirstOperand(0xC2),
                test.setFlags(FLAG_Z).runWithFirstOperand(0xCA),
                test.runWithFirstOperand(0xD2),
                test.setFlags(FLAG_C).runWithFirstOperand(0xDA),
                test.runWithFirstOperand(0xE2),
                test.setFlags(FLAG_PV).runWithFirstOperand(0xEA),
                test.runWithFirstOperand(0xF2),
                test.setFlags(FLAG_S).runWithFirstOperand(0xFA)
        );
    }

    @Test
    public void testNegative_JP_cc__nn() throws Exception {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsMemoryAddressWord(0)
                .verifyPC(context -> context.PC + 3)
                .verifyPair(REG_SP, context -> 0)
                .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitUnary(3,
                test.setFlags(FLAG_Z).runWithFirstOperand(0xC2),
                test.runWithFirstOperand(0xCA),
                test.setFlags(FLAG_C).runWithFirstOperand(0xD2),
                test.runWithFirstOperand(0xDA),
                test.setFlags(FLAG_PV).runWithFirstOperand(0xE2),
                test.runWithFirstOperand(0xEA),
                test.setFlags(FLAG_S).runWithFirstOperand(0xF2),
                test.runWithFirstOperand(0xFA)
        );
    }

    @Test
    public void testJP__mHL() throws Exception {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsMemoryAddressWord(0)
                .firstIsPair(REG_PAIR_HL)
                .verifyPC(context -> context.first);

        Generator.forSome16bitUnary(1,
                test.run(0xE9)
        );
    }

    @Test
    public void testCALL__nn__AND__CALL_cc__nn() throws Exception {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsMemoryAddressWord(0)
                .secondIsPair(REG_SP)
                .verifyPair(REG_SP, context -> (context.second - 2) & 0xFFFF)
                .verifyPC(context -> context.first)
                .verifyWord(context -> (context.second - 2) & 0xFFFF, context -> context.PC + 3)
                .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinary(3,5,
                test.runWithFirstOperand(0xCD),
                test.runWithFirstOperand(0xC4),
                test.setFlags(FLAG_Z).runWithFirstOperand(0xCC),
                test.runWithFirstOperand(0xD4),
                test.setFlags(FLAG_C).runWithFirstOperand(0xDC),
                test.runWithFirstOperand(0xE4),
                test.setFlags(FLAG_PV).runWithFirstOperand(0xEC),
                test.runWithFirstOperand(0xF4),
                test.setFlags(FLAG_S).runWithFirstOperand(0xFC)
        );
    }

    @Test
    public void testNegative_CALL_cc__nn() throws Exception {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsMemoryAddressWord(0)
                .secondIsPair(REG_SP)
                .verifyPair(REG_SP, context -> context.second)
                .verifyPC(context -> context.PC + 3)
                .verifyWord(context -> context.second, context -> 0)
                .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinary(3,5,
                test.setFlags(FLAG_Z).runWithFirstOperand(0xC4),
                test.runWithFirstOperand(0xCC),
                test.setFlags(FLAG_C).runWithFirstOperand(0xD4),
                test.runWithFirstOperand(0xDC),
                test.setFlags(FLAG_PV).runWithFirstOperand(0xE4),
                test.runWithFirstOperand(0xEC),
                test.setFlags(FLAG_S).runWithFirstOperand(0xF4),
                test.runWithFirstOperand(0xFC)
        );
    }

    @Test
    public void testRET__AND__RET__cc() throws Exception {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsAddressAndSecondIsMemoryWord()
                .firstIsPair(REG_SP)
                .verifyPC(context -> context.second)
                .verifyPair(REG_SP, context -> (context.first + 2) & 0xFFFF)
                .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinary(1,
                test.run(0xC9),
                test.run(0xC0),
                test.setFlags(FLAG_Z).run(0xC8),
                test.run(0xD0),
                test.setFlags(FLAG_C).run(0xD8),
                test.run(0xE0),
                test.setFlags(FLAG_PV).run(0xE8),
                test.run(0xF0),
                test.setFlags(FLAG_S).run(0xF8)
        );
    }

    @Test
    public void testNegative_RET__cc() throws Exception {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsAddressAndSecondIsMemoryWord()
                .firstIsPair(REG_SP)
                .verifyPC(context -> context.PC + 1)
                .verifyPair(REG_SP, context -> context.first)
                .keepCurrentInjectorsAfterRun();

        Generator.forSome16bitBinary(1,
                test.setFlags(FLAG_Z).run(0xC0),
                test.run(0xC8),
                test.setFlags(FLAG_C).run(0xD0),
                test.run(0xD8),
                test.setFlags(FLAG_PV).run(0xE0),
                test.run(0xE8),
                test.setFlags(FLAG_S).run(0xF0),
                test.run(0xF8)
        );
    }

    @Test
    public void testRST() throws Exception {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsMemoryAddressWord(0)
                .firstIsPair(REG_SP)
                .verifyPair(REG_SP, context -> (context.first - 2) & 0xFFFF)
                .verifyWord(context -> (context.first - 2) & 0xFFFF, context -> context.PC + 1)
                .keepCurrentInjectorsAfterRun()
                .clearOtherVerifiersAfterRun();

        Generator.forSome16bitUnary(3,
                test.verifyPC(context -> 0).run(0xC7),
                test.verifyPC(context -> 8).run(0xCF),
                test.verifyPC(context -> 0x10).run(0xD7),
                test.verifyPC(context -> 0x18).run(0xDF),
                test.verifyPC(context -> 0x20).run(0xE7),
                test.verifyPC(context -> 0x28).run(0xEF),
                test.verifyPC(context -> 0x30).run(0xF7),
                test.verifyPC(context -> 0x38).run(0xFF)
        );
    }

    @Test
    public void testHLT() throws Exception {
        cpuRunnerImpl.setProgram(0x76);
        cpuRunnerImpl.reset();
        cpuRunnerImpl.expectRunState(CPU.RunState.STATE_STOPPED_NORMAL);
        cpuRunnerImpl.step();
    }

    @Test
    public void testInvalidInstruction() throws Exception {
        cpuRunnerImpl.setProgram(0xED, 0x55);
        cpuRunnerImpl.reset();
        cpuRunnerImpl.expectRunState(CPU.RunState.STATE_STOPPED_BAD_INSTR);
        cpuRunnerImpl.step();
    }

    @Test
    public void testNOP() {
        cpuRunnerImpl.setProgram(0x00);
        cpuRunnerImpl.reset();

        cpuRunnerImpl.step();

        cpuVerifierImpl.checkInterruptsAreDisabled(0);
        cpuVerifierImpl.checkIX(0);
        cpuVerifierImpl.checkIY(0);
        cpuVerifierImpl.checkNotFlags(FLAG_C | FLAG_H | FLAG_N | FLAG_PV | FLAG_S | FLAG_Z);
        cpuVerifierImpl.checkRegister(REG_A, 0);
        cpuVerifierImpl.checkRegister(REG_B, 0);
        cpuVerifierImpl.checkRegister(REG_C, 0);
        cpuVerifierImpl.checkRegister(REG_D, 0);
        cpuVerifierImpl.checkRegister(REG_E, 0);
        cpuVerifierImpl.checkRegister(REG_H, 0);
        cpuVerifierImpl.checkRegister(REG_L, 0);
        cpuVerifierImpl.checkRegisterPair(REG_PAIR_BC, 0);
        cpuVerifierImpl.checkRegisterPair(REG_PAIR_HL, 0);
        cpuVerifierImpl.checkRegisterPair(REG_SP, 0);
        cpuVerifierImpl.checkPC(1);
    }

    @Test
    public void testIM__0() {
        cpuRunnerImpl.setProgram(0xED, 0x46);
        cpuRunnerImpl.reset();

        cpuRunnerImpl.step();

        cpuVerifierImpl.checkIntMode(0);
    }

    @Test
    public void testIM__1() {
        cpuRunnerImpl.setProgram(0xED, 0x56);
        cpuRunnerImpl.reset();

        cpuRunnerImpl.step();

        cpuVerifierImpl.checkIntMode(1);
    }

    @Test
    public void testIM__2() {
        cpuRunnerImpl.setProgram(0xED, 0x5E);
        cpuRunnerImpl.reset();

        cpuRunnerImpl.step();

        cpuVerifierImpl.checkIntMode(2);
    }

    @Test
    public void testJR__e__AND__JR__cc() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .expandMemory(first -> cpuRunnerImpl.getPC() + first.intValue())
                .verifyPC(context -> (2 + context.PC + context.first) & 0xFFFF)
                .keepCurrentInjectorsAfterRun();

        Generator.forSome8bitUnary(
                test.runWithFirstOperand(0x18),
                test.runWithFirstOperand(0x20),
                test.setFlags(FLAG_Z).runWithFirstOperand(0x28),
                test.runWithFirstOperand(0x30),
                test.setFlags(FLAG_C).runWithFirstOperand(0x38)
        );
    }

    @Test
    public void testNegative__JR__e__AND__JR__cc() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .expandMemory(first -> cpuRunnerImpl.getPC() + first.intValue())
                .verifyPC(context -> (context.PC + 2) & 0xFFFF)
                .keepCurrentInjectorsAfterRun();

        Generator.forSome8bitUnary(
                test.setFlags(FLAG_Z).runWithFirstOperand(0x20),
                test.runWithFirstOperand(0x28),
                test.setFlags(FLAG_C).runWithFirstOperand(0x30),
                test.runWithFirstOperand(0x38)
        );
    }

    @Test
    public void testJP__IX() {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsIX()
                .verifyPC(context -> context.first);

        Generator.forSome16bitUnary(
                test.run(0xDD, 0xE9)
        );
    }

    @Test
    public void testJP__IY() {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsIY()
                .verifyPC(context -> context.first);

        Generator.forSome16bitUnary(
                test.run(0xFD, 0xE9)
        );
    }

    @Test
    public void testDJNZ__e() {
        ByteTestBuilder test = new ByteTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .secondIsRegister(REG_B)
                .expandMemory(first -> cpuRunnerImpl.getPC() + first.intValue())
                .verifyPC(context -> {
                    if (((context.second - 1) & 0xFF) == 0) {
                        return context.PC + 2;
                    }
                    return (context.PC + 2 + context.first) & 0xFFFF;
                })
                .verifyRegister(REG_B, context -> (context.second - 1) & 0xFF);

        Generator.forSome8bitBinary(
                test.runWithFirstOperand(0x10)
        );
    }

    @Test
    public void testRETI() {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsAddressAndSecondIsMemoryWord()
                .firstIsPair(REG_SP)
                .verifyPC(context -> context.second)
                .verifyPair(REG_SP, context -> (context.first + 2) & 0xFFFF);

        Generator.forSome16bitBinary(2,
                test.run(0xED, 0x4D)
        );
    }

    @Test
    public void testRETN() {
        IntegerTestBuilder test = new IntegerTestBuilder(cpuRunnerImpl, cpuVerifierImpl)
                .firstIsAddressAndSecondIsMemoryWord()
                .firstIsPair(REG_SP)
                .verifyPC(context -> context.second)
                .verifyPair(REG_SP, context -> (context.first + 2) & 0xFFFF)
                .enableIFF2()
                .disableIFF1()
                .verifyIFF1isEnabled();

        Generator.forSome16bitBinary(2,
                test.run(0xED, 0x45)
        );
    }

}