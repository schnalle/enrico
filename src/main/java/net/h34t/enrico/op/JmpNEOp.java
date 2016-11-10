package net.h34t.enrico.op;

import net.h34t.enrico.*;
import net.h34t.enrico.Compiler;

public class JmpNEOp implements Operation, Compiler.AddressTranslator {

    private Ref label, op1, op2;

    public JmpNEOp(Ref label, Ref op1, Ref op2) {
        this.label = label;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Integer exec(VM vm) {
        if (this.op1.getValue(vm) != this.op2.getValue(vm))
            vm.ip = label.getValue(vm);
        else
            vm.next(3);

        return null;
    }

    @Override
    public int[] encode(Compiler lot) {
        return Encoder.encode(lot, JMPNE, label, op1, op2);
    }

    @Override
    public int length() {
        return 7;
    }

    @Override
    public void translate(LabelOffsetTranslator translator) {
        if (label instanceof Label)
            label = new Const(translator.get((Label) label));
    }

    @Override
    public String toString() {
        return "jmpne to " + label.toString() + " if " + op1.toString() + " != " + op2.toString();
    }
}
