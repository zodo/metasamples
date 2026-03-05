package taskforge;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parsesSimpleTask() {
        List<Task> tasks = TaskParser.parse("build: duration=5");

        assertEquals(1, tasks.size());
        assertEquals("build", tasks.get(0).name());
        assertEquals(5, tasks.get(0).duration());
        assertTrue(tasks.get(0).dependencies().isEmpty());
    }

    @Test
    void parsesTaskWithDependencies() {
        List<Task> tasks = TaskParser.parse("test: duration=3, depends=[compile, lint]");

        assertEquals(1, tasks.size());
        assertEquals("test", tasks.get(0).name());
        assertEquals(List.of("compile", "lint"), tasks.get(0).dependencies());
    }

    @Test
    void skipsCommentsAndEmptyLines() {
        String content = """
                # This is a comment

                build: duration=5

                # Another comment
                test: duration=3, depends=[build]
                """;

        List<Task> tasks = TaskParser.parse(content);
        assertEquals(2, tasks.size());
    }
}
