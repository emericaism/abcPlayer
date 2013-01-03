package sound;


public interface Played{
    
    
    /**
     * Accepts a PlayableVisitor and allows the visitor to define the tyep of
     * this Element for visitor's purpose
     * 
     */    
    public abstract int accept(PlayedVisitor v);
    
    public abstract void setNumerator(int numerator);
    
    public abstract void setDenominator(int denominator);
    
    public abstract int getNumerator();
    
    public abstract int getDenominator();
    
    public abstract <R>R acceptConverter(ConverterVisitor<R> v);
    
    public interface ConverterVisitor<R>{
        public R play(Rest rest);
        public R play(PlayedNote note);
        public R play(Chord c);
        public R play(Tuplet tuplet);
    }
}
    

  
    
    