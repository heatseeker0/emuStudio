/*
 * KISS, YAGNI, DRY
 *
 * (c) Copyright 2015, Peter Jakubčo
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
package emustudio.gui.debugTable;

import emulib.emustudio.debugtable.DebugTable;
import emulib.plugins.cpu.DebugColumn;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Objects;

public class DebugTableImpl extends JTable implements DebugTable {
    public static final Color EVEN_ROW_COLOR = new Color(241, 245, 250);
    public static final Color TABLE_GRID_COLOR = new Color(0xd9d9d9);

    private final DebugTableModel debugModel;
    private final TextCellRenderer textRenderer;
    private final BooleanCellRenderer boolRenderer;

    public DebugTableImpl(DebugTableModel debugModel) {
        super();

        this.debugModel = Objects.requireNonNull(debugModel);
        this.textRenderer = new TextCellRenderer(debugModel);
        this.boolRenderer = new BooleanCellRenderer();

        setModel(debugModel);

        setDefaultRenderer(Boolean.class, boolRenderer);
        setDefaultRenderer(Object.class, textRenderer);
        setDefaultRenderer(String.class, textRenderer);

        setupBooleanCellEditorAndDefaultWidth();

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setOpaque(false);
        setGridColor(TABLE_GRID_COLOR);
        setIntercellSpacing(new Dimension(0, 0));
        // turn off grid painting as we'll handle this manually in order to paint
        // grid lines over the entire viewport.
        setShowGrid(false);
    }

    @Override
    public DebugTableModel getModel() {
        return debugModel;
    }

    private void setupBooleanCellEditorAndDefaultWidth() {
        int columnCount = debugModel.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            TableColumn tableColumn = getColumn(getColumnName(i));
            DebugColumn debugColumn = debugModel.getColumnAt(i);

            if (debugColumn.getClassType() == Boolean.class) {
                tableColumn.setCellEditor(new DefaultCellEditor(new BooleanEditComponent()));
            }
            if (debugColumn.getDefaultWidth() != -1)
            tableColumn.setPreferredWidth(debugColumn.getDefaultWidth());
        }
    }

    @Override
    public void refresh() {
        if (isEnabled()) {
            revalidate();
            repaint();
        }
    }

    @Override
    public void setCustomColumns(List<DebugColumn> customColumns) {
        debugModel.setColumns(customColumns);

    }

    @Override
    public void setDefaultColumns() {
        debugModel.setDefaultColumns();
    }
}
