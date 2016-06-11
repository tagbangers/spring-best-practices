package practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ogawa, Takeshi
 */
@RestController
@RequestMapping("/todo")
public class TodoRestController {

	@Autowired
	private TodoRepository todoRepository;

	@PostMapping
	public Todo add(@RequestParam String title) {
		Todo todo = new Todo();
		todo.setTitle(title);
		return todoRepository.save(todo);
	}

	@PutMapping("/{id}")
	public Todo toggle(@PathVariable long id) {
		Todo todo = todoRepository.findOne(id);
		todo.setDone(!todo.isDone());
		todo = todoRepository.save(todo);
		return todo;
	}

	@DeleteMapping
	public void removeAllDone() {
		List<Todo> todos = todoRepository.findAllByDoneIsTrue();
		todoRepository.deleteInBatch(todos);
	}
}
