DROP TABLE IF EXISTS tb_user CASCADE;
DROP TABLE IF EXISTS tb_verification_token CASCADE;
DROP TABLE IF EXISTS tb_category CASCADE;
DROP TABLE IF EXISTS tb_product CASCADE;
DROP TABLE IF EXISTS tb_market CASCADE;
DROP TABLE IF EXISTS tb_product_quantity CASCADE;

CREATE TABLE tb_user(
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	name VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	password VARCHAR(255) NOT NULL,
	role VARCHAR(50) NOT NULL,
	CONSTRAINT pk_user_id PRIMARY KEY (id),
	CONSTRAINT ck_tb_user_role CHECK (role IN ('USER')),
	CONSTRAINT uk_user_email UNIQUE (email)
);

CREATE TABLE tb_verification_token (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	token UUID NOT NULL,
	data_expiracao TIMESTAMP NOT NULL,
	user_id BIGINT NOT NULL,
	CONSTRAINT pk_verification_token_id PRIMARY KEY (id),
	CONSTRAINT fk_verification_token_user FOREIGN KEY (user_id) REFERENCES tb_user ON DELETE CASCADE
);

CREATE TABLE tb_category(
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	name VARCHAR(50) NOT NULL,
	user_id BIGINT NOT NULL,
	CONSTRAINT pk_category_id PRIMARY KEY (id),
	CONSTRAINT uk_category_name_user UNIQUE (name, user_id),
	CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES tb_user ON DELETE CASCADE
);

CREATE TABLE tb_product(
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	name VARCHAR(50) NOT NULL,
	category_id BIGINT,
	user_id BIGINT NOT NULL,
	active BOOLEAN NOT NULL,
	CONSTRAINT pk_product_id PRIMARY KEY (id),
	CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES tb_category ON DELETE SET NULL,
	CONSTRAINT fk_product_user FOREIGN KEY (user_id) REFERENCES tb_user ON DELETE CASCADE
);

CREATE TABLE tb_market(
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	date_market DATE NOT NULL,
	total_value NUMERIC(12, 2),
	observation VARCHAR(255),
	user_id BIGINT NOT NULL,
	CONSTRAINT pk_market_id PRIMARY KEY (id),
	CONSTRAINT fk_market_user FOREIGN KEY (user_id) REFERENCES tb_user ON DELETE CASCADE
);

CREATE TABLE tb_product_quantity (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	product_id BIGINT NOT NULL,
	quantity INT NOT NULL,
	unit_value NUMERIC(12, 2),
	market_id BIGINT NOT NULL,
	CONSTRAINT pk_product_quantity_id PRIMARY KEY (id),
	CONSTRAINT fk_product_quantity_product FOREIGN KEY (product_id) REFERENCES tb_product,
	CONSTRAINT fk_product_quantity_market FOREIGN KEY (market_id) REFERENCES tb_market ON DELETE CASCADE
);


