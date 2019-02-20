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
import java.util.Arrays;

/* 
User Options:
    h = show help
    d = dump register state
    s = single step through the program (i.e. execute 1 instruction and stop)
    s num = step through num instructions of the program
    r = run until the program ends
    m num1 num2 = display data memory from location num1 to num2
    c = clear all registers, memory, and the program counter to 0
    q = exit the program
*/

class interactive{

    /* error messages */
    private static final String HELP_MESSAGE = 
        "\nh = show help" +
        "\nd = dump register state" +
        "\ns = single step through the program (i.e. execute 1 instruction and stop)" +
        "\ns num = step through num instructions of the program" +
        "\nr = run until the program ends" +
        "\nm num1 num2 = display data memory from location num1 to num2" +
        "\nc = clear all registers, memory, and the program counter to 0" +
        "\nq = exit the program\n";

    private static final String PARSE_INT_ERROR = "        Invalid integer value (reverting to default value)";
    private static final String MEM_ARGS_ERROR  = "        Invalid amount of arguments (use: m num1 num2)";

    private static void programCompleteMsg(){
        float CPI = Globals.Cycles / Globals.instList.size();
        System.out.println("Program complete");
        System.out.print(String.format("CPI = " + "%-10s", CPI));
        System.out.print(String.format("Cycles = " + "%-10s", Globals.Cycles));
        System.out.println(String.format("Instructions = " + "%-10s", Globals.instList.size()));
    }

    /* print registers */
    private static void dump() {

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

        System.out.println();
        System.out.println("pc     if/id   id/exe  exe/mem   mem/wb");
        System.out.print(Globals.registerMap.get("pc") + "      ");

        for (String entry : Globals.pipelineList){
            
            System.out.print(String.format("%-8s", entry));
            // System.out.print(String.format("%-16s", entry));
        }

        System.out.println("\n");

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

        /* run instructions (until end reached) */
        for(int i = 0; (i < numInst) && (pc != Globals.instList.size()); i++) {
            Globals.instList.get(pc).run(); // run instruction
            pc = Globals.registerMap.get("pc");
        }

        System.out.println("        " + numInst + " instruction(s) executed");
    }

    /* run until the program ends */
    private static void run() {
        int pc = Globals.registerMap.get("pc");

        while(pc != Globals.instList.size()) {
            Globals.instList.get(pc).run(); // run instruction
            pc = Globals.registerMap.get("pc");
        }
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

        memEnd = memStart;

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

    /* clear all registers */
    private static void clear() {
        for (Map.Entry<String, Integer> entry : Globals.registerMap.entrySet()) {
            entry.setValue(0);
        }
        System.out.println("        Simulator reset\n");
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
                //case 's' : step(userInput);                     break;      // Step through <userInput> Lines of Code
                case 's' : stepClock(userInput);                break;      // Step through <userInput> clock cycles
                case 'r' : run();                               break;      // Run Until Completion
                case 'm' : memory(userInput);                   break;      // Display Integer Memory Map
                case 'c' : clear();                             break;      // Clear Registers, Memory, PC = 0
            }
            System.out.print("mips> ");
            userInput = sc.nextLine();
        }
        programCompleteMsg();
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
                //case 's' : step(line);                     break;      // Step through <line> Lines of Code
                case 's' : stepClock(line);                     break;      // Step through <line> clock cycles
                case 'r' : run();                               break;      // Run Until Completion
                case 'm' : memory(line);                        break;      // Display Integer Memory Map
                case 'c' : clear();                             break;      // Clear Registers, Memory, PC = 0
                case 'q' : programCompleteMsg(); sc.close(); System.exit(0);          break;
            }
        }


    }
}