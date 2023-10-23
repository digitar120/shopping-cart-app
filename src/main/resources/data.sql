-- CART_ID, CART_DESCRIPTION, USER_ID
INSERT INTO CART VALUES (1, 'Carrito 1', 36693120);

insert into product values (1, 'Lápiz');
insert into product values (2, 'Lapicera');
insert into product values (3, 'Voligoma');

-- item_id, item_quantity, cart_id, product_id
INSERT INTO ITEM VALUES (1, 3, 1, 1);
INSERT INTO ITEM VALUES (2, 5, 1, 2);
