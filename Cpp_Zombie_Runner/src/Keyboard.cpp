#include "Keyboard.h"

KeyboardClient::KeyboardClient( KeyboardServer& kServer )
	: server( kServer )
{}

bool KeyboardClient::KeyIsPressed( unsigned char keycode ) const
{
	return server.keystates[ keycode ];
}

bool KeyboardClient::KeyIsPressed_OneShot( unsigned char keycode )
{
	static bool LastKeyPressed[ 256 ] = {false};
	bool retVal = ( server.keystates[ keycode ] && !LastKeyPressed[ keycode ] );
	LastKeyPressed[keycode] = server.keystates[keycode];
	return retVal;
}

void KeyboardClient::IncDecVar_On_Keypress( short* var,char inc,char dec,char alt_inc,char alt_dec,short multiplier,bool OneShot )
{
	if( OneShot )
	{
		if( KeyIsPressed_OneShot( inc ) || KeyIsPressed_OneShot( alt_inc ) )
			*var += multiplier;
		if( KeyIsPressed_OneShot( dec ) || KeyIsPressed_OneShot( alt_dec ) )
			*var -= multiplier;
	}
	else
	{
		if( KeyIsPressed( inc ) || KeyIsPressed( alt_inc ) )
			*var += multiplier;
		if( KeyIsPressed( dec ) || KeyIsPressed( alt_dec) )
			*var -= multiplier;
	}
}

unsigned char KeyboardClient::ReadKey()
{
	if( server.keybuffer.size() > 0 )
	{
		unsigned char keycode = server.keybuffer.front();
		server.keybuffer.pop();
		return keycode;
	}
	else
	{
		return 0;
	}
}

unsigned char KeyboardClient::PeekKey() const
{	
	if( server.keybuffer.size() > 0 )
	{
		return server.keybuffer.front();
	}
	else
	{
		return 0;
	}
}

unsigned char KeyboardClient::ReadChar()
{
	if( server.charbuffer.size() > 0 )
	{
		unsigned char charcode = server.charbuffer.front();
		server.charbuffer.pop();
		return charcode;
	}
	else
	{
		return 0;
	}
}

unsigned char KeyboardClient::PeekChar() const
{
	if( server.charbuffer.size() > 0 )
	{
		return server.charbuffer.front();
	}
	else
	{
		return 0;
	}
}

void KeyboardClient::FlushKeyBuffer()
{
	while( !server.keybuffer.empty() )
	{
		server.keybuffer.pop();
	}
}

void KeyboardClient::FlushCharBuffer()
{
	while( !server.charbuffer.empty() )
	{
		server.charbuffer.pop();
	}
}

void KeyboardClient::FlushBuffers()
{
	FlushKeyBuffer();
	FlushCharBuffer();
}

KeyboardServer::KeyboardServer()
{
	for( int x = 0; x < nKeys; x++ )
	{
		keystates[ x ] = false;
	}
}

void KeyboardServer::OnKeyPressed( unsigned char keycode )
{
	keystates[ keycode ] = true;
	
	keybuffer.push( keycode );
	if( keybuffer.size() > bufferSize )
	{
		keybuffer.pop();
	}
}

void KeyboardServer::OnKeyReleased( unsigned char keycode )
{
	keystates[ keycode ] = false;
}

void KeyboardServer::OnChar( unsigned char character )
{
	charbuffer.push( character );
	if( charbuffer.size() > bufferSize )
	{
		charbuffer.pop();
	}
}

