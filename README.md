# 315-lab04

## Stuff to change:
-> (h) update help message to reflect addition of pipe option
- (p) add an option that reads pipe registers
- (s) should be one clock-cycle, not just one step this time
    - this means that some instructions will be in the middle of execution
- (r) include a message at the end with CPI, cycles and instructions
- (c) clears:
    - general registers
    - pipe registers
    - CPI/cycles/instructions object

# Data Structures

- pipe registers data structure
    - might make a class within a class for the pipe registers (don't need key values to access)
    - this class will include a print method 

- in run, have a method for each pipeline:
    - pc      
    - if/id   
    - id/exe  
    - exe/mem 
    - mem/wb

- global data structure that keeps track of:
    - CPI
    - cycles
    - instructions

# Other Goals
- remove binary stuff