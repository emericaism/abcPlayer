package sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * A Converter takes in a ABCTune, and creates an ABC SequencePlayer that plays
 * the music represented by ABCTune
 * 
 */
public class Converter {

    private static SequencePlayer player;
    private ABCTune abcTune;
    private ABCMusic abcMusic;
    private double quarterNoteLength = 0.25;
    private int ticksPerQuarterNote;
    private static int ticksPerDefaultNote;
    private int beatsPerMinute;
    private static int currentTick;
    

    /**
     * Constructs a Converter for the ABCTune passed in
     * 
     * @param Tune
     *            The ABC music file represented as an ABCTune
     * @throws InvalidMidiDataException
     * @throws MidiUnavailableException
     * 
     */
    public Converter(ABCTune Tune) throws MidiUnavailableException,
            InvalidMidiDataException {
        abcTune = Tune;
        abcMusic = abcTune.getMusic();
        int tempo = abcTune.getTempo();

        int defaultLengthNum = abcTune.getDefaultLengthNumerator();
        int defaultLengthDenom = abcTune.getDefaultLengthDenominator();
        double defaultNoteLength = (double) defaultLengthNum
                / (double) defaultLengthDenom;

        final double doubleTempo = (double) tempo;

        // Beats Per Minute = Default Note Length / Quarter Note Length * Tempo
        beatsPerMinute = (int) ((defaultNoteLength / quarterNoteLength) * doubleTempo);

        // Quarter Note in terms of the default note length
        int quarterNoteNum = 1;
        int quarterNoteDenom = 4;
        int refactorQuarterNoteNum = quarterNoteNum * defaultLengthDenom;
        int refactorQuarterNoteDenom = quarterNoteDenom * defaultLengthNum;
        double quarterNoteInDefaultNote = (double) refactorQuarterNoteNum
                / (double) refactorQuarterNoteDenom;

        // ticksPerDefaultNote = LCD(default note length, 1 /
        // leastCommonMultiple, quarter note expressed in the default note
        // length
        
        ArrayList<Integer> listOfDenoms = abcTune.getSeenDenoms();
        listOfDenoms.add(defaultLengthDenom);
        listOfDenoms.add(refactorQuarterNoteDenom);
        long[] arrayOfDenoms = new long[listOfDenoms.size()];
        for (int i = 0; i < listOfDenoms.size(); i++){
            arrayOfDenoms[i] = listOfDenoms.get(i);
        }
        long lcmLong = lcm(arrayOfDenoms);
        int lcmDenom = (int) lcmLong;
        
        ticksPerDefaultNote = lcmDenom;

        ticksPerQuarterNote = (int) (quarterNoteInDefaultNote
                * (double) ticksPerDefaultNote);

        player = new SequencePlayer(beatsPerMinute, ticksPerQuarterNote);

    }

    /**
     * From the converter, creates and returns a SequencePlayer with the
     * relevant pitches added
     * 
     * @return SequencePlayer the SequencePlayer representing the music passed
     *         in
     */
    public SequencePlayer player() {
        Map<String, Voice> voiceMap = new HashMap<String, Voice>(
                abcMusic.getVoiceHash());

        // Need to iterate through every voice of the music, and play each of
        // them

        for (Voice voice : voiceMap.values()) {
            currentTick = 0;
            playVoice(voice);
        }
        return player;
    }

    /**
     * Takes in a voice and iterates through all of its group. For each group
     * calls playGroup
     * 
     * @param voice
     *            The voice to be played
     */
    public void playVoice(Voice voice) {
        for (PlayedGroup playedGroup : voice.getPlayedGroups()) {
            if (playedGroup instanceof MeasureElement) {
                playMeasure((MeasureElement) playedGroup);
            } else {
                playRepeat((Repeat) playedGroup);
            }
        }
    }

    /**
     * Takes in a repeat and calls play on each of its measure elements
     * 
     * @param repeat
     *            The repeat to be played
     */

    public void playRepeat(Repeat repeat) {
        for (PlayedGroup playedGroup : repeat.getPlayedGroups()) {
            if (playedGroup instanceof MeasureElement) {
                playMeasure((MeasureElement) playedGroup);
            } else {
                playRepeat((Repeat) playedGroup);
            }
        }
    }

    /**
     * Thanks in a measure element and calls the relevant play on each of its
     * elements Each play call will return the absolute length of the element
     * Therefore, the currentTick should be updated after each play call
     * 
     * @param measure
     *            The measure to be played
     */
    public void playMeasure(MeasureElement measure) {
        for (Played playedElement : measure.getPlayeds()) {
            int numTicks = play(playedElement);
            currentTick += numTicks;
        }
    }

    /**
     * Takes in a note from a chord and adds the midi event associated to the
     * player. Does not increment the current tick position but calculates the
     * length of the note
     * 
     * @param note
     *            The note that will be played
     * @returns numTicks The length of the note in ticks
     * @modifies player The Sequence Player adds the relevant note
     */
    public static int playNoteInChord(PlayedNote note) {
        double numTicksDouble = note.getLengthDoub() * (double) ticksPerDefaultNote;
        int numTicks = (int) numTicksDouble;
        int octave = note.getOctave();
        int accidental = note.getAccidental();
        String baseNote = note.getBaseNote();
        Pitch basePitch = new Pitch(baseNote.charAt(0));
        player.addNote(basePitch.accidentalTranspose(accidental)
                .octaveTranspose(octave).toMidiNote(), currentTick, numTicks);
        return numTicks;
    }

    public static int play(Played playedElement) {
        return playedElement
                .acceptConverter(new Played.ConverterVisitor<Integer>() {
                    /**
                     * Takes in a rest and calculates the length of the rest
                     * 
                     * @param rest
                     *            The rest that is played
                     * @return numTicks The length of the rest in ticks
                     */
                    public Integer play(Rest rest) {
                        double numTicksDouble = rest.getLengthDoub()
                                * (double) ticksPerDefaultNote;
                        int numTicks = (int) numTicksDouble;
                        return numTicks;
                    }

                    /**
                     * Takes in a single element PlayedNote (i.e one that is not
                     * in a chord) and adds the midi event associated with that
                     * note to the player.
                     * 
                     * @param note
                     *            The note to be played
                     * @return numTicks The length of the note in ticks
                     * @modifies player The Sequence Player adds the relevant
                     *           pitch
                     */
                    public Integer play(PlayedNote note) {
                        double numTicksDouble = note.getLengthDoub()
                                * (double) ticksPerDefaultNote;
                        int numTicks = (int) numTicksDouble;
                        
                        int octave = note.getOctave();
                        int accidental = note.getAccidental();
                        String baseNote = note.getBaseNote();
                        Pitch basePitch = new Pitch(baseNote.charAt(0));
                        player.addNote(basePitch
                                .accidentalTranspose(accidental)
                                .octaveTranspose(octave).toMidiNote(),
                                currentTick, numTicks);
                        return numTicks;
                    }

                    /**
                     * Takes in a chord and calls playNoteInChord on each note
                     * of the chord. Calculates the length of the chord
                     * 
                     * @param chord
                     *            The chord to be played
                     * @return numTicks The length of the chord in ticks
                     */
                    public Integer play(Chord c) {
                        int numTicks = 0;
                        for (PlayedNote note : c.getPlayedNotes()) {
                            numTicks = playNoteInChord(note);
                        }
                        return numTicks;
                    }

                    /**
                     * Takes in a tuplet and calls playNoteInTuplet on each note
                     * of the tuplet. Calculates the length of the last element
                     * of the tuplet
                     * 
                     * @param tuplet
                     *            The tuplet to be played
                     * @return numTicks The length of the last element of the
                     *         tuplet in ticks
                     */
                    public Integer play(Tuplet tuplet) {
                        ArrayList<Played> notes = tuplet.getPlayeds();
                        int type = tuplet.getNumObjectsInTuplet();
                        int numTicks = 0;
                        double modifier;
                        if (type == 2) {
                            modifier = 3.0 / 2.0;
                        } else if (type == 3) {
                            modifier = 2.0 / 3.0;
                        } else {
                            modifier = 3.0 / 4.0;
                        }
                        for (int i = 0; i < notes.size(); i++) {
                            if (notes.get(i) instanceof Rest) {

                                int restNumTicks = playRestInTuplet(
                                        (Rest) notes.get(i), modifier);
                                numTicks += restNumTicks;

                            } else {
                                int noteNumTicks = playNoteInTuplet(
                                        (PlayedNote) notes.get(i), modifier, i);
                                numTicks += noteNumTicks;
                            }
                        }
                        return numTicks;
                    }
                });
    }

    /**
     * Takes in a PlayedNote of a tuplet, adds it to the player, after
     * calculating and modifying its length by the requisite amount
     * 
     * @param note
     *            The note to be played
     * @param modifier
     *            The modifier for the particular tuplet
     * @param position
     *            The position of this note in the tupley
     * @return numTicks The length of the note in ticks
     * @modifies player The sequence players adds the correct note
     */
    public static int playNoteInTuplet(PlayedNote note, double modifier, int position) {
        double numTicksDouble = note.getLengthDoub() * modifier
                * (double) ticksPerDefaultNote;
        int numTicks = (int) numTicksDouble;
        int octave = note.getOctave();
        int accidental = note.getAccidental();
        String baseNote = note.getBaseNote();
        Pitch basePitch = new Pitch(baseNote.charAt(0));
        int notePosition = currentTick + position * numTicks;
        
        player.addNote(basePitch.accidentalTranspose(accidental)
                .octaveTranspose(octave).toMidiNote(), notePosition, numTicks);
        return numTicks;
    }

    /**
     * Takes in a Rest in a tuplet, and calculates its length by modifying it by
     * the correct amount
     * 
     * @param rest
     *            The rest to be played
     * @param modifier
     *            The modifier for that tuplet
     * @return numTicks The length of the rest in ticks
     */
    public static int playRestInTuplet(Rest rest, double modifier) {
        double numTicksDouble = rest.getLengthDoub() * modifier
                * (double) ticksPerDefaultNote;
        int numTicks = (int) numTicksDouble;
        return numTicks;
    }
    
    
    // Collection of functions found on stack overflow to calculate lcm of the denominators
    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; 
            a = temp;
        }
        return a;
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    private static long lcm(long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++)
            result = lcm(result, input[i]);
        return result;
    }
}