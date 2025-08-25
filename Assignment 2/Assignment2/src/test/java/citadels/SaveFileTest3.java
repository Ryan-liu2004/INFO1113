package citadels;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

public class SaveFileTest3 {
    public void println(Object str) {System.out.println(str);}

    @Test
    public void Test_test9(){
        println("---------------------");
        println("Test_test9");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload test9\nt\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test whole game workflows and calculate score
    }
    @Test
    public void Test_museumtest1(){
        println("---------------------");
        println("Test_museumtest1");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload museumtest1\nend\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test function of museum and calculate score
    }
}
