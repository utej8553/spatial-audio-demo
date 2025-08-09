# 3D Spatial Audio in Java with OpenAL

## 1. Preparing the Audio

To achieve spatial audio effects with OpenAL, your audio source must be a **mono** (single channel) audio file. This is because OpenAL spatializes sound by positioning a single audio source in 3D space relative to the listener. If you use stereo audio, OpenAL will treat it as a fixed left-right sound and will not apply 3D positioning.

You can convert any MP3 or stereo audio file into a mono WAV file using the powerful command-line tool **FFmpeg**. Here’s the command to convert `sound.mp3` to a mono, 44.1 kHz WAV file:

ffmpeg -i sound.mp3 -ac 1 -ar 44100 sound.wav
- `-ac 1` forces audio channels to mono.

- `-ar 44100` sets the sample rate to 44.1 kHz, a common standard.

- Output file is `sound.wav`.

Place this converted WAV file inside your project directory so your Java application can load it.


## 2. How the Code Works

### 2.1 OpenAL Initialization
The code initializes OpenAL by:

- Opening the default audio device using `alcOpenDevice`. This represents your sound card or output device.
- Creating an OpenAL context with `alcCreateContext`. The context manages all OpenAL state and commands.
- Making this context current with `alcMakeContextCurrent` so subsequent OpenAL calls apply to it.
- Creating capabilities (`AL.createCapabilities`) to access OpenAL functions.

### 2.2 Loading Audio Data
- The mono WAV file is loaded into a `ByteBuffer` in memory.
- An OpenAL buffer is generated and filled with the audio data using `alBufferData`.
- This buffer holds the raw PCM samples ready to be played.

### 2.3 Creating and Positioning a Source
- An OpenAL source acts as a virtual speaker emitting the sound.
- We attach the audio buffer to the source.
- Using `alSource3f(source, AL_POSITION, x, y, z)`, we specify the source’s position in 3D space relative to the listener.

**Coordinate system explanation:**

- X axis: left (-) to right (+)
- Y axis: down (-) to up (+)
- Z axis: behind (-) to front (+)

**Examples of positioning:**

- `(10, 0, 0)`: sound is placed far to the right.
- `(-10, 0, 0)`: sound is far to the left.
- `(0, 0, -5)`: sound is centered but behind the listener.

The listener is typically at `(0, 0, 0)` facing down the negative Z axis.

### 2.4 Playing the Sound
- `alSourcePlay(source)` starts the playback.
- OpenAL internally applies spatialization, adjusting volume and timing between left and right channels to simulate the 3D position.
- When using headphones, this gives a strong sense of directionality.

### 2.5 Optional: HRTF Support
- HRTF (Head-Related Transfer Function) is an advanced technique to simulate how ears receive sound from different directions considering head shape and ear anatomy.
- OpenAL Soft supports HRTF and can be enabled during context creation for more realistic headphone spatialization.
- If enabled, HRTF adds subtle frequency and timing changes that make audio feel like it’s truly coming from a specific point in space.

## 3. Running the Project
- Convert your audio to mono WAV as described in section 1.
- Place `sound.wav` in your project root or resource folder.
- Build and run your Java program, making sure LWJGL (Lightweight Java Game Library) and OpenAL libraries are included in the classpath.
- Use headphones for the best spatial audio experience, as stereo speakers may not give accurate positional cues.

## 4. Spatial Audio Example and Usage Tips
- Position your source at `(10, 0, 0)` to hear the sound mostly in your right ear.
- Move it to `(-10, 0, 0)` to switch it mostly to your left ear.
- Use `(0, 0, -5)` to place the sound behind you.
- Experiment with positions along Y axis for height (up/down).
- You can animate the position over time to create moving sound effects.
