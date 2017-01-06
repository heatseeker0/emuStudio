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
package net.sf.emustudio.ram.compiler.impl;

import net.sf.emustudio.ram.compiler.tree.Label;

import java.util.ArrayList;
import java.util.List;

public class CompilerEnvironment {
    private static List<Label> labels = new ArrayList<>();
    private static List<String> inputs = new ArrayList<>();

    public static void addLabel(Label label) {
        labels.add(label);
    }

    public static int getLabelAddr(String label) {
        String l = label.toUpperCase() + ":";
        for (Label lab : labels) {
            String ll = lab.getValue().toUpperCase();
            if (ll.equals(l)) {
                return lab.getAddress();
            }
        }
        // throw new ...label undefined
        return -1;
    }

    public static List<Label> getLabels() {
        return labels;
    }

    public static void addInputs(List<String> inp) {
        inputs.addAll(inp);
    }

    public static List<String> getInputs() {
        return inputs;
    }

    public static void clear() {
        inputs.clear();
        labels.clear();
    }
}
