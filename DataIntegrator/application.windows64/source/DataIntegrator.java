import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.io.File; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DataIntegrator extends PApplet {

/// @file DataIntegrator.java
/// @brief Parameter space integrator for RTSI
/// @details
///       Integrator for parameter space exploration provided in many data/log/out/par files
///       based on example from: 
///       https://forum.processing.org/two/discussion/1747/reading-filesnames-from-a-folder
//*////////////////////////////////////////////////////////////////////////////////////////



/// 'PATH' MUST BE FULL OR INSIDE "data" OF THIS SKETCH!
String path="";
//"/data/wb/SCC/private/SocialImpact2/DataIntegrator/data/results/"; 
/// ="/home/borkowsk/SCC/private/SocialImpact2/DataIntegrator/data/results/"; 
//String path="/home/borkowsk/SCC/private/SocialImpact2/DataIntegrator/results-01x100/";
//String path="/home/borkowsk/SCC/private/SocialImpact2/DataIntegrator/results-05x100/";
//String path="/home/borkowsk/SCC/private/SocialImpact2/DataIntegrator/results-10x100/";
//String path=""; ///< ="/data/results"; 
//String path="/home/borkowsk/SCC/private/SocialImpact2/DataIntegrator/results-01x100/"; ///< Path to data
//String path="/home/borkowsk/SCC/private/SocialImpact2/DataIntegrator/results-05x100/"; ///< Path to data
//String path="/home/borkowsk/SCC/private/SocialImpact2/DataIntegrator/results-10x100/"; ///< Path to data

boolean usePars=true;  ///< use .par files as input, alternatively .out data files.

String extFilter=usePars?".par":".out";    ///< extension of the files to be processed (.par vs. .out)
String options=usePars?"tsv":"header,tsv"; ///< loading options for the files to be processed (.par vs. .out)

File   dir;   ///< See https://www.tutorialspoint.com/java/java_file_class.htm
File[] files; ///<  ---//--- 

/// Obligatory method setup().
public void setup() 
{
  
  if(path==null || path.length()==0)
            path=dataPath("");
  dir= new File(path);
  path=dir.getAbsolutePath();
  files= dir.listFiles();
  println(path+"\n"+dir+"\n",files);
  println(files.length);
  //noLoop();
  initialiseAction(path,dir);
}

int i=0; ///< counter for processed files

/// Obligatory method draw().
public void draw() 
{ 
  if(i <= files.length - 1)
  {
    String filename = files[i].getAbsolutePath();
    float pos=map(i,0,files.length - 1,0,width);
    
    if (filename.endsWith(extFilter))
    {
      println();        //      println(filename);
      if(usePars)
      { 
        forParFileAction(filename,files[i]);
      }
      else
      {
        forDataFileAction(filename,files[i]);
      }
    } 
    else stroke(255);
    
    line(pos,0,pos,height);
    i++;
  }
  else
  {
    exit();
    //fill(random(256),random(256),random(256));
    //textAlign(CENTER,CENTER);
    //textSize(20);
    //text("Close window to save data!",width/2,height/2);
  }
}

/// Finalising the job.
public void exit()
{
  finaliseAction(path);
  super.exit();
}

//*//////////////////////////////////////
// ISS University of Warsaw.
/// @author Wojciech Borkowski
//*//////////////////////////////////////

/*
File dir = new File("folderName");
File [] files = dir.listFiles();

// here, if i try to print the contents of the files list: println(dir) or println(files), i also receive errors

void setup()
{

}

void draw()
{
  for (int i = 0; i <= files.length - 1; i++)   
  {
    String path = files[i].getAbsolutePath();
    if(path.toLowerCase().endsWith(".properties.ser"))
    {
        println(path.toLowerCase() + ".properties.ser", i);
    }
  }
}
*/

/*
// we'll have a look in the data folder
java.io.File folder = new java.io.File(dataPath(""));

// list the files in the data folder
String[] filenames = folder.list();

// get and display the number of jpg files
println(filenames.length + " jpg files in specified directory");

// display the filenames
for (int i = 0; i < filenames.length; i++) {
  println(filenames[i]);
}
*/
/// @brief ADDITIONAL VARIABLES ARE MADE HERE.
/// @details We add here two variables not calculated currently in the simulation program:
/// *   mnSensAmbDiff!  
///       `abs(meanSensors-ambientTemp)`: The difference between the agents' sensors and the ambient temperature (in step or its averaging from simulation)
/// *   ratioRTSI2WoC  
///       `mnOpAmbDiff/mnSensAmbDiff`: ratio of RTSI opinion error to Helbings like 'wisdom of crowd' error (in step or its averaging from simulation)
///
public void  makeAdditionalVariables(Table srcTable)
{
  //Tu program sie wywali gdy nie bedzie takich zmiennych!
  int ambientTempInd=srcTable.getColumnIndex("ambientTemp");
  int meanSensorsInd=srcTable.getColumnIndex("meanSensors");
  int mnOpAmbDiffInd=srcTable.getColumnIndex("mnOpAmbDiff!");
  
  //Te zmienne zostana po prostu dodane
  int mnSensAmbDiffInd=srcTable.checkColumnIndex("mnSensAmbDiff!");
  int ratioRTSI2WoCInd=srcTable.checkColumnIndex("ratioRTSI2WoC");
  
  //Ile wierszy do przetworzenia
  int      rcout=srcTable.getRowCount();
  
  println("Important var-s at:",ambientTempInd,meanSensorsInd,mnOpAmbDiffInd,
          mnSensAmbDiffInd,ratioRTSI2WoCInd,"rcout:",rcout);
 
  int cempty=0;
  int cOK=0;
  int unexpected=0;
  
  for(int r=0;r<rcout;r++)
  {
      // Some cells (especially whole rows) may be empty!
      String currstr=srcTable.getString(r,ambientTempInd); //print(currstr+"; ");
      
      if(currstr==null || currstr.length()==0)
      {
        cempty++; //print('?');
      }
      else //Dopiero jak nie jest pusta to można liczyć
      {
        float ambientTemp=srcTable.getFloat(r,ambientTempInd); 
        float meanSensors=srcTable.getFloat(r,meanSensorsInd);
        float mnOpAmbDiff=srcTable.getFloat(r,mnOpAmbDiffInd);
        
        if( !Float.isNaN(ambientTemp)
        &&  !Float.isNaN(meanSensors)
        &&  !Float.isNaN(mnOpAmbDiff)
        )
        {
            //  mnSensAmbDiff! = abs(meanSensors-ambientTemp):
            float mnSensAmbDiff= abs(meanSensors-ambientTemp);
            srcTable.setFloat(r,mnSensAmbDiffInd,mnSensAmbDiff);
             
            //  ratioRTSI2WoC  = mnOpAmbDiff/mnSensAmbDiff
            float ratioRTSI2WoC= (mnSensAmbDiff!=0 ? mnOpAmbDiff/mnSensAmbDiff : -9999);
            
            if(ratioRTSI2WoC!=-9999)
            {
                srcTable.setFloat(r,ratioRTSI2WoCInd,ratioRTSI2WoC);
                cOK++;
            }
            else    
                println(r,") mnSensAmbDiff==0 !!!!!");
        }
        else
        {
            unexpected++;
        }
      }
  }
  
  println("Empty rows:",cempty,"; Valid rows:",cOK,"; Unexpectedly invalid rows:",unexpected);
  println("Additional Variables Ready\n");
}

//*//////////////////////////////////////
// ISS University of Warsaw.
/// @author Wojciech Borkowski
//*//////////////////////////////////////

// Specific job implementation (RTSI)
//*//////////////////////////////////////
/// "RTSIsc1.49iRnd0000015151_Ne.4Si50_Cr.0Bi00.00-50SBi03.00-15nA400Am0.0Sm0.025mT0.001No0Al0.1Be0eT0eA800eS0eFS0eFa0.par"

/// Markers for variable spanning the parameter space
String firstMarker="Bi";        ///< Marker of the first variable.
String secondMarker="0Bi[-+1234567890.]+-";  ///< Marker of the second variable. May be regular expression
String thirdMarker="SBi";       ///< Marker of the third variable. Rarery used.

Table table; ///< A Table object

public void initialiseAction(String path,File dir) ///< Must be globally visible for C++.
{
  println("\nStarting job for: ",path);
  table=new Table();
} 

public void finaliseAction(String path) ///< Must be globally visible for C++.
{
  println("\nFinishing job for: ",path);
  // Writing the CSV back to the same file
  String outName=firstMarker;
  if(secondMarker.length()>0) outName+="x"+secondMarker;
  if(thirdMarker.length()>0) outName+="x"+thirdMarker;
  outName=path+"/"+outName+".csv";
  
  saveTable(table,outName);
  println(outName,"saved.");
}

public void forDataFileAction(String filename,File theFile) ///< Must be globally visible for C++.
{
  println("File:",filename);
  
  Table locTable=loadTable(filename,options);
  
  println("Rows:",locTable.getRowCount(),"Colums:",locTable.getColumnCount());
  if(locTable.getRowCount()==0 || locTable.getColumnCount()==0)
  {
    println("Strange source file, exiting...");
    exit();
  }
  
  println();
  
  // ADDITIONAL VARIABLE WILL BE MAKE HERE!
  makeAdditionalVariables(locTable);
  
  if(table.getColumnCount()==0) // first data file
     makeHeaderFrom(table,locTable);
     
  TableRow newRow = table.addRow();
  String shortname=theFile.getName();
  
  // CALCULATIONS HERE!!!
  makeMeanValuesFrom(newRow,locTable);
  
  //Add information
  newRow.setString("filename",shortname);
  
  if(firstMarker.length()>0) 
    newRow.setFloat(firstMarker,getValueAfterMarker(shortname,firstMarker));
  
  if(secondMarker.length()>0) 
    newRow.setFloat(secondMarker,getValueAfterRegex(shortname,secondMarker));
  
  if(thirdMarker.length()>0) 
    newRow.setFloat(thirdMarker,getValueAfterMarker(shortname,thirdMarker));
      
  stroke(0);
}

/// @brief Counts the averages of all numeric variables in the table.
/// ... and writes them to the target row. 
public void makeMeanValuesFrom(TableRow destRow,Table srcTable)  ///< Must be globally visible for C++.
{
  int      dccount=destRow.getColumnCount();
  int      ccount=srcTable.getColumnCount();
  int      offset=dccount-ccount;
  
  if(offset<2)
  {
    println("Invalid input data!\nTo many columns.");
    exit();
  }
  
  double[] summs=new double[ccount]; //Nie wiadomo które są liczbowe, a które tekstowe
  int[]    cvals=new int[ccount]; //print(cvals[0],cvals[1],cvals[3],cvals[5],' ');
  int      rcout=srcTable.getRowCount();
  int      cempty=0;
  
  for(int r=0;r<rcout;r++)
  {
    // Some cells (especially whole rows) may be empty!
    String currstr=srcTable.getString(r,1); //print(currstr+"; ");
      
    if(currstr==null || currstr.length()==0)
    {
        cempty++; print(r,"?; ");
    }
    else  //Dopiero jak pierwsza wartosc nie jest pusta to można liczyć
    for(int c=0;c<ccount;c++)
    {
      float currval=srcTable.getFloat(r,c); //print(currval+"; ");
      if(!Float.isNaN(currval))
      {
        cvals[c]++;
        summs[c]+=currval;
      }
      else println("\n!!! NaN '"+currstr+"' at ",r,srcTable.getColumnTitle(c));
    }
  }
  
  println("\nEmpty rows counter:",cempty);
  
  for(int c=0;c<ccount;c++)
  if(cvals[c]>0)
  {
    float result=(float)(summs[c] / cvals[c]);
    println(destRow.getColumnTitle(c+offset),"\t:\t",summs[c],"/",cvals[c],"=",(summs[c] / cvals[c]),"->",result);
    destRow.setFloat(c+offset,result);
  }
  else destRow.setString(c+offset,"");
}

/// @brief Create column names for the table header.
/// Creates two to four columns for the filename and used variables spanning the parameter space, 
/// and then transfers all column names from the source table.
public void makeHeaderFrom(Table destTable,Table srcTable)  ///< Must be globally visible.
{
  destTable.addColumn("filename");
  if(firstMarker.length()>0) destTable.addColumn(firstMarker);
  if(secondMarker.length()>0) destTable.addColumn(secondMarker);
  if(thirdMarker.length()>0) destTable.addColumn(thirdMarker);
  String[] names=srcTable.getColumnTitles();
  for(String cname : names)
    destTable.addColumn(trim(cname));
  
  println("Columns prepared. Now they are as follow:");
  names=destTable.getColumnTitles(); 
  for(String cname : names)
    print("'"+cname+"' ");
  println();  
}

//*//////////////////////////////////////
// ISS University of Warsaw.
/// @author Wojciech Borkowski
//*//////////////////////////////////////

/// Gets the float value following the marker in the text string. 
/// @note Dot allowed, 'e'/'E' not allowed.
public float getValueAfterMarker(String source,String marker) ///< Must be globally visible for C++.
{
  int pos=source.indexOf(marker);
  if(pos!=-1)
  {
    pos+=marker.length();
    String value="";
    char   curr=source.charAt(pos++);
    
    // Czy ciąg dla wartości dobrze się zaczyna?
    // https://docs.oracle.com/javase/7/docs/api/java/lang/Character.html#getType(char)
    if(curr=='+' || curr=='-' || Character.getType(curr)==Character.DECIMAL_DIGIT_NUMBER ) 
    {
      value+=curr;
      do{ 
        curr=source.charAt(pos++);
        if(Character.getType(curr)==Character.DECIMAL_DIGIT_NUMBER || curr=='.')
          value+=curr;
        else
          return Float.parseFloat(value);
      }
      while(true);
    }
    return -9999;
  }
  else
  return -1111;
}

/// Gets the float value following the regular expression in the text string. 
public float getValueAfterRegex(String source,String regex) ///< Must be globally visible.
{
  String[] matches = match(source,regex);
  
  if(matches==null) return -9999;
  
  String marker=matches[0]; print("FOUND",regex,"as",marker); //DEBUG
  
  float ret=getValueAfterMarker(source,marker); println(' ',ret);
  
  return ret;
}

//*//////////////////////////////////////
// ISS University of Warsaw.
/// @author Wojciech Borkowski
//*//////////////////////////////////////
/// job for integrating *.par files from RTSI.
//*///////////////////////////////////////////

public void forParFileAction(String filename,File theFile) ///< Must be globally visible.
{
  println("File:",filename);
  Table locTable=new Table();
  TableRow dataRow=null;
  
  BufferedReader reader=createReader(filename);
  boolean OK=true;
  String  line;
  
  if(reader!=null)
  {
    dataRow=locTable.addRow();
  } 
  else return;
  
  do
  {
    try 
    {
      line = reader.readLine();
    } 
    catch (IOException e) 
    {
      e.getMessage(); // e.printStackTrace();
      line = null;
    }
    if (line == null) 
    {
      // Stop reading because of an error or file is empty
      OK=false;
    } 
    else 
    {
      String[] lst=split(line,'\t'); //println(line);
      for(String s:lst) trim(s);     //println(lst);
      
      if(lst.length>1 && !lst[0].isEmpty() && lst[1].equals(":") )
      {
        lst[0]=lst[0].replace('.','_');
        lst[0]=lst[0].replace(' ','_');
        locTable.addColumn(lst[0]); //println(lst[0],"=",lst[2]);      assert dataRow!=null;
        dataRow.setString(lst[0],lst[2]);
      }
    }
  }while(OK);
  
  saveTable(locTable, filename+".tsv"); //DEBUG
    
  if(table.getColumnCount()==0) // first data file give the header for 'table'
     makeHeaderFrom(table,locTable);
     
  TableRow newRow = table.addRow();
  moveData(newRow,dataRow);
  
  //Add information
  String shortname=theFile.getName();
  newRow.setString("filename",shortname);
  
  if(firstMarker.length()>0) 
    newRow.setFloat(firstMarker,getValueAfterMarker(shortname,firstMarker));
  
  if(secondMarker.length()>0) 
    newRow.setFloat(secondMarker,getValueAfterRegex(shortname,secondMarker));
  
  if(thirdMarker.length()>0) 
    newRow.setFloat(thirdMarker,getValueAfterMarker(shortname,thirdMarker));
      
  stroke(0);
}

/// Helper for move/copy data from one table to another.
public void moveData(TableRow target,TableRow source) ///< Must be globally visible for C++.
{
  for(int i=0;i<source.getColumnCount();i++)
  {
    String colname=source.getColumnTitle(i); //print(colname+"=");
    String cell=source.getString(i);         //println(cell);
    target.setString(colname,cell);
  }
  println();
}

//*//////////////////////////////////////
// ISS University of Warsaw.
/// @author Wojciech Borkowski
//*//////////////////////////////////////
  public void settings() {  size(300,50); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DataIntegrator" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
