package com.example.hyuk_blog.service;

import com.example.hyuk_blog.dto.PostDto;
import com.example.hyuk_blog.entity.Category;
import com.example.hyuk_blog.entity.PostKr;
import com.example.hyuk_blog.entity.PostJp;
import com.example.hyuk_blog.repository.PostKrRepository;
import com.example.hyuk_blog.repository.PostJpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostKrRepository postKrRepository;
    @Autowired
    private PostJpRepository postJpRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private CommentService commentService;

    // 언어 감지 (ko/ja)
    private String detectLang() {
        String lang = null;
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpSession session = attr.getRequest().getSession(false);
            if (session != null) {
                Object langObj = session.getAttribute("lang");
                if (langObj != null) lang = langObj.toString();
            }
        }
        if (lang == null) {
            lang = LocaleContextHolder.getLocale().getLanguage();
        }
        return lang;
    }

    // 모든 공개 게시글 조회 (lang 파라미터 명시)
    public List<PostDto> getAllPublishedPosts(String lang) {
        if ("ja".equals(lang)) {
            return postJpRepository.findByPublishedOrderByCreatedAtDesc(true)
                    .stream()
                    .map(post -> {
                        PostDto dto = PostDto.fromJpEntity(post);
                        dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                        dto.setCommentCount(commentService.getCommentCount(post.getId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        } else {
            return postKrRepository.findByPublishedOrderByCreatedAtDesc(true)
                    .stream()
                    .map(post -> {
                        PostDto dto = PostDto.fromKrEntity(post);
                        dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                        dto.setCommentCount(commentService.getCommentCount(post.getId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
    }

    // lang 자동 감지
    public List<PostDto> getAllPublishedPosts() {
        return getAllPublishedPosts(detectLang());
    }

    // 모든 게시글 조회 (관리자용)
    public List<PostDto> getAllPosts(String lang) {
        if ("ja".equals(lang)) {
            return postJpRepository.findAllOrderByCreatedAtDesc()
                    .stream()
                    .map(post -> {
                        PostDto dto = PostDto.fromJpEntity(post);
                        dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                        dto.setCommentCount(commentService.getCommentCount(post.getId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        } else {
            return postKrRepository.findAllOrderByCreatedAtDesc()
                    .stream()
                    .map(post -> {
                        PostDto dto = PostDto.fromKrEntity(post);
                        dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                        dto.setCommentCount(commentService.getCommentCount(post.getId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
    }

    // ID로 게시글 조회
    public Optional<PostDto> getPostById(Long id, String lang) {
        if ("ja".equals(lang)) {
            return postJpRepository.findById(id).map(post -> {
                PostDto dto = PostDto.fromJpEntity(post);
                dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                dto.setCommentCount(commentService.getCommentCount(post.getId()));
                return dto;
            });
        } else {
            return postKrRepository.findById(id).map(post -> {
                PostDto dto = PostDto.fromKrEntity(post);
                dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                dto.setCommentCount(commentService.getCommentCount(post.getId()));
                return dto;
            });
        }
    }

    // 게시글 저장
    public PostDto savePost(PostDto postDto, String lang) {
        if ("ja".equals(lang)) {
            // 오직 posts_jp에만 저장
            PostJp saved = postJpRepository.save(postDto.toJpEntity());
            return PostDto.fromJpEntity(saved);
        } else if ("ko".equals(lang)) {
            // 오직 posts_kr에만 저장
            PostKr saved = postKrRepository.save(postDto.toKrEntity());
            return PostDto.fromKrEntity(saved);
        } else {
            // 예외 상황: 잘못된 lang 값이 들어온 경우
            System.err.println("[PostService] savePost: Unexpected lang value: " + lang);
            throw new IllegalArgumentException("Unsupported language: " + lang);
        }
    }

    // 게시글 수정
    public Optional<PostDto> updatePost(Long id, PostDto postDto, String lang) {
        if ("ja".equals(lang)) {
            return postJpRepository.findById(id).map(existing -> {
                existing.setTitle(postDto.getTitleJa());
                existing.setSummary(postDto.getSummaryJa());
                existing.setContent(postDto.getContentJa());
                existing.setImageUrl(postDto.getImageUrl());
                existing.setPublished(postDto.isPublished());
                existing.setCategory(postDto.getCategory());
                return PostDto.fromJpEntity(postJpRepository.save(existing));
            });
        } else {
            return postKrRepository.findById(id).map(existing -> {
                existing.setTitle(postDto.getTitleKo());
                existing.setSummary(postDto.getSummaryKo());
                existing.setContent(postDto.getContentKo());
                existing.setImageUrl(postDto.getImageUrl());
                existing.setPublished(postDto.isPublished());
                existing.setCategory(postDto.getCategory());
                return PostDto.fromKrEntity(postKrRepository.save(existing));
            });
        }
    }

    // 게시글 삭제
    public boolean deletePost(Long id, String lang) {
        if ("ja".equals(lang)) {
            if (postJpRepository.existsById(id)) {
                postJpRepository.deleteById(id);
                return true;
            }
        } else {
            if (postKrRepository.existsById(id)) {
                postKrRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    // 제목으로 검색 (공개된 게시글만)
    public List<PostDto> searchPublishedPosts(String query, String lang) {
        if ("ja".equals(lang)) {
            return postJpRepository.findByTitleContainingAndPublishedOrderByCreatedAtDesc(query, true)
                    .stream()
                    .map(post -> {
                        PostDto dto = PostDto.fromJpEntity(post);
                        dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                        dto.setCommentCount(commentService.getCommentCount(post.getId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        } else {
            return postKrRepository.findByTitleContainingAndPublishedOrderByCreatedAtDesc(query, true)
                    .stream()
                    .map(post -> {
                        PostDto dto = PostDto.fromKrEntity(post);
                        dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                        dto.setCommentCount(commentService.getCommentCount(post.getId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
    }

    // 카테고리로 공개된 게시글 조회
    public List<PostDto> getPublishedPostsByCategory(Category category, String lang) {
        if ("ja".equals(lang)) {
            return postJpRepository.findByCategoryAndPublishedOrderByCreatedAtDesc(category, true)
                    .stream()
                    .map(post -> {
                        PostDto dto = PostDto.fromJpEntity(post);
                        dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                        dto.setCommentCount(commentService.getCommentCount(post.getId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        } else {
            return postKrRepository.findByCategoryAndPublishedOrderByCreatedAtDesc(category, true)
                    .stream()
                    .map(post -> {
                        PostDto dto = PostDto.fromKrEntity(post);
                        dto.setLikeCount(likeService.getLikeCount(post.getId(), lang));
                        dto.setCommentCount(commentService.getCommentCount(post.getId()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        }
    }
}