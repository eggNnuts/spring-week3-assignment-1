package com.codesoom.assignment.application;

import com.codesoom.assignment.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("TaskService 클래스")
class TaskServiceTest {
    private static final Long TASK_ID_1 = 1L;
    private static final Long TASK_ID_2 = 2L;
    private static final String TASK_TITLE_1 = "task1";
    private static final String TASK_TITLE_2 = "task2";
    private static final String NEW_TITLE = "new title";

    private TaskService taskService;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
        task1 = new Task();
        task1.setId(TASK_ID_1);
        task1.setTitle(TASK_TITLE_1);
        task2 = new Task();
        task2.setId(TASK_ID_2);
        task2.setTitle(TASK_TITLE_2);
    }

    @Nested
    @DisplayName("getTasks 메서드는")
    class Describe_getTasks {
        @Nested
        @DisplayName("할 일 목록이 있다면")
        class Context_with_tasks {

            @BeforeEach
            void setUp() {
                taskService.createTask(task1);
                taskService.createTask(task2);
            }

            @DisplayName("할 일 목록을 리턴한다")
            @Test
            void it_returns_tasks() {
                List<Task> tasks = taskService.getTasks();

                assertAll(
                        () -> assertThat(tasks).hasSize(2),
                        () -> assertThat(tasks.get(0).getTitle()).isEqualTo(TASK_TITLE_1)
                );
            }
        }

        @Nested
        @DisplayName("할 일인 목록이 없다면")
        class Context_without_tasks {
            @DisplayName("비어있는 값을 리턴한다")
            @Test
            void it_returns_empty_tasks() {
                List<Task> tasks = taskService.getTasks();

                assertThat(tasks).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("getTask 메서드는")
    class Describe_getTask {
        final Long id = 1L;

        @Nested
        @DisplayName("찾고자하는 할 일이 있으면")
        class Context_with_task {
            @BeforeEach
            void setUp() {
                taskService.createTask(task1);
            }

            @DisplayName("할 일을 리턴한다")
            @Test
            void it_returns_tasks() {
                assertThat(taskService.getTask(id).getTitle()).isEqualTo(TASK_TITLE_1);
            }
        }

        @Nested
        @DisplayName("찾고자하는 할 일이 없으면")
        class Context_without_tasks {

            @DisplayName("예외를 발생시킨다")
            @Test
            void it_throws_exception() {
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> taskService.getTask(id));
            }
        }
    }

    @Nested
    @DisplayName("createTask 메서드는")
    class Describe_createTask {
        Task task = new Task();

        @BeforeEach
        void setUp() {
            task.setId(TASK_ID_1);
            task.setTitle(TASK_TITLE_1);
        }

        @DisplayName("createTask는 새로운 할 일을 추가한다")
        @Test
        void it_returns_task_and_size() {
            int beforeTaskSize = taskService.getTasks().size();

            Task newTask = taskService.createTask(this.task);

            int afterTaskSize = taskService.getTasks().size();

            assertThat(afterTaskSize - beforeTaskSize).isEqualTo(1);
            assertThat(newTask.getTitle()).isEqualTo(task.getTitle());
        }
    }

    @Nested
    @DisplayName("updateTask 메서드는")
    class Describe_updateTask {
        final Long id = 1L;
        Task task;

        @Nested
        @DisplayName("갱신하려는 할 일 있으면")
        class Context_with_task {

            @BeforeEach
            void setUp() {
                task = new Task();
                task.setTitle(NEW_TITLE);
                taskService.createTask(task1);
            }

            @DisplayName("변경된 일 할 일을 리턴한다")
            @Test
            void It_returns_update_task() {
                Task updatedTask = taskService.updateTask(id, task);

                assertThat(updatedTask.getTitle()).isEqualTo(NEW_TITLE);
            }
        }

        @Nested
        @DisplayName("갱신하려는 할 일이 없으면")
        class Context_without_tasks {

            @DisplayName("예외를 발생시킨다")
            @Test
            void it_throws_exception() {
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> taskService.updateTask(id, task));
            }
        }
    }

    @Nested
    @DisplayName("deleteTask 메서드는")
    class Describe_deleteTask {
        final Long id = 1L;

        @Nested
        @DisplayName("삭제하려는 할 일이 있으면")
        class Context_with_task {
            @BeforeEach
            void setUp() {
                taskService.createTask(task1);
                taskService.createTask(task2);
            }

            @DisplayName("할 일이 삭제되어 할 일 목록 수가 줄어든다")
            @Test
            void it_returns_delete_task() {
                int beforeTaskSize = taskService.getTasks().size();

                taskService.deleteTask(id);

                int afterTaskSize = taskService.getTasks().size();

                assertThat(beforeTaskSize - afterTaskSize).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("삭제하려는 할 일이 없으면")
        class Context_without_tasks {

            @DisplayName("예외를 발생시킨다")
            @Test
            void it_throws_exception() {
                assertThatExceptionOfType(TaskNotFoundException.class)
                        .isThrownBy(() -> taskService.deleteTask(id));
            }
        }
    }
}
