package taskforge;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskParser {

    private static final Pattern TASK_PATTERN = Pattern.compile(
            "^\\s*(\\w+)\\s*:\\s*(.+)$"
    );
    private static final Pattern DURATION_PATTERN = Pattern.compile(
            "duration\\s*=\\s*(\\d+)"
    );
    private static final Pattern DEPENDS_PATTERN = Pattern.compile(
            "depends\\s*=\\s*\\[([^]]*)]"
    );

    public static List<Task> parse(Path filePath) throws IOException {
        return parse(Files.readString(filePath));
    }

    public static List<Task> parse(String content) {
        List<Task> tasks = new ArrayList<>();

        for (String line : content.split("\n")) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            Matcher taskMatcher = TASK_PATTERN.matcher(line);
            if (!taskMatcher.matches()) {
                throw new IllegalArgumentException("Invalid task definition: " + line);
            }

            String name = taskMatcher.group(1);
            String props = taskMatcher.group(2);

            // duration
            Matcher dm = DURATION_PATTERN.matcher(props);
            if (!dm.find()) {
                throw new IllegalArgumentException("Missing duration in: " + line);
            }
            int duration = Integer.parseInt(dm.group(1));

            // dependencies
            List<String> deps = new ArrayList<>();
            Matcher dpm = DEPENDS_PATTERN.matcher(props);
            if (dpm.find()) {
                String depStr = dpm.group(1).trim();
                if (!depStr.isEmpty()) {
                    for (String dep : depStr.split(",")) {
                        dep = dep.trim();
                        if (!dep.isEmpty()) {
                            deps.add(dep);
                        }
                    }
                }
            }

            tasks.add(new Task(name, duration, deps));
        }

        return tasks;
    }
}
