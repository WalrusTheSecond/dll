
****************
* DLL
* Computer Science 221 Tuesday/Thursday 12 pm
* 4/11/2025
* Evan Wallace
**************** 

OVERVIEW:

This program is a double linked list in Java which supports indexed and operations in both directions. It has the functionality to add, remove, and retrieve elements from any position in the list, as well as a iterator with fail-fast behavior.


INCLUDED FILES:

 * IUDoubleLinkedList.java - source file containing the implementation of double linked list
 * IndexedUnsortedList.java - An interface that defines the expected methods for lists
 * Node.java - A Class that represents a node in the double-linked list
 * ListTester.java - A tester class which thoroughly tests IndexedUnsortedList implementations.
 * README - this file


COMPILING AND RUNNING:

 From the directory containing your source files, compile the program using the command
  - javac IUDoubleLinkedList.java
 
 To run the program you will need a driver class with a main() method. For example:
 javac ListTester.java
 java ListTester

 The console will then display the results after the program finishes.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

The program is built around a double-linked list structure where each node references the next and previous node. The list has dynamic resizing and provides constant time insertions and deletions at the head and tail, and O(n) time operations at other points.

The IUDoubleLinkedList class constantly maintains references to the head and tail nodes, it also keeps track of the size(size), and modifications made(modCount). It contains the standard list operations like add, remove, get, indexOf, ect.

The DLLlistIterator class allows for travel in both directions through the list, following the ListIterator interface. It includes logic to handle structural changes(add, remove, set) while keeping track of its modification count to enforce fail-fast behavior.

TESTING:

 The program was tested using a large combination of tests. The testing strategy was to take each possible scenario and run it through tests for lists of that length, so any scenario ending in three elements would be subject to the same tests, which would check almost all possible scenarios. The testing left out lists of size greater than 3 elements. There are no known bugs in the code.


DISCUSSION:

 Some issues I encountered while programming were nullPointerExceptions, there were lots of moments where I would be off by one in the conditions of loops and if statements. Another was implementing ListIterator, I had many issues accurately keeping track of the modification count as sometimes I would call it twice because I would call other methods which updated the modification count then I would update it again at the end of the method. Which would increase or decrease the count by 2 causing ConcurrentModificationExceptions. These issues were solved by using the tester class to find where the exceptions were coming from, and then staring at the screen for 10 minutes. One major realization I had was the importance of separating the node references prevNode and nextNode from lastReturned, this caused me lots of grief as I would change the prevNode reference and then return lastReturned and lots of things would go awry. 
 
----------------------------------------------------------------------------

