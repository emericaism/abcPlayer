package sound;

import java.util.ArrayList;
import sound.Parsere.ParserException;

public class Chord implements Played{
    
    private ArrayList<PlayedNote> elems;
    private int numerator;
    private int denominator;
    private double length;
    
    public Chord(){
        this.elems = new ArrayList<PlayedNote>();
    }
    
    
    public Chord(ArrayList<PlayedNote> elems){
        //this.numerator = numerator;
        //this.denominator = denominator;
        this.elems = elems;
    }
    
    /**
     * make sure notes in chord are all same length
     * else throws exception
     */
    public void checkLen() throws ParserException{
        if (!this.works()){//to be implemented by me
            throw new ParserException("Notes in chord not same lengths");
        }
    }
    public void setNumerator(int numerator){
        this.numerator=numerator;
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
    public double getLengthDoub(){
        double q = this.numerator/this.denominator;
        return q;
    }
    

    
    
    public void addPlayedNote(PlayedNote pla){
        this.elems.add(pla);
    }
    
    public ArrayList<PlayedNote> getPlayedNotes(){
        return this.elems;
    }
    
    public Boolean works(){
        if(this.elems.isEmpty()){
            return true;
        }
        else{
            double i = this.elems.get(0).getLengthDoub();
            for (PlayedNote y: this.elems){
                if(y.getLengthDoub() != i){
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (PlayedNote plan : elems){
            sb.append(plan.toString()+" ");
        }
        sb.append("]");
        return sb.toString();
    }
    public int accept(PlayedVisitor v){
        return v.visit(this);
    }

    
    public void addToPlayable() {
        //something like... with each note starting at the right start tick
        //for (SingleElement e:elements) {
        //    e.addToPlayable();
        //}
    }
    
    public <R>R acceptConverter(ConverterVisitor<R> v) {return v.play(this);}
}