CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    password TEXT NOT NULL,
    salt VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    role_id BIGINT PRIMARY KEY,
    role_name varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_role_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) references users(user_id),
    FOREIGN KEY (role_id) references roles(role_id)
);

CREATE TABLE IF NOT EXISTS posts (
    post_id BIGINT PRIMARY KEY,
    post_name VARCHAR(255) NOT NULL,
    post_image VARCHAR(255),
    post_date DATE NOT NULL,
    post_content TEXT NOT NULL,
    post_author BIGINT NOT NULL,
    visitor_count BIGINT NOT NULL DEFAULT 0,
    FOREIGN KEY (post_author) references users(user_id)
);

CREATE TABLE IF NOT EXISTS tags (
    tag_id BIGINT PRIMARY KEY,
    tag_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS post_tags (
    post_tag_id BIGINT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    FOREIGN KEY (post_id) references posts(post_id),
    FOREIGN KEY (tag_id) references tags(tag_id)
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    comment_content VARCHAR(120) NOT NULL,
    comment_date DATE NOT NULL,
    FOREIGN KEY post_id references posts(post_id)
);