package tacos.web.api;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import tacos.Ingredient;

public class IngredientModelAssembler extends RepresentationModelAssemblerSupport<Ingredient, IngredientModel> {

	public IngredientModelAssembler() {
		super(IngredientController.class , IngredientModel.class);
	}

	@Override
	public IngredientModel toModel(Ingredient ingredient) {
		return createModelWithId(ingredient.getId(), ingredient);
	}
}
