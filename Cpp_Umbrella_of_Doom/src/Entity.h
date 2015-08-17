#pragma once

#include <SFML\Graphics.hpp>
#include "Node.h"
#include "LinkedList.h"

enum {
	PLAYER,
	ENEMY,
	UMBRELLA,
	DROPLET,
	BOSS
};

class Entity : public Node {
public:
	Entity();
	Entity(int id, sf::Sprite** sprites, int nSprites, LinkedList* entities);
	~Entity();

	sf::Sprite** getSprites();
	void setSprites(sf::Sprite** sprites);

	LinkedList* getEntities();
	void setEntities(LinkedList* entities);

	sf::Sprite* getDrawingSprite();
	void setDrawingSpriteIndex(int index);

	float getX();
	void setX(float x);
	float getY();
	void setY(float y);
	void move(float x, float y);

	int getId();
	void setId(int id);
	int getNSprites();
	void setNSprites(int nSprites);
	int getAnimationCounter();
	void setAnimationCounter(int count);

	virtual void print() override;

private:
	LinkedList* entities;
	sf::Sprite** sprites;
	int id, drawingSpriteIndex, nSprites, animationCounter;
	float x, y;
};

