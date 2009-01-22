/*
 * MacroPseudoNode.java
 *
 * Created on Sobota, 2007, september 29, 13:44
 *
 * KEEP IT SIMPLE, STUPID
 * some things just: YOU AREN'T GONNA NEED IT
 */

package tree8080;

import compiler8080.HEXFileHandler;
import compiler8080.compileEnv;
import java.util.Vector;
import plugins.compiler.IMessageReporter;
import tree8080Abstract.ExprNode;
import tree8080Abstract.PseudoNode;

/**
 *
 * @author vbmacher
 */
public class MacroPseudoNode extends PseudoNode {
    private Vector<String> params; // macro parameters
    private Vector<ExprNode> call_params; // concrete parameters, they can change
    private Statement stat;
    private String mnemo;
    
    /** Creates a new instance of MacroPseudoNode */
    public MacroPseudoNode(String name, Vector<String> params, Statement s, int line,
            int column) {
        super(line,column);
        this.mnemo = name;
        if (params == null) this.params = new Vector<String>();
        else this.params = params;
        this.stat = s;
    }

    
    public String getName() { return mnemo; }
    
    public void setCallParams(Vector<ExprNode> params) { this.call_params = params; }

    /// compile time /// 
    public int getSize() { return 0; }
    public int getStatSize() { return stat.getSize(); }
    
    public void pass1(IMessageReporter r) throws Exception {
        stat.pass1(r); // pass1 creates block symbol table (local for block)
    }
    
    // for pass4
    private compileEnv newEnv;
    // this is macro expansion ! can be called only in MacroCallPseudo class
    // call parameters have to be set
    public int pass2(compileEnv env, int addr_start) throws Exception {
        newEnv = new compileEnv();
        // add local statement env to newEnv
        stat.getCompileEnv().copyTo(newEnv);
        env.copyTo(newEnv); // add parent statement env to newEnv
        // remove all existing definitions of params name (from level-up environment)
        for (int i = 0; i < params.size(); i++)
            newEnv.removeAllDefinitions(params.get(i));
        // check of call_params
        if (call_params == null) throw new Exception("[" + line + "," + column
                + "] Unknown macro parameters");
        if (call_params.size() != params.size())
            throw new Exception("[" + line + "," + column 
                    + "] Incorrect macro paramers count");
        // create/rewrite symbols => parameters as equ pseudo instructions
        for (int i = 0; i < params.size(); i++)
            newEnv.addEquDef(new EquPseudoNode(params.get(i),call_params.get(i),
            		line,column));
        return stat.pass2(newEnv, addr_start);
    }

    
    public void pass4(HEXFileHandler hex) throws Exception {
        stat.pass4(hex,newEnv);
    }

}
