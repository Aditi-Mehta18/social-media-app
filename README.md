# Social Media App (Java, JDBC, MySQL, MongoDB, Console)

A hybrid-DB console backend demonstrating clean DAO → Service → Model layering, with MySQL (users, profiles, follows)
and MongoDB (posts, likes, comments). Unit tested with JUnit 5 + Mockito. Logging via Logback.

## Architecture

- config: DB connections (existing MySQLConnection, MongoConnection)
- dao: JDBC DAOs and Mongo DAOs
- service: DI-friendly services (no-arg for Main + injected-DAO constructors for tests)
- validation: input validators
- dto: feed DTOs
- model: entities (User, Profile, Post, Comment, Like)
- test: JUnit + Mockito service tests

### Key flows

- User: register (BCrypt), login, get profile
- Follow: follow/unfollow, list following
- Feed: timeline of followees’ posts with like counts and top 2 comments
- Search: users (MySQL LIKE), posts (Mongo text index)

## Why Hybrid DB

- MySQL: strong consistency and relational constraints for users/profiles/follows
- MongoDB: flexible, append-heavy content (posts/likes/comments) + text search and simple fan-out read pattern

## DB schema and indexes

- MySQL
    - users(user_id PK, username UNIQUE, email UNIQUE, full_name, password_hash, created_at)
    - profiles(profile_id PK, user_id FK, bio, location, created_at)
    - follows(user_id, target_id, created_at, UNIQUE(user_id, target_id), INDEX on user_id, target_id)
- Mongo
    - posts: { _id:ObjectId, userId:int, content:string, tags:[string], createdAt:long } with indexes: { userId:1 }, {
      content:"text" }
    - comments: { _id, postId:ObjectId, userId:int, text, commentedAt:long } with index: { postId:1 }
    - likes: { _id, postId:ObjectId, userId:int, likedAt:long } with indexes: { postId:1 }, unique(postId,userId)

Create index example (Mongo shell):

```
db.posts.createIndex({ content: "text" })
db.posts.createIndex({ userId: 1 })
db.comments.createIndex({ postId: 1 })
db.likes.createIndex({ postId: 1 })
db.likes.createIndex({ postId: 1, userId: 1 }, { unique: true })
```

## Run

- Ensure MySQL and MongoDB are running and MySQLConnection/MongoConnection point to valid instances.
- In IntelliJ, run Main.java.
- Tests: Right-click src/test/java → Run ‘All Tests’.

## Logging

- Logback configured (console + file). Add rolling policies as needed.
- Use MDC (requestId/userId) if integrating with an API layer later.

## Validation & Exceptions

- UserValidator: username/email/password rules
- PostValidator / CommentValidator: simple input guards
- Exceptions: ValidationException, DuplicateUserException, InvalidCredentialsException, UserNotFoundException,
  DatabaseException

## Tests

- DI-based tests with Mockito (no mockito-inline needed)
- Services covered: User, Post, Like, Comment, Profile, Feed, Search

## Sample CLI flows

- Register → Login → Create Post → Like → Comment → View Feed

## Future Enhancements

- Add paging for feed/search
- Seed/test data loaders
- Stronger password policy and rate-limited login
- Integration tests with testcontainers (if allowed)
