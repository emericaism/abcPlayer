package sound;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

public class ConverterTest {
    @Test
    public void pieceOneTest() {
        Lexer lexer = new Lexer("sample_abc//piece1.abc");
        Parsere parser = new Parsere(lexer);
        Converter converter;
        try {
            converter = new Converter(parser.getTune());
            SequencePlayer outputPlayer = converter.player();
            
            //Expected output
            try {
                // 140 beats per minute, 12 ticks per quarter note
                SequencePlayer player = new SequencePlayer(140, 12);
                // create all necessary pitches
                Pitch c = new Pitch('C');
                Pitch d = new Pitch('D');
                Pitch e = new Pitch('E');
                Pitch f = new Pitch('F');
                Pitch g = new Pitch('G');

                // ////////////measure line/////////////
                player.addNote(c.toMidiNote(), 0, 12);
                player.addNote(c.toMidiNote(), 12, 12);
                player.addNote(c.toMidiNote(), 24, 9);
                player.addNote(d.toMidiNote(), 33, 3);
                player.addNote(e.toMidiNote(), 36, 12);
                // ////////////measure line/////////////
                player.addNote(e.toMidiNote(), 48, 9);
                player.addNote(d.toMidiNote(), 57, 3);
                player.addNote(e.toMidiNote(), 60, 9);
                player.addNote(f.toMidiNote(), 69, 3);
                player.addNote(g.toMidiNote(), 72, 24);
                // ////////////measure line/////////////
                player.addNote(c.octaveTranspose(1).toMidiNote(), 96, 4);
                player.addNote(c.octaveTranspose(1).toMidiNote(), 100, 4);
                player.addNote(c.octaveTranspose(1).toMidiNote(), 104, 4);
                player.addNote(g.toMidiNote(), 108, 4);
                player.addNote(g.toMidiNote(), 112, 4);
                player.addNote(g.toMidiNote(), 116, 4);
                player.addNote(e.toMidiNote(), 120, 4);
                player.addNote(e.toMidiNote(), 124, 4);
                player.addNote(e.toMidiNote(), 128, 4);
                player.addNote(c.toMidiNote(), 132, 4);
                player.addNote(c.toMidiNote(), 136, 4);
                player.addNote(c.toMidiNote(), 140, 4);
                // ////////////measure line/////////////
                player.addNote(g.toMidiNote(), 144, 9);
                player.addNote(f.toMidiNote(), 153, 3);
                player.addNote(e.toMidiNote(), 156, 9);
                player.addNote(d.toMidiNote(), 165, 3);
                player.addNote(c.toMidiNote(), 168, 24);
                // ////////////end of piece/////////////
                assertEquals(outputPlayer.toString(), player.toString());
            } catch (MidiUnavailableException e) {
                assertTrue(false);
            } catch (InvalidMidiDataException e) {
                assertTrue(false);
            }
        } catch (MidiUnavailableException e1) {
            System.out.println("Sequence player exception");
        } catch (InvalidMidiDataException e1) {
            System.out.println("Sequence player exception");
            e1.printStackTrace();
        }
        
        

        // Expected output
       
    }

    @Test
    public void pieceTwoTest() {
        Lexer lexer = new Lexer("sample_abc//piece2.abc");
        Parsere parser = new Parsere(lexer);
        Converter converter;
        try {
            converter = new Converter(parser.getTune());
            SequencePlayer outputPlayer = converter.player();
            // Expected Output
            SequencePlayer player;
            try {
                player = new SequencePlayer(200, 12);
                // create the full set of pitches that will be transposed
                Pitch c = new Pitch('C');
                Pitch d = new Pitch('D');
                Pitch e = new Pitch('E');
                Pitch f = new Pitch('F');
                Pitch g = new Pitch('G');
                Pitch a = new Pitch('A');
                Pitch b = new Pitch('B');

                // ////////////measure line/////////////
                player.addNote(f.accidentalTranspose(1).toMidiNote(), 0, 6);
                player.addNote(e.octaveTranspose(1).toMidiNote(), 0, 6);
                player.addNote(f.accidentalTranspose(1).toMidiNote(), 6, 6);
                player.addNote(e.octaveTranspose(1).toMidiNote(), 6, 6);
                // rest for 6 ticks (eighth note)
                player.addNote(f.accidentalTranspose(1).toMidiNote(), 18, 6);
                player.addNote(e.octaveTranspose(1).toMidiNote(), 18, 6);
                // rest for 6 ticks (eighth note)
                player.addNote(f.accidentalTranspose(1).toMidiNote(), 30, 6);
                player.addNote(c.octaveTranspose(1).toMidiNote(), 30, 6);

                player.addNote(f.accidentalTranspose(1).toMidiNote(), 36, 12);
                player.addNote(e.octaveTranspose(1).toMidiNote(), 36, 12);

                // ////////////measure line/////////////
                player.addNote(g.toMidiNote(), 48, 12);
                player.addNote(b.toMidiNote(), 48, 12);
                player.addNote(g.octaveTranspose(1).toMidiNote(), 48, 12);
                // rest for 12 ticks (quarter note)
                player.addNote(g.toMidiNote(), 72, 12);
                // rest for 12 ticks (quarter note)

                // ////////////measure line/////////////
                player.addNote(c.octaveTranspose(1).toMidiNote(), 96, 18);
                player.addNote(g.toMidiNote(), 114, 6);
                // rest for 12 ticks (quarter note)
                player.addNote(e.toMidiNote(), 132, 12);

                // ////////////measure line/////////////
                player.addNote(e.toMidiNote(), 144, 6);
                player.addNote(a.toMidiNote(), 150, 12);
                player.addNote(b.toMidiNote(), 162, 12);
                player.addNote(b.accidentalTranspose(-1).toMidiNote(), 174, 6);
                player.addNote(a.toMidiNote(), 180, 12);

                // ////////////measure line/////////////
                player.addNote(g.toMidiNote(), 192, 8);
                player.addNote(e.octaveTranspose(1).toMidiNote(), 200, 8);
                player.addNote(g.octaveTranspose(1).toMidiNote(), 208, 8);
                player.addNote(a.octaveTranspose(1).toMidiNote(), 216, 12);
                player.addNote(f.octaveTranspose(1).toMidiNote(), 228, 6);
                player.addNote(g.octaveTranspose(1).toMidiNote(), 234, 6);

                // ////////////measure line/////////////
                // rest for 6 ticks (eighth note)
                player.addNote(e.octaveTranspose(1).toMidiNote(), 246, 12);
                player.addNote(c.octaveTranspose(1).toMidiNote(), 258, 6);
                player.addNote(d.octaveTranspose(1).toMidiNote(), 264, 6);
                player.addNote(b.toMidiNote(), 270, 9);
                // rest for 8 ticks (dotted eighth note)

                assertEquals(outputPlayer.toString(), player.toString());

            } catch (MidiUnavailableException e1) {
                e1.printStackTrace();
                assertTrue(false);
            } catch (InvalidMidiDataException e1) {
                e1.printStackTrace();
                assertTrue(false);
            }
        } catch (MidiUnavailableException e2) {
            System.out.println("Sequence player exception");
        } catch (InvalidMidiDataException e2) {
            System.out.println("Sequence player exception");
        }
        

       
    }

    @Test
    public void gMajorScaleTest() {
        Lexer lexer = new Lexer("sample_abc//g_major_scale_test.abc");
        Parsere parser = new Parsere(lexer);
        Converter converter;
        try {
            converter = new Converter(parser.getTune());
            SequencePlayer outputPlayer = converter.player();
            
         // Expected output
            SequencePlayer player;
            try {
                player = new SequencePlayer(100, 4);

                // create the full set of pitches that will be transposed
                Pitch c = new Pitch('C');
                Pitch d = new Pitch('D');
                Pitch e = new Pitch('E');
                Pitch f = new Pitch('F');
                Pitch g = new Pitch('G');
                Pitch a = new Pitch('A');
                Pitch b = new Pitch('B');

                player.addNote(g.octaveTranspose(-1).toMidiNote(), 0, 4);
                player.addNote(a.octaveTranspose(-1).toMidiNote(), 4, 6);
                player.addNote(b.octaveTranspose(-1).toMidiNote(), 10, 8);
                player.addNote(c.toMidiNote(), 18, 6);

                player.addNote(d.toMidiNote(), 24, 4);
                player.addNote(e.toMidiNote(), 28, 2);
                player.addNote(f.accidentalTranspose(1).toMidiNote(), 30, 4);
                player.addNote(g.octaveTranspose(1).toMidiNote(), 34, 1);

                player.addNote(g.octaveTranspose(-1).toMidiNote(), 35, 4);
                player.addNote(a.octaveTranspose(-1).toMidiNote(), 39, 6);
                player.addNote(b.octaveTranspose(-1).toMidiNote(), 45, 8);
                player.addNote(c.toMidiNote(), 53, 6);

                player.addNote(a.toMidiNote(), 63, 4);
                player.addNote(b.toMidiNote(), 67, 4);
                player.addNote(c.octaveTranspose(1).toMidiNote(), 71, 4);

                player.addNote(d.octaveTranspose(1).toMidiNote(), 75, 4);
                player.addNote(e.octaveTranspose(1).toMidiNote(), 79, 4);
                player.addNote(f.accidentalTranspose(1).octaveTranspose(1)
                        .toMidiNote(), 83, 4);
                player.addNote(g.octaveTranspose(2).toMidiNote(), 87, 4);

                assertEquals(outputPlayer.toString(), player.toString());

            } catch (MidiUnavailableException e2) {
                e2.printStackTrace();
                assertTrue(false);
            } catch (InvalidMidiDataException e2) {
                e2.printStackTrace();
                assertTrue(false);

            }
        } catch (MidiUnavailableException e1) {
           System.out.println("Sequence player exception");
        } catch (InvalidMidiDataException e1) {
            System.out.println("Sequence player exception");
        }
        

        
    }
}