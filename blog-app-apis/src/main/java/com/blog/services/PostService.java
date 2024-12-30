
package com.blog.services;

import java.util.List;

import com.blog.entities.Post;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;

public interface PostService {

	//create
	
	PostDto createPost(PostDto postDto ,Integer userId	,Integer categoryId);

	//update
	PostDto updatePost(PostDto postDto,Integer postId);
	
	//delete
	void deletePost(Integer postId);
	
	//get all post
	PostResponse getAllPost(Integer pageSize,Integer pageNumber);
	
	//get single post
	PostDto getPostById(Integer postid);
	
	//get all posts by category
	List<PostDto> getPostsByCategory(Integer categoryId);
	
	//get all post by user
	List<PostDto> getPostsByUser(Integer userId);
	
	//search by keyword
	List<PostDto> searchPosts(String keyword);
	
	
	
	
}
