//package com.jiaboyan.collection.concurrent;
//
//import java.util.*;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * Created by jiaboyan on 2017/10/25.
// */
//public class CopyOnWriteArrayListCodeSource {
//
//    public class CopyOnWriteArrayList<E>
//            implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
//        private static final long serialVersionUID = 8673264195747942595L;
//
//        //ReentrantLock锁，没有使用Synchronized
//        transient final ReentrantLock lock = new ReentrantLock();
//
//        //集合底层数据结构：数组（volatile修饰）
//        private volatile transient Object[] array;
//
//        //CopyOnWrite容器中重要方法：获取底层数组
//        final Object[] getArray() {
//            return array;
//        }
//
//        //CopyOnWrite容器中重要方法：设置底层数组
//        final void setArray(Object[] a) {
//            array = a;
//        }
//
//        //构造方法：
//        public CopyOnWriteArrayList() {
//            setArray(new Object[0]);
//        }
//        public CopyOnWriteArrayList(Collection<? extends E> c) {
//            Object[] elements = c.toArray();
//            if (elements.getClass() != Object[].class)
//                elements = Arrays.copyOf(elements, elements.length, Object[].class);
//            setArray(elements);
//        }
//        public CopyOnWriteArrayList(E[] toCopyIn) {
//            setArray(Arrays.copyOf(toCopyIn, toCopyIn.length, Object[].class));
//        }
//
//        public int size() {
//            return getArray().length;
//        }
//        public boolean isEmpty() {
//            return size() == 0;
//        }
//        private static boolean eq(Object o1, Object o2) {
//            return (o1 == null ? o2 == null : o1.equals(o2));
//        }
//        private static int indexOf(Object o, Object[] elements,
//                                   int index, int fence) {
//            if (o == null) {
//                for (int i = index; i < fence; i++)
//                    if (elements[i] == null)
//                        return i;
//            } else {
//                for (int i = index; i < fence; i++)
//                    if (o.equals(elements[i]))
//                        return i;
//            }
//            return -1;
//        }
//        private static int lastIndexOf(Object o, Object[] elements, int index) {
//            if (o == null) {
//                for (int i = index; i >= 0; i--)
//                    if (elements[i] == null)
//                        return i;
//            } else {
//                for (int i = index; i >= 0; i--)
//                    if (o.equals(elements[i]))
//                        return i;
//            }
//            return -1;
//        }
//        public boolean contains(Object o) {
//            Object[] elements = getArray();
//            return indexOf(o, elements, 0, elements.length) >= 0;
//        }
//        public int indexOf(Object o) {
//            Object[] elements = getArray();
//            return indexOf(o, elements, 0, elements.length);
//        }
//        public int indexOf(E e, int index) {
//            Object[] elements = getArray();
//            return indexOf(e, elements, index, elements.length);
//        }
//        public int lastIndexOf(Object o) {
//            Object[] elements = getArray();
//            return lastIndexOf(o, elements, elements.length - 1);
//        }
//        public int lastIndexOf(E e, int index) {
//            Object[] elements = getArray();
//            return lastIndexOf(e, elements, index);
//        }
//        public Object clone() {
//            try {
//                java.util.concurrent.CopyOnWriteArrayList c = (java.util.concurrent.CopyOnWriteArrayList)(super.clone());
//                c.resetLock();
//                return c;
//            } catch (CloneNotSupportedException e) {
//                throw new InternalError();
//            }
//        }
//        public Object[] toArray() {
//            Object[] elements = getArray();
//            return Arrays.copyOf(elements, elements.length);
//        }
//        @SuppressWarnings("unchecked")
//        public <T> T[] toArray(T a[]) {
//            Object[] elements = getArray();
//            int len = elements.length;
//            if (a.length < len)
//                return (T[]) Arrays.copyOf(elements, len, a.getClass());
//            else {
//                System.arraycopy(elements, 0, a, 0, len);
//                if (a.length > len)
//                    a[len] = null;
//                return a;
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        private E get(Object[] a, int index) {
//            return (E) a[index];
//        }
//
//        public E get(int index) {
//            return get(getArray(), index);
//        }
//
//        public E set(int index, E element) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                E oldValue = get(elements, index);
//
//                if (oldValue != element) {
//                    int len = elements.length;
//                    Object[] newElements = Arrays.copyOf(elements, len);
//                    newElements[index] = element;
//                    setArray(newElements);
//                } else {
//                    setArray(elements);
//                }
//                return oldValue;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public boolean add(E e) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                Object[] newElements = Arrays.copyOf(elements, len + 1);
//                newElements[len] = e;
//                setArray(newElements);
//                return true;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public void add(int index, E element) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                if (index > len || index < 0)
//                    throw new IndexOutOfBoundsException("Index: "+index+
//                            ", Size: "+len);
//                Object[] newElements;
//                int numMoved = len - index;
//                if (numMoved == 0)
//                    newElements = Arrays.copyOf(elements, len + 1);
//                else {
//                    newElements = new Object[len + 1];
//                    System.arraycopy(elements, 0, newElements, 0, index);
//                    System.arraycopy(elements, index, newElements, index + 1,
//                            numMoved);
//                }
//                newElements[index] = element;
//                setArray(newElements);
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public E remove(int index) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                E oldValue = get(elements, index);
//                int numMoved = len - index - 1;
//                if (numMoved == 0)
//                    setArray(Arrays.copyOf(elements, len - 1));
//                else {
//                    Object[] newElements = new Object[len - 1];
//                    System.arraycopy(elements, 0, newElements, 0, index);
//                    System.arraycopy(elements, index + 1, newElements, index,
//                            numMoved);
//                    setArray(newElements);
//                }
//                return oldValue;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public boolean remove(Object o) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                if (len != 0) {
//                    int newlen = len - 1;
//                    Object[] newElements = new Object[newlen];
//
//                    for (int i = 0; i < newlen; ++i) {
//                        if (eq(o, elements[i])) {
//                            // found one;  copy remaining and exit
//                            for (int k = i + 1; k < len; ++k)
//                                newElements[k-1] = elements[k];
//                            setArray(newElements);
//                            return true;
//                        } else
//                            newElements[i] = elements[i];
//                    }
//                    if (eq(o, elements[newlen])) {
//                        setArray(newElements);
//                        return true;
//                    }
//                }
//                return false;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        private void removeRange(int fromIndex, int toIndex) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//
//                if (fromIndex < 0 || toIndex > len || toIndex < fromIndex)
//                    throw new IndexOutOfBoundsException();
//                int newlen = len - (toIndex - fromIndex);
//                int numMoved = len - toIndex;
//                if (numMoved == 0)
//                    setArray(Arrays.copyOf(elements, newlen));
//                else {
//                    Object[] newElements = new Object[newlen];
//                    System.arraycopy(elements, 0, newElements, 0, fromIndex);
//                    System.arraycopy(elements, toIndex, newElements,
//                            fromIndex, numMoved);
//                    setArray(newElements);
//                }
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public boolean addIfAbsent(E e) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                Object[] newElements = new Object[len + 1];
//                for (int i = 0; i < len; ++i) {
//                    if (eq(e, elements[i]))
//                        return false; // exit, throwing away copy
//                    else
//                        newElements[i] = elements[i];
//                }
//                newElements[len] = e;
//                setArray(newElements);
//                return true;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public boolean containsAll(Collection<?> c) {
//            Object[] elements = getArray();
//            int len = elements.length;
//            for (Object e : c) {
//                if (indexOf(e, elements, 0, len) < 0)
//                    return false;
//            }
//            return true;
//        }
//
//        public boolean removeAll(Collection<?> c) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                if (len != 0) {
//                    int newlen = 0;
//                    Object[] temp = new Object[len];
//                    for (int i = 0; i < len; ++i) {
//                        Object element = elements[i];
//                        if (!c.contains(element))
//                            temp[newlen++] = element;
//                    }
//                    if (newlen != len) {
//                        setArray(Arrays.copyOf(temp, newlen));
//                        return true;
//                    }
//                }
//                return false;
//            } finally {
//                lock.unlock();
//            }
//        }
//        public boolean retainAll(Collection<?> c) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                if (len != 0) {
//                    int newlen = 0;
//                    Object[] temp = new Object[len];
//                    for (int i = 0; i < len; ++i) {
//                        Object element = elements[i];
//                        if (c.contains(element))
//                            temp[newlen++] = element;
//                    }
//                    if (newlen != len) {
//                        setArray(Arrays.copyOf(temp, newlen));
//                        return true;
//                    }
//                }
//                return false;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public int addAllAbsent(Collection<? extends E> c) {
//            Object[] cs = c.toArray();
//            if (cs.length == 0)
//                return 0;
//            Object[] uniq = new Object[cs.length];
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                int added = 0;
//                for (int i = 0; i < cs.length; ++i) { // scan for duplicates
//                    Object e = cs[i];
//                    if (indexOf(e, elements, 0, len) < 0 &&
//                            indexOf(e, uniq, 0, added) < 0)
//                        uniq[added++] = e;
//                }
//                if (added > 0) {
//                    Object[] newElements = Arrays.copyOf(elements, len + added);
//                    System.arraycopy(uniq, 0, newElements, len, added);
//                    setArray(newElements);
//                }
//                return added;
//            } finally {
//                lock.unlock();
//            }
//        }
//        public void clear() {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                setArray(new Object[0]);
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public boolean addAll(Collection<? extends E> c) {
//            Object[] cs = c.toArray();
//            if (cs.length == 0)
//                return false;
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                Object[] newElements = Arrays.copyOf(elements, len + cs.length);
//                System.arraycopy(cs, 0, newElements, len, cs.length);
//                setArray(newElements);
//                return true;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public boolean addAll(int index, Collection<? extends E> c) {
//            Object[] cs = c.toArray();
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                if (index > len || index < 0)
//                    throw new IndexOutOfBoundsException("Index: "+index+
//                            ", Size: "+len);
//                if (cs.length == 0)
//                    return false;
//                int numMoved = len - index;
//                Object[] newElements;
//                if (numMoved == 0)
//                    newElements = Arrays.copyOf(elements, len + cs.length);
//                else {
//                    newElements = new Object[len + cs.length];
//                    System.arraycopy(elements, 0, newElements, 0, index);
//                    System.arraycopy(elements, index,
//                            newElements, index + cs.length,
//                            numMoved);
//                }
//                System.arraycopy(cs, 0, newElements, index, cs.length);
//                setArray(newElements);
//                return true;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        private void writeObject(java.io.ObjectOutputStream s)
//                throws java.io.IOException{
//
//            s.defaultWriteObject();
//
//            Object[] elements = getArray();
//            // Write out array length
//            s.writeInt(elements.length);
//
//            // Write out all elements in the proper order.
//            for (Object element : elements)
//                s.writeObject(element);
//        }
//
//        private void readObject(java.io.ObjectInputStream s)
//                throws java.io.IOException, ClassNotFoundException {
//
//            s.defaultReadObject();
//
//            // bind to new lock
//            resetLock();
//
//            // Read in array length and allocate array
//            int len = s.readInt();
//            Object[] elements = new Object[len];
//
//            // Read in all elements in the proper order.
//            for (int i = 0; i < len; i++)
//                elements[i] = s.readObject();
//            setArray(elements);
//        }
//
//        public String toString() {
//            return Arrays.toString(getArray());
//        }
//
//        public boolean equals(Object o) {
//            if (o == this)
//                return true;
//            if (!(o instanceof List))
//                return false;
//
//            List<?> list = (List<?>)(o);
//            Iterator<?> it = list.iterator();
//            Object[] elements = getArray();
//            int len = elements.length;
//            for (int i = 0; i < len; ++i)
//                if (!it.hasNext() || !eq(elements[i], it.next()))
//                    return false;
//            if (it.hasNext())
//                return false;
//            return true;
//        }
//
//        public int hashCode() {
//            int hashCode = 1;
//            Object[] elements = getArray();
//            int len = elements.length;
//            for (int i = 0; i < len; ++i) {
//                Object obj = elements[i];
//                hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
//            }
//            return hashCode;
//        }
//
//        public Iterator<E> iterator() {
//            return new java.util.concurrent.CopyOnWriteArrayList.COWIterator<E>(getArray(), 0);
//        }
//
//        public ListIterator<E> listIterator() {
//            return new java.util.concurrent.CopyOnWriteArrayList.COWIterator<E>(getArray(), 0);
//        }
//
//        /**
//         * {@inheritDoc}
//         *
//         * <p>The returned iterator provides a snapshot of the state of the list
//         * when the iterator was constructed. No synchronization is needed while
//         * traversing the iterator. The iterator does <em>NOT</em> support the
//         * <tt>remove</tt>, <tt>set</tt> or <tt>add</tt> methods.
//         *
//         * @throws IndexOutOfBoundsException {@inheritDoc}
//         */
//        public ListIterator<E> listIterator(final int index) {
//            Object[] elements = getArray();
//            int len = elements.length;
//            if (index<0 || index>len)
//                throw new IndexOutOfBoundsException("Index: "+index);
//
//            return new java.util.concurrent.CopyOnWriteArrayList.COWIterator<E>(elements, index);
//        }
//
//        private static class COWIterator<E> implements ListIterator<E> {
//            /** Snapshot of the array */
//            private final Object[] snapshot;
//            /** Index of element to be returned by subsequent call to next.  */
//            private int cursor;
//
//            private COWIterator(Object[] elements, int initialCursor) {
//                cursor = initialCursor;
//                snapshot = elements;
//            }
//
//            public boolean hasNext() {
//                return cursor < snapshot.length;
//            }
//
//            public boolean hasPrevious() {
//                return cursor > 0;
//            }
//
//            @SuppressWarnings("unchecked")
//            public E next() {
//                if (! hasNext())
//                    throw new NoSuchElementException();
//                return (E) snapshot[cursor++];
//            }
//
//            @SuppressWarnings("unchecked")
//            public E previous() {
//                if (! hasPrevious())
//                    throw new NoSuchElementException();
//                return (E) snapshot[--cursor];
//            }
//
//            public int nextIndex() {
//                return cursor;
//            }
//
//            public int previousIndex() {
//                return cursor-1;
//            }
//
//            /**
//             * Not supported. Always throws UnsupportedOperationException.
//             * @throws UnsupportedOperationException always; <tt>remove</tt>
//             *         is not supported by this iterator.
//             */
//            public void remove() {
//                throw new UnsupportedOperationException();
//            }
//
//            /**
//             * Not supported. Always throws UnsupportedOperationException.
//             * @throws UnsupportedOperationException always; <tt>set</tt>
//             *         is not supported by this iterator.
//             */
//            public void set(E e) {
//                throw new UnsupportedOperationException();
//            }
//
//            /**
//             * Not supported. Always throws UnsupportedOperationException.
//             * @throws UnsupportedOperationException always; <tt>add</tt>
//             *         is not supported by this iterator.
//             */
//            public void add(E e) {
//                throw new UnsupportedOperationException();
//            }
//        }
//
//        /**
//         * Returns a view of the portion of this list between
//         * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.
//         * The returned list is backed by this list, so changes in the
//         * returned list are reflected in this list.
//         *
//         * <p>The semantics of the list returned by this method become
//         * undefined if the backing list (i.e., this list) is modified in
//         * any way other than via the returned list.
//         *
//         * @param fromIndex low endpoint (inclusive) of the subList
//         * @param toIndex high endpoint (exclusive) of the subList
//         * @return a view of the specified range within this list
//         * @throws IndexOutOfBoundsException {@inheritDoc}
//         */
//        public List<E> subList(int fromIndex, int toIndex) {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                Object[] elements = getArray();
//                int len = elements.length;
//                if (fromIndex < 0 || toIndex > len || fromIndex > toIndex)
//                    throw new IndexOutOfBoundsException();
//                return new java.util.concurrent.CopyOnWriteArrayList.COWSubList<E>(this, fromIndex, toIndex);
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        /**
//         * Sublist for CopyOnWriteArrayList.
//         * This class extends AbstractList merely for convenience, to
//         * avoid having to define addAll, etc. This doesn't hurt, but
//         * is wasteful.  This class does not need or use modCount
//         * mechanics in AbstractList, but does need to check for
//         * concurrent modification using similar mechanics.  On each
//         * operation, the array that we expect the backing list to use
//         * is checked and updated.  Since we do this for all of the
//         * base operations invoked by those defined in AbstractList,
//         * all is well.  While inefficient, this is not worth
//         * improving.  The kinds of list operations inherited from
//         * AbstractList are already so slow on COW sublists that
//         * adding a bit more space/time doesn't seem even noticeable.
//         */
//        private static class COWSubList<E>
//                extends AbstractList<E>
//                implements RandomAccess
//        {
//            private final java.util.concurrent.CopyOnWriteArrayList<E> l;
//            private final int offset;
//            private int size;
//            private Object[] expectedArray;
//
//            // only call this holding l's lock
//            COWSubList(java.util.concurrent.CopyOnWriteArrayList<E> list,
//                       int fromIndex, int toIndex) {
//                l = list;
//                expectedArray = l.getArray();
//                offset = fromIndex;
//                size = toIndex - fromIndex;
//            }
//
//            // only call this holding l's lock
//            private void checkForComodification() {
//                if (l.getArray() != expectedArray)
//                    throw new ConcurrentModificationException();
//            }
//
//            // only call this holding l's lock
//            private void rangeCheck(int index) {
//                if (index<0 || index>=size)
//                    throw new IndexOutOfBoundsException("Index: "+index+
//                            ",Size: "+size);
//            }
//
//            public E set(int index, E element) {
//                final ReentrantLock lock = l.lock;
//                lock.lock();
//                try {
//                    rangeCheck(index);
//                    checkForComodification();
//                    E x = l.set(index+offset, element);
//                    expectedArray = l.getArray();
//                    return x;
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public E get(int index) {
//                final ReentrantLock lock = l.lock;
//                lock.lock();
//                try {
//                    rangeCheck(index);
//                    checkForComodification();
//                    return l.get(index+offset);
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public int size() {
//                final ReentrantLock lock = l.lock;
//                lock.lock();
//                try {
//                    checkForComodification();
//                    return size;
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public void add(int index, E element) {
//                final ReentrantLock lock = l.lock;
//                lock.lock();
//                try {
//                    checkForComodification();
//                    if (index<0 || index>size)
//                        throw new IndexOutOfBoundsException();
//                    l.add(index+offset, element);
//                    expectedArray = l.getArray();
//                    size++;
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public void clear() {
//                final ReentrantLock lock = l.lock;
//                lock.lock();
//                try {
//                    checkForComodification();
//                    l.removeRange(offset, offset+size);
//                    expectedArray = l.getArray();
//                    size = 0;
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public E remove(int index) {
//                final ReentrantLock lock = l.lock;
//                lock.lock();
//                try {
//                    rangeCheck(index);
//                    checkForComodification();
//                    E result = l.remove(index+offset);
//                    expectedArray = l.getArray();
//                    size--;
//                    return result;
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public boolean remove(Object o) {
//                int index = indexOf(o);
//                if (index == -1)
//                    return false;
//                remove(index);
//                return true;
//            }
//
//            public Iterator<E> iterator() {
//                final ReentrantLock lock = l.lock;
//                lock.lock();
//                try {
//                    checkForComodification();
//                    return new java.util.concurrent.CopyOnWriteArrayList.COWSubListIterator<E>(l, 0, offset, size);
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public ListIterator<E> listIterator(final int index) {
//                final ReentrantLock lock = l.lock;
//                lock.lock();
//                try {
//                    checkForComodification();
//                    if (index<0 || index>size)
//                        throw new IndexOutOfBoundsException("Index: "+index+
//                                ", Size: "+size);
//                    return new java.util.concurrent.CopyOnWriteArrayList.COWSubListIterator<E>(l, index, offset, size);
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public List<E> subList(int fromIndex, int toIndex) {
//                final ReentrantLock lock = l.lock;
//                lock.lock();
//                try {
//                    checkForComodification();
//                    if (fromIndex<0 || toIndex>size)
//                        throw new IndexOutOfBoundsException();
//                    return new java.util.concurrent.CopyOnWriteArrayList.COWSubList<E>(l, fromIndex + offset,
//                            toIndex + offset);
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//        }
//
//
//        private static class COWSubListIterator<E> implements ListIterator<E> {
//            private final ListIterator<E> i;
//            private final int index;
//            private final int offset;
//            private final int size;
//
//            COWSubListIterator(List<E> l, int index, int offset,
//                               int size) {
//                this.index = index;
//                this.offset = offset;
//                this.size = size;
//                i = l.listIterator(index+offset);
//            }
//
//            public boolean hasNext() {
//                return nextIndex() < size;
//            }
//
//            public E next() {
//                if (hasNext())
//                    return i.next();
//                else
//                    throw new NoSuchElementException();
//            }
//
//            public boolean hasPrevious() {
//                return previousIndex() >= 0;
//            }
//
//            public E previous() {
//                if (hasPrevious())
//                    return i.previous();
//                else
//                    throw new NoSuchElementException();
//            }
//
//            public int nextIndex() {
//                return i.nextIndex() - offset;
//            }
//
//            public int previousIndex() {
//                return i.previousIndex() - offset;
//            }
//
//            public void remove() {
//                throw new UnsupportedOperationException();
//            }
//
//            public void set(E e) {
//                throw new UnsupportedOperationException();
//            }
//
//            public void add(E e) {
//                throw new UnsupportedOperationException();
//            }
//        }
//
//        // Support for resetting lock while deserializing
//        private void resetLock() {
//            UNSAFE.putObjectVolatile(this, lockOffset, new ReentrantLock());
//        }
//        private static final sun.misc.Unsafe UNSAFE;
//        private static final long lockOffset;
//        static {
//            try {
//                UNSAFE = sun.misc.Unsafe.getUnsafe();
//                Class k = java.util.concurrent.CopyOnWriteArrayList.class;
//                lockOffset = UNSAFE.objectFieldOffset
//                        (k.getDeclaredField("lock"));
//            } catch (Exception e) {
//                throw new Error(e);
//            }
//        }
//    }
//
//
//
//
//
//}
