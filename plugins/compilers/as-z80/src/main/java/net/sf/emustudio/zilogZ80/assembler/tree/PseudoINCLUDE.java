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
package net.sf.emustudio.zilogZ80.assembler.tree;

import emulib.runtime.HEXFileManager;
import java_cup.runtime.ComplexSymbolFactory;
import net.sf.emustudio.zilogZ80.assembler.exceptions.CompilerException;
import net.sf.emustudio.zilogZ80.assembler.exceptions.UnexpectedEOFException;
import net.sf.emustudio.zilogZ80.assembler.impl.CompilerImpl;
import net.sf.emustudio.zilogZ80.assembler.impl.LexerImpl;
import net.sf.emustudio.zilogZ80.assembler.impl.Namespace;
import net.sf.emustudio.zilogZ80.assembler.impl.ParserImpl;
import net.sf.emustudio.zilogZ80.assembler.treeAbstract.Pseudo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PseudoINCLUDE extends Pseudo {
    private final String fileName;
    private Program program;
    private Namespace namespace;
    private final CompilerImpl compiler;

    public PseudoINCLUDE(String filename, int line, int column, CompilerImpl compiler) {
        super(line, column);
        this.fileName = filename.replace("\\", File.separator);
        this.compiler = compiler;
    }

    @Override
    public int getSize() {
        return program.getSize();
    }

    private File findIncludeFile(String tmpFileName) {
        File tmpFile = new File(tmpFileName);
        if (tmpFile.isAbsolute()) {
            return tmpFile;
        } else {
            return new File(namespace.getInputFile().getParent()
                + File.separator + tmpFileName);
        }
    }


    /**
     * Method compare filename (in the include statement)
     * with filename given by the parameter
     *
     * @return true if filenames equal, false if not
     */
    public boolean isEqualName(String tmpFileName) {
        return findIncludeFile(fileName).equals(findIncludeFile(tmpFileName));
    }

    @Override
    public void pass1() throws Exception {
    }

    public void pass1(List<String> includefiles, Namespace parent) throws Exception {
        try {
            namespace = new Namespace(parent.getInputFile().getAbsolutePath());

            File file = findIncludeFile(fileName);

            FileReader f = new FileReader(file);
            LexerImpl lexer = new LexerImpl(f);
            ParserImpl parser = new ParserImpl(lexer, new ComplexSymbolFactory());
            parser.setCompiler(compiler);

            parser.setReportPrefixString(file.getName() + ": ");
            Object s = parser.parse().value;
            parser.setReportPrefixString(null);
            if (s == null) {
                throw new UnexpectedEOFException(line, column, file.getAbsolutePath());
            }
            program = (Program) s;
            program.addIncludeFiles(includefiles);
            namespace = parent;

            if (program.getIncludeLoops(fileName)) {
                throw new CompilerException(line, column, "Error: Infinite INCLUDE loop (" + file.getAbsolutePath() + ")");
            }
            program.pass1(namespace); // create symbol table
        } catch (IOException e) {
            throw new Exception(fileName + ": I/O Error");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("[" + line + "," + column + "] " + e.getMessage());
        }
    }

    @Override
    public int pass2(Namespace parentEnv, int addr_start) throws Exception {
        // try to evaluate all expressions + compute relative addresses
        return program.pass2(addr_start);
    }

    @Override
    public void generateCode(HEXFileManager hex) throws Exception {
        while (program.pass3(namespace) == true) {
            // :-)
        }
        if (namespace.getPassNeedCount() != 0) {
            throw new Exception("Error: can't evaulate all expressions");
        }
        program.pass4(hex);
    }
}
