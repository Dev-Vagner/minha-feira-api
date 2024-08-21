DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;
DROP TABLE IF EXISTS produto CASCADE;
DROP TABLE IF EXISTS feira CASCADE;
DROP TABLE IF EXISTS produto_quantidade CASCADE;

CREATE TABLE usuario(
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(50) NOT NULL,
	CONSTRAINT pk_usuario_id PRIMARY KEY (id),
	CONSTRAINT uk_usuario_email UNIQUE (email)
);

CREATE TABLE categoria(
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	nome VARCHAR(50) NOT NULL,
	usuario_id BIGINT NOT NULL,
	CONSTRAINT pk_categoria_id PRIMARY KEY (id),
	CONSTRAINT uk_categoria_nome_usuario UNIQUE (nome, usuario_id),
	CONSTRAINT fk_categoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuario ON DELETE CASCADE
);

CREATE TABLE produto(
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	nome VARCHAR(50) NOT NULL,
	categoria_id BIGINT,
	usuario_id BIGINT NOT NULL,
	ativo BOOLEAN NOT NULL,
	CONSTRAINT pk_produto_id PRIMARY KEY (id),
	CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria ON DELETE SET NULL,
	CONSTRAINT fk_produto_usuario FOREIGN KEY (usuario_id) REFERENCES usuario ON DELETE CASCADE
);

CREATE TABLE feira (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	data_feira DATE NOT NULL,
	valor_total NUMERIC(12, 2),
	usuario_id BIGINT NOT NULL,
	CONSTRAINT pk_feira_id PRIMARY KEY (id),
	CONSTRAINT fk_feira_usuario FOREIGN KEY (usuario_id) REFERENCES usuario ON DELETE CASCADE
);

CREATE TABLE produto_quantidade (
	id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
	produto_id BIGINT NOT NULL,
	quantidade INT NOT NULL,
	valor_unidade NUMERIC(12, 2),
	feira_id BIGINT NOT NULL,
	CONSTRAINT pk_produto_quantidade_id PRIMARY KEY (id),
	CONSTRAINT uk_produto_feira UNIQUE (produto_id, feira_id),
	CONSTRAINT fk_produto_quantidade_produto FOREIGN KEY (produto_id) REFERENCES produto,
	CONSTRAINT fk_produto_quantidade_feira FOREIGN KEY (feira_id) REFERENCES feira ON DELETE CASCADE
);


