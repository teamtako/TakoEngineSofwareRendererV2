package audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public class Source {

	int sourceID;

	public Source() {
		sourceID = AL10.alGenSources();
	}

	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}

	public void setVelocity(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_VELOCITY, x, y, z);
	}

	public void setLooping(boolean looping) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public void play(int buffer) {
		AL10.alSourceStop(sourceID);
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}

	public void delete() {
		AL10.alDeleteSources(sourceID);
	}
	
}
