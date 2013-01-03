package sound;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import sound.Parsere.ParserException;
import sound.ABCTune;

public class ParserTest {
    /**
     * 
     * @param s String... specifying all strings to be written to temporary file
     * Line break appended at the end of each string for formatting
     * @return temporary file for testing
     * @throws IOException
     */
    private File createFile(String... s) throws IOException {
        File t = File.createTempFile("randomLexerTest", "abc");
        t.deleteOnExit();
        BufferedWriter tWriter = new BufferedWriter(new FileWriter(t));
        for (int i = 0; i < s.length; ++i) {
            tWriter.write(s[i] + "\n");
        }
        tWriter.close();
        return t;
    }
    private String justVoice(String s){
        int i = s.indexOf("voices:");
        return s.substring(8+i);
    }
    //private String str
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled(){
        assert false;
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testOnFineInput() throws IOException, ParserException {
        Parsere parsere = new Parsere(new Lexer(createFile("X:1", "T:City Lights", "Q:140", 
                "C:Nujabes", "L:1/8", "M:C", "K:Eb").getPath()));
        ABCTune dLep = parsere.getTune();
        assertEquals("1", dLep.getNumber());
        assertEquals("City Lights", dLep.getTitle());
        assertEquals("Nujabes", dLep.getComposer());
        assertEquals(140, dLep.getTempo());
        assertEquals("C", dLep.getMeterString());
        assertEquals(1, dLep.getDefaultLengthNumerator());
        assertEquals(8, dLep.getDefaultLengthDenominator());
        assertEquals("Eb", dLep.getKey());
    }
    
    @Test(expected = ParserException.class)
    public void missingIndexNum() throws IOException, ParserException {
        Parsere parsere = new Parsere(new Lexer(createFile("Q:100", "T:Florida Room",
                "C:Donald Fagen","L:1/4","M:C","K:D").getPath()));
        ABCTune derp = parsere.getTune();
    }
    
    @Test(expected = ParserException.class)
    public void titleNotSecond() throws IOException, ParserException {
        Parsere parsere = new Parsere(new Lexer(createFile("X:2","Q:95",
                "T:Derpsters Adventure","C:Elvis Presley","L:1/4","M:C","K:C").getPath()));
        ABCTune derp = parsere.getTune();
    }
    
    @Test(expected = ParserException.class)
    public void messedUpTups() throws IOException, ParserException {
        Parsere parsere = new Parsere(new Lexer(createFile("X:1", "T:Hello World Song", "C:Beathoven",
                "L:1/4", "K:C",
                "G _e (3F2g/g/").getPath()));
        ABCTune derp = parsere.getTune();
    }
    
    @Test
    public void singleRepeat() throws IOException, ParserException {
        Parsere parsere = new Parsere(new Lexer(createFile("X:1", "T:Hypnotoad's Anthem", "L:1/4", "K: C",
                "d d b g :|").getPath()));
        ABCTune derp = parsere.getTune();
        assertEquals("{1=Voice: null\n<0>d(0)48 <0>d(0)48 <0>b(0)48 <0>g(0)48  <0>d(0)48 <0>d(0)48 <0>b(0)48 <0>g(0)48  \n}", justVoice(derp.toString()));
        
    }
    
    @Test
    public void testPieceOne() throws IOException, ParserException{
        Lexer lexer = new Lexer("sample_abc//piece1.abc");
        Parsere parser = new Parsere(lexer);
        
    }
    
    @Test(expected = ParserException.class)
    public void measuresIncorrect() throws IOException, ParserException {
        Parsere parsere = new Parsere(new Lexer(createFile("X:1", "T:" +
        		"Ran Out of Time", "L:1/4", "K:C", "g g | g g g | g g ").getPath()
        		));
        ABCTune derp = parsere.getTune();
    }
    
    
    
    
    
    
    

}