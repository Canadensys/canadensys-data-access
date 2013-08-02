/* lookup table structure changes for Vascan 3 */
ALTER TABLE lookup ADD COLUMN `_left` INT;
ALTER TABLE lookup ADD COLUMN `_right` INT;
ALTER TABLE lookup CHANGE COLUMN `order` `_order` VARCHAR(255);
