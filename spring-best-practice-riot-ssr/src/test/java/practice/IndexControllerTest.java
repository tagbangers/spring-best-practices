package practice;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.ExtendedModelMap;
import practice.IndexController;

import static org.junit.Assert.*;

/**
 * @author Ogawa, Takeshi
 */
public class IndexControllerTest {

	@Test
	public void index() throws Exception {
		IndexController controller = new IndexController();
		controller.setResourceLoader(new DefaultResourceLoader());
		String viewName = controller.index(new ExtendedModelMap());
		assertEquals("index", viewName);
	}
}