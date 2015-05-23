package practice.post_redirect_get;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/session")
@SessionAttributes(types = SessionExampleForm.class)
public class SessionExampleController {

	public static final String FORM_MODEL_KEY = "form";
	public static final String ERRORS_MODEL_KEY = BindingResult.MODEL_KEY_PREFIX + FORM_MODEL_KEY;

	@ExceptionHandler(HttpSessionRequiredException.class)
	public String handleHttpSessionRequiredException(HttpSessionRequiredException exception) {
		return "redirect:/session?expired";
	}

	@RequestMapping
	public String init(Model model) {
		SessionExampleForm form = new SessionExampleForm();
		model.addAttribute(FORM_MODEL_KEY, form);
		return "session-step1";
	}

	@RequestMapping(params = "step1")
	public String step1(Model model) {
		SessionExampleForm form = (SessionExampleForm) model.asMap().get(FORM_MODEL_KEY);
		if (form == null) {
			form = new SessionExampleForm();
		}
		model.addAttribute(FORM_MODEL_KEY, form);
		return "session-step1";
	}

	@RequestMapping(params = "step1", method = RequestMethod.POST)
	public String step1(
			@Validated SessionExampleForm form,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(FORM_MODEL_KEY, form);
		redirectAttributes.addFlashAttribute(ERRORS_MODEL_KEY, result);

		if (result.hasFieldErrors("email")) {
			return "redirect:/session?step1";
		}

		redirectAttributes.getFlashAttributes().clear();
		return "redirect:/session?step2";
	}

	@RequestMapping(params = "step2")
	public String step2(Model model) throws HttpSessionRequiredException {
		SessionExampleForm form = (SessionExampleForm) model.asMap().get(FORM_MODEL_KEY);
		if (form == null) {
			throw new HttpSessionRequiredException("Expected session attribute '" + FORM_MODEL_KEY + "'");
		}
		model.addAttribute(FORM_MODEL_KEY, form);
		return "session-step2";
	}

	@RequestMapping(params = "step2", method = RequestMethod.POST)
	public String step2(
			@Validated SessionExampleForm form,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute(FORM_MODEL_KEY, form);
		redirectAttributes.addFlashAttribute(ERRORS_MODEL_KEY, result);

		if (result.hasErrors()) {
			return "redirect:/session?step2";
		}

		// Do something. For example, call a method of a service class.

		redirectAttributes.getFlashAttributes().clear();
		redirectAttributes.addFlashAttribute("message", "Success!");
		return "redirect:/session";
	}
}
