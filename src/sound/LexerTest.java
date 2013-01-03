package sound;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import sound.FieldToken.FieldType;
import sound.StructuralToken.StructType;

public class LexerTest {

    @Test
    public void getHeaderAndMusicLinesLexerTest() {
        Lexer l = new Lexer("sample_abc\\paddy.abc");
        ArrayList<String> expectedHeaderLines = new ArrayList<String>();
        expectedHeaderLines.add("X:1");
        expectedHeaderLines.add("T:Paddy O'Rafferty");
        expectedHeaderLines.add("C:Trad.");
        expectedHeaderLines.add("M:6/8");
        expectedHeaderLines.add("Q:200");
        expectedHeaderLines.add("K:D");
        
        ArrayList<String> expectedMusicLines = new ArrayList<String>();
        expectedMusicLines.add("dff cee|def gfe|dff cee|dfe dBA|");
        expectedMusicLines.add("dff cee|def gfe|faf gfe|[1 dfe dBA:|[2 dfe dcB|]");
        expectedMusicLines.add("A3 B3|gfe fdB|AFA B2c|dfe dcB|");
        expectedMusicLines.add("A3 B3|efe efg|faf gfe|[1 dfe dcB:|[2 dfe dBA|]");
        expectedMusicLines.add("fAA eAA| def gfe|fAA eAA|dfe dBA|");
        expectedMusicLines.add("fAA eAA| def gfe|faf gfe|dfe dBA:|");
//        System.out.println(l.headerLines);
//        System.out.println(l.musicLines);
        assertEquals(expectedHeaderLines,l.headerLines);
        assertEquals(expectedMusicLines,l.musicLines);
    }
    
    @Test
    public void getHeaderAndMusicLinesWithEmptyLinesLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_test_music.abc");
        ArrayList<String> expectedHeaderLines = new ArrayList<String>();
        expectedHeaderLines.add("X:1");
        expectedHeaderLines.add("T:Paddy O'Rafferty");
        expectedHeaderLines.add("C:Trad.");
        expectedHeaderLines.add("");
        expectedHeaderLines.add("M:6/8 %hello");
        expectedHeaderLines.add("Q:200");
        expectedHeaderLines.add("");
        expectedHeaderLines.add("K:D");
        
        ArrayList<String> expectedMusicLines = new ArrayList<String>();
        expectedMusicLines.add("dff cee|def gfe|dff cee|dfe dBA|");
        expectedMusicLines.add("dff cee|def gfe|faf gfe|[1 dfe dBA:|[2 dfe dcB|]");
        expectedMusicLines.add("A3 B3|gfe fdB|AFA B2c|dfe dcB|");
        expectedMusicLines.add("A3 B3|efe efg|faf gfe|[1 dfe dcB:|[2 dfe dBA|]");
        expectedMusicLines.add("fAA eAA| def gfe|fAA eAA|dfe dBA|");
        expectedMusicLines.add("");
        expectedMusicLines.add("fAA eAA| def gfe|faf gfe|dfe dBA:|");
        assertEquals(expectedHeaderLines,l.headerLines);
        assertEquals(expectedMusicLines,l.musicLines);
    }
    
    @Test
    public void getHeaderTokensLexerTest() {
        Lexer l = new Lexer("sample_abc\\paddy.abc");
        ArrayList<FieldToken> expectedHeaderTokens = new ArrayList<FieldToken>();
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDINDEXNUMBER, "1"));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDTITLE, "Paddy O'Rafferty"));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDCOMPOSER, "Trad."));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDMETER, "6/8"));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDTEMPO, "200"));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDKEY, "D"));

        ArrayList<FieldToken> headerTokens = l.getHeaderTokens();
        assertEquals(expectedHeaderTokens,headerTokens);
    }
    
    @Test
    public void getHeaderTokensWithEmptyLinesLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_test_music.abc");
        ArrayList<FieldToken> expectedHeaderTokens = new ArrayList<FieldToken>();
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDINDEXNUMBER, "1"));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDTITLE, "Paddy O'Rafferty"));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDCOMPOSER, "Trad."));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDMETER, "6/8"));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDTEMPO, "200"));
        expectedHeaderTokens.add(new FieldToken(FieldType.FIELDKEY, "D"));

        ArrayList<FieldToken> headerTokens = l.getHeaderTokens();
        assertEquals(expectedHeaderTokens,headerTokens);
    }
    
    @Test
    public void getSimpleMusicTokensLexerTest() {
        Lexer l = new Lexer("sample_abc\\scale.abc");
        //C D E F | G A B c | c B A G F E D C |
        ArrayList<Token> expectedMusicTokens = new ArrayList<Token>();
        //(Integer n, Integer d, String b, Integer a, Boolean hasA, Integer o)
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        
        ArrayList<Token> musicTokens = l.getMusicTokens();
        assertEquals(expectedMusicTokens,musicTokens);
    }
    
    @Test
    public void getMusicTokensMultipleLinesLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_music_tokenizer_test.abc");
        ArrayList<Token> expectedMusicTokens = new ArrayList<Token>();
        //(Integer n, Integer d, String b, Integer a, Boolean hasA, Integer o)
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 1));
        //new line
        //first space of line gets trimmed
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        //last space of line gets trimmed
        //new line
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        ArrayList<Token> musicTokens = l.getMusicTokens();
        assertEquals(expectedMusicTokens,musicTokens);
    }
    @Test
    public void getMusicTokensDifferentLengthsLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_lengths_test.abc");
        //C23/2 D2 E F/4 | G4 A/ B c | c2 B5/6 A G F E D C |
        ArrayList<Token> expectedMusicTokens = new ArrayList<Token>();
        expectedMusicTokens.add(new PlayedNoteToken(23, 2, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 4, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(4, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 2, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "C", 0, false, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(5, 6, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
      
        
        ArrayList<Token> musicTokens = l.getMusicTokens();

        assertEquals(expectedMusicTokens,musicTokens);
    }
    
    @Test
    public void getMusicTokensMultipleVoicesLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_many_voices_and_lines_test.abc");
        ArrayList<Token> expectedMusicTokens = new ArrayList<Token>();
        expectedMusicTokens.add(new ChangeOfVoiceToken("1"));
        //new line
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 1));
        //new line
        expectedMusicTokens.add(new ChangeOfVoiceToken("23hello"));
        //new line
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        //new line
        expectedMusicTokens.add(new ChangeOfVoiceToken("1"));
        //new line
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        ArrayList<Token> musicTokens = l.getMusicTokens();
        assertEquals(expectedMusicTokens,musicTokens);
    }
    
    
    @Test
    public void getMusicTokensDifferentSpacingLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_spacing_test.abc");
        //C23/2 D2E F/4 |G4 A/ B c | c2B5/6A G F E D C |
        ArrayList<Token> expectedMusicTokens = new ArrayList<Token>();
        expectedMusicTokens.add(new PlayedNoteToken(23, 2, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 4, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new PlayedNoteToken(4, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 2, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "C", 0, false, 1));
        expectedMusicTokens.add(new PlayedNoteToken(5, 6, "B", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));   
        ArrayList<Token> musicTokens = l.getMusicTokens();
        assertEquals(expectedMusicTokens,musicTokens);
    }
    @Test
    public void getMusicTokensOctavesLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_octaves_test.abc");
        //C'''23/2 D2E, F/4 |G4 A,,/ B c | c'2B5/6A G F E D C |
        ArrayList<Token> expectedMusicTokens = new ArrayList<Token>();
        expectedMusicTokens.add(new PlayedNoteToken(23, 2, "C", 0, false, 3));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, -1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 4, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new PlayedNoteToken(4, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 2, "A", 0, false, -2));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "C", 0, false, 2));
        expectedMusicTokens.add(new PlayedNoteToken(5, 6, "B", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));   
        ArrayList<Token> musicTokens = l.getMusicTokens();
        assertEquals(expectedMusicTokens,musicTokens);
    }
    @Test
    public void getMusicTokensAccidentalsLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_accidentals_test.abc");
        //^^C'''23/2 _D2^E, F/4 |G4 _A,,/ B =c | c'2B5/6A G ^F E D C |
        ArrayList<Token> expectedMusicTokens = new ArrayList<Token>();
        expectedMusicTokens.add(new PlayedNoteToken(23, 2, "C", 2, true, 3));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "D", -1, true, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 1, true, -1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 4, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new PlayedNoteToken(4, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 2, "A", -1, true, -2));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, true, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "C", 0, false, 2));
        expectedMusicTokens.add(new PlayedNoteToken(5, 6, "B", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 1, true, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));   
        ArrayList<Token> musicTokens = l.getMusicTokens();
        assertEquals(expectedMusicTokens,musicTokens);
    }
    
    @Test
    public void getMusicTokensBarlinesLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_barline_tokens_test.abc");
        //^^C'''23/2 _D2^E, F/4 |:G4 _A,,/ B =c |[ c'2B5/6A G ^F E D C ]|
        //^^C'''23/2 _D2^E, F/4 :|G4 _A,,/ B =c || c'2B5/6A G ^F E D C [1
        //^^C'''23/2 _D2^E, F/4 |G4 _A,,/ B =c [2 c'2B5/6A G ^F E D C ||
        ArrayList<Token> expectedMusicTokens = new ArrayList<Token>();
        expectedMusicTokens.add(new PlayedNoteToken(23, 2, "C", 2, true, 3));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "D", -1, true, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 1, true, -1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 4, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BEGINREPEAT));
        expectedMusicTokens.add(new PlayedNoteToken(4, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 2, "A", -1, true, -2));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, true, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.ENDOFSECTION));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "C", 0, false, 2));
        expectedMusicTokens.add(new PlayedNoteToken(5, 6, "B", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 1, true, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.ENDOFSECTION)); 
        
        expectedMusicTokens.add(new PlayedNoteToken(23, 2, "C", 2, true, 3));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "D", -1, true, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 1, true, -1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 4, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.ENDREPEAT));
        expectedMusicTokens.add(new PlayedNoteToken(4, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 2, "A", -1, true, -2));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, true, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.ENDOFSECTION));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "C", 0, false, 2));
        expectedMusicTokens.add(new PlayedNoteToken(5, 6, "B", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 1, true, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.FIRSTREPEAT));  
        
        expectedMusicTokens.add(new PlayedNoteToken(23, 2, "C", 2, true, 3));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "D", -1, true, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 1, true, -1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 4, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BARLINE));
        expectedMusicTokens.add(new PlayedNoteToken(4, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 2, "A", -1, true, -2));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, true, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.SECONDREPEAT));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "C", 0, false, 2));
        expectedMusicTokens.add(new PlayedNoteToken(5, 6, "B", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 1, true, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.ENDOFSECTION));  
        
        ArrayList<Token> musicTokens = l.getMusicTokens();
      
        assertEquals(expectedMusicTokens,musicTokens);
    }
    
    
    @Test
    public void getMusicTokensTupletsAndChordsAndCommentLexerTest() {
        Lexer l = new Lexer("sample_abc\\lexer_tuplets_and_chords_test.abc");
        //(2^^C'''23/2_D2 ^E, F/4 |:[G4 _A,,/] B =c |[ (3c'2B5/6A G ^F [ED] C |]
        ArrayList<Token> expectedMusicTokens = new ArrayList<Token>();
        expectedMusicTokens.add(new TupletToken(2));
        expectedMusicTokens.add(new PlayedNoteToken(23, 2, "C", 2, true, 3));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "D", -1, true, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 1, true, -1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 4, "F", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BEGINREPEAT));
        expectedMusicTokens.add(new StructuralToken(StructType.BEGINCHORD));
        expectedMusicTokens.add(new PlayedNoteToken(4, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 2, "A", -1, true, -2));
        expectedMusicTokens.add(new StructuralToken(StructType.ENDCHORD));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "B", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, true, 1));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.ENDOFSECTION));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new TupletToken(3));
        expectedMusicTokens.add(new PlayedNoteToken(2, 1, "C", 0, false, 2));
        expectedMusicTokens.add(new PlayedNoteToken(5, 6, "B", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "A", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "G", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "F", 1, true, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.BEGINCHORD));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "E", 0, false, 0));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "D", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.ENDCHORD));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new PlayedNoteToken(1, 1, "C", 0, false, 0));
        expectedMusicTokens.add(new StructuralToken(StructType.SPACE));
        expectedMusicTokens.add(new StructuralToken(StructType.ENDOFSECTION)); 
        ArrayList<Token> musicTokens = l.getMusicTokens();

      
        assertEquals(expectedMusicTokens,musicTokens);
    }
    
    //check no Exceptions are thrown
    @Test
    public void getPaddyLexerTest() {
        Lexer l = new Lexer("sample_abc\\paddy.abc");
        ArrayList<Token> musicTokens = l.getMusicTokens();
    }
    
    //check no Exceptions are thrown
    @Test
    public void furEliseLexerTest() {
        Lexer l = new Lexer("sample_abc\\fur_elise.abc");
        ArrayList<Token> musicTokens = l.getMusicTokens();
    }
}


