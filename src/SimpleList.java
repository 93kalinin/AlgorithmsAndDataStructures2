
interface SimpleList<T> extends Iterable<T> {

    class Node<T> {

        private T value;
        private Node<T> next;

        Node(T value, Node<T> next) {

            this.value = value;
            this.next = next;
        }

        T getValue() { return value; }
        void setValue(T value) { this.value = value; }
        Node<T> getNext() { return next; }
        void setNext(Node<T> next) { this.next = next; }
    }

    void insertAt(T newValue, int index);
    int indexOf(T targetValue);
    T valueOf(int index);
    T remove(int index);
}
