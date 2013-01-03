package sound;

import sound.*;

import java.util.ArrayList;
import java.util.HashMap;


public class Parsere {
    static final int TICKS_PER_QUARTER = 48;

    @SuppressWarnings("serial")
    public static class ParserException extends RuntimeException {
        public ParserException(String msg) {
            super(msg);
        }
    }

    private final Lexer lexer;
    private final ABCMusic music;
    private final ABCTune tune;
    public ArrayList<Integer> seenDenoms;
    public HashMap<String,Integer> defaultAccidentals = new HashMap<String, Integer>();
    public HashMap<String,Integer> measureAccidentals = new HashMap<String, Integer>();


    private HashMap<String, Repeat> repeats;
    private int tempo;

    public Parsere(Lexer lexer) throws ParserException {
        this.lexer = lexer;
        this.music = new ABCMusic();
        this.tune = new ABCTune();
        this.repeats = new HashMap<String, Repeat>();
        ArrayList<Integer> seenDenoms = new ArrayList<Integer>();
        ArrayList<FieldToken> fieldToks = lexer.getHeaderTokens();
        ArrayList<Token> musicToks = lexer.getMusicTokens();

        
        ///////ACTUAL CODE//////////
        parseFields(fieldToks);
        setKeySignature();
        measureAccidentals = defaultAccidentals;
        parseMusic(musicToks);

        this.tune.setMusic(music);
        ////////////////////////
    }




    private int[] slashParse(String string) {
        int[] intList = new int[2];
        int slashIndex = string.indexOf("/");
        int spaceIndex = string.indexOf(" ");
        int i;
        if (spaceIndex == -1) {
            i = Integer.parseInt(string.substring(0, slashIndex));
        } else {
            i = Integer.parseInt(string.substring(spaceIndex + 1, slashIndex));

        }
        intList[0] = i;
        intList[1] = Integer.parseInt(string.substring(slashIndex + 1,
                string.length()));
        return intList;
    }



    public ABCTune getTune(){
        return this.tune;
    }

    private void parseFields(ArrayList<FieldToken> fieldToks) throws ParserException{
        int i = 0;
        for (FieldToken f : fieldToks){
            switch(f.getFieldType()){
            case FIELDMETER:
                if (i==0){
                    throw new ParserException("expected index number");
                }
                if (i==1){
                    throw new ParserException("expected Title");
                }
                int numerator = 1;
                int denominator = 1;
                String meterString = f.getStringValue();
                this.tune.setMeterString(meterString);
                if (meterString.equals("C")){
                    numerator = 4;
                    denominator = 4;
                }
                else{
                    int[] mtr = slashParse(meterString);
                    numerator = mtr[0];
                    denominator = mtr[1];
                }
                this.tune.setMeterString(f.getStringValue());
                i++;
                break;
            case FIELDTEMPO:
                if (i==0){
                    throw new ParserException("expected index number");
                }
                if (i==1){
                    throw new ParserException("expected Title");
                }
                String tString = f.getStringValue();
                this.tune.setTempo(Integer.parseInt(tString));
                i++;
                break;
            case FIELDKEY:
                if (i==0){
                    throw new ParserException("expected index number");
                }
                if (i!=fieldToks.size()-1){
                    throw new ParserException("Key not at end of header");
                }
                String kString = f.getStringValue();
                kString.toLowerCase();
                this.tune.setKey(kString);
                i++;
                break;
            case FIELDDEFAULTLENGTH:
                if (i==0){
                    throw new ParserException("expected index number");
                }
                if (i==1){
                    throw new ParserException("expected Title");
                }
                String lString = f.getStringValue();       
                //thi
                int[] ND = slashParse(lString);
                int lenNum = ND[0];
                int lenDen = ND[1];
                double q1 = lenNum/lenDen;
                this.tune.setDefaultLengthNumerator(lenNum);
                this.tune.setDefaultLengthDenominator(lenDen);
                i++;
                break;
            case FIELDVOICE:
                if (i==0){
                    throw new ParserException("expected index number");
                }
                if (i==1){
                    throw new ParserException("expected Title");
                }
                String vString = f.getStringValue();
                Voice vocal = new Voice();
                Repeat rep = new Repeat();
                this.music.add(vString, vocal);
                repeats.put(vString,rep);
                i++;
                break;   
            case FIELDTITLE:
                if (i==0){
                    throw new ParserException("expected index number");
                }
                if (i!=1)
                {
                    throw new ParserException("Title must be the second header field");
                }
                String title = f.getStringValue();
                this.tune.setTitle(title);
                i++;
                break;
            case FIELDCOMPOSER:
                if (i==0){
                    throw new ParserException("expected index number");
                }
                if (i==1){
                    throw new ParserException("expected Title");
                }
                String cString = f.getStringValue();
                this.tune.setComposer(cString);
                i++;
                break;
            case FIELDINDEXNUMBER:
                if (i!=0)
                {
                    throw new ParserException("Number must be the first header field");
                }
                String nString = f.getStringValue();
                this.tune.setNumber(nString);
                i++;
                break;    
            }
        }
    }

    private void parseMusic(ArrayList<Token> musicToks) throws ParserException{
        String previouslySeen = "";
        String currentlyPlaying = "";
        String currentRepeatIndex12 = "";
        String currVoice = "1";

        PlayedNote note = new PlayedNote();
        Rest rest = new Rest();
        Tuplet tuplet = new Tuplet();
        Chord chord = new Chord();
        MeasureElement mselement = new MeasureElement();
        Repeat repeat = new Repeat();
        int tupSize = 0;
        ArrayList <Played> elementz = new ArrayList<Played>();
        int tupInd = 0;
        Voice voice = new Voice();
        ArrayList <Integer> seenDenoms = new ArrayList<Integer>();


        HashMap<String, Voice> voices = music.getVoiceHash();

        if (voices.isEmpty()){
            this.music.add(currVoice, voice);
        }
        if (repeats.isEmpty()){
            repeats.put(currVoice, repeat);
        }
        
        for(Token tok : musicToks){
            

            switch(tok.getType()){
            case PLAYEDNOTETOKEN:

                PlayedNoteToken ptok = (PlayedNoteToken) tok;
                int accidental = 0;
                int octave = ptok.getOctave();
                if(ptok.hasAccidental()){
                    accidental = ptok.getAccidental();
                }
                else{
                    accidental = 0;
                }
                String letter = ptok.getBaseNote();
                if(!currentlyPlaying.equals("chord") && !currentlyPlaying.equals("tuplet")){
                    currentlyPlaying = "regNote";
                }




                if(!ptok.hasAccidental()) { //no accidental, jsut get measure accidental
                    accidental = measureAccidentals.get(letter);
                }
                else { //has accidental, replace measure accidental
                    measureAccidentals.put(letter, accidental);
                }

                if(ptok.hasAccidental()){
                    previouslySeen = "note+accidental";
                }
                else{
                    previouslySeen = "regNote";
                }

                note.setNumerator(ptok.getNumerator());
                note.setDenominator(ptok.getDenominator());
                note.setAccidental(accidental);
                note.setBaseNote(letter);
                note.setOctave(octave);

                if(currentlyPlaying.equals("regNote")){
                    mselement.addPlayed(note);
                }
                else if (currentlyPlaying.equals("chord")){
                    chord.addPlayedNote(note);
                    chord.works();
                }
                
                if (currentlyPlaying.equals("tuplet")){
                    seenDenoms.add(note.getDenominator()*tupSize);
                }
                else{
                    seenDenoms.add(note.getDenominator());
                }
                if (currentlyPlaying.equals("tuplet")){
                    tuplet.addPlayed(note);
                    tupInd ++;
                    if (tupInd  == tupSize){
                        mselement.addPlayed(tuplet);
                        tuplet = new Tuplet();
                        currentlyPlaying = "";
                    }
                    
                }
                
                note = new PlayedNote();
                break;

            case FIELDTOKEN:
                throw new ParserException("This should not happen if Lexer lexed accurately.");
            case RESTTOKEN:
                RestToken ptok1 = (RestToken) tok;
                if (!currentlyPlaying.equals("tuplet")){
                    currentlyPlaying = "rest";
                    previouslySeen = "rest";
                }
                else if(currentlyPlaying.equals("chord")){
                    throw new ParserException("Chord has rest in it");
                }
                
                rest.setNumerator(ptok1.getNumerator());
                rest.setDenominator(ptok1.getDenominator());
                if (currentlyPlaying.equals("tuplet")){
                    tuplet.addPlayed(note);
                    tupInd ++;
                    if (tupInd  == tupSize){
                        mselement.addPlayed(tuplet);
                        tuplet = new Tuplet();
                        currentlyPlaying = "";
                    }
                    
                }
                else{
                    mselement.addPlayed(rest);
                }
                
                rest = new Rest();
                break;

            case CHANGEOFVOICETOKEN:
                ChangeOfVoiceToken vtok = (ChangeOfVoiceToken) tok;
                String vString = vtok.getStringValue();
                currVoice = vString;
                break;
            case END:
                break;
            case TUPLETTOKEN:
                TupletToken ttok = (TupletToken) tok;
                switch(ttok.getTupletType()){
                case DUPLET:
                    tupInd = 0;
                    currentlyPlaying = "tuplet";
                    tupSize = 2;
                    elementz.clear();
                    tuplet = new Tuplet(2, elementz);
                    break;
                case TRIPLET:
                    tupInd = 0;
                    currentlyPlaying = "tuplet";
                    tupSize = 3;
                    elementz.clear();
                    tuplet = new Tuplet(3, elementz);
                    break;
                case QUADRUPLET:
                    tupInd = 0;
                    currentlyPlaying = "tuplet";
                    tupSize = 4;
                    elementz.clear();
                    tuplet = new Tuplet(4, elementz);
                    break;
                }
                break;

            case STRUCTURALTOKEN:
                StructuralToken stok = (StructuralToken)tok;
                switch(stok.getStructuralType()){

                case ENDOFSECTION:
                    if(currentRepeatIndex12.equals("2")){
                        voices.get(currVoice).addPlayedGroup(repeats.get(currVoice));
                        voices.get(currVoice).addPlayedGroup(mselement);
                        repeats.get(currVoice).erase();
                        currentRepeatIndex12 = "";
                    }
                    else if(currentRepeatIndex12.equals("")){
                        voices.get(currVoice).addPlayedGroup(mselement);
                        repeats.get(currVoice).addPlayedGroup(mselement);
                    }
                    mselement = new MeasureElement();
                    currentlyPlaying = "";
                    measureAccidentals = defaultAccidentals;
                    break;
                case BEGINREPEAT:
                    mselement = new MeasureElement();
                    repeats.get(currVoice).erase();
                    measureAccidentals = defaultAccidentals;
                    currentRepeatIndex12 = "";
                    break;

                case ENDREPEAT:
                    if(currentRepeatIndex12.equals("1")){
                        voices.get(currVoice).addPlayedGroup(mselement);
                    }
                    else if (currentRepeatIndex12.equals("")){
                        Repeat repe = repeats.get(currVoice);
                        repe.addPlayedGroup(mselement);
                        voices.get(currVoice).addPlayedGroup(repe);
                    }
                    mselement = new MeasureElement();
                    currentlyPlaying = "";
                    measureAccidentals = defaultAccidentals;
                    break;
                case FIRSTREPEAT:
                    currentRepeatIndex12 = "1";
                    break;
                case SECONDREPEAT:
                    currentRepeatIndex12 = "2";
                    break;
                case BARLINE:
                    if(currentRepeatIndex12.equals("2")){
                        voices.get(currVoice).addPlayedGroup(repeats.get(currVoice));
                        voices.get(currVoice).addPlayedGroup(mselement);
                        repeats.get(currVoice).erase();
                        currentRepeatIndex12 = "";
                    }
                    else if(currentRepeatIndex12.equals("")){
                        voices.get(currVoice).addPlayedGroup(mselement);
                        repeats.get(currVoice).addPlayedGroup(mselement);
                    }
                    mselement = new MeasureElement();
                    currentlyPlaying = "";
                    measureAccidentals = defaultAccidentals;
                    break;
                case SPACE:
                    break;
                case BEGINCHORD:
                    if (currentlyPlaying.equals("chord")){
                        throw new ParserException("no chords within chords");
                    }
                    currentlyPlaying = "chord";
                    chord = new Chord();
                    break;
                case END:
                    throw new ParserException("This should not happen if Lexer lexed accurately.");

                case ENDCHORD: 
                    mselement.addPlayed(chord);
                    chord = new Chord();
                    currentlyPlaying = "";
                    break;
                }
                break;
            }
        }
        this.tune.setSeenDenoms(seenDenoms);
    }
    ////////////////////////////////////////KEY SIGNATURE//////////////////////////////////////////   
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void setKeySignature() throws ParserException{
        String key = this.tune.getKey();
        defaultAccidentals.put("A", 0);
        defaultAccidentals.put("B", 0);
        defaultAccidentals.put("C", 0);
        defaultAccidentals.put("D", 0);
        defaultAccidentals.put("E", 0);
        defaultAccidentals.put("F", 0); 
        defaultAccidentals.put("G", 0);

        //Leave as default accidentals for C and Am
        if (key.equalsIgnoreCase("C") || key.equalsIgnoreCase("Am")){}

        //Key signatures with flats
        else if (key.equalsIgnoreCase("F") || key.equalsIgnoreCase("Dm")){
            defaultAccidentals.put("B", -1);
        }
        else if (key.equalsIgnoreCase("Bb") || key.equalsIgnoreCase("Gm")){
            defaultAccidentals.put("B", -1);
            defaultAccidentals.put("E", -1);
        }
        else if (key.equalsIgnoreCase("Eb") || key.equalsIgnoreCase("Cm")){
            defaultAccidentals.put("B", -1);
            defaultAccidentals.put("E", -1);
            defaultAccidentals.put("A", -1);
            //em.out.println(defaultAccidentals.get("A"));
        }
        else if (key.equalsIgnoreCase("Ab") || key.equalsIgnoreCase("Fm")){
            defaultAccidentals.put("B", -1);
            defaultAccidentals.put("E", -1);
            defaultAccidentals.put("A", -1);
            defaultAccidentals.put("D", -1);
        }
        else if (key.equalsIgnoreCase("Db") || key.equalsIgnoreCase("Bbm")){
            defaultAccidentals.put("B", -1);
            defaultAccidentals.put("E", -1);
            defaultAccidentals.put("A", -1);
            defaultAccidentals.put("D", -1);
            defaultAccidentals.put("G", -1);
        }
        else if (key.equalsIgnoreCase("Gb") || key.equalsIgnoreCase("Ebm")){
            defaultAccidentals.put("B", -1);
            defaultAccidentals.put("E", -1);
            defaultAccidentals.put("A", -1);
            defaultAccidentals.put("D", -1);
            defaultAccidentals.put("G", -1);
            defaultAccidentals.put("C", -1);
        }
        else if (key.equalsIgnoreCase("Gb") || key.equalsIgnoreCase("Abm")){
            defaultAccidentals.put("B", -1);
            defaultAccidentals.put("E", -1);
            defaultAccidentals.put("A", -1);
            defaultAccidentals.put("D", -1);
            defaultAccidentals.put("G", -1);
            defaultAccidentals.put("C", -1);
            defaultAccidentals.put("F", -1);           
        }
        //Key signatures with sharps
        else if (key.equalsIgnoreCase("G") || key.equalsIgnoreCase("Em")){
            defaultAccidentals.put("F", 1);
        }
        else if (key.equalsIgnoreCase("D") || key.equalsIgnoreCase("Bm")){
            defaultAccidentals.put("F", 1);
            defaultAccidentals.put("C", 1);
        }
        else if (key.equalsIgnoreCase("A") || key.equalsIgnoreCase("F#m")){
            defaultAccidentals.put("F", 1);
            defaultAccidentals.put("C", 1);
            defaultAccidentals.put("G", 1);
        }
        else if (key.equalsIgnoreCase("E") || key.equalsIgnoreCase("C#m")){
            defaultAccidentals.put("F", 1);
            defaultAccidentals.put("C", 1);
            defaultAccidentals.put("G", 1);
            defaultAccidentals.put("D", 1);
        }
        else if (key.equalsIgnoreCase("B") || key.equalsIgnoreCase("G#m")){
            defaultAccidentals.put("F", 1);
            defaultAccidentals.put("C", 1);
            defaultAccidentals.put("G", 1);
            defaultAccidentals.put("D", 1);
            defaultAccidentals.put("A", 1);
        }
        else if (key.equalsIgnoreCase("F#") || key.equalsIgnoreCase("D#m")){
            defaultAccidentals.put("F", 1);
            defaultAccidentals.put("C", 1);
            defaultAccidentals.put("G", 1);
            defaultAccidentals.put("D", 1);
            defaultAccidentals.put("A", 1);
            defaultAccidentals.put("E", 1);
        }
        else if (key.equalsIgnoreCase("C#") || key.equalsIgnoreCase("A#m")){
            defaultAccidentals.put("F", 1);
            defaultAccidentals.put("C", 1);
            defaultAccidentals.put("G", 1);
            defaultAccidentals.put("D", 1);
            defaultAccidentals.put("A", 1);
            defaultAccidentals.put("E", 1);
            defaultAccidentals.put("B", 1);
        }
        else{ //Invalid key signature
            throw new IllegalArgumentException("Invalid Key Signature");
        }
    }

}
