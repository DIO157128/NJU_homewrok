package cpu.instr.all_instrs;

public class Hlt implements Instruction{
    @Override
    public int exec(int opcode) {
        //toBinaryStr("11110100");
        //通过 指令长度为 -1 来完成停机操作
        return -1;
    }

    @Override
    public String fetchInstr(String eip, int opcode) {
        return null;
    }

    @Override
    public boolean isIndirectAddressing() {
        return false;
    }

    @Override
    public void fetchOperand() {

    }
}
