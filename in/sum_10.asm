#This is test program 1
#This computes the sum of 1 to 10
	
	addi $a0, $a0, 10 # load $a0 with 10
loop:	add $s0, $s0, $a0
	sw $a0, 0($a0) 
	addi $a0, $a0, -1  # decrement $a0
	bne $a0, $0, loop  # if $a0 equals 0 then break out of the loop
	





	
