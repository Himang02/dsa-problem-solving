import java.util.*;

public class StringMethods {
    public static void main(String[] args) {
        String str = "HelloWorlldo";
        System.out.println(str.length());
        System.out.println(str.charAt(0));
        System.out.println(str.substring(0, 5));                            // 5 in not included
        System.out.println(str.substring(5));                               // from index 5 to end
        System.out.println(str.indexOf('o'));                               // first occurrence of 'o'
        System.out.println(str.indexOf('o', str.indexOf('o') + 1));         // start searching from index
        System.out.println(str.indexOf('o', str.indexOf('o', str.indexOf('o') + 1)+1));     
        System.out.println(str.indexOf("ll"));
        System.out.println(str.indexOf("ll", str.indexOf("ll")+1));
        System.out.println(str.contains("abc"));
        System.out.println(str.startsWith("He"));
        System.out.println(str.startsWith("he"));
        System.out.println(str.endsWith("ld"));
        System.out.println(str.equals("HelloWorlldo"));
        System.out.println(str.equalsIgnoreCase("helloworld"));
        String str2 = "   Hello World   ";
        System.out.println(str2.trim());
        System.out.println(str.replace("Hello", "Hi"));
        System.out.println(str.toLowerCase());
        System.out.println(str.toUpperCase());
        System.out.println(str);


        /* 
        * Sorting string and char array
        */
        char chArr1[] = str.toCharArray();
        char chArr2[] = str.toCharArray();
        
        // using in-built sort function
        Arrays.sort(chArr1);
        String str3 = new String(chArr1);
        System.out.println(str3);
        
        // using count sort (because we know the range of characters)
        int count[] = new int[52];
        for(int i=0; i<chArr2.length; i++) {
            if(chArr2[i] >= 'a' && chArr2[i] <= 'z') {
                count[chArr2[i] - 'a' + 26]++;
            } else if(chArr2[i] >= 'A' && chArr2[i] <= 'Z') {
                count[chArr2[i] - 'A']++;
            }
        }
        int index = 0;
        for(int i=0; i<52; i++) {
            while(count[i] > 0) {
                if(i < 26) {
                    chArr2[index++] = (char)('A' + i);
                } else {
                    chArr2[index++] = (char)('a' + i - 26);
                }
                count[i]--;
            }
        }
        String str4 = new String(chArr2);
        System.out.println(str4);



    }
}