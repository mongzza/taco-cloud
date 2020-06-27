package tacos;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Order {

	private long id;
	private Date placedAt;

	@NotBlank(message = "주문자명은 필수 입력값입니다.")
	private String deliveryName;

	@NotBlank(message = "시/도는 필수 입력값입니다.")
	private String deliveryCity;

	@NotBlank(message = "시/구/군은 필수 입력값입니다.")
	private String deliveryDistrict;

	@NotBlank(message = "도로명은 필수 입력값입니다.")
	private String deliveryStreet;

	@NotBlank(message = "상세주소는 필수 입력값입니다.")
	private String deliveryDetail;

	@CreditCardNumber(message = "올바르지 않은 카드번호입니다.")
	private String ccNumber;

	@Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
			 message = "유효기간은 반드시 MM/YY 형식으로 입력해야합니다.")
	private String ccExpiration;

	@Digits(integer = 3, fraction = 0, message = "올바르지 않은 CVC입니다.")
	private String ccCVC;

	private List<Taco> tacos = new ArrayList<>();

	public void addDesign(Taco design) {
		this.tacos.add(design);
	}

}
