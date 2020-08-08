package tacos.web.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/design", produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoRestController {
	private TacoRepository tacoRepo;

	//@Autowired
	//EntityLinks entityLinks;

	public DesignTacoRestController(TacoRepository tacoRepo) {
		this.tacoRepo = tacoRepo;
	}

	@GetMapping("/recent")
	public CollectionModel<TacoModel> recentTacos() {
		PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
		List<Taco> tacos = tacoRepo.findAll(page).getContent();

		List<TacoModel> tacoModels = (List<TacoModel>) new TacoModelAssembler().toCollectionModel(tacos);
		CollectionModel<TacoModel> recentResources = CollectionModel.of(tacoModels);

		recentResources.add(linkTo(methodOn(DesignTacoRestController.class).recentTacos()).withRel("recents"));

		return recentResources;
	}

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Taco postTaco(@RequestBody Taco taco) {
		return tacoRepo.save(taco);
	}

}
