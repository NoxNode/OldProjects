#pragma once

#include "Entity.h"

class Boss : public Entity {
public:
	Boss(LinkedList* entities);
	void update(int* cameraX, int difficulty);

	int getHealth();
	void setHealth(int health);

private:
	int health, cooldown;
	bool aggroed;
};
