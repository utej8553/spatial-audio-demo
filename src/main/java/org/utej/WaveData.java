package org.utej;

import org.lwjgl.BufferUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;

public class WaveData {
    public final int format;
    public final int samplerate;
    public final ShortBuffer data;

    private WaveData(int format, int samplerate, ShortBuffer data) {
        this.format = format;
        this.samplerate = samplerate;
        this.data = data;
    }

    public static WaveData create(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] header = new byte[44];
            if (fis.read(header, 0, 44) != 44) {
                throw new IOException("Invalid WAV header");
            }

            int channels = ((header[23] & 0xFF) << 8) | (header[22] & 0xFF);
            int sampleRate = ((header[27] & 0xFF) << 24) | ((header[26] & 0xFF) << 16) |
                    ((header[25] & 0xFF) << 8) | (header[24] & 0xFF);
            int bitsPerSample = ((header[35] & 0xFF) << 8) | (header[34] & 0xFF);

            int format;
            if (channels == 1) {
                format = (bitsPerSample == 8) ? AL_FORMAT_MONO8 : AL_FORMAT_MONO16;
            } else {
                format = (bitsPerSample == 8) ? AL_FORMAT_STEREO8 : AL_FORMAT_STEREO16;
            }

            byte[] rawData = fis.readAllBytes();
            ByteBuffer bb = BufferUtils.createByteBuffer(rawData.length);
            bb.put(rawData);
            bb.flip();

            ShortBuffer sb = bb.asShortBuffer();
            return new WaveData(format, sampleRate, sb);
        }
    }

    public void dispose() {
        data.clear();
    }
}
