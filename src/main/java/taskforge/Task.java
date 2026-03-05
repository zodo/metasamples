package taskforge;

import java.util.List;

public record Task(String name, int duration, List<String> dependencies) {

    public Task {
        dependencies = List.copyOf(dependencies);
    }

    public record ExecutionResult(String taskName, Status status, long startTime, long endTime) {

        public enum Status { SUCCESS, FAILED, SKIPPED }

        public long duration() {
            return endTime - startTime;
        }
    }
}
