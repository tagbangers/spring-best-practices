package practice.post_redirect_get;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/basic")
public class BasicExampleController {

	public static final String FORM_MODEL_KEY = "form";
	public static final String ERRORS_MODEL_KEY = BindingResult.MODEL_KEY_PREFIX + FORM_MODEL_KEY;

	@RequestMapping
	public String get(Model model) {
		BasicExampleForm form = (BasicExampleForm) model.asMap().get(FORM_MODEL_KEY);
		if (form == null) {
			form = new BasicExampleForm();
		}
		model.addAttribute(FORM_MODEL_KEY, form);
		return "basic";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String post(
			@Validated BasicExampleForm form,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(FORM_MODEL_KEY, form);
		redirectAttributes.addFlashAttribute(ERRORS_MODEL_KEY, result);

		if (result.hasErrors()) {
			return "redirect:/basic";
		}

		// Do something. For example, call a method of a service class.

		redirectAttributes.getFlashAttributes().clear();
		redirectAttributes.addFlashAttribute("message", "Success!");
		return "redirect:/basic";
	}
}
