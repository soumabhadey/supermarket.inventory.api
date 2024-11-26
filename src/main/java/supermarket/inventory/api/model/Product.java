package supermarket.inventory.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Product", uniqueConstraints = { @UniqueConstraint(columnNames = { "product_name", "expiry_date" }) })
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int id;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private double mrp;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true, foreignKey = @ForeignKey(name = "FK_Product_Category"))
    private Category category;

    @Column(nullable = false)
    private int count;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = true, foreignKey = @ForeignKey(name = "FK_Product_Supplier"))
    private Supplier supplier;

    public Product() {
        super();
    }

    public Product(String name, double mrp, Category category, int count, LocalDate expiryDate, Supplier supplier) {
        super();
        this.name = name;
        this.mrp = mrp;
        this.category = category;
        this.count = count;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
    }

    public Product(int id, String name, double mrp, Category category, int count, LocalDate expiryDate,
            Supplier supplier) {
        super();
        this.id = id;
        this.name = name;
        this.mrp = mrp;
        this.category = category;
        this.count = count;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", mrp=" + mrp + ", category=" + category + ", count=" + count
                + ", expiryDate=" + expiryDate + ", supplier=" + supplier + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(expiryDate, id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        return Objects.equals(expiryDate, other.expiryDate) && id == other.id && Objects.equals(name, other.name);
    }

}
