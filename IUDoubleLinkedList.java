import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head, tail;
    private int size, modCount;
    public IUDoubleLinkedList() {
        this.size = this.modCount = 0;
        this.head = this.tail = null;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node(element);
        if(isEmpty()) {
            this.head = this.tail = newNode;
        } else {
            newNode.setNext(this.head);
            this.head.setPrev(newNode);
            this.head = newNode;
        }
        this.size++;
        this.modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node(element);
        if(isEmpty()) {
            this.head = this.tail = newNode;
        } else {
            newNode.setPrev(this.tail);
            this.tail.setNext(newNode);
            this.tail = newNode;
        }
        this.size++;
        this.modCount++;
    }
    
    @Override
    public void add(T element) {
        addToRear(element);        
    }

    @Override
    public void addAfter(T element, T target) {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty.");
        }
    
        // Find the target node.
        Node<T> current = head;
        while (current != null) {
            // Use safe equality check to account for potential nulls.
            if ((target == null && current.getElement() == null) ||
                (target != null && target.equals(current.getElement()))) {
                // Target found. Create a new node.
                Node<T> newNode = new Node<>(element);
    
                // Set newNode's pointers:
                newNode.setPrev(current);
                newNode.setNext(current.getNext());
    
                // Attach newNode after current.
                current.setNext(newNode);
                
                // If current was not the tail, update the next node's previous pointer.
                if (newNode.getNext() != null) {
                    newNode.getNext().setPrev(newNode);
                } else {  // Otherwise, newNode becomes the new tail.
                    tail = newNode;
                }
                
                size++;
                modCount++;
                return;
            }
            current = current.getNext();
        }
    
        // If the target was never found, signal the error.
        throw new NoSuchElementException("Target not found in the list.");
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> newNode = new Node<>(element);

        if (index == 0) {
            // Add to front
            if (isEmpty()) {
                this.head = this.tail = newNode;
            } else {
                newNode.setNext(this.head);
                this.head.setPrev(newNode);
                this.head = newNode;
            }
        } else if (index == this.size) {
            // Add to rear
            newNode.setPrev(this.tail);
            this.tail.setNext(newNode);
            this.tail = newNode;
        } else {
            // Insert in middle
            Node<T> current = this.head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            newNode.setNext(current);
            newNode.setPrev(current.getPrev());
            current.getPrev().setNext(newNode);
            current.setPrev(newNode);
        }

        this.size++;
        this.modCount++;
    }

    @Override
    public T removeFirst() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        } 
        Node<T> returnNode = this.head;
        this.head = this.head.getNext();
        this.size--;
        this.modCount++;
        return returnNode.getElement();
    }

    @Override
    public T removeLast() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        } 
        Node<T> returnNode = this.tail;
        this.tail = this.tail.getPrev();
        this.size--;
        this.modCount++;
        return returnNode.getElement();
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<T> current = this.head;

        while (current != null) {
            if (current.getElement().equals(element)) {
                // Handle removal of head
                if (current == head) {
                    return removeFirst();
                }

                // Handle removal of tail
                if (current == tail) {
                    return removeLast();
                }

                // Removal from the middle
                current.getPrev().setNext(current.getNext());
                current.getNext().setPrev(current.getPrev());

                this.size--;
                this.modCount++;
                return current.getElement();
            }
            current = current.getNext();
        }

        throw new NoSuchElementException("Element not found in the list.");
    }

    
    @Override
    public T remove(int index) { 
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        } 
        Node<T> nodeReturn;
        T element;
        if(index == 0){
            element = this.head.getElement();
            removeFirst();
        } else if(index == this.size-1){
            element = this.tail.getElement();
            removeLast();
        } else {
            Node<T> curNode = this.head;
            for(int i = 0; i < index; i++){
                curNode = curNode.getNext();
            }
            element = curNode.getElement();
            curNode.getPrev().setNext(curNode.getNext());
            curNode.getNext().setPrev(curNode.getPrev());
        }
        this.size--;
        this.modCount++;

        return element;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> curNode = this.head;
        for (int i = 0; i < index; i++) {
            curNode = curNode.getNext();
        }
        curNode.setElement(element);
        this.modCount++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> curNode = head;
        for (int i = 0; i < index; i++) {
            curNode = curNode.getNext();
        }
        return curNode.getElement();
    }

    @Override
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;
    
        while (current != null) {
            if ((element == null && current.getElement() == null) ||
                (element != null && element.equals(current.getElement()))) {
                return index;
            }
            current = current.getNext();
            index++;
        }
    
        return -1;  // Element not found
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> firstNode = this.head;
        return firstNode.getElement();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> lastNode = this.tail;
        return lastNode.getElement();
    }

    @Override
    public boolean contains(T target) {
        Node<T> current = head;
    
		while (current != null) {
			if (current.getElement().equals(target)) {
				return true; 
			}
			current = current.getNext();
		}
		
		return false; 
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        if(size == 0){
            return true;
        } 
        return false;
    }

    @Override
    public String toString() {
		if (isEmpty()) {
			return "[]"; // Return empty brackets if the list is empty
		}
	
		String result = "[";
		Node<T> current = head;
	
		while (current != null) {
			result += current.getElement(); // Append element
			if (current.getNext() != null) {
				result += ", "; // Add a comma separator if there's a next element
			}
			current = current.getNext();
		}
	
		result += "]";
		return result;
	}





    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return new DLLlistIterator();
    }

    

    @Override
    public ListIterator<T> listIterator() {
        return new DLLlistIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLlistIterator(startingIndex);
    }

    private class DLLlistIterator implements ListIterator<T> {
        private Node<T> nextNode;
        private Node<T> prevNode;
        private Node<T> lastReturned;
        private int index;
        private int iterModCount;

        public DLLlistIterator() {
            this(0);
        }

        public DLLlistIterator(int startIndex) {
            if (startIndex < 0 || startIndex > size){
                throw new IndexOutOfBoundsException();
            }
            nextNode = head;
            for (int i = 0; i < startIndex; i++) {
                nextNode = nextNode.getNext();
            }

            prevNode = (nextNode == null) ? tail : nextNode.getPrev();
            lastReturned = null;
            index = startIndex;
            iterModCount = modCount;
        }



        @Override
        public void add(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
    
            Node<T> newNode = new Node<>(e);
    
            newNode.setPrev(prevNode);
            newNode.setNext(nextNode);
    
            if (prevNode != null) {
                prevNode.setNext(newNode);
            } else {
                head = newNode;
            }
    
            if (nextNode != null) {
                nextNode.setPrev(newNode);
            } else {
                tail = newNode;
            }
    
            prevNode = newNode;
            size++;
            index++;
            modCount++;
            iterModCount++;
            lastReturned = null;
        }

        @Override
        public void set(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
    
            lastReturned.setElement(e);
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public boolean hasPrevious() {
            return prevNode != null;
        }

        @Override
        public T next() {
            if (modCount != iterModCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = nextNode;
            prevNode = nextNode;
            nextNode = nextNode.getNext();
            index++;
            return lastReturned.getElement();
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public T previous() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            lastReturned = prevNode;
            nextNode = prevNode;
            prevNode = prevNode.getPrev();
            index--;
            return lastReturned.getElement();
        }
        

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
    
            Node<T> toRemove = lastReturned;
    
            if (toRemove == head) {
                head = toRemove.getNext();
                if (head != null) head.setPrev(null);
            } else {
                toRemove.getPrev().setNext(toRemove.getNext());
            }
    
            if (toRemove == tail) {
                tail = toRemove.getPrev();
                if (tail != null) tail.setNext(null);
            } else {
                toRemove.getNext().setPrev(toRemove.getPrev());
            }
    
            if (toRemove == prevNode) {
                prevNode = toRemove.getPrev();
                index--;
            } else {
                nextNode = toRemove.getNext();
            }
    
            size--;
            modCount++;
            iterModCount++;
            lastReturned = null;
        }

    }

    

    

    

    

    

    
}
