package sound;

import java.util.ArrayList;

public class MeasureElement extends PlayedGroup{
    private ArrayList<Played> playeds;
    
    public MeasureElement(){
        this.playeds = new ArrayList<Played>();
        
    }
    
    public MeasureElement(ArrayList<Played> playeds){
        this.playeds = playeds;
    }
    
    public ArrayList<Played> getPlayeds(){
        return this.playeds;
    }
    
    public ArrayList<PlayedGroup> getPlayedGroups(){
        return new ArrayList<PlayedGroup>();
    }
    
    @Override
    public int accept(PlayedVisitor v){
        return v.visit(this);
    }
    
    public void addPlayed(Played pl){
        this.playeds.add(pl);
    }
    
    /**
     * toString of MeasureElemeent
     */
    @Override
    public String toString(){
        String y = "";
        for (Played x : playeds){
            y = y + x + " ";
        }
        return y;
    }
    
}
