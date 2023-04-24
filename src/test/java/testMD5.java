import com.example.zhxy.util.MD5;
import org.junit.jupiter.api.Test;

/**
 * @author Cavan
 * @date 2023-03-22
 * @qq 2069543852
 */
public class testMD5 {

    @Test
    public void testMD5(){
        String encrypt = MD5.encrypt("BUzhidao520@");
        //21232f297a57a5a743894a0e4a801fc3
        String encrypt1 = MD5.encrypt("admin");
        System.out.println(encrypt1);
//        String password = encrypt.substring(25);
//        System.out.println(password);
    }

    //e10adc3949ba59abbe56e057f20f883e
    //4b012f806930582f116b7dadf4101fef

}
