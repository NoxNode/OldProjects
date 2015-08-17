#pragma once
#include <math.h>

class GeneralUpdates
{
public:
	void SwapVars( int* x1,int* x2 )
	{
		short temp = *x1;
		*x1 = *x2;
		*x2 = temp;
	};
	void LimitVar( short* var,short max,short min = 0 )
	{
		if( *var > max )
			*var = max;
		if( *var < min )
			*var = min;
	};
	short GetDigit( short var,short digit )
	{
		return (var % (digit*10)) / digit;
	};
};

class Timer
{
public:
	Timer()
	{
		time = 0;
	};
	void SetTimer( short set )
	{
		time = set;
	};
	short GetTimer()
	{
		return time;
	};
	bool Time( short timelimit )
	{
		if( time >= timelimit )
		{
			time = 0;
			return true;
		}
		else
		{
			time++;
			return false;
		}
	};
	short time;
};

class Collision
{	
public:
	bool CircCollision( short x1,short y1,short radius1,short x2,short y2,short radius2 )
	{
		if( sqrt( (float)(x1 - x2)*(x1 - x2)
				+ (float)(y1 - y2)*(y1 - y2) ) < radius1 + radius2 )
		{
			return true;
		}
		return false;
	};
	bool RectCollision( short x1,short y1,short width1,short height1,short x2,short y2,short width2,short height2 )
	{
		for( short x = x1; x <= x1 + width1; x++ )
		{
			for( short y = y1; y <= y1 + height1; y++ )
			{
				if( x >= x2 && x <= x2 + width2 && y >= y2 && y <= y2 + height2 )
					return true;
			}
		}
		return false;
	};
	bool RectCircCollision( short rx,short ry,short width,short height,short cx,short cy,short rad )
	{
		for( int x = rx; x <= rx + width; x++ )
		{
			for( int y = ry; y <= ry + height; y++ )
			{
				if( sqrt( (float)( (x - cx)*(x - cx) + (y - cy)*(y - cy) ) ) <= rad )
						return true;
			}
		}
		return false;
	};
};