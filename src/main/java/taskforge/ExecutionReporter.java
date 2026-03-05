package taskforge;

import java.util.List;

public class ExecutionReporter {

    public static void printReport(List<Task.ExecutionResult> results) {
        if (results.isEmpty()) {
            System.out.println("No tasks executed.");
            return;
        }

        long baseTime = results.stream()
                .mapToLong(Task.ExecutionResult::startTime)
                .min().orElse(0);

        System.out.println("=== Execution Report ===");
        System.out.println();
        System.out.printf("  %-20s  %-10s  %10s  %10s  %10s%n",
                "Task", "Status", "Start", "End", "Duration");
        System.out.println("  " + "-".repeat(66));

        for (Task.ExecutionResult r : results) {
            System.out.printf("  %-20s  %-10s  %8dms  %8dms  %8dms%n",
                    r.taskName(), r.status(),
                    r.startTime() - baseTime,
                    r.endTime() - baseTime,
                    r.duration());
        }

        System.out.println();
        long totalTime = results.stream()
                .mapToLong(Task.ExecutionResult::endTime)
                .max().orElse(0) - baseTime;
        System.out.printf("  Tasks: %d | Total time: %dms%n", results.size(), totalTime);
        System.out.println();
    }
}
