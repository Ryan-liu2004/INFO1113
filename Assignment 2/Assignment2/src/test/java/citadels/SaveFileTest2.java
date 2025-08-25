package citadels;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

public class SaveFileTest2 {
    public void println(Object str) {System.out.println(str);}
    @Test
    public void Test_test1(){
        println("---------------------");
        println("Test_test1");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload test1\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test whole game workflows
    }
    @Test
    public void Test_test2(){
        println("---------------------");
        println("Test_test2");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload test2\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test whole game workflows
    }
    @Test
    public void Test_test3(){
        println("---------------------");
        println("Test_test3");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload test3\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test whole game workflows
    }
    @Test
    public void Test_test4(){
        println("---------------------");
        println("Test_test4");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload test4\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test whole game workflows
    }
    @Test
    public void Test_test5(){
        println("---------------------");
        println("Test_test5");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload test5\nall\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test whole game workflows
    }
    @Test
    public void Test_test6(){
        println("---------------------");
        println("Test_test6");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload test6\nt\nt\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test whole game workflows and calculate score
    }
    @Test
    public void Test_test7(){
        println("---------------------");
        println("Test_test7");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload test7\nt\nt\nt\nt\nt\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test whole game workflows and calculate score
    }
    @Test
    public void Test_test8(){
        println("---------------------");
        println("Test_test8");

        StringBuilder sb = new StringBuilder();
        sb.append("admin\nload test8\nt\nt\nt\nt\nt\nt\n");
        ByteArrayInputStream in = new ByteArrayInputStream(sb.toString().getBytes());
        System.setIn(in);
        Super.testing_resetinput();
        
        App.main(new String[]{});
        //test whole game workflows and calculate score
    }
}
