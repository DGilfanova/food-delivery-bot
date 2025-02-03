ALTER TABLE order_item
DROP CONSTRAINT order_item_dish_id_fkey;

ALTER TABLE order_item
ADD CONSTRAINT order_item_dish_id_fkey
FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE;
