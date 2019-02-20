# test2
# CPI: 1.727 Cycles: 19 Instructions: 11

j next		# 1 cycle penalty
equal1:	add $a0, $0, $0

next:	addi $a0, $0, 100
	addi $a1, $0, 101
	beq $a0, $a1, equal1	# fall through
	addi $a0, $0, 101
	beq $a0, $a1, equal2	# taken branch
	lw $a0, 0($0)
	lw $a0, 0($0)
	lw $a0, 0($0)

equal2:	add $a0, $0, $0
  add $a0, $0, $0
  add $a0, $0, $0
  add $a0, $0, $0
  add $a0, $0, $0
	
