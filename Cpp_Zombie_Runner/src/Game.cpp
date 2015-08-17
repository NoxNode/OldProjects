#include "Game.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <float.h>
#include <math.h>

/**********************////////// Constructing/Destructing Functions ////////////////************************/
Game::Game( HWND hWnd,KeyboardServer& kServer,const MouseServer& mServer )
:	gfx( hWnd ),
	audio( hWnd ),
	kbd( kServer ),
	mouse( mServer )
{
	srand( (unsigned int)time( NULL ) );

	SCREENWIDTH = gfx.GetScreenWidth();
	SCREENHEIGHT = gfx.GetScreenHeight();
	
	WORLDWIDTH = SCREENWIDTH;
	WORLDHEIGHT = SCREENHEIGHT;

	SPEED = SCREENWIDTH / 200;

	WORLDWIDTH = 2400;
	WORLDHEIGHT = 1200;
	SPEED = 10;

	//Initialize Struct variables/sprites/sounds
	Initialize();

	soc.StartSock();
}
Game::~Game()
{
	//Free Sprites
	FreeSprite(&Face);
	FreeSprite(&Zombie);
	FreeSprite(&Skull);
	FreeSprite(&Revive);
	FreeSprite(&OneUp);
	FreeSprite(&Coin);
	FreeSprite(&StarFace);
	FreeSprite(&StarPower);
	//Free Sound
	Ding.~Sound();
	Boing.~Sound();
	Star.~Sound();
	Oneup.~Sound();
	Hit.~Sound();
	TenUp.~Sound();
	FiveUp.~Sound();
	//EndSock
	soc.EndSock();
}
void Game::Initialize()
{
	soc.SetCon(false);
	//Game class variables
	
	one.cameraX = 0;
	one.cameraY = 0;

	OnlineSetDefault = true;

	nEnemies = 0;
	GameOver = false;
	Pause = false;
	screenstates = START;
	players = ONE;

	LoadSettings();

	EndConnection = false;

	//Character Struct Variables

	one.x = WORLDWIDTH/3;
	one.y = WORLDHEIGHT/2;
	one.speed = SPEED;
	one.dead = false;
	one.collected_coin = false;
	one.lives = 0;
	one.collected_star = false;

	two.x = (WORLDWIDTH*2)/3;
	two.y = WORLDHEIGHT/2;
	two.speed = SPEED;
	two.dead = false;
	two.collected_coin = false;
	two.lives = 0;
	two.collected_star = false;

	//Item Struct Variables

	coin.x = rand() % (WORLDWIDTH-w_and_h);
	coin.y = rand() % (WORLDHEIGHT-w_and_h);
	
	rev.x = rand() % (WORLDWIDTH-w_and_h);
	rev.y = rand() % (WORLDHEIGHT-w_and_h);

	oneup.x = rand() % (WORLDWIDTH-w_and_h);
	oneup.y = rand() % (WORLDHEIGHT-w_and_h);
	
	star.x = rand() % (WORLDWIDTH-w_and_h);
	star.y = rand() % (WORLDHEIGHT-w_and_h);
	
	confess.x = rand() % (WORLDWIDTH-w_and_h);
	confess.y = rand() % (WORLDHEIGHT-w_and_h);
	
	mass.x = rand() % (WORLDWIDTH-w_and_h);
	mass.y = rand() % (WORLDHEIGHT-w_and_h);

	//Enemy Struct Variables
	
	for( int i = 0; i < NENEMIES; i++ )
	{
		enemy[i].x = rand() % (WORLDHEIGHT-w_and_h);
		enemy[i].y = rand() % (WORLDHEIGHT-w_and_h);
		enemy[i].vx = (rand() % (SPEED*2)) - SPEED;
		enemy[i].vy = (rand() % (SPEED*2)) - SPEED;
	}
	
	//Sprites
	LoadSprite( &Face,"Images/Player.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &Zombie,"Images/Enemy.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &Skull,"Images/DeadPlayer.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &Coin,"Images/Point.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &Revive,"Images/Revive.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &StarPower,"Images/Invulnerability.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &OneUp,"Images/OneUp.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &StarFace,"Images/InvulnerablePlayer.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &Confession,"Images/TenUp.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &Mass,"Images/FiveUp.bmp",w_and_h,w_and_h,D3DCOLOR_XRGB(0,0,0) );
	LoadSprite( &Title,"Images/Title.bmp",TitleX,TitleY,D3DCOLOR_XRGB(0,0,0) );
	//Sounds
	Ding = audio.CreateSound("Sounds/Point.wav");
	Boing = audio.CreateSound("Sounds/Revive.wav");
	Star = audio.CreateSound("Sounds/Invulnerable.wav");
	Oneup = audio.CreateSound("Sounds/OneUp.wav");
	Hit = audio.CreateSound("Sounds/Hit.wav");
	TenUp = audio.CreateSound("Sounds/TenUp.wav");
	FiveUp = audio.CreateSound("Sounds/FiveUp.wav");
	//Buttons
	One.height = 100;
	One.width = 250;
	One.x = SCREENWIDTH/2-255;
	One.y = 190;

	Two.height = 100;
	Two.width = 250;
	Two.x = SCREENWIDTH/2-255;
	Two.y = 300;

	Client.height = 100;
	Client.width = 250;
	Client.x = SCREENWIDTH/2+5;
	Client.y = 190;

	Server.height = 100;
	Server.width = 250;
	Server.x = SCREENWIDTH/2+5;
	Server.y = 300;

	SpeedUp.width = 250;
	SpeedUp.height = 100;
	SpeedUp.x = SCREENWIDTH/2 + 265;
	SpeedUp.y = 190;

	SpeedDown.width = 250;
	SpeedDown.height = 100;
	SpeedDown.x = SCREENWIDTH/2 + 265;
	SpeedDown.y = 300;

	WorldSizeUp.width = 250;
	WorldSizeUp.height = 100;
	WorldSizeUp.x = SCREENWIDTH/2 - 515;
	WorldSizeUp.y = 190;

	WorldSizeDown.width = 250;
	WorldSizeDown.height = 100;
	WorldSizeDown.x = SCREENWIDTH/2 - 515;
	WorldSizeDown.y = 300;
}

/**********************////////// Drawing Functions ////////////////************************/

void Game::DrawMap_and_Instructions()
{
	gfx.DrawRect( 1,1,WORLDWIDTH/20+4,WORLDHEIGHT/20+4,255,255,255 );
	if( players != ONE )
		gfx.DrawSolidRect( two.x/w_and_h+2,two.y/w_and_h+2,two.x/w_and_h+5,two.y/w_and_h+5,0,0,255 );
	gfx.DrawSolidRect( coin.x/w_and_h+2,coin.y/w_and_h+2,coin.x/w_and_h+5,coin.y/w_and_h+5,255,255,0 );
	if( two.dead && players != ONE )
		gfx.DrawSolidRect( rev.x/w_and_h+2,rev.y/w_and_h+2,rev.x/w_and_h+5,rev.y/w_and_h+5,70,130,180 );
	if( one.dead && players == TWO )
		gfx.DrawSolidRect( rev.x/w_and_h+2,rev.y/w_and_h+2,rev.x/w_and_h+5,rev.y/w_and_h+5,70,130,180 );
	gfx.DrawSolidRect( one.x/w_and_h+2,one.y/w_and_h+2,one.x/w_and_h+5,one.y/w_and_h+5,0,255,0 );
	for( int index = 0; index < nEnemies; index++ )
	{
		gfx.DrawSolidRect( enemy[index].x/w_and_h+2,enemy[index].y/w_and_h+2,enemy[index].x/w_and_h+5,enemy[index].y/w_and_h+5,255,0,0 );
	}
	if( players == TWO )
	{
		gfx.DrawLineClipped( -1			- one.cameraX, -1			- one.cameraY, WORLDWIDTH - one.cameraX	, -1			  - one.cameraY,255,255,255,0,0,SCREENWIDTH/2 );
		gfx.DrawLineClipped( -1			- one.cameraX, -1			- one.cameraY, -1		  - one.cameraX	, WORLDHEIGHT - one.cameraY,255,255,255,0,0,SCREENWIDTH/2 );
		gfx.DrawLineClipped( WORLDWIDTH - one.cameraX, -1			- one.cameraY, WORLDWIDTH	  - one.cameraX	, WORLDHEIGHT	  - one.cameraY,255,255,255,0,0,SCREENWIDTH/2 );
		gfx.DrawLineClipped( WORLDWIDTH - one.cameraX, WORLDHEIGHT  - one.cameraY, -1			  - one.cameraX	, WORLDHEIGHT		  - one.cameraY,255,255,255,0,0,SCREENWIDTH/2 );
		
		gfx.DrawLineClipped( -1			- two.cameraX, -1			- two.cameraY, WORLDWIDTH - two.cameraX	, -1			  - two.cameraY,255,255,255,SCREENWIDTH/2 );
		gfx.DrawLineClipped( -1			- two.cameraX, -1			- two.cameraY, -1		  - two.cameraX	, WORLDHEIGHT - two.cameraY,255,255,255,SCREENWIDTH/2 );
		gfx.DrawLineClipped( WORLDWIDTH - two.cameraX, -1			- two.cameraY, WORLDWIDTH	  - two.cameraX	, WORLDHEIGHT	  - two.cameraY,255,255,255,SCREENWIDTH/2 );
		gfx.DrawLineClipped( WORLDWIDTH - two.cameraX, WORLDHEIGHT  - two.cameraY, -1			  - two.cameraX	, WORLDHEIGHT		  - two.cameraY,255,255,255,SCREENWIDTH/2 );
	}
	else
	{
		gfx.DrawLineClipped(0 - one.cameraX, 0 - one.cameraY, WORLDWIDTH - one.cameraX - 1, 0 - one.cameraY, 255, 255, 255);
		gfx.DrawLineClipped(0 - one.cameraX, 0 - one.cameraY, 0 - one.cameraX, WORLDHEIGHT - one.cameraY - 1, 255, 255, 255);
		gfx.DrawLineClipped(WORLDWIDTH - one.cameraX - 1, 0 - one.cameraY, WORLDWIDTH - one.cameraX - 1, WORLDHEIGHT - one.cameraY - 1, 255, 255, 255);
		gfx.DrawLineClipped(WORLDWIDTH - one.cameraX - 1, WORLDHEIGHT - one.cameraY - 1, 0 - one.cameraX, WORLDHEIGHT - one.cameraY - 1, 255, 255, 255);
	}
	if( GameOver )
	{
		gfx.DrawWord( "GAMEOVER",SCREENWIDTH/2-(4 * 15),SCREENHEIGHT/2-15/2-20,10,3,2,0,255,0 );
		gfx.DrawWord( "PRESS SPACE TO RESTART",SCREENWIDTH/2-(11 * 15),SCREENHEIGHT/2-15/2,10,3,2,0,255,0 );
		if( players == ONE || players == TWO )
			gfx.DrawWord( "press b to go back to menu",SCREENWIDTH/2-(13 * 15),SCREENHEIGHT/2+15,10,3,2,0,255,0 );
	}
	if( Pause )
	{
		gfx.DrawWord( "press p to resume",SCREENWIDTH/2-(17 * 15)/2,SCREENHEIGHT/2-15,10,3,2,255,255,255 );
		gfx.DrawWord("press space to restart",SCREENWIDTH/2-(11 * 15),SCREENHEIGHT/2+0,10,3,2,255,255,255 );
		if( players == ONE || players == TWO )
			gfx.DrawWord( "press b to go back to menu",SCREENWIDTH/2-(13 * 15),SCREENHEIGHT/2-30,10,3,2,255,255,255 );
	}
	if( players == TWO )
	{
		int coinquadpointX = (two.x+5)+((coin.x+10-two.x+10)/4)-two.cameraX;
		int coinquadpointY = (two.y+5)+((coin.y+10-two.y+10)/4)-two.cameraY;
		int revquadpointX = (two.x+5)+((rev.x+10-two.x+10)/4)-two.cameraX;
		int revquadpointY = (two.y+5)+((rev.y+10-two.y+10)/4)-two.cameraY;
		gfx.DrawLineClipped( two.x+10-two.cameraX,two.y+10-two.cameraY,coinquadpointX,coinquadpointY,255,255,0,SCREENWIDTH/2 );
		if( one.dead )
			gfx.DrawLineClipped( two.x+10-two.cameraX,two.y+10-two.cameraY,revquadpointX,revquadpointY,0,0,255,SCREENWIDTH/2 );
		
		int onecoinquadpointX = (one.x+5)+((coin.x+10-one.x+10)/4)-one.cameraX;
		int onecoinquadpointY = (one.y+5)+((coin.y+10-one.y+10)/4)-one.cameraY;
		int onerevquadpointX = (one.x+5)+((rev.x+10-one.x+10)/4)-one.cameraX;
		int onerevquadpointY = (one.y+5)+((rev.y+10-one.y+10)/4)-one.cameraY;
		gfx.DrawLineClipped( one.x+10-one.cameraX,one.y+10-one.cameraY,onecoinquadpointX,onecoinquadpointY,255,255,0,0,0,SCREENWIDTH/2 );
		if( two.dead )
			gfx.DrawLineClipped( one.x+10-one.cameraX,one.y+10-one.cameraY,onerevquadpointX,onerevquadpointY,0,0,255,0,0,SCREENWIDTH/2 );
	}
	else
	{
		int coinquadpointX = (one.x+5)+((coin.x+10-one.x+10)/4)-one.cameraX;
		int coinquadpointY = (one.y+5)+((coin.y+10-one.y+10)/4)-one.cameraY;
		int revquadpointX = (one.x+5)+((rev.x+10-one.x+10)/4)-one.cameraX;
		int revquadpointY = (one.y+5)+((rev.y+10-one.y+10)/4)-one.cameraY;
		gfx.DrawLineClipped( one.x+10-one.cameraX,one.y+10-one.cameraY,coinquadpointX,coinquadpointY,255,255,0 );
		if( two.dead )
			gfx.DrawLineClipped( one.x+10-one.cameraX,one.y+10-one.cameraY,revquadpointX,revquadpointY,0,0,255 );
	}
	if( nEnemies == 0 )
	{
		gfx.DrawWord( "SEE THE README DOC FOR THE CONTROLS",SCREENWIDTH/2-(35 * 15)/2,(SCREENHEIGHT*2)/3,10,3,2,255,255,255 );
	}
}
void Game::DrawClippedSprite( short refx,short refy,short x,short y,Sprite* sprite,int minx,int miny,int maxx,int maxy )
{
	if( players == TWO )
	{
		if( minx == 0 )
		{
			if( x + w_and_h < refx + w_and_h/2 + SCREENWIDTH/4 && x > refx + w_and_h/2 - SCREENWIDTH/4
				&& y + w_and_h < refy + w_and_h/2 + SCREENHEIGHT/2 && y > refy + w_and_h/2 - SCREENHEIGHT/2 )
				gfx.DrawSprite( x - one.cameraX,y - one.cameraY,sprite );

			else if( x < refx + w_and_h/2 + SCREENWIDTH/4 && x - w_and_h < refx + w_and_h/2 - SCREENWIDTH/4
				&& y < refy + w_and_h/2 + SCREENHEIGHT/2 && y - w_and_h > refy + w_and_h/2 - SCREENHEIGHT/2 )
				gfx.DrawSpriteClipped( x-one.cameraX,y-one.cameraY,sprite,minx,miny,maxx,maxy );
		}
		else
		{
			if( x + w_and_h < refx + w_and_h/2 + SCREENWIDTH/4 && x > refx + w_and_h/2 - SCREENWIDTH/4
				&& y + w_and_h < refy + w_and_h/2 + SCREENHEIGHT/2 && y > refy + w_and_h/2 - SCREENHEIGHT/2 )
				gfx.DrawSprite( x - two.cameraX,y - two.cameraY,sprite );

			else if( x < refx + w_and_h/2 + SCREENWIDTH/4 && x - w_and_h < refx + w_and_h/2 - SCREENWIDTH/4
				&& y < refy + w_and_h/2 + SCREENHEIGHT/2 && y - w_and_h > refy + w_and_h/2 - SCREENHEIGHT/2 )
				gfx.DrawSpriteClipped( x-two.cameraX,y-two.cameraY,sprite,minx,miny,maxx,maxy );
		}
	}
	else
	{
		if( x + w_and_h < refx + w_and_h/2 + SCREENWIDTH/2 && x > refx + w_and_h/2 - SCREENWIDTH/2
			&& y + w_and_h < refy + w_and_h/2 + SCREENHEIGHT/2 && y > refy + w_and_h/2 - SCREENHEIGHT/2 )
			gfx.DrawSprite( x-one.cameraX,y-one.cameraY,sprite );

		else if( x < refx + w_and_h/2 + SCREENWIDTH/2 && x + w_and_h > refx - w_and_h/2 - SCREENWIDTH/2
			&& y < refy + w_and_h/2 + SCREENHEIGHT/2 && y + w_and_h > refy - w_and_h/2 - SCREENHEIGHT/2 )
			gfx.DrawSpriteClipped( x-one.cameraX,y-one.cameraY,sprite );
	}
}
void Game::DrawMenu()
{
	gfx.DrawWord( "players",SCREENWIDTH/2 - ( 7 * 7 ),50,10,3,2,255,255,255 );

	gfx.DrawRect( SCREENWIDTH/2-255,85,SCREENWIDTH/2-5,85+100,255,255,255 );
	gfx.DrawWord( "offline",SCREENWIDTH/2-130 - ( 7 * 7 ),125,10,3,2,255,0,0 );
	if( players == ONE )
		gfx.DrawRect( SCREENWIDTH/2-255,190,SCREENWIDTH/2-5,190+100,0,255,0 );
	else
		gfx.DrawRect( SCREENWIDTH/2-255,190,SCREENWIDTH/2-5,190+100,255,255,255 );
	gfx.DrawWord( "one",SCREENWIDTH/2-130 - ( 3 * 7 ),225,10,3,2,255,255,255 );
	if( players == TWO )
		gfx.DrawRect( SCREENWIDTH/2-255,300,SCREENWIDTH/2-5,300+100,0,255,0 );
	else
		gfx.DrawRect( SCREENWIDTH/2-255,300,SCREENWIDTH/2-5,300+100,255,255,255 );
	gfx.DrawWord( "two",SCREENWIDTH/2-130 - ( 3 * 7 ),340,10,3,2,255,255,255 );
		
	gfx.DrawWord( "online",SCREENWIDTH/2+130 - ( 3 * 15 ),125,10,3,2,255,0,0 );
	gfx.DrawRect( SCREENWIDTH/2+5,85,SCREENWIDTH/2+255,85+100,255,255,255 );
	gfx.DrawWord( "client",SCREENWIDTH/2+130 - ( 3 * 15 ),225,10,3,2,255,255,255 );
	if( players == CLIENT )
		gfx.DrawRect( SCREENWIDTH/2+5,190,SCREENWIDTH/2+255,190+100,0,255,0 );
	else
		gfx.DrawRect( SCREENWIDTH/2+5,190,SCREENWIDTH/2+255,190+100,255,255,255 );
	gfx.DrawWord( "server",SCREENWIDTH/2+130 - ( 3 * 15 ),340,10,3,2,255,255,255 );
	if( players == SERVER )
		gfx.DrawRect( SCREENWIDTH/2+5,300,SCREENWIDTH/2+255,300+100,0,255,0 );
	else
		gfx.DrawRect( SCREENWIDTH/2+5,300,SCREENWIDTH/2+255,300+100,255,255,255 );
				
	gfx.DrawWord( "speed up",SCREENWIDTH/2+380 - ( 4 * 15 ),225,10,3,2,255,255,255 );
	gfx.DrawRect( SCREENWIDTH/2+265,190,SCREENWIDTH/2+515,190+100,255,255,255 );
	gfx.DrawWord( "speed down",SCREENWIDTH/2+380 - ( 5 * 15 ),340,10,3,2,255,255,255 );
	gfx.DrawRect( SCREENWIDTH/2+265,300,SCREENWIDTH/2+515,300+100,255,255,255 );
	gfx.DrawVariable( SPEED,SCREENWIDTH/2+265+100,140,10,3,2,255,255,255 );

	gfx.DrawWord( "worldsize up",SCREENWIDTH/2-380 - ( 6 * 15 ),225,10,3,2,255,255,255 );
	gfx.DrawRect( SCREENWIDTH/2-265,190,SCREENWIDTH/2-515,190+100,255,255,255 );
	gfx.DrawWord( "worldsize down",SCREENWIDTH/2-380 - ( 7 * 15 ),340,10,3,2,255,255,255 );
	gfx.DrawRect( SCREENWIDTH/2-265,300,SCREENWIDTH/2-515,300+100,255,255,255 );
	gfx.DrawVariable( WORLDWIDTH,SCREENWIDTH/2-265-150,130,10,3,2,255,255,255 );
	gfx.DrawVariable( WORLDHEIGHT,SCREENWIDTH/2-265-150,150,10,3,2,255,255,255 );

	if( (players == SERVER || players == CLIENT) && !OnlineSetDefault )
	{
		gfx.DrawWord( "online requires the default size and speed",SCREENWIDTH/2 - (22 * 15 ),SCREENHEIGHT - 150,10,3,2,255,255,255 );
		gfx.DrawNumbers( "2400   1200     10",SCREENWIDTH/2 - (9 * 15 ),SCREENHEIGHT - 130,10,3,2,255,255,255 );
		gfx.DrawWord(    "     x      and   ",SCREENWIDTH/2 - (9 * 15 ),SCREENHEIGHT - 130,10,3,2,255,255,255 );
	}
	gfx.DrawWord( "after you click a player setting, press space to continue",SCREENWIDTH/2-( 57 * 5 ),SCREENHEIGHT-100,7,2,1,255,255,255 );
}
void Game::DrawSocketErrors()
{
	if( (players == CLIENT || players == SERVER) )
	{
		if( WSAGetLastError() == 10054 && players == CLIENT )
		{
			if( onlinetimer.Time(120 ) )
				EndConnection = true;
			gfx.DrawWord( "Server closed",5,5,10,3,2,255,255,255 );
			gfx.DrawWord( "Closing in   seconds",5,20,10,3,2,255,255,255 );
			gfx.DrawNumbers( "2",5 + (11 * 15),20,10,3,2,255,255,255 );
		}
		else if( WSAGetLastError() == 10053 && players == SERVER )
		{
			if( onlinetimer.Time(120 ) )
				EndConnection = true;
			gfx.DrawWord( "client closed",5,5,10,3,2,255,255,255 );
			gfx.DrawWord( "Closing in   seconds",5,20,10,3,2,255,255,255 );
			gfx.DrawNumbers( "2",5 + (11 * 15),20,10,3,2,255,255,255 );
		}
		else if( soc.GetErrorCode() == 10061 && players == CLIENT )
		{
			if( onlinetimer.Time(120 ) )
				EndConnection = true;
			gfx.DrawWord( "Server Not Available",5,5,10,3,2,255,255,255 );
			gfx.DrawWord( "Closing in   seconds",5,20,10,3,2,255,255,255 );
			gfx.DrawNumbers( "2",5 + (11 * 15),20,10,3,2,255,255,255 );
		}

		else if( soc.GetError() ) //If there was an error starting,sending or recieving with the socket, stop the game and show the error code
		{
			gfx.DrawWord( "windows socket error code",5,5,10,3,2,255,255,255 );
			gfx.DrawVariable( soc.GetErrorCode(),5,20,10,3,2,255,255,255 );
			gfx.DrawWord( "occurred",5,40,10,3,2,255,255,255 );
			if( onlinetimer.Time(120 ) )
				EndConnection = true;
			gfx.DrawWord( "Closing in   seconds",5,60,10,3,2,255,255,255 );
			gfx.DrawNumbers( "2",5 + (11 * 15),60,10,3,2,255,255,255 );
		}
		if( !( soc.GetCon() || soc.GetError() ) && players == CLIENT && OnlineSetDefault )
			gfx.DrawWord( "connecting",5,5,10,3,2,255,255,255 );
		if( !( soc.GetCon() || soc.GetError() ) && players == SERVER && OnlineSetDefault )
			gfx.DrawWord( "waiting for a connection",5,5,10,3,2,255,255,255 );
	}
}
void Game::DrawOnePlayerGame()
{
	if (WORLDWIDTH != SCREENWIDTH) {
		//Coin
		gfx.DrawSpriteClipped(coin.x - one.cameraX, coin.y - one.cameraY, &Coin);
		//Revive
		if (two.dead && players != ONE)
			gfx.DrawSpriteClipped(rev.x - one.cameraX, rev.y - one.cameraY, &Revive);
		//Player One
		if (!one.dead)
			gfx.DrawSprite(one.x - one.cameraX, one.y - one.cameraY, &Face);
		if (one.dead)
			gfx.DrawSprite(one.x - one.cameraX, one.y - one.cameraY, &Skull);
		//Player Two
		if (!two.dead && players != ONE)
			gfx.DrawSpriteClipped(two.x - one.cameraX, two.y - one.cameraY, &Face);
		if (two.dead && players != ONE)
			gfx.DrawSpriteClipped(two.x - one.cameraX, two.y - one.cameraY, &Skull);
		//Enemies
		for (int i = 0; i < nEnemies; i++)
		{
			DrawClippedSprite(one.x, one.y, enemy[i].x, enemy[i].y, &Zombie);
		}
		//StarFace
		if (one.collected_star && startimer.GetTimer() < (HolyWaterDuration * 3) / 4)
			gfx.DrawSprite(one.x - one.cameraX, one.y - one.cameraY, &StarFace);
		if (one.collected_star && startimer.GetTimer() > (HolyWaterDuration * 3) / 4 && startimer.GetTimer() % 10 < 5)
			gfx.DrawSprite(one.x - one.cameraX, one.y - one.cameraY, &StarFace);
		//Items
		if (players == ONE && nEnemies != 0)
		{
			//Star
			if (nEnemies % star.level == 0 && !star.collected)
				DrawClippedSprite(one.x, one.y, star.x, star.y, &StarPower);
			//Oneup
			if (nEnemies % oneup.level == 0 && !oneup.collected)
				DrawClippedSprite(one.x, one.y, oneup.x, oneup.y, &OneUp);
			//Confession
			if (nEnemies % confess.level == 0 && !confess.collected)
				DrawClippedSprite(one.x, one.y, confess.x, confess.y, &Confession);
			//Mass
			if (nEnemies % mass.level == 0 && !mass.collected)
				DrawClippedSprite(one.x, one.y, mass.x, mass.y, &Mass);
		}
	}
	else {

		//Coin
		gfx.DrawSprite(coin.x - one.cameraX, coin.y - one.cameraY, &Coin);
		//Revive
		if (two.dead && players != ONE)
			gfx.DrawSprite(rev.x - one.cameraX, rev.y - one.cameraY, &Revive);
		//Player One
		if (!one.dead)
			gfx.DrawSprite(one.x - one.cameraX, one.y - one.cameraY, &Face);
		if (one.dead)
			gfx.DrawSprite(one.x - one.cameraX, one.y - one.cameraY, &Skull);
		//Player Two
		if (!two.dead && players != ONE)
			gfx.DrawSprite(two.x - one.cameraX, two.y - one.cameraY, &Face);
		if (two.dead && players != ONE)
			gfx.DrawSprite(two.x - one.cameraX, two.y - one.cameraY, &Skull);
		//Enemies
		for (int i = 0; i < nEnemies; i++)
		{
			gfx.DrawSprite(enemy[i].x, enemy[i].y, &Zombie);
		}
		//StarFace
		if (one.collected_star && startimer.GetTimer() < (HolyWaterDuration * 3) / 4)
			gfx.DrawSprite(one.x - one.cameraX, one.y - one.cameraY, &StarFace);
		if (one.collected_star && startimer.GetTimer() > (HolyWaterDuration * 3) / 4 && startimer.GetTimer() % 10 < 5)
			gfx.DrawSprite(one.x - one.cameraX, one.y - one.cameraY, &StarFace);
		//Items
		if (players == ONE && nEnemies != 0)
		{
			//Star
			if (nEnemies % star.level == 0 && !star.collected)
				gfx.DrawSprite(star.x, star.y, &StarPower);
			//Oneup
			if (nEnemies % oneup.level == 0 && !oneup.collected)
				gfx.DrawSprite(oneup.x, oneup.y, &OneUp);
			//Confession
			if (nEnemies % confess.level == 0 && !confess.collected)
				gfx.DrawSprite(confess.x, confess.y, &Confession);
			//Mass
			if (nEnemies % mass.level == 0 && !mass.collected)
				gfx.DrawSprite(mass.x, mass.y, &Mass);
		}
	}
	gfx.DrawWord( "lives",5,SCREENHEIGHT-20,10,3,2,255,255,255 );
	gfx.DrawVariable( one.lives,5+(5 * 15)+10,SCREENHEIGHT-20,10,3,2,255,255,255 );
}
void Game::DrawTwoPlayerGame()
{
	gfx.DrawLine( SCREENWIDTH/2,0,SCREENWIDTH/2,SCREENHEIGHT-1,255,255,255 );

	//Player Two

	//Coin
	gfx.DrawSpriteClipped( coin.x-two.cameraX,coin.y-two.cameraY,&Coin,SCREENWIDTH/2 );
	//Revive
	if( one.dead )
		gfx.DrawSpriteClipped( rev.x-two.cameraX,rev.y-two.cameraY,&Revive,SCREENWIDTH/2 );
	//Player Two
	if( !two.dead )
		gfx.DrawSprite( two.x-two.cameraX,two.y-two.cameraY,&Face );
	if( two.dead )
		gfx.DrawSprite( two.x-two.cameraX,two.y-two.cameraY,&Skull );
	//Player One
	if( !one.dead )
		gfx.DrawSpriteClipped( one.x-two.cameraX,one.y-two.cameraY,&Face,SCREENWIDTH/2 );
	if( one.dead )
		gfx.DrawSpriteClipped( one.x-two.cameraX,one.y-two.cameraY,&Skull,SCREENWIDTH/2 );
	//Enemies
	for( int i = 0; i < nEnemies; i++ )
	{
		DrawClippedSprite( two.x,two.y,enemy[i].x,enemy[i].y,&Zombie,SCREENWIDTH/2 );
	}
	//StarFace
	if( one.collected_star && startimer.GetTimer() < (HolyWaterDuration*3)/4 )
		gfx.DrawSprite( two.x-two.cameraX,two.y-two.cameraY,&StarFace );
		if( one.collected_star && startimer.GetTimer() > (HolyWaterDuration*3)/4 && startimer.GetTimer() % 10 < 5 )
			gfx.DrawSprite( two.x-two.cameraX,two.y-two.cameraY,&StarFace );
	//Items
	if( nEnemies != 0 )
	{
		//Star
		if( nEnemies % star.level == 0 && !star.collected )
			DrawClippedSprite( two.x,two.y,star.x,star.y,&StarPower,SCREENWIDTH/2 );
		//Oneup
		if( nEnemies % oneup.level == 0 && !oneup.collected )
			DrawClippedSprite( two.x,two.y,oneup.x,oneup.y,&OneUp,SCREENWIDTH/2 );
		//Confession
		if( nEnemies % confess.level == 0 && !confess.collected )
			DrawClippedSprite( two.x,two.y,confess.x,confess.y,&Confession,SCREENWIDTH/2 );
		//Mass
		if( nEnemies % mass.level == 0 && !mass.collected )
			DrawClippedSprite( two.x,two.y,mass.x,mass.y,&Mass,SCREENWIDTH/2 );
	}
	gfx.DrawWord( "two",SCREENWIDTH-5-(3 * 15),SCREENHEIGHT-35,10,3,2,255,255,255 );
	gfx.DrawWord( "lives",SCREENWIDTH-(5+(5 * 15)+10+(2 * 15)+10),SCREENHEIGHT-20,10,3,2,255,255,255 );
	gfx.DrawVariable( two.lives,SCREENWIDTH-(5+(2 * 15)+10),SCREENHEIGHT-20,10,3,2,255,255,255 );

	//Player One

	//Coin
	gfx.DrawSpriteClipped( coin.x-one.cameraX,coin.y-one.cameraY,&Coin,0,0,SCREENWIDTH/2 );
	//Revive
	if( two.dead )
		gfx.DrawSpriteClipped( rev.x-one.cameraX,rev.y-one.cameraY,&Revive,0,0,SCREENWIDTH/2 );
	//Player One
	if( !one.dead )
		gfx.DrawSprite( one.x-one.cameraX,one.y-one.cameraY,&Face );
	if( one.dead )
		gfx.DrawSprite( one.x-one.cameraX,one.y-one.cameraY,&Skull );
	//Player Two
	if( !two.dead && players != ONE )
		gfx.DrawSpriteClipped( two.x-one.cameraX,two.y-one.cameraY,&Face,0,0,SCREENWIDTH/2 );
	if( two.dead && players != ONE )
		gfx.DrawSpriteClipped( two.x-one.cameraX,two.y-one.cameraY,&Skull,0,0,SCREENWIDTH/2 );
	//Enemies
	for( int i = 0; i < nEnemies; i++ )
	{
		DrawClippedSprite( one.x,one.y,enemy[i].x,enemy[i].y,&Zombie,0,0,SCREENWIDTH/2 );
	}
	//StarFace
	if( one.collected_star && startimer.GetTimer() < (HolyWaterDuration*3)/4 )
		gfx.DrawSprite( one.x-one.cameraX,one.y-one.cameraY,&StarFace );
		if( one.collected_star && startimer.GetTimer() > (HolyWaterDuration*3)/4 && startimer.GetTimer() % 10 < 5 )
		gfx.DrawSprite( one.x-one.cameraX,one.y-one.cameraY,&StarFace );
	//Items
	if( nEnemies != 0 )
	{
		//Star
		if( nEnemies % star.level == 0 && !star.collected )
			DrawClippedSprite( one.x,one.y,star.x,star.y,&StarPower );
		//Oneup
		if( nEnemies % oneup.level == 0 && !oneup.collected )
			DrawClippedSprite( one.x,one.y,oneup.x,oneup.y,&OneUp );
		//Confession
		if( nEnemies % confess.level == 0 && !confess.collected )
			DrawClippedSprite( one.x,one.y,confess.x,confess.y,&Confession );
		//Mass
		if( nEnemies % mass.level == 0 && !mass.collected )
			DrawClippedSprite( one.x,one.y,mass.x,mass.y,&Mass );
	}
	gfx.DrawWord( "one",5,SCREENHEIGHT-35,10,3,2,255,255,255 );
	gfx.DrawWord( "Lives",5,SCREENHEIGHT-20,10,3,2,255,255,255 );
	gfx.DrawVariable( one.lives,5+(5 * 15)+10,SCREENHEIGHT-20,10,3,2,255,255,255 );
}

/**********************////////// Updating Functions ////////////////************************/

void Game::UpdateEnemy()
{
	lotto = rand() % NENEMIES;
	static bool LastHit[2] = {false};
	static short LastHitIndex[2] = {0};
	for( int index = 0; index < nEnemies; index++ )
	{
		if( index == lotto && players != CLIENT )
		{
			enemy[index].vx = (rand() % (SPEED*2)) - SPEED;
			enemy[index].vy = (rand() % (SPEED*2)) - SPEED;
		}
		enemy[ index ].x += (enemy[ index ].vx);
		enemy[ index ].y += (enemy[ index ].vy);

		if( enemy[ index ].x < 0 )
		{
			enemy[ index ].x = 0;
			enemy[ index ].vx = -enemy[ index ].vx;
		}
		else if( enemy[ index ].x > WORLDWIDTH - w_and_h )
		{
			enemy[ index ].x = WORLDWIDTH - w_and_h;
			enemy[ index ].vx = -enemy[ index ].vx;
		}
		if( enemy[ index ].y < 0 )
		{
			enemy[ index ].y = 0;
			enemy[ index ].vy = -enemy[ index ].vy;
		}
		else if( enemy[ index ].y > WORLDHEIGHT - w_and_h )
		{
			enemy[ index ].y = WORLDHEIGHT - w_and_h;
			enemy[ index ].vy = -enemy[ index ].vy;
		}	

		if( !one.dead && !one.collected_star && collision.CircCollision( one.x,one.y,w_and_h/2,enemy[ index ].x,enemy[ index ].y,w_and_h/2 ) && !LastHit[1] )
		{
			if( one.lives != 0 )
			{
				one.lives--;
				LastHit[1] = true;
				LastHitIndex[1] = index;
				Hit.Play();
			}
			else
			{
				one.dead = true;
				Hit.Play();
			}
		}
		if( !two.dead && !two.collected_star && players == TWO && collision.CircCollision( two.x,two.y,w_and_h/2,enemy[ index ].x,enemy[ index ].y,w_and_h/2 ) && !LastHit[2] )
		{
			if( two.lives != 0 )
			{
				two.lives--;
				LastHit[2] = true;
				LastHitIndex[2] = index;
				Hit.Play();
			}
			else
			{
				two.dead = true;
				Hit.Play();
			}
		}
	}
	if( LastHit[1] && !collision.CircCollision( one.x,one.y,w_and_h/2,enemy[ LastHitIndex[1] ].x,enemy[ LastHitIndex[1] ].y,w_and_h/2 ) )
		LastHit[1] = false;
	if( LastHit[2] && !collision.CircCollision( two.x,two.y,w_and_h/2,enemy[ LastHitIndex[2] ].x,enemy[ LastHitIndex[2] ].y,w_and_h/2 ) )
		LastHit[2] = false;
}
void Game::UpdateMenu()
{
	if( mouse.ButtonIsPressed( One,true ) )
		players = ONE;
	if( mouse.ButtonIsPressed( Two,true ) )
		players = TWO;
	if( mouse.ButtonIsPressed( Client,true ) )
		players = CLIENT;
	if( mouse.ButtonIsPressed( Server,true ) )
		players = SERVER;
	if( mouse.ButtonIsPressed( SpeedUp,true,true ) )
		SPEED++;
	if( mouse.ButtonIsPressed( SpeedDown,true,true ) )
		SPEED--;
	if( mouse.ButtonIsPressed( WorldSizeUp,true,true ) )
	{
		WORLDWIDTH+=200;
		WORLDHEIGHT+=100;
	}
	if( mouse.ButtonIsPressed( WorldSizeDown,true,true ) )
	{
		WORLDWIDTH-=200;
		WORLDHEIGHT-=100;
	}
	update.LimitVar( &SPEED,20,1 );
	update.LimitVar( &WORLDWIDTH,20000,1000 );
	update.LimitVar( &WORLDHEIGHT,10000,500 );
	if( (SPEED != 10 || WORLDWIDTH != 2400 || WORLDHEIGHT != 1200) )
		OnlineSetDefault = false;
	else
		OnlineSetDefault = true;
	if( kbd.KeyIsPressed_OneShot(' ') )
	{
		coin.x = rand() % (WORLDWIDTH-w_and_h);
		coin.y = rand() % (WORLDHEIGHT-w_and_h);
	
		rev.x = rand() % (WORLDWIDTH-w_and_h);
		rev.y = rand() % (WORLDHEIGHT-w_and_h);

		oneup.x = rand() % (WORLDWIDTH-w_and_h);
		oneup.y = rand() % (WORLDHEIGHT-w_and_h);
	
		star.x = rand() % (WORLDWIDTH-w_and_h);
		star.y = rand() % (WORLDHEIGHT-w_and_h);

		confess.x = rand() % (WORLDWIDTH-w_and_h);
		confess.y = rand() % (WORLDHEIGHT-w_and_h);

		mass.x = rand() % (WORLDWIDTH-w_and_h);
		mass.y = rand() % (WORLDHEIGHT-w_and_h);
	
		for( int i = 0; i < NENEMIES; i++ )
		{
			enemy[i].x = rand() % (WORLDHEIGHT-w_and_h);
			enemy[i].y = rand() % (WORLDHEIGHT-w_and_h);
			enemy[i].vx = (rand() % (SPEED*2)) - SPEED;
			enemy[i].vy = (rand() % (SPEED*2)) - SPEED;
		}
			
		one.x = WORLDWIDTH/3;
		if( players == SERVER )
			one.x = (WORLDWIDTH*2)/3;
		else
			two.x = (WORLDWIDTH*2)/3;
		two.y = WORLDHEIGHT/2;
		one.y = WORLDHEIGHT/2;
		one.speed = SPEED;
		two.speed = SPEED;

		if( (players == SERVER || players == CLIENT) && OnlineSetDefault )
			screenstates = GAME;
		if( !(players == SERVER || players == CLIENT ) )
			screenstates = GAME;
	}
}
void Game::UpdateGame()
{
	static bool Revived = false;
	//initialize if server
	static bool initialize = true;
	if( players == SERVER && initialize )
	{
		one.x = (WORLDWIDTH*2)/3;
		initialize = false;
	}
	if( players == CLIENT )
	{
		//Player
		soc.sendbuf[0] = one.dead+1;
		soc.sendbuf[1] = update.GetDigit(one.x,1)+(update.GetDigit(one.x,10))*10+1;
		soc.sendbuf[2] = update.GetDigit(one.x,100)+(update.GetDigit(one.x,1000))*10+1;
		soc.sendbuf[3] = update.GetDigit(one.y,1)+(update.GetDigit(one.y,10))*10+1;
		soc.sendbuf[4] = update.GetDigit(one.y,100)+(update.GetDigit(one.y,1000))*10+1;

		soc.sendbuf[5] = one.collected_coin+1;
		soc.sendbuf[6] = Revived+1;
		soc.sendbuf[7] = Pause+1;
		//end buffer
		soc.sendbuf[8] = 0;

		soc.Send(soc.sendbuf,sizeof(soc.sendbuf));
		soc.Recieve(soc.recvbuf,sizeof(soc.recvbuf));

		//Player
		two.dead = soc.recvbuf[0]-1;
		two.x = soc.recvbuf[1] + (soc.recvbuf[2]-1)*100-1;
		two.y = soc.recvbuf[3] + (soc.recvbuf[4]-1)*100-1;
		//Coin XY
		coin.x = soc.recvbuf[5] + (soc.recvbuf[6]-1)*100-1;
		coin.y = soc.recvbuf[7] + (soc.recvbuf[8]-1)*100-1;

		two.collected_coin = soc.recvbuf[15]-1;
		if( (soc.recvbuf[16]-1) == true )
		{
			one.dead = false;
			Boing.Play();
		}
		Pause = soc.recvbuf[17]-1;
		short lotto = soc.recvbuf[18]-1;
		enemy[lotto].vx = soc.recvbuf[19]-1;
		enemy[lotto].vy = soc.recvbuf[20]-1;
		//end buffer
		soc.recvbuf[21] = 0;
	}
	if( players == SERVER )
	{
		soc.Recieve(soc.recvbuf,sizeof(soc.recvbuf));

		two.dead = soc.recvbuf[0]-1;
		two.x = soc.recvbuf[1] + (soc.recvbuf[2]-1)*100-1;
		two.y = soc.recvbuf[3] + (soc.recvbuf[4]-1)*100-1;
		
		two.collected_coin = soc.recvbuf[5]-1;
		if( (soc.recvbuf[6]-1) == true )
		{
			one.dead = false;
			Boing.Play();
		}
		Pause = soc.recvbuf[7]-1;
		//end buffer
		soc.recvbuf[8] = 0;
	}
		
	if( GameOver && !two.dead && players != ONE )
	{
		GameOver = false;
		one.x = WORLDWIDTH/3;
		one.y = WORLDHEIGHT/2;
		if( players == SERVER )
			one.x = (WORLDWIDTH*2)/3;
		one.dead = false;
		nEnemies = 0;
	}

	//Game
	//Only update when the game isn't over, there isn't a socket error, and the game isn't paused
	if( !GameOver && !soc.GetError() && !Pause )
	{
		//Pause
		if( kbd.KeyIsPressed_OneShot('P') )
			Pause = true;
		//Controls
		if( players != TWO )
		{
			if( !one.dead )
			{
				kbd.IncDecVar_On_Keypress( &one.x,39,37,'D','A',one.speed );
				kbd.IncDecVar_On_Keypress( &one.y,40,38,'S','W',one.speed );
			}
			update.LimitVar( &one.x,WORLDWIDTH-w_and_h );
			update.LimitVar( &one.y,WORLDHEIGHT-w_and_h );
		}
			
		if( players == ONE || players == TWO )
		{
			//Coin
			if( collision.CircCollision( one.x,one.y,w_and_h/2,coin.x,coin.y,w_and_h/2) )
			{
				nEnemies++;
				coin.x = rand() % (WORLDWIDTH-w_and_h);
				coin.y = rand() % (WORLDHEIGHT-w_and_h);
				if( players == ONE && nEnemies != 0 )
				{
					if( one.x < WORLDWIDTH/2 )
					{
						enemy[nEnemies-1].x = WORLDWIDTH - w_and_h;
						enemy[nEnemies-1].y = WORLDHEIGHT - w_and_h;
					}
					else
					{
						enemy[nEnemies-1].x = 0;
						enemy[nEnemies-1].y = 0;
					}
				}
				else if( nEnemies != 0 )
				{
					if( !(collision.RectCollision( two.x,two.y,20,20,WORLDWIDTH/2,WORLDHEIGHT/2,WORLDWIDTH/2,WORLDHEIGHT/2 ) ||
						collision.RectCollision( one.x,one.y,20,20,WORLDWIDTH/2,WORLDHEIGHT/2,WORLDWIDTH/2,WORLDHEIGHT/2 )) )
					{
						enemy[nEnemies-1].x = WORLDWIDTH - w_and_h;
						enemy[nEnemies-1].y = WORLDHEIGHT - w_and_h;
					}
					else if( !(collision.RectCollision( two.x,two.y,20,20,0,0,WORLDWIDTH/2,WORLDHEIGHT/2 ) ||
						collision.RectCollision( one.x,one.y,20,20,0,0,WORLDWIDTH/2,WORLDHEIGHT/2 )) )
					{
						enemy[nEnemies-1].x = 0;
						enemy[nEnemies-1].y = 0;
					}
					else
					{
						enemy[nEnemies-1].x = WORLDWIDTH - w_and_h;
						enemy[nEnemies-1].y = 0;						
					}
				}
				Ding.Play();
			}
			//Items
			if( nEnemies != 0 )
			{
				if( nEnemies % oneup.level == 0 )
				{
					if( oneup.appear.Time( oneup.appearance ) )
						oneup.collected = true;
				}
				else
				{
					oneup.collected = false;
					oneup.appear.SetTimer(0);
				}
				if( nEnemies % star.level == 0 )
				{
					if( star.appear.Time( star.appearance ) )
						star.collected = true;
				}
				else
				{
					star.collected = false;
					star.appear.SetTimer(0);
				}
				if( nEnemies % confess.level == 0 )
				{
					if( confess.appear.Time( confess.appearance ) )
						confess.collected = true;
				}
				else
				{
					confess.collected = false;
					confess.appear.SetTimer(0);
				}
				if( nEnemies % mass.level == 0 )
				{
					if( mass.appear.Time( mass.appearance ) )
						mass.collected = true;
				}
				else
				{
					mass.collected = false;
					mass.appear.SetTimer(0);
				}
				//Star
				if( nEnemies % star.level == 0 && !star.collected )
					if( collision.RectCircCollision( star.x,star.y,w_and_h,w_and_h,one.x+w_and_h/2,one.y+w_and_h/2,w_and_h/2 ) )
					{
						one.collected_star = true;
						startimer.SetTimer(0);
						star.x = rand() % (WORLDWIDTH-w_and_h);
						star.y = rand() % (WORLDHEIGHT-w_and_h);
						Star.Play();
						star.collected = true;
					}
				//Oneup
				if( nEnemies % oneup.level == 0 && !oneup.collected )
					if( collision.RectCircCollision( oneup.x,oneup.y,w_and_h,w_and_h,one.x+w_and_h/2,one.y+w_and_h/2,w_and_h/2 ) )
					{
						one.lives++;
						oneup.x = rand() % (WORLDWIDTH-w_and_h);
						oneup.y = rand() % (WORLDHEIGHT-w_and_h);
						Oneup.Play();
						oneup.collected = true;
					}
				//Confession
				if( nEnemies % confess.level == 0 && !confess.collected )
					if( collision.RectCircCollision( confess.x,confess.y,w_and_h,w_and_h,one.x+w_and_h/2,one.y+w_and_h/2,w_and_h/2 ) )
					{
						one.lives+=10;
						confess.x = rand() % (WORLDWIDTH-w_and_h);
						confess.y = rand() % (WORLDHEIGHT-w_and_h);
						TenUp.Play();
						confess.collected = true;
					}
				//Mass
				if( nEnemies % mass.level == 0 && !mass.collected )
					if( collision.RectCircCollision( mass.x,mass.y,w_and_h,w_and_h,one.x+w_and_h/2,one.y+w_and_h/2,w_and_h/2 ) )
					{
						one.lives+=5;
						mass.x = rand() % (WORLDWIDTH-w_and_h);
						mass.y = rand() % (WORLDHEIGHT-w_and_h);
						FiveUp.Play();
						mass.collected = true;
					}
			}
			//Time Star
			if( one.collected_star && startimer.Time( HolyWaterDuration ) )
				one.collected_star = false;
		}

		if( players == TWO )
		{
			if( !one.dead )
			{
				kbd.IncDecVar_On_Keypress( &one.x,'D','A',0,0,one.speed );
				kbd.IncDecVar_On_Keypress( &one.y,'S','W',0,0,one.speed );
			}
			update.LimitVar( &one.x,WORLDWIDTH-w_and_h );
			update.LimitVar( &one.y,WORLDHEIGHT-w_and_h );

			if( kbd.KeyIsPressed_OneShot('Z') )
				one.speed = SPEED;

			if( !two.dead )
			{
				kbd.IncDecVar_On_Keypress( &two.x,39,37,0,0,two.speed );
				kbd.IncDecVar_On_Keypress( &two.y,40,38,0,0,two.speed );
			}
			update.LimitVar( &two.x,WORLDWIDTH-w_and_h );
			update.LimitVar( &two.y,WORLDHEIGHT-w_and_h );

			if( kbd.KeyIsPressed_OneShot('M') )
				two.speed = SPEED;

			//Coin
			if( collision.CircCollision( two.x,two.y,w_and_h/2,coin.x,coin.y,w_and_h/2) )
			{
				nEnemies++;
				if( nEnemies != 0 )
				{
					if( !(collision.RectCollision( two.x,two.y,20,20,WORLDWIDTH/2,WORLDHEIGHT/2,WORLDWIDTH/2,WORLDHEIGHT/2 ) ||
						collision.RectCollision( one.x,one.y,20,20,WORLDWIDTH/2,WORLDHEIGHT/2,WORLDWIDTH/2,WORLDHEIGHT/2 )) )
					{
						enemy[nEnemies-1].x = WORLDWIDTH - w_and_h;
						enemy[nEnemies-1].y = WORLDHEIGHT - w_and_h;
					}
					else if( !(collision.RectCollision( two.x,two.y,20,20,0,0,WORLDWIDTH/2,WORLDHEIGHT/2 ) ||
						collision.RectCollision( one.x,one.y,20,20,0,0,WORLDWIDTH/2,WORLDHEIGHT/2 )) )
					{
						enemy[nEnemies-1].x = 0;
						enemy[nEnemies-1].y = 0;
					}
					else
					{
						enemy[nEnemies-1].x = WORLDWIDTH - w_and_h;
						enemy[nEnemies-1].y = 0;						
					}
				}
				coin.x = rand() % (WORLDWIDTH-w_and_h);
				coin.y = rand() % (WORLDHEIGHT-w_and_h);
				Ding.Play();
			}
			//2 Revive 1
			if( one.dead && collision.CircCollision( two.x,two.y,w_and_h/2,rev.x,rev.y,w_and_h/2 ) )
			{
				Revived = true;
				rev.x = rand() % (WORLDWIDTH-w_and_h);
				rev.y = rand() % (WORLDHEIGHT-w_and_h);
			}
			//1 Revive 2
			if( two.dead && collision.CircCollision( one.x,one.y,w_and_h/2,rev.x,rev.y,w_and_h/2 ) )
			{
				Revived = true;
				rev.x = rand() % (WORLDWIDTH-w_and_h);
				rev.y = rand() % (WORLDHEIGHT-w_and_h);
			}
			if( ( two.dead || one.dead ) && Revived )
			{
				one.dead = false;
				two.dead = false;
				Revived = false;
				Boing.Play();
			}
			if( nEnemies != 0 )
			{
				//Star
				if( nEnemies % star.level == 0 && !star.collected )
					if( collision.RectCircCollision( star.x,star.y,w_and_h,w_and_h,two.x+w_and_h/2,two.y+w_and_h/2,w_and_h/2 ) )
					{
						one.collected_star = true;
						startimer.SetTimer(0);
						star.x = rand() % (WORLDWIDTH-w_and_h);
						star.y = rand() % (WORLDHEIGHT-w_and_h);
						Star.Play();
						star.collected = true;
					}
				//twoup
				if( nEnemies % oneup.level == 0 && !oneup.collected )
					if( collision.RectCircCollision( oneup.x,oneup.y,w_and_h,w_and_h,two.x+w_and_h/2,two.y+w_and_h/2,w_and_h/2 ) )
					{
						two.lives++;
						oneup.x = rand() % (WORLDWIDTH-w_and_h);
						oneup.y = rand() % (WORLDHEIGHT-w_and_h);
						Oneup.Play();
						oneup.collected = true;
					}
				//Confession
				if( nEnemies % confess.level == 0 && !confess.collected )
					if( collision.RectCircCollision( confess.x,confess.y,w_and_h,w_and_h,two.x+w_and_h/2,two.y+w_and_h/2,w_and_h/2 ) )
					{
						two.lives+=10;
						confess.x = rand() % (WORLDWIDTH-w_and_h);
						confess.y = rand() % (WORLDHEIGHT-w_and_h);
						TenUp.Play();
						confess.collected = true;
					}
				//Mass
				if( nEnemies % mass.level == 0 && !mass.collected )
					if( collision.RectCircCollision( mass.x,mass.y,w_and_h,w_and_h,two.x+w_and_h/2,two.y+w_and_h/2,w_and_h/2 ) )
					{
						two.lives+=5;
						mass.x = rand() % (WORLDWIDTH-w_and_h);
						mass.y = rand() % (WORLDHEIGHT-w_and_h);
						FiveUp.Play();
						mass.collected = true;
					}
			}
			//Star
			if( one.collected_star )
				two.collected_star = true;
			else
				two.collected_star = false;
		}

		//Coin
		if( (players == CLIENT || players == SERVER) )
		{
			if( one.collected_coin )
				one.collected_coin = false;
			if( collision.CircCollision( one.x,one.y,w_and_h/2,coin.x,coin.y,w_and_h/2) || two.collected_coin )
			{
				if( players == SERVER )
				{
					coin.x = rand() % (WORLDWIDTH-w_and_h);
					coin.y = rand() % (WORLDHEIGHT-w_and_h);
				}
				nEnemies++;
				Ding.Play();
				if( !two.collected_coin )
				{
					one.collected_coin = true;
				}
			}
		}
		//Enemy XY
		if( players == CLIENT )
		{
			if( nEnemies != 0 )
			{
				enemy[nEnemies-1].x = soc.recvbuf[9] + (soc.recvbuf[10]-1)*100-1;
				enemy[nEnemies-1].y = soc.recvbuf[11] + (soc.recvbuf[12]-1)*100-1;

				enemy[nEnemies-1].vx = soc.recvbuf[13]-1;
				enemy[nEnemies-1].vy = soc.recvbuf[14]-1;
			}
		}

		//Revive	
		if( (players == CLIENT || players == SERVER) )
		{
			if( two.dead && collision.CircCollision( one.x,one.y,w_and_h/2,rev.x,rev.y,w_and_h/2 ) )
			{
				Revived = true;
				rev.x = rand() % (WORLDWIDTH-w_and_h);
				rev.y = rand() % (WORLDHEIGHT-w_and_h);
				Boing.Play();
			}
			if( !two.dead && Revived )
				Revived = false;
		}

		UpdateEnemy();
		if( one.dead && two.dead && players != ONE )
			GameOver = true;
		if( one.dead && players == ONE )
			GameOver = true;
		if( nEnemies == NENEMIES )
			GameOver = true;
	}
	if( GameOver ) //If space is pressed while game is over, reset
	{
		if( kbd.KeyIsPressed_OneShot(' ') )
		{
			one.x = WORLDWIDTH/3;
			if( players == SERVER )
				one.x = (WORLDWIDTH*2)/3;
			else
				two.x = (WORLDWIDTH*2)/3;
			two.y = WORLDHEIGHT/2;
			one.y = WORLDHEIGHT/2;
			one.dead = false;
			two.dead = false;
			one.lives = 0;
			two.lives = 0;
			nEnemies = 0;
			GameOver = false;
			one.collected_star = false;
		}
		if( kbd.KeyIsPressed_OneShot('B') && (players == ONE || players == TWO) )
		{
			one.dead = false;
			two.dead = false;
			one.lives = 0;
			two.lives = 0;
			nEnemies = 0;
			GameOver = false;
			screenstates = MENU;
			one.collected_star = false;
		}
	}
	if( Pause )
	{
		if( kbd.KeyIsPressed_OneShot('P') )
			Pause = false;
		if( kbd.KeyIsPressed_OneShot(' ') )
		{
			one.x = WORLDWIDTH/3;
			if( players == SERVER )
				one.x = (WORLDWIDTH*2)/3;
			else
				two.x = (WORLDWIDTH*2)/3;
			two.y = WORLDHEIGHT/2;
			one.y = WORLDHEIGHT/2;
			one.dead = false;
			two.dead = false;
			one.lives = 0;
			two.lives = 0;
			nEnemies = 0;
			Pause = false;
			one.collected_star = false;
		}
		if( kbd.KeyIsPressed_OneShot('B') && (players == ONE || players == TWO) )
		{
			one.dead = false;
			two.dead = false;
			one.lives = 0;
			two.lives = 0;
			nEnemies = 0;
			Pause = false;
			screenstates = MENU;
		}
	}

	if( players == SERVER )
	{
		//Player
		soc.sendbuf[0] = one.dead+1;
		soc.sendbuf[1] = (update.GetDigit(one.x,1)+(update.GetDigit(one.x,10))*10)+1;
		soc.sendbuf[2] = (update.GetDigit(one.x,100)+(update.GetDigit(one.x,1000))*10)+1;
		soc.sendbuf[3] = (update.GetDigit(one.y,1)+(update.GetDigit(one.y,10))*10)+1;
		soc.sendbuf[4] = (update.GetDigit(one.y,100)+(update.GetDigit(one.y,1000))*10)+1;
		//Coin XY
		soc.sendbuf[5] = (update.GetDigit(coin.x,1)+(update.GetDigit(coin.x,10))*10)+1;
		soc.sendbuf[6] = (update.GetDigit(coin.x,100)+(update.GetDigit(coin.x,1000))*10)+1;
		soc.sendbuf[7] = (update.GetDigit(coin.y,1)+(update.GetDigit(coin.y,10))*10)+1;
		soc.sendbuf[8] = (update.GetDigit(coin.y,100)+(update.GetDigit(coin.y,1000))*10)+1;
		//Enemy XY
		if( nEnemies != 0 )
		{
			soc.sendbuf[9] = (update.GetDigit(enemy[nEnemies-1].x,1)+(update.GetDigit(enemy[nEnemies-1].x,10))*10)+1;
			soc.sendbuf[10] = (update.GetDigit(enemy[nEnemies-1].x,100)+(update.GetDigit(enemy[nEnemies-1].x,1000))*10)+1;
			soc.sendbuf[11] = (update.GetDigit(enemy[nEnemies-1].y,1)+(update.GetDigit(enemy[nEnemies-1].y,10))*10)+1;
			soc.sendbuf[12] = (update.GetDigit(enemy[nEnemies-1].y,100)+(update.GetDigit(enemy[nEnemies-1].y,1000))*10)+1;
			//Enemy speed
			soc.sendbuf[13] = enemy[nEnemies-1].vx+1;
			soc.sendbuf[14] = enemy[nEnemies-1].vy+1;
		}

		soc.sendbuf[15] = one.collected_coin+1;
		soc.sendbuf[16] = Revived+1;
		soc.sendbuf[17] = Pause+1;
		soc.sendbuf[18] = lotto+1;
		soc.sendbuf[19] = enemy[lotto].vx+1;
		soc.sendbuf[20] = enemy[lotto].vy+1;
		//end buffer
		soc.sendbuf[21] = 0;

		soc.Send(soc.sendbuf,sizeof(soc.sendbuf));
	}
}
void Game::LoadSettings()
{
	FILE* pFile;
	fopen_s( &pFile,"Docs/Settings.txt","r" );
	char c[100] = {0};

	fscanf_s( pFile,"%s",c );
	fseek( pFile,2,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	HolyWaterDuration = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	oneup.appearance = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	star.appearance = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	confess.appearance = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	mass.appearance = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	oneup.level = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	star.level = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	confess.level = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	mass.level = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	TitleX = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	TitleY = atoi(c);
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",c );
	fseek( pFile,1,SEEK_CUR );
	fscanf_s( pFile,"%s",&c );
	w_and_h = atoi(c);

	fclose( pFile );
}

/**********************////////////// Main Game Functions ////////////////************************/
void Game::Go()
{
	//Update
	if( screenstates == START )
	{
		if( kbd.KeyIsPressed_OneShot(' ') )
		{
			screenstates = MENU;
			FreeSprite(&Title);
		}
	}
	if( screenstates == MENU )
	{
		UpdateMenu();
	}
	if( screenstates == GAME && ((soc.GetCon() && (players == CLIENT || players == SERVER)) || players == ONE || players == TWO) )
	{
		UpdateGame();
	}

	/*************** Camera ****************/
	
	if (players != TWO && WORLDWIDTH != SCREENWIDTH)
	{
		one.cameraX = one.x + w_and_h / 2 - (SCREENWIDTH / 2);
		one.cameraY = one.y + w_and_h / 2 - (SCREENHEIGHT / 2);
	}
	if (players == TWO)
	{
		one.cameraX = one.x + w_and_h / 2 - (SCREENWIDTH / 4);
		one.cameraY = one.y + w_and_h / 2 - (SCREENHEIGHT / 2);

		two.cameraX = two.x + w_and_h / 2 - (SCREENWIDTH * 3) / 4;
		two.cameraY = two.y + w_and_h / 2 - (SCREENHEIGHT / 2);
	}

	//Draw and display frame
	gfx.BeginFrame();
	ComposeFrame();
	gfx.EndFrame();

	if( !soc.GetCon() && players == CLIENT && !soc.GetError() && OnlineSetDefault )
	{
		soc.Connect();
		if( !soc.GetError() )
		{
			soc.SetCon(true);
			screenstates = GAME;
		}
	}
	if( !soc.GetCon() && players == SERVER && !soc.GetError() && OnlineSetDefault )
	{
		soc.Accept();
		if( !soc.GetError() )
		{
			soc.SetCon(true);
			screenstates = GAME;
		}
	}
}

//Put wanted pixels on the back buffer
void Game::ComposeFrame()
{
	/*************** Draw Frame ****************/

	if( screenstates == START )
	{
		gfx.DrawSprite( SCREENWIDTH/2-(TitleX/2),SCREENHEIGHT/2-(TitleY/2),&Title);
	}
	if( screenstates == MENU )
	{
		DrawMenu();
	}
	if( screenstates == GAME )
	{
		DrawMap_and_Instructions();
		//Split Screen
		if( players == TWO )
		{
			DrawTwoPlayerGame();
		}
		else
		{
			DrawOnePlayerGame();
		}
		gfx.DrawWord( "score",SCREENWIDTH-((5+1+4)*15),5,10,3,2,255,255,255 );//5 chars + space + 4 (below) back,char size = 15
		gfx.DrawVariable( nEnemies,SCREENWIDTH-(4*15),5,10,3,2,255,255,255 );//4 chars back
		if( nEnemies == NENEMIES )
			gfx.DrawWord( "you won",SCREENWIDTH/2-( 7 * 10 ),SCREENHEIGHT/2 + 100,15,3,2,255,255,255 );
	}
	DrawSocketErrors();
}