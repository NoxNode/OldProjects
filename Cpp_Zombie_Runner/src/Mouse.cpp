#include "Mouse.h"

MouseClient::MouseClient( const MouseServer& server )
: server( server )
{}
int MouseClient::GetMouseX() const
{
	return server.x;
}
int MouseClient::GetMouseY() const
{
	return server.y;
}
bool MouseClient::LeftIsPressed() const
{
	return server.leftIsPressed;
}
bool MouseClient::RightIsPressed() const
{
	return server.rightIsPressed;
}
bool MouseClient::LeftIsPressed_OneShot()
{
	static bool LastKeyPressed = false;
	bool retVal = ( server.leftIsPressed && !LastKeyPressed );
	LastKeyPressed = server.leftIsPressed;
	return retVal;
}
bool MouseClient::RightIsPressed_OneShot()
{
	static bool LastKeyPressed = false;
	bool retVal = ( server.rightIsPressed && !LastKeyPressed );
	LastKeyPressed = server.rightIsPressed;
	return retVal;
}
bool MouseClient::IsInWindow() const
{
	return server.isInWindow;
}
bool MouseClient::ButtonIsPressed( RectButton button,bool TrueLeft_FalseRight,bool OneShot )
{
	if( mousecollision.RectCollision( server.x,server.y,0,0,button.x,button.y,button.width,button.height) )
	{
		if( TrueLeft_FalseRight == true )
		{
			if( OneShot && LeftIsPressed_OneShot() )
				return true;
			if( !OneShot && LeftIsPressed() )
				return true;
		}
		if( TrueLeft_FalseRight == false )
		{
			if( OneShot && RightIsPressed_OneShot() )
				return true;
			if( !OneShot && RightIsPressed() )
				return true;
		}
	}
	return false;
}
bool MouseClient::ButtonIsPressed( CircButton button,bool TrueLeft_FalseRight,bool OneShot )
{	
	if( mousecollision.CircCollision( server.x,server.y,0,button.x,button.y,button.rad ) )
	{
		if( TrueLeft_FalseRight == true )
		{
			if( OneShot && LeftIsPressed_OneShot() )
				return true;
			if( !OneShot && LeftIsPressed() )
				return true;
		}
		if( TrueLeft_FalseRight == false )
		{
			if( OneShot && RightIsPressed_OneShot() )
				return true;
			if( !OneShot && RightIsPressed() )
				return true;
		}
	}
	return false;
}

MouseServer::MouseServer()
:	isInWindow( false ),
	leftIsPressed( false ),
	rightIsPressed( false ),
	x( -1 ),
	y( -1 )
{}
void MouseServer::OnMouseMove( int x,int y )
{
	this->x = x;
	this->y = y;
}
void MouseServer::OnMouseLeave()
{
	isInWindow = false;
}
void MouseServer::OnMouseEnter()
{
	isInWindow = true;
}
void MouseServer::OnLeftPressed()
{
	leftIsPressed = true;
}
void MouseServer::OnLeftReleased()
{
	leftIsPressed = false;
}
void MouseServer::OnRightPressed()
{
	rightIsPressed = true;
}
void MouseServer::OnRightReleased()
{
	rightIsPressed = false;
}
bool MouseServer::IsInWindow() const
{
	return isInWindow;
}