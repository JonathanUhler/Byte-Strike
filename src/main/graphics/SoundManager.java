package graphics;


import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/**
 * Manages the audio capabilities of the game.
 *
 * @author Jonathan Uhler
 */
public class SoundManager {

	/** The top-level path for all sounds. */
	public static final String SOUND_PATH = "assets/sounds";
	/** The file extension for sound files. */
	public static final String SOUND_EXT = ".wav";


	/**
	 * This class cannot be constructed.
	 */
	private SoundManager() { }


	/**
	 * Plays a sound from the name of its file. The file name should not include an extension or
	 * any components of {@code SoundManager.SOUND_PATH}. If the sound file is nested beyond
	 * the {@code SOUND_PATH}, those directories should be included. A leading slash is not
	 * required.
	 * <p>
	 * The sound, if loaded, will play asynchronously in a new thread. This thread is destroyed
	 * after the method call. If the sound cannot be played due to an exception, the
	 * stack trace will be printed and the thread will be gracefully exited.
	 *
	 * @param fileName  strictly the name of the sound file to play.
	 */
	public static void playSound(String fileName) {
		Thread soundThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String path = SOUND_PATH + "/" + fileName + SOUND_EXT;
						URL url = Thread.currentThread()
							.getContextClassLoader()
							.getResource(path);
						
						AudioInputStream ais = AudioSystem.getAudioInputStream(url);  
						Clip c = AudioSystem.getClip();
						c.open(ais);
						c.start();
					}
					catch (Exception e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
						return;
					}
				}
			});

		soundThread.start();
	}

}
