package sound;

import java.util.ArrayList;


public class Tuplet implements Played{
    
    private int tupletType;
    private double length;
    private int numerator;
    private int denominator;
    private ArrayList<Played> elems;
    
    public Tuplet(){
        
    }
    
    public Tuplet(int tupletType){
        this.tupletType = tupletType;
    }
    
    
    public Tuplet(int tupletType, ArrayList<Played> elems) {
        this.tupletType = tupletType;
        this.elems = elems;        
                
    }
    
    /**
     * get ticklength!
     */
    public double getLength(){
        return this.length;
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
    
    public void setLength(double length){
        this.length = length;
    }
    public void addPlayed(Played pl){
        this.elems.add(pl);
    }
    
    public int getNumObjectsInTuplet(){
        return this.tupletType;
    }
    
    public ArrayList<Played> getPlayeds(){
        return this.elems;
    }
    
    public void setType(int type){
        this.tupletType = type;
    }
    
    /**
     * toString for tuplets
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("(" + tupletType);
        for (Played pl : elems){
            sb.append(pl.toString());
        }
        return sb.toString();
    }
    public int accept(PlayedVisitor v){
        return v.visit(this);
    }
    public void addToPlayable() {
        //something like... with incrementing ticks calculated somewhere
        for (Played e:elems) {
            System.out.println("a");
            //e.addPlayable();
        }
    }
    
    public <R>R acceptConverter(ConverterVisitor<R> v) {return v.play(this);}
}
