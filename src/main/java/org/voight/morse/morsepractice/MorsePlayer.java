/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.voight.morse.morsepractice;

import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Jeffrey Voight <jeff.voight@gmail.com>
 */
public class MorsePlayer {

    /**
     *
     */
    protected int frequency = 44100;

    /**
     *
     */
    protected int speed = 10;

    /**
     *
     */
    protected int hz=700;
    static final Logger LOG = Logger.getLogger(MorsePlayer.class.getName());
    HashMap<String, Symbol> symbols = new HashMap<>();
     AudioFormat af;
     SourceDataLine sdl;

    /**
     *
     * @param _frequency
     * @param _hz
     * @param _speed
     * @throws IOException
     * @throws LineUnavailableException
     */
    public MorsePlayer(int _frequency, int _hz, int _speed) throws IOException, LineUnavailableException {
        setFrequency(_frequency);
        setHz(_hz);
        setSpeed(_speed);
        af = new AudioFormat(_frequency, 8, 1, true, false);
        sdl = AudioSystem.getSourceDataLine(af);
        Properties p = new Properties();
        p.load(new FileReader("src/main/resources/ITUsymbols.properties"));
        Enumeration e = p.propertyNames();
        while (e.hasMoreElements()) {
            String key = ((String)e.nextElement());
            String value = (String) p.getProperty(key);
            symbols.put(key.toUpperCase(), new Symbol(key.charAt(0), value, hz, speed));
        }
        symbols.put(" ", new Symbol(' ', "", hz, speed));
    }

    public Symbol getSymbol(String l){
        return symbols.get(l);
    }
    
    /**
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     *
     * @param _frequency
     */
    final public void setFrequency(int _frequency){
        if(_frequency>44100){
            LOG.severe("44100 is about the limit, chum.");
        }
        frequency=_frequency;
    }
    
    /**
     *
     * @param hz
     */
    public final void setHz(int hz) {
        if (hz > 1000) {
            LOG.severe("You have selected a frequency greater than 1000Hz. That's nuts.");
        }
        this.hz = hz;
    }

    /**
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    final public void setSpeed(int speed) {
        if (speed > 30) {
            LOG.severe("You have selected a speed greater than 30 gpm. That's nuts.");
        }
        this.speed = speed;
    }

    /**
     *
     * @param _s
     * @throws LineUnavailableException
     */
    public void play(Symbol _s) throws LineUnavailableException{
        playTone(_s.getBytes());
    }
    
    /**
     *
     * @param _c
     * @throws LineUnavailableException
     */
    public void play(char _c) throws LineUnavailableException {        
        play(symbols.get(""+Character.toUpperCase(_c)));
    }
    
    public Symbol getSymbol(char _c){
        return symbols.get(""+Character.toUpperCase(_c));
    }

    /**
     *
     * @param _s
     * @throws LineUnavailableException
     */
    public void play(String _s) throws LineUnavailableException {
        int strLen=_s.length();
        for(int i=0;i<strLen;i++){
            play(_s.charAt(i));
        }
    }

    /**
     *
     * @param byteArray
     * @throws LineUnavailableException
     */
    public void playTone(byte[] byteArray) throws LineUnavailableException {
        sdl = AudioSystem.getSourceDataLine(af);

        sdl.open(af);
        sdl.start();
        for (int i = 0; i < byteArray.length; i++) {
            sdl.write(byteArray, i, 1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
}
