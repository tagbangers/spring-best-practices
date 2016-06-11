package practice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Ogawa, Takeshi
 */
@Controller
public class IndexController {

	private ResourceLoader resourceLoader;

	private TodoRepository todoRepository;

	private ObjectMapper objectMapper;

	@Autowired
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Autowired
	public void setTodoRepository(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@RequestMapping("/")
	public String index(Model model) throws ScriptException, IOException {
		NashornScriptEngine nashorn = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/jvm-npm.js").getInputStream()));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/nashorn-riot.js").getInputStream()));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/riot.js").getInputStream()));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/riot-render.js").getInputStream()));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/todo.js").getInputStream()));

		List<Todo> todos = todoRepository.findAll();
		String todoTag = (String) nashorn.eval(String.format("riot.render('todo', {title: 'あああ', items: %s})", objectMapper.writeValueAsString(todos)));

		model.addAttribute("title", "I want to behave!");
		model.addAttribute("todos", todos);
		model.addAttribute("todoTag", todoTag);
		return "index";
	}
}
