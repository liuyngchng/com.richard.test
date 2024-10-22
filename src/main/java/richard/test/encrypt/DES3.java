package cm.iot.cmn.utl;

import java.util.Scanner;

public class DES3 {

    /**
     * 十六轮子密钥
     */
//    private static byte[][] sub_key = new byte[16][48];

    /*---------------------*/
/*-------------------------------------------------------------
     各种置换表
-------------------------------------------------------------*/
    /**
     * IP置换表
     */
    private static final char[] ip_tbl= {
            58,50,42,34,26,18,10, 2,60,52,44,36,28,20,12, 4,
            62,54,46,38,30,22,14, 6,64,56,48,40,32,24,16, 8,
            57,49,41,33,25,17, 9, 1,59,51,43,35,27,19,11, 3,
            61,53,45,37,29,21,13, 5,63,55,47,39,31,23,15, 7
    };

    /**
     * IP-1置换表
     */
    private static final char[] ipr_tbl = {
            40, 8,48,16,56,24,64,32,39, 7,47,15,55,23,63,31,
            38, 6,46,14,54,22,62,30,37, 5,45,13,53,21,61,29,
            36, 4,44,12,52,20,60,28,35, 3,43,11,51,19,59,27,
            34, 2,42,10,50,18,58,26,33, 1,41, 9,49,17,57,25
    };

    /**
     * E扩展表
     */
    private static final char[] e_tbl = {
            32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9,
            8, 9,10,11,12,13,12,13,14,15,16,17,
            16,17,18,19,20,21,20,21,22,23,24,25,
            24,25,26,27,28,29,28,29,30,31,32, 1
    };

    /**
     * PC1置换表
     */
    private static final char[] pc1_tbl = {
            57,49,41,33,25,17, 9, 1,58,50,42,34,26,18,
            10, 2,59,51,43,35,27,19,11, 3,60,52,44,36,
            63,55,47,39,31,23,15, 7,62,54,46,38,30,22,
            14, 6,61,53,45,37,29,21,13, 5,28,20,12, 4
    };

    /**
     * pc2表
     */
    private static final char[] pc2_tbl = {
            14,17,11,24, 1, 5, 3,28,15, 6,21,10,
            23,19,12, 4,26, 8,16, 7,27,20,13, 2,
            41,52,31,37,47,55,30,40,51,34,33,48,
            44,49,39,56,34,53,46,42,50,36,29,32
    };

    /**
     * 移位表
     */
    private static final char[] mv_tbl = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    /**
     * S盒
     */
    private static final int [][][]s_box = {
            //S1
            {
                    {14, 4,13, 1, 2,15,11, 8, 3,10, 6,12, 5, 9, 0, 7},
                    { 0,15, 7, 4,14, 2,13, 1,10, 6,12,11, 9, 5, 3, 8},
                    { 4, 1,14, 8,13, 6, 2,11,15,12, 9, 7, 3,10, 5, 0},
                    {15,12, 8, 2, 4, 9, 1, 7, 5,11, 3,14,10, 0, 6,13}
            },
            //S2
            {
                    {15, 1, 8,14, 6,11, 3, 4, 9, 7, 2,13,12, 0, 5,10},
                    { 3,13, 4, 7,15, 2, 8,14,12, 0, 1,10, 6, 9,11, 5},
                    { 0,14, 7,11,10, 4,13, 1, 5, 8,12, 6, 9, 3, 2,15},
                    {13, 8,10, 1, 3,15, 4, 2,11, 6, 7,12, 0, 5,14, 9}
            },
            //S3
            {
                    {10, 0, 9,14, 6, 3,15, 5, 1,13,12, 7,11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6,10, 2, 8, 5,14,12,11,15, 1},
                    {13, 6, 4, 9, 8,15, 3, 0,11, 1, 2,12, 5,10,14, 7},
                    { 1,10,13, 0, 6, 9, 8, 7, 4,15,14, 3,11, 5, 2,12}
            },
            //S4
            {
                    { 7,13,14, 3, 0, 6, 9,10, 1, 2, 8, 5,11,12, 4,15},
                    {13, 8,11, 5, 6,15, 0, 3, 4, 7, 2,12, 1,10,14, 9},
                    {10, 6, 9, 0,12,11, 7,13,15, 1, 3,14, 5, 2, 8, 4},
                    { 3,15, 0, 6,10, 1,13, 8, 9, 4, 5,11,12, 7, 2,14}
            },
            //S5
            {
                    { 2,12, 4, 1, 7,10,11, 6, 8, 5, 3,15,13, 0,14, 9},
                    {14,11, 2,12, 4, 7,13, 1, 5, 0,15,10, 3, 9, 8, 6},
                    { 4, 2, 1,11,10,13, 7, 8,15, 9,12, 5, 6, 3, 0,14},
                    {11, 8,12, 7, 1,14, 2,13, 6,15, 0, 9,10, 4, 5, 3}
            },
            //S6
            {
                    {12, 1,10,15, 9, 2, 6, 8, 0,13, 3, 4,14, 7, 5,11},
                    {10,15, 4, 2, 7,12, 0, 5, 6, 1,13,14, 0,11, 3, 8},
                    { 9,14,15, 5, 2, 8,12, 3, 7, 0, 4,10, 1,13,11, 6},
                    { 4, 3, 2,12, 9, 5,15,10,11,14, 1, 7, 6, 0, 8,13}
            },
            //S7
            {
                    { 4,11, 2,14,15, 0, 8,13, 3,12, 9, 7, 5,10, 6, 1},
                    {13, 0,11, 7, 4, 0, 1,10,14, 3, 5,12, 2,15, 8, 6},
                    { 1, 4,11,13,12, 3, 7,14,10,15, 6, 8, 0, 5, 9, 2},
                    { 6,11,13, 8, 1, 4,10, 7, 9, 5, 0,15,14, 2, 3,12}
            },
            //S8
            {
                    {13, 2, 8, 4, 6,15,11, 1,10, 9, 3,14, 5, 0,12, 7},
                    { 1,15,13, 8,10, 3, 7, 4,12, 5, 6,11, 0,14, 9, 2},
                    { 7,11, 4, 1, 9,12,14, 2, 0, 6,10,13,15, 3, 5, 8},
                    { 2, 1,14, 7, 4,10, 8,13,15,12, 9, 0, 3, 5, 6,11}
            }
    };

    /**
     * P置换表
     */
    private static final char []p_tbl = {
            16, 7,20,21,29,12,28,17, 1,15,23,26, 5,18,31,10,
            2, 8,24,14,32,27, 3, 9,19,13,30, 6,22,11, 4,25
    };

    /**
     * 二进制数组的拷贝
     */
    private static void bit_cpy(byte[] data_out, byte[] data_in, int num) {
        for(int i = 0; i < num; i++){
            data_out[i] = data_in[i];
        }
    }


    /**
     * 字节转换成位
     */
    private static byte[] byte_to_bit(byte[] data_in, int num) {
        byte[] data_out = new byte[num];
        for(int i = 0; i < num; i++) {
            data_out[i] = (byte)((data_in[i / 8] >> (i % 8)) & 0x01);
        }
        return data_out;
    }

    /**
     * 二进制转换成十六进制
     */
    private static char[] bit_to_hex(byte[] data_in, int num) {
        char [] data_out = new char[16];
        int i;
        for(i = 0; i < num / 4; i++) {
            data_out[i] = 0;
        }
        for (i = 0; i < num / 4; i++) {
            data_out[i] = (char)(data_in[4 * i] + data_in[4 * i + 1] * 2
                    + data_in[4 * i + 2] * 4 + data_in[4 * i + 3] * 8);
            if (data_out[i] % 16 > 9) {
                data_out[i] = (char)(data_out[i] % 16 + '7');
            } else {
                data_out[i] = (char)(data_out[i] % 16 + '0');
            }
        }
        return data_out;
    }

    /**
     * 十六进制转二进制
     */
    private static byte[] hex_to_bit(char[] data_in, int num) {
        byte[] data_out = new byte[num];
        int i;
        for (i = 0; i < num; i++) {
            if (data_in[i / 4] <= '9') {
                data_out[i] = (byte)(((data_in[i / 4] - '0') >> (i % 4)) & 0x01);
            } else {
                data_out[i] = (byte)(((data_in[i / 4] - '7') >> (i % 4)) & 0x01);
            }
        }
        return data_out;
    }

    /**
     * 位转换成字节
     */
    private static char[] bit_to_byte(byte[] msg_in, int num) {
        char[] my_msg = new char[8];
        for (int i = 0; i < (num / 8); i++) {
            my_msg[i] = 0;
        }
        for (int i = 0; i < num; i++) {
            my_msg[i / 8] |= msg_in[i] << (i % 8);
        }
        return my_msg;
    }

    /**
     * 各种表的置换算法
     */
    private static byte [] tbl_replace(byte[] data_in, final char[] table, int num) {
        byte[] data_out = new byte[num];
        for (int i = 0; i < num; i++) {
            data_out[i] = data_in[table[i] - 1];
        }
        return data_out;
    }

    /**
     * 左移位
     */
    private static void loop_bit(byte[] data_out, int mv_step, int len) {
        byte []tmp = new byte[mv_step];
        DES3.bit_cpy(tmp, data_out, mv_step);
        // 实现数组指针 data_out + mv_step
        byte[] data_out_mv_step = new byte[data_out.length - mv_step];
        for (int i = 0; i < data_out_mv_step.length; i++) {
            data_out_mv_step[i] = data_out[i + mv_step];
        }
        DES3.bit_cpy(data_out, data_out_mv_step, len - mv_step);

//        DES3.bit_cpy(data_out + len - mv_step, tmp, mv_step);
        for (int i = 0; i < mv_step; i++) {
            data_out[i + len -mv_step] = tmp[i];
        }
    }

    /**
     * 异或操作
     */
    private static byte[] xor(byte[] msg_out, byte[] msg_in, int num) {
        byte[] tmp = new byte[num];
        for (int i = 0; i < num; i++) {
            tmp[i] = (byte)(msg_out[i] ^ msg_in[i]);
        }
        return tmp;
    }

    /**
     * 生成16轮的子密钥
     */
    private static byte[][] set_key(char[] my_key) {
        byte[][]    sub_key = new byte[16][48];
//        byte[] key_bit_l, key_bit_r;
//        key_bit_l = &key_bit[0];        //key的左边28位；
//        key_bit_r = &key_bit[28];       //key的右边28位；
        byte []tmp_i = new byte[my_key.length];
        for (int i = 0;i< tmp_i.length; i++) {
            tmp_i[i] = (byte)my_key[i];
        }
        byte[] key_bit =DES3.byte_to_bit(tmp_i, 64);

        key_bit = DES3.tbl_replace(key_bit, DES3.pc1_tbl, 56);//pc-1 置换

        for(int i = 0; i < 16; i++) {
            DES3.loop_bit(key_bit, DES3.mv_tbl[i], 28);
            byte []key_bit_r = new byte[key_bit.length-28];
            for (int j = 0; j < key_bit_r.length; j ++) {
                key_bit_r[j] = key_bit[j+28];
            }
            DES3.loop_bit(key_bit_r, DES3.mv_tbl[i], 28);
            for (int j = 0; j < key_bit_r.length; j ++) {
                key_bit[j+28] = key_bit_r[j];
            }
            sub_key[i] = DES3.tbl_replace(key_bit, DES3.pc2_tbl, 48);//pc-2置换
        }
        return sub_key;
    }

    /**
     * S盒变换
     */
    private static byte[] s_change(byte[] data_out, byte[] data_in) {
        int i;
        int r = 0, c = 0;//S盒的行和列；
//        for(i = 0; i < 8; i++, data_in = data_in + 6, data_out = data_out + 4) {
        int j = 0;
        byte[] data_in_tmp, data_out_tmp = null;
        for (i = 0; i < 8; i++) {
            data_in_tmp = new byte[data_in.length -6 *j];
            data_out_tmp = new byte[data_out.length - 4 *j];
            for (int m = 0; m < data_in_tmp.length; m++) {
                data_in_tmp[m] = data_in[m + 6 * j];
            }
            for (int n = 0; n < data_out_tmp.length - 4 *j; n++) {
                data_out_tmp[n] = data_out[n + 4 *j];
            }
            r = data_in_tmp[0] * 2 + data_in_tmp[5] * 1;
            c = data_in_tmp[1] * 8 + data_in_tmp[2] * 4 + data_in_tmp[3] * 2 + data_in_tmp[4] * 1;
            byte[] tmp = new byte[DES3.s_box[i][r].length - c];
            for (int k = 0; k < tmp.length; k++) {
                tmp[k] = (byte)DES3.s_box[i][r][k+c];
            }
            data_out_tmp = DES3.byte_to_bit(tmp, 4);
            j++;
        }
        return data_out_tmp;
    }
    private static byte[] f_change(byte[] data_out, byte[] data_in) {
        byte []msg_e = DES3.tbl_replace(data_out, DES3.e_tbl, 48);
        msg_e = DES3.xor(msg_e, data_in, 48);
        DES3.s_change(data_out, msg_e);
        byte[] tmp = DES3.tbl_replace(data_out, DES3.p_tbl, 32);
        return tmp;
    }

    /**
     * DES 加密
     */
    private static char[] encrypt(final char[] plain_txt, final byte[][] sub_key) {
        byte[] msg_bit_l = new byte[32]; // &msg_bit[0]

        byte[] msg_bit_r = new byte[32]; //&msg_bit[32];
        byte[]      tmp = new byte[32];
        System.out.print("msg_i:");
        byte [] my_msg_i = new byte[plain_txt.length];
        for (int i = 0; i < plain_txt.length; i++) {
            my_msg_i[i] = (byte)plain_txt[i];
            System.out.print(String.format("%d ", (int)my_msg_i[i]));
        }
        System.out.print("\nmsg_bit:");
        byte[] msg_bit = DES3.byte_to_bit(my_msg_i, 64);
        for (int i = 0; i < msg_bit.length; i++) {
            System.out.print(msg_bit[i]);
        }
        System.out.print("\n");
        msg_bit = DES3.tbl_replace(msg_bit, DES3.ip_tbl, 64);
        for (int i = 0; i< msg_bit_l.length; i++) {
            msg_bit_l[i] = msg_bit[i];
        }
        for (int i = 0; i< msg_bit_r.length; i++) {
            msg_bit_r[i] = msg_bit[32+i];
        }
        for (int i = 0; i < 16; i++) {
            DES3.bit_cpy(tmp, msg_bit_r, 32);
            DES3.f_change(msg_bit_r, sub_key[i]);
            DES3.xor(msg_bit_r, msg_bit_l, 32);
            DES3.bit_cpy(msg_bit_l, tmp, 32);
        }
        msg_bit = DES3.tbl_replace(msg_bit, DES3.ipr_tbl, 64);
        return DES3.bit_to_hex(msg_bit, 64);
    }

    /**
     * DES轮解密算法
     */
    private static char[] decrypt(char[] cypher, byte[][] dec_sub_key) {
        byte[] msg_bit_l = new byte[32]; // &msg_bit[0],
        byte[] msg_bit_r = new byte[32]; // &msg_bit[32];
        byte[] tmp = new byte[32];
        byte[] msg_bit = DES3.hex_to_bit(cypher, 64);
        msg_bit = DES3.tbl_replace(msg_bit, ip_tbl, 64);
        for (int i = 0; i< msg_bit_l.length; i++) {
            msg_bit_l[i] = msg_bit[i];
        }
        for (int i = 0; i< msg_bit_r.length; i++) {
            msg_bit_r[i] = msg_bit[32+i];
        }
        for (int i = 15; i >= 0; i--) {
            DES3.bit_cpy(tmp, msg_bit_l, 32);
            DES3.f_change(msg_bit_l, dec_sub_key[i]);
            DES3.xor(msg_bit_l, msg_bit_r, 32);
            DES3.bit_cpy(msg_bit_r, tmp, 32);
        }
        msg_bit = DES3.tbl_replace(msg_bit, DES3.ipr_tbl, 64);
        return DES3.bit_to_byte(msg_bit, 64);
    }


    public static void main(String[] args) {
        String plain_txt = "hellodec";
        String enc_key = "hellokey";
        char[] enc_key_a = enc_key.toCharArray();
        char[] plain_txt_a=plain_txt.toCharArray();
        System.out.print("plain_txt:");
        for (int i = 0; i < plain_txt_a.length; i++) {
            System.out.print(String.format("%c", plain_txt_a[i]));
        }
        System.out.print("\nenc_sub_key:\n");
        byte[][] enc_sub_key = DES3.set_key(enc_key_a);
        for (int i = 0; i < enc_sub_key.length; i++) {
            for (int j = 0; j < enc_sub_key[0].length; j++) {
                System.out.print(String.format("%d", enc_sub_key[i][j]));
            }
//            System.out.print("\n");
        }
        System.out.print("\n");
        char[] result = DES3.encrypt(plain_txt_a, enc_sub_key);
        System.out.println(String.format("enc %s with key %s, get result %s", plain_txt, enc_key, new String(result)));
        DES3.test();
    }

    public static void test(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入加密内容(8 bytes):");
        char [] plain_txt = scanner.next().toCharArray();
        int i = plain_txt.length;
        while (i != 8) {
            System.out.println("输入内容长度有误，请输入8字节加密内容");
            plain_txt = scanner.next().toCharArray();
            i = plain_txt.length;
        }
        System.out.println("请输入加密密钥(8 bytes):");
        char [] enc_key = scanner.next().toCharArray();
        i = enc_key.length;
        while (i != 8) {
            System.out.println("加密密钥长度有误,请输入8字节加密密钥");
            enc_key = scanner.next().toCharArray();
            i = enc_key.length;
        }
        byte[][] enc_sub_key = DES3.set_key(enc_key);              // 生成16轮的加密子密钥；
        char[] cypher = DES3.encrypt(plain_txt, enc_sub_key);    // des的轮加密过程
        System.out.println(
            String.format(
                "使用密钥 %s 加密明文 %s 获得密文 %s",
                new String(enc_key), new String(plain_txt), new String(cypher)
            )
        );
//        System.out.println("请输入解密密钥(8 byte):");
//        char[] dec_key = scanner.next().toCharArray();
//        i = dec_key.length;
//        while (i != 8) {
//            System.out.println("解密密钥长度有误，请输入解密密钥(8 byte)");
//            dec_key = scanner.next().toCharArray();
//            i = dec_key.length;
//        }
//        byte[][] dec_sub_key = DES3.set_key(dec_key);              // 生成16轮的解密子密钥；
        char[] dec_dt = DES3.decrypt(cypher, enc_sub_key);    // 解密;
        System.out.println(
            String.format(
                "使用密钥 %s 解密密文 %s 获得明文 %s", new String(enc_key), new String(cypher), new String(dec_dt)
            )
        );
    }
}
