#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "Node.h"



Node createNode(){
  Node newNode;
  newNode.next = NULL;
  newNode.previous = NULL;
  newNode.item = NULL;
  newNode.nodeNumber=0;
  return newNode;
}

void setNodeNumber(Node *curNode, int number){
  curNode->nodeNumber = number;
}

Node* breakBefore(Node *node){
  Node* returnNode = node->previous;
  if(returnNode == NULL){
    return returnNode;
  }
  else{
    node->previous = NULL;
    returnNode->next = NULL;
    return returnNode;}

}

Node* breakAfter(Node *node){
  Node* returnNode = node->next;
  if(returnNode == NULL){
    return returnNode;
  }
  else{
    node->next = NULL;
    returnNode->previous = NULL;
    return returnNode;}
}

void nodeAppend(Node *node, Node *node2){
  node->next=node2;
  node2->previous=node;
}
void nodePrepend(Node *node, Node *node2){
  node->previous=node2;
  node2->next=node; 
}

void printNodeData(Node *node){
  printf("The Node Number is: %d\n",node->nodeNumber );
  printf("The Node Address is: %p\n",node);
  printf("The Previous Node Address is: %p\n",getPrevious(node));
  printf("The Next Node Address is: %p\n",getNext(node));
  printf("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN\n");
}

Node* getPrevious(Node* node){
  return node->previous;
}
Node* getNext(Node* node){
  return node->next;
}
