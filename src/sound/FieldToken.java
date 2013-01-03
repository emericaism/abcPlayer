package sound;

/**
 * Class of Tokens representing fields in the header
 */
public class FieldToken implements Token {
    
    private Type type;
    private FieldType fieldType;
    private String stringValue;
     
    /**
     * types of FieldTokens:
     */
    public static enum FieldType {
        FIELDINDEXNUMBER, FIELDTITLE, FIELDCOMPOSER, FIELDDEFAULTLENGTH, FIELDMETER, FIELDTEMPO, FIELDVOICE, FIELDKEY
    }
    
    /**
     * Create a FieldToken with the correct parameters
     * @param ftype FieldType of this new FieldToken
     * @param stringVal the String value to be stored
     */
    public FieldToken(FieldType ftype, String stringVal) {
        type = Type.STRUCTURALTOKEN;
        fieldType = ftype;
        stringValue = stringVal;
    }

    /**
     * getter functions for fields
     */
    public Type getType() {return type;}
    public FieldType getFieldType() {return fieldType;}
    public String getStringValue() {return stringValue;}
    
    
    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()){
            return false;
        }
        if (this.getFieldType().equals(((FieldToken)o).getFieldType()) && this.getStringValue().equals(((FieldToken)o).getStringValue())) {
            return true;
        }
        return false;
    }
}
