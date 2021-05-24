#include "NodeArray.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <assert.h>

NodeArray createNodeArray(){
  NodeArray newNodeArray;
  newNodeArray.size = Array_Size;
  newNodeArray.nAvailNodes = Array_Size;
  newNodeArray.nodePool[0]=createNode();

  for(int i = 1; i < Array_Size; i++){
    Node *curNode = &newNodeArray.nodePool[i-1];

    newNodeArray.nodePool[i] = createNode();
    Node* nextNode = &newNodeArray.nodePool[i];
    setNodeNumber(nextNode,i);
  }
  return newNodeArray;
}

void linkNodes(NodeArray *nodeArray){
  Node *firstNode = &(nodeArray->nodePool[0]);
  for(int i = 1; i < nodeArray->size; i++){
      Node *nextNode = &(nodeArray->nodePool[i]);
      firstNode->next = nextNode;
      firstNode = nextNode;
  }
  nodeArray->head = &(nodeArray->nodePool[0]);
  nodeArray->tail = &(nodeArray->nodePool[(nodeArray->size)-1]);

}

// Return first available Nodes, if No nodes are available return NULL;

Node* getNode(NodeArray *nodeArray){
  if(nodeArray->nAvailNodes==0){
    return NULL;
  }
  else{
    Node *ret = nodeArray->head;
    assert(nodeArray->head->previous == NULL);
    nodeArray->head=nodeArray->head->next;
    ret->next = NULL;
    nodeArray->nAvailNodes=nodeArray->nAvailNodes-1;
    return ret;
  }
}

//places Node back into availabl POOL of nodes;
// Node has to belong to the saem array;
void putNode(NodeArray *nodeArray,Node *node){
  nodeArray->tail->next=node;
  nodeArray->tail = node;
  nodeArray->nAvailNodes=nodeArray->nAvailNodes+1;
}

// Debugging Functions;
void printNodeIDs(NodeArray *nodeArray){
  int size = nodeArray->size;
  for (int i = 0; i < size; i++){
    Node* nodeAddress = &(nodeArray->nodePool[i]);
    printNodeData(nodeAddress);
  }
}

// check if the createdFreeNodes are Linked;
void traverseFreeNodes(NodeArray *nodeArray){
  Node *firstNode = nodeArray->head;
  Node *nextNode = firstNode->next;
  while (nextNode!=NULL){
    printNodeData(nextNode);
    nextNode=getNext(nextNode);
  }
  printf("printin head and tail\n");
  printf("Head Number; %d \n",nodeArray->head->nodeNumber );
  printf("Tail Number; %d \n",nodeArray->tail->nodeNumber );

}
