package LeetCode.src.Problems.P601_P700.P698_PartitionToKEqualSumSubsets.Java;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class SolutionTest {
    
    Solution solution;
    
    @BeforeEach
    public void setUp() throws Exception {
        solution = new Solution();
    }
    
    @AfterEach
    public void tearDown() throws Exception {
        solution = null;
    }
    
    @Test
    public void MainFunction() {
        assertTimeout(Duration.ofMillis(500), () -> {
             String[] args = new String[0];
             assertAll(() -> Solution.main(args));
        });
    }
    
    @Test
    public void TrivialCase1() {
        int[] nums = {4, 3, 2, 3, 5, 2, 1};
        int k = 4;
        assertTimeout(Duration.ofMillis(500), () -> {
            boolean expected = true;
            boolean actual = Solution.canPartitionKSubsets(nums, k);
            assertEquals(expected, actual);
        });
    }
}