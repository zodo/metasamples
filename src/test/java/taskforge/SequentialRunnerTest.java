package taskforge;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SequentialRunnerTest {

    @Test
    void executesAllTasks() {
        TaskGraph graph = new TaskGraph();
        graph.addTask(new Task("a", 1, List.of()));
        graph.addTask(new Task("b", 1, List.of("a")));
        graph.addTask(new Task("c", 1, List.of("b")));

        TaskRunner runner = new TaskRunner.SequentialRunner();
        List<Task.ExecutionResult> results = runner.run(graph);

        assertEquals(3, results.size());
        assertTrue(results.stream()
                .allMatch(r -> r.status() == Task.ExecutionResult.Status.SUCCESS));
    }

    @Test
    void tasksRunSequentially() {
        TaskGraph graph = new TaskGraph();
        graph.addTask(new Task("first", 1, List.of()));
        graph.addTask(new Task("second", 1, List.of("first")));

        TaskRunner runner = new TaskRunner.SequentialRunner();
        List<Task.ExecutionResult> results = runner.run(graph);

        assertEquals(2, results.size());
        assertTrue(results.get(0).endTime() <= results.get(1).startTime(),
                "Tasks should run sequentially without overlap");
    }
}
