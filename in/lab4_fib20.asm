# compute fibonacci numbers
#
# 20th fibonacci number = 6765
# CPI = 1.409	cycles = 209704	instructions = 148818

addi $a0, $0, 20	# input argument
addi $sp, $0, 4095	# initialize stack pointer
jal fibonacci
j end

fibonacci:	addi $t0, $0, 3
		slt $t1, $a0, $t0
		bne $0, $t1, basecase	# check if argument is less than 3

		addi $sp, $sp, -3
		sw $a0, 0($sp)
		sw $ra, 1($sp)
		addi $a0, $a0, -1
		jal fibonacci		# compute fibonacci(n-1)

		sw $v0, 2($sp)
		lw $a0, 0($sp)
		addi $a0, $a0, -2
		jal fibonacci		# compute fibonacci(n-2)
		lw $t0, 2($sp)
		add $v0, $v0, $t0

		lw $ra, 1($sp)
		addi $sp, $sp, 3
		jr $ra

basecase:	addi $v0, $0, 1
		jr $ra


end: add $0, $0, $0
