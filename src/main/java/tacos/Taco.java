package tacos;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date createdAt;

	@NotNull
	@Size(min=2, message="타코 이름은 최소 2자 이상이어야 합니다.")
	private String name;

	@ManyToMany(targetEntity = Ingredient.class)
	@Size(min=1, message = "최소 1개 이상의 재료를 선택해야 합니다.")
	private List<Ingredient> ingredients;

	@PrePersist
	void createdAt() {
		this.createdAt = new Date();
	}
}
