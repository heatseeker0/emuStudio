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
package net.sf.emustudio.memory.standard.impl;

import net.sf.emustudio.memory.standard.StandardMemoryContext;

public class AddressRangeImpl implements StandardMemoryContext.AddressRange {
    private final int startAddress;
    private final int stopAddress;

    public AddressRangeImpl(int startAddress, int stopAddress) {
        if (startAddress < 0 || stopAddress < 0) {
            throw new IllegalArgumentException("Start address and stop address must be >= 0!");
        }
        if (stopAddress < startAddress) {
            throw new IllegalArgumentException("Start address must be <= stop address!");
        }

        this.startAddress = startAddress;
        this.stopAddress = stopAddress;
    }

    @Override
    public int getStartAddress() {
        return startAddress;
    }

    @Override
    public int getStopAddress() {
        return stopAddress;
    }

    @Override
    public int compareTo(StandardMemoryContext.AddressRange o) {
        if (o == this) {
            return 0;
        }
        if (startAddress < o.getStartAddress()) {
            return -1;
        } else if (startAddress > o.getStartAddress()) {
            return 1;
        } else {
            return 0;
        }
    }
}
