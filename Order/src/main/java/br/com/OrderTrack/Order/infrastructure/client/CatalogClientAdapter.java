package br.com.OrderTrack.Order.infrastructure.client;

import br.com.OrderTrack.Order.domain.port.out.ProductGateway;
import br.com.OrderTrack.Product.application.product.dto.ProductDetailsDTO;
import br.com.OrderTrack.Product.domain.product.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

public class CatalogClientAdapter implements ProductGateway {

    private final RestTemplate rt = new RestTemplate();

    @Value("${app.services.product-url:http://product-service:8086}")
    private String productServiceUrl;

    @Override
    public Optional<Product> findByName(String name) {
        try {
            String url = UriComponentsBuilder
                    .fromPath(productServiceUrl + "/orderTrack/product/all")
                    .queryParam("name", name)
                    .toUriString();

            // O ideal seria um endpoint específico /search?name={name} que retorna ProductDetailsDTO

            var response = rt.getForObject(url, ProductDetailsDTO.class);
            return Optional.of(response.toDomain());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
