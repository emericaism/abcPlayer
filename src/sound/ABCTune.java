package sound;

import java.util.ArrayList;

public class ABCTune {
    private ABCHeader myABCHeader;
    private String myABCMusic;
    private ABCMusic music;
    private double defaultLengthNumber;
    private double meter;
    private int tempo;
    private ArrayList<Integer> ds;
    private String key;
    private String title = "unknown";
    private String composer = "unknown";
    private String number = "unknown";
    private String defaultLengthString="1/8";
    private int DLnumerator;
    private int DLdenominator;
    private String meterString = "4/4";
    
    //public ABCTune(ABCHeader abcheader, String ABCMusic){
    public ABCTune(){
        //this.myABCHeader = abcheader;
        //this.myABCMusic = ABCMusic;
        this.defaultLengthNumber = 24;
        this.meter = 192;
        this.DLnumerator = 0;
        this.DLdenominator = 0;
        this.tempo = 100;
        this.key = "c";
        this.ds = new ArrayList<Integer>();
        this.music= new ABCMusic();
    }
    

    public ABCHeader getValsFromHeader(){
        return this.myABCHeader;
    }
    public void setMeterString(String s){
        this.meterString=s;
        
    }
    public String getMeterString(){
        return this.meterString;
        
    }
    public void setdefaultLengthString(String s){
        this.defaultLengthString = s;
    }
    
    public String getDefLenStr() {
        return this.defaultLengthString;
    }
    public void setDefaultLengthNumerator(int num){
        this.DLnumerator = num;
    }
    public void setDefaultLengthDenominator(int den){
        this.DLdenominator = den;
    }
    public int getDefaultLengthNumerator(){
        return this.DLnumerator;
    }
    public int getDefaultLengthDenominator(){
        return this.DLdenominator;
    }
    public void setNumber(String s){
        this.number = s;
    }
    
    public String getNumber() {
        return this.number;
    }

    public void setTitle(String s) {
        this.title = s;
    }

    public String getTitle() {
        return this.title;
    }
    
    public void setSeenDenoms(ArrayList<Integer> ds){
        this.ds = ds;
    }
    
    public ArrayList<Integer> getSeenDenoms(){
        return this.ds;
    }

    public void setComposer(String s) {
        this.composer = s;
    }

    public String getComposer() {
        return this.composer;
    }
  
    public double getDefaultLen(){
        return this.defaultLengthNumber;
    }

    public void setDefaultLen(double defaultLengthNumber){
        this.defaultLengthNumber = defaultLengthNumber;
    }
    
    public double getMeter(){
        return this.meter;
    }
  
    public void setMeter(double meter){
        this.meter = meter;
    }
   
    public String getKey(){
        return this.key;
    }
 
    public void setKey(String key){
        this.key = key;
    }
  
    public int getTempo(){
        return this.tempo;
    }
    
    public void setTempo(int tempo){
        this.tempo = tempo;
    }

    public ABCMusic getMusic(){
        return this.music;
    }
    
    public void setMusic(ABCMusic music){
        this.music = music;
    }
    
    
}
