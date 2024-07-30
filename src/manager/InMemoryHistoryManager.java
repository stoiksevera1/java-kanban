package manager;

import task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head = null;

    private  Map<Integer, Node<Task>> historyKey = new HashMap<>();


    @Override
    public void add(Task task) {
        if (historyKey.containsKey(task.getId())) {
            if (head != null) {
                remove(task.getId());

            }
            historyKey.put(task.getId(), linkLast(task));
        } else {
            historyKey.put(task.getId(), linkLast(task));
        }
    }


    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        if (head != null) {
            Node<Task> iterableNode = head;
            while (iterableNode.next != null) {
                historyList.add(iterableNode.data);
                iterableNode = iterableNode.next;
            }
            historyList.add(iterableNode.data);
        }
        return historyList;
    }

    @Override
    public void remove(int id) {
        Node<Task> removeNode = historyKey.get(id);
        if (head.next == null) {
            head = null;
        } else if (removeNode.prev == null) {
            head = head.next;
            head.prev = null;
        } else if (removeNode.next == null) {
            removeNode.prev = null;
        } else {
            removeNode.prev.next = removeNode.next;
            removeNode.next.prev = removeNode.prev;
        }
    }

    public Node<Task> linkLast(Task task) {
        Node<Task> newNode = new Node<>(task);
        Node<Task> currentNode = head;

        if (head == null) {
            head = newNode;
            return head;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
            currentNode.next.prev = currentNode;
        }
        return currentNode.next;
    }

}

