package audio;

import org.lwjgl.openal.AL10;

public class Source {

	int sourceID;
	
	public Source() {
		sourceID = AL10.alGenSources();
	}
	
	public void setLooping(boolean looping) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public void play(int buffer) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}
	
}
