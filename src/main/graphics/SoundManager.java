package graphics;


import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class SoundManager {

	public static final String SOUND_PATH = "assets/sounds";
	public static final String SOUND_EXT = ".wav";


	private SoundManager() { }


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
