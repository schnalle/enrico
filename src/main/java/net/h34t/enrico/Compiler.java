package net.h34t.enrico;

import net.h34t.enrico.op.LabelOp;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes the intermediate representation of a program and turns it into byte code.
 */
public class Compiler {

    private boolean enableDebug;

    public static String toString(int[] byteCode) {
        StringBuilder sb = new StringBuilder();

        for (int code : byteCode)
            sb.append(code).append(" ");

        return sb.toString();
    }

    public Compiler enableDebugOutput(boolean enableDebug) {
        this.enableDebug = enableDebug;
        return this;
    }

    /**
     * Translates the program into (virtual) machine code.
     *
     * @return the emitted machine code
     */
    public int[] compile(Program program) {
        // first pass gathers the label offsets
        LabelOffsetTranslator lot = new LabelOffsetTranslator();
        int offset = 0;

        for (Operation op : program.getOperations()) {
            if (op instanceof LabelOp) {
                lot.put((Label) ((LabelOp) op).getLabel(), offset);
            } else {
                offset += op.length();
            }
        }

        // System.out.println(lot.toString());

        // second pass generates the actual byte code
        // after applying the label address translation if needed
        List<Integer> byteCode = new ArrayList<>();
        offset = 0;

        for (Operation op : program.getOperations()) {
            if (op instanceof Operation.AddressTranslator)
                ((Operation.AddressTranslator) op).translate(lot);

            int[] encOp = op.encode(lot);
            for (int iop : encOp) {
                byteCode.add(iop);
            }

            offset += encOp.length;

            if (enableDebug)
                System.out.printf("%d: %s%n", offset, op.toString());
        }

        int[] bc = new int[byteCode.size()];
        for (int i = 0, ii = bc.length; i < ii; i++)
            bc[i] = byteCode.get(i);

        return bc;
    }

}
