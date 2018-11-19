//package com.jiaboyan.collection.list;
//
//import java.util.*;
//
///**
// * Created by jiaboyan on 2017/7/29.
// */
//public class LinkedListCodeSource {
//
//    public class LinkedList<E>
//            extends AbstractSequentialList<E>
//            implements List<E>, Deque<E>, Cloneable, java.io.Serializable {
//
//        //LinkedList的元素个数：
//        transient int size = 0;
//
//        //LinkedList的头结点：Node内部类
//        transient java.util.LinkedList.Node<E> first;
//
//        //LinkedList尾结点：Node内部类
//        transient java.util.LinkedList.Node<E> last;
//
//        //空实现：
//        public LinkedList() {
//        }
//
//        //调用添加方法：
//        public LinkedList(Collection<? extends E> c) {
//            this();
//            addAll(c);
//        }
//
//        //LinkedList添加首结点 first：
//        public void addFirst(E e) {
//            linkFirst(e);
//        }
//        //first节点插入新元素：addFirst()调用
//        private void linkFirst(E e) {
//            //头结点：
//            final java.util.LinkedList.Node<E> f = first;
//            //创建一个新节点，并指向头结点f：
//            final java.util.LinkedList.Node<E> newNode = new java.util.LinkedList.Node<>(null, e, f);
//            //将新节点赋值给头几点：
//            first = newNode;
//            //如果头节点为null，则是第一个元素插入，将新节点也设置为尾结点；
//            if (f == null)
//                last = newNode;
//            else
//                //将之前的头结点的前指针指向新的结点：
//                f.prev = newNode;
//            //长度+1
//            size++;
//            //操作数+1
//            modCount++;
//        }
//
//        //添加元素：添加到最后一个结点；
//        public boolean add(E e) {
//            linkLast(e);
//            return true;
//        }
//
//        //添加最后一个结点 last：
//        public void addLast(E e) {
//            linkLast(e);
//        }
//
//        //last节点插入新元素：addLast()调用
//        void linkLast(E e) {
//            //将尾结点赋值个体L:
//            final java.util.LinkedList.Node<E> l = last;
//            //创建新的结点，将新节点的前指针指向l:
//            final java.util.LinkedList.Node<E> newNode = new java.util.LinkedList.Node<>(l, e, null);
//            //新节点置为尾结点：
//            last = newNode;
//            //如果尾结点l为null：则是空集合新插入
//            if (l == null)
//                //头结点也置为 新节点：
//                first = newNode;
//            else
//                //l节点的后指针指向新节点：
//                l.next = newNode;
//            //长度+1
//            size++;
//            //操作数+1
//            modCount++;
//        }
//
//        //向对应角标添加元素：
//        public void add(int index, E element) {
//            //检查传入的角标 是否正确：
//            checkPositionIndex(index);
//            //如果插入角标==集合长度，则插入到集合的最后面：
//            if (index == size)
//                linkLast(element);
//            else
//                //插入到对应角标的位置：获取此角标下的元素先
//                linkBefore(element, node(index));
//        }
//        //在succ前插入 新元素e：
//        void linkBefore(E e, java.util.LinkedList.Node<E> succ) {
//            //获取被插入元素succ的前指针元素：
//            final java.util.LinkedList.Node<E> pred = succ.prev;
//            //创建新增元素节点，前指针 和 后指针分别指向对应元素：
//            final java.util.LinkedList.Node<E> newNode = new java.util.LinkedList.Node<>(pred, e, succ);
//            succ.prev = newNode;
//            //succ的前指针元素可能为null，为null的话说明succ是头结点，则把新建立的结点置为头结点：
//            if (pred == null)
//                first = newNode;
//            else
//                //succ前指针不为null，则将前指针的结点的后指针指向新节点：
//                pred.next = newNode;
//            //长度+1
//            size++;
//            //操作数+1
//            modCount++;
//        }
//        //移除首个结点：如果集合还没有元素则报错
//        public E removeFirst() {
//            final java.util.LinkedList.Node<E> f = first;
//            //首节点为null，则抛出异常；
//            if (f == null)
//                throw new NoSuchElementException();
//            return unlinkFirst(f);
//        }
//
//        //删除LinkedList的头结点：removeFirst()方法调用
//        private E unlinkFirst(java.util.LinkedList.Node<E> f) {
//            //f为头结点：获取头结点元素E
//            final E element = f.item;
//            //获取头结点的尾指针结点：
//            final java.util.LinkedList.Node<E> next = f.next;
//            //将头结点元素置为null：
//            f.item = null;
//            //头结点尾指针置为null：
//            f.next = null;
//            //将头结点的尾指针指向的结点next置为first
//            first = next;
//            //尾指针指向结点next为null的话，就将尾结点也置为null（LinkedList中只有一个元素时出现）
//            if (next == null)
//                last = null;
//            else
//                //将尾指针指向结点next的 前指针置为null；
//                next.prev = null;
//            // 长度减1
//            size--;
//            //操作数+1
//            modCount++;
//            //返回被删除的元素：
//            return element;
//        }
//
//        //移除最后一个结点：如果集合还没有元素则报错
//        public E removeLast() {
//            //获取最后一个结点：
//            final java.util.LinkedList.Node<E> l = last;
//            if (l == null)
//                throw new NoSuchElementException();
//            //删除尾结点：
//            return unlinkLast(l);
//        }
//        //删除LinkedList的尾结点：removeLast()方法调用
//        private E unlinkLast(java.util.LinkedList.Node<E> l) {
//            final E element = l.item;
//            final java.util.LinkedList.Node<E> prev = l.prev;
//            l.item = null;
//            l.prev = null; // help GC
//            last = prev;
//            if (prev == null)
//                first = null;
//            else
//                prev.next = null;
//            size--;
//            modCount++;
//            return element;
//        }
//
//        //删除LinkedList中的元素，可以删除为null的元素，逐个遍历LinkedList的元素；
//        //重复元素只删除第一个：
//        public boolean remove(Object o) {
//            //如果删除元素为null：
//            if (o == null) {
//                for (java.util.LinkedList.Node<E> x = first; x != null; x = x.next) {
//                    if (x.item == null) {
//                        unlink(x);
//                        return true;
//                    }
//                }
//            } else {
//                //如果删除元素不为null：
//                for (java.util.LinkedList.Node<E> x = first; x != null; x = x.next) {
//                    if (o.equals(x.item)) {
//                        unlink(x);
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//        //移除LinkedList结点：remove()方法中调用
//        E unlink(java.util.LinkedList.Node<E> x) {
//            //获取被删除结点的元素E：
//            final E element = x.item;
//            //获取被删除元素的后指针结点：
//            final java.util.LinkedList.Node<E> next = x.next;
//            //获取被删除元素的前指针结点：
//            final java.util.LinkedList.Node<E> prev = x.prev;
//
//            //被删除结点的 前结点为null的话：
//            if (prev == null) {
//                //将后指针指向的结点置为头结点
//                first = next;
//            } else {
//                //前置结点的  尾结点指向被删除的next结点；
//                prev.next = next;
//                //被删除结点前指针置为null:
//                x.prev = null;
//            }
//            //对尾结点同样处理：
//            if (next == null) {
//                last = prev;
//            } else {
//                next.prev = prev;
//                x.next = null;
//            }
//
//            x.item = null;
//            size--;
//            modCount++;
//            return element;
//        }
//
//        //得到首个结点：集合没有元素报错
//        public E getFirst() {
//            //获取first结点：
//            final java.util.LinkedList.Node<E> f = first;
//            if (f == null)
//                throw new NoSuchElementException();
//            return f.item;
//        }
//
//        //得到最后一个结点：集合没有元素报错
//        public E getLast() {
//            //获取last结点：
//            final java.util.LinkedList.Node<E> l = last;
//            if (l == null)
//                throw new NoSuchElementException();
//            return l.item;
//        }
//
//        //判断obj 是否存在：
//        public boolean contains(Object o) {
//            return indexOf(o) != -1;
//        }
//        //LinkedList长度：
//        public int size() {
//            return size;
//        }
//
//        //添加集合：从最后size所在的index开始：
//        public boolean addAll(Collection<? extends E> c) {
//            return addAll(size, c);
//        }
//        //LinkedList添加集合：
//        public boolean addAll(int index, Collection<? extends E> c) {
//            //检查角标是否正确：
//            checkPositionIndex(index);
//            Object[] a = c.toArray();
//            int numNew = a.length;
//            if (numNew == 0)
//                return false;
//            java.util.LinkedList.Node<E> pred, succ;
//            if (index == size) {
//                succ = null;
//                pred = last;
//            } else {
//                succ = node(index);
//                pred = succ.prev;
//            }
//            for (Object o : a) {
//                E e = (E) o;
//                java.util.LinkedList.Node<E> newNode = new java.util.LinkedList.Node<>(pred, e, null);
//                if (pred == null)
//                    first = newNode;
//                else
//                    pred.next = newNode;
//                pred = newNode;
//            }
//            if (succ == null) {
//                last = pred;
//            } else {
//                pred.next = succ;
//                succ.prev = pred;
//            }
//            size += numNew;
//            modCount++;
//            return true;
//        }
//
//        //清空LinkedList：
//        public void clear() {
//            //遍历LinedList集合：
//            for (java.util.LinkedList.Node<E> x = first; x != null; ) {
//                //将每个结点的前指针 尾指针  元素都置为null：
//                java.util.LinkedList.Node<E> next = x.next;
//                x.item = null;
//                x.next = null;
//                x.prev = null;
//                x = next;
//            }
//            //头尾结点 都置为null：
//            first = last = null;
//            //长度置为0
//            size = 0;
//            //操作数+1：
//            modCount++;
//        }
//
//        //获取相应角标的元素：
//        public E get(int index) {
//            //检查角标是否正确：
//            checkElementIndex(index);
//            //获取角标所属结点的 元素值：
//            return node(index).item;
//        }
//
//        //设置对应角标的元素：
//        public E set(int index, E element) {
//            checkElementIndex(index);
//            java.util.LinkedList.Node<E> x = node(index);
//            E oldVal = x.item;
//            x.item = element;
//            return oldVal;
//        }
//
//        //删除对应角标的元素：
//        public E remove(int index) {
//            checkElementIndex(index);
//            return unlink(node(index));
//        }
//
//        //获取对应角标所属于的结点：
//        java.util.LinkedList.Node<E> node(int index) {
//            //位运算：如果位置索引小于列表长度的一半，从前面开始遍历；否则，从后面开始遍历；
//            if (index < (size >> 1)) {
//                java.util.LinkedList.Node<E> x = first;
//                //从头结点开始遍历：遍历的长度就是index的长度，获取对应的index的元素
//                for (int i = 0; i < index; i++)
//                    x = x.next;
//                return x;
//            } else {
//                //从集合尾结点遍历：
//                java.util.LinkedList.Node<E> x = last;
//                //同样道理：
//                for (int i = size - 1; i > index; i--)
//                    x = x.prev;
//                return x;
//            }
//        }
//        // 左移相当于*2，只是要注意边界问题。如char a = 65； a<<1 按照*2来算为130;
//        // 但有符号char的取值范围-128~127，已经越界，多超出了3个数值，
//        // 所以从-128算起的第三个数值-126才是a<<1的正确结果。
//        //而右移相当于除以2，只是要注意移位比较多的时候结果会趋近去一个非常小的数，如上面结果中的-1，0。
//
//        private boolean isElementIndex(int index) {
//            return index >= 0 && index < size;
//        }
//
//        //判断传入的index是否正确：
//        private boolean isPositionIndex(int index) {
//            return index >= 0 && index <= size;
//        }
//
//        private String outOfBoundsMsg(int index) {
//            return "Index: "+index+", Size: "+size;
//        }
//
//        private void checkElementIndex(int index) {
//            if (!isElementIndex(index))
//                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
//        }
//
//        //检查传入的角标 是否正确：
//        private void checkPositionIndex(int index) {
//            //必须大于0 ，并且不能大于当前size：
//            if (!isPositionIndex(index))
//                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
//        }
//        public int indexOf(Object o) {
//            int index = 0;
//            if (o == null) {
//                for (java.util.LinkedList.Node<E> x = first; x != null; x = x.next) {
//                    if (x.item == null)
//                        return index;
//                    index++;
//                }
//            } else {
//                for (java.util.LinkedList.Node<E> x = first; x != null; x = x.next) {
//                    if (o.equals(x.item))
//                        return index;
//                    index++;
//                }
//            }
//            return -1;
//        }
//        public int lastIndexOf(Object o) {
//            int index = size;
//            if (o == null) {
//                for (java.util.LinkedList.Node<E> x = last; x != null; x = x.prev) {
//                    index--;
//                    if (x.item == null)
//                        return index;
//                }
//            } else {
//                for (java.util.LinkedList.Node<E> x = last; x != null; x = x.prev) {
//                    index--;
//                    if (o.equals(x.item))
//                        return index;
//                }
//            }
//            return -1;
//        }
//
//        //获取第一个元素，不存在则抛异常
//        public E element() {
//            return getFirst();
//        }
//
//        //出队，获取第一个元素，不出队列，只是获取
//        // 队列先进先出；不存在不抛异常，返回null
//        public E peek() {
//            //获取头结点：
//            final java.util.LinkedList.Node<E> f = first;
//            //存在获取头结点元素，不存在返回null
//            return (f == null) ? null : f.item;
//        }
//
//        //出队，并移除第一个元素；不存在不抛异常。
//        public E poll() {
//            final java.util.LinkedList.Node<E> f = first;
//            return (f == null) ? null : unlinkFirst(f);
//        }
//
//        //出队（删除第一个结点），如果不存在会抛出异常而不是返回null，存在的话会返回值并移除这个元素（节点）
//        public E remove() {
//            return removeFirst();
//        }
//
//        //入队(插入最后一个结点)从最后一个元素
//        public boolean offer(E e) {
//            return add(e);
//        }
//
//        //入队（插入头结点），始终返回true
//        public boolean offerFirst(E e) {
//            addFirst(e);
//            return true;
//        }
//
//        //入队（插入尾结点），始终返回true
//        public boolean offerLast(E e) {
//            addLast(e);
//            return true;
//        }
//
//        //出队（从前端），获得第一个元素，不存在会返回null，不会删除元素（节点）
//        public E peekFirst() {
//            final java.util.LinkedList.Node<E> f = first;
//            return (f == null) ? null : f.item;
//        }
//
//        //出队（从后端），获得最后一个元素，不存在会返回null，不会删除元素（节点）
//        public E peekLast() {
//            final java.util.LinkedList.Node<E> l = last;
//            return (l == null) ? null : l.item;
//        }
//
//        //出队（从前端），获得第一个元素，不存在会返回null，会删除元素（节点）
//        public E pollFirst() {
//            final java.util.LinkedList.Node<E> f = first;
//            return (f == null) ? null : unlinkFirst(f);
//        }
//
//        //出队（从后端），获得最后一个元素，不存在会返回null，会删除元素（节点）
//        public E pollLast() {
//            final java.util.LinkedList.Node<E> l = last;
//            return (l == null) ? null : unlinkLast(l);
//        }
//
//        //入栈，从前面添加  栈 后进先出
//        public void push(E e) {
//            addFirst(e);
//        }
//
//        //出栈，返回栈顶元素，从前面移除（会删除）
//        public E pop() {
//            return removeFirst();
//        }
//
//        //节点的数据结构，包含前后节点的引用和当前节点
//        private static class Node<E> {
//            //结点元素：
//            E item;
//            //结点后指针
//            java.util.LinkedList.Node<E> next;
//            //结点前指针
//            java.util.LinkedList.Node<E> prev;
//
//            Node(java.util.LinkedList.Node<E> prev, E element, java.util.LinkedList.Node<E> next) {
//                this.item = element;
//                this.next = next;
//                this.prev = prev;
//            }
//        }
//
//        //迭代器：
//        public ListIterator<E> listIterator(int index) {
//            checkPositionIndex(index);
//            return new java.util.LinkedList.ListItr(index);
//        }
//        private class ListItr implements ListIterator<E> {
//            private java.util.LinkedList.Node<E> lastReturned = null;
//            private java.util.LinkedList.Node<E> next;
//            private int nextIndex;
//            private int expectedModCount = modCount;
//
//            ListItr(int index) {
//                next = (index == size) ? null : node(index);
//                nextIndex = index;
//            }
//
//            public boolean hasNext() {
//                return nextIndex < size;
//            }
//
//            public E next() {
//                checkForComodification();
//                if (!hasNext())
//                    throw new NoSuchElementException();
//
//                lastReturned = next;
//                next = next.next;
//                nextIndex++;
//                return lastReturned.item;
//            }
//
//            public boolean hasPrevious() {
//                return nextIndex > 0;
//            }
//
//            public E previous() {
//                checkForComodification();
//                if (!hasPrevious())
//                    throw new NoSuchElementException();
//
//                lastReturned = next = (next == null) ? last : next.prev;
//                nextIndex--;
//                return lastReturned.item;
//            }
//
//            public int nextIndex() {
//                return nextIndex;
//            }
//
//            public int previousIndex() {
//                return nextIndex - 1;
//            }
//
//            public void remove() {
//                checkForComodification();
//                if (lastReturned == null)
//                    throw new IllegalStateException();
//
//                java.util.LinkedList.Node<E> lastNext = lastReturned.next;
//                unlink(lastReturned);
//                if (next == lastReturned)
//                    next = lastNext;
//                else
//                    nextIndex--;
//                lastReturned = null;
//                expectedModCount++;
//            }
//
//            public void set(E e) {
//                if (lastReturned == null)
//                    throw new IllegalStateException();
//                checkForComodification();
//                lastReturned.item = e;
//            }
//
//            public void add(E e) {
//                checkForComodification();
//                lastReturned = null;
//                if (next == null)
//                    linkLast(e);
//                else
//                    linkBefore(e, next);
//                nextIndex++;
//                expectedModCount++;
//            }
//
//            final void checkForComodification() {
//                if (modCount != expectedModCount)
//                    throw new ConcurrentModificationException();
//            }
//        }
//
//        private java.util.LinkedList<E> superClone() {
//            try {
//                return (java.util.LinkedList<E>) super.clone();
//            } catch (CloneNotSupportedException e) {
//                throw new InternalError();
//            }
//        }
//
//        public Object clone() {
//            java.util.LinkedList<E> clone = superClone();
//            clone.first = clone.last = null;
//            clone.size = 0;
//            clone.modCount = 0;
//            for (java.util.LinkedList.Node<E> x = first; x != null; x = x.next)
//                clone.add(x.item);
//            return clone;
//        }
//
//        public Object[] toArray() {
//            Object[] result = new Object[size];
//            int i = 0;
//            for (java.util.LinkedList.Node<E> x = first; x != null; x = x.next)
//                result[i++] = x.item;
//            return result;
//        }
//        @SuppressWarnings("unchecked")
//        public <T> T[] toArray(T[] a) {
//            if (a.length < size)
//                a = (T[])java.lang.reflect.Array.newInstance(
//                        a.getClass().getComponentType(), size);
//            int i = 0;
//            Object[] result = a;
//            for (java.util.LinkedList.Node<E> x = first; x != null; x = x.next)
//                result[i++] = x.item;
//
//            if (a.length > size)
//                a[size] = null;
//
//            return a;
//        }
//
//        private static final long serialVersionUID = 876323262645176354L;
//
//        private void writeObject(java.io.ObjectOutputStream s)
//                throws java.io.IOException {
//            s.defaultWriteObject();
//
//            s.writeInt(size);
//
//            for (java.util.LinkedList.Node<E> x = first; x != null; x = x.next)
//                s.writeObject(x.item);
//        }
//        @SuppressWarnings("unchecked")
//        private void readObject(java.io.ObjectInputStream s)
//                throws java.io.IOException, ClassNotFoundException {
//            s.defaultReadObject();
//
//            int size = s.readInt();
//
//            for (int i = 0; i < size; i++)
//                linkLast((E)s.readObject());
//        }
//    }
//
//}
//
