package cn.ournh.manager.controller;

import cn.ournh.entity.Product;
import cn.ournh.entity.enums.ProductStatus;
import cn.ournh.util.RestUtil;
import org.aspectj.bridge.IMessage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Pageable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    /**
     * 正常数据
     */
    private static List<Product> normals = new ArrayList<>();
    private static List<Product> exception = new ArrayList<>();

    @BeforeClass
    public static void init(){
        Product p1 = new Product("T001","余额宝", ProductStatus.AUDITING.name(),
                BigDecimal.valueOf(10), BigDecimal.valueOf(1),BigDecimal.valueOf(3.42));
        Product p2 = new Product("T002","财付通", ProductStatus.AUDITING.name(),
                BigDecimal.valueOf(1200), BigDecimal.valueOf(0),BigDecimal.valueOf(3.4));
        Product p3 = new Product("T003","陆金所", ProductStatus.AUDITING.name(),
                BigDecimal.valueOf(140), BigDecimal.valueOf(10),BigDecimal.valueOf(5.47));
        normals.add(p1);
        normals.add(p2);
        normals.add(p3);


        Product e1 = new Product(null,"编号不可为空", ProductStatus.AUDITING.name(),
                BigDecimal.valueOf(10), BigDecimal.valueOf(1),BigDecimal.valueOf(3.42));
        Product e2 = new Product("E002","收益率范围错误", ProductStatus.AUDITING.name(),
                BigDecimal.valueOf(1200), BigDecimal.valueOf(0),BigDecimal.valueOf(31));
        Product e3 = new Product("T003","投资步长要为整数", ProductStatus.AUDITING.name(),
                BigDecimal.valueOf(140), BigDecimal.valueOf(1.02),BigDecimal.valueOf(5.47));
        exception.add(e1);
        exception.add(e2);
        exception.add(e3);

        ResponseErrorHandler errorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        };
        restTemplate.setErrorHandler(errorHandler);

    }
    private static RestTemplate restTemplate = new RestTemplate();

    @Value("http://localhost:${local.server.port}/manager")
    private String baseUrl;

    @Test
    public void create(){

        normals.forEach(product ->{
           HashMap<String,String> result= RestUtil.postJSON(restTemplate,baseUrl+"/products",product, HashMap.class);
            System.out.println(result);
        });

    }

    @Test
    public void createException(){
        exception.forEach(product -> {
            HashMap<String,String> result= RestUtil.postJSON(restTemplate,baseUrl+"/products",product,HashMap.class);
            Assert.isTrue(result.get("message").equals(product.getName()),"插入失败");
            System.out.println(result);
        });

    }

}
