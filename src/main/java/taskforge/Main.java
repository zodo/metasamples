package taskforge;

import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: taskforge <file.tasks>");
            System.exit(1);
        }

        Path filePath = Path.of(args[0]);
        System.out.println("Loading tasks from: " + filePath);

        List<Task> tasks = TaskParser.parse(filePath);
        System.out.printf("Parsed %d tasks%n", tasks.size());

        TaskGraph graph = new TaskGraph();
        for (Task task : tasks) {
            graph.addTask(task);
        }

        System.out.println("Running tasks sequentially...");
        System.out.println();

        TaskRunner runner = new TaskRunner.SequentialRunner();
        List<Task.ExecutionResult> results = runner.run(graph);

        ExecutionReporter.printReport(results);
    }
}
