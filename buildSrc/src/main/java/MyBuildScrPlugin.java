import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class MyBuildScrPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.task("helloTask").doFirst(new Action<Task>() {
            @Override
            public void execute(Task task) {
                System.out.println("hello doFirst...");
            }
        }).doLast(new Action<Task>() {
            @Override
            public void execute(Task task) {
                System.out.println("hello doLast...");
            }
        });
    }
}
