import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.*;
import java.math.*; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class TestJ extends DriverTypesSim {
	private static double[][] testLen = {{1,0.5,30,5},{2,1,20,5},{3,2,20,5},{4,3,20,5},{5,5,20,5},{6,1.50,20,5}};
    private static double[][] courseInfo = {{3.0, 0.8, 30.0}, {2.0, 1.0, 60.0}, {1.0, 0.5, 20.0}, {5.0, 0.5, 20.0}, {4.0, 0.5, 20.0}};
    
    
  //OVERALL TESTS IN DriverTypesSim.class
   //Test to see if program can handle nonexistent files
  @Test
    public void printErrorIfNoFile() {
    	String filename = "inp.xml";
		XMLFileData data = new XMLFileData(filename);
		data.readFile();
    	assertNull(data.getList());
    }
  
  //Test to see if courseInfo is properly being used in calculateDistanceValuesForDrivers
  @Test
  public void printTableIfCourseInfoValid() {
	  ArrayList<ArrayList<Double>> printData = calculateDistanceValuesForDrivers(2,3.3,courseInfo);
	  ArrayList<ArrayList<Double>> expectData = calculateDistanceValuesForDrivers(2,3.3,courseInfo);
	  
	  assertEquals(expectData, printData);
  }
  
 //Test to see if totalDistance is being calulcated from each segment
  @SuppressWarnings("deprecation")
  @Test
  public void testSumLenFunction() {
	  assertEquals(13, sumLen(testLen),0.003);	  
  }
  
  @SuppressWarnings("deprecation")
  @Test
  public void testSort2dFunction() {
	  double[][] expectData = {{1.0, 0.5, 20.0},  {2.0, 1.0, 60.0}, {3.0, 0.8, 30.0}, {4.0, 0.5, 20.0}, {5.0, 0.5, 20.0} };
	  double[][] courseInfoC = courseInfo.clone();
	  sort2D(courseInfoC,0);
	  
	  assertEquals(expectData, courseInfoC);
  }
  

  
  

}
