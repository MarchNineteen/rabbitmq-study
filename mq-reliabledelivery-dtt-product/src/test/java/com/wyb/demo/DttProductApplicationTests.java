package com.wyb.demo;

import com.wyb.mapper.ProductInfoMapper;
import com.wyb.service.IProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DttProductApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private IProductService productService;

	@Autowired
	private ProductInfoMapper productInfoMapper;



	@Test
	public void testUpdateProductStore2() {
		productInfoMapper.updateProductStoreById(1);
	}

}
