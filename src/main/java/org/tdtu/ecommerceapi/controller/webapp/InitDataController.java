package org.tdtu.ecommerceapi.controller.webapp;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.AppGroup;
import org.tdtu.ecommerceapi.model.rest.*;
import org.tdtu.ecommerceapi.repository.*;

import java.time.OffsetDateTime;
import java.util.*;

@Tag(name = "Webapp.InitData")
@RestController
@RequestMapping("/v1/webapp/data")
@RequiredArgsConstructor
@Slf4j
public class InitDataController {
    private final MongoOperations mongoOperations;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;
    private final GoogleAccountRepository googleAccountRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PromotionRepository promotionRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @PostMapping("/delete-data")
    public ResponseEntity<?> deleteData() {
        groupRepository.deleteAll();
        accountRepository.deleteAll();
        googleAccountRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        addressRepository.deleteAll();
        cartRepository.deleteAll();
        cartItemRepository.deleteAll();
        promotionRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        log.info("All data has been deleted successfully.");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/init-data")
    public ResponseEntity<?> initData() {
//        groupRepository.deleteAll();
//        accountRepository.deleteAll();
//        productRepository.deleteAll();
//        categoryRepository.deleteAll();
//        productRepository.deleteAll();
//        addressRepository.deleteAll();
//        cartRepository.deleteAll();
//        cartItemRepository.deleteAll();
//        promotionRepository.deleteAll();
//        orderRepository.deleteAll();
//        orderItemRepository.deleteAll();

        // -- Group --
        AppGroup adminGroup = groupRepository.findByName("admin").orElse(new AppGroup());
        if (adminGroup.getId() == null) {
            adminGroup.setName("admin");
            adminGroup = groupRepository.save(adminGroup);
        }

        AppGroup sellerGroup = groupRepository.findByName("seller").orElse(new AppGroup());
        if (sellerGroup.getId() == null) {
            sellerGroup.setName("seller");
            sellerGroup = groupRepository.save(sellerGroup);
        }

        AppGroup userGroup = groupRepository.findByName("user").orElse(new AppGroup());
        if (userGroup.getId() == null) {
            userGroup.setName("user");
            userGroup = groupRepository.save(userGroup);
        }

        // -- Account --
        Account admin = accountRepository.findByEmail("admin@gmail.com").orElse(new Account());
        if (admin.getId() == null) {
            admin.setUsername("Alex");
            admin.setEmail("admin@gmail.com");
            admin.setPhoneNumber("0909090909");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setGroup(adminGroup);
            admin = accountRepository.save(admin);
        }

        Account user = accountRepository.findByEmail("user@gmail.com").orElse(new Account());
        if (user.getId() == null) {
            user.setUsername("Rose");
            user.setEmail("user@gmail.com");
            user.setPhoneNumber("0808080808");
            user.setPassword(passwordEncoder.encode("password"));
            user.setGroup(userGroup);
            user = accountRepository.save(user);
        }

        // Business data
        // -- Category --
        Query electronicsQuery = new Query(Criteria.where("categoryName").is("Electronics"));
        Category electronics = mongoOperations.findOne(electronicsQuery, Category.class);
        if (electronics == null) {
            electronics = new Category();
            electronics.setCategoryName("Electronics");
            electronics = categoryRepository.save(electronics);
        }

        Query booksQuery = new Query(Criteria.where("categoryName").is("Books"));
        Category books = mongoOperations.findOne(booksQuery, Category.class);
        if (books == null) {
            books = new Category();
            books.setCategoryName("Books");
            books = categoryRepository.save(books);
        }

        // -- Product --
        // Create products for Electronics category
        Query phoneQuery = new Query(Criteria.where("productName").is("iPhone 16 Pro Max 1TB"));
        Product phone = mongoOperations.findOne(phoneQuery, Product.class);
        if (phone == null) {
            phone = new Product();
            phone.setId(null);
            phone.setProductName("iPhone 16 Pro Max 1TB");
            phone.setImage("iphone.png");
            phone.setDescription("Latest smartphone");
            phone.setQuantity(100);
            phone.setPrice(2000000);
            phone.setCategory(electronics);
            phone = productRepository.save(phone);
        }

        Query laptopQuery = new Query(Criteria.where("productName").is("ThinkPad X1 Carbon Gen 13 Aura Edition"));
        Product laptop = mongoOperations.findOne(laptopQuery, Product.class);
        if (laptop == null) {
            laptop = new Product();
            laptop.setId(null);
            laptop.setProductName("ThinkPad X1 Carbon Gen 13 Aura Edition");
            laptop.setImage("laptop2.png");
            laptop.setDescription("High-performance laptop");
            laptop.setQuantity(50);
            laptop.setPrice(5600000);
            laptop.setCategory(electronics);
            laptop = productRepository.save(laptop);
        }

        Query tabletQuery = new Query(Criteria.where("productName").is("Samsung Galaxy Tab S6"));
        Product tablet = mongoOperations.findOne(tabletQuery, Product.class);
        if (tablet == null) {
            tablet = new Product();
            tablet.setId(null);
            tablet.setProductName("Samsung Galaxy Tab S6");
            tablet.setImage("tablet.png");
            tablet.setDescription("Portable tablet");
            tablet.setQuantity(75);
            tablet.setPrice(1560000);
            tablet.setCategory(electronics);
            tablet = productRepository.save(tablet);
        }

        // Create product for Books category
        Query bookQuery = new Query(Criteria.where("productName").is("Programming Book"));
        Product book = mongoOperations.findOne(bookQuery, Product.class);
        if (book == null) {
            book = new Product();
            book.setId(null);
            book.setProductName("Programming Book");
            book.setImage("book.png");
            book.setDescription("Learn programming");
            book.setQuantity(200);
            book.setPrice(65000);
            book.setCategory(books);
            book = productRepository.save(book);
        }

        // Create product for Electronics category
        Query smartwatchQuery = new Query(Criteria.where("productName").is("Apple Smartwatch"));
        Product smartwatch = mongoOperations.findOne(smartwatchQuery, Product.class);
        if (smartwatch == null) {
            smartwatch = new Product();
            smartwatch.setId(null);
            smartwatch.setProductName("Apple Smartwatch");
            smartwatch.setImage("watch.png");
            smartwatch.setDescription("Fitness tracking smartwatch");
            smartwatch.setQuantity(80);
            smartwatch.setPrice(1200000);
            smartwatch.setCategory(electronics);
            smartwatch = productRepository.save(smartwatch);
        }

        // Create product for Electronics category
        Query earbudsQuery = new Query(Criteria.where("productName").is("Wireless Earbuds"));
        Product earbuds = mongoOperations.findOne(earbudsQuery, Product.class);
        if (earbuds == null) {
            earbuds = new Product();
            earbuds.setId(null);
            earbuds.setProductName("Wireless Earbuds");
            earbuds.setImage("earbuds.png");
            earbuds.setDescription("True wireless stereo earbuds");
            earbuds.setQuantity(150);
            earbuds.setPrice(800000);
            earbuds.setCategory(electronics);
            earbuds = productRepository.save(earbuds);
        }

        // Create product for Books category
        Query novelQuery = new Query(Criteria.where("productName").is("MacBook Pro 16 inch M3 Max"));
        Product novel = mongoOperations.findOne(novelQuery, Product.class);
        if (novel == null) {
            novel = new Product();
            novel.setId(null);
            novel.setProductName("MacBook Pro 16 inch M3 Max");
            novel.setImage("mac.png");
            novel.setDescription("Latest MacBook Pro with M3 Max chip");
            novel.setQuantity(300);
            novel.setPrice(24000000);
            novel.setCategory(electronics);
            novel = productRepository.save(novel);
        }

        // Create product for Books category
        Query textbookQuery = new Query(Criteria.where("productName").is("Laptop Lenovo IdeaPad Slim 5"));
        Product textbook = mongoOperations.findOne(textbookQuery, Product.class);
        if (textbook == null) {
            textbook = new Product();
            textbook.setId(null);
            textbook.setProductName("Laptop Lenovo IdeaPad Slim 5");
            textbook.setImage("laptop.png");
            textbook.setDescription("The best laptop for students");
            textbook.setQuantity(100);
            textbook.setPrice(10000000);
            textbook.setCategory(electronics);
            textbook = productRepository.save(textbook);
        }

        // Create product for Electronics category
        Query speakerQuery = new Query(Criteria.where("productName").is("Bluetooth Speaker"));
        Product speaker = mongoOperations.findOne(speakerQuery, Product.class);
        if (speaker == null) {
            speaker = new Product();
            speaker.setId(null);
            speaker.setProductName("Bluetooth Speaker");
            speaker.setImage("speaker.png");
            speaker.setDescription("Portable Bluetooth speaker");
            speaker.setQuantity(60);
            speaker.setPrice(500000);
            speaker.setCategory(electronics);
            speaker = productRepository.save(speaker);
        }

        // -- Addresses --
        // Create addresses for account
        if (admin != null) {
            Address adminAddress1 = new Address();
            adminAddress1.setStreet("1600 Pennsylvania Avenue NW");
            adminAddress1.setBuildingName("The White House");
            adminAddress1.setCity("Washington");
            adminAddress1.setCountry("United States");
            adminAddress1.setState("DC");
            adminAddress1.setPincode("20500");
            adminAddress1.setAccount(admin);

            Address adminAddress2 = new Address();
            adminAddress2.setStreet("10 Downing Street");
            adminAddress2.setBuildingName("Prime Minister's Office");
            adminAddress2.setCity("London");
            adminAddress2.setCountry("United Kingdom");
            adminAddress2.setState("England");
            adminAddress2.setPincode("SW1A 2AA");
            adminAddress2.setAccount(admin);

            mongoOperations.save(adminAddress1);
            mongoOperations.save(adminAddress2);
        }


        Address userAddress1 = new Address();
        if (user != null) {
            userAddress1.setStreet("221B Baker Street");
            userAddress1.setBuildingName("Sherlock Holmes Museum");
            userAddress1.setCity("London");
            userAddress1.setCountry("United Kingdom");
            userAddress1.setState("England");
            userAddress1.setPincode("NW1 6XE");
            userAddress1.setAccount(user);

            Address userAddress2 = new Address();
            userAddress2.setStreet("1 Infinite Loop");
            userAddress2.setBuildingName("Apple Headquarters");
            userAddress2.setCity("Cupertino");
            userAddress2.setCountry("United States");
            userAddress2.setState("California");
            userAddress2.setPincode("95014");
            userAddress2.setAccount(user);

            userAddress1 = mongoOperations.save(userAddress1);
            userAddress2 = mongoOperations.save(userAddress2);
        }

        // -- Carts and CartItems --
        Cart cart = new Cart();
        if (user != null) {
            cart.setAccount(user);
            cart = cartRepository.save(cart);

            List<Product> randomProducts = new ArrayList<>();
            randomProducts.add(laptop);
            randomProducts.add(tablet);
            randomProducts.add(book);

            for (Product product : randomProducts) {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(cart);
                cartItem.setProductPrice(product.getPrice());
                cartItem.setQuantity(new Random().nextInt(9) + 3);
                cartItemRepository.save(cartItem);
            }
        }

        // -- Promotions --
        // Create ALL_PRODUCTS promotion
        Promotion allProductsPromotion = new Promotion();
        allProductsPromotion.setPromotionName("10% Off All Products");
        allProductsPromotion.setPromotionCode("ALL10");
        allProductsPromotion.setDescription("Get 10% off on all products.");
        allProductsPromotion.setStartDate(OffsetDateTime.now());
        allProductsPromotion.setEndDate(OffsetDateTime.now().plusDays(30));
        allProductsPromotion.setDiscountAmount(10.0);
        allProductsPromotion.setPromotionType(PromotionType.ALL_PRODUCTS);
        allProductsPromotion.setProportionType(ProportionType.PERCENTAGE);
        allProductsPromotion.setMinOrderValue(0.0);
        allProductsPromotion = promotionRepository.save(allProductsPromotion);

        Promotion allProductsPromotion20 = new Promotion();
        allProductsPromotion20.setPromotionName("20% Off All Products");
        allProductsPromotion20.setPromotionCode("ALL20");
        allProductsPromotion20.setDescription("Get 20% off on all products.");
        allProductsPromotion20.setStartDate(OffsetDateTime.now());
        allProductsPromotion20.setEndDate(OffsetDateTime.now().plusDays(30));
        allProductsPromotion20.setDiscountAmount(20.0);
        allProductsPromotion20.setPromotionType(PromotionType.ALL_PRODUCTS);
        allProductsPromotion20.setProportionType(ProportionType.PERCENTAGE);
        allProductsPromotion20.setMinOrderValue(0.0);
        allProductsPromotion20 = promotionRepository.save(allProductsPromotion20);

        // Create SPECIFIC_PRODUCTS promotion
        Promotion specificProductsPromotion = new Promotion();
        specificProductsPromotion.setPromotionName("20% Off Specific Products");
        specificProductsPromotion.setPromotionCode("SPECIFIC20");
        specificProductsPromotion.setDescription("Get 20% off on selected products.");
        specificProductsPromotion.setStartDate(OffsetDateTime.now());
        specificProductsPromotion.setEndDate(OffsetDateTime.now().plusDays(30));
        specificProductsPromotion.setDiscountAmount(20.0);
        specificProductsPromotion.setPromotionType(PromotionType.SPECIFIC_PRODUCTS);
        specificProductsPromotion.setProportionType(ProportionType.PERCENTAGE);
        specificProductsPromotion.setMinOrderValue(0.0);
        specificProductsPromotion.setProductIds(new HashSet<>(Arrays.asList(laptop.getId(), book.getId())));
        specificProductsPromotion = promotionRepository.save(specificProductsPromotion);

        // Create ORDER_TOTAL promotion
        Promotion orderTotalPromotion = new Promotion();
        orderTotalPromotion.setPromotionName("1000 Off Orders Over 500");
        orderTotalPromotion.setPromotionCode("ORDER1000");
        orderTotalPromotion.setDescription("Get 1000 off on orders over 500.");
        orderTotalPromotion.setStartDate(OffsetDateTime.now());
        orderTotalPromotion.setEndDate(OffsetDateTime.now().plusDays(30));
        orderTotalPromotion.setDiscountAmount(1000.0);
        orderTotalPromotion.setPromotionType(PromotionType.ORDER_TOTAL);
        orderTotalPromotion.setProportionType(ProportionType.ABSOLUTE);
        orderTotalPromotion.setMinOrderValue(500.0);
        orderTotalPromotion = promotionRepository.save(orderTotalPromotion);

        // -- Logging --
        log.info("Cart ID: " + "\"" + cart.getId().toString() + "\"");
        log.info("User/Address ID: " + "\"" + userAddress1.getId().toString() + "\"");
        log.info("Promotion ALL_PRODUCTS 10: " + "\"" + allProductsPromotion.getId().toString() + "\"");
        log.info("Promotion ALL_PRODUCTS 20: " + "\"" + allProductsPromotion20.getId().toString() + "\"");
        log.info("Promotion SPECIFIC_PRODUCTS: " + "\"" + specificProductsPromotion.getId().toString() + "\"");
        log.info("Promotion ORDER_TOTAL: " + "\"" + orderTotalPromotion.getId().toString() + "\"");
        // -- Logging --
        log.info("{\"cartId\": \"" + cart.getId().toString() + "\", " +
                "\"addressId\": \"" + userAddress1.getId().toString() + "\", " +
                "\"promotionIds\": [\"" + allProductsPromotion.getId().toString() + "\", " +
                "\"" + allProductsPromotion20.getId().toString() + "\", " +
                "\"" + specificProductsPromotion.getId().toString() + "\", " +
                "\"" + orderTotalPromotion.getId().toString() + "\"], " +
                "\"shipCOD\": false}");

        // -- New Data --
        // -- Category --
        Query notebookQuery = new Query(Criteria.where("categoryName").is("Laptop"));
        Category notebook = mongoOperations.findOne(notebookQuery, Category.class);
        if (notebook == null) {
            notebook = new Category();
            notebook.setCategoryName("Laptop");
            notebook = categoryRepository.save(notebook);
        }

        Query taiNgheQuery = new Query(Criteria.where("categoryName").is("Tai nghe"));
        Category taiNghe = mongoOperations.findOne(taiNgheQuery, Category.class);
        if (taiNghe == null) {
            taiNghe = new Category();
            taiNghe.setCategoryName("Tai nghe");
            taiNghe = categoryRepository.save(taiNghe);
        }

        Query manHinhQuery = new Query(Criteria.where("categoryName").is("Màn hình"));
        Category manHinh = mongoOperations.findOne(manHinhQuery, Category.class);
        if (manHinh == null) {
            manHinh = new Category();
            manHinh.setCategoryName("Màn hình");
            manHinh = categoryRepository.save(manHinh);
        }

        Query iphoneQuery = new Query(Criteria.where("categoryName").is("Iphone"));
        Category iphone = mongoOperations.findOne(iphoneQuery, Category.class);
        if (iphone == null) {
            iphone = new Category();
            iphone.setCategoryName("Iphone");
            iphone = categoryRepository.save(iphone);
        }

        Query linhKienQuery = new Query(Criteria.where("categoryName").is("Linh kiện máy tính"));
        Category linhKien = mongoOperations.findOne(linhKienQuery, Category.class);
        if (linhKien == null) {
            linhKien = new Category();
            linhKien.setCategoryName("Linh kiện máy tính");
            linhKien = categoryRepository.save(linhKien);
        }

// -- Product --
// Create products for Laptop category
        Query asusZenbookQuery = new Query(Criteria.where("productName").is("Asus Zenbook 14 OLED UX3405MA"));
        Product asusZenbook = mongoOperations.findOne(asusZenbookQuery, Product.class);
        if (asusZenbook == null) {
            asusZenbook = new Product();
            asusZenbook.setId(null);
            asusZenbook.setProductName("Asus Zenbook 14 OLED UX3405MA");
            asusZenbook.setImage("asus-zenbook-14-oled.png");
            asusZenbook.setDescription("{\"summary\":\"Laptop mỏng nhẹ cao cấp\",\"description\":\"Asus Zenbook 14 OLED với màn hình sắc nét, hiệu năng mạnh mẽ, thiết kế tinh tế\",\"attribute\":{\"Màn hình\":\"14 inch OLED 2.8K\",\"CPU\":\"Intel Core Ultra 7 155H\",\"RAM\":\"16GB LPDDR5\",\"Ổ cứng\":\"1TB SSD NVMe\",\"Card đồ họa\":\"Intel Arc Graphics\",\"Pin\":\"75Wh\",\"Trọng lượng\":\"1.28kg\",\"Hệ điều hành\":\"Windows 11 Home\"}}");
            asusZenbook.setQuantity(50);
            asusZenbook.setPrice(32990000);
            asusZenbook.setCategory(notebook);
            asusZenbook = productRepository.save(asusZenbook);
        }

        Query macBookAirQuery = new Query(Criteria.where("productName").is("MacBook Air M2 256GB"));
        Product macBookAir = mongoOperations.findOne(macBookAirQuery, Product.class);
        if (macBookAir == null) {
            macBookAir = new Product();
            macBookAir.setId(null);
            macBookAir.setProductName("MacBook Air M2 256GB");
            macBookAir.setImage("macbook-air-m2.png");
            macBookAir.setDescription("{\"summary\":\"Laptop siêu mỏng nhẹ\",\"description\":\"MacBook Air M2 với thiết kế tinh tế, pin lâu, hiệu năng vượt trội\",\"color\":[\"#C0C0C0\",\"#000000\",\"#FFD700\"],\"attribute\":{\"Màn hình\":\"13.6 inch Retina\",\"CPU\":\"Apple M2 8-core\",\"RAM\":\"8GB\",\"Ổ cứng\":\"256GB SSD\",\"Card đồ họa\":\"Apple M2 8-core GPU\",\"Pin\":\"18 giờ\",\"Trọng lượng\":\"1.24kg\",\"Hệ điều hành\":\"macOS Ventura\"}}");
            macBookAir.setQuantity(40);
            macBookAir.setPrice(29990000);
            macBookAir.setCategory(notebook);
            macBookAir = productRepository.save(macBookAir);
        }

        Query lenovoLegionQuery = new Query(Criteria.where("productName").is("Lenovo Legion 5 Pro 16IAH7H"));
        Product lenovoLegion = mongoOperations.findOne(lenovoLegionQuery, Product.class);
        if (lenovoLegion == null) {
            lenovoLegion = new Product();
            lenovoLegion.setId(null);
            lenovoLegion.setProductName("Lenovo Legion 5 Pro 16IAH7H");
            lenovoLegion.setImage("lenovo-legion-5-pro.png");
            lenovoLegion.setDescription("{\"summary\":\"Laptop gaming mạnh mẽ\",\"description\":\"Lenovo Legion 5 Pro với card đồ họa RTX, màn hình 2K, lý tưởng cho gaming\",\"attribute\":{\"Màn hình\":\"16 inch WQXGA 165Hz\",\"CPU\":\"Intel Core i7-12700H\",\"RAM\":\"16GB DDR4\",\"Ổ cứng\":\"1TB SSD NVMe\",\"Card đồ họa\":\"NVIDIA RTX 3060 6GB\",\"Pin\":\"80Wh\",\"Trọng lượng\":\"2.49kg\",\"Hệ điều hành\":\"Windows 11 Home\"}}");
            lenovoLegion.setQuantity(30);
            lenovoLegion.setPrice(38990000);
            lenovoLegion.setCategory(notebook);
            lenovoLegion = productRepository.save(lenovoLegion);
        }

        Query dellXpsQuery = new Query(Criteria.where("productName").is("Dell XPS 13 9315"));
        Product dellXps = mongoOperations.findOne(dellXpsQuery, Product.class);
        if (dellXps == null) {
            dellXps = new Product();
            dellXps.setId(null);
            dellXps.setProductName("Dell XPS 13 9315");
            dellXps.setImage("dell-xps-13.png");
            dellXps.setDescription("{\"summary\":\"Laptop doanh nhân cao cấp\",\"description\":\"Dell XPS 13 với thiết kế siêu mỏng, màn hình 4K, hiệu năng ổn định\",\"attribute\":{\"Màn hình\":\"13.4 inch 4K UHD+\",\"CPU\":\"Intel Core i7-1250U\",\"RAM\":\"16GB LPDDR5\",\"Ổ cứng\":\"512GB SSD NVMe\",\"Card đồ họa\":\"Intel Iris Xe\",\"Pin\":\"55Wh\",\"Trọng lượng\":\"1.17kg\",\"Hệ điều hành\":\"Windows 11 Pro\"}}");
            dellXps.setQuantity(25);
            dellXps.setPrice(35990000);
            dellXps.setCategory(notebook);
            dellXps = productRepository.save(dellXps);
        }

        Query hpPavilionQuery = new Query(Criteria.where("productName").is("HP Pavilion Aero 13"));
        Product hpPavilion = mongoOperations.findOne(hpPavilionQuery, Product.class);
        if (hpPavilion == null) {
            hpPavilion = new Product();
            hpPavilion.setId(null);
            hpPavilion.setProductName("HP Pavilion Aero 13");
            hpPavilion.setImage("hp-pavilion-aero-13.png");
            hpPavilion.setDescription("{\"summary\":\"Laptop văn phòng nhẹ\",\"description\":\"HP Pavilion Aero 13 với trọng lượng siêu nhẹ, phù hợp làm việc di động\",\"attribute\":{\"Màn hình\":\"13.3 inch WUXGA\",\"CPU\":\"AMD Ryzen 5 5600U\",\"RAM\":\"8GB DDR4\",\"Ổ cứng\":\"512GB SSD NVMe\",\"Card đồ họa\":\"AMD Radeon Graphics\",\"Pin\":\"43Wh\",\"Trọng lượng\":\"0.97kg\",\"Hệ điều hành\":\"Windows 11 Home\"}}");
            hpPavilion.setQuantity(60);
            hpPavilion.setPrice(19990000);
            hpPavilion.setCategory(notebook);
            hpPavilion = productRepository.save(hpPavilion);
        }

        Query acerAspireQuery = new Query(Criteria.where("productName").is("Acer Aspire 5 A515-58M"));
        Product acerAspire = mongoOperations.findOne(acerAspireQuery, Product.class);
        if (acerAspire == null) {
            acerAspire = new Product();
            acerAspire.setId(null);
            acerAspire.setProductName("Acer Aspire 5 A515-58M");
            acerAspire.setImage("acer-aspire-5.png");
            acerAspire.setDescription("{\"summary\":\"Laptop đa dụng giá tốt\",\"description\":\"Acer Aspire 5 với hiệu năng ổn, phù hợp học tập và làm việc\",\"attribute\":{\"Màn hình\":\"15.6 inch Full HD\",\"CPU\":\"Intel Core i5-1335U\",\"RAM\":\"8GB DDR4\",\"Ổ cứng\":\"512GB SSD NVMe\",\"Card đồ họa\":\"Intel Iris Xe\",\"Pin\":\"50Wh\",\"Trọng lượng\":\"1.78kg\",\"Hệ điều hành\":\"Windows 11 Home\"}}");
            acerAspire.setQuantity(70);
            acerAspire.setPrice(15990000);
            acerAspire.setCategory(notebook);
            acerAspire = productRepository.save(acerAspire);
        }

        Query msiKatanaQuery = new Query(Criteria.where("productName").is("MSI Katana GF66"));
        Product msiKatana = mongoOperations.findOne(msiKatanaQuery, Product.class);
        if (msiKatana == null) {
            msiKatana = new Product();
            msiKatana.setId(null);
            msiKatana.setProductName("MSI Katana GF66");
            msiKatana.setImage("msi-katana-gf66.png");
            msiKatana.setDescription("{\"summary\":\"Laptop gaming tầm trung\",\"description\":\"MSI Katana GF66 với cấu hình mạnh, thiết kế đậm chất gaming\",\"attribute\":{\"Màn hình\":\"15.6 inch Full HD 144Hz\",\"CPU\":\"Intel Core i7-12650H\",\"RAM\":\"16GB DDR4\",\"Ổ cứng\":\"512GB SSD NVMe\",\"Card đồ họa\":\"NVIDIA RTX 3050 4GB\",\"Pin\":\"53.5Wh\",\"Trọng lượng\":\"2.25kg\",\"Hệ điều hành\":\"Windows 11 Home\"}}");
            msiKatana.setQuantity(45);
            msiKatana.setPrice(26990000);
            msiKatana.setCategory(notebook);
            msiKatana = productRepository.save(msiKatana);
        }

        Query asusTufQuery = new Query(Criteria.where("productName").is("Asus TUF Gaming A15"));
        Product asusTuf = mongoOperations.findOne(asusTufQuery, Product.class);
        if (asusTuf == null) {
            asusTuf = new Product();
            asusTuf.setId(null);
            asusTuf.setProductName("Asus TUF Gaming A15");
            asusTuf.setImage("asus-tuf-gaming-a15.png");
            asusTuf.setDescription("{\"summary\":\"Laptop gaming bền bỉ\",\"description\":\"Asus TUF Gaming A15 với độ bền chuẩn quân đội, hiệu năng cao\",\"attribute\":{\"Màn hình\":\"15.6 inch Full HD 144Hz\",\"CPU\":\"AMD Ryzen 7 7735HS\",\"RAM\":\"16GB DDR5\",\"Ổ cứng\":\"1TB SSD NVMe\",\"Card đồ họa\":\"NVIDIA RTX 4060 8GB\",\"Pin\":\"90Wh\",\"Trọng lượng\":\"2.2kg\",\"Hệ điều hành\":\"Windows 11 Home\"}}");
            asusTuf.setQuantity(35);
            asusTuf.setPrice(31990000);
            asusTuf.setCategory(notebook);
            asusTuf = productRepository.save(asusTuf);
        }

// Create products for Tai nghe category
        Query sonyWhQuery = new Query(Criteria.where("productName").is("Sony WH-1000XM5"));
        Product sonyWh = mongoOperations.findOne(sonyWhQuery, Product.class);
        if (sonyWh == null) {
            sonyWh = new Product();
            sonyWh.setId(null);
            sonyWh.setProductName("Sony WH-1000XM5");
            sonyWh.setImage("sony-wh-1000xm5.png");
            sonyWh.setDescription("{\"summary\":\"Tai nghe không dây cao cấp\",\"description\":\"Sony WH-1000XM5 với chống ồn vượt trội, âm thanh Hi-Res, pin lâu\",\"attribute\":{\"Kết nối\":\"Bluetooth 5.2\",\"Chống ồn\":\"Có\",\"Thời gian pin\":\"30 giờ\",\"Trở kháng\":\"48 Ohm\",\"Độ nhạy\":\"102dB\",\"Tần số đáp ứng\":\"4Hz-40kHz\",\"Micro\":\"Có, 4 mic\",\"Trọng lượng\":\"250g\"}}");
            sonyWh.setQuantity(80);
            sonyWh.setPrice(8490000);
            sonyWh.setCategory(taiNghe);
            sonyWh = productRepository.save(sonyWh);
        }

        Query airPodsProQuery = new Query(Criteria.where("productName").is("AirPods Pro 2"));
        Product airPodsPro = mongoOperations.findOne(airPodsProQuery, Product.class);
        if (airPodsPro == null) {
            airPodsPro = new Product();
            airPodsPro.setId(null);
            airPodsPro.setProductName("AirPods Pro 2");
            airPodsPro.setImage("airpods-pro-2.png");
            airPodsPro.setDescription("{\"summary\":\"Tai nghe không dây Apple\",\"description\":\"AirPods Pro 2 với chất âm sống động, chống ồn chủ động, tích hợp sạc USB-C\",\"color\":[\"#FFFFFF\"],\"attribute\":{\"Kết nối\":\"Bluetooth 5.3\",\"Chống ồn\":\"Có\",\"Thời gian pin\":\"6 giờ (30 giờ với hộp sạc)\",\"Trở kháng\":\"N/A\",\"Độ nhạy\":\"N/A\",\"Tần số đáp ứng\":\"20Hz-20kHz\",\"Micro\":\"Có, 2 mic\",\"Trọng lượng\":\"5.3g (mỗi tai nghe)\"}}");
            airPodsPro.setQuantity(100);
            airPodsPro.setPrice(6490000);
            airPodsPro.setCategory(taiNghe);
            airPodsPro = productRepository.save(airPodsPro);
        }

        Query razerBlackSharkQuery = new Query(Criteria.where("productName").is("Razer BlackShark V2"));
        Product razerBlackShark = mongoOperations.findOne(razerBlackSharkQuery, Product.class);
        if (razerBlackShark == null) {
            razerBlackShark = new Product();
            razerBlackShark.setId(null);
            razerBlackShark.setProductName("Razer BlackShark V2");
            razerBlackShark.setImage("razer-blackshark-v2.png");
            razerBlackShark.setDescription("{\"summary\":\"Tai nghe gaming chất lượng\",\"description\":\"Razer BlackShark V2 với âm thanh vòm 7.1, mic rõ nét, thoải mái khi sử dụng\",\"attribute\":{\"Kết nối\":\"USB/3.5mm\",\"Chống ồn\":\"Không\",\"Thời gian pin\":\"N/A\",\"Trở kháng\":\"32 Ohm\",\"Độ nhạy\":\"100dB\",\"Tần số đáp ứng\":\"12Hz-28kHz\",\"Micro\":\"Có, tháo rời\",\"Trọng lượng\":\"262g\"}}");
            razerBlackShark.setQuantity(60);
            razerBlackShark.setPrice(2490000);
            razerBlackShark.setCategory(taiNghe);
            razerBlackShark = productRepository.save(razerBlackShark);
        }

        Query logitechGProQuery = new Query(Criteria.where("productName").is("Logitech G Pro X"));
        Product logitechGPro = mongoOperations.findOne(logitechGProQuery, Product.class);
        if (logitechGPro == null) {
            logitechGPro = new Product();
            logitechGPro.setId(null);
            logitechGPro.setProductName("Logitech G Pro X");
            logitechGPro.setImage("logitech-g-pro-x.png");
            logitechGPro.setDescription("{\"summary\":\"Tai nghe gaming chuyên nghiệp\",\"description\":\"Logitech G Pro X với âm thanh vòm, mic Blue VO!CE, thiết kế bền bỉ\",\"attribute\":{\"Kết nối\":\"USB/3.5mm\",\"Chống ồn\":\"Không\",\"Thời gian pin\":\"N/A\",\"Trở kháng\":\"35 Ohm\",\"Độ nhạy\":\"91.7dB\",\"Tần số đáp ứng\":\"20Hz-20kHz\",\"Micro\":\"Có, tháo rời\",\"Trọng lượng\":\"320g\"}}");
            logitechGPro.setQuantity(50);
            logitechGPro.setPrice(3290000);
            logitechGPro.setCategory(taiNghe);
            logitechGPro = productRepository.save(logitechGPro);
        }

        Query jblTuneQuery = new Query(Criteria.where("productName").is("JBL Tune 510BT"));
        Product jblTune = mongoOperations.findOne(jblTuneQuery, Product.class);
        if (jblTune == null) {
            jblTune = new Product();
            jblTune.setId(null);
            jblTune.setProductName("JBL Tune 510BT");
            jblTune.setImage("jbl-tune-510bt.png");
            jblTune.setDescription("{\"summary\":\"Tai nghe Bluetooth giá tốt\",\"description\":\"JBL Tune 510BT với âm bass mạnh, pin lâu, thiết kế gọn nhẹ\",\"color\":[\"#000000\",\"#FFFFFF\"],\"attribute\":{\"Kết nối\":\"Bluetooth 5.0\",\"Chống ồn\":\"Không\",\"Thời gian pin\":\"40 giờ\",\"Trở kháng\":\"32 Ohm\",\"Độ nhạy\":\"103.5dB\",\"Tần số đáp ứng\":\"20Hz-20kHz\",\"Micro\":\"Có\",\"Trọng lượng\":\"160g\"}}");
            jblTune.setQuantity(90);
            jblTune.setPrice(1490000);
            jblTune.setCategory(taiNghe);
            jblTune = productRepository.save(jblTune);
        }

        Query corsairHsQuery = new Query(Criteria.where("productName").is("Corsair HS80 RGB"));
        Product corsairHs = mongoOperations.findOne(corsairHsQuery, Product.class);
        if (corsairHs == null) {
            corsairHs = new Product();
            corsairHs.setId(null);
            corsairHs.setProductName("Corsair HS80 RGB");
            corsairHs.setImage("corsair-hs80-rgb.png");
            corsairHs.setDescription("{\"summary\":\"Tai nghe gaming không dây\",\"description\":\"Corsair HS80 RGB với âm thanh Dolby Atmos, thiết kế cao cấp, pin lâu\",\"attribute\":{\"Kết nối\":\"USB/2.4GHz\",\"Chống ồn\":\"Không\",\"Thời gian pin\":\"20 giờ\",\"Trở kháng\":\"32 Ohm\",\"Độ nhạy\":\"116dB\",\"Tần số đáp ứng\":\"20Hz-40kHz\",\"Micro\":\"Có, omnidirectional\",\"Trọng lượng\":\"370g\"}}");
            corsairHs.setQuantity(40);
            corsairHs.setPrice(3990000);
            corsairHs.setCategory(taiNghe);
            corsairHs = productRepository.save(corsairHs);
        }

        Query sonyWfQuery = new Query(Criteria.where("productName").is("Sony WF-1000XM5"));
        Product sonyWf = mongoOperations.findOne(sonyWfQuery, Product.class);
        if (sonyWf == null) {
            sonyWf = new Product();
            sonyWf.setId(null);
            sonyWf.setProductName("Sony WF-1000XM5");
            sonyWf.setImage("sony-wf-1000xm5.png");
            sonyWf.setDescription("{\"summary\":\"Tai nghe true wireless cao cấp\",\"description\":\"Sony WF-1000XM5 với chống ồn tốt, chất âm đỉnh cao, nhỏ gọn\",\"color\":[\"#000000\",\"#FFFFFF\"],\"attribute\":{\"Kết nối\":\"Bluetooth 5.3\",\"Chống ồn\":\"Có\",\"Thời gian pin\":\"8 giờ (24 giờ với hộp sạc)\",\"Trở kháng\":\"N/A\",\"Độ nhạy\":\"N/A\",\"Tần số đáp ứng\":\"20Hz-40kHz\",\"Micro\":\"Có, 3 mic\",\"Trọng lượng\":\"5.9g (mỗi tai nghe)\"}}");
            sonyWf.setQuantity(70);
            sonyWf.setPrice(6490000);
            sonyWf.setCategory(taiNghe);
            sonyWf = productRepository.save(sonyWf);
        }

// Create products for Màn hình category
        Query asusProArtQuery = new Query(Criteria.where("productName").is("Asus ProArt PA278CV"));
        Product asusProArt = mongoOperations.findOne(asusProArtQuery, Product.class);
        if (asusProArt == null) {
            asusProArt = new Product();
            asusProArt.setId(null);
            asusProArt.setProductName("Asus ProArt PA278CV");
            asusProArt.setImage("asus-proart-pa278cv.png");
            asusProArt.setDescription("{\"summary\":\"Màn hình đồ họa chuyên nghiệp\",\"description\":\"Asus ProArt PA278CV với màu sắc chính xác, độ phân giải 2K, lý tưởng cho thiết kế\",\"attribute\":{\"Màn hình\":\"27 inch\",\"Độ phân giải\":\"2560x1440 (2K)\",\"Tấm nền\":\"IPS\",\"Tần số quét\":\"75Hz\",\"Độ sáng\":\"350 nits\",\"Tỷ lệ tương phản\":\"1000:1\",\"Thời gian phản hồi\":\"5ms\",\"Cổng kết nối\":\"DisplayPort, HDMI, USB-C\"}}");
            asusProArt.setQuantity(30);
            asusProArt.setPrice(10990000);
            asusProArt.setCategory(manHinh);
            asusProArt = productRepository.save(asusProArt);
        }

        Query lgUltraGearQuery = new Query(Criteria.where("productName").is("LG UltraGear 27GP850"));
        Product lgUltraGear = mongoOperations.findOne(lgUltraGearQuery, Product.class);
        if (lgUltraGear == null) {
            lgUltraGear = new Product();
            lgUltraGear.setId(null);
            lgUltraGear.setProductName("LG UltraGear 27GP850");
            lgUltraGear.setImage("lg-ultragear-27gp850.png");
            lgUltraGear.setDescription("{\"summary\":\"Màn hình gaming cao cấp\",\"description\":\"LG UltraGear 27GP850 với tần số quét cao, hỗ trợ HDR, màu sắc sống động\",\"attribute\":{\"Màn hình\":\"27 inch\",\"Độ phân giải\":\"2560x1440 (2K)\",\"Tấm nền\":\"IPS\",\"Tần số quét\":\"165Hz\",\"Độ sáng\":\"400 nits\",\"Tỷ lệ tương phản\":\"1000:1\",\"Thời gian phản hồi\":\"1ms\",\"Cổng kết nối\":\"DisplayPort, HDMI, USB\"}}");
            lgUltraGear.setQuantity(25);
            lgUltraGear.setPrice(11990000);
            lgUltraGear.setCategory(manHinh);
            lgUltraGear = productRepository.save(lgUltraGear);
        }

        Query dellUltraSharpQuery = new Query(Criteria.where("productName").is("Dell UltraSharp U2723QE"));
        Product dellUltraSharp = mongoOperations.findOne(dellUltraSharpQuery, Product.class);
        if (dellUltraSharp == null) {
            dellUltraSharp = new Product();
            dellUltraSharp.setId(null);
            dellUltraSharp.setProductName("Dell UltraSharp U2723QE");
            dellUltraSharp.setImage("dell-ultrasharp-u2723qe.png");
            dellUltraSharp.setDescription("{\"summary\":\"Màn hình 4K văn phòng\",\"description\":\"Dell UltraSharp U2723QE với độ phân giải 4K, màu sắc trung thực, đa dạng cổng kết nối\",\"attribute\":{\"Màn hình\":\"27 inch\",\"Độ phân giải\":\"3840x2160 (4K)\",\"Tấm nền\":\"IPS\",\"Tần số quét\":\"60Hz\",\"Độ sáng\":\"400 nits\",\"Tỷ lệ tương phản\":\"2000:1\",\"Thời gian phản hồi\":\"5ms\",\"Cổng kết nối\":\"HDMI, DisplayPort, USB-C, USB-A\"}}");
            dellUltraSharp.setQuantity(35);
            dellUltraSharp.setPrice(14990000);
            dellUltraSharp.setCategory(manHinh);
            dellUltraSharp = productRepository.save(dellUltraSharp);
        }

        Query samsungOdysseyQuery = new Query(Criteria.where("productName").is("Samsung Odyssey G5"));
        Product samsungOdyssey = mongoOperations.findOne(samsungOdysseyQuery, Product.class);
        if (samsungOdyssey == null) {
            samsungOdyssey = new Product();
            samsungOdyssey.setId(null);
            samsungOdyssey.setProductName("Samsung Odyssey G5");
            samsungOdyssey.setImage("samsung-odyssey-g5.png");
            samsungOdyssey.setDescription("{\"summary\":\"Màn hình cong gaming\",\"description\":\"Samsung Odyssey G5 với thiết kế cong, tần số quét cao, trải nghiệm gaming mượt mà\",\"attribute\":{\"Màn hình\":\"32 inch\",\"Độ phân giải\":\"2560x1440 (2K)\",\"Tấm nền\":\"VA\",\"Tần số quét\":\"144Hz\",\"Độ sáng\":\"300 nits\",\"Tỷ lệ tương phản\":\"2500:1\",\"Thời gian phản hồi\":\"1ms\",\"Cổng kết nối\":\"HDMI, DisplayPort\"}}");
            samsungOdyssey.setQuantity(40);
            samsungOdyssey.setPrice(8990000);
            samsungOdyssey.setCategory(manHinh);
            samsungOdyssey = productRepository.save(samsungOdyssey);
        }

        Query acerPredatorQuery = new Query(Criteria.where("productName").is("Acer Predator X27"));
        Product acerPredator = mongoOperations.findOne(acerPredatorQuery, Product.class);
        if (acerPredator == null) {
            acerPredator = new Product();
            acerPredator.setId(null);
            acerPredator.setProductName("Acer Predator X27");
            acerPredator.setImage("acer-predator-x27.png");
            acerPredator.setDescription("{\"summary\":\"Màn hình gaming 4K\",\"description\":\"Acer Predator X27 với tần số quét 144Hz, hỗ trợ HDR, lý tưởng cho gaming cao cấp\",\"attribute\":{\"Màn hình\":\"27 inch\",\"Độ phân giải\":\"3840x2160 (4K)\",\"Tấm nền\":\"IPS\",\"Tần số quét\":\"144Hz\",\"Độ sáng\":\"600 nits\",\"Tỷ lệ tương phản\":\"1000:1\",\"Thời gian phản hồi\":\"4ms\",\"Cổng kết nối\":\"HDMI, DisplayPort, USB\"}}");
            acerPredator.setQuantity(20);
            acerPredator.setPrice(19990000);
            acerPredator.setCategory(manHinh);
            acerPredator = productRepository.save(acerPredator);
        }

        Query benqPdQuery = new Query(Criteria.where("productName").is("BenQ PD2705Q"));
        Product benqPd = mongoOperations.findOne(benqPdQuery, Product.class);
        if (benqPd == null) {
            benqPd = new Product();
            benqPd.setId(null);
            benqPd.setProductName("BenQ PD2705Q");
            benqPd.setImage("benq-pd2705q.png");
            benqPd.setDescription("{\"summary\":\"Màn hình thiết kế đồ họa\",\"description\":\"BenQ PD2705Q với độ phủ màu 100% sRGB, lý tưởng cho thiết kế và chỉnh sửa ảnh\",\"attribute\":{\"Màn hình\":\"27 inch\",\"Đ climates phân giải\":\"2560x1440 (2K)\",\"Tấm nền\":\"IPS\",\"Tần số quét\":\"60Hz\",\"Độ sáng\":\"300 nits\",\"Tỷ lệ tương phản\":\"1000:1\",\"Thời gian phản hồi\":\"5ms\",\"Cổng kết nối\":\"HDMI, DisplayPort, USB-C\"}}");
            benqPd.setQuantity(30);
            benqPd.setPrice(10490000);
            benqPd.setCategory(manHinh);
            benqPd = productRepository.save(benqPd);
        }

        Query msiOptixQuery = new Query(Criteria.where("productName").is("MSI Optix MAG274QRF"));
        Product msiOptix = mongoOperations.findOne(msiOptixQuery, Product.class);
        if (msiOptix == null) {
            msiOptix = new Product();
            msiOptix.setId(null);
            msiOptix.setProductName("MSI Optix MAG274QRF");
            msiOptix.setImage("msi-optix-mag274qrf.png");
            msiOptix.setDescription("{\"summary\":\"Màn hình gaming giá tốt\",\"description\":\"MSI Optix MAG274QRF với tần số quét 165Hz, màu sắc sống động, phù hợp gaming\",\"attribute\":{\"Màn hình\":\"27 inch\",\"Độ phân giải\":\"2560x1440 (2K)\",\"Tấm nền\":\"IPS\",\"Tần số quét\":\"165Hz\",\"Độ sáng\":\"300 nits\",\"Tỷ lệ tương phản\":\"1000:1\",\"Thời gian phản hồi\":\"1ms\",\"Cổng kết nối\":\"HDMI, DisplayPort, USB-C\"}}");
            msiOptix.setQuantity(45);
            msiOptix.setPrice(9490000);
            msiOptix.setCategory(manHinh);
            msiOptix = productRepository.save(msiOptix);
        }

        Query viewSonicQuery = new Query(Criteria.where("productName").is("ViewSonic VX3218-PC"));
        Product viewSonic = mongoOperations.findOne(viewSonicQuery, Product.class);
        if (viewSonic == null) {
            viewSonic = new Product();
            viewSonic.setId(null);
            viewSonic.setProductName("ViewSonic VX3218-PC");
            viewSonic.setImage("viewsonic-vx3218-pc.png");
            viewSonic.setDescription("{\"summary\":\"Màn hình cong giá rẻ\",\"description\":\"ViewSonic VX3218-PC với thiết kế cong, phù hợp gaming và giải trí\",\"attribute\":{\"Màn hình\":\"32 inch\",\"Độ phân giải\":\"1920x1080 (Full HD)\",\"Tấm nền\":\"VA\",\"Tần số quét\":\"165Hz\",\"Độ sáng\":\"250 nits\",\"Tỷ lệ tương phản\":\"3000:1\",\"Thời gian phản hồi\":\"1ms\",\"Cổng kết nối\":\"HDMI, DisplayPort\"}}");
            viewSonic.setQuantity(50);
            viewSonic.setPrice(6490000);
            viewSonic.setCategory(manHinh);
            viewSonic = productRepository.save(viewSonic);
        }

// Create products for Iphone category
        Query iphone15_128Query = new Query(Criteria.where("productName").is("iPhone 15 128GB"));
        Product iphone15_128 = mongoOperations.findOne(iphone15_128Query, Product.class);
        if (iphone15_128 == null) {
            iphone15_128 = new Product();
            iphone15_128.setId(null);
            iphone15_128.setProductName("iPhone 15 128GB");
            iphone15_128.setImage("iphone-15-128gb.png");
            iphone15_128.setDescription("{\"summary\":\"Điện thoại iPhone 15 cao cấp\",\"description\":\"iPhone 15 với chip A16 Bionic, camera 48MP, thiết kế tinh tế, hỗ trợ USB-C\",\"link_video\":\"https://www.youtube.com/watch?v=izjLMMP8B7s\",\"color\":[\"#000000\",\"#FFFFFF\",\"#FFC1CC\",\"#ADD8E6\",\"#90EE90\"],\"attribute\":{\"Màn hình\":\"6.1 inch Super Retina XDR\",\"Độ mỏng viền\":\"1.5mm\",\"Chất liệu mặt lưng\":\"Kính\",\"Chip\":\"A16 Bionic\",\"Camera\":\"48MP + 12MP\",\"Pin\":\"3349mAh\",\"Bộ nhớ\":\"128GB\",\"Hệ điều hành\":\"iOS 17\",\"Kết nối\":\"5G, USB-C\",\"Chống nước\":\"IP68\"}}");
            iphone15_128.setQuantity(60);
            iphone15_128.setPrice(22990000);
            iphone15_128.setCategory(iphone);
            iphone15_128 = productRepository.save(iphone15_128);
        }

        Query iphone15_256Query = new Query(Criteria.where("productName").is("iPhone 15 256GB"));
        Product iphone15_256 = mongoOperations.findOne(iphone15_256Query, Product.class);
        if (iphone15_256 == null) {
            iphone15_256 = new Product();
            iphone15_256.setId(null);
            iphone15_256.setProductName("iPhone 15 256GB");
            iphone15_256.setImage("iphone-15-256gb.png");
            iphone15_256.setDescription("{\"summary\":\"iPhone 15 với dung lượng lớn\",\"description\":\"iPhone 15 256GB với hiệu năng mạnh mẽ, camera cải tiến, sạc USB-C\",\"link_video\":\"https://www.youtube.com/watch?v=izjLMMP8B7s\",\"color\":[\"#000000\",\"#FFFFFF\",\"#FFC1CC\",\"#ADD8E6\",\"#90EE90\"],\"attribute\":{\"Màn hình\":\"6.1 inch Super Retina XDR\",\"Độ mỏng viền\":\"1.5mm\",\"Chất liệu mặt lưng\":\"Kính\",\"Chip\":\"A16 Bionic\",\"Camera\":\"48MP + 12MP\",\"Pin\":\"3349mAh\",\"Bộ nhớ\":\"256GB\",\"Hệ điều hành\":\"iOS 17\",\"Kết nối\":\"5G, USB-C\",\"Chống nước\":\"IP68\"}}");
            iphone15_256.setQuantity(50);
            iphone15_256.setPrice(25990000);
            iphone15_256.setCategory(iphone);
            iphone15_256 = productRepository.save(iphone15_256);
        }

        Query iphone15_512Query = new Query(Criteria.where("productName").is("iPhone 15 512GB"));
        Product iphone15_512 = mongoOperations.findOne(iphone15_512Query, Product.class);
        if (iphone15_512 == null) {
            iphone15_512 = new Product();
            iphone15_512.setId(null);
            iphone15_512.setProductName("iPhone 15 512GB");
            iphone15_512.setImage("iphone-15-512gb.png");
            iphone15_512.setDescription("{\"summary\":\"iPhone 15 dung lượng cao\",\"description\":\"iPhone 15 512GB với camera 48MP, pin lâu dài, hiệu năng vượt trội\",\"link_video\":\"https://www.youtube.com/watch?v=izjLMMP8B7s\",\"color\":[\"#000000\",\"#FFFFFF\",\"#FFC1CC\",\"#ADD8E6\",\"#90EE90\"],\"attribute\":{\"Màn hình\":\"6.1 inch Super Retina XDR\",\"Độ mỏng viền\":\"1.5mm\",\"Chất liệu mặt lưng\":\"Kính\",\"Chip\":\"A16 Bionic\",\"Camera\":\"48MP + 12MP\",\"Pin\":\"3349mAh\",\"Bộ nhớ\":\"512GB\",\"Hệ điều hành\":\"iOS 17\",\"Kết nối\":\"5G, USB-C\",\"Chống nước\":\"IP68\"}}");
            iphone15_512.setQuantity(40);
            iphone15_512.setPrice(30990000);
            iphone15_512.setCategory(iphone);
            iphone15_512 = productRepository.save(iphone15_512);
        }

        Query iphone15Plus_128Query = new Query(Criteria.where("productName").is("iPhone 15 Plus 128GB"));
        Product iphone15Plus_128 = mongoOperations.findOne(iphone15Plus_128Query, Product.class);
        if (iphone15Plus_128 == null) {
            iphone15Plus_128 = new Product();
            iphone15Plus_128.setId(null);
            iphone15Plus_128.setProductName("iPhone 15 Plus 128GB");
            iphone15Plus_128.setImage("iphone-15-plus-128gb.png");
            iphone15Plus_128.setDescription("{\"summary\":\"iPhone 15 Plus màn hình lớn\",\"description\":\"iPhone 15 Plus với màn hình 6.7 inch, chip A16 Bionic, camera 48MP\",\"link_video\":\"https://www.youtube.com/watch?v=izjLMMP8B7s\",\"color\":[\"#000000\",\"#FFFFFF\",\"#FFC1CC\",\"#ADD8E6\",\"#90EE90\"],\"attribute\":{\"Màn hình\":\"6.7 inch Super Retina XDR\",\"Độ mỏng viền\":\"1.5mm\",\"Chất liệu mặt lưng\":\"Kính\",\"Chip\":\"A16 Bionic\",\"Camera\":\"48MP + 12MP\",\"Pin\":\"4383mAh\",\"Bộ nhớ\":\"128GB\",\"Hệ điều hành\":\"iOS 17\",\"Kết nối\":\"5G, USB-C\",\"Chống nước\":\"IP68\"}}");
            iphone15Plus_128.setQuantity(45);
            iphone15Plus_128.setPrice(25990000);
            iphone15Plus_128.setCategory(iphone);
            iphone15Plus_128 = productRepository.save(iphone15Plus_128);
        }

        Query iphone15Plus_256Query = new Query(Criteria.where("productName").is("iPhone 15 Plus 256GB"));
        Product iphone15Plus_256 = mongoOperations.findOne(iphone15Plus_256Query, Product.class);
        if (iphone15Plus_256 == null) {
            iphone15Plus_256 = new Product();
            iphone15Plus_256.setId(null);
            iphone15Plus_256.setProductName("iPhone 15 Plus 256GB");
            iphone15Plus_256.setImage("iphone-15-plus-256gb.png");
            iphone15Plus_256.setDescription("{\"summary\":\"iPhone 15 Plus dung lượng lớn\",\"description\":\"iPhone 15 Plus 256GB với camera 48MP, pin lâu, thiết kế cao cấp\",\"link_video\":\"https://www.youtube.com/watch?v=izjLMMP8B7s\",\"color\":[\"#000000\",\"#FFFFFF\",\"#FFC1CC\",\"#ADD8E6\",\"#90EE90\"],\"attribute\":{\"Màn hình\":\"6.7 inch Super Retina XDR\",\"Độ mỏng viền\":\"1.5mm\",\"Chất liệu mặt lưng\":\"Kính\",\"Chip\":\"A16 Bionic\",\"Camera\":\"48MP + 12MP\",\"Pin\":\"4383mAh\",\"Bộ nhớ\":\"256GB\",\"Hệ điều hành\":\"iOS 17\",\"Kết nối\":\"5G, USB-C\",\"Chống nước\":\"IP68\"}}");
            iphone15Plus_256.setQuantity(35);
            iphone15Plus_256.setPrice(28990000);
            iphone15Plus_256.setCategory(iphone);
            iphone15Plus_256 = productRepository.save(iphone15Plus_256);
        }

        Query iphone15Pro_256Query = new Query(Criteria.where("productName").is("iPhone 15 Pro 256GB"));
        Product iphone15Pro_256 = mongoOperations.findOne(iphone15Pro_256Query, Product.class);
        if (iphone15Pro_256 == null) {
            iphone15Pro_256 = new Product();
            iphone15Pro_256.setId(null);
            iphone15Pro_256.setProductName("iPhone 15 Pro 256GB");
            iphone15Pro_256.setImage("iphone-15-pro-256gb.png");
            iphone15Pro_256.setDescription("{\"summary\":\"iPhone 15 Pro cao cấp\",\"description\":\"iPhone 15 Pro với chip A17 Pro, khung titan, camera 48MP cải tiến\",\"link_video\":\"https://www.youtube.com/watch?v=izjLMMP8B7s\",\"color\":[\"#000000\",\"#FFFFFF\",\"#00008B\",\"#DAA520\"],\"attribute\":{\"Màn hình\":\"6.1 inch Super Retina XDR\",\"Độ mỏng viền\":\"1.2mm\",\"Chất liệu mặt lưng\":\"Kính\",\"Chip\":\"A17 Pro\",\"Camera\":\"48MP + 12MP + 12MP\",\"Pin\":\"3274mAh\",\"Bộ nhớ\":\"256GB\",\"Hệ điều hành\":\"iOS 17\",\"Kết nối\":\"5G, USB-C\",\"Chống nước\":\"IP68\"}}");
            iphone15Pro_256.setQuantity(30);
            iphone15Pro_256.setPrice(31990000);
            iphone15Pro_256.setCategory(iphone);
            iphone15Pro_256 = productRepository.save(iphone15Pro_256);
        }

        Query iphone15ProMax_256Query = new Query(Criteria.where("productName").is("iPhone 15 Pro Max 256GB"));
        Product iphone15ProMax_256 = mongoOperations.findOne(iphone15ProMax_256Query, Product.class);
        if (iphone15ProMax_256 == null) {
            iphone15ProMax_256 = new Product();
            iphone15ProMax_256.setId(null);
            iphone15ProMax_256.setProductName("iPhone 15 Pro Max 256GB");
            iphone15ProMax_256.setImage("iphone-15-pro-max-256gb.png");
            iphone15ProMax_256.setDescription("{\"summary\":\"iPhone 15 Pro Max đỉnh cao\",\"description\":\"iPhone 15 Pro Max với chip A17 Pro, camera zoom 5x, khung titan\",\"link_video\":\"https://www.youtube.com/watch?v=izjLMMP8B7s\",\"color\":[\"#000000\",\"#FFFFFF\",\"#00008B\",\"#DAA520\"],\"attribute\":{\"Màn hình\":\"6.7 inch Super Retina XDR\",\"Độ mỏng viền\":\"1.2mm\",\"Chất liệu mặt lưng\":\"Kính\",\"Chip\":\"A17 Pro\",\"Camera\":\"48MP + 12MP + 12MP\",\"Pin\":\"4422mAh\",\"Bộ nhớ\":\"256GB\",\"Hệ điều hành\":\"iOS 17\",\"Kết nối\":\"5G, USB-C\",\"Chống nước\":\"IP68\"}}");
            iphone15ProMax_256.setQuantity(25);
            iphone15ProMax_256.setPrice(34990000);
            iphone15ProMax_256.setCategory(iphone);
            iphone15ProMax_256 = productRepository.save(iphone15ProMax_256);
        }

        Query iphone15ProMax_512Query = new Query(Criteria.where("productName").is("iPhone 15 Pro Max 512GB"));
        Product iphone15ProMax_512 = mongoOperations.findOne(iphone15ProMax_512Query, Product.class);
        if (iphone15ProMax_512 == null) {
            iphone15ProMax_512 = new Product();
            iphone15ProMax_512.setId(null);
            iphone15ProMax_512.setProductName("iPhone 15 Pro Max 512GB");
            iphone15ProMax_512.setImage("iphone-15-pro-max-512gb.png");
            iphone15ProMax_512.setDescription("{\"summary\":\"iPhone 15 Pro Max dung lượng lớn\",\"description\":\"iPhone 15 Pro Max 512GB với camera zoom 5x, hiệu năng mạnh mẽ\",\"link_video\":\"https://www.youtube.com/watch?v=izjLMMP8B7s\",\"color\":[\"#000000\",\"#FFFFFF\",\"#00008B\",\"#DAA520\"],\"attribute\":{\"Màn hình\":\"6.7 inch Super Retina XDR\",\"Độ mỏng viền\":\"1.2mm\",\"Chất liệu mặt lưng\":\"Kính\",\"Chip\":\"A17 Pro\",\"Camera\":\"48MP + 12MP + 12MP\",\"Pin\":\"4422mAh\",\"Bộ nhớ\":\"512GB\",\"Hệ điều hành\":\"iOS 17\",\"Kết nối\":\"5G, USB-C\",\"Chống nước\":\"IP68\"}}");
            iphone15ProMax_512.setQuantity(20);
            iphone15ProMax_512.setPrice(39990000);
            iphone15ProMax_512.setCategory(iphone);
            iphone15ProMax_512 = productRepository.save(iphone15ProMax_512);
        }

// Create products for Linh kiện máy tính category
        Query intelCoreQuery = new Query(Criteria.where("productName").is("Intel Core i7-12700K"));
        Product intelCore = mongoOperations.findOne(intelCoreQuery, Product.class);
        if (intelCore == null) {
            intelCore = new Product();
            intelCore.setId(null);
            intelCore.setProductName("Intel Core i7-12700K");
            intelCore.setImage("intel-core-i7-12700k.png");
            intelCore.setDescription("{\"summary\":\"CPU hiệu năng cao\",\"description\":\"Intel Core i7-12700K với 12 nhân, phù hợp gaming và đồ họa chuyên sâu\",\"attribute\":{\"Số nhân\":\"12\",\"Số luồng\":\"20\",\"Xung nhịp cơ bản\":\"3.6GHz\",\"Xung nhịp tối đa\":\"5.0GHz\",\"Bộ nhớ đệm\":\"25MB\",\"Socket\":\"LGA 1700\",\"TDP\":\"125W\",\"Hỗ trợ RAM\":\"DDR4, DDR5\"}}");
            intelCore.setQuantity(50);
            intelCore.setPrice(9990000);
            intelCore.setCategory(linhKien);
            intelCore = productRepository.save(intelCore);
        }

        Query nvidiaRtxQuery = new Query(Criteria.where("productName").is("NVIDIA RTX 3060 Ti"));
        Product nvidiaRtx = mongoOperations.findOne(nvidiaRtxQuery, Product.class);
        if (nvidiaRtx == null) {
            nvidiaRtx = new Product();
            nvidiaRtx.setId(null);
            nvidiaRtx.setProductName("NVIDIA RTX 3060 Ti");
            nvidiaRtx.setImage("nvidia-rtx-3060-ti.png");
            nvidiaRtx.setDescription("{\"summary\":\"Card đồ họa gaming\",\"description\":\"NVIDIA RTX 3060 Ti với hiệu năng mạnh, hỗ trợ ray tracing, lý tưởng cho gaming\",\"attribute\":{\"VRAM\":\"8GB GDDR6\",\"CUDA Cores\":\"4864\",\"Xung nhịp\":\"1665MHz\",\"Băng thông bộ nhớ\":\"448GB/s\",\"Kết nối\":\"PCIe 4.0\",\"Cổng xuất hình\":\"HDMI 2.1, DisplayPort 1.4a\",\"TDP\":\"200W\"}}");
            nvidiaRtx.setQuantity(40);
            nvidiaRtx.setPrice(10990000);
            nvidiaRtx.setCategory(linhKien);
            nvidiaRtx = productRepository.save(nvidiaRtx);
        }

        Query corsairVengeanceQuery = new Query(Criteria.where("productName").is("Corsair Vengeance 16GB DDR4"));
        Product corsairVengeance = mongoOperations.findOne(corsairVengeanceQuery, Product.class);
        if (corsairVengeance == null) {
            corsairVengeance = new Product();
            corsairVengeance.setId(null);
            corsairVengeance.setProductName("Corsair Vengeance 16GB DDR4");
            corsairVengeance.setImage("corsair-vengeance-16gb.png");
            corsairVengeance.setDescription("{\"summary\":\"RAM tốc độ cao\",\"description\":\"Corsair Vengeance 16GB DDR4 với tốc độ 3200MHz, ổn định, dễ ép xung\",\"attribute\":{\"Dung lượng\":\"16GB (2x8GB)\",\"Tốc độ\":\"3200MHz\",\"Loại\":\"DDR4\",\"Độ trễ\":\"CL16\",\"Điện áp\":\"1.35V\",\"Hỗ trợ ép xung\":\"Có\",\"Tản nhiệt\":\"Có\"}}");
            corsairVengeance.setQuantity(80);
            corsairVengeance.setPrice(1490000);
            corsairVengeance.setCategory(linhKien);
            corsairVengeance = productRepository.save(corsairVengeance);
        }

        Query samsungEvoQuery = new Query(Criteria.where("productName").is("Samsung 970 EVO Plus 1TB"));
        Product samsungEvo = mongoOperations.findOne(samsungEvoQuery, Product.class);
        if (samsungEvo == null) {
            samsungEvo = new Product();
            samsungEvo.setId(null);
            samsungEvo.setProductName("Samsung 970 EVO Plus 1TB");
            samsungEvo.setImage("samsung-970-evo-plus.png");
            samsungEvo.setDescription("{\"summary\":\"Ổ SSD tốc độ cao\",\"description\":\"Samsung 970 EVO Plus 1TB với tốc độ đọc/ghi vượt trội, phù hợp lưu trữ lớn\",\"attribute\":{\"Dung lượng\":\"1TB\",\"Tốc độ đọc\":\"3500MB/s\",\"Tốc độ ghi\":\"3300MB/s\",\"Giao tiếp\":\"NVMe PCIe 3.0\",\"Loại chip nhớ\":\"V-NAND 3-bit MLC\",\"TBW\":\"600TB\",\"Kích thước\":\"M.2 2280\"}}");
            samsungEvo.setQuantity(60);
            samsungEvo.setPrice(3490000);
            samsungEvo.setCategory(linhKien);
            samsungEvo = productRepository.save(samsungEvo);
        }

        Query asusRogQuery = new Query(Criteria.where("productName").is("Asus ROG Strix Z690-E"));
        Product asusRog = mongoOperations.findOne(asusRogQuery, Product.class);
        if (asusRog == null) {
            asusRog = new Product();
            asusRog.setId(null);
            asusRog.setProductName("Asus ROG Strix Z690-E");
            asusRog.setImage("asus-rog-strix-z690-e.png");
            asusRog.setDescription("{\"summary\":\"Bo mạch chủ cao cấp\",\"description\":\"Asus ROG Strix Z690-E với hỗ trợ DDR5, PCIe 5.0, phù hợp cấu hình mạnh\",\"attribute\":{\"Chipset\":\"Z690\",\"Socket\":\"LGA 1700\",\"Hỗ trợ RAM\":\"DDR5, tối đa 128GB\",\"Khe PCIe\":\"PCIe 5.0 x16\",\"Cổng lưu trữ\":\"6x SATA, 4x M.2\",\"Kết nối mạng\":\"Wi-Fi 6E, 2.5Gb Ethernet\",\"USB\":\"USB 3.2 Gen 2x2\"}}");
            asusRog.setQuantity(30);
            asusRog.setPrice(7990000);
            asusRog.setCategory(linhKien);
            asusRog = productRepository.save(asusRog);
        }

        Query corsairRmQuery = new Query(Criteria.where("productName").is("Corsair RM750x PSU"));
        Product corsairRm = mongoOperations.findOne(corsairRmQuery, Product.class);
        if (corsairRm == null) {
            corsairRm = new Product();
            corsairRm.setId(null);
            corsairRm.setProductName("Corsair RM750x PSU");
            corsairRm.setImage("corsair-rm750x.png");
            corsairRm.setDescription("{\"summary\":\"Nguồn máy tính chất lượng\",\"description\":\"Corsair RM750x với chứng nhận 80 PLUS Gold, hoạt động êm, hiệu suất cao\",\"attribute\":{\"Công suất\":\"750W\",\"Chứng nhận\":\"80 PLUS Gold\",\"Kích thước quạt\":\"135mm\",\"Hiệu suất\":\"90%\",\"Cáp mô-đun\":\"Có\",\"Kết nối\":\"24-pin ATX, 8-pin EPS\",\"Bảo hành\":\"10 năm\"}}");
            corsairRm.setQuantity(70);
            corsairRm.setPrice(2990000);
            corsairRm.setCategory(linhKien);
            corsairRm = productRepository.save(corsairRm);
        }

        Query wdBlackQuery = new Query(Criteria.where("productName").is("Western Digital Black 2TB HDD"));
        Product wdBlack = mongoOperations.findOne(wdBlackQuery, Product.class);
        if (wdBlack == null) {
            wdBlack = new Product();
            wdBlack.setId(null);
            wdBlack.setProductName("Western Digital Black 2TB HDD");
            wdBlack.setImage("wd-black-2tb.png");
            wdBlack.setDescription("{\"summary\":\"Ổ cứng dung lượng lớn\",\"description\":\"Western Digital Black 2TB với tốc độ cao, phù hợp lưu trữ game và dữ liệu\",\"attribute\":{\"Dung lượng\":\"2TB\",\"Tốc độ vòng quay\":\"7200RPM\",\"Bộ đệm\":\"64MB\",\"Giao tiếp\":\"SATA 6Gb/s\",\"Kích thước\":\"3.5 inch\",\"TBW\":\"300TB\",\"Bảo hành\":\"5 năm\"}}");
            wdBlack.setQuantity(50);
            wdBlack.setPrice(2490000);
            wdBlack.setCategory(linhKien);
            wdBlack = productRepository.save(wdBlack);
        }

        return ResponseEntity.ok().build();
    }
}
