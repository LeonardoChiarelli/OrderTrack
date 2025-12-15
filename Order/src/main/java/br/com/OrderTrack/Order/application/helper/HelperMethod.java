package br.com.OrderTrack.Order.application.helper;

import br.com.OrderTrack.Order.infrastructure.product.ProductEntity;
import br.com.OrderTrack.Order.infrastructure.order.JPAOrderRepository;
import br.com.OrderTrack.Order.infrastructure.product.IProductRepository;
import br.com.OrderTrack.Order.domain.exception.ValidationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelperMethod {

    @Autowired
    private JPAOrderRepository orderRepository;

    @Autowired
    private IProductRepository productRepository;

    public static JPAOrderRepository orderRepo;
    public static IProductRepository productRepo;

    public static ProductEntity loadProductsByName(String productName){
        return productRepo.findByName(productName).orElseThrow(() -> new ValidationException("ProductEntity not found"));
    }

    @PostConstruct
    public void init(){
        HelperMethod.orderRepo = orderRepository;
        HelperMethod.productRepo = productRepository;
    }
}
