package com.geek.week01;


import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Week01Application extends ClassLoader  {

	public static void main(String[] args) throws Exception{
		Object helloInst = new Week01Application().findClass("Hello").newInstance();
		Method hello = helloInst.getClass().getDeclaredMethod("hello");
		hello.invoke(helloInst);
	}

	@Override
	protected Class<?> findClass(String name){
		byte[] targetBytes = null;
		try {
			URL resource = Week01Application.getSystemClassLoader().getResource("Hello.xlass");
			byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
			targetBytes = new byte[bytes.length];
			int i = 0;
			final byte mask = (byte) 0xff;
			for (byte aByte : bytes) {
				targetBytes[i++] = new Integer(255 - aByte & mask).byteValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.defineClass(name, targetBytes,0, targetBytes.length);
	}

}
