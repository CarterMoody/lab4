# test2
# CPI: 1.727 Cycles: 19 Instructions: 11

j next		# 1 cycle penalty
equal1:	add $a0, $0, $0 # 2

next:	addi $a0, $0, 100 # 3
	addi $a1, $0, 101 # 4
	beq $a0, $a1, equal1	# 5 fall through
	addi $a0, $0, 101 		# 6
	beq $a0, $a1, equal2	# 7 taken branch
	lw $a0, 0($0)			# 8
	lw $a0, 0($0)			# 9
	lw $a0, 0($0)			# 10

equal2:	add $a0, $0, $0		# 11
  add $a0, $0, $0			# 12
  add $a0, $0, $0			# 13
  add $a0, $0, $0			# 14
  add $a0, $0, $0			# 15
	
