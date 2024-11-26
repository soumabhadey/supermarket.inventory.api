ALTER TABLE Product DROP FOREIGN KEY FK_Product_Category;

ALTER TABLE Product ADD CONSTRAINT FK_Product_Category_Real FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE SET NULL;

ALTER TABLE Product DROP FOREIGN KEY FK_Product_Supplier;

ALTER TABLE Product ADD CONSTRAINT FK_Product_Supplier_Real FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id) ON DELETE SET NULL;