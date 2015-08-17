package com.nox.engine.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class JavaSoundClip extends Sound {
	private Clip clip;
	private AudioInputStream ais;
	private File file;
	private boolean restartIfAlreadyRunning;

	public JavaSoundClip(String fileName, boolean restartIfAlreadyRunning) {
		file = new File(fileName);
		initSound(file);
	}

	public JavaSoundClip(JavaSoundClip sound) {
		this.file = sound.file;
		this.restartIfAlreadyRunning = sound.restartIfAlreadyRunning;
		initSound(file);
	}

	public void setRestartIfAlreadyRunning(boolean value) {
		this.restartIfAlreadyRunning = value;
	}

	private void initSound(File file) {
		this.close();
		try {
			clip = AudioSystem.getClip();
			ais = AudioSystem.getAudioInputStream(file);
			clip.open(ais);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Audio file " + file.getPath() + " was not found. Make sure you entered the file path and name correctly.");
			System.exit(0);
		} catch(LineUnavailableException e) {
			e.printStackTrace();
		} catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Audio file " + file.getPath() + " is not in a supported format. If you want to know a supported format, the WAV format is supported.");
			System.exit(0);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void play() {
		if(clip != null && clip.isOpen()) {
			if(clip.isRunning()) {
				if(restartIfAlreadyRunning) {
					stop();
					clip.setFramePosition(0);
					clip.start();
				}
			}
			else {
				clip.setFramePosition(0);
				clip.start();
			}
		}
		else {
			initSound(file);
			clip.setFramePosition(0);
			clip.start();
		}
	}

	/**
	 * 
	 * @param count
	 *            0 means play once, 1 means play twice etc
	 */
	@Override
	public void loop(int count) {
		if(clip != null && clip.isOpen()) {
			if(clip.isRunning()) {
				if(restartIfAlreadyRunning) {
					stop();
					clip.setFramePosition(0);
					clip.loop(count);
				}
			}
			else {
				clip.setFramePosition(0);
				clip.loop(count);
			}
		}
		else {
			initSound(file);
			clip.setFramePosition(0);
			clip.loop(count);
		}
	}

	/**
	 * loops infinately
	 */
	@Override
	public void loop() {
		if(clip != null && clip.isOpen()) {
			if(clip.isRunning()) {
				if(restartIfAlreadyRunning) {
					stop();
					clip.setFramePosition(0);
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				}
			}
			else {
				clip.setFramePosition(0);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
		else {
			initSound(file);
			clip.setFramePosition(0);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	@Override
	public void stop() {
		if(clip != null && clip.isOpen()) {
			clip.stop();
		}
	}

	public void close() {
		if(clip != null && clip.isOpen()) {
			clip.stop();
			clip.close();
		}
		if(ais != null) {
			try {
				ais.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void finalize() {
		this.close();
	}
}
