package net.sf.emustudio.cpu.testsuite;

import net.sf.emustudio.cpu.testsuite.injectors.InstructionNoOperands;
import net.sf.emustudio.cpu.testsuite.injectors.InstructionOperand;
import net.sf.emustudio.cpu.testsuite.injectors.MemoryAddress;
import net.sf.emustudio.cpu.testsuite.injectors.MemoryByte;
import net.sf.emustudio.cpu.testsuite.injectors.MemoryWord;
import net.sf.emustudio.cpu.testsuite.runners.Runner;
import net.sf.emustudio.cpu.testsuite.runners.RunnerContext;
import net.sf.emustudio.cpu.testsuite.runners.RunnerInjector;
import net.sf.emustudio.cpu.testsuite.verifiers.FlagsVerifier;
import net.sf.emustudio.cpu.testsuite.verifiers.MemoryByteVerifier;
import net.sf.emustudio.cpu.testsuite.verifiers.MemoryWordVerifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class TestBuilder<K extends Number, SpecificTestBuilder extends TestBuilder,
        CpuRunnerType extends CpuRunner, CpuVerifierType extends CpuVerifier> {
    protected final CpuRunnerType cpuRunner;
    protected final CpuVerifierType cpuVerifier;
    protected final Runner runner;

    private final List<Consumer<RunnerContext<K>>> verifiers = new ArrayList<Consumer<RunnerContext<K>>>();
    private final List<Consumer<RunnerContext<K>>> verifiersToClearAfterRun = new ArrayList<Consumer<RunnerContext<K>>>();
    private boolean newVerifiersShallBeClearedAfterRun = false;

    protected Function<RunnerContext<K>, Integer> lastOperation;

    protected TestBuilder(CpuRunnerType cpuRunner, CpuVerifierType cpuVerifier) {
        this.cpuRunner = Objects.requireNonNull(cpuRunner);
        this.cpuVerifier = Objects.requireNonNull(cpuVerifier);
        this.runner = new Runner<K, CpuRunnerType>(cpuRunner);
    }

    public SpecificTestBuilder clearAllVerifiers() {
        verifiers.clear();
        verifiersToClearAfterRun.clear();
        return (SpecificTestBuilder)this;
    }

    protected void addVerifier(Consumer<RunnerContext<K>> verifier) {
        if (newVerifiersShallBeClearedAfterRun) {
            verifiersToClearAfterRun.add(verifier);
        } else {
            this.verifiers.add(verifier);
        }
    }

    protected void addVerifiers(Collection<Consumer<RunnerContext<K>>> verifiers) {
        if (newVerifiersShallBeClearedAfterRun) {
            verifiersToClearAfterRun.addAll(verifiers);
        } else {
            this.verifiers.addAll(verifiers);
        }
    }

    public SpecificTestBuilder verifyAll(Collection<Consumer<RunnerContext<K>>> verifiers) {
        addVerifiers(verifiers);
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder registerIsRandom(int register, int maxValue) {
        Random random = new Random();
        runner.injectFirst((tmpRunner, argument) -> cpuRunner.setRegister(register, random.nextInt(maxValue + 1)));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder printRegister(int register) {
        runner.injectBoth((runner, first, second) ->
                        System.out.println(String.format("REG_%d=%x", register, runner.getRegisters().get(register)))
        );
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder printOperands() {
        runner.injectBoth((runner, first, second) ->
                System.out.println(String.format("first=%x, second=%x", first, second))
        );
        return (SpecificTestBuilder)this;
    }

    public List<Consumer<RunnerContext<K>>> getVerifiers() {
        List<Consumer<RunnerContext<K>>> list = new ArrayList<>(verifiers);
        list.addAll(verifiersToClearAfterRun);
        return Collections.unmodifiableList(list);
    }

    public SpecificTestBuilder verifyFlags(FlagsBuilder flagsBuilder, Function<RunnerContext<K>, Integer> operator) {
        lastOperation = operator;
        return verifyFlagsOfLastOp(flagsBuilder);
    }

    public SpecificTestBuilder verifyFlagsOfLastOp(FlagsBuilder flagsBuilder) {
        if (lastOperation == null) {
            throw new IllegalStateException("Last operation is not set!");
        }
        Function<RunnerContext<K>, Integer> operation = lastOperation;
        addVerifier(new FlagsVerifier<K>(cpuVerifier, operation, flagsBuilder));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder verifyByte(int address, Function<RunnerContext<K>, Integer> operator) {
        lastOperation = operator;
        return verifyByte(address);
    }

    public SpecificTestBuilder verifyWord(Function<RunnerContext<K>, Integer> addressOperator,
                                          Function<RunnerContext<K>, Integer> operator) {
        lastOperation = operator;
        addVerifier(new MemoryWordVerifier(cpuVerifier, operator, addressOperator));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder verifyByte(int address) {
        return verifyByte(context -> address);
    }

    public SpecificTestBuilder verifyByte(Function<RunnerContext<K>, Integer> addressOperator,
                                          Function<RunnerContext<K>, Integer> operator) {
        lastOperation = operator;
        return verifyByte(addressOperator);
    }

    public SpecificTestBuilder verifyByte(Function<RunnerContext<K>, Integer> addressOperator) {
        if (lastOperation == null) {
            throw new IllegalStateException("Last operation is not set!");
        }
        addVerifier(new MemoryByteVerifier<K>(cpuVerifier, lastOperation, addressOperator));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder keepCurrentInjectorsAfterRun() {
        runner.keepCurrentInjectorsAfterClear();
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder clearOtherVerifiersAfterRun() {
        newVerifiersShallBeClearedAfterRun = true;
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder firstIsMemoryByteAt(int address) {
        runner.injectFirst(new MemoryByte(address));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder secondIsMemoryByteAt(int address) {
        runner.injectSecond(new MemoryByte(address));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder firstIsMemoryWordAt(int address) {
        runner.injectFirst(new MemoryWord(address));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder secondIsMemoryWordAt(int address) {
        runner.injectSecond(new MemoryWord(address));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder firstIsMemoryAddressByte(int value) {
        runner.injectFirst(new MemoryAddress(Byte.valueOf((byte) value)));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder secondIsMemoryAddressByte(int value) {
        runner.injectSecond(new MemoryAddress(Byte.valueOf((byte) value)));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder firstIsMemoryAddressWord(int value) {
        runner.injectFirst(new MemoryAddress(value));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder secondIsMemoryAddressWord(int value) {
        runner.injectSecond(new MemoryAddress(value));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder firstIsAddressAndSecondIsMemoryWord() {
        runner.injectBoth((runner, first, second) -> {
            runner.ensureProgramSize(first.intValue() + 4);
            runner.setByte(first.intValue(), second.intValue() & 0xFF);
            runner.setByte(first.intValue() + 1, (second.intValue() >>> 8) & 0xFF);
        });
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder firstIsAddressAndSecondIsMemoryByte() {
        runner.injectBoth((runner, first, second) -> {
            runner.ensureProgramSize(first.intValue() + 4);
            runner.setByte(first.intValue(), second.intValue() & 0xFF);
        });
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder setFlags(int flags) {
        runner.injectFirst((tmpRunner, argument) -> tmpRunner.setFlags(flags));
        return (SpecificTestBuilder)this;
    }

    public SpecificTestBuilder expandMemory(Function<Number, Integer> address) {
        runner.injectFirst((tmpRunner, argument) -> tmpRunner.ensureProgramSize(address.apply(argument)));
        return (SpecificTestBuilder)this;
    }

    public Test<K> run(int... instruction) {
        return create(new InstructionNoOperands<>(instruction), true);
    }

    public Test<K> runWithFirstOperand(int instruction) {
        return create(new InstructionOperand<K, CpuRunnerType>(instruction), true);
    }

    public Test<K> runWithSecondOperand(int instruction) {
        return create(new InstructionOperand<K, CpuRunnerType>(instruction), false);
    }

    protected Test<K> create(RunnerInjector<K, CpuRunnerType> instruction, boolean first) {
        List<Consumer<RunnerContext<K>>> allVerifiers = getVerifiers();
        if (allVerifiers.isEmpty()) {
            throw new IllegalStateException("At least one verifier must be set");
        }
        Runner<K, CpuRunnerType> tmpRunner = runner.clone();
        if (first) {
            tmpRunner.injectFirst(instruction);
        } else {
            tmpRunner.injectSecond(instruction);
        }
        runner.clearInjectors();
        verifiersToClearAfterRun.clear();
        return new Test<K>(tmpRunner, allVerifiers);
    }
}
