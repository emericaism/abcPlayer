package sound;

/**
 * Class of Tokens that deal with the overall structure of the music
 */
public class StructuralToken implements Token {
    
    private Type type;
    private StructType structType;
    
    /**
     * types of Structural Tokens
     */
    public static enum StructType {
        ENDOFSECTION,
        BEGINREPEAT,
        ENDREPEAT,
        FIRSTREPEAT,
        SECONDREPEAT,
        BARLINE,
        SPACE,
        BEGINCHORD,
        ENDCHORD,
        END
    }
    
    /**
     * Create a StructuralToken with the correct structural type
     * @param stype, StructType is the Structural type
     */
    public StructuralToken(StructType stype) {
        type = Type.STRUCTURALTOKEN;
        structType = stype;
    }

    /**
     * getter functions for fields
     */
    public Type getType() {return type;}
    public StructType getStructuralType() {return structType;}

    /**
     * Override the equals method for StructuralToken comparisons
     */
    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()){
            return false;
        }
        if (this.getStructuralType().equals(((StructuralToken)o).getStructuralType())) {
            return true;
        }
        return false;
    }
}
