#include "Node.h"

#define Array_Size 100

typedef struct NodeArray{
  int size;
  int nAvailNodes;
  Node nodePool[Array_Size];
  Node *head;
  Node *tail;

} NodeArray;


NodeArray createNodeArray();
void linkNodes(NodeArray *nodeArray);
Node* getNode(NodeArray *nodeArray);
void putNode(NodeArray *nodeArray,Node *node);

void printNodeIDs(NodeArray *nodeArray);
void traverseFreeNodes(NodeArray *nodeArray);
