/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.tuke.emustudio.rasp.memory.gui;

import javax.swing.table.AbstractTableModel;
import sk.tuke.emustudio.rasp.memory.memoryitems.MemoryItem;
import sk.tuke.emustudio.rasp.memory.memoryitems.NumberMemoryItem;
import sk.tuke.emustudio.rasp.memory.memoryitems.RASPInstruction;
import sk.tuke.emustudio.rasp.memory.RASPMemoryContext;

/**
 * MODEL for the table with memory content.
 *
 * @author miso
 */
public class RASPTableModel extends AbstractTableModel {

    private final RASPMemoryContext memory;

    /**
     * Default constructor.
     *
     * @param memory the memory that will hold the content.
     */
    public RASPTableModel(final RASPMemoryContext memory) {
        this.memory = memory;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //only numeric value (at column 1) is editable
        return columnIndex == 1;
    }

    /**
     * Get number of items in memory.
     *
     * @return the number of items in memory
     */
    @Override
    public int getRowCount() {
        return memory.getSize();
    }

    /**
     * Get number of columns in memory table; it is 3: |"address" | "numeric
     * value" |"cell value"|
     *
     * @return 3
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /**
     * Returns string contained in the addess column (column No. 0).
     *
     * @param rowIndex the index of the row in the address column
     * @return string contained in the addess column
     */
    private String getAddressColumnText(int rowIndex) {
        String label = memory.getLabel(rowIndex);
        if (label != null) {
            return String.valueOf(rowIndex) + " " + label.toLowerCase();
        } else {
            return String.valueOf(rowIndex);
        }
    }

    /**
     * Returns string contained in the numeric value column (column No. 1)
     *
     * @param rowIndex the index of the row in the numeric value column
     * @return string contained in the numeric value column
     */
    private String getNumericValueColumnText(int rowIndex) {
        MemoryItem item = memory.read(rowIndex);
        if (item instanceof NumberMemoryItem) {
            return ((NumberMemoryItem) item).toString();
        } else if (item instanceof RASPInstruction) {
            return String.valueOf(((RASPInstruction) item).getCode());
        }
        return "";
    }

    /**
     * Returns string representation of the label at given address, if there is
     * any.
     *
     * @param address the address
     * @return string representation of the label at given address, if there is
     * any
     */
    private String addressToLabelString(int address) {
        String label = memory.getLabel(address);
        if (label != null) {
            int index = label.lastIndexOf(':');
            label = label.substring(0, index);
            return label.toLowerCase();
        } else {
            //if no label at the address, simply return the number
            return String.valueOf(address);
        }
    }

    /**
     * Returns string contained in the mnemonic column (column No. 2)
     *
     * @param rowIndex the index of the row in the mnemonic column
     * @return string contained in the mnemonic column
     */
    private String getMnemonicColumnValue(int rowIndex) {
        //get item at given position
        MemoryItem item = (MemoryItem) memory.read(rowIndex);
        //item is a number
        if (item instanceof NumberMemoryItem) {
            //item is NOT the ACCUMULATOR
            if (rowIndex != 0) {
                MemoryItem previousItem = memory.read(rowIndex - 1); //get the item at the previous position
                if (previousItem instanceof RASPInstruction) {
                    RASPInstruction instruction = (RASPInstruction) previousItem;
                    int code = instruction.getCode();
                    //the previos instruction is a jump instruction, so this number is an address
                    if (code == RASPInstruction.JMP || code == RASPInstruction.JZ || code == RASPInstruction.JGTZ) {
                        return addressToLabelString(((NumberMemoryItem) item).getValue());
                    } else {
                        return ((NumberMemoryItem) item).toString(); //previos item is NOT a jump instruction 
                    }
                } else {
                    return ((NumberMemoryItem) item).toString(); //previous item is not an instruction, simply return the number
                }

            } else {
                //item is the ACCUMULATOR
                return ((NumberMemoryItem) item).toString();
            }
        } //item is an instruction, so return its mnemonics
        else if (item instanceof RASPInstruction) {
            return ((RASPInstruction) item).getCodeStr();
        }
        return "";
    }

    /**
     * Returns the string representation of cell in the memory table cell at
     * given position.
     *
     * @param rowIndex
     * @param columnIndex
     * @return string representation of memory table cell at given position
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //column with addresses, optionally marked with label
        if (columnIndex == 0) {
            return getAddressColumnText(rowIndex);
        } //column with numeric value of the memory cell
        else if (columnIndex == 1) {
            return getNumericValueColumnText(rowIndex);
        } //column with mnemonic representation of the memory cell (mnemonic if it is an istruction, number if it is a number memory item)
        else {
            return getMnemonicColumnValue(rowIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Address";
            case 1:
                return "Numeric Value";
            case 2:
                return "Mnemonic";
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        try {
            int numberValue = Integer.parseInt((String) value);
            memory.write(rowIndex, new NumberMemoryItem(numberValue));
        } catch (NumberFormatException e) {
            //do nothing, invalid value was inserted
        }

    }

}
