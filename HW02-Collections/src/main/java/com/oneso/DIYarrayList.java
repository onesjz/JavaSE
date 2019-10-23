package com.oneso;

import java.util.*;

public class DIYarrayList<T> implements List<T> {

  private Object[] elements;

  private static final Object[] EMPTY = {};
  private static final int DEFAULT_CAPACITY = 10;

  private int size;

  public DIYarrayList(Collection<? extends T> collection) {
    elements = collection.toArray();

    if ((size = elements.length) != 0) {
      if (elements.getClass() != Object[].class)
        elements = Arrays.copyOf(elements, size, Object[].class);
    } else {
      elements = EMPTY;
    }
  }

  public DIYarrayList(int initSize) {
    if (initSize < 0)
      throw new IllegalArgumentException("Size < 0");

    elements = new Object[initSize];
  }

  public DIYarrayList() {
    this.elements = EMPTY;
    size = 0;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public boolean contains(Object o) {
    return get(indexOf(o)) != null;
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      int cursor;

      @Override
      public boolean hasNext() {
        return cursor != size;
      }

      @Override
      public T next() {
        int i = cursor;
        if (i > size)
          throw new NoSuchElementException();
        cursor++;
        return (T) elements[i];
      }
    };
  }

  @Override
  public Object[] toArray() {
    return elements;
  }

  @Override
  public <T1> T1[] toArray(T1[] a) {
    throw new UnsupportedOperationException("Doesn't support");
  }

  @Override
  public boolean add(T t) {
    if (size == elements.length) {
      elements = grow();
    }

    elements[size] = t;
    size++;
    return true;
  }

  @Override
  public boolean remove(Object o) {
    return remove(indexOf(o)) != null;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    throw new UnsupportedOperationException("Doesn't support");
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    Object[] newElements = c.toArray();
    int newElemSize = newElements.length;

    if (newElemSize == 0)
      return false;

    int newSize = size + newElemSize;
    Object[] tempElements = new Object[newSize];

    for (int i = 0; i < newSize; i++) {
      if (i < size)
        tempElements[i] = elements[i];
      else
        tempElements[i] = newElements[i];
    }

    elements = tempElements;
    size = newSize;
    return true;
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    Object[] newElements = c.toArray();
    int newElementSize = newElements.length;

    if (newElementSize == 0 || index < 0)
      return false;

    int newSize = size + newElementSize;
    Object[] tempElements = new Object[newSize];

    for (int i = 0; i < newSize; i++) {
      if (i >= index && i < newElementSize)
        tempElements[i] = newElements[i];
      else
        tempElements[i] = elements[i];
    }

    elements = tempElements;
    if (size <= newSize)
      size = newSize;
    return true;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException("Doesn't support");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException("Doesn't support");
  }

  @Override
  public void clear() {
    elements = EMPTY;
    size = 0;
  }

  @Override
  public T get(int index) {
    if (index < 0 || index > size)
      throw new ArrayIndexOutOfBoundsException("index < 0 || index > size");

    return (T) elements[index];
  }

  @Override
  public T set(int index, T element) {
    if (index < 0 || index > size)
      throw new ArrayIndexOutOfBoundsException("index < 0 || index > size");

    T oldType = (T) elements[index];
    elements[index] = element;
    return oldType;
  }

  @Override
  public void add(int index, T element) {
    throw new UnsupportedOperationException("Doesn't support");
  }

  @Override
  public T remove(int index) {
    if (index < 0 || index > size)
      throw new ArrayIndexOutOfBoundsException("index < 0 || index > size");

    T oldType = (T) elements[index];
    if (size - 1 > index) {
      System.arraycopy(elements, index + 1, elements, index, size - index - 1);
    }
    elements[size] = null;
    size--;

    int oldCapacity = elements.length;
    if (oldCapacity > size) {
      int newCapacity = (oldCapacity - size) / 2;
      elements = (size == 0) ? EMPTY : Arrays.copyOf(elements, newCapacity);
    }

    return oldType;
  }

  @Override
  public int indexOf(Object o) {
    if (o == null || size == 0)
      return -1;

    for (int i = 0; i < size; i++) {
      if (o.equals(elements[i]))
        return i;
    }
    return -1;
  }

  @Override
  public int lastIndexOf(Object o) {
    if (o == null || size == 0)
      return -1;

    for (int i = size - 1; i >= 0; i--) {
      if (o.equals(elements[i]))
        return i;
    }
    return -1;
  }

  @Override
  public ListIterator<T> listIterator() {
    return new ListIterator<T>() {
      int cursor;
      int lastRet;

      @Override
      public boolean hasNext() {
        return cursor != size;
      }

      @Override
      public T next() {
        int i = cursor;
        if (i >= size)
          throw new NoSuchElementException();

        Object[] elementData = elements;
        cursor = i + 1;
        return (T) elementData[lastRet = i];
      }

      @Override
      public boolean hasPrevious() {
        return cursor != 0;
      }

      @Override
      public T previous() {
        throw new UnsupportedOperationException("Doesn't support");
      }

      @Override
      public int nextIndex() {
        return cursor;
      }

      @Override
      public int previousIndex() {
        return cursor - 1;
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Doesn't support");
      }

      @Override
      public void set(T t) {
        DIYarrayList.this.set(lastRet, t);
      }

      @Override
      public void add(T t) {
        throw new UnsupportedOperationException("Doesn't support");
      }
    };
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    throw new UnsupportedOperationException("Doesn't support");
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException("Doesn't support");
  }

  @Override
  public void sort(Comparator<? super T> c) {
    Arrays.sort((T[]) elements, 0, size, c);
  }

  private Object[] grow() {
    int oldCapacity = elements.length;
    if (oldCapacity > 0 || elements != EMPTY) {
      int newCapacity = oldCapacity * 2;
      Object[] tempElements = new Object[newCapacity];
      System.arraycopy(elements, 0, tempElements, 0, size);
      return tempElements;
    } else {
      return new Object[Math.max(DEFAULT_CAPACITY, size)];
    }
  }
}
