package supermarket.inventory.api.dao.structure;

import java.util.List;

import supermarket.inventory.api.model.Supplier;

public interface ISupplierDAO {
    Supplier createSupplier(Supplier supplier);
    Supplier readSupplier(int id);
    List<Supplier> readAllSuppliers();
    Supplier updateSupplier(int id, Supplier newSupplier);
    Supplier deleteSupplier(int id);
}
