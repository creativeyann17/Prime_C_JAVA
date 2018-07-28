#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifdef OMP
#include <omp.h>
#endif

typedef int bool;
#define true 	1
#define false 	0

static bool isPrime(size_t l_Value){
	size_t i=0;
	for(i=2;i<l_Value;++i){
		if(l_Value%i==0){
			return false;
		}
	}
	return true;
}

#define SIZE 1000000

int main(int argc,char * argv[])
{

	size_t l_Count=0;
	
	#ifdef OMP

	#pragma omp parallel
	{
		size_t i;
		
		#pragma omp for schedule(guided) reduction(+:l_Count)
		for(i=0;i<SIZE;++i){
			l_Count+=(isPrime(i))?1:0;
		}
	
	}
	
	#else
	
	size_t i;
	for(i=0;i<SIZE;++i){
		if(isPrime(i))l_Count++;
	}
	
	#endif
	
	printf("Results: %u\n",l_Count);
	
	return 0;
}

