#include <stdio.h>

unsigned short CRC16_XMODEM(unsigned char *puchMsg, unsigned int usDataLen)
{
	unsigned short wCRCin = 0x0000;
 	unsigned short wCPoly = 0x1021;
   	unsigned char wChar = 0;
    
   	while (usDataLen--) 
   	{
   		wChar = *(puchMsg++);
   		wCRCin ^= (wChar << 8);
   		for (int i = 0; i < 8; i++) 
   		{
   			if (wCRCin & 0x8000)
   				wCRCin = (wCRCin << 1) ^ wCPoly;
   			else
   				wCRCin = wCRCin << 1;
   		}
   	}
   	return (wCRCin);
}

void test_CRC16_Xmode() 
{
  	unsigned char data[15];
   	// 980012010001123456785A2608CE23
   	data[0] = 0x98;
   	data[1] = 0x00;
   	data[2] = 0x12;
   	data[3] = 0x01;
   	data[4] = 0x00;
   	data[5] = 0x01;
   	data[6] = 0x12;
   	data[7] = 0x34;
   	data[8] = 0x56;
   	data[9] = 0x78;
   	data[10] = 0x5A;
   	data[11] = 0x26;
   	data[12] = 0x08;
   	data[13] = 0xCE;
   	data[14] = 0x23;
   	char str1[128];
   	sprintf(str1, "%X", CRC16_XMODEM(data, 15));
   	printf("%s\r\n", str1);
}

int main()
{
   	test_CRC16_Xmode();
   	getchar();
    return 0;
}
