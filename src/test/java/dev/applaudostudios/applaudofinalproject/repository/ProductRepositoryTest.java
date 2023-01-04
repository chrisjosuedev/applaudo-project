package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.models.Product;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    private Validator validator;
    private Product product1, product2, product3;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        product1 = Product.builder()
                .productName("Camisa")
                .description("Hombre")
                .unitPrice(32.32)
                .stock(23)
                .status(true)
                .build();

        product2 = Product.builder()
                .productName("Pulsera")
                .description("Con rallas")
                .unitPrice(13.22)
                .stock(12)
                .status(true)
                .build();

        product3 = Product.builder()
                .productName("Helados")
                .description("Polo Norte")
                .unitPrice(6.42)
                .stock(6)
                .status(false)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Nested
    @DisplayName("My JPA Queries Test")
    class MyJpaTest {
        @Test
        @DisplayName("FindByProductName With Existing Product")
        void givenName_whenFindByProductNameWithAExistingProduct_thenProductIsPresent() {
            Optional<Product> productFound = productRepository.findByProductNameContainingIgnoreCase("camisa");
            assertTrue(productFound.isPresent());
            assertThat(productFound.get())
                    .usingRecursiveComparison()
                    .isEqualTo(product1);
        }

        @Test
        @DisplayName("FindByProduct with a Non Existing Product")
        void givenName_whenFindByProductNameWithANonExistingProduct_thenUserIsNotPresent() {
            Optional<Product> productFound = productRepository.findByProductNameContainingIgnoreCase("NoExists");
            assertThat(productFound).isEmpty();
        }

        @Test
        @DisplayName("FindById With Status True")
        void givenId_whenFindByIdWithTrueProduct_thenProductIsPresent() {
            Optional<Product> productFound = productRepository.findByIdAndStatusIsTrue(product1.getId());

            assertTrue(productFound.isPresent());

            assertThat(productFound.get())
                    .usingRecursiveComparison()
                    .isEqualTo(product1);
        }

        @Test
        @DisplayName("FindById With Status False and returns nothing")
        void givenId_whenFindByIdWithFalseProduct_thenProductDoesntExists() {
            Optional<Product> productFound = productRepository.findByIdAndStatusIsTrue(product3.getId());
            assertTrue(productFound.isEmpty());
        }

        @Test
        @DisplayName("FindAllByStatusIsTrue active products list")
        void givenProducts_whenFindAll_thenProductsListSizeWithStatusTrue() {
            List<Product> productsList = productRepository.findAllByStatusIsTrue();
            assertThat(productsList.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("FindAllByStatusIsTrue active products list with pageable")
        void givenProducts_whenFindAllWithPagination_thenProductPaginationListSizeWithStatusTrue() {
            List<Product> productsList = productRepository.findAllByStatusIsTrue(PageRequest.of(0, 1));
            assertThat(productsList.size()).isEqualTo(1);
            assertThat(productsList.get(0).getProductName()).isEqualTo("Camisa");
        }
    }

    @Nested
    @DisplayName("JPA Implementations | Exceptions")
    class JpaImplementations {
        @Test
        @DisplayName("Save an Product with incorrect Stock get Constraint Exceptions")
        void givenProductData_WhenSaveProductWithInvalidStock_thenConstraintSetList() {
            Product productTest = Product.builder()
                    .productName("Product Name")
                    .description("Description")
                    .stock(-32)
                    .unitPrice(43.43)
                    .status(true)
                    .build();

            Set<ConstraintViolation<Product>> violations = validator.validate(productTest);
            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("Save an Product with Null Product Name and Description get Constraint Exceptions")
        void givenProductData_WhenSaveProductWithNullValues_thenConstraintSetList() {
            Product productTest = Product.builder()
                    .stock(32)
                    .unitPrice(43.43)
                    .status(true)
                    .build();

            Set<ConstraintViolation<Product>> violations = validator.validate(productTest);
            assertThat(violations.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("Save an Product with incorrect Quantity get Constraint Exceptions")
        void givenProductData_WhenSaveProductWithInvalidQuantity_thenConstraintSetList() {
            Product productTest = Product.builder()
                    .productName("Product Name")
                    .description("Description")
                    .stock(32)
                    .unitPrice(-2323)
                    .status(true)
                    .build();

            Set<ConstraintViolation<Product>> violations = validator.validate(productTest);
            assertThat(violations.size()).isEqualTo(1);
        }
    }
}
