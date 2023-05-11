package ncl.csc8019.group12.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test class is mainly used to test the correctness of the encryption and decryption methods (using the RSA algorithm),
 * and the md5 encryption method in the ConfuseUtil tool class.
 *
 * @author wei tan
 */
public class ConfuseUtilTest {

    Long number = 123L;
    String str = "123@123.com";

    @Test
    public void testEncrypt() throws Exception {
        String data = ConfuseUtil.encryptByRSA(number);
        System.out.println(data);
        Assert.assertEquals(number, ConfuseUtil.decryptToLongByRSA(data));

        data = ConfuseUtil.encryptByRSA(str);
        System.out.println(data);
        Assert.assertEquals(str, ConfuseUtil.decryptByRSA(data));

    }

    @Test
    public void testMd5() {
        Assert.assertEquals(
                ConfuseUtil.md5("123")
                , ConfuseUtil.md5("123"));
        System.out.println(ConfuseUtil.md5("123"));
    }

}
