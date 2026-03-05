package taskforge;

import java.util.ArrayList;
import java.util.List;

public interface TaskRunner {
    List<Task.ExecutionResult> run(TaskGraph graph);

    // --- Sequential implementation ---

    class SequentialRunner implements TaskRunner {

        private static final long TIME_SCALE_MS = 100;

        @Override
        public List<Task.ExecutionResult> run(TaskGraph graph) {
            List<String> order = graph.topologicalSort();
            List<Task.ExecutionResult> results = new ArrayList<>();

            for (String taskName : order) {
                Task task = graph.getTask(taskName);
                long start = System.currentTimeMillis();
                simulateRun(task);
                results.add(
                    new Task.ExecutionResult(
                        taskName,
                        Task.ExecutionResult.Status.SUCCESS,
                        start,
                        System.currentTimeMillis()
                    )
                );
            }

            return results;
        }
    }

    private static void simulateRun(Task task) {
        System.out.printf(
            "  [%s] started (duration=%d)%n",
            task.name(),
            task.duration()
        );
        try {
            // fake execution
            Thread.sleep(task.duration() * 100L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("  [%s] done%n", task.name());
    }
}
