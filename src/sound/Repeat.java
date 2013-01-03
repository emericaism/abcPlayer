package sound;

import java.util.ArrayList;

public class Repeat extends PlayedGroup{
    private ArrayList<PlayedGroup> playeds;
    
    public Repeat(){
        this.playeds = new ArrayList<PlayedGroup>();
        
    }
    
    public Repeat(ArrayList<PlayedGroup> playeds){
        this.playeds = playeds;
    }
    
    public ArrayList<PlayedGroup> getPlayeds(){
        return this.playeds;
    }
    
    public ArrayList<PlayedGroup> getPlayedGroups(){
        return new ArrayList<PlayedGroup>();
    }
    
    public void addPlayedGroup(PlayedGroup yy){
        this.playeds.add(yy);
    }
    
    @Override
    public int accept(PlayedVisitor v){
        return v.visit(this);
    }
    
    public void addPlayed(PlayedGroup pl){
        this.playeds.add(pl);
    }
    public void erase(){
        this.playeds.clear();
    }
    
    /**
     * toString of Repeat
     */
    @Override
    public String toString(){
        String y = "repeat";
        return y;
    }
    
    
    
}
