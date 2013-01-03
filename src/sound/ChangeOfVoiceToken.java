package sound;

/**
 * Class of Tokens representing a Change of Voice at the start of a line in the music
 */
public class ChangeOfVoiceToken implements Token {
    private Type type;
    private String stringValue;
    
    /**
     * Create a ChangeOfVoiceToken with the correct parameters
     * @param stringVal the String name of the voice
     */
    public ChangeOfVoiceToken(String stringVal) {
        type = Type.CHANGEOFVOICETOKEN;
        stringValue = stringVal;
    }
    
    /**
     * getter functions for fields
     */
    public Type getType() {return type;}
    public String getStringValue() {return stringValue;}
    
    /**
     * Override the equals method for FieldToken comparisons
     */
    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()){
            return false;
        }
        if (this.getStringValue().equals(((ChangeOfVoiceToken)o).getStringValue())) {
            return true;
        }
        return false;
    }
}
