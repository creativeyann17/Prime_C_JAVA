gcc -O2 -Werror Prime.c -o PrimeIterative
gcc -fopenmp -O2 -DOMP -Werror Prime.c -o PrimeParallel
