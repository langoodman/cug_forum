package com.cug.forum.modules.service.impl;

import com.cug.forum.modules.repository.CommentRepository;
import com.cug.forum.modules.service.complementor.CommentComplementor;
import com.cug.forum.modules.data.CommentVO;
import com.cug.forum.modules.entity.Comment;
import com.cug.forum.modules.service.CommentService;
import com.cug.forum.modules.service.PostService;
import com.cug.forum.modules.service.UserEventService;
import com.cug.forum.modules.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private UserEventService userEventService;
	@Autowired
	private PostService postService;
	
	@Override
	public Page<CommentVO> paging4Admin(Pageable pageable) {
		Page<Comment> page = commentRepository.findAll(pageable);
		List<CommentVO> rets = CommentComplementor.of(page.getContent())
				.flutBuildUser()
				.getComments();
		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	public Page<CommentVO> pagingByAuthorId(Pageable pageable, long authorId) {
		Page<Comment> page = commentRepository.findAllByAuthorId(pageable, authorId);

		List<CommentVO> rets = CommentComplementor.of(page.getContent())
				.flutBuildUser()
				.flutBuildParent()
				.flutBuildPost()
				.getComments();
		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	public Page<CommentVO> pagingByPostId(Pageable pageable, long postId) {
		Page<Comment> page = commentRepository.findAllByPostId(pageable, postId);

		List<CommentVO> rets = CommentComplementor.of(page.getContent())
				.flutBuildUser()
				.flutBuildParent()
				.getComments();
		return new PageImpl<>(rets, pageable, page.getTotalElements());
	}

	@Override
	public List<CommentVO> findLatestComments(int maxResults) {
		Pageable pageable = PageRequest.of(0, maxResults, Sort.by(Sort.Direction.DESC, "id"));
		Page<Comment> page = commentRepository.findAll(pageable);
		return CommentComplementor.of(page.getContent())
				.flutBuildUser()
				.getComments();
	}

	@Override
	public Map<Long, CommentVO> findByIds(Set<Long> ids) {
		List<Comment> list = commentRepository.findAllById(ids);
		return CommentComplementor.of(list)
				.flutBuildUser()
				.toMap();
	}

	@Override
	public Comment findById(long id) {
		return commentRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public long post(CommentVO comment) {
		Comment po = new Comment();
		
		po.setAuthorId(comment.getAuthorId());
		po.setPostId(comment.getPostId());
		po.setContent(comment.getContent());
		po.setCreated(new Date());
		po.setPid(comment.getPid());
		commentRepository.save(po);

		userEventService.identityComment(comment.getAuthorId(), true);
		return po.getId();
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void delete(List<Long> ids) {
		List<Comment> list = commentRepository.removeByIdIn(ids);
		if (CollectionUtils.isNotEmpty(list)) {
			list.forEach(po -> {
				userEventService.identityComment(po.getAuthorId(), false);
			});
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void delete(long id, long authorId) {
		Optional<Comment> optional = commentRepository.findById(id);
		if (optional.isPresent()) {
			Comment po = optional.get();
			// ??????????????????????????????????????????
			Assert.isTrue(po.getAuthorId() == authorId, "????????????");
			commentRepository.deleteById(id);

			userEventService.identityComment(authorId, false);
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteByPostId(long postId) {
		List<Comment> list = commentRepository.removeByPostId(postId);
		if (CollectionUtils.isNotEmpty(list)) {
			Set<Long> userIds = new HashSet<>();
			list.forEach(n -> userIds.add(n.getAuthorId()));
			userEventService.identityComment(userIds, false);
		}
	}

	@Override
	public long count() {
		return commentRepository.count();
	}

	@Override
	public long countByAuthorIdAndPostId(long authorId, long toId) {
		return commentRepository.countByAuthorIdAndPostId(authorId, toId);
	}

}
