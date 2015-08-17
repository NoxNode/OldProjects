#pragma once
#include <Windows.h>
#include <queue>

class KeyboardServer;

class KeyboardClient
{
public:
	KeyboardClient( KeyboardServer& kServer );
	bool KeyIsPressed( unsigned char keycode ) const;
	bool KeyIsPressed_OneShot( unsigned char keycode );
	unsigned char ReadKey();
	unsigned char PeekKey() const;
	unsigned char ReadChar();
	unsigned char PeekChar() const;
	void FlushKeyBuffer();
	void FlushCharBuffer();
	void FlushBuffers();
	void IncDecVar_On_Keypress( short* var,char inc,char dec,char alt_inc = 0,char alt_dec = 0,short multiplier = 1,bool OneShot = false );
private:
	KeyboardServer& server;
};

class KeyboardServer
{
	friend KeyboardClient;
public:
	KeyboardServer();
	void OnKeyPressed( unsigned char keycode );
	void OnKeyReleased( unsigned char keycode );
	void OnChar( unsigned char character );
private:
	static const int nKeys = 256;
	static const int bufferSize = 4;
	bool keystates[ nKeys ];
	std::queue<unsigned char> keybuffer;
	std::queue<unsigned char> charbuffer;
};