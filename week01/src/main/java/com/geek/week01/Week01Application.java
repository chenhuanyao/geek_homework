package com.geek.week01;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

@SpringBootApplication
public class Week01Application extends ClassLoader implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Week01Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("******************************");
		Object helloInst = new Week01Application().findClass("Hello").newInstance();
		Method hello = helloInst.getClass().getDeclaredMethod("hello");
		hello.invoke(helloInst);
		System.out.println("******************************");
	}

	@Override
	protected Class<?> findClass(String name){
		byte[] targetBytes;
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:Hello.xlass");
		try(InputStream inputStream = resource.getInputStream();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream()){
			byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
			int rc;
			while ((rc = inputStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			byte[] bytes = swapStream.toByteArray();
			targetBytes = new byte[bytes.length];
			int i = 0;
			final byte mask = (byte) 0xff;
			for (byte aByte : bytes) {
				targetBytes[i++] = new Integer(255 - aByte & mask).byteValue();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return super.defineClass(name, targetBytes,0, targetBytes.length);
	}


}
