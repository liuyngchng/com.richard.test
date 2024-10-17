/*
 * draw_sin.c
 *
 *  Created on: Oct 17, 2024
 *      Author: rd
 */
#include <stdio.h>
#include <math.h>

#define PI 3.14

int main ( void )
{
	double r;
	int cols, c;

	for(r=1; r>0; r-=0.1)
	{
		cols = asin(r)* 10;
		for(c=0; c<cols; c++ )
			printf( " " );
		printf ( "." );
		for ( ; c<PI*10-cols-1; c++ )
			printf( " " );
		printf ( "." );

		printf ( "\n" );
	}

	for ( r=0; r>=-1; r-=0.1 )
	{
		cols = ( PI - asin(r) ) * 10;
		for ( c=0; c<cols+2; c++ )
			printf ( " " );
		printf ( "." );
		for ( ; c<10*(2*PI+asin(r))+1; c++ )
			printf ( " " );
		printf ( "." );
		printf ( "\n" );
	}

	return 0;
}


