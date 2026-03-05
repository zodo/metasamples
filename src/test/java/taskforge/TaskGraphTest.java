package taskforge;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskGraphTest {

    @Test
    void linearChain() {
        TaskGraph graph = new TaskGraph();
        graph.addTask(new Task("a", 1, List.of()));
        graph.addTask(new Task("b", 2, List.of("a")));
        graph.addTask(new Task("c", 3, List.of("b")));

        List<String> sorted = graph.topologicalSort();

        assertEquals(3, sorted.size());
        assertTrue(sorted.contains("a"));
        assertTrue(sorted.contains("b"));
        assertTrue(sorted.contains("c"));
    }

    @Test
    void independentTasks() {
        TaskGraph graph = new TaskGraph();
        graph.addTask(new Task("x", 1, List.of()));
        graph.addTask(new Task("y", 2, List.of()));
        graph.addTask(new Task("z", 3, List.of()));

        List<String> sorted = graph.topologicalSort();

        assertEquals(3, sorted.size());
        assertTrue(sorted.containsAll(List.of("x", "y", "z")));
    }

    @Test
    void diamondDependency() {
        TaskGraph graph = new TaskGraph();
        graph.addTask(new Task("compile", 5, List.of()));
        graph.addTask(new Task("test", 3, List.of("compile")));
        graph.addTask(new Task("lint", 2, List.of("compile")));
        graph.addTask(new Task("package", 4, List.of("test", "lint")));
        graph.addTask(new Task("deploy", 1, List.of("package")));

        List<String> sorted = graph.topologicalSort();

        assertEquals(5, sorted.size());

        // each task must come after all its dependencies
        assertTrue(sorted.indexOf("compile") < sorted.indexOf("test"),
                "compile should be scheduled before test, but got: " + sorted);
        assertTrue(sorted.indexOf("compile") < sorted.indexOf("lint"),
                "compile should be scheduled before lint, but got: " + sorted);
        assertTrue(sorted.indexOf("test") < sorted.indexOf("package"),
                "test should be scheduled before package, but got: " + sorted);
        assertTrue(sorted.indexOf("lint") < sorted.indexOf("package"),
                "lint should be scheduled before package, but got: " + sorted);
        assertTrue(sorted.indexOf("package") < sorted.indexOf("deploy"),
                "package should be scheduled before deploy, but got: " + sorted);
    }
}
