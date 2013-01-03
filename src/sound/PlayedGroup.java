package sound;
import java.util.ArrayList;







public abstract class PlayedGroup{


    public abstract int accept(PlayedVisitor v);
    
    public abstract ArrayList<PlayedGroup> getPlayedGroups();

    

}