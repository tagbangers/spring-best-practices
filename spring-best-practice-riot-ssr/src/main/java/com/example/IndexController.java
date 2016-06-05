package com.example;

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

@Controller
public class IndexController {

	@Autowired
	private ResourceLoader resourceLoader;

	@RequestMapping("/")
	public String index(Model model) throws ScriptException, IOException {
		NashornScriptEngine nashorn = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/jvm-npm.js").getInputStream()));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/riot-sdom.js").getInputStream()));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/nashorn-polyfill.js").getInputStream()));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/riot.js").getInputStream()));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/riot-index.js").getInputStream()));
		nashorn.eval(new InputStreamReader(resourceLoader.getResource("classpath:static/todo.js").getInputStream()));

		String todo = (String) nashorn.eval("riot.render('todo', {title: 'あああ', items: [{title: 'いいい', done: false}]})");
		model.addAttribute("todo", todo);

		return "index";
	}
}
