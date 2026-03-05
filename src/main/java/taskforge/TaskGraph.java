package taskforge;

import java.util.*;

public class TaskGraph {

    private final Map<String, Task> tasks = new LinkedHashMap<>();
    private final Map<String, List<String>> adjacency = new HashMap<>();

    public void addTask(Task task) {
        tasks.put(task.name(), task);
        adjacency.putIfAbsent(task.name(), new ArrayList<>());
        for (String dep : task.dependencies()) {
            adjacency
                .computeIfAbsent(dep, k -> new ArrayList<>())
                .add(task.name());
        }
    }

    public List<String> topologicalSort() {
        Map<String, Integer> inDegree = new HashMap<>();
        for (String name : tasks.keySet()) {
            inDegree.put(name, 0);
        }
        for (Map.Entry<String, List<String>> entry : adjacency.entrySet()) {
            for (String neighbor : entry.getValue()) {
                inDegree.merge(neighbor, 0, Integer::sum);
            }
        }

        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<String> sorted = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            sorted.add(current);
            for (String neighbor : adjacency.getOrDefault(current, List.of())) {
                inDegree.merge(neighbor, -1, Integer::sum);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sorted.size() != tasks.size()) {
            throw new IllegalStateException("Graph contains a cycle");
        }

        return sorted;
    }

    public Task getTask(String name) {
        return tasks.get(name);
    }

    public Collection<Task> getAllTasks() {
        return Collections.unmodifiableCollection(tasks.values());
    }

    public Set<String> getTaskNames() {
        return Collections.unmodifiableSet(tasks.keySet());
    }

    public int size() {
        return tasks.size();
    }
}
