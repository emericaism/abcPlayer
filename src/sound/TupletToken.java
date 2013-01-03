package sound;

/**
 * Class of Token that represents the start of a tuplet: ( [2-4]
 */
public class TupletToken implements Token {
    
    private Type type;
    private TupletType tupletType;
    
    /**
     * types of Tuplet Tokens
     */
    public static enum TupletType {
        DUPLET,
        TRIPLET,
        QUADRUPLET
    }
    
    /**
     * Create a TupletToken with the correct tuplet type
     * @param tupInt Integer representing tuplet type
     * @throws IllegalArgumentException if tupInt is not 2 or 3 or 4 (we only allow duplets, triplets, quadruplets)
     */
    public TupletToken(Integer tupInt) {
        type = Type.TUPLETTOKEN;
        if (tupInt == 2) {
                tupletType = TupletType.DUPLET;
        }
        else if (tupInt == 3) {
            tupletType = TupletType.TRIPLET;
        }
        else if (tupInt == 4) {
            tupletType = TupletType.QUADRUPLET;
        }
        else {
            throw new IllegalArgumentException("Only duplets, triplets and quadruplets allowed");            
        }  
    }
    
    /**
     * getter functions for fields
     */
    public Type getType() {return type;}
    public TupletType getTupletType() {return tupletType;}
    
    /**
     * Override the equals method for TupletToken comparisons
     */
    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()){
            return false;
        }
        if (this.getTupletType().equals(((TupletToken)o).getTupletType())) {
            return true;
        }
        return false;
    }
}

