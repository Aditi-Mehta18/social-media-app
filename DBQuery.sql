CREATE DATABASE social_media_app;
USE social_media_app;


CREATE TABLE IF NOT EXISTS users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookmarks (
  user_id INT NOT NULL,
  post_id VARCHAR(24) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_bookmark UNIQUE (user_id, post_id),
  INDEX idx_bookmarks_user (user_id)
);


CREATE TABLE IF NOT EXISTS profiles (
  profile_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  bio VARCHAR(255),
  location VARCHAR(100),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX (user_id),
  CONSTRAINT fk_profiles_user
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS follows (
  user_id INT NOT NULL,
  target_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uq_follow UNIQUE (user_id, target_id),
  INDEX idx_follow_user (user_id),
  INDEX idx_follow_target (target_id),
  CONSTRAINT fk_follows_user
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_follows_target
    FOREIGN KEY (target_id) REFERENCES users(user_id)
    ON DELETE CASCADE
);