package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.ProductDto;
import dev.applaudostudios.applaudofinalproject.helpers.db.ProductHelper;
import dev.applaudostudios.applaudofinalproject.helpers.patterns.ObjectNull;
import dev.applaudostudios.applaudofinalproject.models.Product;
import dev.applaudostudios.applaudofinalproject.repository.ProductRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ObjectNull objectNull;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductHelper productHelper;
    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .productName("Camisa")
                .description("Hombre")
                .unitPrice(32.32)
                .stock(23)
                .status(true)
                .build();

        productDto = ProductDto.builder()
                .productName("Updated product name")
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .stock(product.getStock())
                .status(product.isStatus())
                .build();
    }

    @Nested
    @DisplayName("Product Service Tests")
    class ProductServiceImpTest {
        @Test
        @DisplayName("FindAllProducts without Pagination")
        void givenUser_WhenFindAllProductsWithNoPagination_thenReturnList() {
            given(productRepository.findAllByStatusIsTrue()).willReturn(List.of(product));
            List<Product> allProducts = productService.findAll(null, null);
            assertThat(allProducts.size()).isGreaterThan(0);
            assertThat(allProducts.get(0).getProductName()).isEqualTo("Camisa");
        }

        @Test
        @DisplayName("FindAllProducts with Pagination")
        void givenUser_WhenFindAllProductsWithPagination_thenReturnPaginationList() {
            given(productRepository.findAllByStatusIsTrue(
                    PageRequest.of(1, 1)
            )).willReturn(List.of(product));
            List<Product> allProducts = productService.findAll(1, 1);
            assertThat(allProducts.size()).isGreaterThan(0);
            assertThat(allProducts.get(0).getProductName()).isEqualTo("Camisa");
        }
        @Test
        @DisplayName("Find a Product By Id with Existing Product Id")
        void givenAProductId_whenFindProductById_thenReturnProductFound() {
            given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

            Product productFound = productService.findById(product.getId());

            assertThat(productFound).isNotNull();
            assertThat(productFound.getProductName()).isEqualTo("Camisa");
        }

        @Test
        @DisplayName("Create a Product with Non Existing Product Name")
        void givenAProduct_whenCreateANewProductWithValidData_thenReturnProductNotNull() {
            given(productRepository.save(product)).willReturn(product);
            given(productHelper.productFromDto(productDto)).willReturn(product);
            Product newProduct = productService.createProduct(productDto);
            assertThat(newProduct).isNotNull();
        }

        @Test
        @DisplayName("Update a Product with Valid id")
        void givenAProductId_whenUpdateProduct_thenReturnProductUpdated() {
            given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
            given(productRepository.save(product)).willReturn(product);

            product.setProductName(productDto.getProductName());

            Product updatedProduct = productService.updateProduct(product.getId(), productDto);
            assertThat(updatedProduct).isNotNull();
            assertThat(updatedProduct.getProductName()).isEqualTo("Updated product name");
        }

        @Test
        @DisplayName("Delete a Product with Valid id")
        void givenAProductId_whenDeleteProduct_thenReturnObjectNull() {
            given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
            given(productRepository.save(product)).willReturn(product);
            given(objectNull.getObjectNull()).willReturn(Collections.emptyList());

            Object removedProduct = productService.deleteProduct(product.getId());

            assertEquals(Collections.emptyList(), removedProduct);
        }
    }

    @Nested
    @DisplayName("Product Service Tests | Throw an Exception")
    class ProductServiceImpTestException {
        @Test
        @DisplayName("FindById a Product with Invalid id and Throws an Exception")
        void givenAProductId_whenFindByIdProduct_thenThrowsAnException() {
            given(productRepository.findById(323L)).willReturn(Optional.empty());

            assertThrows(MyBusinessException.class, () -> {
                productService.findById(323L);
            });
        }
        @Test
        @DisplayName("Create a Product With Existing Name and Throws an Exception")
        void givenAProduct_whenCreateANewProductWithExistingName_thenThrowsAnException() {
            given(productHelper.findByName(productDto.getProductName())).willReturn(true);

            assertThrows(MyBusinessException.class, () -> {
                productService.createProduct(productDto);
            });
        }

        @Test
        @DisplayName("Update a Product with Invalid id and Throws an Exception")
        void givenAProductId_whenUpdateProduct_thenThrowsAnException() {
            given(productRepository.findById(323L)).willReturn(Optional.empty());

            assertThrows(MyBusinessException.class, () -> {
                productService.updateProduct(323L, productDto);
            });
        }

        @Test
        @DisplayName("Delete a Product with Invalid id and Throws an Exception")
        void givenAProductId_whenDeleteProduct_thenThrowsAnException() {
            given(productRepository.findById(323L)).willReturn(Optional.empty());

            assertThrows(MyBusinessException.class, () -> {
                productService.deleteProduct(323L);
            });
        }
    }
}