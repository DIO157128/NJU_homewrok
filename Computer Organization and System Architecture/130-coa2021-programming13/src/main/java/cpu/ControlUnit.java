package cpu;

import cpu.instr.all_instrs.InstrFactory;
import cpu.instr.all_instrs.Instruction;
import cpu.registers.EIP;

public class ControlUnit {
    private static String ICC = "00";
    private static Instruction instruction;
    private static int currentOpcode = -1;

    public Object exec(){
        ICC = "00";
        int len = -1;
        while(ICC != "11"){
            switch (ICC){
                //取指令
                case "00":
                    //取出正确的操作码和指令
                    currentOpcode = CPU.instrFetch(CPU_State.eip.read(),1);
                    instruction = InstrFactory.getInstr(currentOpcode);
                    instruction.fetchInstr(CPU_State.eip.read(), currentOpcode);

                    assert ControlUnit.instruction != null;

                    if(instruction.isIndirectAddressing()){
                        ICC = "01";
                    }
                    else{
                        ICC = "10";
                    }
                    break;

                    //间接取值
                case "01":
                    instruction.fetchOperand();
                    ICC = "10";
                    break;

                    //执行
                case "10":
                    len = instruction.exec(currentOpcode);
                    if(len == -1){
                        ICC = "11";
                        break;
                    }
                    ((EIP) CPU_State.eip).plus(len);
                    ICC = "00";
                    break;
            }
        }
        return len;
    }

}
