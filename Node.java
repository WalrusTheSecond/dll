/**
 * Node represents a node in a double list.
 *
 * @author Evan Wallace
 */
public class Node<E> {
	private Node<E> next;
	private Node<E> prev;
	private E element;

	/**
  	 * Creates an empty node.
  	 */
	public Node() {
		next = null;
		element = null;
		prev = null;
	}

	/**
  	 * Creates a node storing the specified element.
 	 *
  	 * @param elem
  	 *            the element to be stored within the new node
  	 */
	public Node(E elem) {
		next = null;
		element = elem;
		prev = null;
	}

	/**
 	 * Returns the node that follows this one.
  	 *
  	 * @return the node that follows the current one
  	 */
	public Node<E> getNext() {
		return next;
	}

	/**
 	 * Returns the node that is prior to this one.
  	 *
  	 * @return the node that is previous to the current one
  	 */
	  public Node<E> getPrev() {
		return prev;
	}

	/**
 	 * Sets the node that follows this one.
 	 *
 	 * @param node
 	 *            the node to be set to follow the current one
 	 */
	public void setNext(Node<E> node) {
		next = node;
	}

	/**
 	 * Sets the node that is previous to this one.
 	 *
 	 * @param node
 	 *            the node to be set to be previous to the current one
 	 */
	  public void setPrev(Node<E> node) {
		prev = node;
	}

	/**
 	 * Returns the element stored in this node.
 	 *
 	 * @return the element stored in this node
 	 */
	public E getElement() {
		return element;
	}

	/**
 	 * Sets the element stored in this node.
  	 *
  	 * @param elem
  	 *            the element to be stored in this node
  	 */
	public void setElement(E elem) {
		element = elem;
	}

	@Override
	public String toString() {
		return "Element: " + element.toString() + " Has next: " + (next != null) + " Has previous: " + (prev != null);
	}
}
