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
package net.sf.emustudio.ssem.assembler.tree;

import java.util.HashMap;
import java.util.Map;

public class Program implements ASTnode {
    private final Map<Integer, ASTnode> nodes = new HashMap<>();
    private int startLine = 0;
    private int previousLine = 0;

    public void statement(int line, ASTnode node) {
        previousLine = line;
        nodes.put(line, node);
    }
    
    public void nextLineStarts() {
        this.startLine = previousLine;
    }
    
    public int getStartLine() {
        return startLine;
    }

    @Override
    public void accept(ASTvisitor visitor) throws Exception {
        for (Map.Entry<Integer, ASTnode> node : nodes.entrySet()) {
            visitor.setCurrentLine(node.getKey());
            node.getValue().accept(visitor);
        }
    }
}
