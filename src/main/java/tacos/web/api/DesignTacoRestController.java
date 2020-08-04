package tacos.web.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tacos.Order;
import tacos.Taco;
import tacos.data.TacoRepository;

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
	public Iterable<Taco> recentTacos() {
		PageRequest page = PageRequest.of(
				0, 12, Sort.by("createdAt").descending()	);
		return tacoRepo.findAll(page).getContent();
	}

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Taco postTaco(@RequestBody Taco taco) {
		return tacoRepo.save(taco);
	}

}
