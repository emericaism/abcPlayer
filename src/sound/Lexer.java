package sound;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sound.FieldToken.FieldType;
import sound.StructuralToken.StructType;
import sound.Token.Type;


/**
 * A lexer takes a string and splits it into tokens that are meaningful to a
 * parser.
 * The lexer uses patterns or String.equals to match input substrings with token types.
 * 
 */
public class Lexer {
    
    //define patterns to match input substring with tokens
    private final Pattern lengthDigitPattern = Pattern.compile("[0-9]");
    private final Pattern baseNotePattern = Pattern.compile("[a-gA-G]");
    private final Pattern lowerCaseBaseNotePattern = Pattern.compile("[a-g]");
    
    //private fields
    private final String myFileName; //name of file to be read
    public final ArrayList<String> headerLines;
    public final ArrayList<String> musicLines;
    //for music lexing
    private Integer linePtr = 0;
    private Integer lineEndIndex = 0;
    private String currentTrimmedLine = "";
    
    /**
     * @effects Creates the lexer using the passed fileName and sets headerLines and musicLines
     * @param fileName String name of file to be tokenized
     * @throws IllegalArgumentException if filename is null
     */
    public Lexer(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("file name must not be null");
        }
        //initialize private fields
        this.myFileName = fileName;
        this.headerLines = new ArrayList<String>();
        this.musicLines = new ArrayList<String>();   
        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            //set headerLines and musicLines fields
            getHeaderAndMusicLines(fileScanner);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("file not found");
        }
    }
    
    /**
     * @effects Mutate myScanner to get the header and music
     *  lines and add them to headerLines and musicLines
     * @param scanner, Scanner reading the file
     * 
     */
    public void getHeaderAndMusicLines(Scanner scanner) {
        boolean reachedEndHeader = false;
        while (scanner.hasNextLine()){ // file still has lines
            String line = scanner.nextLine();
            if (!reachedEndHeader) { 
                this.headerLines.add(line);
                if (line.length()!=0) { //empty lines have length 0
                    if (line.substring(0, 1).equals("K")) {
                        reachedEndHeader = true;
                    }
                }
            }
            else {
                this.musicLines.add(line);
            }
        }
    }
    
    ////////////////HEADER////////////////////////////
    
    /**
     * @effects Gets all of the header tokens
     * @return ArrayList<FieldToken> of all header tokens
     * @throws IllegalArgumentException for invalid header
     */
    public ArrayList<FieldToken> getHeaderTokens() {
        ArrayList<FieldToken> headerTokens = new ArrayList<FieldToken>();
        FieldType fieldType = null;
        String trimmed;
        for (String line:headerLines) {
            trimmed = line;
            if (trimmed.indexOf("%") != -1){ //remove comments
                trimmed = trimmed.substring(0, trimmed.indexOf("%"));
            }
            trimmed = trimmed.trim(); //remove spaces
            if (!trimmed.equals("")){ //check for empty lines
                String firstPart = trimmed.substring(0, 2);
                if (firstPart.equals("X:")){
                    fieldType = FieldType.FIELDINDEXNUMBER;
                }
                else if (firstPart.equals("T:")){
                    fieldType = FieldType.FIELDTITLE;
                }
                else if (firstPart.equals("C:")){
                    fieldType = FieldType.FIELDCOMPOSER;
                }
                else if (firstPart.equals("L:")){
                    fieldType = FieldType.FIELDDEFAULTLENGTH;
                }
                else if (firstPart.equals("M:")){
                    fieldType = FieldType.FIELDMETER;
                }
                else if (firstPart.equals("Q:")){
                    fieldType = FieldType.FIELDTEMPO;
                }
                else if (firstPart.equals("V:")){
                    fieldType = FieldType.FIELDVOICE;
                }
                else if (firstPart.equals("K:")){
                    fieldType = FieldType.FIELDKEY;
                }
                else { //not a fieldType or \n
                    throw new IllegalArgumentException("Invalid header");
                }
                String stringVal = trimmed.substring(2);
                stringVal = stringVal.trim();
                headerTokens.add(new FieldToken(fieldType,stringVal));
            }
        }        
        return headerTokens;
    }
    
    
    
    ////////////////MUSIC////////////////////////////
    
    /**
     * @effects Mutate linePtr and currentTrimmedLine to get all of the music Tokens
     * @return ArrayList<Token> of all tokens for the music
     */
    public ArrayList<Token> getMusicTokens() {
        ArrayList<Token> musicTokens = new ArrayList<Token>();
        for (String line:musicLines) {
            String trimmed = line;
            if (trimmed.indexOf("%") != -1){ //remove comments
                trimmed = trimmed.substring(0, trimmed.indexOf("%"));
            }
            trimmed = trimmed.trim(); //remove spaces
            linePtr = 0; //reset to start of the new line
            lineEndIndex = trimmed.length()-1;
            currentTrimmedLine = trimmed;
            musicTokens.addAll(getAllTokensForLine());
        }
        return musicTokens;
        
    }
    
    /**
     * @effects Mutates linePtr and currentTrimmedLine to get all of the music Tokens for the current line
     * @return ArrayList<Token> of all tokens for the next line of music
     */
    private ArrayList<Token> getAllTokensForLine() {
        ArrayList<Token> allTokensForLine = new ArrayList<Token>();
        Boolean hitEnd = false;
        Token nextTok = getNextLineToken();
        if (nextTok.getType() == Type.STRUCTURALTOKEN) {
            if (((StructuralToken)nextTok).getStructuralType() == StructType.END){
                hitEnd = true;
            }
        }
        while (!hitEnd) {
            allTokensForLine.add(nextTok);
            nextTok = getNextLineToken();
            if (nextTok.getType() == Type.STRUCTURALTOKEN) {
                if (((StructuralToken)nextTok).getStructuralType() == StructType.END){
                    hitEnd = true;
                }
            }
        }
        return allTokensForLine;
    }
    
    /**
     * Determines if getCharAtCurrentLineIndex() can be called without an Exception (the case with true)
     * @return true if linePtr is currently pointing to a character on the current line
     */
    private Boolean lineHasNext() { //if currently your pointer is pointing within the line
      if (linePtr<=lineEndIndex) {
          return true;
      }
      else { // index>lastIndex
          return false;
      }
    }
      
    
    /**
     * Get the String character at the current index
     * @return String character at current line index pointed to by linePtr
     * @throws IndexOutOfBoundsException if !lineHasNext() when this method is called
     */
    private String getCharAtCurrentLineIndex() {
        char c = currentTrimmedLine.charAt(linePtr);
        String s = String.valueOf(c);
        return s;
    }
 
    /**
     * Requires currentTrimmedLine has been de-commented and trimmed
     * @effects Mutates linePtr
     * @return the next Token from the current line
     * @throws IllegalArgumentException for invalid characters
     */
    private Token getNextLineToken(){
        Token token;
        if (lineHasNext()) {
            String c = getCharAtCurrentLineIndex();            
            Matcher mBaseNote = baseNotePattern.matcher(c);
            if (mBaseNote.matches()) {
                token = getPlayedNoteTokenFromBaseNote(0,false); //arguments are accidental, hasAccidental
            }
            else if (c.equals("_") || c.equals("^") || c.equals("=")) {
                token = getPlayedNoteTokenFromAccidental();
            }
            else if (c.equals("(")) {
                token = getTupletToken();
            }
            else if (c.equals("V")) {
                String name = getVoiceName();
                token = new ChangeOfVoiceToken(name);
            }
            else if (c.equals("z")) {
                token = getRestToken();
            }
            else if (c.equals(" ")) {
                token = new StructuralToken(StructType.SPACE);
                linePtr++;
            }
            else if (c.equals("|")) {
                //either a barline, end-of-section, begin-repeat
                token = getTokenFromBarline();
            }
            else if (c.equals("[")) {
                //either an end-of-section,  first-repeat/second-repeat, begin-chord
                token = getTokenFromOpenBracket();
            }
            else if (c.equals("]")) {
                //end of chord
                token = new StructuralToken(StructType.ENDCHORD);
                linePtr++;
            }
            else if (c.equals(":")) {
                //end-repeat
                token = getEndRepeatToken();
            }
            else {
                throw new IllegalArgumentException("Unexpected character");
            }
        }
        //linePtr is pointing to the first character of the next token that could be created in this successive call of this method
        else {
            token = new StructuralToken(StructType.END);
        }
        return token;
    }
    
    /**
     * Check for either an end-of-section,  first-repeat/second-repeat, begin-chord
     * @requires getCharAtCurrentLineIndex() is "["
     * @effects Mutates linePtr
     * @return the next StructuralToken in the current line
     * @throws IllegalArgumentException for invalid sequence of characters on line
     */
    private StructuralToken getTokenFromOpenBracket() {
        String c = getCharAtCurrentLineIndex();
        if (c.equals("[")) {
            linePtr++;
            if (!lineHasNext()) { //Must be just a | barline
                return new StructuralToken(StructType.BARLINE);
            }
            else {
                c = getCharAtCurrentLineIndex();
                if (c.equals("|")) {
                    linePtr++; //linePtr points to first character after [|
                    if (lineHasNext()) {
                        c = getCharAtCurrentLineIndex();
                        if (c.equals("|") || c.equals("]")) {
                            throw new IllegalArgumentException("Cannot have three of [ or | or ] in a row");
                        }
                    }
                    return new StructuralToken(StructType.ENDOFSECTION);  
                }
                else if (c.equals("1")) {
                    linePtr++; //linePtr points to first character after [1
                    return new StructuralToken(StructType.FIRSTREPEAT);  
                }
                else if (c.equals("2")) {
                    linePtr++; //linePtr points to first character after [2
                    return new StructuralToken(StructType.SECONDREPEAT);  
                }
                else { //Begin chord
                    return new StructuralToken(StructType.BEGINCHORD);
                }
            }
        }
        else {
            throw new IllegalArgumentException("Expected a [");
        }
    }
    
    /**
     * Check for either a bar-line, end-of-section, begin-repeat
     * @requires getCharAtCurrentLineIndex() is "|"
     * @effects Mutates linePtr
     * @return the next StructuralToken in the current line
     * @throws IllegalArgumentException for invalid sequence of characters on line
     */
    private StructuralToken getTokenFromBarline() {
        String c = getCharAtCurrentLineIndex();
        if (c.equals("|")) {
            linePtr++;
            if (!lineHasNext()) { //Must be just a | barline
                return new StructuralToken(StructType.BARLINE);
            }
            else {
                c = getCharAtCurrentLineIndex();
                if (c.equals("|") || c.equals("]") || c.equals("[")) {
                    linePtr++; //linePtr points to first character after || or |]
                    if (lineHasNext()) {
                        c = getCharAtCurrentLineIndex();
                        if (c.equals("|") || c.equals("]")) {
                            throw new IllegalArgumentException("Cannot have three of | or ] in a row");
                        }
                        else if (c.equals("1")){
                            linePtr++;
                            return new StructuralToken(StructType.FIRSTREPEAT);
                        }
                        else if (c.equals("2")) {
                            linePtr++;
                            return new StructuralToken(StructType.SECONDREPEAT);
                        }
                    }
                    return new StructuralToken(StructType.ENDOFSECTION);  
                }
                else if (c.equals(":")) {
                    linePtr++; //linePtr points to first character after |:
                    if (lineHasNext()) {
                        c = getCharAtCurrentLineIndex();
                        if (c.equals("|") || c.equals("]") || c.equals(":")) {
                            throw new IllegalArgumentException("Cannot have three of |:| or ]:] or |::");
                        }
                    }
                    return new StructuralToken(StructType.BEGINREPEAT);  
                }
                else { //Just an ordinary barline
                    return new StructuralToken(StructType.BARLINE);
                }
            }
        }
        else {
            throw new IllegalArgumentException("Expected a |");
        }
    }
    
    /**
     * Check for end-repeat
     * @requires getCharAtCurrentLineIndex() is ":"
     * @effects Mutates linePtr
     * @return the next StructuralToken in the current line\
     * @throws IllegalArgumentException for invalid characters following :
     */
    private StructuralToken getEndRepeatToken() {
        String c = getCharAtCurrentLineIndex();
        if (c.equals(":")) {
            linePtr++;
            if (!lineHasNext()) {
                throw new IllegalArgumentException(": must be followed by |");
            }
            if (!getCharAtCurrentLineIndex().equals("|")){
                throw new IllegalArgumentException(": must be followed by |");
            }
            else {
                linePtr++; //linePtr points to first character after :|
                return new StructuralToken(StructType.ENDREPEAT);      
            }
        }
        else {
            throw new IllegalArgumentException("Expected a :");
        }
    }
    
    /**
     * Get voice name that the music is changing to
     * @requires getCharAtCurrentLineIndex() is "V"
     * @effects Mutates linePtr
     * @return String voiceName for the voice the music is changing to
     */
    private String getVoiceName() {
        String c = getCharAtCurrentLineIndex();
        if (c.equals("V")) {
            linePtr++;
            if (!lineHasNext()) {
                throw new IllegalArgumentException("V must be followed by :");
            }
            if (!getCharAtCurrentLineIndex().equals(":")){
                throw new IllegalArgumentException("V must be followed by :");
            }
            else {
                String voiceName = currentTrimmedLine.substring(2).trim(); //the rest of the line must be the voice name
                linePtr = lineEndIndex+1;
                return voiceName;           
            }
        }
        else {
            throw new IllegalArgumentException("Expected a V");
        }
    }
    
    
    /**
     * Get the next TupletToken
     * @effects Mutates linePtr
     * @return the next TupletToken in the current line
     * @throws IllegalArgumentException for invalid characters following (
     */
    private TupletToken getTupletToken(){
        String open = getCharAtCurrentLineIndex();
        TupletToken tok;
        
        //check getCharAtCurrentLineIndex() currently points to the rest
        if (!open.equals("(")) {
            throw new IllegalArgumentException("Expected a ( for a tuplet");
        }
        
        linePtr++; // linePtr now expects a number for tuplet type
        if (lineHasNext()) {
            String c = getCharAtCurrentLineIndex(); //expect c to be "2","3" or "4"
            tok = new TupletToken(Integer.valueOf(c));
            linePtr++;
            return tok;
        }
        else {
            throw new IllegalArgumentException("( must be followed by tuplet integer");
        }
    }

    /**
     * Get the next RestToken on the line
     * @requires getCharAtCurrentLineIndex() currently points to the rest. Else, throw IllegalArgumentException
     * @effects Mutates linePtr
     * @return the next RestToken in the current line
     * @throws IllegalArgumentException for invalid character sequence
     */
    private RestToken getRestToken(){
        String rest = getCharAtCurrentLineIndex();
        Integer[] numdenom;
        RestToken tok;
        
        //check getCharAtCurrentLineIndex() currently points to the rest
        if (!rest.equals("z")) {
            throw new IllegalArgumentException("Expected a rest, z");
        }
        
        linePtr++; // linePtr now pointing to possible length
        numdenom = getNumeratorAndDenominator();
        //linePtr now points to after a length; check it's not a / (invalid)
        if (getCharAtCurrentLineIndex().equals("/")) {
            throw new IllegalArgumentException("Invalid location of /");
        }
        Integer numerator = numdenom[0];
        Integer denominator = numdenom[1];
        tok = new RestToken(numerator, denominator);
        return tok;
    }
    
    /**
     * Gets next PlayedNoteToken
     * @effects Mutates linePtr
     * @return the next PlayedNoteToken in the current line
     * @throws IllegalArgumentException for invalid character sequence
     * @param accidental the accidental value for this played Note
     * @param hasAccidental Boolean if the note had the accidental
     * @return next PlayedNoteToken on line
     */
    private PlayedNoteToken getPlayedNoteTokenFromBaseNote(int accidental, boolean hasAccidental){
        String baseNote = getCharAtCurrentLineIndex();
        Integer octave = 0;
        Integer[] numdenom;
        PlayedNoteToken tok;
        
        //check getCharAtCurrentLineIndex() currently points to the baseNote
        Matcher mBaseNote = baseNotePattern.matcher(baseNote);
        if (!mBaseNote.matches()) {
            throw new IllegalArgumentException("Expected a baseNote");
        }
        
        //Check for upper case baseNotes because they are an octave higher
        Matcher mLowerCaseBaseNote = lowerCaseBaseNotePattern.matcher(baseNote);
        if (mLowerCaseBaseNote.matches()) {
            baseNote = baseNote.toUpperCase();
            octave++;
        }
        
        linePtr++; // linePtr now pointing to possible octave or length
        //get octave
        octave = octave + getOctaveFromString();
        //linePtr now points to a possible length
        numdenom = getNumeratorAndDenominator();
        //linePtr now points to after a length; check it's not a / (invalid)
        if (lineHasNext()) {
            if (getCharAtCurrentLineIndex().equals("/")) {
                throw new IllegalArgumentException("Invalid location of /");
            }
        }
        Integer numerator = numdenom[0];
        Integer denominator = numdenom[1];
        tok = new PlayedNoteToken(numerator, denominator, baseNote, accidental, hasAccidental, octave);
        return tok;
    }
    
    
    /**
     * Get the octave value
     * @requires getCharAtCurrentLineIndex() currently is a possible first "'" or a ","
     * @return Integer octave value
     */
    private Integer getOctaveFromString() {
        Integer octave = 0;
        Boolean hadUp = false;
        Boolean hadDown = false;
        while (lineHasNext()) {
            String c = getCharAtCurrentLineIndex();
            if (c.equals("'")) {
                if (hadDown) {
                    throw new IllegalArgumentException("Cannot have , followed by '");
                }
                hadUp = true;
                octave++;
            }
            else if (c.equals(",")) {
                if (hadUp) {
                    throw new IllegalArgumentException("Cannot have ' followed by ,");
                }
                hadDown = true;
                octave--;
            }
            else {
                return octave; //linePtr is pointing to the character immediately following the octave info
            }
            linePtr++;
        }
        return octave;
    }
    
    /**
     * Gets the played note's numerator and denominator values, or sets them to the default
     * @requires getCharAtCurrentLineIndex() is the first character of a possible Numerator/Denominator
     * @return Integer[] of numerator and denominator
     */
    private Integer[] getNumeratorAndDenominator() {
        Integer[] numDenom;
        //set default numerators and denominators
        Integer numerator = 1;
        Integer denominator = 1;
        String num = "";
        String denom = "";
        if (lineHasNext()) {
            String c = getCharAtCurrentLineIndex();
            Matcher mLengthDigit = lengthDigitPattern.matcher(c);
            //////////////////////////////////////////////////////FINDING NUMERATOR//////////////////////////////////////////////
            while (mLengthDigit.matches()) {
                num += c;
                linePtr++;
                if (lineHasNext()) {
                    c = getCharAtCurrentLineIndex();
                    mLengthDigit = lengthDigitPattern.matcher(c);
                }
                else {//no more characters on the line!
                    if (!num.equals("")) {
                        numerator = Integer.valueOf(num);
                        numDenom = new Integer[] {numerator, denominator}; // {num,1}
                        return numDenom;
                    }
                }
            }
            //finished numerator (c points to after numerator); convert num, otherwise keep the default numerator = 1
            if (!num.equals("")) {
                numerator = Integer.valueOf(num);              
            }
            //////////////////////////////////////////////////////END FINDING NUMERATOR//////////////////////////////////////////////
            //////////////////////////////////////////////////////FINDING DENOMINATOR//////////////////////////////////////////////
            if (c.equals("/")) {
                //default denominator becomes 2
                denominator = 2;
                linePtr++;
                if (lineHasNext()) { //check if there is a denominator Integer
                    c = getCharAtCurrentLineIndex();
                    mLengthDigit = lengthDigitPattern.matcher(c);
                    //get the denominator if it exists
                    while (mLengthDigit.matches()) {
                        denom += c;
                        linePtr++;
                        if (lineHasNext()) {
                            c = getCharAtCurrentLineIndex();
                            mLengthDigit = lengthDigitPattern.matcher(c);
                        }
                        else {//no more characters on the line!
                            if (!denom.equals("")) {
                                denominator = Integer.valueOf(denom);
                            }
                            //using default denominator
                            numDenom = new Integer[] {numerator, denominator}; // {num,2}
                            return numDenom;
                        }
                    }
                    //current character is no longer a valid digit
                    //finished denominator (c points to after denominator); convert denom, otherwise keep the default denominator = 2
                    if (!denom.equals("")) {
                        denominator = Integer.valueOf(denom);              
                    }
                    //////////////////////////////////////////////////////END FINDING DENOMINATOR//////////////////////////////////////////////
                } 
                //Else: Nothing left on the line after / : NOTEnum/ -> denom = 2    //{num,2}
            }
            //Else: No denominator (no / ); NOTEnum -> denom = 1    //{num,1}           
        }
        //Else: No info on num or denom    //{1,1}
        numDenom = new Integer[] {numerator, denominator};
        return numDenom;
    }
        
    /**
     * Gets the accidental value for the 
     * @requires getCharAtCurrentLineIndex() currently is a first "^" or a "_" or a single "="
     * @return Integer accidental value for the playednote
     */
    private Integer getAccidentalFromString() {
        Integer accidental = 0;
        String c = getCharAtCurrentLineIndex();
        if (c.equals("=")) {
            linePtr++;
            //check we don't have a =^ or == or =_
            if (lineHasNext()) {
                c = getCharAtCurrentLineIndex();
                if (c.equals("^") || c.equals("=") || c.equals("_")) {
                    throw new IllegalArgumentException("Cannot have accidental follow a =");
                }
            }
        }
        else if (c.equals("^")) {
            accidental++;
            linePtr++;
            if (lineHasNext()) {
                c = getCharAtCurrentLineIndex();
                if (c.equals("^")) { //we have a ^^
                    accidental++;
                    linePtr++;
                    //check that it's not a ^^^
                    if (lineHasNext()) {
                        c = getCharAtCurrentLineIndex();
                        if (c.equals("^")) {
                            throw new IllegalArgumentException("Cannot have more than two ^ consecutively");
                        }
                    }
                }
                else if (c.equals("_")) {
                    throw new IllegalArgumentException("Cannot have ^ followed by _");
                }
            }
        }
        else if (c.equals("_")) {
            accidental--;
            linePtr++;
            if (lineHasNext()) {
                c = getCharAtCurrentLineIndex();
                if (c.equals("_")) { //we have a __ (double)
                    accidental--;
                    linePtr++;
                    //check that it's not a ___ (triple)
                    if (lineHasNext()) {
                        c = getCharAtCurrentLineIndex();
                        if (c.equals("_")) {
                            throw new IllegalArgumentException("Cannot have more than two _ consecutively");
                        }
                    }
                }
                else if (c.equals("^")) {
                    throw new IllegalArgumentException("Cannot have _ followed by ^");
                }
            }
        }
        else {
            throw new IllegalArgumentException("Expected an accidental");
        }
    //linePtr is pointing to the character immediately following the last accidental (which should be a baseNote)
    return accidental;
    }
    
    
    /**
     * Gets the next PlayedNoteToken on the line, that starts with an accidental
     * @requires getCharAtCurrentLineIndex() currently points to the accidental. Else, throw IllegalArgumentException
     * @return the next PlayedNoteToken
     */
    private PlayedNoteToken getPlayedNoteTokenFromAccidental() {
        Integer accidental = getAccidentalFromString();
        return getPlayedNoteTokenFromBaseNote(accidental, true);
    }
    
}
       