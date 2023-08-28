package blog.ex.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import blog.ex.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegisterControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@BeforeEach
	public void prepareData() {
		when(userService.createAccount(any(),any(),any())).thenReturn(false);
		when(userService.createAccount("Rob","rob@rob.com", "password")).thenReturn(true);
	}
	
	@Test
	public void testGetLoginRegisterPage_Succeed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/login");
		mockMvc.perform(request).andExpect(view().name("loginregister.html"));
	}
	
	@Test 
	public void testRegisterCorrectUserNameCorrectEmailCorrectPassword_Succed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/register/process")
				.param("register_user_name", "Rob")
				.param("register_user_email", "rob@rob.com")
				.param("register_user_password", "password");
		
		mockMvc.perform(request)
		.andExpect(redirectedUrl("/login")).andReturn();
	}
	
	@Test 
	public void testRegisterEmptyUserNameCorrectEmailCorrectPassword_Unsuccessful() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/register/process")
				.param("register_user_name", "")
				.param("register_user_email", "rob@rob.com")
				.param("register_user_password", "password");
		
		mockMvc.perform(request)
		.andExpect(redirectedUrl("/register")).andReturn();
	}
	
	@Test 
	public void testRegisterCorrectUserNameEmptyEmailCorrectPassword_Unsuccessful() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/register/process")
				.param("register_user_name", "Rob")
				.param("register_user_email", "")
				.param("register_user_password", "password");
		
		mockMvc.perform(request)
		.andExpect(redirectedUrl("/register")).andReturn();
	}
	
	@Test 
	public void testRegisterCorrectUserNameCorrectEmailEmptyPassword_Unsuccessful() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/register/process")
				.param("register_user_name", "Rob")
				.param("register_user_email", "rob@rob.com")
				.param("register_user_password", "");
		
		mockMvc.perform(request)
		.andExpect(redirectedUrl("/register")).andReturn();
	}
}
