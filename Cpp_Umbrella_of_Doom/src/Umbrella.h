#pragma once

#include "Entity.h"

class Umbrella : public Entity {
public:
	Umbrella(LinkedList* entities);
	void update();

	virtual void print() override;
};

