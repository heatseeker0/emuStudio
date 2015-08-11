package net.sf.emustudio.intel8080.impl.suite;

import net.sf.emustudio.cpu.testsuite.FlagsBuilder;

import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_AC;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_C;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_P;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_S;
import static net.sf.emustudio.intel8080.impl.EmulatorEngine.FLAG_Z;

public class FlagsBuilderImpl<T extends Number> extends FlagsBuilder<T, FlagsBuilderImpl> {

    public FlagsBuilderImpl sign() {
        evaluators.add((first, second, result) -> {
            if ((byte)result < 0) {
                expectedFlags |= FLAG_S;
            } else {
                expectedNotFlags |= FLAG_S;
            }
        });
        return this;
    }

    public FlagsBuilderImpl zero() {
        evaluators.add((first, second, result) -> {
            if ((byte)result == 0) {
                expectedFlags |= FLAG_Z;
            } else {
                expectedNotFlags |= FLAG_Z;
            }
        });
        return this;
    }

    public FlagsBuilderImpl parity() {
        evaluators.add((first, second, result) -> {
            int numberOf1 = 0;
            int intResult = result & 0xFF;

            for (int i = 0; i < 8; i++) {
                if ((intResult & 1) == 1) {
                    numberOf1++;
                }
                intResult = intResult >>> 1;
            }

            if (numberOf1 % 2 == 0) {
                expectedFlags |= FLAG_P;
            } else {
                expectedNotFlags |= FLAG_P;
            }
        });
        return this;
    }

    public FlagsBuilderImpl carry15() {
        evaluators.add((first, second, result) -> {
            if ((result & 0x10000) == 0x10000) {
                expectedFlags |= FLAG_C;
            } else {
                expectedNotFlags |= FLAG_C;
            }
        });
        return this;
    }

    public FlagsBuilderImpl carry() {
        evaluators.add((first, second, result) -> {
            if ((result & 0x100) == 0x100) {
                expectedFlags |= FLAG_C;
            } else {
                expectedNotFlags |= FLAG_C;
            }
        });
        return this;
    }

    public FlagsBuilderImpl carryIsFirstOperandMSB() {
        evaluators.add((first, second, result) -> {
            if ((first.intValue() & 0x80) == 0x80) {
                expectedFlags |= FLAG_C;
            } else {
                expectedNotFlags |= FLAG_C;
            }
        });
        return this;
    }

    public FlagsBuilderImpl carryIsFirstOperandLSB() {
        evaluators.add((first, second, result) -> {
            if ((first.intValue() & 1) == 1) {
                expectedFlags |= FLAG_C;
            } else {
                expectedNotFlags |= FLAG_C;
            }
        });
        return this;
    }

    public FlagsBuilderImpl carryIsReset() {
        evaluators.add((first, second, result) -> expectedNotFlags |= FLAG_C);
        return this;
    }

    private boolean isAuxCarry(int first, int sumWith) {
        int mask = sumWith & first;
        int xormask = sumWith ^ first;

        int C0 = mask & 1;
        int C1 = ((mask >>> 1) ^ (C0 & (xormask >>> 1))) & 1;
        int C2 = ((mask >>> 2) ^ (C1 & (xormask >>> 2))) & 1;
        int C3 = ((mask >>> 3) ^ (C2 & (xormask >>> 3))) & 1;

        return (C3 != 0);
    }

    public FlagsBuilderImpl auxCarry() {
        evaluators.add((first, second, result) -> {
            int firstInt = first.intValue();
            byte diff = (byte)((result - firstInt) & 0xFF);

            if (isAuxCarry(first.intValue(), diff)) {
                expectedFlags |= FLAG_AC;
            } else {
                expectedNotFlags |= FLAG_AC;
            }
        });
        return this;
    }

    public FlagsBuilderImpl auxCarryIsReset() {
        evaluators.add((first, second, result) -> expectedNotFlags |= FLAG_AC);
        return this;
    }

    public FlagsBuilderImpl auxCarryDAA() {
        evaluators.add((first, second, result) -> {
            int firstInt = first.intValue();
            int diff = (result - firstInt) & 0x0F;

            if ((diff == 6) && isAuxCarry(firstInt, 6)) {
                expectedFlags |= FLAG_AC;
            } else {
                expectedNotFlags |= FLAG_AC;
            }
        });
        return this;
    }

}