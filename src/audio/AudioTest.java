package audio;

import java.io.File;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.util.WaveData;

public class AudioTest {

	static int buffer;
	static Source source;
	
	public static void main(String[] args) throws InterruptedException{
		try {
			AL.create();
			AL11.alListener3i(AL10.AL_POSITION, 0, 0, 0);
			AL11.alListener3i(AL10.AL_VELOCITY, 0, 0, 0);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		buffer = loadSound("audio/bruh_audio6.wav");
		source = new Source(); 
		source.setLooping(true);
		source.play(buffer);
		
		float xPos = 10;
		source.setPosition(xPos, 0, 0);
		while(xPos>-10) {
			xPos-=0.03f;
			source.setPosition(xPos, 0, 0);
			Thread.sleep(10);
		}
		source.delete();
		AL10.alDeleteBuffers(buffer);
		AL.destroy();
	}
	
	static int loadSound(String fileName) {
		int buffer = AL10.alGenBuffers();
		WaveData data = WaveData.create(fileName);
		AL10.alBufferData(buffer, data.format, data.data, data.samplerate);
		data.dispose();
		return buffer;
	}
	
}
