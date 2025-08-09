package org.utej;

import org.lwjgl.openal.*;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    public static void main(String[] args) throws IOException {
        long device = alcOpenDevice((java.nio.ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("❌ Failed to open default audio device");
        }
        long context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            alcCloseDevice(device);
            throw new IllegalStateException("❌ Failed to create OpenAL context");
        }
        alcMakeContextCurrent(context);
        AL.createCapabilities(ALC.createCapabilities(device));

        WaveData waveFile = WaveData.create("sound.wav");
        if (waveFile == null) {
            throw new IOException("❌ Failed to load sound.wav");
        }

        int buffer = alGenBuffers();
        alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
        waveFile.dispose();

        int source = alGenSources();
        alSourcei(source, AL_BUFFER, buffer);
        alSourcef(source, AL_GAIN, 1f);
        alSourcef(source, AL_PITCH, 1f);
        alSourcei(source, AL_LOOPING, AL_TRUE);

        alListener3f(AL_POSITION, 0, 0, 0);
        alListener3f(AL_VELOCITY, 0, 0, 0);
        alListenerfv(AL_ORIENTATION, new float[]{0, 0, -1, 0, 1, 0});

        alSource3f(source, AL_POSITION, 5f, 0, 0);

        alSourcePlay(source);

        System.out.println("🎧 Playing sound fully on RIGHT side");
        while (true) {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
    }
}
