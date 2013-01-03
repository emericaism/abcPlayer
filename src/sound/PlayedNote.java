package sound;

import sound.Pitch;

public class PlayedNote implements Played{
    
//    private final HashMap<String,Integer> baseNoteToValue;
    private Pitch pitch;
    private String baseNote;
    private Integer accidental;
    private Integer octave;
    private Integer numerator;
    private Integer denominator;
    
    public PlayedNote(){
        
    }
    public PlayedNote(String b, Integer numerator, Integer denominator,  Integer a, Integer o) {
        
//        baseNoteToValue = new HashMap<String,Integer>();      
//        baseNoteToValue.put("A", 9);
//        baseNoteToValue.put("B", 11);
//        baseNoteToValue.put("C", 0);
//        baseNoteToValue.put("D", 2);
//        baseNoteToValue.put("E", 4);
//        baseNoteToValue.put("F", 5);
//        baseNoteToValue.put("G", 5);
        
        this.baseNote = b;
        this.numerator = numerator;
        this.denominator = denominator;
        this.accidental = a;
        this.octave = o;
    }
    
    public Pitch getPitch(){
        return this.pitch;
        
    }
    public void setNumerator(int numerator){
        this.numerator = numerator;
    }
    public void setDenominator(int denominator){
        this.denominator = denominator;
    }
    public int getNumerator(){
        return this.numerator;
        
    }
    public int getDenominator(){
        return this.denominator;
    }
    
    public int getOctave(){
        return this.octave;
        
    }
    public int getAccidental(){
        return this.accidental;
    }
    public String getBaseNote(){
        return this.baseNote;
    }

    
    public void setPitch(Pitch pitch){
        this.pitch = pitch;
    }
    public void setOctave(int oct){
        this.octave = oct;
    }
    public void transposeOct(int k){
        this.octave += k;
    }
    public void setAccidental(int accidental){
        this.accidental = accidental;
    }
    public void incrementAccidental(int k){
        this.accidental += k;
    }
    public void setBaseNote(String base){
        this.baseNote = base;
        
    }
    public double getLengthDoub(){
        double q = (double) this.numerator/ (double) this.denominator;
        return q;
    }
    
    @Override
    public String toString(){
        return "<" + accidental + ">" + baseNote + "(" + octave + ")" + numerator.toString() + denominator.toString();
       
    }
    public int accept(PlayedVisitor v){
        return v.visit(this);
    }
    public void addToPlayable() {
        Pitch p = new Pitch(baseNote.charAt(0)); //check for uppercase/lowercase cuz that also determines octave
        p.accidentalTranspose(accidental).octaveTranspose(octave);
        //TODO:Implement something that adds a pitch to the playable for the right length
    }
    
    public <R>R acceptConverter(ConverterVisitor<R> v) {return v.play(this);}
}
