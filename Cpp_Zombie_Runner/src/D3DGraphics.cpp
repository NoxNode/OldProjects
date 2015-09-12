#include "D3DGraphics.h"
#include <stdlib.h>
#include <math.h>
#include <assert.h>
#include <GdiPlus.h>
#include <stdio.h>
#include "Bitmap.h"
#pragma comment( lib,"gdiplus.lib" )
#pragma comment( lib,"D3D9.lib" )

void D3DGraphics::Swap( int* x1,int* x2 )
{
	int temp = *x1;
	*x1 = *x2;
	*x2 = temp;
}

short D3DGraphics::GetScreenWidth()
{	
	//Get Screen Resolution
	RECT desktop;
   const HWND hDesktop = GetDesktopWindow();
   GetWindowRect(hDesktop, &desktop);
	RECT wr;
    return desktop.right;
}

short D3DGraphics::GetScreenHeight()
{	
	//Get Screen Resolution
	RECT desktop;
   const HWND hDesktop = GetDesktopWindow();
   GetWindowRect(hDesktop, &desktop);
	RECT wr;
    return desktop.bottom-48-30;
}


//Main class functions
D3DGraphics::D3DGraphics( HWND hWnd )
	:
pDirect3D( NULL ),
pDevice( NULL ),
pBackBuffer( NULL ),
pSysBuffer( NULL )
{
	SCREENWIDTH = GetScreenWidth();
	SCREENHEIGHT = GetScreenHeight();

	HRESULT result;

	backRect.pBits = NULL;
	
	pDirect3D = Direct3DCreate9( D3D_SDK_VERSION );
	assert( pDirect3D != NULL );

    D3DPRESENT_PARAMETERS d3dpp;
    ZeroMemory( &d3dpp,sizeof( d3dpp ) );
    d3dpp.Windowed = TRUE;
    d3dpp.SwapEffect = D3DSWAPEFFECT_DISCARD;
    d3dpp.BackBufferFormat = D3DFMT_UNKNOWN;
	d3dpp.PresentationInterval = D3DPRESENT_INTERVAL_ONE;
	d3dpp.Flags = D3DPRESENTFLAG_LOCKABLE_BACKBUFFER;

    result = pDirect3D->CreateDevice( D3DADAPTER_DEFAULT,D3DDEVTYPE_HAL,hWnd,
		D3DCREATE_HARDWARE_VERTEXPROCESSING | D3DCREATE_PUREDEVICE,&d3dpp,&pDevice );
	assert( !FAILED( result ) );

	result = pDevice->GetBackBuffer( 0,0,D3DBACKBUFFER_TYPE_MONO,&pBackBuffer );
	assert( !FAILED( result ) );

	pSysBuffer = new D3DCOLOR[ SCREENWIDTH * SCREENHEIGHT ];
}
D3DGraphics::~D3DGraphics()
{
	if( pDevice )
	{
		pDevice->Release();
		pDevice = NULL;
	}
	if( pDirect3D )
	{
		pDirect3D->Release();
		pDirect3D = NULL;
	}
	if( pBackBuffer )
	{
		pBackBuffer->Release();
		pBackBuffer = NULL;
	}
	if( pSysBuffer )
	{
		delete pSysBuffer;
		pSysBuffer = NULL;
	}
}
void D3DGraphics::BeginFrame()
{
	memset( pSysBuffer,FILLVALUE,sizeof( D3DCOLOR ) * SCREENWIDTH * SCREENHEIGHT );
}
void D3DGraphics::EndFrame()
{
	HRESULT result;

	result = pBackBuffer->LockRect( &backRect,NULL,NULL );
	assert( !FAILED( result ) );

	for( int y = 0; y < SCREENHEIGHT; y++ )
	{
		memcpy( &((BYTE*)backRect.pBits)[ backRect.Pitch * y ],&pSysBuffer[ SCREENWIDTH * y ],sizeof( D3DCOLOR ) * SCREENWIDTH );
	}

	result = pBackBuffer->UnlockRect();
	assert( !FAILED( result ) );

	result = pDevice->Present( NULL,NULL,NULL,NULL );
	assert( !FAILED( result ) );
}

//Pixels
void D3DGraphics::PutPixel( int x,int y,int r,int g,int b )
{	
	assert( x >= 0 );
	assert( y >= 0 );
	assert( x < SCREENWIDTH );
	assert( y < SCREENHEIGHT );
	pSysBuffer[ x + SCREENWIDTH * y ] = D3DCOLOR_XRGB( r,g,b );
}
void D3DGraphics::PutPixel( int x,int y,D3DCOLOR c )
{	
	assert( x >= 0 );
	assert( y >= 0 );
	assert( x < SCREENWIDTH );
	assert( y < SCREENHEIGHT );
	pSysBuffer[ x + SCREENWIDTH * y ] = c;
}
D3DCOLOR D3DGraphics::GetPixel( int x,int y )
{
	assert( x >= 0 );
	assert( y >= 0 );
	assert( x < SCREENWIDTH );
	assert( y < SCREENHEIGHT );
	return pSysBuffer[ x + SCREENWIDTH * y ];
}
void D3DGraphics::PutPixelClipped( int x,int y,int r,int g,int b,int minx,int miny,int maxx,int maxy )
{	
	if( x >= minx && y >= miny && x < maxx && y < maxy )
	{
		pSysBuffer[ x + SCREENWIDTH * y ] = D3DCOLOR_XRGB( r,g,b );
	}
}
void D3DGraphics::PutPixelClipped( int x,int y,D3DCOLOR c,int minx,int miny,int maxx,int maxy )
{	
	if( x >= minx && y >= miny && x < maxx && y < maxy )
	{
		pSysBuffer[ x + SCREENWIDTH * y ] = c;
	}
}

//Sprites
void LoadSprite( Sprite* sprite,const char* filename,
	unsigned int width,unsigned int height,D3DCOLOR key )
{
	sprite->surface = (D3DCOLOR*)malloc( sizeof( D3DCOLOR ) * width * height );
	LoadBmp( filename,sprite->surface );
	sprite->height = height;
	sprite->width = width;
	sprite->key = key;
}
void FreeSprite( Sprite* sprite )
{
	free( sprite->surface );
}
void D3DGraphics::DrawSprite( int xoff,int yoff,Sprite* sprite )
{
	for( int y = 0; y < sprite->height; y++ )
	{
		for( int x = 0; x < sprite->width; x++ )
		{
			D3DCOLOR c = sprite->surface[ x + y * sprite->width ];
			if( c != sprite->key )
			{
				PutPixel( x + xoff,y + yoff,c );
			}
		}
	}
}
void D3DGraphics::DrawSpriteClipped( int xoff,int yoff,Sprite* sprite,int minx,int miny,int maxx,int maxy )
{
	for( int y = 0; y < sprite->height; y++ )
	{
		for( int x = 0; x < sprite->width; x++ )
		{
			D3DCOLOR c = sprite->surface[ x + y * sprite->width ];
			if( c != sprite->key )
			{
				PutPixelClipped( x + xoff,y + yoff,c,minx,miny,maxx,maxy );
			}
		}
	}
}

//Shapes
void D3DGraphics::DrawLine( int x1,int y1,int x2,int y2,int r,int g,int blu )
{
	int dx = x2 - x1;
	int dy = y2 - y1;

	if( dy == 0 && dx == 0 )
	{
		PutPixel( x1,y1,r,g,blu );
	}
	else if( abs( dy ) > abs( dx ) )
	{
		if( dy < 0 )
		{
			Swap( &x1,&x2 );
			Swap( &y1,&y2 );
		}
		float m = (float)dx / (float)dy;
		float b = x1 - m*y1;
		for( int y = y1; y <= y2; y = y + 1 )
		{
			int x = (int)(m*y + b + 0.5f);
			PutPixel( x,y,r,g,blu );
		}
	}
	else
	{
		if( dx < 0 )
		{
			Swap( &x1,&x2 );
			Swap( &y1,&y2 );
		}
		float m = (float)dy / (float)dx;
		float b = y1 - m*x1;
		for( int x = x1; x <= x2; x = x + 1 )
		{
			int y = (int)(m*x + b + 0.5f);
			PutPixel( x,y,r,g,blu );
		}
	}
}
void D3DGraphics::DrawLineClipped( int x1,int y1,int x2,int y2,int r,int g,int blu,int minx,int miny,int maxx,int maxy )
{
	int dx = x2 - x1;
	int dy = y2 - y1;

	if( dy == 0 && dx == 0 )
	{
		PutPixelClipped( x1,y1,r,g,blu,minx,miny,maxx,maxy );
	}
	else if( abs( dy ) > abs( dx ) )
	{
		if( dy < 0 )
		{
			Swap( &x1,&x2 );
			Swap( &y1,&y2 );
		}
		float m = (float)dx / (float)dy;
		float b = x1 - m*y1;
		for( int y = y1; y <= y2; y = y + 1 )
		{
			int x = (int)(m*y + b + 0.5f);
			PutPixelClipped( x,y,r,g,blu,minx,miny,maxx,maxy );
		}
	}
	else
	{
		if( dx < 0 )
		{
			Swap( &x1,&x2 );
			Swap( &y1,&y2 );
		}
		float m = (float)dy / (float)dx;
		float b = y1 - m*x1;
		for( int x = x1; x <= x2; x = x + 1 )
		{
			int y = (int)(m*x + b + 0.5f);
			PutPixelClipped( x,y,r,g,blu,minx,miny,maxx,maxy );
		}
	}
}
void D3DGraphics::DrawCircle( int centerX,int centerY,int radius,int r,int g,int b )
{
	int rSquared = radius*radius;
	int xPivot = (int)(radius * 0.707107f + 0.5f);
	for( int x = 0; x <= xPivot; x++ )
	{
		int y = (int)(sqrt( (float)( rSquared - x*x ) ) + 0.5f);
		PutPixel( centerX + x,centerY + y,r,g,b );
		PutPixel( centerX - x,centerY + y,r,g,b );
		PutPixel( centerX + x,centerY - y,r,g,b );
		PutPixel( centerX - x,centerY - y,r,g,b );
		PutPixel( centerX + y,centerY + x,r,g,b );
		PutPixel( centerX - y,centerY + x,r,g,b );
		PutPixel( centerX + y,centerY - x,r,g,b );
		PutPixel( centerX - y,centerY - x,r,g,b );
	}
}
void D3DGraphics::DrawHalfCircleRight( int centerX,int centerY,int radius,int r,int g,int b )
{
	int rSquared = radius*radius;
	int xPivot = (int)(radius * 0.707107f + 0.5f);
	for( int x = 0; x <= xPivot; x++ )
	{
		int y = (int)(sqrt( (float)( rSquared - x*x ) ) + 0.5f);
		PutPixel( centerX + x,centerY + y,r,g,b );
		PutPixel( centerX + x,centerY - y,r,g,b );
		PutPixel( centerX + y,centerY + x,r,g,b );
		PutPixel( centerX + y,centerY - x,r,g,b );
	}
}
void D3DGraphics::DrawHalfCircleLeft( int centerX,int centerY,int radius,int r,int g,int b )
{
	int rSquared = radius*radius;
	int xPivot = (int)(radius * 0.707107f + 0.5f);
	for( int x = 0; x <= xPivot; x++ )
	{
		int y = (int)(sqrt( (float)( rSquared - x*x ) ) + 0.5f);
		PutPixel( centerX - x,centerY + y,r,g,b );
		PutPixel( centerX - x,centerY - y,r,g,b );
		PutPixel( centerX - y,centerY + x,r,g,b );
		PutPixel( centerX - y,centerY - x,r,g,b );
	}
}
void D3DGraphics::DrawHalfCircleDown( int centerX,int centerY,int radius,int r,int g,int b )
{
	int rSquared = radius*radius;
	int xPivot = (int)(radius * 0.707107f + 0.5f);
	for( int x = 0; x <= xPivot; x++ )
	{
		int y = (int)(sqrt( (float)( rSquared - x*x ) ) + 0.5f);
		PutPixel( centerX + x,centerY + y,r,g,b );
		PutPixel( centerX - x,centerY + y,r,g,b );
		PutPixel( centerX + y,centerY + x,r,g,b );
		PutPixel( centerX - y,centerY + x,r,g,b );
	}
}
void D3DGraphics::DrawHalfCircleUp( int centerX,int centerY,int radius,int r,int g,int b )
{
	int rSquared = radius*radius;
	int xPivot = (int)(radius * 0.707107f + 0.5f);
	for( int x = 0; x <= xPivot; x++ )
	{
		int y = (int)(sqrt( (float)( rSquared - x*x ) ) + 0.5f);
		PutPixel( centerX - x,centerY - y,r,g,b );
		PutPixel( centerX + x,centerY - y,r,g,b );
		PutPixel( centerX + y,centerY - x,r,g,b );
		PutPixel( centerX - y,centerY - x,r,g,b );
	}
}
void D3DGraphics::DrawSolidCircle( int cx,int cy,int radius,int r,int g,int b )
{
	for( int x = cx - radius; x < cx + radius; x++ )
	{
		for( int y = cy - radius; y < cy + radius; y++ )
		{
			if( sqrt( (float)( (x - cx)*(x - cx) + (y - cy)*(y - cy) ) ) < radius )
			{
				PutPixel( x,y,r,g,b );
			}
		}
	}
}
void D3DGraphics::DrawRect( int x1,int y1,int x2,int y2,int r,int g,int b )
{
	DrawLine(x1,y1,x2,y1,r,g,b);
	DrawLine(x1,y2,x2,y2,r,g,b);
	DrawLine(x1,y1,x1,y2,r,g,b);
	DrawLine(x2,y1,x2,y2,r,g,b);
}
void D3DGraphics::DrawSolidRect( int x1,int y1,int x2,int y2,int r,int g,int b )
{
	for( int y = y1; y <= y2; y++ )
	{
		for( int x = x1; x <= x2; x++ )
		{
			PutPixel( x,y,r,g,b );
		}
	}
}
void D3DGraphics::DrawSolidRect( int x1,int y1,int width,int height,int c )
{
	for( int y = y1; y <= y1+height; y++ )
	{
		for( int x = x1; x <= x1+width; x++ )
		{
			PutPixel( x,y,c );
		}
	}
}

//Words & Numbers
void D3DGraphics::DrawVariable( int variable,int x,int y,int size,int spacing,int thickness,int r,int g,int b )
{
	char Numbers[100];
	_itoa_s(variable,Numbers,10);
	DrawNumbers(Numbers,x,y,size,spacing,thickness,r,g,b);
}
void D3DGraphics::DrawWord( const char* Word,int x,int y,int size,int spacing,int thickness,int r,int g,int b )
{
	int xt = thickness;
	int yt = thickness;
	for( int index = 0; Word[index] != '\0'; index++ )
	{
		for( xt = thickness; xt != 0; xt-- )
		{
			for( yt = thickness; yt != 0; yt-- )
			{
				DrawLetter( Word[index],x+xt,y+yt,size,r,g,b);
			}
		}
		if( thickness != 0 && xt == 0 )
		{
			xt = thickness;
		}
		if( thickness != 0 && yt == 0 )
		{
			yt = thickness;
		}
		x = x+size+spacing+thickness;
	}
}
void D3DGraphics::DrawNumbers( const char* Numbers,int x,int y,int size,int spacing,int thickness,int r,int g,int b )
{
	int xt = thickness;
	int yt = thickness;
	for( int index = 0; Numbers[index] != '\0'; index++ )
	{
		for( xt = thickness; xt != 0; xt-- )
		{
			for( yt = thickness; yt != 0; yt-- )
			{
				DrawNumber( Numbers[index],x+xt,y+yt,size,r,g,b);
			}
		}
		if( thickness != 0 && xt == 0 )
		{
			xt = thickness;
		}
		if( thickness != 0 && yt == 0 )
		{
			yt = thickness;
		}
		x = x+size+spacing+thickness;
	}
}
void D3DGraphics::DrawNumber( char Number,int x,int y,int size,int r,int g,int b )
{
	switch( Number )
	{
	case'0':
		DrawCircle( x+(size/2),y+(size/2),size/2,r,g,b );
		break;

	case '1':
		DrawLine( x+(size/2),y,x+(size/2),y+size,r,g,b );
		DrawLine( x+(size/2),y,x,y+(size/4),r,g,b );
		DrawLine( x,y+size,x+size,y+size,r,g,b );
		break;

	case '2':
		DrawHalfCircleUp( x+(size/2),y+(size/2),size/2,r,g,b );
		DrawLine( x+size,y+(size/2),x,y+size,r,g,b );
		DrawLine( x,y+size,x+size,y+size,r,g,b );
		break;

	case '3':
		DrawHalfCircleRight( x+((size*3)/4),y+(size/4),size/4,r,g,b );
		DrawHalfCircleRight( x+((size*3)/4),y+((size*3)/4),size/4,r,g,b );
		DrawLine( x,y,x+((size*3)/4),y,r,g,b );
		DrawLine( x,y+size,x+((size*3)/4),y+size,r,g,b );
		break;

	case '4':
		DrawLine( x+size,y,x,y+((size*3)/4),r,g,b );
		DrawLine( x,y+((size*3)/4),x+size,y+((size*3)/4),r,g,b );
		DrawLine( x+size,y,x+size,y+size,r,g,b );
		break;

	case '5':
		DrawLine( x+size,y,x,y,r,g,b );
		DrawLine( x,y,x,y+(size/2),r,g,b );
		DrawHalfCircleRight( x+((size*3)/4),y+((size*3)/4),size/4,r,g,b );
		DrawLine( x,y+(size/2),x+((size*3)/4),y+(size/2),r,g,b );
		DrawLine( x,y+size,x+((size*3)/4),y+size,r,g,b );
		break;

	case '6':
		DrawHalfCircleLeft( x+(size/2),y+(size/2),size/2,r,g,b );
		DrawLine( x+(size/2),y,x+size,y,r,g,b );
		DrawHalfCircleRight( x+((size*2)/3),y+((size*5)/7),(size/3),r,g,b );
		DrawHalfCircleLeft( x+(size/2),y+((size*5)/7),(size/3),r,g,b );
		break;
		
	case '7':
		DrawLine( x,y,x+size,y,r,g,b );
		DrawLine( x+size,y,x,y+size,r,g,b );
		break;

	case '8':
		DrawHalfCircleLeft( x+(size/4),y+(size/4),size/4,r,g,b );
		DrawHalfCircleRight( x+((size*3)/4),y+(size/4),size/4,r,g,b );
		DrawHalfCircleLeft( x+(size/4),y+((size*3)/4),size/4,r,g,b );
		DrawHalfCircleRight( x+((size*3)/4),y+((size*3)/4),size/4,r,g,b );
		DrawLine( x+(size/4),y,x+((size*3)/4),y,r,g,b );
		DrawLine( x+(size/4),y+size,x+((size*3)/4),y+size,r,g,b );
		DrawLine( x+(size/4),y+(size/2),x+((size*3)/4),y+(size/2),r,g,b );
		break;

	case '9':
		DrawHalfCircleRight( x+(size/2),y+(size/2),size/2,r,g,b );
		DrawLine( x,y+size,x+(size/2),y+size,r,g,b );
		DrawHalfCircleRight( x+(size/2),y+(size/3),(size/3),r,g,b );
		DrawHalfCircleLeft( x+(size/3),y+(size/3),(size/3),r,g,b );
		DrawLine( x+(size/3),y,x+((size*2)/3),y,r,g,b );
		DrawLine( x+(size/3),y+((size*3)/5),x+(size/2),y+((size*3)/5),r,g,b );
		break;

	default:
		return;
	}
}
void D3DGraphics::DrawLetter( char letter,int x,int y,int size,int r,int g,int b )
{
	if( letter > 96 && letter < 123 )
	{
		letter = letter - 32;
	}

	switch( letter )
	{
	case 'A':
	
		DrawLine( x,(y+size),x+(size/2),y,r,g,b);
		DrawLine( x+(size/2),y,(x+size),(y+size),r,g,b);
		DrawLine( x+(size/6),y+((size*7)/10),x+((size*6)/7),y+((size*7)/10),r,g,b);
		break;

	case 'B':
	
		DrawLine( x,y,x,(y+size),r,g,b);
		DrawHalfCircleRight( x+((size*3)/4),y+((size*3)/4),(size/4),r,g,b);
		DrawHalfCircleRight( x+((size*3)/4),y+(size/4),(size/4),r,g,b);
		DrawLine( x,y,x+((size*3)/4),y,r,g,b);
		DrawLine( x,y+size,x+((size*3)/4),y+size,r,g,b);
		DrawLine( x,y+(size/2),x+((size*3)/4),y+(size/2),r,g,b);
		break;

	case 'C':
	
		DrawHalfCircleLeft( x+(size/2),y+(size/2),(size/2),r,g,b);
		DrawLine( x+(size/2),y,x+size,y,r,g,b);
		DrawLine( x+(size/2),y+size,x+size,y+size,r,g,b);
		break;

	case 'D':
	
		DrawHalfCircleRight( x+(size/2),y+(size/2),(size/2),r,g,b);
		DrawLine( x,y,x,y+size,r,g,b);
		DrawLine( x,y,x+(size/2),y,r,g,b);
		DrawLine( x,y+size,x+(size/2),y+size,r,g,b);
		break;

	case 'E':
	
		DrawLine( x,y,x,y+size,r,g,b);
		DrawLine( x,y,x+size,y,r,g,b);
		DrawLine( x,y+(size/2),x+size,y+(size/2),r,g,b);
		DrawLine( x,y+size,x+size,y+size,r,g,b);
		break;

	case 'F':
	
		DrawLine( x,y,x+size,y,r,g,b);
		DrawLine( x,y,x,y+size,r,g,b);
		DrawLine( x,y+(size/2),x+size,y+(size/2),r,g,b);
		break;

	case 'G':
	
		DrawHalfCircleLeft( x+(size/2),y+(size/2),(size/2),r,g,b);
		DrawLine( x+(size/2),y,x+((size*3)/4),y,r,g,b);
		DrawLine( x+(size/2),y+size,x+((size*3)/4),y+size,r,g,b);
		DrawLine( x+((size*3)/4),y+size,x+((size*3)/4),y+(size/2),r,g,b);
		DrawLine( x+((size*3)/4),y+(size/2),x+(size/2),y+(size/2),r,g,b);
		break;

	case 'H':
	
		DrawLine( x,y,x,y+size,r,g,b);
		DrawLine( x+size,y,x+size,y+size,r,g,b);
		DrawLine( x,y+(size/2),x+size,y+(size/2),r,g,b);
		break;

	case 'I':
	
		DrawLine( x,y,x+size,y,r,g,b);
		DrawLine( x,y+size,x+size,y+size,r,g,b);
		DrawLine( x+(size/2),y,x+(size/2),y+size,r,g,b);
		break;

	case 'J':
	
		DrawLine( x,y,x+size,y,r,g,b);
		DrawLine( x+(size/2),y,x+(size/2),y+((size*3)/4),r,g,b);
		DrawHalfCircleDown( x+(size/4),y+((size*3)/4),size/4,r,g,b);
		break;

	case 'K':
	
		DrawLine( x,y,x,y+size,r,g,b);
		DrawLine( x,y+(size/2),x+size,y,r,g,b);
		DrawLine( x,y+(size/2),x+size,y+size,r,g,b);		
		break;

	case 'L':
	
		DrawLine( x,y,x,y+size,r,g,b);
		DrawLine( x,y+size,x+size,y+size,r,g,b);
		break;

	case 'M':
	
		DrawLine( x,y,x,y+size,r,g,b);
		DrawLine( x,y,x+(size/2),y+(size/2),r,g,b);
		DrawLine( x+size,y,x+(size/2),y+(size/2),r,g,b);
		DrawLine( x+size,y,x+size,y+size,r,g,b);
		break;

	case 'N':
	
		DrawLine( x,y,x,y+size,r,g,b);
		DrawLine( x,y,x+size,y+size,r,g,b);
		DrawLine( x+size,y+size,x+size,y,r,g,b);
		break;

	case 'O':
	
		DrawCircle( x+(size/2),y+(size/2),size/2,r,g,b);
		break;

	case 'P':
	
		DrawLine( x,y,x,y+size,r,g,b);		
		DrawHalfCircleRight( x+((size*3)/4),y+(size/4),(size/4),r,g,b);
		DrawLine( x,y,x+((size*3)/4),y,r,g,b);
		DrawLine( x,y+(size/2),x+((size*3)/4),y+(size/2),r,g,b);
		break;

	case 'Q':
	
		DrawCircle( x+(size/2),y+(size/2),size/2,r,g,b);
		DrawLine( x+(size/2),y+(size/2),x+size,y+size,r,g,b);
		break;

	case 'R':
	
		DrawLine( x,y,x,y+size,r,g,b);		
		DrawHalfCircleRight( x+((size*3)/4),y+(size/4),(size/4),r,g,b);
		DrawLine( x,y,x+((size*3)/4),y,r,g,b);
		DrawLine( x,y+(size/2),x+((size*3)/4),y+(size/2),r,g,b);
		DrawLine( x,y+(size/2),x+size,y+size,r,g,b);
		break;

	case 'S':
	
		DrawHalfCircleLeft( x+(size/4),y+(size/4),(size/4),r,g,b);
		DrawLine( x+(size/4),y,x+((size*3)/4),y,r,g,b);
		DrawHalfCircleRight( x+(size/2),y+((size*3)/4),(size/4),r,g,b);
		DrawLine( x+(size/2),y+size,x,y+size,r,g,b);
		DrawLine( x+(size/4),y+(size/2),x+(size/2),y+(size/2),r,g,b);
		break;

	case 'T':
	
		DrawLine( x,y,x+size,y,r,g,b);
		DrawLine( x+(size/2),y,x+(size/2),y+size,r,g,b);
		break;

	case 'U':
	
		DrawHalfCircleDown( x+(size/2),y+(size/2),size/2,r,g,b);
		DrawLine( x,y,x,y+(size/2),r,g,b);
		DrawLine( x+size,y,x+size,y+(size/2),r,g,b);
		break;

	case 'V':
	
		DrawLine( x,y,x+(size/2),y+size,r,g,b);
		DrawLine( x+size,y,x+(size/2),y+size,r,g,b);
		break;

	case 'W':
	
		DrawLine( x,y,x+(size/4),y+size,r,g,b);
		DrawLine( x+(size/4),y+size,x+(size/2),y+(size/2),r,g,b);
		DrawLine( x+(size/2),y+(size/2),x+((size*3)/4),y+size,r,g,b);
		DrawLine( x+((size*3)/4),y+size,x+size,y,r,g,b);
		break;

	case 'X':
	
		DrawLine( x,y,x+size,y+size,r,g,b);
		DrawLine( x+size,y,x,y+size,r,g,b);
		break;

	case 'Y':
	
		DrawLine( x,y,x+(size/2),y+(size/2),r,g,b);
		DrawLine( x+size,y,x+(size/2),y+(size/2),r,g,b);
		DrawLine( x+(size/2),y+size,x+(size/2),y+(size/2),r,g,b);
		break;

	case 'Z':
	
		DrawLine( x,y,x+size,y,r,g,b);
		DrawLine( x+size,y,x,y+size,r,g,b);
		DrawLine( x,y+size,x+size,y+size,r,g,b);
		break;

	default:
		return;
	}
}