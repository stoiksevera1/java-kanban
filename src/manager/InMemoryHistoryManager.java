package manager;

import task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head = null;

    private static Map<Integer, Node<Task>> history = new HashMap<>();


    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            remove(task.getId());
            history.remove(task.getId());
        }
        history.put(task.getId(), linkLast(task));
    }


    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> iterableNode = head;
        while (iterableNode.next != null) {
            historyList.add(iterableNode.data);
            iterableNode = iterableNode.next;
        }
        return historyList;
    }

    @Override
    public void remove(int id) {
        Node<Task> removeNode = history.get(id);
        if (!removeNode.equals(head)) {
            if (removeNode.next != null) {
                removeNode.next.prev = removeNode.prev;
                removeNode.prev.next = removeNode.next;
            } else {
                removeNode.prev.next = null;
            }
        } else {
            head = null;
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

