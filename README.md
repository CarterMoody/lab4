# 315-lab04

## Stuff to change:
- (s) should be one clock-cycle, not just one step this time
    - this means that some instructions will be in the middle of execution
- (r) include a message at the end with CPI, cycles and instructions
    - must end on cycle end, not PC
- (c) clears:
    - general registers
    - pipe registers (set them all back to empty)
    - CPI/cycles/instructions object (maybe???)

# Questions
- does clearing registers also clear the pipeline? (answer: yes it does)