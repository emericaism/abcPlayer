package sound;

import java.util.HashMap;

public class ABCMusic{
    
    private HashMap<String, Voice> voiceHash;
    

    public ABCMusic(){
        this.voiceHash = new HashMap<String, Voice>();
    }

    public ABCMusic(HashMap<String, Voice> voiceHash){
        this.voiceHash = voiceHash;
    }
    
    /**
     * 
     * @return: voiceHash
     */
    public HashMap<String, Voice> getVoiceHash(){
        return this.voiceHash;
    }
    
    /**
     * 
     * @param key
     * @return: the value
     */
    public Voice get(String key) {
        if (!voiceHash.containsKey(key)) {
            voiceHash.put(key, new Voice());
        }
        return voiceHash.get(key);
    }
    
    /**
     * 
     * @param s: String s
     * @param v: Voice to be added to voiceHash
     */
    public void add(String s, Voice v){
        this.voiceHash.put(s, v);
    }
    
    /**
     * @return: ABC as string, which is really a list of voices.
     */
    public String toString() {
        return "voices: " + voiceHash;
    }
 }