package es.uvigo.esei.dgss.exercise.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import es.uvigo.esei.dgss.exercises.domain.Comment;
import es.uvigo.esei.dgss.exercises.domain.Like;
import es.uvigo.esei.dgss.exercises.domain.Link;
import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;
import es.uvigo.esei.dgss.exercises.domain.PostDTO;
import es.uvigo.esei.dgss.exercises.domain.User;
import es.uvigo.esei.dgss.exercises.domain.Video;

@Stateless
public class PostEJB {
	
	@PersistenceContext
	private EntityManager em;
	
	@EJB
	private StatisticsEJB statisticsEJB;
	
	@EJB
	private UserEJB userEJB;
	
	@Resource
	private SessionContext ctx;
	
	public Post findPostById(int id) {
		Post toRet = em.find(Post.class, id);
		if(userEJB.isAuthenticated(toRet.getUser().getLogin())) {
			return toRet;
		} 
		return null;
	}
	
	public Comment findCommentById(int id) {
		return em.find(Comment.class, id);
	}
	
	public List<Post> getPosts() {
		List<Post> toRet = new ArrayList<>();
		toRet = em.createQuery("SELECT p FROM Post p", Post.class).getResultList();
		return toRet;
	} 
	
	public List<Post> getPostsOfUser(User user) {
		List<Post> toRet = new ArrayList<>();
		toRet = em.createQuery("SELECT p FROM Post p WHERE p.user = :us", Post.class).setParameter("us", user).getResultList();
		return toRet;
	}
	
	public List<Post> getFriendsPosts(User user) {
		Query query = em
				.createQuery("SELECT p FROM Post p, UserFriendship uf WHERE "
						+ "(uf.user1 = :us AND p.user = uf.user2) OR "
						+ "uf.user2 = :us AND p.user = uf.user1)",
						Post.class)
				.setParameter("us", user);

		List<Post> posts = (List<Post>) query.getResultList();
		return posts;
	}
	
	public List<Video> getVideos() {
		List<Video> toRet = new ArrayList<>();
		toRet = em.createQuery("SELECT p FROM Post p", Video.class).getResultList();
		return toRet;
	} 
	
	public List<Video> getVideosOfUser(User user) {
		List<Video> toRet = new ArrayList<>();
		toRet = em.createQuery("SELECT p FROM Post p WHERE p.user = :us", Video.class).setParameter("us", user).getResultList();
		return toRet;
	}
	
	public List<Photo> getPhotos() {
		List<Photo> toRet = new ArrayList<>();
		toRet = em.createQuery("SELECT p FROM Post p", Photo.class).getResultList();
		return toRet;
	} 
	
	public List<Photo> getPhotosOfUser(User user) {
		List<Photo> toRet = new ArrayList<>();
		toRet = em.createQuery("SELECT p FROM Post p WHERE p.user = :us", Photo.class).setParameter("us", user).getResultList();
		return toRet;
	}
	
	public List<Link> getLinks() {
		List<Link> toRet = new ArrayList<>();
		toRet = em.createQuery("SELECT p FROM Post p", Link.class).getResultList();
		return toRet;
	} 
	
	public List<Link> getLinksOfUser(User user) {
		List<Link> toRet = new ArrayList<>();
		toRet = em.createQuery("SELECT p FROM Post p WHERE p.user = :us", Link.class).setParameter("us", user).getResultList();
		return toRet;
	}
	
	public Video createVideoREST(Video video){
		video.setUser(userEJB.findUserById(ctx.getCallerPrincipal().getName()));
		em.persist(video);
		statisticsEJB.incrementPostCount();
		return video;
	}
	
	public Video createVideo(Video video){
		em.persist(video);
		statisticsEJB.incrementPostCount();
		return video;
	}
	
	public Photo createPhotoREST(Photo photo){
		photo.setUser(userEJB.findUserById(ctx.getCallerPrincipal().getName()));
		em.persist(photo);
		statisticsEJB.incrementPostCount();
		return photo;
	}
	
	public Photo createPhoto(Photo photo){
		em.persist(photo);
		statisticsEJB.incrementPostCount();
		return photo;
	}
	
	public Link createLinkREST(Link link){
		link.setUser(userEJB.findUserById(ctx.getCallerPrincipal().getName()));
		em.persist(link);
		statisticsEJB.incrementPostCount();
		return link;
	}
	
	public Link createLink(Link link){
		em.persist(link);
		statisticsEJB.incrementPostCount();
		return link;
	}
	
	public Video updateVideo(int id, Integer duration){
		Video video = (Video) findPostById(id);
		video.setDuration(duration);
		video.setDate(new Date());
		em.persist(video);
		return video;
	}
	
	public Photo updatePhoto(int id, Byte [] content){
		Photo photo = (Photo) findPostById(id);
		photo.setContent(content);
		photo.setDate(new Date());
		em.persist(photo);
		return photo;
	}
	
	public Link updateLink(int id, String url){
		Link link = (Link) findPostById(id);
		link.setUrl(url);
		link.setDate(new Date());
		em.persist(link);
		return link;
	}
	
	public void removePost(Post post) {
		em.remove(post);
		statisticsEJB.decrementPostCount();
	}
	
	public void deletePost(int id){
		Post post = findPostById(id);
		em.remove(post);
		statisticsEJB.decrementPostCount();
	}
	
	public Comment addComment(Post post, Comment comment){
		post.addComment(comment);
		em.persist(post);
		return comment;
	}
	
	public Comment updateComment(int id, String text){
		Comment comment = findCommentById(id);
		comment.setComment(text);
		comment.setDate(new Date());
		em.persist(comment);
		return comment;
	}
	
	public void removeComment(Comment comment){
		em.remove(comment);
	}
	
	public List<Post> getCommentedPostsByFriends(User user, Date date) {
		Query query = em
				.createQuery("SELECT p FROM Post p WHERE "
						+ "p in (SELECT c.post FROM Comment c WHERE "
						+ "(c.user in (SELECT uf.user1 FROM UserFriendship uf WHERE uf.user2 = :us) OR "
						+ "c.user in (SELECT uf.user2 FROM UserFriendship uf WHERE uf.user1 = :us)) AND "
						+ "c.date >= :dat)",
						Post.class)
				.setParameter("us", user).setParameter("dat", date);
		
		List<Post> posts = (List<Post>) query.getResultList();
		return posts;
	}
	
	public List<Photo> getPicturesUserLikes(User user) {
		Query query = em
				.createQuery("SELECT p FROM Photo p WHERE "
						+ "p in (SELECT l.post FROM Like l WHERE l.user = :us)", Photo.class)
				.setParameter("us", user);
		
		List<Photo> toRet = (List<Photo>) query.getResultList();
		
		return toRet;
	}
	
	public List<PostDTO> getMyWallPosts() {
		User user = userEJB.findUserById(ctx.getCallerPrincipal().getName());
		
		List <Post> wall = em.createQuery("SELECT p FROM Post p WHERE p.user = :us OR "
				+ "(p.user in (SELECT uf.user1 FROM UserFriendship uf WHERE uf.user2 = :us) OR "
				+ "p.user in (SELECT uf.user2 FROM UserFriendship uf WHERE uf.user1 = :us))", Post.class)
				.setParameter("us", user).getResultList();
		
		List<PostDTO> toRet = new ArrayList<>();
		
		for(Post p : wall) {
			toRet.add(p.toDTO());
		}

		return toRet;
	}
	
	public List<PostDTO> getMyPosts() {
		User user = userEJB.findUserById(ctx.getCallerPrincipal().getName());
		
		List <Post> posts = em.createQuery("SELECT p FROM Post p WHERE p.user = :us", Post.class)
				.setParameter("us", user).getResultList();
		
		List<PostDTO> toRet = new ArrayList<>();
		
		for(Post p : posts) {
			toRet.add(p.toDTO());
		}

		return toRet;
	}
	
	public Like giveLike(int id) {
		Post post = findPostById(id);
		User user = userEJB.findUserById(ctx.getCallerPrincipal().getName());
		Like like = new Like();
		like.setUser(user);
		like.setPost(post);
		
		return userEJB.addLike(like);
	}
}

