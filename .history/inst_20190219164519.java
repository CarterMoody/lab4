 /*
    Name:         Robert Hensley, Carter Moody
    Section:      09
    Description:  an object that stores properties of an opcode instruction
*/

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class inst {

    // This Map is used to Translate Registers to Their Binary Representaiton in 5 bit Strings
    // fix serializable error
    private static final Map<String, String> registerMap = new HashMap<String, String>() {{
        put("$0", "00000");     // Register Zero
        put("$zero", "00000");
        put("$Zero", "00000");

        put("$1", "00001");     // $at, WE DO NOT NEED TO SUPPORT THIS REGISTER

        put("$2", "00010");     //Return Values from Functions
        put("$v0", "00010");
        put("$3", "00011");
        put("$v1", "00011");

        put("$4", "00100");      // Arguments to Functions $a0-$a3
        put("$a0", "00100");
        put("$5", "00101");
        put("$a1", "00101");
        put("$6", "00110");
        put("$a2", "00110");
        put("$7", "00111");
        put("$a3", "00111");

        put("$8", "01000");     // Temporary Registers $t0-$t7
        put("$t0", "01000");
        put("$9", "01001");
        put("$t1", "01001");
        put("$10", "01010");
        put("$t2", "01010");
        put("$11", "01011");
        put("$t3", "01011");
        put("$12", "01100");
        put("$t4", "01100");
        put("$13", "01101");
        put("$t5", "01101");
        put("$14", "01110");
        put("$t6", "01110");
        put("$15", "01111");
        put("$t7", "01111");

        put("$16", "10000");    // Saved Registers $s0-$s7
        put("$s0", "10000");             
        put("$17", "10001");
        put("$s1", "10001"); 
        put("$18", "10010");
        put("$s2", "10010"); 
        put("$19", "10011");
        put("$s3", "10011"); 
        put("$20", "10100");
        put("$s4", "10100"); 
        put("$21", "10101");
        put("$s5", "10101"); 
        put("$22", "10110");
        put("$s6", "10110"); 
        put("$23", "10111");
        put("$s7", "10111");         

        put("$24", "11000");    // More Temporary Registers $t8-$t9
        put("$t8", "11000");
        put("$25", "11001");
        put("$t9", "11001");     
        
        put("$26", "11010");    // Reserved for Kernel DO NOT NEED TO SUPPORT
        put("$k0", "11010");
        put("$27", "11011");
        put("$k1", "11011"); 
        
        put("$28", "11100");    // Global Area Pointer
        put("$gp", "11100");
        
        put("$29", "11101");    // Stack Pointer
        put("$sp", "11101");
        
        put("$30", "11110");    // Frame Pointer
        put("$fp", "11110");

        put("$31", "11111");    // Return Address
        put("$ra", "11111");   
        
    }};

    private static final Map<String, String> opcodeMap = new HashMap<String, String>() {{
        put("and", "100100");
        put("or", "100101");
        put("add", "100000");
        put("addi", "001000");
        put("sll", "000000");
        put("sub", "100010");
        put("slt", "101010");
        put("beq", "000100");
        put("bne", "000101");
        put("lw", "100011");
        put("sw", "101011");
        put("j", "000010");
        put("jr", "001000");
        put("jal", "000011");
    }};
    
    private String opcode;
    private String opType;      // string for run function
    private int lineNo;         // position of opcode in the assembly file
    private String binary;      // stores the binary form of opcode instruction

    /* instruction arguments */
    private String rd;          // destination register for:    and, or, add, sub, slt, sll 
    private String rs;          // source register for:         and, or, add, sub, slt, addi, beq, bne, jr
    private String rt;          // source register for:         and, or, add, sub, slt, addi, beq, bne, lw, sw, sll
    private String base;        // base register for:           lw, sw
    private int imm = 0;        // immediate integer for:       addi, beq, bne, lw (offset), sw (offset), sll (sa), j, jal
    

    private void immediateConvert(String immediate, boolean J) {
        int num = 0;

        try { 
            num = Integer.parseInt(immediate); 
        } catch(NumberFormatException e) { 
            num = (Globals.labelMap.get(immediate)) - 1; 
            if(!J) {
                num -= this.lineNo;
            }
        }

        this.imm = num;
    }

    private String immediateBinary(int i) {
        String binary; 

        // Convert num to 32 bit binary string
        binary = Long.toBinaryString( Integer.toUnsignedLong(this.imm) | 0x100000000L ).substring(1);   

        // Grab the last "i" characters of the string ( corrected length of Immediate Value in Instruction) 
        binary = binary.substring(binary.length() - i);         

        return binary;
    }

    /* constructor */
    inst(String opcode, String args[], int lineNo) {
        String offset[];

        this.opcode = opcode;
        this.lineNo = lineNo;

        // case_1: and or add sub slt
        if(opcode.equals("and") || opcode.equals("or") || opcode.equals("add") || 
           opcode.equals("sub") || opcode.equals("slt")) {
            this.rd = args[0];
            this.rs = args[1];
            this.rt = args[2];
            
            this.binary = "000000 " + 
            registerMap.get(this.rs) + " " + 
            registerMap.get(this.rt) + " " +
            registerMap.get(this.rd) + 
            " 00000 " + opcodeMap.get(this.opcode);

            this.opType = "RD";

        } 
        
        // case_2: addi beq bne
        if(opcode.equals("addi") || opcode.equals("beq") || opcode.equals("bne")) {
            this.rs = args[0];
            this.rt = args[1];
            immediateConvert(args[2], false);

            this.binary = opcodeMap.get(this.opcode) + " " + 
            registerMap.get(this.rs) + " " +
            registerMap.get(this.rt) + " " + 
            immediateBinary(16);

            this.opType = (this.opcode.equals("addi")) ? "RD" : "PC"; 
        } 
        
        // case_3: lw sw
        if(opcode.equals("lw") || opcode.equals("sw")) {
            offset = args[1].replaceAll("\\)", "").split("\\(\\$");
            
            this.base = "$" + offset[1];
            this.rt = args[0];
            immediateConvert(offset[0], false);

            this.binary = opcodeMap.get(this.opcode) + " " + 
            registerMap.get(this.base) + " " +
            registerMap.get(this.rt) + " " +
            immediateBinary(16);

            this.opType = "MEM";
        } 
        
        // case_4: sll
        if(this.opcode.equals("sll")) {
            
            this.rd = args[0]; 
            this.rt = args[1];
            immediateConvert(args[2], false); 

            this.binary = "000000 00000 " +
            registerMap.get(this.rt) + " " +
            registerMap.get(this.rd) + " " +
            immediateBinary(5) + " " +
            opcodeMap.get(this.opcode);

            this.opType = "RD";

        } 
        
        // case_5: j jal
        if(this.opcode.equals("j") || this.opcode.equals("jal")) {
            
            immediateConvert(args[0], true); 

            this.binary = opcodeMap.get(this.opcode) + " " +
            immediateBinary(26);

            this.opType = "PC";
        } 
        
        // case_6: jr
        if(this.opcode.equals("jr")) {
            
            this.rs =  args[0];

            this.binary = "000000 " +
            registerMap.get(this.rs) + 
            " 000000000000000 " +
            opcodeMap.get(this.opcode);

            this.opType = "PC";
        }

    }

    // Checks to see if instObj.opcode (this.opcode) matches opcode, then print out Binary
    public void toBinary() {
        
        // change to check if opcode exists in the opcode map (error state)
        if (!opcodeMap.containsKey(this.opcode)) {
            System.out.println("invalid instruction: " + this.opcode);
            System.exit(0);
        }

        System.out.println(this.binary);
    }

    /* opcode logic */

    /* 
        and, or, add, addi, sub, sll, slt 
        - operations that store a result in rd
    */
    private void RDop() {

        int PC = Globals.registerMap.get("pc");
        int rs = Globals.registerMap.get(this.rs);
        int rt = Globals.registerMap.get(this.rt);
        int rd = 0;

        switch(this.opcode) {
            case "and"  : rd = rs & rt;             break;
            case "or"   : rd = rs | rt;             break;
            case "add"  : rd = rs + rt;             break;
            case "addi" : rs = rt + imm;            break;
            case "sub"  : rd = rs - rt;             break;
            case "sll"  : rd = rt << this.imm;      break;
            case "slt"  : rd = (rs < rt) ? 1 : 0;   break;
        }
        
        if (this.opcode.equals("addi")) {
            Globals.registerMap.put(this.rs, rs);   // store rd
        } else {
            Globals.registerMap.put(this.rd, rd);   // store rd
        }
        
        Globals.registerMap.put("pc", PC + 1);      // increment PC

    }

    /* 
        lw, sw 
        - memory operations
        - note: imm in this case is base
    */
    private void MEMop() {
        int PC = Globals.registerMap.get("pc");
        int base = Globals.registerMap.get(this.base);

        switch(this.opcode) {
            case "lw"   : Globals.registerMap.put(this.rt, Globals.memory[base + imm]);
            break;
            case "sw"   : Globals.memory[base + imm] = Globals.registerMap.get(this.rt);
            break;
        }
        
        Globals.registerMap.put("pc", PC + 1);  // increment PC
    }

    /* 
        beq, bne, jal, j, jr
        - operations that change the PC 
    */
    private void PCop() {
        int PC = Globals.registerMap.get("pc");
        int rs = 0;
        int rt = 0;
        int offset = 0;

        // make this less messy
        if(!(this.opcode.equals("j") || this.opcode.equals("jal"))) {
            if(!this.opcode.equals("jr"))
                rt = Globals.registerMap.get(this.rt);
            rs = Globals.registerMap.get(this.rs);
        }

        switch(this.opcode) {
            case "beq"  : offset = (rs == rt) ? (this.imm + 1) : 1;     break;
            case "bne"  : offset = (rs != rt) ? (this.imm + 1) : 1;     break;
            case "jal"  : Globals.registerMap.put("$ra", PC + 1);   // continue
            case "j"    : offset = this.imm;                            break;
            case "jr"   : offset = rs;                                  break; 
        }   

        if(this.opcode.charAt(0) == 'j') {
            Globals.registerMap.put("pc", offset);  // increment PC
        } else {
            Globals.registerMap.put("pc", PC + offset);  // increment PC
        }
    }

    private void write_back() {
        System.out.println(Globals.pipelineList.get(3));
    }

    private void memory() {
        // access the memory locations if needed
        System.out.println(Globals.pipelineList.get(2));
    }

    private void execute() {
        System.out.println(Globals.pipelineList.get(1));
    }

    private void decode() {
        System.out.println(Globals.pipelineList.get(0));
    }

    private void fetch() {
        // get the opcode for the instruction
        // store it in the linked list

        int PC = Globals.registerMap.get("pc") + 1;
        
        
    }

    /* runs a single clockcycle */
    public void run() {
        Globals.Cycles += 1;        // Increment Total Clock Cycles
        write_back();
		memory();
		execute();
		decode();
        fetch();
        
        /*
        switch(this.opType) {
            case "RD"   : RDop();   break; 
            case "MEM"  : MEMop();  break;
            case "PC"   : PCop();   break;
        }
        */
    }

}