package net.h34t.enrico.op;

import net.h34t.enrico.Operation;
import net.h34t.enrico.Program;
import net.h34t.enrico.Ref;
import net.h34t.enrico.VM;

public class JmpEOp implements Operation {

    private final Ref label;
    private final Ref op1, op2;

    public JmpEOp(Ref label, Ref op1, Ref op2) {
        this.label = label;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public Integer exec(VM vm, Program program) {
        if (this.op1.getValue(vm) == this.op2.getValue(vm))
            vm.ip = label.getValue(vm);
        else
            vm.next();

        return null;
    }

    @Override
    public String toString() {
        return "jmp to " + label.toString() + " if " + op1.toString() + " = " + op2.toString();
    }

}
