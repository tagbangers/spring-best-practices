package practice;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Ogawa, Takeshi
 */
public class IndexControllerTest {

	@Mock
	private ResourceLoader resourceLoader;

	@Mock
	private TodoRepository todoRepository;

	@Mock
	private ObjectMapper objectMapper = new ObjectMapper();

	@InjectMocks
	private IndexController indexController = new IndexController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void index() throws Exception {
		ResourceLoader defaultResourceLoader = new DefaultResourceLoader();
		when(resourceLoader.getResource(anyString())).thenAnswer((Answer<Resource>) invocationOnMock -> defaultResourceLoader.getResource((String) invocationOnMock.getArguments()[0]));

		List<Todo> todos = new ArrayList<>();
		todos.add(new Todo(1, "title1", true));
		todos.add(new Todo(2, "title2", false));
		todos.add(new Todo(3, "title3", false));
		when(todoRepository.findAll()).thenReturn(todos);

		when(objectMapper.writeValueAsString(anyObject())).thenReturn(new ObjectMapper().writeValueAsString(todos));

		String viewName = indexController.index(new ExtendedModelMap());
		assertEquals("index", viewName);
	}
}