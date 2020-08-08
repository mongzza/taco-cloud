package tacos.web.api;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import tacos.Ingredient;

public class IngredientModelAssembler extends RepresentationModelAssemblerSupport<Ingredient, IngredientModel> {

	public IngredientModelAssembler() {
		super(IngredientController.class , IngredientModel.class);
	}

	@Override
	protected IngredientModel instantiateModel(Ingredient ingredient) {
		return new IngredientModel(ingredient);
	}

	@Override
	public IngredientModel toModel(Ingredient ingredient) {
		return createModelWithId(ingredient.getId(), ingredient);
	}
}
