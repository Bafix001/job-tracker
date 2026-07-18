CREATE TABLE "user" (
  id SERIAL PRIMARY KEY,
  pseudo varchar(255) NOT NULL,
  email varchar(255) NOT NULL UNIQUE,
  password varchar(255) NOT NULL,
  created_at date
);

CREATE TABLE refresh_token (
  id SERIAL PRIMARY KEY,
  token varchar(255) NOT NULL UNIQUE,
  user_id bigint NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
  expiry_date timestamp
);
