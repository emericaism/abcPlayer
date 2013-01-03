package sound;

import java.util.ArrayList;

public class Voice{
    
    private String name;
    private ArrayList<PlayedGroup> playedGroups;

    public Voice(){
        this.playedGroups = new ArrayList<PlayedGroup>();
    }
    

    public Voice(String name){
        this.name = name;
        this.playedGroups = new ArrayList<PlayedGroup>();
    }
    

    public Voice(ArrayList<PlayedGroup> playedGroups){
        this.playedGroups = playedGroups;
    }

    public void addPlayedGroup(PlayedGroup p){
        this.playedGroups.add(p);
    }
    

    public ArrayList<PlayedGroup> getPlayedGroups(){
        return this.playedGroups;
    }
    

    public String getName(){
        return name;
    }
    
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Voice: " + name + "\n");
        for (PlayedGroup pg : playedGroups) {
            stringBuilder.append(pg.toString());
            stringBuilder.append(" ");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}