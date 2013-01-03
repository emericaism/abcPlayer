package sound;



public class Rest implements Played{
    public double length;
    public int numerator;
    public int denominator;
    
    public Rest(){
        
    }
 
    public Rest(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }
    
    public double getLength(){
        return this.length;
    }
    public void setLength(double length){
        this.length = length;
        
    }
    public void setNumerator(int numerator){
        this.numerator = numerator;
    }
    public void setDenominator(int denominator){
        this.denominator = denominator;
    }
    
    public int getDenominator(){
        return this.denominator;
    }
    public int getNumerator(){
        return this.numerator;
    }
    public double getLengthDoub(){
        double q = (double) this.numerator / (double) this.denominator;
        return q;
    }
    /**
     * toString of Rest (note)
     */
    @Override
    public String toString(){
        return "z" + this.getLength();
    }
    
    public int accept(PlayedVisitor v){
        return v.visit(this);
    }
    
    public void addToPlayable() {
        //TODO:Implement something that increments ticks on the playable
    }
    
    public <R>R acceptConverter(ConverterVisitor<R> v) {return v.play(this);}
}
