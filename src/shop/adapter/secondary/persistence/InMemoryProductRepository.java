package shop.adapter.secondary.persistence;

import shop.domain.model.Product;
import shop.domain.port.secondary.ProductRepository;

import java.util.*;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    public InMemoryProductRepository() {
        // Инициализация тестовыми данными
        addProduct(new Product("P1", "Смартфон", 24999.0));
        addProduct(new Product("P2", "Ноутбук", 49999.0));
        addProduct(new Product("P3", "Наушники", 3999.0));
        addProduct(new Product("P4", "Клавиатура", 1999.0));
        addProduct(new Product("P5", "Мышь", 999.0));
    }

    private void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
}