package net.sf.emustudio.intel8080.impl.suite.runners;

import net.sf.emustudio.intel8080.impl.suite.CpuRunner;

import java.util.Objects;
import java.util.function.Function;

public class ImmediateWordWithMemoryByte implements Function<Integer, RunnerContext<Integer>> {
    private final CpuRunner runner;
    private final int instruction;
    private final byte value;

    public ImmediateWordWithMemoryByte(CpuRunner runner, int instruction, byte value) {
        this.runner = Objects.requireNonNull(runner);
        this.instruction = instruction;
        this.value = value;
    }

    @Override
    public RunnerContext<Integer> apply(Integer first) {
        int[] program = new int[first + 2];
        program[0] = instruction;
        program[1] = first & 0xFF;
        program[2] = (first >>> 8) & 0xFF;
        program[first] = value & 0xFF;
        runner.resetProgram(program);

        int PC = runner.getPC();
        int SP = runner.getSP();

        runner.step();
        return new RunnerContext<>(first, 0, 0, 0, PC, SP);
    }

}