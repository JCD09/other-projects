#include <stdbool.h>

typedef struct Node{
  void* item;
  int nodeNumber;
  struct Node* next;
  struct Node* previous;
} Node;

// creating a new Node;
Node createNode();


// Function for Debugging purposes
void printNodeData(Node* node);
void setNodeNumber(Node *curNode, int number);

// Breaks the link between node and next node, returns pointer to next node
Node* breakAfter(Node *node);
Node* breakBefore(Node *node);


void nodeAppend(Node *node, Node *node2);
void nodePrepend(Node *node, Node *node2);


Node* getPrevious(Node* node);
Node* getNext(Node* node);
