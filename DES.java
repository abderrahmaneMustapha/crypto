import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.*;

class DES{
  String message ;
  String key;
  public DES( String message, String key ){
    this.message = message;
    this.key = key;
  }

  public  String textToHexa()  {
    byte [] myBytes = {};
    try {
      myBytes = message.getBytes("UTF-8");
    } catch(Exception exception_name) {

    }

     return DatatypeConverter.printHexBinary(myBytes);
  }

  public String hexToBinary(String block){
    Map <String, String>  result = new HashMap<String, String>();
    if( !block.isEmpty()  ){
    result.put("0", "0000");
    result.put("1", "0001");
    result.put("2", "0010");
    result.put("3", "0011");
    result.put("4", "0100");
    result.put("5", "0101");
    result.put("6", "0110");
    result.put("7", "0111");
    result.put("8", "1000");
    result.put("9", "1001");
    result.put("A", "1010");
    result.put("B", "1011");
    result.put("C", "1100");
    result.put("D", "1101");
    result.put("E", "1110");
    result.put("F", "1111");
  }

  String returned_msg = "";
   for (Integer i = 0; i < block.length(); i++) {
      returned_msg += result.get(block.substring(i,i+1));
   }
    return returned_msg;
  }


  // Step 01
  public String Pc_1 (String [] k){
    String joined_K = String.join("",k);
    int [] PC1 = {
      57,   49,    41,   33,    25,    17,    9,
      1,   58,    50,   42,    34,    26,   18,
      10,    2,    59,   51,    43,    35,   27,
      19,   11,     3,   60,    52,    44,   36,
      63,   55,    47,   39,    31,    23,   15,
      7,   62,   54,   46,    38,    30,   22,
      14,    6,   61,   53,    45,    37,   29,
      21,   13,     5,   28,    20,    12,    4,
    };
    String result_K  = "";
    for (int i=0 ; i < 56; i++){
      result_K += joined_K.charAt(PC1[i] - 1);
    }


    return result_K;
  }

  public void shift(String [] C, String [] D){
    for(int  i= 0 ; i< 17; i++){
      if( i== 0 || i ==1|| i==8 || i==15){
        C[i+1] =  C[i].substring(1,C[i].length()) + C[i].charAt(0);
        D[i+1] =  D[i].substring(1,D[i].length()) + D[i].charAt(0);


      }else {
         C[i+1] = C[i].substring(2, C[i].length())+ C[i].charAt(0) + C[i].charAt(1);
         D[i+1] = D[i].substring(2, D[i].length())+ D[i].charAt(0) + D[i].charAt(1);

      }

    }
    /*for (Integer i = 0; i < 17; i++) {
      System.out.println("C["+i+"]= "+C[i]);
      System.out.println("D["+i+"]= "+D[i] + "\n");
    }*/
  }

  public void Pc_2(String [] C , String [] D, String [] K){
      String [] temp=  new String[17];
      for(int i=1 ;  i <17; i++){
        temp[i-1] = C[i] + D[i];
      };

      int [] pc_2 = {
        14,    17,   11,    24,     1,    5,
        3,    28,   15,    6,    21,   10,
        23,    19,   12,     4,    26,    8,
        16,     7,   27,    20,    13,    2,
        41,    52,   31,    37,    47,   55,
        30,    40,   51,    45,    33,   48,
        44,    49,   39,    56,    34,   53,
        46,    42,   50,    36,    29,  32,
      };

      for(int i = 0; i < 16; i++){
        String add = "";
        for (int j = 0; j < 48; j++) {
          add+= temp[i].charAt(pc_2[j]-1);
        }

        K[i] = add;


      }

  }

  public String ip(String m){
    int [] IP = {
    58,    50,   42,    34,    26,   18,    10,    2,
    60,    52,   44,    36,    28,   20,    12,    4,
    62,    54,   46,    38,    30,   22,    14,    6,
    64,    56,   48,    40,    32,   24,    16,    8,
    57,    49,   41,    33,    25,   17,     9,    1,
    59,    51,   43,    35,    27,   19,    11,    3,
    61,    53,   45,    37,    29,   21,    13,    5,
    63,    55,   47,    39,    31,   23,    15,    7};

    String result = "";
    for(int i=0; i< 64; i++){
      result+= m.charAt(IP[i]-1);
    }

    return result;
  }

  public String add_zeros(String block){
    String result = block;
    for(int i=0; i < 4 - block.length(); i++){
         result = "0" + result;
    }

    return result;
  }
  public String sbox_and_xor(int [] s, String result, int i, String F){

    int row  = Integer.parseInt(result.substring(i,i+1) + result.substring(i+5,i+6), 2)  * 16;
    int col = Integer.parseInt(result.substring(i+1,i+5), 2);
    String val = add_zeros(Integer.toBinaryString(s[row+col]));
    return val;

  }

  public String p(String block){
  /*he permutation P is defined in the following table.
   P yields a 32-bit output from a
  32-bit input by permuting the bits of the input block.*/
  int [] P = {
   16,   7,  20,  21,
   29,  12,  28,  17,
    1,  15,  23,  26,
    5,  18,  31,  10,
    2,   8,  24,  14,
   32,  27,   3,   9,
   19,  13,  30,   6,
   22,  11,   4,  25,
 };
  String permutation_result = "";
  for(int i=0; i< 32; i++){
    permutation_result += block.charAt(P[i] - 1);
  }

  return permutation_result;
  }
  public String f(String R, String K){
    //  we first expand each block Rn-1 from 32 bits to 48 bits.
    int[] E = { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13,
       12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23,
       24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };
     String e_r = "";

     for (Integer i = 0; i < 48; i++) {
       e_r += R.charAt(E[i]-1);
     };
     //K0 xor E(R0)
     String xor_result = "";
     for (Integer i = 0; i < 48; i++) {
        xor_result += Integer.parseInt(e_r.substring(i, i+1)) ^  Integer.parseInt(K.substring(i, i+1));
         };

    //  8  S boxes
    int[] sbox1 = { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
       0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14,
       8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9,
       1, 7, 5, 11, 3, 14, 10, 0, 6, 13 };
   int[] sbox2 = { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
       3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7,
       11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3,
       15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 };
   int[] sbox3 = { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
       13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4,
       9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9,
       8, 7, 4, 15, 14, 3, 11, 5, 2, 12 };
   int[] sbox4 = { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
       13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9,
       0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1,
       13, 8, 9, 4, 5, 11, 12, 7, 2, 14 };
   int[] sbox5 = { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
       14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1,
       11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1,
       14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 };
   int[] sbox6 = { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
       10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14,
       15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9,
       5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 };
   int[] sbox7 = { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
       13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11,
       13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4,
       10, 7, 9, 5, 0, 15, 14, 2, 3, 12 };
   int[] sbox8 = { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
       1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4,
       1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10,
       8, 13, 15, 12, 9, 0, 3, 5, 6, 11 };
      String f_result = "";

     f_result+= sbox_and_xor(sbox1, xor_result, 0, f_result);
     f_result+= sbox_and_xor(sbox2, xor_result, 6, f_result);
     f_result+= sbox_and_xor(sbox3, xor_result, 12, f_result);
     f_result+= sbox_and_xor(sbox4, xor_result, 18, f_result);
     f_result+= sbox_and_xor(sbox5, xor_result, 24, f_result);
     f_result+= sbox_and_xor(sbox6, xor_result, 30, f_result);
     f_result+= sbox_and_xor(sbox7, xor_result, 36, f_result);
     f_result+= sbox_and_xor(sbox8, xor_result, 42, f_result);

     f_result =  p(f_result);
     //System.out.println(f_result);

      return f_result;
  }

  public String l_xor_f(String l, String f){
    String result = "";
    for(int i=0; i<32 ; i++){
      result += Integer.parseInt(l.substring(i, i+1)) ^ Integer.parseInt(f.substring(i, i+1));
    }
    return result;
  }

  public String last_perm(String block){
   int[] IPi =
	{
			40, 8, 48, 16, 56, 24, 64, 32,
	        39, 7, 47, 15, 55, 23, 63, 31,
	        38, 6, 46, 14, 54, 22, 62, 30,
	        37, 5, 45, 13, 53, 21, 61, 29,
	        36, 4, 44, 12, 52, 20, 60, 28,
	        35, 3, 43 ,11, 51, 19, 59, 27,
	        34, 2, 42, 10, 50, 18, 58, 26,
	        33, 1, 41, 9, 49, 17, 57, 25
	};
  String result = "";
  for(int i = 0;i < 64; i++ ){
      result += block.charAt(IPi[i]-1);
  }

  return result;
  }
  public static void main(String[] args) {
    /*  String m = "Your lips are smoother than vaseline".toUpperCase();


      String hex = des.textToHexa();
      //System.out.println(block_hex);
      // we take a block of 64 bite  this mean that we take  hex text of 16 lenght
      String block_hex = hex.substring(0,16);
      //System.out.println(block_hex);
      String [] M  = new String[16];
      for(int i=0 ; i < block_hex.length(); i++){
          M[i] = des.hexToBinary(block_hex.substring(i,i+1));
          //System.out.println(des.hexToBinary(block_hex.substring(i,i+1)));
      }*/
      String k = "133457799BBCDFF1";
      String M  = "0123456789ABCDEF";
      DES des  = new DES(M, k);
      M = des.hexToBinary(M);

      String [] L = new String[17];
      String [] R = new String[17];
      M = des.ip(M);

      L[0] = M.substring(0,32);
      R[0] = M.substring(32,64);

      /*
      Ln+1 = Rn-1
      Rn = Ln-1 + f(Rn-1,Kn)
      */

///key generaion
     String  [] K= new String[8];
     int i = 0 , j= 0;

     while( i< k.length() &&   j < 8 )
     {
       K[j] = des.hexToBinary(k.substring(i,i+1)) + des.hexToBinary(k.substring(i+1,i+2));
      //System.out.println(K[j].charAt(0));
      i+=2;
      j++;
     }


    /*
    // Step 1: Create 16 subkeys, each of which is 48-bits long.
    */
    String temp_K_Plus  = des.Pc_1(K);

    /*String [] K_Plus = new String[8];
    j = 0;
    String temp = "";
    for( i=0; i< temp_K_Plus.length(); i++){
      temp += temp_K_Plus.charAt(i);
      if( temp.length() == 7 ){
        K_Plus[j] = temp;
        temp = "";
          j++;

      }
    }
    System.out.println(String.join(" ",K_Plus));
    */

    String [] C = new String[18];
    String [] D = new String[18];
    C[0] = temp_K_Plus.substring(0, 28);
    D[0] = temp_K_Plus.substring(28, 56);
    des.shift(C,D);

    String [] new_key =  new String[17];
    des.Pc_2(C,D,new_key);
    for(int index=1; index < 17; index++){
        String func  = des.f(R[index-1],new_key[index-1]);
          L[index] = R[index-1];

          R[index] = des.l_xor_f(L[index-1], func);

    }
    System.out.println();
    //11101111010010100110010101000100
    //11101111010010100110010101000100
    String mix_reverse = R[16] + L[16];
    //System.out.println(mix_reverse);
    //0000101001001100110110011001010101000011010000100011001000110100

    mix_reverse = des.last_perm(mix_reverse);
    String [] Crypted = new String[8];

    int counter = 0;
    for(int c = 0 ; c < 64 ; c+=8){

      Crypted[counter] = mix_reverse.substring(c, c+8);
      System.out.println(Crypted[counter]);
      counter ++;

    }
    String [] hex = new String[8];
    for(int o = 0 ; o < 8 ; o++){

      int decimal = Integer.parseInt(Crypted[o], 2);
      hex[o]= Integer.toString(decimal, 16);
    }






  }
}
