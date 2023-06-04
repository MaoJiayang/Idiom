package com.nucleon;
import org.junit.Test;
import static org.junit.Assert.*;
import com.nucleon.game.GameFlow;
public class GameFlowTest {
    
    @Test
    public void testStartGame() {
        GameFlow gameFlow = new GameFlow();
        gameFlow.loadData();
    }
}
