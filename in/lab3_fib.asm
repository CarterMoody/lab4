# compute fibonacci numbers
# the sixth number is stored in $v0 when the program completes

addi $a0, $0, 6	# input argument # 0
addi $sp, $0, 4095	# initialize stack pointer # 1
jal fibonacci # 2
j end # 3

fibonacci:	addi $t0, $0, 3 #4
		slt $t1, $a0, $t0 #5
		bne $0, $t1, basecase	# check if argument is less than 3 #6

		addi $sp, $sp, -3 #7
		sw $a0, 0($sp) #8
		sw $ra, 1($sp) #9
		addi $a0, $a0, -1 #10
		jal fibonacci		# compute fibonacci(n-1) #11

		sw $v0, 2($sp) #12
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
