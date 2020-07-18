package tacos.security;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.User;

@Data
public class RegistrationForm {

	private String username;
	private String password;
	private String fullname;
	private String city;
	private String district;
	private String street;
	private String detail;
	private String phone;

	public User toUser(PasswordEncoder passwordEncoder) {
		return new User(
				username, passwordEncoder.encode(password),
				fullname, city, district, street, detail, phone
		);
	}
}
