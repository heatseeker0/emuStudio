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
package net.sf.emustudio.intel8080.gui;

import net.sf.emustudio.intel8080.impl.EmulatorEngine;

import javax.swing.table.AbstractTableModel;

class FlagsModel extends AbstractTableModel {
    private String[] flags = {"S", "Z", "A", "P", "C"};
    private int[] flagsI = {0, 0, 0, 0, 0};
    private EmulatorEngine cpu;

    FlagsModel(EmulatorEngine cpu) {
        this.cpu = cpu;
    }

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return flags[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (rowIndex) {
            case 0:
                return flags[columnIndex];
            case 1:
                return flagsI[columnIndex];
        }
        return null;
    }

    @Override
    public void fireTableDataChanged() {
        short F = cpu.flags;
        flagsI[0] = ((F & EmulatorEngine.FLAG_S) != 0) ? 1 : 0;
        flagsI[1] = ((F & EmulatorEngine.FLAG_Z) != 0) ? 1 : 0;
        flagsI[2] = ((F & EmulatorEngine.FLAG_AC) != 0) ? 1 : 0;
        flagsI[3] = ((F & EmulatorEngine.FLAG_P) != 0) ? 1 : 0;
        flagsI[4] = ((F & EmulatorEngine.FLAG_C) != 0) ? 1 : 0;
        super.fireTableDataChanged();
    }
}
