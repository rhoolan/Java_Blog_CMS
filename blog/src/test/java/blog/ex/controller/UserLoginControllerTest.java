package blog.ex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import blog.ex.model.entity.UserEntity;
import blog.ex.service.UserService;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@BeforeEach 
	public void prepareData() {
		UserEntity userEntity = new UserEntity(1L, "Rob", "rob@rob.com", "test", "salt");
		when(userService.login(eq("rob@rob.com"), eq("test"))).thenReturn(userEntity);
	}
	
	// ログインページを正しく取得出来たかどうかのテスト
	@Test
	public void testGetLoginRegisterPage_Succeed()throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/login");
		mockMvc.perform(request).andExpect(view().name("loginregister.html"));
	}
	
	@Test
	public void testLogin_CorrectUserEmailCorrectPassword_Successful() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/login/process")
				.param("login_email", "rob@rob.com")
				.param("login_user_password", "test");
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(redirectedUrl("/author/home/list"))
				.andReturn();
				
		HttpSession session = result.getRequest().getSession();
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		assertNotNull(user);
		assertEquals("Rob", user.getUserName());
		assertEquals("rob@rob.com", user.getEmail());
		assertEquals("test", user.getPassword());
	} 
	
	@Test
	public void testLogin_WrongUserEmailCorrectPassword_Unsuccessful() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.post("/login/process")
				.param("login_email", "mika@mika.com")
				.param("login_user_password", "test");
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(redirectedUrl("/register")).andReturn();
		
		HttpSession session = result.getRequest().getSession();
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		assertNull(user);
		
	}
	
	@Test
	public void testLogin_EmptyUserEmailCorrectPassword_Unsuccessful() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.post("/login/process")
				.param("login_email", "")
				.param("login_user_password", "test");
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(redirectedUrl("/register")).andReturn();
		
		HttpSession session = result.getRequest().getSession();
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		assertNull(user);
		
	}
	
	@Test
	public void testLogin_CorrectUserEmailInorrectPassword_Unsuccessful() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.post("/login/process")
				.param("login_email", "rob@rob.com")
				.param("login_user_password", "badpass");
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(redirectedUrl("/register")).andReturn();
		
		HttpSession session = result.getRequest().getSession();
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		assertNull(user);
		
	}
	
	@Test
	public void testLogin_CorrectUserEmailEmptyPassword_Unsuccessful() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.post("/login/process")
				.param("login_email", "rob@rob.com")
				.param("login_user_password", "");
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(redirectedUrl("/register")).andReturn();
		
		HttpSession session = result.getRequest().getSession();
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		assertNull(user);
		
	}
	
	@Test
	public void testLogin_EmptyUserEmailEmptyPassword_Unsuccessful() throws Exception{
		RequestBuilder request = MockMvcRequestBuilders
				.post("/login/process")
				.param("login_email", "")
				.param("login_user_password", "");
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(redirectedUrl("/register")).andReturn();
		
		HttpSession session = result.getRequest().getSession();
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		assertNull(user);
		
	}
	@Test
	public void testLogin_IncorrectUserEmailIncorrectPassword_Unsuccessful() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.post("/login/process")
				.param("login_email", "mika@mika.com")
				.param("login_user_password", "badpass");
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(redirectedUrl("/register")).andReturn();
		
		HttpSession session = result.getRequest().getSession();
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		assertNull(user);
	}
}


