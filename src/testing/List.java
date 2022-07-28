package testing;

public class List<Type> {

	private Element first;
	private int length;

	public List() {

	}

	public void add(Type content) {
		if (first == null) {
			first = new Element(content);
			length++;
			return;
		}
		Element current = first;
		while (current.getNext() != null) {
			current = current.getNext();
		}
		current.setNext(new Element(content));
		current.getNext().setPrevious(current);
		length++;
	}
	
	public void clear() {
		first = null;
		length = 0;
	}

	public boolean contains(Type content) {
		Element current = null;
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				current = first;
				if (current.CONTENT.equals(content)) {
					return true;
				}
				continue;
			}
			current = current.getNext();
			if (current.CONTENT.equals(content)) {
				return true;
			}
		}
		return false;
	}

	public void add(Type content, int idx) {
		Element current = first;
		if (idx == 0) {
			first = new Element(content);
			first.setNext(current);
			first.getNext().setPrevious(first);
			length++;
			return;
		}
		try {
			for (int i = 0; i < idx; i++) {
				current = current.getNext();
			}
			if (current == null) {
				add(content);
				return;
			}
			current.getPrevious().setNext(new Element(content));
			current.getPrevious().getNext().setPrevious(current.getPrevious());
			current.getPrevious().getNext().setNext(current);
			current.setPrevious(current.getPrevious().getNext());
			length++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Type get(int idx) {
		Element current = first;
		try {
			for (int i = 0; i < idx; i++) {
				current = current.getNext();
			}
			return current.getContent();
		} catch (Exception e) {
			return null;
		}
	}

	public void remove(int idx) {
		if (idx > length - 1) {
			return;
		}
		if (idx == 0) {
			first = first.getNext();
			first.setPrevious(null);
			length--;
			return;
		}
		Element current = first;
		try {
			for (int i = 0; i < idx; i++) {
				current = current.getNext();
			}
			current.getPrevious().setNext(current.getNext());
			if (current.getNext() != null) {
				current.getNext().setPrevious(current.getPrevious());
			}
			length--;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int length() {
		return length;
	}

	private class Element {
		private Element next, previous;
		private final Type CONTENT;

		public Element(Type content) {
			CONTENT = content;
		}

		public void setNext(Element next) {
			this.next = next;
		}

		public Element getNext() {
			return next;
		}

		public void setPrevious(Element previous) {
			this.previous = previous;
		}

		public Element getPrevious() {
			return previous;
		}

		public Type getContent() {
			return CONTENT;
		}
	}

}
