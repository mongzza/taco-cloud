package tacos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import tacos.web.DesignTacoController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(DesignTacoController.class)
public class DesignTacoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testShowDesignForm() throws Exception {
		mockMvc.perform(get("/design"))
				.andExpect(status().isOk())
				.andExpect(view().name("design"))
				.andExpect(content().string(containsString("Design your taco!")));
	}
}
