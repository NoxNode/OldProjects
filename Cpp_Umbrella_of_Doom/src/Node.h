#pragma once

class Node {
public:
	Node();
	~Node();

	Node* getNext();
	void setNext(Node* node);
	virtual void print();

private:
	Node* next;
};
