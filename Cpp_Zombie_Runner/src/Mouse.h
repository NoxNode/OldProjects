#pragma once
#include "Updates.h"

class MouseServer;

struct RectButton
{
	short x,y,width,height;
};
struct CircButton
{
	short x,y,rad;
};

class MouseClient
{
public:
	MouseClient( const MouseServer& server );
	int GetMouseX() const;
	int GetMouseY() const;
	bool LeftIsPressed() const;
	bool RightIsPressed() const;
	bool LeftIsPressed_OneShot();
	bool RightIsPressed_OneShot();
	bool IsInWindow() const;
	bool ButtonIsPressed( RectButton button,bool TrueLeft_FalseRight,bool OneShot = false );
	bool ButtonIsPressed( CircButton button,bool TrueLeft_FalseRight,bool OneShot = false );
private:
	const MouseServer& server;
	Collision mousecollision;
};

class MouseServer
{
	friend MouseClient;
public:
	MouseServer();
	void OnMouseMove( int x,int y );
	void OnMouseLeave();
	void OnMouseEnter();
	void OnLeftPressed();
	void OnLeftReleased();
	void OnRightPressed();
	void OnRightReleased();
	bool IsInWindow() const;
private:
	int x;
	int y;
	bool leftIsPressed;
	bool rightIsPressed;
	bool isInWindow;
};