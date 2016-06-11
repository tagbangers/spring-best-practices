package practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ogawa, Takeshi
 */
@RestController
@RequestMapping("/todo")
public class TodoRestController {

	@Autowired
	private TodoRepository todoRepository;

	@PostMapping
	public Todo add() {
		return null;
	}

	@PutMapping
	public Todo toggle() {
		return null;
	}

	@DeleteMapping
	public void removeAllDone() {

	}
}
