CREATE TABLE IF NOT EXISTS restaurant
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255)            NOT NULL,
    address    TEXT,
    phone      VARCHAR(20),
    is_active  BOOLEAN   DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    updated_at TIMESTAMP DEFAULT now() NOT NULL

);

CREATE TABLE IF NOT EXISTS menu
(
    id            BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT                  NOT NULL,
    name          VARCHAR(255)            NOT NULL,
    version       VARCHAR(255)            NOT NULL,
    status        VARCHAR(50)             NOT NULL,
    created_at    TIMESTAMP DEFAULT now() NOT NULL,
    updated_at    TIMESTAMP DEFAULT now() NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dish
(
    id           BIGSERIAL PRIMARY KEY,
    menu_id      BIGINT                  NOT NULL,
    name         VARCHAR(255)            NOT NULL,
    description  TEXT,
    price        DECIMAL(10, 2)          NOT NULL,
    is_available BOOLEAN   DEFAULT TRUE,
    created_at   TIMESTAMP DEFAULT now() NOT NULL,
    updated_at   TIMESTAMP DEFAULT now() NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users
(
    id          BIGSERIAL PRIMARY KEY,
    telegram_id BIGINT                  NOT NULL,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    phone       VARCHAR(20),
    created_at  TIMESTAMP DEFAULT now() NOT NULL,
    updated_at  TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT                  NOT NULL,
    restaurant_id BIGINT                  NOT NULL,
    total_price   DECIMAL(10, 2)          NOT NULL,
    status        VARCHAR(50)             NOT NULL,
    created_at    TIMESTAMP DEFAULT now() NOT NULL,
    updated_at    TIMESTAMP DEFAULT now() NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_item
(
    id       BIGSERIAL PRIMARY KEY,
    order_id BIGINT         NOT NULL,
    dish_id  BIGINT         NOT NULL,
    quantity BIGINT         NOT NULL,
    price    DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES orders (id) ON DELETE CASCADE
);
