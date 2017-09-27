package cn.itcast.test.md5;

import org.junit.Test;
import org.springframework.util.DigestUtils;

public class TestMD5 {
	@Test
	public void test1() throws Exception {
		String password  = "123";
		String rs = DigestUtils.md5DigestAsHex(password.getBytes());
		System.out.println(rs);
	}
}
