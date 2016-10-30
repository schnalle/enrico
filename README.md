# Enrico

Enrico is a weekend-project toy interpreter for an simplified assembly-like language.

All operations operate on the memory of the *virtual machine* (Italicised because it's not really a virtual machine i 
think, just a memory context. Is there a difference?).

The VM consists of:

* Four general purpose registers: a, b, c and d.
* The instruction register, which can be only manipulated by `jmp*`, the `call` and the `ret` operations.
  Otherwise it's incremented after every operation.
* Main memory: just an array of integers. Currently, integers are the only data type. It's accessed through the
  `load` and `save` operations.
* Stack: a simple stack, accessible through the `push`, `pop` and `peek` operations.
* CallStack: the `call` operation stores the current IP on the call stack. The `ret` operation pops it and resumes
  program execution at the next instruction.

## Program

A program consists of a list of operations plus a hash map for looking up the ips of labels.

## Parser

Parses valid program text (from a file, stream or string) into a program. One operation per line. 
Empty lines and everything after a `#` (comment) is discarded.

## Operations

### Arithmetic

* `add reg p1 p2`: sets reg to p1 + p2
* `sub reg p1 p2`: sets reg to p1 - p2
* `mul reg p1 p2`: sets reg to p1 * p2
* `div reg p1 p2`: sets reg to p1 / p2
* `mod reg p1 p2`: sets reg to p1 % p2 (the remainder)

### Register operations

* `set reg value`: sets the value of reg to value, which might be another register or a constant
* `swp reg reg`: swaps values of two registers

### Memory operations

* `load reg value`: sets the register to the value of the memory at offset `value`
* `save reg value`: sets the value of the memory at offset `value` to register
* `push reg`: pushes the register on the stack
* `pop reg`: pops the register from the stack
* `peek reg`: sets the register to the topmost element of the stack without removing it

### Program flow

* `jmp label`: an unconditional jump
* `jmpe label op1 op2`: jumps to label if op1 == op2
* `jmpne label op1 op2`: jumps to label if op1 != op2
* `jmpgt label op1 op2`: jumps to label if op1 > op2
* `jmplt label op1 op2`: jumps to label if op1 < op2
* `call label`: unconditional jump that stores the ip on the call stack, to be used in combination with `ret`
* `ret`: unconditional jump to one instruction after the last stored ip on the call stack
* `res op`: exits the program, returning the value of op
* `label label_name`: defines a label which can be used as the target of program flow operations

# Example programs

## Fibonacci

    # calculates the nth fibonacci number (0, 1, 1, 2, 3, 5, ...)

    load d 0            # the desired fib number is stored here
    jmpe def0 d 1       # special cases for 0
    jmpe def1 d 2       # and 1

    set c 2             # initialize the current iteration to 2, because 0 and 1 are special cases
    set a 0             # predefine the first two fib nums
    set b 1

    label loop
    add a a b           # add the two numbers before
    swp a b             # thanks to this swap we don't need memory
    add c c 1           # current iteration counter
    jmpne loop c d      # if the current iteration isn't there yet, repeat
    res b               # otherwise we've got a result!

    label def0
    res 0

    label def1
    res 1

to run the program:

    Program p = Parser.load(new File("fibonacci.enr"));

    // the memory is exactly 1 int large and contains the parameter for the fibonacci program (i.e. we want the 13th
    // fibonacci number)
    int result = new Interpreter().run(new VM(new int[]{13}), p)
    // result should now equal 144
    
## Notes

### What is this?

Weekend project - I was bored. I don't really know anything about VMs, compilers, interpreters, etc.
 
### Why "Enrico"?
 
Enrico is the name of a TV clown. And the project is a joke.