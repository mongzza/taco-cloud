package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {

	@GetMapping
	public String showDesignForm(Model model) {
		List<Ingredient> ingredients = Arrays.asList(
				new Ingredient("FLTO", "밀가루 또띠아", Type.WRAP),
				new Ingredient("COTO", "옥수수 또띠아", Type.WRAP),
				new Ingredient("GRBF", "떡갈비", Type.PROTEIN),
				new Ingredient("CARN", "소불고기", Type.PROTEIN),
				new Ingredient("TMTO", "큐브 토마토", Type.VEGGIES),
				new Ingredient("LETC", "상추", Type.VEGGIES),
				new Ingredient("CHED", "체다 치즈", Type.CHEESE),
				new Ingredient("MOZZ", "모짜렐라 치즈", Type.CHEESE),
				new Ingredient("SLSA", "살사", Type.SAUCE),
				new Ingredient("SRCR", "사워크림", Type.SAUCE)
		);

		Type[] types = Ingredient.Type.values();
		for(Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
		model.addAttribute("taco", new Taco());

		return "design";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients
				.stream()
				.filter(x -> x.getType().equals(type))
				.collect(Collectors.toList());
	}

	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors) {
		if(errors.hasErrors()) {
			return "design";
		}

		// 타코 디자인 저장
		log.info("Processing design : " + design);

		return "redirect:/orders/current";
	}
}
