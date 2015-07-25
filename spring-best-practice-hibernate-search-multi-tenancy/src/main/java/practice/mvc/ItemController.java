package practice.mvc;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import practice.entity.Item;
import practice.repository.ItemRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/{tenant}")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping
	public String items(@PathVariable String tenant, Model model) {
		List<Item> items = itemRepository.search();
		model.addAttribute("tenant", tenant);
		model.addAttribute("items", items);
		return "items";
	}

	@RequestMapping("/re-index")
	@Transactional
	public String reIndex() throws InterruptedException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		fullTextEntityManager.createIndexer().startAndWait();
		return "redirect:/{tenant}";
	}
}
