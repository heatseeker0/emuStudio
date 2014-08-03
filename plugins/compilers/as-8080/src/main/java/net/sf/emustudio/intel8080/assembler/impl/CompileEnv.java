/*
 * CompileEnv.java
 *
 * Created on Pondelok, 2007, október 8, 18:08
 *
 * Copyright (C) 2007-2012 Peter Jakubčo
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
package net.sf.emustudio.intel8080.assembler.impl;

import java.util.ArrayList;
import java.util.List;
import net.sf.emustudio.intel8080.assembler.tree.EquPseudoNode;
import net.sf.emustudio.intel8080.assembler.tree.InstructionNode;
import net.sf.emustudio.intel8080.assembler.tree.LabelNode;
import net.sf.emustudio.intel8080.assembler.tree.MacroPseudoNode;
import net.sf.emustudio.intel8080.assembler.tree.SetPseudoNode;

/**
 * Compile environment.
 *
 * It stores needed values for all compiler passes. This is something like symbol table.
 *
 * Sets, macros and equs are pseudoinstructions that aren't added into symbol table in pass1. This means if eg. equ
 * wasn't defined before first use, error raises.
 *
 * @author vbmacher
 */
public class CompileEnv {

    private List<LabelNode> defLabels;        // labelnode objects
    private List<MacroPseudoNode> defMacros;  // all macros
    private List<EquPseudoNode> defEqus;      // all equs
    private List<SetPseudoNode> defSets;      // all sets
    private List<InstructionNode> passNeed;   // objects that need more passes

    /**
     * Creates a new instance of CompileEnv
     */
    public CompileEnv() {
        defLabels = new ArrayList<LabelNode>();
        defMacros = new ArrayList<MacroPseudoNode>();
        defEqus = new ArrayList<EquPseudoNode>();
        defSets = new ArrayList<SetPseudoNode>();
        passNeed = new ArrayList<InstructionNode>();
    }

    // check if id is already defined (as whatever)
    private boolean idExists(String name) {
        for (int i = defLabels.size() - 1; i >= 0; i--) {
            LabelNode in = (LabelNode) defLabels.get(i);
            if (in.getName().equals(name)) {
                return true;
            }
        }
        for (int i = defMacros.size() - 1; i >= 0; i--) {
            MacroPseudoNode mn = (MacroPseudoNode) defMacros.get(i);
            if (mn.getName().equals(name)) {
                return true;
            }
        }
        for (int i = defEqus.size() - 1; i >= 0; i--) {
            EquPseudoNode mn = (EquPseudoNode) defEqus.get(i);
            if (mn.getName().equals(name)) {
                return true;
            }
        }
        for (int i = defSets.size() - 1; i >= 0; i--) {
            SetPseudoNode mn = (SetPseudoNode) defSets.get(i);
            if (mn.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    //----
    public boolean addLabelDef(LabelNode l) {
        if (idExists(l.getName()) == true) {
            return false;
        }
        defLabels.add(l);
        return true;
    }

    public LabelNode getLabel(String name) {
        for (int i = 0; i < defLabels.size(); i++) {
            LabelNode label = (LabelNode) defLabels.get(i);
            if (label.getName().equals(name)) {
                return label;
            }
        }
        return null;
    }

    //-----
    public boolean addMacroDef(MacroPseudoNode m) {
        if (idExists(m.getName()) == true) {
            return false;
        }
        defMacros.add(m);
        return true;
    }

    // search for macro definition in symbol table
    public MacroPseudoNode getMacro(String name) {
        for (int i = 0; i < defMacros.size(); i++) {
            MacroPseudoNode mac = (MacroPseudoNode) defMacros.get(i);
            if (mac.getName().equals(name)) {
                return mac;
            }
        }
        return null;
    }

    //---
    public boolean addEquDef(EquPseudoNode e) {
        if (idExists(e.getName()) == true) {
            return false;
        }
        defEqus.add(e);
        return true;
    }

    public EquPseudoNode getEqu(String name) {
        for (int i = 0; i < defEqus.size(); i++) {
            EquPseudoNode equ = (EquPseudoNode) defEqus.get(i);
            if (equ.getName().equals(name)) {
                return equ;
            }
        }
        return null;
    }

    //---
    // prida alebo prepise existujucu definiciu
    // pridava sa samozrejme az v pass2
    public boolean addSetDef(SetPseudoNode s) {
        SetPseudoNode exs = getSet(s.getName());
        if (exs != null) {
            defSets.remove(exs);
        }
        if (idExists(s.getName()) == true) {
            return false;
        }
        defSets.add(s);
        return true;
    }

    public SetPseudoNode getSet(String name) {
        for (int i = 0; i < defSets.size(); i++) {
            SetPseudoNode set = (SetPseudoNode) defSets.get(i);
            if (set.getName().equals(name)) {
                return set;
            }
        }
        return null;
    }

    //---
    // odstrani vsetky existujuce definicie s danym nazvom
    // vyuziva sa pri bloku macro
    public void removeAllDefinitions(String name) {
        for (int i = defLabels.size() - 1; i >= 0; i--) {
            LabelNode in = (LabelNode) defLabels.get(i);
            if (in.getName().equals(name)) {
                defLabels.remove(i);
            }
        }
        for (int i = defMacros.size() - 1; i >= 0; i--) {
            MacroPseudoNode mn = (MacroPseudoNode) defMacros.get(i);
            if (mn.getName().equals(name)) {
                defMacros.remove(i);
            }
        }
        for (int i = defEqus.size() - 1; i >= 0; i--) {
            EquPseudoNode mn = (EquPseudoNode) defEqus.get(i);
            if (mn.getName().equals(name)) {
                defEqus.remove(i);
            }
        }
        for (int i = defSets.size() - 1; i >= 0; i--) {
            SetPseudoNode mn = (SetPseudoNode) defSets.get(i);
            if (mn.getName().equals(name)) {
                defSets.remove(i);
            }
        }
    }

    public boolean copyTo(CompileEnv env) {
        boolean r = true;
        for (int i = 0; i < defLabels.size(); i++) {
            r &= env.addLabelDef((LabelNode) defLabels.get(i));
        }
        for (int i = 0; i < defMacros.size(); i++) {
            r &= env.addMacroDef((MacroPseudoNode) defMacros.get(i));
        }
        for (int i = 0; i < defEqus.size(); i++) {
            r &= env.addEquDef((EquPseudoNode) defEqus.get(i));
        }
        for (int i = 0; i < defSets.size(); i++) {
            r &= env.addSetDef((SetPseudoNode) defSets.get(i));
        }
        return r;
    }
    //---

    public void addPassNeed(InstructionNode n) {
        passNeed.add(n);
    }

    public int getPassNeedCount() {
        return passNeed.size();
    }

    public InstructionNode getPassNeed(int index) {
        return (InstructionNode) passNeed.get(index);
    }

    public void removePassNeed(InstructionNode n) {
        passNeed.remove(n);
    }

    public void removePassNeed(int index) {
        passNeed.remove(index);
    }

    public void clearPassNeeds() {
        passNeed.clear();
    }
}