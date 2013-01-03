package sound;




import sound.Chord;
import sound.PlayedNote;
import sound.Rest;
import sound.Tuplet;
import sound.MeasureElement;


public interface PlayedVisitor {
        public int visit(MeasureElement measure);
        public int visit(PlayedNote note);
        public int visit(Chord chord);
        public int visit(Rest rest);
        public int visit(Tuplet tuplet);
        public int visit(Repeat repeat);
        
    }


