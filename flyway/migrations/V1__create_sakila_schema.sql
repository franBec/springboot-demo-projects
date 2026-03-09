CREATE TABLE actor (
    actor_id    SMALLINT      NOT NULL,
    first_name  VARCHAR(45)   NOT NULL,
    last_name   VARCHAR(45)   NOT NULL,
    last_update TIMESTAMP     NOT NULL,
    CONSTRAINT pk_actor PRIMARY KEY (actor_id)
);

CREATE TABLE address (
    address_id  SMALLINT      NOT NULL,
    address     VARCHAR(50)   NOT NULL,
    address2    VARCHAR(50),
    district    VARCHAR(20)   NOT NULL,
    city_id     SMALLINT      NOT NULL,
    postal_code VARCHAR(10),
    phone       VARCHAR(20)   NOT NULL,
    last_update TIMESTAMP     NOT NULL,
    CONSTRAINT pk_address PRIMARY KEY (address_id)
);

CREATE INDEX idx_fk_city_id ON address (city_id);

CREATE TABLE category (
    category_id SMALLINT      NOT NULL,
    name        VARCHAR(25)   NOT NULL,
    last_update TIMESTAMP     NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (category_id)
);

CREATE TABLE country (
    country_id  SMALLINT      NOT NULL,
    country     VARCHAR(50)   NOT NULL,
    last_update TIMESTAMP     NOT NULL,
    CONSTRAINT pk_country PRIMARY KEY (country_id)
);

CREATE TABLE language (
    language_id SMALLINT      NOT NULL,
    name        VARCHAR(20)   NOT NULL,
    last_update TIMESTAMP     NOT NULL,
    CONSTRAINT pk_language PRIMARY KEY (language_id)
);

CREATE TABLE city (
    city_id     SMALLINT      NOT NULL,
    city        VARCHAR(50)   NOT NULL,
    country_id  SMALLINT      NOT NULL,
    last_update TIMESTAMP     NOT NULL,
    CONSTRAINT pk_city PRIMARY KEY (city_id)
);

CREATE TABLE store (
    store_id         SMALLINT  NOT NULL,
    manager_staff_id SMALLINT  NOT NULL,
    address_id       SMALLINT  NOT NULL,
    last_update      TIMESTAMP NOT NULL,
    CONSTRAINT pk_store PRIMARY KEY (store_id)
);

CREATE TABLE staff (
    staff_id    SMALLINT      NOT NULL,
    first_name  VARCHAR(45)   NOT NULL,
    last_name   VARCHAR(45)   NOT NULL,
    address_id  SMALLINT      NOT NULL,
    picture     BYTEA,
    email       VARCHAR(50),
    store_id    SMALLINT      NOT NULL,
    active      BOOLEAN       NOT NULL,
    username    VARCHAR(16)   NOT NULL,
    password    VARCHAR(40),
    last_update TIMESTAMP     NOT NULL,
    CONSTRAINT pk_staff PRIMARY KEY (staff_id)
);

CREATE TABLE customer (
    customer_id SMALLINT      NOT NULL,
    store_id    SMALLINT      NOT NULL,
    first_name  VARCHAR(45)   NOT NULL,
    last_name   VARCHAR(45)   NOT NULL,
    email       VARCHAR(50),
    address_id  SMALLINT      NOT NULL,
    active      BOOLEAN       NOT NULL,
    create_date TIMESTAMP     NOT NULL,
    last_update TIMESTAMP     NOT NULL,
    CONSTRAINT pk_customer PRIMARY KEY (customer_id)
);

CREATE TABLE film (
    film_id              SMALLINT       NOT NULL,
    title                VARCHAR(255)   NOT NULL,
    description          TEXT,
    release_year         DATE,
    language_id          SMALLINT       NOT NULL,
    original_language_id SMALLINT,
    rental_duration      SMALLINT       NOT NULL,
    rental_rate          DECIMAL(4, 2)  NOT NULL,
    length               SMALLINT,
    replacement_cost     DECIMAL(5, 2)  NOT NULL,
    rating               VARCHAR(5),
    special_features     VARCHAR(54),
    last_update          TIMESTAMP      NOT NULL,
    CONSTRAINT pk_film PRIMARY KEY (film_id)
);

CREATE INDEX idx_title ON film (title);

CREATE TABLE inventory (
    inventory_id INTEGER   NOT NULL,
    film_id      SMALLINT  NOT NULL,
    store_id     SMALLINT  NOT NULL,
    last_update  TIMESTAMP NOT NULL,
    CONSTRAINT pk_inventory PRIMARY KEY (inventory_id)
);

CREATE TABLE rental (
    rental_id    INTEGER   NOT NULL,
    rental_date  TIMESTAMP NOT NULL,
    inventory_id INTEGER   NOT NULL,
    customer_id  SMALLINT  NOT NULL,
    return_date  TIMESTAMP,
    staff_id     SMALLINT  NOT NULL,
    last_update  TIMESTAMP NOT NULL,
    CONSTRAINT pk_rental PRIMARY KEY (rental_id)
);

CREATE TABLE film_actor (
    actor_id    SMALLINT  NOT NULL,
    film_id     SMALLINT  NOT NULL,
    last_update TIMESTAMP NOT NULL,
    CONSTRAINT pk_film_actor PRIMARY KEY (actor_id, film_id)
);

CREATE TABLE film_category (
    film_id     SMALLINT  NOT NULL,
    category_id SMALLINT  NOT NULL,
    last_update TIMESTAMP NOT NULL,
    CONSTRAINT pk_film_category PRIMARY KEY (category_id, film_id)
);

CREATE TABLE payment (
    payment_id   SMALLINT      NOT NULL,
    customer_id  SMALLINT      NOT NULL,
    staff_id     SMALLINT      NOT NULL,
    rental_id    INTEGER,
    amount       DECIMAL(5, 2) NOT NULL,
    payment_date TIMESTAMP     NOT NULL,
    last_update  TIMESTAMP     NOT NULL,
    CONSTRAINT pk_payment PRIMARY KEY (payment_id)
);

ALTER TABLE film
    ADD CONSTRAINT fk_film_language
    FOREIGN KEY (language_id) REFERENCES language (language_id);

ALTER TABLE film
    ADD CONSTRAINT fk_film_language_original
    FOREIGN KEY (original_language_id) REFERENCES language (language_id);

ALTER TABLE film_actor
    ADD CONSTRAINT fk_film_actor_actor
    FOREIGN KEY (actor_id) REFERENCES actor (actor_id);

ALTER TABLE film_actor
    ADD CONSTRAINT fk_film_actor_film
    FOREIGN KEY (film_id) REFERENCES film (film_id);

ALTER TABLE film_category
    ADD CONSTRAINT fk_film_category_film
    FOREIGN KEY (film_id) REFERENCES film (film_id);

ALTER TABLE film_category
    ADD CONSTRAINT fk_film_category_category
    FOREIGN KEY (category_id) REFERENCES category (category_id);

ALTER TABLE inventory
    ADD CONSTRAINT fk_inventory_film
    FOREIGN KEY (film_id) REFERENCES film (film_id);

ALTER TABLE inventory
    ADD CONSTRAINT fk_inventory_store
    FOREIGN KEY (store_id) REFERENCES store (store_id);

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_customer
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id);

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_rental
    FOREIGN KEY (rental_id) REFERENCES rental (rental_id);

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_staff
    FOREIGN KEY (staff_id) REFERENCES staff (staff_id);

ALTER TABLE rental
    ADD CONSTRAINT fk_rental_customer
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id);

ALTER TABLE rental
    ADD CONSTRAINT fk_rental_inventory
    FOREIGN KEY (inventory_id) REFERENCES inventory (inventory_id);

ALTER TABLE rental
    ADD CONSTRAINT fk_rental_staff
    FOREIGN KEY (staff_id) REFERENCES staff (staff_id);

ALTER TABLE address
    ADD CONSTRAINT fk_address_city
    FOREIGN KEY (city_id) REFERENCES city (city_id);

ALTER TABLE city
    ADD CONSTRAINT fk_city_country
    FOREIGN KEY (country_id) REFERENCES country (country_id);

ALTER TABLE customer
    ADD CONSTRAINT fk_customer_address
    FOREIGN KEY (address_id) REFERENCES address (address_id);

ALTER TABLE customer
    ADD CONSTRAINT fk_customer_store
    FOREIGN KEY (store_id) REFERENCES store (store_id);

ALTER TABLE staff
    ADD CONSTRAINT fk_staff_address
    FOREIGN KEY (address_id) REFERENCES address (address_id);

ALTER TABLE staff
    ADD CONSTRAINT fk_staff_store
    FOREIGN KEY (store_id) REFERENCES store (store_id);

ALTER TABLE store
    ADD CONSTRAINT fk_store_address
    FOREIGN KEY (address_id) REFERENCES address (address_id);

ALTER TABLE store
    ADD CONSTRAINT fk_store_manager
    FOREIGN KEY (manager_staff_id) REFERENCES staff (staff_id);
