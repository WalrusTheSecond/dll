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
            newNode = this.head;
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
            newNode = this.tail;
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
                Node<T> prevNode = currNode.getNext();
                node.setNext(currNode.getNext());
				currNode.setNext(node);
                node.setPrev(currNode);
                prevNode.setPrev(node);

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
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> newNode = new Node(element);
        if (index == 0){
            this.head = newNode;
            if (this.size == 0){
                this.tail = newNode;
            }
            this.size++;
            this.modCount++;
            return;
        } else {
            Node<T> currNode = this.head;
            for(int i = 1; i < this.size; i++){
                if(i == index){
                    Node<T> prevNode = currNode.getNext();
                    if(currNode == this.tail){
                        newNode.setPrev(currNode);
                        currNode.setNext(newNode);
                        this.tail = newNode;
                        this.size++;
                        this.modCount++;
                        return;
                    }
                    newNode.setNext(currNode.getNext());
				    currNode.setNext(newNode);
                    newNode.setPrev(currNode);
                    prevNode.setPrev(newNode);
                    this.size++;
                    this.modCount++;
                    return;

                }
            currNode = currNode.getNext();
        } 
        }
        
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
        Node<T> returnNode = this.tail;
        this.tail = this.tail.getPrev();
        this.size--;
        this.modCount++;
        return returnNode.getElement();
    }

    @Override
    public T remove(T element) {
        Node<T> currNode = this.head;
        Node<T> nodeReturn = null;
        int count = 0;
        while(currNode.getElement() != element){
            currNode = currNode.getNext();
            count++;
            if(count == this.size){
                throw new NoSuchElementException();
            }
        }
        nodeReturn = currNode;
        

        return nodeReturn.getElement();
    }

    @Override
    public boolean contains(T target) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public T first() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T get(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int indexOf(T element) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T last() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    

    @Override
    public T remove(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    

    

    @Override
    public void set(int index, T element) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    
}
