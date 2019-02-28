# lab4_test1.asm
#
# CPI = 1.400	cycles = 42	instructions = 30

# instructions with rd as destination
	
lw $a0, 0($a1)	    # 1
add $t0, $a0, $a1	# stall, 2

lw $a0, 0($a1)
add $t0, $a1, $a0	# stall, 3

lw $a0, 0($a1)      # 4
add $t0, $t0, $t0	# no stall, 5

lw $a0, 0($a1)      # 6
sub $t0, $a1, $a0	# stall, 7

lw $a0, 0($a1)      # 8
sub $t0, $a0, $a1	# stall, 9

lw $a0, 0($a1)	    # 10
sub $t0, $t0, $t0	# no stall, 11

lw $a0, 0($a1)		# 12
slt $t0, $a0, $a1	# stall, 9

lw $a0, 0($a1)      # 10
slt $t0, $a1, $a0	# stall, 

lw $a0, 0($a1)
slt $t0, $t0, $t0	# no stall

# instructions with rt as destination

lw $a0, 0($a1)		# no stall
addi $t0, $a1, 1

lw $a0, 0($a1)		# stall
addi $t0, $a0, 1

lw $a0, 0($a1)		# no stall (fix this) FIX THIS
addi $a0, $t0, 1    # overwriting load value (so no write bit)


lw $a0, 0($a1)	
lw $a0, 0($a1)		# no stall  (fix this) 
                    # overwriting load value (so no write bit)

lw $a0, 0($a1)
lw $a1, 0($a0)		# stall

lw $0, 0($s1)		# no stall (fix this)
add $s2, $s1, $0