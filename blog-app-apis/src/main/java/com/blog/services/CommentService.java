package com.blog.services;

import com.blog.payloads.CommentDto;

public interface CommentService {

	
	//create
	 CommentDto   createComment(CommentDto commentDto ,Integer postId);
	 
	 
	 //delete
	 void deleteComment(Integer commentId);
}
