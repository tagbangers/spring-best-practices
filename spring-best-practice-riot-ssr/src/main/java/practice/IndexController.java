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

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping("/")
	public String index(Model model) throws ScriptException, IOException {
		NashornScriptEngine nashorn = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/jvm-npm.js").getInputStream(), "UTF-8"));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/nashorn-riot.js").getInputStream(), "UTF-8"));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/riot.js").getInputStream(), "UTF-8"));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/riot-render.js").getInputStream(), "UTF-8"));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/js/todo.js").getInputStream(), "UTF-8"));

		String title = "I want to behave!";
		List<Todo> todos = todoRepository.findAll();
		String todoTag = (String) nashorn.eval(String.format("riot.render('todo', {title: '%s', items: %s})", title, objectMapper.writeValueAsString(todos)));

		model.addAttribute("title", title);
		model.addAttribute("todos", todos);
		model.addAttribute("todoTag", todoTag);
		return "index";
	}
}
