package sound;

/**
 * Terminals and Non-terminals are specified in the design document
 * A token is a lexical item that the parser uses.
 */

public interface Token {
    /**
     * Possible Token.Type (each has a Class that implements Token):
     */
    public static enum Type {
        FIELDTOKEN, STRUCTURALTOKEN, RESTTOKEN, PLAYEDNOTETOKEN, TUPLETTOKEN, CHANGEOFVOICETOKEN, END
    }
    public Type getType();
}
