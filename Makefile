lab4:
	javac *.java

clean:
	rm *.class

test:
	java lab4 in/lab4_test1.asm scripts/lab4_test1.asm > my/lab4_test1.output
	diff -w -B my/lab4_test1.output out/lab4_test1.output
	echo
	java lab4 in/lab4_test2.asm scripts/lab4_test2.asm > my/lab4_test2.output
	diff -w -B my/lab4_test2.output out/lab4_test2.output
	echo
	java lab4 in/lab4_fib10.asm scripts/lab4_fib10.asm > my/lab4_fib10.output
	diff -w -B my/lab4_fib10.output out/lab4_fib10.output
	echo
	java lab4 in/lab4_fib20.asm scripts/lab4_fib20.asm > my/lab4_fib20.output
	diff -w -B my/lab4_fib20.output out/lab4_fib20.output
	echo

test1:
	java lab4 in/lab4_test1.asm scripts/lab4_test1.asm
	java lab4 in/lab4_test1.asm scripts/lab4_test1.asm > my/lab4_test1.output
	echo
	echo
	diff -w -B my/lab4_test1.output out/lab4_test1.output

test2:
	java lab4 in/lab4_test2.asm scripts/lab4_test2.asm
	java lab4 in/lab4_test2.asm scripts/lab4_test2.asm > my/lab4_test2.output
	echo
	echo
	diff -w -B my/lab4_test2.output out/lab4_test2.output

test3: 
	java lab4 in/lab4_fib10.asm scripts/lab4_fib10.asm
	java lab4 in/lab4_fib10.asm scripts/lab4_fib10.asm > my/lab4_fib10.output
	echo
	echo
	diff -w -B my/lab4_fib10.output out/lab4_fib10.output

test4: 
	java lab4 in/lab4_fib20.asm scripts/lab4_fib20.asm
	java lab4 in/lab4_fib20.asm scripts/lab4_fib20.asm > my/lab4_fib20.output
	echo
	echo
	diff -w -B my/lab4_fib20.output out/lab4_fib20.output

interact1:
	java lab4 in/lab4_test1.asm

interact2:
	java lab4 in/lab4_test2.asm

interact3:
	java lab4 in/lab4_fib10.asm

interact4:
	java lab4 in/lab4_fib20.asm
