/*
    Name:         Robert Hensley, Carter Moody
    Section:      09
    Description:  a class of user input methods
*/

/* I/O Libraries */
import java.io.*; 
import java.util.Scanner;
import java.util.Formatter;

/* Objects */
import java.lang.String;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

/* 
User Options:
    h = show help
    d = dump register state
    p = show pipeline registers
    s = step through a single clock cycle step (i.e. simulate 1 cycle and stop)
    s num = step through num clock cycles
    r = run until the program ends and display timing summary
    m num1 num2 = display data memory from location num1 to num2
    c = clear all registers, memory, and the program counter to 0
    q = exit the program
*/

class interactive{

    /* error messages */
    private static final String HELP_MESSAGE = 
        "\nh = show help" +
        "\nd = dump register state" +
        "\np = show pipeline registers" +
        "\ns = step through a single clock cycle step (i.e. simulate 1 cycle and stop)" +
        "\ns num = step through num clock cycles" +
        "\nr = run until the program ends and display timing summary" +
        "\nm num1 num2 = display data memory from location num1 to num2" +
        "\nc = clear all registers, memory, and the program counter to 0" +
        "\nq = exit the program\n";

    private static final String PARSE_INT_ERROR = "        Invalid integer value (reverting to default value)";
    private static final String MEM_ARGS_ERROR  = "        Invalid amount of arguments (use: m num1 num2)";

    /* print registers */
    public static void dump() {

        int i = 0;

        System.out.println();

        for (Map.Entry<String, Integer> entry : Globals.registerMap.entrySet()) {
            
            if(i == 0) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            } else {
                System.out.print(String.format("%-16s", entry.getKey() + " = " + entry.getValue()));
                if((i % 4) == 0)
                    System.out.println();
            }

            i++;
            
        }

        System.out.println("\n");

    }

    private static void pipeline() {

        int pc;

        if(Globals.pipelineList.get(2).opcode.matches("j|jal|jr")) {
            pc = Globals.pipelineList.get(4).pc - 1;
        } else {
            pc = Globals.pipelineList.get(3).pc;
        }

        System.out.println("\npc      if/id   id/exe  exe/mem mem/wb");
        System.out.print(pc + "       ");

        for (int i = 3; i >= 0; --i) {
            System.out.print(String.format("%-8s", Globals.pipelineList.get(i).opcode));
        }

        System.out.println("\n");

    }

    private static void printFullPipe() {
        for(pipe p : Globals.pipelineList) {
            System.out.print(p.opcode + ", ");
            System.out.print(p.pc + ", ");
            System.out.println(p.stall);
        }

        System.out.println("\n");
    }

    private static void pipelineStep() {

        pipe wb = Globals.pipelineList.get(0);  
        pipe mem = Globals.pipelineList.get(1);
        pipe ex = Globals.pipelineList.get(2);
        pipe d = Globals.pipelineList.get(3);
        pipe f = Globals.pipelineList.get(4);

        if(ex.stall) {
            Globals.pipelineList.add(3, new pipe("stall", d.pc));
        }

        if(mem.threeSquash) {
            Globals.pipelineList.remove(2);
            Globals.pipelineList.remove(2);
            Globals.pipelineList.remove(2);
            Globals.pipelineList.add(2, new pipe("squash", f.pc));
            Globals.pipelineList.add(2, new pipe("squash", f.pc));
            Globals.pipelineList.add(2, new pipe("squash", f.pc));
        }

        // if not at the end
        if(Globals.pipelineList.size() > 4) {

            Globals.pipelineList.pop();
            Globals.Cycles += 1;

            if(!wb.opcode.matches("squash|stall|empty"))
                Globals.Instructions += 1;
        }

    }

    /* run step(s) */
    private static void stepClock(String userInput) {
        
        int pc = Globals.registerMap.get("pc");
        int numInst = 1;
        String args[] = userInput.split(" ");

        if(args.length == 2) {
            try {
                numInst = Integer.parseInt(args[1]); 
            } catch (NumberFormatException e) {
                System.out.println(PARSE_INT_ERROR);
            }
        }

        /* run instructions (until end reached) 
        for(int i = 0; (i < numInst) && (!emptyCheck() || (pc == 0)); i++) {
            if((pc == Globals.instList.size())) {
                pipelineStep(new inst("empty", null, 0));
            } else {
                pipelineStep(Globals.instList.get(pc));
                pc = Globals.registerMap.get("pc");
            }
        }
        */

        // System.out.println(mem.stall);
        // System.out.println(mem.opcode);

        // check for stalls

        pipeline();     // print the pipeline
        pipelineStep();

    }

    /* prints CPI info */
    private static void programCompleteMsg(){
        
        double CPI = (double)Globals.Cycles / Globals.Instructions;
        System.out.println("\nProgram complete");
        System.out.print(String.format("CPI = " + "%-10.3f", CPI));
        System.out.print(String.format("Cycles = " + "%-10s", Globals.Cycles));
        System.out.println(String.format("Instructions = " + "%-10s\n", Globals.Instructions));
        
    }

    /* run until the program ends */
    private static void run() {

        while(Globals.pipelineList.size() > 4) {
            pipelineStep();
        }

        programCompleteMsg();
    }

    /* m num1 num2 = display data memory from location num1 to num2 */
    private static void memory(String userInput) {
        int memStart = 0;
        int memEnd = 0;

        String args[] = userInput.split(" ");

        if(args.length != 3) {
            System.out.println(MEM_ARGS_ERROR);
            return;
        }

        /* read in memStart and memEnd */
        try {
            memStart = Integer.parseInt(args[1]); 
        } catch (NumberFormatException e) {
            System.out.println(PARSE_INT_ERROR);
        }

        memEnd = memStart; // default value

        try {
            memEnd = Integer.parseInt(args[2]); 
        } catch (NumberFormatException e) {
            System.out.println(PARSE_INT_ERROR);
        }

        /* print memory content */

        System.out.println();
        for(;memStart <= memEnd; memStart ++) {
            System.out.println("[" + memStart + "] = " + Globals.memory[memStart]);
        }
        System.out.println();

    }

    /* clear all registers and pipeline */
    private static void clear() {
        for (Map.Entry<String, Integer> entry : Globals.registerMap.entrySet()) {
            entry.setValue(0);
        }
        System.out.println("        Simulator reset\n");

        // clear memory
        Globals.memory = new int[Globals.MEMORY_SIZE];

        // reset the pipeline
        Globals.pipelineList = new LinkedList<pipe>(Arrays.asList(
            new pipe("empty", 0),
            new pipe("empty", 0),
            new pipe("empty", 0)
        ));

        // reset instruction stuff
        Globals.Cycles = 0;
        Globals.Instructions = 0;

        // run everything (emulation)
        lab4.run();
        
    }

    /* interactive mode */
    public static void interactiveLoop() {

        Scanner sc = new Scanner(System.in);
        System.out.print("mips> ");
        String userInput = sc.nextLine();

        char c;     // user input option                                 

        /* loop until quit */
        while ((c = userInput.toLowerCase().charAt(0)) != 'q') { 

            switch(c) {
                case 'h' : System.out.println(HELP_MESSAGE);    break;      // Show Help
                case 'd' : dump();                              break;      // Dump Register State
                case 'p' : pipeline();                          break;      // Show Pipeline Registers
                case 's' : stepClock(userInput);                break;      // Step through <userInput> clock cycles
                case 'r' : run();                               break;      // Run Until Completion
                case 'm' : memory(userInput);                   break;      // Display Integer Memory Map
                case 'c' : clear();                             break;      // Clear Registers, Memory, PC = 0
                case 'i' : programCompleteMsg();                break;      // Display CPI and Instruction Info
            }
            System.out.print("mips> ");
            userInput = sc.nextLine();
        }
        sc.close();
        System.exit(0);
    }

    /* non-interactive mode */
    public static void runScript(String script) {

        File file = new File(script);
        Scanner sc = null;
        String line;

        try { 
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            System.exit(1);
        }

        while (sc.hasNextLine()) {
            line = sc.nextLine();
            System.out.println("mips> " + line);
            switch(line.toLowerCase().charAt(0)) {
                case 'h' : System.out.println(HELP_MESSAGE);    break;      // Show Help
                case 'd' : dump();                              break;      // Dump Register State
                case 'p' : pipeline();                          break;      // Show Pipeline Registers
                case 's' : stepClock(line);                     break;      // Step through <line> clock cycles
                case 'r' : run();                               break;      // Run Until Completion
                case 'm' : memory(line);                        break;      // Display Integer Memory Map
                case 'c' : clear();                             break;      // Clear Registers, Memory, PC = 0
                case 'q' : sc.close(); System.exit(0);          break;
            }
        }


    }
}