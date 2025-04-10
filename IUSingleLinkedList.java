import java.lang.reflect.InaccessibleObjectException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size, modCount;
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		this.head = this.tail = null;
		this.size = 0;
		this.modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		Node<T> node = new Node<T>(element);
		if(isEmpty()){    
            this.head = this.tail = node;
        } else {
            node.setNext(this.head);
            this.head = node;
        } 
        this.size++;
        this.modCount++;
	}

	@Override
	public void addToRear(T element) {
		if(isEmpty()){
            Node<T> node = new Node<T>(element);
            this.head = this.tail = node;

        } else {
            Node<T> node = new Node<T>(element);
            this.tail.setNext(node);
            this.tail = node;
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
        Node<T> currNode = this.head;
        int count = 0;
    
        if (isEmpty()){
            throw new NoSuchElementException();
        }
		while(count < this.size) {

            if(currNode.getElement() == target){
                Node<T> node = new Node<T>(element);
                node.setNext(currNode.getNext());
				currNode.setNext(node);
				this.size++;
				this.modCount++;
				return;
            }
			currNode = currNode.getNext();
            count++;

			if(count == this.size){
				throw new NoSuchElementException();
			}
        }
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
	
		Node<T> node = new Node<>(element);
	
		if (index == 0) { // Insert at the head
			node.setNext(head);
			head = node;
			if (this.size == 0) { // If adding to an empty list, update tail as well
				this.tail = node;
			}
		} else { 
			Node<T> currNode = this.head;
			for (int i = 0; i < index - 1; i++) {
				currNode = currNode.getNext();
			}
			
			node.setNext(currNode.getNext());
			currNode.setNext(node);
	
			if (node.getNext() == null) { // If added at the last position, update tail
				this.tail = node;
			}
		}
	
		size++;
		modCount++;
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T first = this.head.getElement();
		this.head = this.head.getNext();

		if (this.head.getElement() == null) {
			this.tail = null;
		}

		this.size--;
		this.modCount++;
		return first;
	}

	@Override
public T removeLast() {
    if (isEmpty()) {
        throw new NoSuchElementException();
    }

    if (size == 1) { // If there's only one element
        return removeFirst();
    }

    Node<T> curr = head;
    while (curr.getNext() != tail) {
        curr = curr.getNext();
    }

    T last = tail.getElement();
    tail = curr;
    tail.setNext(null);
    
    size--;
    modCount++;
    return last;
}
	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		Node<T> previous = null;
		Node<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			head = current.getNext();
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		} else { //somewhere in the middle
			previous.setNext(current.getNext());
		}
		
		size--;
		modCount++;
		
		return current.getElement();
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index >= this.size){
			throw new IndexOutOfBoundsException();
		}
		T element = head.getElement();
		if(index == 0){
			removeFirst();
		}else{
			Node<T> currentNode = this.head;
			for(int i = 0; i < index - 1; i++){
				currentNode = currentNode.getNext();
			}
			Node<T> toRemove = currentNode.getNext();
            element = toRemove.getElement();
            currentNode.setNext(toRemove.getNext());
            if (toRemove == tail) {
                tail = currentNode;
            }
            size--;
            modCount++;
        }
        return element;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        current.setElement(element);
		this.modCount++;
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getElement();
	}

	@Override
	public int indexOf(T element) {
	Node<T> current = head;
    int index = 0;
    
    while (current != null) {
        if (current.getElement().equals(element)) {
            return index; // Found the element, return the index
        }
        current = current.getNext();
        index++;
    }
    
    return -1; // Element not found
	}

	@Override
	public T first() {
	    if (isEmpty()) {
			throw new NoSuchElementException("List is empty.");
		}
		return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
			throw new NoSuchElementException("List is empty.");
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		Node<T> current = head;
    
		while (current != null) {
			if (current.getElement().equals(target)) {
				return true; // Found the element
			}
			current = current.getNext();
		}
		
		return false; // Element not found
	}

	@Override
	public boolean isEmpty() {
		if(size == 0) {
            return true;
        }
		return false;
	}

	@Override
	public int size() {
		return size;
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
		return new SllIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SllIterator implements Iterator<T> {
        private Node<T> nextNode;
        private Node<T> lastReturned;
        private int iterModCount;

        public SllIterator() {
            nextNode = head;
            lastReturned = null;
            iterModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
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
            nextNode = nextNode.getNext();
            return lastReturned.getElement();
        }

        @Override
        public void remove() {
            if (modCount != iterModCount) {
                throw new ConcurrentModificationException();
            }
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            IUSingleLinkedList.this.remove(lastReturned.getElement());
            lastReturned = null;
            iterModCount++;
        }
	}
}