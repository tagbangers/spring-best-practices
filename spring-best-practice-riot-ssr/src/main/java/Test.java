import jdk.nashorn.api.scripting.NashornScriptEngine;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;

/**
 * Created by ogawa on 2016/06/05.
 */
public class Test {

	public static void main(String[] args) throws ScriptException {
		NashornScriptEngine nashorn = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");
//		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/react.js")));
//		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/react-dom.js")));

		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/jvm-npm.js")));


//		nashorn.eval("var exports = {};");
//		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/simple-dom.js")));
//		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/simple-html-tokenizer.js")));
		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/riot-sdom.js")));

		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/nashorn-polyfill.js")));

		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/riot.js")));
		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/riot-index.js")));

		nashorn.eval(new InputStreamReader(Test.class.getClassLoader().getResourceAsStream("static/todo.js")));

		Object todo = nashorn.eval("riot.render('todo', {title: 'あああ', items: [{title: 'いいい', done: 'ううう'}]})");
		System.out.println(todo);
	}
}
