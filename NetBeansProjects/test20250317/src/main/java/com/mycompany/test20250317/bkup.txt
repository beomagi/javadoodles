
package com.mycompany.test20250317;

import java.util.*;

public class Test20250317 {
    
    public static int counter=0;
    public static volatile int threadActive=1;
    public static volatile String pass;
    
    public static String makePass(int[] passchars, char[] opts) {
        StringBuilder password = new StringBuilder(passchars.length);
        for (int a=0;a<passchars.length;a++){
            if (passchars[a]>=0) {
                password.append(opts[passchars[a]]);
            }
        }
        return password.toString();
    }

    public static void incrementArray(int[] passchars, int dictionarysize){
        passchars[0]+=1;
        for (int a=0;a<passchars.length;a++){
            if (passchars[a]>=dictionarysize) {
                passchars[a]=0;
                passchars[a+1]+=1;
            }
        }
    }
   
    public static void main(String[] args) throws InterruptedException {
        Thread outputThread = new Thread(() -> {
            while (threadActive==1) {
                System.out.println("...checked "+ counter+" passwords... "+pass);
                try {
                    Thread.sleep(1000); // wait 1 second between prints
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        outputThread.start();
        char[] options = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',' '};
        String password = "ANG3L5";
        int maxChars = 7;                           //  ------- set max chars here -------
        int[] passwordChars = new int[maxChars+1];  //  ------- set size +1, when extra char gets set, all combinations of length maxChars were used -------
        Arrays.fill(passwordChars,-1);              //  ------- initialize to -1, for no chars -------
        while(passwordChars[maxChars]<0){
            counter+=1;
            incrementArray(passwordChars,options.length);    //  ------- +1 to char counter array -------
            pass=makePass(passwordChars,options);            //  ------- get password from char counter array -------
            if (pass.equals(password)) {                     //  ------- compare
                threadActive=0;
                System.out.println("Password found --- "+pass);
                passwordChars[maxChars]=0;//quits - flag for end
            }
        }
        try {
            outputThread.join();
        } catch (InterruptedException e) {}
    }
}