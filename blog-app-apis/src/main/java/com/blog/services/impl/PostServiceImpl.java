package com.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	

	@Override
	public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {
		
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "User id", userId));
			
		Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "category id", categoryId));
		
		Post post=this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost =this.postRepo.save(post);
		
		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "post id", postId));
	    
		post.setTitle(postDto.getTitle());
	    post.setContent(postDto.getContent());
	    
	    
	   Post updatePost= this.postRepo.save(post);
		
		return this.modelMapper.map(updatePost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "post id", postId));
        this.postRepo.delete(post);
	}

	@Override
	public PostResponse getAllPost( Integer pageNumber,Integer pageSize) {
		
		Pageable p=PageRequest.of(pageNumber, pageSize);
		
	Page<Post> pagePost = this.postRepo.findAll(p);
	
	List<Post> Allposts = pagePost.getContent();
      List<PostDto>	postDtos=Allposts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
      PostResponse postResponse=new PostResponse();
      postResponse.setContent(postDtos);
      postResponse.setPageNumber(pagePost.getNumber());
      postResponse.setPageSize(pagePost.getSize());
      postResponse.setTotalElements(pagePost.getTotalElements());

      postResponse.setTotalPages(pagePost.getTotalPages());
      postResponse.setLasrPage(pagePost.isLast());
      
      return postResponse ;
	}
	

	@Override
	public PostDto getPostById(Integer postid) {
	 Post post = this.postRepo.findById(postid).orElseThrow(()->new ResourceNotFoundException("Post", "post id", postid));
		
		return this.modelMapper.map(post, PostDto.class);
	}

	
	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "category id", categoryId));
		
		List<Post> posts=this.postRepo.findByCategory(cat);
			
		
	List<PostDto> postDtos=	posts.stream().map((post)->this.modelMapper.map(post, PostDto.class))
			.collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User  user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "userId", userId));
		List<Post> posts=this.postRepo.findByUser(user);
		
		List<PostDto> postDtos=posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword){
		  List<Post> posts = this.postRepo.searchByTitle("%"+keyword+"%");
		  
		  List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		  
		  return postDtos;
	}
}
