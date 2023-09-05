DROP TABLE IF EXISTS CART;
CREATE TABLE CART (
    CART_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    CART_DESCRIPTION VARCHAR NOT NULL
);

DROP TABLE IF EXISTS ITEM;
CREATE TABLE ITEM (
    ITEM_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    ITEM_DESCRIPTION VARCHAR NOT NULL,
    ITEM_QUANTITY INT NOT NULL DEFAULT 0,
    CART_ID BIGINT NOT NULL
);