package player;

import org.junit.Test;

public class MainPlayerTest{
    
    @Test
    public void pieceOneTest(){
        Main.play("sample_abc//piece1.abc");
    }
    
    @Test
    public void pieceTwoTest(){
        Main.play("sample_abc//piece2.abc");
    }
}