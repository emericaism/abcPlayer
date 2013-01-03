package sound;

/**
 * Class of Tokens representing a Rest
 */
public class RestToken implements Token{
    
    private Type type;
    private Integer numerator;
    private Integer denominator;
    
    /**
     * Create a RestToken with the correct parameters
     * @param n Integer numerator of length
     * @param d Integer denominator of length
     * @throws IllegalArgumentException if n==0 or d==0
     */
    public RestToken(Integer n, Integer d) {
        type = Type.RESTTOKEN;
        if (n == 0 || d == 0) {
            throw new IllegalArgumentException("Invalid PlayedNote length");
        }
        numerator = n;
        denominator = d;
    }

    /**
     * getter functions for fields
     */
    public Integer getNumerator() {return numerator;}
    public Integer getDenominator() {return denominator;}
    public Type getType() {return type;}
    
    /**
     * Override the equals method for RestToken comparisons
     */
    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()){
            return false;
        }
        RestToken oRest = (RestToken) o;
        if (this.getType().equals(oRest.getType()) &&
                this.getNumerator().equals(oRest.getNumerator()) &&
                this.getDenominator().equals(oRest.getDenominator())
                ) {       
            return true;
        }
        return false;
    }
}
