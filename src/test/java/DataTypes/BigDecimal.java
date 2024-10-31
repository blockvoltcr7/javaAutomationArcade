package DataTypes;

public class BigDecimal {

    //create a big decimal object and then convert it to a hundthreths plaec value
    public static void main(String[] args) {
        java.math.BigDecimal bigDecimal = new java.math.BigDecimal("123.456");
        java.math.BigDecimal hundredthsPlace = bigDecimal.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        System.out.println(hundredthsPlace);
    }

    //create a scenario in a function where one data structure has 5 decimal places and the other has 10 and create an assertion statement to ensure that the values are correct
    public static void compareBigDecimals() {
        java.math.BigDecimal bigDecimal1 = new java.math.BigDecimal("123.45678");
        java.math.BigDecimal bigDecimal2 = new java.math.BigDecimal("123.45678");
        assert bigDecimal1.compareTo(bigDecimal2) == 0;
    }
}
