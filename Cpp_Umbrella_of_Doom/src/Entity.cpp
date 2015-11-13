#include "Entity.h"
#include <iostream>

Entity::Entity() :
Node(),
id(0),
sprites(0),
nSprites(0),
drawingSpriteIndex(0),
x(0),
y(0),
entities(0),
animationCounter(0)
{}

Entity::Entity(int id, sf::Sprite** sprites, int nSprites, LinkedList* entities) :
Node(),
id(id),
sprites(sprites),
nSprites(nSprites),
drawingSpriteIndex(0),
x(0),
y(0),
entities(entities),
animationCounter(0)
{}

Entity::~Entity() {
	for (int i = 0; i < nSprites; i++) {
		delete sprites[i];
	}
	delete sprites;
}

sf::Sprite** Entity::getSprites() {
	return sprites;
}

void Entity::setSprites(sf::Sprite** sprites) {
	this->sprites = sprites;
}

LinkedList* Entity::getEntities() {
	return entities;
}

void Entity::setEntities(LinkedList* entities) {
	this->entities = entities;
}

sf::Sprite* Entity::getDrawingSprite() {
	sprites[drawingSpriteIndex]->setPosition(x, y);
	return sprites[drawingSpriteIndex];
}

void Entity::setDrawingSpriteIndex(int index) {
	drawingSpriteIndex = index;
}

float Entity::getX() {
	return x;
}

void Entity::setX(float x) {
	this->x = x;
}

float Entity::getY() {
	return y;
}

void Entity::setY(float y) {
	this->y = y;
}

void Entity::move(float x, float y) {
	this->x += x;
	this->y += y;
}

int Entity::getId() {
	return id;
}

void Entity::setId(int id) {
	this->id = id;
}

int Entity::getNSprites() {
	return nSprites;
}

void Entity::setNSprites(int nSprites) {
	this->nSprites = nSprites;
}

int Entity::getAnimationCounter() {
	return animationCounter;
}

void Entity::setAnimationCounter(int count) {
	animationCounter = count;
}

void Entity::print() {
	using namespace std;
	cout << "I'm an Entity" << endl;
}
