# compute fibonacci numbers (test 3)
#
# 10th fibonacci number = 55
# CPI = 1.414	cycles = 1694	instructions = 1198

addi $a0, $0, 2	# 1 input argument 
addi $sp, $0, 4095	# 2 initialize stack pointer
jal fibonacci		# 3
j end				# 4

fibonacci:	addi $t0, $0, 3		# 5 
		slt $t1, $a0, $t0		# 6
		bne $0, $t1, basecase	# 7 check if argument is less than 3

		addi $sp, $sp, -3		# 8
		sw $a0, 0($sp)			# 9
		sw $ra, 1($sp)			# 10
		addi $a0, $a0, -1		# 11
		jal fibonacci			# 12 compute fibonacci(n-1)

		sw $v0, 2($sp)			# 13
		lw $a0, 0($sp)			# 14
		addi $a0, $a0, -2		# 15
		jal fibonacci			# 16 compute fibonacci(n-2)
		lw $t0, 2($sp)			# 17
		add $v0, $v0, $t0		# 18

		lw $ra, 1($sp)			# 19
		addi $sp, $sp, 3		# 20
		jr $ra					# 21

basecase:	addi $v0, $0, 1		# 22
		jr $ra					# 23


end: add $0, $0, $0				# 24
