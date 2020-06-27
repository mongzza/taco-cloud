package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tacos.data.IngredientRepository;

@SpringBootApplication
public class TacoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(IngredientRepository repo) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				repo.save(new Ingredient("FLTO", "밀가루 또띠아", Ingredient.Type.WRAP));
				repo.save(new Ingredient("COTO", "옥수수 또띠아", Ingredient.Type.WRAP));
				repo.save(new Ingredient("GRBF", "떡갈비", Ingredient.Type.PROTEIN));
				repo.save(new Ingredient("CARN", "불고기", Ingredient.Type.PROTEIN));
				repo.save(new Ingredient("TMTO", "토마토 슬라이스", Ingredient.Type.VEGGIES));
				repo.save(new Ingredient("LETC", "상추", Ingredient.Type.VEGGIES));
				repo.save(new Ingredient("CHED", "체다치즈", Ingredient.Type.CHEESE));
				repo.save(new Ingredient("JACK", "몬테레이 잭", Ingredient.Type.CHEESE));
				repo.save(new Ingredient("SLSA", "살사", Ingredient.Type.SAUCE ));
				repo.save(new Ingredient("SRCR", "사워 크림", Ingredient.Type.SAUCE));
			}
		};
	}

}
