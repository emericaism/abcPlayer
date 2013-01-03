package sound;


/**
 * Class of Tokens representing a PlayedNote
 */
public class PlayedNoteToken implements Token {
    
    private Type type;
    private Integer length;
    private Integer numerator;
    private Integer denominator;
    private String baseNote;
    private Integer accidental;
    private Boolean hasAcc;
    private Integer octave;
     
    /**
     * Create a PlayedNoteToken with the correct parameters
     * @param n Integer numerator of length
     * @param d Integer denominator of length
     * @param b String base note (must be [A-G], upper-case)
     * @param a Integer accidental value (positive for sharps, negative for flats, 0 for natural or no accidental)
     * @param hasA Boolean if pitch had an accidental (to distinguish between a natural and no accidental)
     * @param o Integer octave transpose
     * @throws IllegalArgumentException if n==0 or d==0
     */
    public PlayedNoteToken(Integer n, Integer d, String b, Integer a, Boolean hasA, Integer o) {
        type = Type.PLAYEDNOTETOKEN;

        if (n == 0 || d == 0) {
            throw new IllegalArgumentException("Invalid PlayedNote length");
        }
        numerator = n;
        denominator = d;
        baseNote = b;
        accidental = a;
        hasAcc = hasA;
        octave = o;       
    }
    
    /**
     * getter functions for fields
     */
    public Type getType() {return type;}
    public Integer getNumerator() {return numerator;}
    public Integer getDenominator() {return denominator;}
    public String getBaseNote() {return baseNote;}
    public Integer getAccidental() {return accidental;}
    public Boolean hasAccidental() {return hasAcc;}
    public Integer getOctave() {return octave;}
    
    /**
     * Override the equals method for PlayedNoteToken comparisons
     */
    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()){
            return false;
        }
        PlayedNoteToken oPlayed = (PlayedNoteToken) o;
        if (this.getType().equals(oPlayed.getType()) &&
                this.getNumerator().equals(oPlayed.getNumerator()) &&
                this.getDenominator().equals(oPlayed.getDenominator()) &&
                this.getBaseNote().equals(oPlayed.getBaseNote()) &&
                this.getAccidental().equals(oPlayed.getAccidental()) &&
                this.hasAccidental().equals(oPlayed.hasAccidental()) &&
                this.getOctave().equals(oPlayed.getOctave())
                ) { 
            return true;
        }
        return false;
    }   
}

