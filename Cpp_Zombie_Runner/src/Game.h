#pragma once

#include "D3DGraphics.h"
#include "Keyboard.h"
#include "Mouse.h"
#include "Sound.h"
#include "TCP.h"
#include "Updates.h"

#define NENEMIES 100
static short w_and_h = 20;
static short WORLDWIDTH = 2400;
static short WORLDHEIGHT = 1200;
static short SPEED = 10;

class Game
{
private:
	/********************************/
	/*           Enums              */

	enum ScreenStates { START,MENU,GAME };
	enum Players { ONE,TWO,CLIENT,SERVER };
	ScreenStates screenstates;
	Players players;

	/********************************/

	/********************************/
	/*         Structs              */
	
	struct Player
	{
		short x,y,speed,lives,cameraX,cameraY;
		bool dead,collected_coin,collected_star;
	};
	Player one,two;

	struct Item
	{
		short x,y,appearance,level;
		Timer appear;
		bool collected;
	};
	Item coin,rev,star,oneup,confess,mass;

	struct Enemy
	{
		short x,y,vx,vy;
	};
	Enemy enemy[NENEMIES];

	/********************************/
	
	/********************************/
	/*         Sprites              */

	Sprite Face,Zombie,Skull,Coin,Revive,StarFace,OneUp,StarPower,Confession,Mass,Title;
	
	/********************************/

	/********************************/
	/*            Sounds            */

	Sound Boing,Ding,Oneup,Star,Hit,FiveUp,TenUp;

	/********************************/

	/********************************/
	/*           Buttons            */

	RectButton One,Two,Client,Server,SpeedUp,SpeedDown,
		WorldSizeUp,WorldSizeDown,NEnemiesUp,NEnemiesDown;
	
	/********************************/
	/********************************/
	/*            Timers            */
		
	Timer onlinetimer,startimer;
	
	/********************************/
public:
	Game( HWND hWnd,KeyboardServer& kServer,const MouseServer& mServer );
	~Game();
	void Go();
private:
	void ComposeFrame();
	/********************************/
	/*       Functions              */
	
	void Initialize();
	
	/**********************////////// Drawing Functions ////////////////************************/

	void DrawMap_and_Instructions();
	void DrawClippedSprite( short refx,short refy,short x,short y,Sprite* sprite,int minx = 0,int miny = 0,int maxx = SCREENWIDTH,int maxy = SCREENHEIGHT );
	void DrawMenu();
	void DrawSocketErrors();
	void DrawTwoPlayerGame();
	void DrawOnePlayerGame();
	
	/**********************////////// Updating Functions ////////////////************************/

	void UpdateEnemy();
	void UpdateMenu();
	void UpdateGame();
	void LoadSettings();

	/********************************/
	//Outside classes used in Game
	D3DGraphics gfx;
	KeyboardClient kbd;
	MouseClient mouse;
	DSound audio;
	TCP soc;
	GeneralUpdates update;
	Collision collision;
	/********************************/
	/*       Variables              */

	short nEnemies;
	bool GameOver;
	bool Pause;
	short lotto;
	bool collected;

	bool OnlineSetDefault;

	short HolyWaterDuration;

	short TitleX,TitleY;

public:
	bool EndConnection;

	/********************************/
};