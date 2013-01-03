package player;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.Converter;
import sound.Lexer;
import sound.Parsere;
import sound.SequencePlayer;

/**
 * Main entry point of your application.
 */
public class Main {

	/**
	 * Plays the input file using Java MIDI API and displays
	 * header information to the standard output stream.
	 * 
	 * <p>Your code <b>should not</b> exit the application abnormally using
	 * System.exit()</p>
	 * 
	 * @param file the name of input abc file
	 */
	public static void play(String file) {
		Lexer lexer = new Lexer(file);
		Parsere parser = new Parsere(lexer);
		Converter converter;
        try {
            converter = new Converter(parser.getTune());
            SequencePlayer player = converter.player();
            player.play();
        } catch (MidiUnavailableException e) {
            System.out.println("Sequence player error");
        } catch (InvalidMidiDataException e) {
            System.out.println("Sequence player error");
        }
		
	}
}
