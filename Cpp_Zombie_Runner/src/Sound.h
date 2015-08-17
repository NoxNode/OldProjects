#pragma once

#include <windows.h>
#include <mmsystem.h>
#include <dsound.h>
#include <stdio.h>
#include "Updates.h"

class DSound;

class Sound
{
	friend DSound;
public:
	Sound( const Sound& base );
	Sound();
	~Sound();
	const Sound& operator=( const Sound& rhs );
	void Play( short attenuation = DSBVOLUME_MAX );
	void Loop( short duration,short attenuation = DSBVOLUME_MAX );
	void SetLoopTimer( short set ) { LoopTimer.SetTimer(set); };
	void Stop() {pBuffer->Stop();};
private:
	Sound( IDirectSoundBuffer8* pSecondaryBuffer );
	Timer LoopTimer;
private:
	IDirectSoundBuffer8* pBuffer;
};

class DSound
{
private:
	struct WaveHeaderType
	{
		char chunkId[4];
		unsigned long chunkSize;
		char format[4];
		char subChunkId[4];
		unsigned long subChunkSize;
		unsigned short audioFormat;
		unsigned short numChannels;
		unsigned long sampleRate;
		unsigned long bytesPerSecond;
		unsigned short blockAlign;
		unsigned short bitsPerSample;
		char dataChunkId[4];
		unsigned long dataSize;
	};
public:
	DSound( HWND hWnd );
	~DSound();
	Sound CreateSound( char* wavFileName );
private:
	DSound();
private:
	IDirectSound8* pDirectSound;
	IDirectSoundBuffer* pPrimaryBuffer;
};
