package com.custom.di;

import com.custom.di.infrastructure.Factory;
import com.custom.di.services.MyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiApplicationTests {

	@Test
	public void contextLoads() {
		MyService object = Factory.getInstance().createObject(MyService.class);

		System.out.println(object.getKey());
	}

}

