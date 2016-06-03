package practice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SampleController {

	@RequestMapping("/")
	public String index(
			RedirectAttributes redirectAttributes,
			HttpServletRequest request,
			HttpServletResponse response) {
//		redirectAttributes.addFlashAttribute("message", "Hello!");
//		return "redirect:/result";

		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		flashMap.put("message", "Hello!");
		RequestContextUtils.getFlashMapManager(request).saveOutputFlashMap(flashMap, request, response);
		return "index";
	}

	@RequestMapping("/result")
	public String result() {
		return "result";
	}
}
