cc = gcc
#prog1 = ListConcat_Test
prog1 = ListAdd_test
prog2 = ListConcat_test

OBJS = Node.o NodeArray.o LIST.o t_ListConcat.o t_ListAdd.o t_ListSearch.o t_ListFree.o t_ListRemoveTrim.o t_ListInsert.o main.o




one: $(OBJS)
	$(cc) -o ListRemoveTrim_test Node.o NodeArray.o LIST.o t_ListRemoveTrim.o

two: $(OBJS)
	$(cc) -o ListConcat_test Node.o NodeArray.o LIST.o t_ListConcat.o

three: $(OBJS)
	$(cc) -o ListSearch_test Node.o NodeArray.o LIST.o t_ListSearch.o

four: $(OBJS)
	$(cc) -o ListFree_test Node.o NodeArray.o LIST.o t_ListFree.o

five: $(OBJS)
	$(cc) -o ListAdd_test Node.o NodeArray.o LIST.o t_ListAdd.o

six: $(OBJS)
	$(cc) -o ListInsert_test Node.o NodeArray.o LIST.o t_ListInsert.o

# THIS IS EMPTY MAIN FILE. INCLUDING IT BECAUSE INSTRUCTIONS SAYS SO
main: $(OBJS)
	$(cc) -o main Node.o NodeArray.o LIST.o main.o


LIST.o:
	$(cc) -c LIST.c

Node.o:
	$(cc) -c Node.c

NodeArray.o:
	$(cc) -c NodeArray.c

t_ListAdd.o:
	$(cc) -c t_ListAdd.c
t_ListConcat.o:
	$(cc) -c t_ListConcat.c
t_ListFree.o:
	$(cc) -c t_ListFree.c
t_ListSearch.o:
	$(cc) -c t_ListSearch.c
t_ListRemoveTrim.o:
	$(cc) -c t_ListRemoveTrim.c
t_ListInsert.o:
	$(cc) -c t_ListInsert.c
main.o:
	$(cc) -c main.c

clean:
	rm *.o
