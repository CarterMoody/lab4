lab3:
	javac *.java

clean:
	rm *.class

test:
	java lab3 in/lab3_fib.asm scripts/lab3_fib.script > my/lab3_fib.output
	diff -w -B my/lab3_fib.output out/lab3_fib.output
	echo
	java lab3 in/lab3_test3.asm scripts/lab3_test3.script > my/lab3_test3.output
	diff -w -B my/lab3_test3.output out/lab3_test3.output
	echo
	java lab3 in/sum_10.asm scripts/sum_10.script > my/sum_10.output
	diff -w -B my/sum_10.output out/sum_10.output

test1: 
	java lab3 in/lab3_fib.asm scripts/lab3_fib.script
	java lab3 in/lab3_fib.asm scripts/lab3_fib.script > my/lab3_fib.output
	echo
	echo
	diff -w -B my/lab3_fib.output out/lab3_fib.output

test2: 
	java lab3 in/lab3_test3.asm scripts/lab3_test3.script
	java lab3 in/lab3_test3.asm scripts/lab3_test3.script > my/lab3_test3.output
	echo
	echo
	diff -w -B my/lab3_test3.output out/lab3_test3.output

test3: 
	java lab3 in/sum_10.asm scripts/sum_10.script
	echo
	echo
	java lab3 in/sum_10.asm scripts/sum_10.script > my/sum_10.output
	diff -w -B my/sum_10.output out/sum_10.output
