#include "Node.h"
#include <iostream>

Node::Node() {
	next = nullptr;
}

Node::~Node() {
	if (next != nullptr)
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
