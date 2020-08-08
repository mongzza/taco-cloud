package tacos.web.api;

import lombok.Getter;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import tacos.Ingredient;
import tacos.Taco;

import java.util.Date;

@RestResource(rel = "tacos", path = "tacoes")
@Relation(value = "taco", collectionRelation = "tacos")
public class TacoModel extends RepresentationModel<TacoModel> {

	private static final IngredientModelAssembler ingredientAssembler = new IngredientModelAssembler();

	@Getter
	private final String name;

	@Getter
	private final Date createdAt;

	@Getter
	private final CollectionModel<IngredientModel> ingredients;

	public TacoModel(Taco taco) {
		this.name = taco.getName();
		this.createdAt = taco.getCreatedAt();
		this.ingredients = ingredientAssembler.toCollectionModel(taco.getIngredients());
	}
}
