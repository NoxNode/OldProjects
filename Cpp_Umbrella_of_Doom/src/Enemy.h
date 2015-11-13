#pragma once

#include "Entity.h"

class Enemy : public Entity {
public:
	Enemy(LinkedList* entities);
	void update(int* cameraX, int difficulty);

	int getHealth();
	void setHealth(int health);

	virtual void print() override;

private:
	int health, cooldown;
	bool aggroed;
};
