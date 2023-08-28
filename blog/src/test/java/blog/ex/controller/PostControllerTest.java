package blog.ex.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import blog.ex.model.entity.PostEntity;
import blog.ex.model.entity.UserEntity;
import blog.ex.service.PostService;
import blog.ex.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	@MockBean
	private PostService postService;
	
	private MockHttpSession session;
	
	@BeforeEach 
	public void prepareData() {
		UserEntity userList = new UserEntity();
		userList.setUserId(1L);
		userList.setUserName("test");
		
		List<PostEntity> postList = new ArrayList<>();
		postList.add(new PostEntity());
		postList.add(new PostEntity());

		session = new MockHttpSession();
		session.setAttribute("user", userList);
				
	}
	
	@Test 
	public void testGetNewPostPage_Succed() throws Exception {
		mockMvc.perform(get("/author/home/newpost").session(session))
		.andExpect(status().isOk())
		.andExpect(view().name("newpost.html"))
		.andExpect(model().attributeExists("userName","registerMessage"))
		.andExpect(model().attribute("userName", "test"));
	}
	
	@Test
	public void testSavePost_Succeed() throws Exception {
	    when(postService.createBlogPost(anyString(), anyString(), any(LocalDateTime.class), anyString(), anyLong(), anyLong())).thenReturn(true);

	    String fileImage = "test-image.jpg";
	    String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + fileImage;
	    MockMultipartFile postImage = new MockMultipartFile("postImage", fileImage, "image/jpeg", new byte[0]);
	    LocalDateTime date = LocalDateTime.now();

	    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/author/newpost/process")
	            .file(postImage)
	            .param("postTitle", "Test post")
	            .param("postContent", "Test content")
	            .session(session))
	            .andExpect(status().is3xxRedirection())
	            .andReturn();

	    // リダイレクト先のURLを検証
	    String redirectedUrl = result.getResponse().getRedirectedUrl();
	    assertEquals("/author/home/list", redirectedUrl);

	    verify(postService, times(1)).createBlogPost(eq("Test post"), eq(fileName), eq(date), eq("Test content"), eq(1L), eq(0L));
	}
	
	
// Rob's code	
//	@Test 
//	public void testSavePost_Succeed() throws Exception {
//		when(postService.createBlogPost(anyString(), anyString(), any(LocalDateTime.class), anyString(), anyLong(), anyLong())).thenReturn(true);
//		
//		String fileImage = "test-image.jpg";
//		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + fileImage;
//		MockMultipartFile postImage = new MockMultipartFile("postImage", fileImage, "image/jpeg", new byte[0]);
//		LocalDateTime date = LocalDateTime.now();
//		
//		mockMvc.perform(MockMvcRequestBuilders.multipart("/author/newpost/process")
//				.file(postImage)
//				.param("postTitle", "Test post")
//				.param("postContent", "Test content")
//				.session(session))
////			.andExpect(status().isOk()
//			.andExpect(redirectedUrl("/author/home/list"));
//		
//		//verify(postService, times(1)).createBlogPost(eq("Test post"), eq(fileName), eq(date), eq("Test content"), eq(1L), eq(0L));
//	}
	
//	@Test
//	public void testSavePost_Succeed() throws Exception {
//	    when(postService.createBlogPost(anyString(), anyString(), any(LocalDateTime.class), anyString(), anyLong(), anyLong())).thenReturn(true);
//
//	    String fileImage = "test-image.jpg";
//	    String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + fileImage;
//	    MockMultipartFile postImage = new MockMultipartFile("postImage", fileImage, "image/jpeg", new byte[0]);
//	    LocalDateTime date = LocalDateTime.now();
//
//	    mockMvc.perform(MockMvcRequestBuilders.multipart("/author/newpost/process")
//	            .file(postImage)
//	            .param("postTitle", "Test post")
//	            .param("postContent", "Test content")
//	            .sessionAttr("user", new UserEntity()) // Set the user session attribute appropriately
//	    )
//	            .andExpect(status().is3xxRedirection())
//	            .andExpect(redirectedUrl("/author/home/list"))
//	            .andReturn();
//
//	    verify(postService, times(1)).createBlogPost(eq("Test post"), eq(fileName), eq(date), eq("Test content"), eq(1L), eq(0L));
//	}
}
	
