package practice;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {

	@RequestMapping
	public String hello(
			@AuthenticationPrincipal AuthorizedUserDetails user,
			Model model) {
		model.addAttribute("user", user);
		return "hello";
	}
}
