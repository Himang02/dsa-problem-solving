import java.util.*;

public class MathMethods {
    public static void main(String[] args) {
        /* 
         * Maximum value of various data types
         * Integer: 2,147,483,647
         * Long: 9,223,372,036,854,775,807
         * Float: 3.4028235E38
         * Double: 1.7976931348623157E308
         */

        System.out.println(Math.abs(-10));         // absolute value
        System.out.println(Math.max(10, 20));
        System.out.println(Math.min(10, 20));
        System.out.println(Math.sqrt(17));
        System.out.println(Math.pow(2, 3));
        System.out.println(Math.log(10));       // natural log
        System.out.println(Math.log10(10));     // base 10 log
        System.out.println(Math.log(16)/Math.log(2));        // base 2 log
        System.out.println(Math.exp(1));        // e^x
        System.out.println(Math.sin(Math.PI/2)); // sin 90 degree
        System.out.println(Math.cos(0));         // cos 0 degree
        System.out.println(Math.tan(Math.PI/4)); // tan 45 degree
        System.out.println(Math.ceil(4.3));      // smallest integer greater than or equal to the argument
        System.out.println(Math.floor(4.7));     // largest integer less than or equal to the argument
        System.out.println(Math.round(4.5f));    // round to nearest integer
        System.out.println(Math.round(4.5));     // round to nearest integer
        System.out.println(Math.random());        // random number between 0.0 and 1.0

        // print a float up to 3 decimal places
        System.out.println(String.format("%.3f", Math.random()*100));

        int a = 9, b = 4;
        System.out.println((a+b-1)/b);  // ceiling of integer division
        System.out.println(a/b); // floor of integer division   

    }
}