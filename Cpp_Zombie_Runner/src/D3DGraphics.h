#pragma once

#include <d3d9.h>

static short SCREENWIDTH = 1000;
static short SCREENHEIGHT = 500;
#define FILLVALUE 0x00

//Sprites
struct Sprite
{
	int width;
	int height;
	D3DCOLOR key;
	D3DCOLOR* surface;
};
void LoadSprite( Sprite* sprite,const char* filename,
	unsigned int width,unsigned int height,D3DCOLOR key );
void FreeSprite( Sprite* sprite );

class D3DGraphics
{
public:
	//Main class functions
	D3DGraphics( HWND hWnd );
	~D3DGraphics();
	void BeginFrame();
	void EndFrame();

	void Swap( int* x1,int* x2 );
	short GetScreenWidth();
	short GetScreenHeight();

	//Pixel
	void PutPixel( int x,int y,int r,int g,int b );
	void PutPixel( int x,int y,D3DCOLOR c );
	D3DCOLOR GetPixel( int x,int y );
	void PutPixelClipped( int x,int y,int r,int g,int b,int minx = 0,int miny = 0,int maxx = SCREENWIDTH,int maxy = SCREENHEIGHT );
	void PutPixelClipped( int x,int y,D3DCOLOR c,int minx = 0,int miny = 0,int maxx = SCREENWIDTH,int maxy = SCREENHEIGHT );

	//Sprites
	void DrawSprite( int x,int y,Sprite* sprite );
	void DrawSpriteClipped( int x,int y,Sprite* sprite,int minx = 0,int miny = 0,int maxx = SCREENWIDTH,int maxy = SCREENHEIGHT );

	//Shapes
	void DrawLine( int x1,int y1,int x2,int y2,int r,int g,int b );
	void DrawLineClipped( int x1,int y1,int x2,int y2,int r,int g,int b,int minx = 0,int miny = 0,int maxx = SCREENWIDTH,int maxy = SCREENHEIGHT );
	void DrawCircle( int cx,int cy,int radius,int r,int g,int b );
	void DrawHalfCircleRight( int cx,int cy,int radius,int r,int g,int b );
	void DrawHalfCircleLeft( int cx,int cy,int radius,int r,int g,int b );
	void DrawHalfCircleDown( int cx,int cy,int radius,int r,int g,int b );
	void DrawHalfCircleUp( int cx,int cy,int radius,int r,int g,int b );
	void DrawSolidCircle( int cx,int cy,int radius,int r,int g,int b );
	void DrawRect( int x1,int y1,int x2,int y2,int r,int g,int b );
	void DrawSolidRect( int x1,int y1,int x2,int y2,int r,int g,int b );
	void DrawSolidRect( int x1,int y1,int width,int height,int c );

	//Words & Numbers
	void DrawWord( const char* Word,int x,int y,int size,int spacing,int thickness,int r,int g,int b );
	void DrawLetter( char letter,int x,int y,int size,int r,int g,int b );
	void DrawVariable( int variable,int x,int y,int size,int spacing,int thickness,int r,int g,int b );
	void DrawNumbers( const char* Numbers,int x,int y,int size,int spacing,int thickenss,int r,int g,int b );
	void DrawNumber( char Number,int x,int y,int size,int r,int g,int b );

private:
	IDirect3D9*			pDirect3D;
	IDirect3DDevice9*	pDevice;
	IDirect3DSurface9*	pBackBuffer;
	D3DLOCKED_RECT		backRect;
	D3DCOLOR*			pSysBuffer;
};