#include "Node.h"
#include <iostream>

Node::Node() {
	next = 0;
}

Node::~Node() {
	if (next != 0)
		delete next;
}

Node* Node::getNext() {
	return next;
}

void Node::setNext(Node* node) {
	next = node;
}

void Node::print() {
	using namespace std;
	cout << "I'm a node" << endl;
}
