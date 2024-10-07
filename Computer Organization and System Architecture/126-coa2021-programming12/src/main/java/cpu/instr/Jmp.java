package cpu.instr;

import cpu.CPU_State;
import cpu.alu.ALU;
import cpu.mmu.MMU;
import util.DataType;

public class Jmp implements Instruction {

    private final MMU mmu = MMU.getMMU();
    private int len = 0;
    private String instr;
    private  ALU alu=new ALU();

    public int exec(int opcode) {
        if (opcode == 0xe9) {
            len = 1 + 4;
            instr = String.valueOf(mmu.read(CPU_State.cs.read() + CPU_State.eip.read(), len));
            String rel = MMU.ToBitStream(instr.substring(1, 5));
            String dest = CPU_State.eip.read();
            CPU_State.eip.write(alu.add(new DataType(dest), new DataType(rel)).toString());
        }
        return len;
    }

}
