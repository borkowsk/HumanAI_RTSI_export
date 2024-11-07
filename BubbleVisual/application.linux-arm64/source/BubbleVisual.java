import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.svg.*; 
import processing.pdf.*; 
import java.text.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class BubbleVisual extends PApplet {

/// Program do wizualizacji bąbelkowej
/// Rozwinięty z przykładu do Processingu:
///   "Loading Tabular Data * by Daniel Shiffman." 
///   https://processing.org/examples/loadsavetable.html
 ///< https://processing.org/reference/libraries/svg/index.html
 ///< https://processing.org/reference/libraries/pdf/index.html

/// Data filename. It can be a full path or name of
/// a file located in the "data" directory of the script!
//String fileName="/home/borkowsk/SCC/private/SocialImpact2/Grapher/testpoza.csv";
//String fileName="testNew.csv";
/// from DataIntegrator on RTSI data
String fileName="Bix0BiROZxSBi.csv";
//"BixSBixSi_zPAR.csv";
//"BixSBixSi-20220724_results_disp5x10.csv"; 
//"BixSBixSi-dense_rozrzut10.csv";                     
//"BixSBixSi-przeliczone-disp5.csv"; 
//"RTSIsc1.48dRnd0000014052_Ne.4Si50_Cr.0Bi02.00-5SBi10.00-5nA160Am0.0Sm0.25mT0.001No0Al0.1Be0eT0eA160eS0eFS0eFa0.out";
//"RTSIsc1.48dRnd0000013055_Ne.4Si50_Cr.0Bi05.00-5SBi05.00-5nA160Am0.0Sm0.25mT0.001No0Al0.1Be0eT0eA160eS0eFS0eFa0.out";
//"RTSIsc1.48dRnd0000011851_Ne.4Si50_Cr.0Bi01.00-5SBi-01.00-5nA160Am0.0Sm0.25mT0.001No0Al0.1Be0eT0eA160eS0eFS0eFa0.out";
String dictName="BixSBixSi.csv.dict"; ///< If it is empty, is prepared as fileName + ".dict" (without path maj be in strange lacation!)
String options="header,csv";  ///< + ",csv" ",tsv" optionally, when file has not proper extension

//String fileName="/home/borkowsk/SCC/private/SocialImpact2/RToSInfluence/src/RTSIPNASbr140/results/RTSIsc1.48cRnd0000000016_Ne.4Si50_Cr.0Bi10.00-30SBi05.00-5nA160Am0.0Sm0.25mT0.001No0Al0.1Be0eT0eA160eS0eFS0eFa0.out";
//String options="header,tsv";

/// Path to graphics driver class
//String driver=SVG; ///< Possible drivers are: P2D, P3D, FX2D, PDF, SVG
//String driver=PDF;   ///< Possible drivers are: P2D, P3D, FX2D, PDF, SVG
String driver=""; ///< empty driver means default. Probably P2D.

//String output="output.pdf"; ///< for PDF
//String output="output.svg"; ///< for SVG
String output=""; ///< for others than PDF or SVG it MUST be empty!
/// Few output format is acceptable in processing, but .png looks like the best option:
String forceOutputExt=".png";//".tif";//".jpg";",png";".gif";

float TEXTSIZE=10;
float BUBBLE_OPACITY=200;
float BUBBLE_MAXDIAM=10;
float BUBBLE_VERDIAM=18;

int   reqWidth=1000; ///< required width of scatter graph
int   reqLegend=300; ///< required width of legend
int   reqHeight=720; ///< required hight of scatter graph
int   reqStatus=PApplet.parseInt(BUBBLE_VERDIAM+TEXTSIZE*7); ///< required hight of scatter bar
int LEGEND_BACKGROUND=color( 144,111,144 );

int   outputSelector=1; ///< what type of visualisation? 0 - no any.

/// Names of used variablesSenAmbDif_fin_MED
String CaseName="filename";//""$REPET";
String VarX="Bi";//"BIASmean_for_typical_agents";//"H9";//" ambientTemp";//" meanTemp";//" StepCounter";//" ambientTemp";
String VarY="SBi";//"BiROZ";;//" StepCounter";//"meanOpinion";
String VarDiam="NUMBER_OF_REPET";//"providersCount";//" liveCount";
String VarRed="";//"OpiAmbDif_fin_MED";//"OpiAmbDif_fin_MED"; OpiAmbDif_fin_MEAN //"TomRatio_fin_MED";//OpiAmbDif_fin_MED";//"meanDistSelf";
String VarGreen="";//"SenAmbDif_fin_MED";//"TomRatio_fin_MED";//"SenAmbDif_fin_MED";//"OpiAmbDif_fin_MED";//"SenAmbDif_fin_MED";//"mnSensAmbDiff!";//"providersCount";//meanDistSelf";//" StepCounter";//" meanTemp";//"$REPET";// ambientTemp";//" StepCounter";//
String VarBlue="TomRatio_fin_MED";//"SenAmbDif_fin_MED";//"SenAmbDif_fin_MEAN";//"TomRatio_fin_MED"; ///meanDistSelf";//" meanTemp";//$REPET";//" StepCounter";

boolean RedLog10=false;
boolean GreLog10=false;
boolean BluLog10=false;

boolean usedName=CaseName.length()>0;
boolean usedX=VarX.length()>0;
boolean usedY=VarY.length()>0;
boolean usedDiam=VarDiam.length()>0;
boolean usedRed=VarRed.length()>0;
boolean usedGreen=VarGreen.length()>0;
boolean usedBlue=VarBlue.length()>0;
//boolean used=.length()>0;

int   debug_level=0;///< GLOBAL!

public void settings() 
{ 
  checkCommandLine();
  if(driver.length()>0 || output.length()>0)
  {
    if(output.length()<=0)
    {
        if(driver.equals(SVG)) output="output.svg"; ///< for SVG
        if(driver.equals(PDF)) output="output.pdf"; ///< for PDF
    }
    println("driver:",driver,"\noutput:",output);
    size(reqWidth+reqLegend,reqHeight+reqStatus,driver,output);
  }
  else
    size(reqWidth+reqLegend,reqHeight+reqStatus);
}

public void setup()
{ 
  //println("'Bubbler' - Program do wizualizacji wykresów bąbelkowych");
  println("'Bubbler' - Program for bubble charts visualization");
  textAlign(CENTER,CENTER);
  text("'Bubbler' - Program for bubble charts visualization",width/2,height/2);
  println("(c) 2022 https://www.researchgate.net/profile/WOJCIECH_BORKOWSKI");
  println("OPTIONS are:","X","Y","D","R","G","B","CASENAME","FILENAME","FILEOPTIONS","OUTPUT & more...");
  println("Separator is ':'. For example: 'R:$id'\n");
  //println("Default SHAPE of points:",DEF_BUBBLE_SHAPE);
  
  loadData(fileName,options); // Loading data and print variable names!
  
  //initDictionary(fileName);
  //saveDictionary(fileName);
  loadDictionary(dictName.equals("")?fileName+".dict":dictName);
  
  if(outputSelector>0
  && usedX && usedY
  ) // For now only one type.
  {
    makeBubbles();
    textSize(TEXTSIZE);
    
    if(!(output.length()>0 && driver.length()>0))
    {
      frameRate(10);
      ellipseMode(CENTER);  // Set ellipseMode to CENTER
      //ellipseMode(RADIUS);  // Set ellipseMode to RADIUS
      println("\nFor make output graphics press SPACE!");
    }
  
  }
  else
  exit();
}

public void draw() 
{
  if(frameCount==20) // To z jakiegoś powodu może trwać długo, dlatego jest odsunięte w czasie
  {
    surface.setTitle(windowTitle); // https://processing.org/reference/setTitle_.html
    println("Windows title is '"+windowTitle+"'");
  }
  
  if(outputSelector>0) // For now only one.
  {
    drawBubbles(); //Drawing scatter plot with sizable and colorable points represented by diferent shapes
    if(reqStatus>0) fillStatus();
  }  
  
  // PDF and SVG required exit in draw to save data into file
  if(output.length()>0 && driver.length()>0)
  {
     //save(windowTitle); //No save() for PGraphicsSVG & No save() for PGraphicsPDF !!!
     exit();
  }
}

public void exit()
{
  println("ALL DONE");
  super.exit();
}

public void fillStatus()
{
  float colscaleBeg=height-reqStatus+BUBBLE_VERDIAM/2;
  textAlign(LEFT,TOP);  
  if(usedX){ fill(64);      text( "X: "+VarX + " : " + dictionaryInfo(VarX), BUBBLE_MAXDIAM,colscaleBeg+=TEXTSIZE); }
  if(usedY){ fill(128,0,0); text( "Y: "+VarY + " : " + dictionaryInfo(VarY), BUBBLE_MAXDIAM,colscaleBeg+=TEXTSIZE); }
  if(usedDiam){ fill(0);    text( "D: "+VarDiam + " : " + dictionaryInfo(VarDiam), BUBBLE_MAXDIAM,colscaleBeg+=TEXTSIZE); }
  
  if(usedRed){ fill(255,0,0);   text("R: "+(RedLog10?"log10(x+1) ":"")
                                     +VarRed + " : " + dictionaryInfo(VarRed), BUBBLE_MAXDIAM,colscaleBeg+=TEXTSIZE); }
  if(usedGreen){ fill(0,255,0); text("G: "+(GreLog10?"log10(x+1) ":"")
                                     +VarGreen + " : " + dictionaryInfo(VarGreen), BUBBLE_MAXDIAM,colscaleBeg+=TEXTSIZE); }
  if(usedBlue){ fill(0,0,255);  text("B: "+(BluLog10?"log10(x+1) ":"")
                                     +VarBlue + " : " + dictionaryInfo(VarBlue), BUBBLE_MAXDIAM,colscaleBeg+=TEXTSIZE); }
  
  if(usedName){ fill(0); text( "CaseName: "+CaseName + " : " + dictionaryInfo(CaseName), BUBBLE_MAXDIAM ,colscaleBeg+=TEXTSIZE); }
}

//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// "Bubble Visalisation" - Program do wizualizacji bąbelkowej - HumanAI,GuestXR, ISS University of Warsaw.
/// 2022 see: "https://www.researchgate.net/profile/WOJCIECH_BORKOWSKI" @author Wojciech Borkowski
//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// A Bubble class
//*//////////////////////////////////////

enum  BubbShape { Circle,Rect,Diamond,Triangle }
BubbShape DEF_BUBBLE_SHAPE=BubbShape.Rect; ///< Use string2BubbShape(String name) for conversion

public BubbShape string2BubbShape(String name)
{
  switch(name.charAt(0)){
    default: println("Warning!","Bubb.Shape",name,"is unknown. Using default.\n(only first letter is meaningfull)");
    case 'c':
    case 'C': return BubbShape.Circle;
    case 'r':
    case 'R': return BubbShape.Rect;
    case 'd':
    case 'D': return BubbShape.Diamond;
    case 't':
    case 'T': return BubbShape.Triangle;    
  }
}

//float BUBBLE_OPACITY=255;
//float BUBBLE_MAXDIAM=24;
//float BUBBLE_VERDIAM=48;

static float minX=MAX_FLOAT,maxX=-MAX_FLOAT;
static float minY=MAX_FLOAT,maxY=-MAX_FLOAT;
static float minD=MAX_FLOAT,maxD=-MAX_FLOAT;

/// Zakresy dla kolorów muszą być ukryte przed zewnetrznymi
static float _minRed=MAX_FLOAT;
static float _maxRed=-MAX_FLOAT;
static float _minGreen=MAX_FLOAT;
static float _maxGreen=-MAX_FLOAT;
static float _minBlue=MAX_FLOAT;
static float _maxBlue=-MAX_FLOAT;

public float minRed() { return RedLog10? 0 :_minRed; }
public float maxRed() { return RedLog10? (float)Math.log10(_maxRed+1):_maxRed; }

public float minBlue() { return BluLog10? 0 :_minBlue; }
public float maxBlue() { return BluLog10? (float)Math.log10(_maxBlue+1):_maxBlue; }

public float minGreen() { return GreLog10? 0 :_minGreen; }
public float maxGreen() { return GreLog10? (float)Math.log10(_maxGreen+1):_maxGreen; }
  
/// A Bubble class definition
class Bubble 
{
  private BubbShape shape=DEF_BUBBLE_SHAPE;
  private float red; //?RedLog10
  private float green;
  private float blue;
  private float x;
  private float y;
  private float diameter;
  private String name;
 
  public
  boolean over = false;
  boolean prin = false;
  
  /// Create  the Bubble (constructor)
  Bubble(float x_, float y_,float dia_,String s,float R_,float G_,float B_) 
  {
    name = s;
    
    x = x_; if(x>maxX) maxX=x; if(x<minX) minX=x;
    y = y_; if(y>maxY) maxY=y; if(y<minY) minY=y;
    diameter = dia_; if(diameter>maxD) maxD=diameter; if(diameter<minD) minD=diameter;
    
    red=R_;    if(red>_maxRed) _maxRed=red;         if(red<_minRed) _minRed=red;
    green=G_;  if(green>_maxGreen) _maxGreen=green; if(green<_minGreen) _minGreen=green;
    blue=B_;   if(blue>_maxBlue) _maxBlue=blue;     if(blue<_minBlue) _minBlue=blue;
  }
  
  /// Checking if mouse is over the Bubble
  public void rollover(float px, float py,boolean withPrint) 
  {
    float scx=map(x,minX,maxX,0,width-reqLegend);
    float scy=height-reqStatus-map(y,minY,maxY,0,height-reqStatus);
    float sdi=map(diameter,minD,maxD,2,20);
    float d = dist(px,py,scx,scy);
    if (d < sdi/2) {
      over = true;
      if(withPrint) prin=true;
    } else {
      over = false;
    }
  }
  
  /// Display the Bubble
  public void display() 
  {
    float scx=map(x,minX,maxX,0,width-reqLegend);
    float scy=height-reqStatus-map(y,minY,maxY,0,height-reqStatus);
    float sdi=map(diameter,minD,maxD,2,BUBBLE_MAXDIAM);
    float sdj=map(diameter,minD,maxD,2,BUBBLE_VERDIAM);
    float sred=( RedLog10 && usedRed ?
                    map( red>=0?(float)(Math.log10(red+1)):0 ,0,(float)(Math.log10(_maxRed+1)),0,255):
                    map( red,_minRed,_maxRed,0,255)
               );
    float sgreen=( GreLog10 && usedGreen ?
                    map( green>=0?(float)(Math.log10(green+1)):0 ,0,(float)(Math.log10(_maxGreen+1)),0,255):
                    map(green,_minGreen,_maxGreen,0,255)
               );
    float sblue=( BluLog10 && usedBlue ?
                    map( blue>=0?(float)(Math.log10(blue+1)):0 ,0,(float)(Math.log10(_maxBlue+1)),0,255):
                    map( blue,_minBlue,_maxBlue,0,255)
               );
    
    fill(sred,sgreen,sblue,BUBBLE_OPACITY);
    
    switch(shape){
    default:  
    case Circle:  ellipse(scx,scy,sdi,sdj); break;
    case Triangle: {float halfi=sdi/2,halfj=sdj/2; triangle(scx,scy-halfj,scx-halfi,scy+halfj/2,scx+halfi,scy+halfj/2); } break;
    case Rect: {float halfi=sdi/2,halfj=sdj/2;rect(scx-halfi,scy-halfj,sdi,sdj);} break;
    case Diamond:{float halfi=sdi/2,halfj=sdj/2;quad(scx-halfi,scy,scx,scy+halfj,scx+halfi,scy,scx,scy-halfj);} break;
    }
    
    if (over) {
      fill(sred,sgreen,sblue);
      textAlign(CENTER);
      text((usedName?"'"+name+"'\n":"")
           +"x:"+nf(x)+",y:"+nf(y)
           +(usedDiam?",d:"+nf(diameter):"")
           +(usedRed?",R:"+nf(red):"")
           +(usedGreen?",G:"+nf(green):"")
           +(usedBlue?",B:"+nf(blue):"")
           ,scx,scy+sdi/2+20);
      
      if(prin)
      {  
        println(frameCount,")");
        if(usedName)println("'"+name+"'");
        println("x:"+VarX+"\t=\t",x);
        println("y:"+VarY+"\t=\t",y);
        if(usedDiam)  
          println("d:"+VarDiam+"\t=\t",diameter);
        if(usedRed)   
          println("R:"+VarRed+"\t=\t",red);
        if(usedGreen) 
          println("G:"+VarGreen+"\t=\t",green);
        if(usedBlue)  
          println("B:"+VarBlue+"\t=\t",blue);
        prin=false;
      }
    }
  }
}


/// The size of the array of Bubble objects is determined 
/// by the total number of rows in the CSV
public void makeBubbles() 
{
  _minRed=Float.MAX_VALUE;
  _maxRed=-Float.MAX_VALUE;
  _minGreen=Float.MAX_VALUE;
  _maxGreen=-Float.MAX_VALUE;
  _minBlue=Float.MAX_VALUE;
  _maxBlue=-Float.MAX_VALUE;
  
  minX=Float.MAX_VALUE;
  maxX=-Float.MAX_VALUE;
  minY=Float.MAX_VALUE;
  maxY=-Float.MAX_VALUE;
  minD=Float.MAX_VALUE;
  maxD=-Float.MAX_VALUE;

  bubbles = new Bubble[table.getRowCount()]; 

  // You can access iterate over all the rows in a table
  int rowCount = 0;
  for (TableRow row : table.rows()) {
    // You can access the fields via their column name (or index)
    String n = "";
    if (usedName) n=row.getString(CaseName);

    float x = row.getFloat(VarX);  
    if (Float.isNaN(x)) { println("Invalid 'X':",row.getString(VarX),row.getString(CaseName)); continue; }
    
    float y = row.getFloat(VarY);  
    if (Float.isNaN(y)) { println("Invalid 'Y':",row.getString(VarY),row.getString(CaseName));  continue; }
    
    float d = 2;
    if (usedDiam) { 
      d=row.getFloat(VarDiam); 
      if (Float.isNaN(d)) { println("Invalid 'D':",row.getString(VarDiam),row.getString(CaseName));  continue; }
    }

    float red=1,green=1,blue=1;
    
    if (usedRed) { 
      red=row.getFloat(VarRed);  
      if (Float.isNaN(red)) { println("Invalid 'Red':",VarRed,row.getString(VarRed),row.getString(CaseName));  continue; }
    }
    
    if (usedGreen) { 
      green=row.getFloat(VarGreen); 
      if (Float.isNaN(green)) { println("Invalid 'Green':",VarGreen,row.getString(VarGreen),row.getString(CaseName));  continue; }
    }
    
    if (usedBlue) { 
      blue=row.getFloat(VarBlue); 
      if (Float.isNaN(blue)) { println("Invalid 'Blue':",VarBlue,row.getString(VarBlue),row.getString(CaseName));  continue; }
    }

    // Make a Bubble object out of the data read
    bubbles[rowCount] = new Bubble(x, y, d, n, red, green, blue);
    rowCount++;
  }

  // When all values are equal
  if (_minRed==_maxRed)     { _maxRed=_minRed+1;  }
  if (_minGreen==_maxGreen) { _maxGreen=_minGreen+1;  }
  if (_minBlue==_maxBlue)   { _maxBlue=_minBlue+1;  }
  if (minX==maxX) { minX=maxX-1;  }
  if (minY==maxY) { minY=maxY-1;  }
  if (minD==maxD) { minD=maxD-1;  }
}

//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// "Bubble Visalisation" - Program do wizualizacji bąbelkowej - HumanAI,GuestXR, ISS University of Warsaw.
/// 2022 see: "https://www.researchgate.net/profile/WOJCIECH_BORKOWSKI" @author Wojciech Borkowski
//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// @brief Load CSV file into a Table object.
/// "header" option indicates the file has a header row
/// WARNING! First name have allways leading space!
public void loadData(String filename,String options)
{
  println("filename:",filename);
  table = loadTable(filename,options);
  println("Rows:",table.getRowCount(),"Colums:",table.getColumnCount());
  if(table.getRowCount()==0 || table.getColumnCount()==0)
  {
    println("Strange file, exiting...");
    exit();
  }
  print("Column names (case sensitive): ");
  String[] names=table.getColumnTitles();
  for(String cname : names) 
    print("'"+cname+"' ");//! WARNING! First name (may) have allways leading space!
  println("\n\nBe careful with spaces, when they are included in names!\n");
}

Table table; ///< A target table object.

//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// "Bubble Visalisation" - Program do wizualizacji bąbelkowej - HumanAI,GuestXR, ISS University of Warsaw.
/// 2022 see: "https://www.researchgate.net/profile/WOJCIECH_BORKOWSKI" @author Wojciech Borkowski
//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
//import java.io.FileReader;
//import java.io.BufferedReader;
 //SimpleTextFormat;

public void initDictionary()
{
  variablesInformation=new StringDict(new String[][] {
   { "filename", "Name of the source file" },
   { "step", "This is the simulation step number" },
   { "$STEP", "This is the simulation step number" },
   { "repet", "This is the repetition number of the simulation" },
   { "$REPET","This is the repetition number of the simulation" },
   { "repetition","Repetition number of the simulation" }
 });
 
 println(variablesInformation);
}

public String  dictionaryInfo(String keyStr)
{
  String out=variablesInformation.get(keyStr);
  if(out!=null)
      return out;
  else
      return "???";
}

public void loadDictionary(String filename)
{
  java.io.File file=new java.io.File(filename);
  if(file.exists())
  {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
    String strDate = sdfDate.format(file.lastModified());
    println("Dictionary load from:",file.getAbsolutePath(),strDate);
    BufferedReader reader=createReader(file.getAbsolutePath());
    variablesInformation=new StringDict(reader);
    println(variablesInformation);
    println("\n\nBe careful with spaces mimic tabs in this file!\n");
  }
  else
  {
    println("\nNot exist file:",file.getAbsolutePath());
    println("Default dictionary will be prepared!");
    initDictionary();
    println();
  }
}

public void saveDictionary(String corename)
{
  if(variablesInformation!=null && corename!=null && corename.length()>0)
  {
    String filename=corename+".dict";
    java.io.File file=new java.io.File(filename);
    println("Dictionary saved in:",file.getAbsolutePath());
    variablesInformation.save(file);
  }
}

StringDict variablesInformation; ///< Target object for handle dictionary.


//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// "Bubble Visalisation" - Program do wizualizacji bąbelkowej - HumanAI,GuestXR, ISS University of Warsaw.
/// 2022 see: "https://www.researchgate.net/profile/WOJCIECH_BORKOWSKI" @author Wojciech Borkowski
//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// @brief Data visualisation.
//*///////////////////////////
public void drawBubbles() 
{
  background(255);
  noStroke();

  // Display all bubbles
  for (Bubble b : bubbles) 
  {
    if (b==null) continue;
    b.display();
    b.rollover(mouseX, mouseY, false);
  }

  // Display legend with color scales
  noStroke();
  fill(LEGEND_BACKGROUND);//LEGEND_BACKGROUND 144,111,144
  rect(width-reqLegend, 0, reqLegend, height);

  fill(128);
  textAlign(LEFT, BOTTOM);
  text(nf(minX)+" "+VarX, 0, height-reqStatus);
  textAlign(RIGHT, BOTTOM);
  text(nf(maxX)+" ", width-reqLegend, height-reqStatus);
  fill(255, 0, 0);
  textAlign(CENTER, TOP);
  text(nf(maxY), (width-reqLegend)/2, 0);
  text(VarY, (width-reqLegend)/2, 12);
  textAlign(CENTER, BOTTOM);
  text(nf(minY), (width-reqLegend)/2, height-reqStatus);

  float sdi=map(maxD, minD, maxD, 2, BUBBLE_MAXDIAM);
  float sdj=map(maxD, minD, maxD, 2, BUBBLE_VERDIAM);

  if (usedDiam)
  {
    fill(64); 
    textAlign(CENTER, TOP);
    text("Diameter: "+VarDiam, width-reqLegend/2, 0);  

    float scx=width-sdi/2;
    float scy=sdj/2;
    switch(DEF_BUBBLE_SHAPE) { //ellipse(width-sdi,sdi,sdi,sdi);
    default:  
    case Circle:  
      ellipse(scx, scy, sdi, sdj); 
      break;
    case Triangle: 
      {
        float halfi=sdi/2, halfj=sdj/2; 
        triangle(scx, scy-halfj, scx-halfi, scy+halfj/2, scx+halfi, scy+halfj/2);
      } 
      break;
    case Rect: 
      {
        float halfi=sdi/2, halfj=sdj/2;
        rect(scx-halfi, scy-halfj, sdi, sdj);
      } 
      break;
    case Diamond:
      {
        float halfi=sdi/2, halfj=sdj/2;
        quad(scx-halfi, scy, scx, scy+halfj, scx+halfi, scy, scx, scy-halfj);
      } 
      break;
    }
    textAlign(RIGHT, TOP);
    text("max: "+nf(maxD), width-sdi, sdj);

    float sdimin=map(minD, minD, maxD, 2, BUBBLE_MAXDIAM);
    ellipse(width-reqLegend+sdi, sdi, sdimin, sdimin);
    textAlign(LEFT, TOP);
    text("min: "+nf(minD), width-reqLegend+sdi, sdj);
  }

  // Color scales
  float colscaleBeg=sdj+2*TEXTSIZE; //Moving vertical start position
  float scaleSide=min(256, reqLegend);
  int badge=notDivRest(PApplet.parseInt(scaleSide), 16, 32);         //print(scaleSide,"/",badge,"=");
  badge=min(PApplet.parseInt(scaleSide/badge), PApplet.parseInt(BUBBLE_MAXDIAM));//println(badge);

  //1D scale
  if ( (usedRed && !usedGreen && !usedBlue) //one component
    ||  (!usedRed && usedGreen && !usedBlue) //one component
    ||  (!usedRed && !usedGreen && usedBlue) //one component
    ||  (!usedRed && VarGreen.equals(VarBlue)) //two equal components
    ||  (!usedGreen && VarBlue.equals(VarRed)) //two equal components
    ||  (!usedBlue && VarRed.equals(VarGreen)) //two equal components
    ||  (usedRed && usedGreen && usedBlue && VarRed.equals(VarGreen) && VarGreen.equals(VarBlue) ) //three equal components
    )
  {
    fill(usedRed?255:0, usedGreen?255:0, usedBlue?255:0);
    textAlign(LEFT, TOP);
    text(VarRed+"-"+VarGreen+"-"+VarBlue, width-reqLegend, colscaleBeg);
    colscaleBeg+=TEXTSIZE;
    scaleSimple(width-reqLegend, colscaleBeg, scaleSide, scaleSide/2, 
      badge, 
      usedRed, usedGreen, usedBlue, BUBBLE_OPACITY, color(255));
    fill(255);
    textAlign(LEFT, BOTTOM);
    text(nf(min(usedRed?minRed():Float.MAX_VALUE, usedGreen?minGreen():Float.MAX_VALUE, usedBlue?minBlue():Float.MAX_VALUE)), 
      width-reqLegend, colscaleBeg+scaleSide/2);
    fill(0);                              
    textAlign(RIGHT, BOTTOM);
    text(nf(max(usedRed?maxRed():-Float.MAX_VALUE, usedGreen?maxGreen():-Float.MAX_VALUE, usedBlue?maxBlue():-Float.MAX_VALUE)), 
      width-reqLegend+scaleSide, colscaleBeg+scaleSide/2);           
    colscaleBeg+=scaleSide+TEXTSIZE;
  } else // 2 or 3D color scales
  {
    // Is it enought vertical space? 
    if (usedRed && usedBlue && usedGreen
      && (3*scaleSide+32)> height-colscaleBeg) 
    {
      scaleSide=(int)((height-colscaleBeg)/3-32);
      badge=notDivRest(PApplet.parseInt(scaleSide), 16, 32);
      badge=min(PApplet.parseInt(scaleSide/badge), PApplet.parseInt(BUBBLE_MAXDIAM));
    }

    //Block for RED x BLUE
    if (usedRed && usedBlue && !VarRed.equals(VarBlue))
    {
      fill(255);     
      textAlign(CENTER, TOP);
      text(" x ", width-reqLegend+scaleSide/2, colscaleBeg);
      fill(0, 0, 255); 
      textAlign(RIGHT, TOP); 
      text(VarBlue, width-reqLegend+scaleSide, colscaleBeg);
      fill(255, 0, 0); 
      textAlign(LEFT, TOP);  
      text(VarRed, width-reqLegend, colscaleBeg);
      colscaleBeg+=TEXTSIZE;
      float defaultGreen=0;
      if (usedGreen)
        if (VarGreen.equals(VarRed)) defaultGreen=-1;
        else if (VarGreen.equals(VarBlue)) defaultGreen=-2;
        else
        {
          int position=frameCount % (int)scaleSide; //print(position);
          defaultGreen=map(position, 0, scaleSide, 0, 255); //println("->",defaultGreen);
          fill(0, defaultGreen, 0);
          noStroke();
          triangle(width-reqLegend+position, colscaleBeg+scaleSide, width-reqLegend+position-2, colscaleBeg+scaleSide+10, 
            width-reqLegend+position+2, colscaleBeg+scaleSide+10);
          text(VarGreen+" ", width-reqLegend+position+2, colscaleBeg+scaleSide);
        } 
      scaleRedBlue(width-reqLegend, colscaleBeg, scaleSide, scaleSide, 
        badge, 
        defaultGreen, BUBBLE_OPACITY, color(255));
      fill(0);
      textAlign(LEFT, BOTTOM);
      text(nf(minRed()), width-reqLegend, colscaleBeg+scaleSide);
      textAlign(RIGHT, BOTTOM);
      text(nf(maxRed()), width-reqLegend+scaleSide, colscaleBeg+scaleSide);   
      fill(0, 128, 0);
      textAlign(CENTER, TOP);
      text(nf(minBlue()), width-reqLegend+scaleSide/2, colscaleBeg);
      textAlign(CENTER, BOTTOM);
      text(nf(maxBlue()), width-reqLegend+scaleSide/2, colscaleBeg+scaleSide);
      colscaleBeg+=scaleSide+TEXTSIZE;
    }

    //Block for BLUE x GREEN
    if (usedGreen && usedBlue && !VarGreen.equals(VarBlue))
    {
      fill(255);     
      textAlign(CENTER, TOP);
      text(" x ", width-reqLegend+scaleSide/2, colscaleBeg);
      fill(0, 0, 255); 
      textAlign(RIGHT, TOP); 
      text(VarBlue, width-reqLegend+scaleSide, colscaleBeg);
      fill(0, 255, 0); 
      textAlign(LEFT, TOP);  
      text(VarGreen, width-reqLegend, colscaleBeg);
      colscaleBeg+=TEXTSIZE;
      float defaultRed=0;
      if (usedRed)
        if (VarRed.equals(VarGreen)) defaultRed=-1;
        else if (VarRed.equals(VarBlue)) defaultRed=-2;
        else
        {
          int position=frameCount % (int)scaleSide; //print(position);
          defaultRed=map(position, 0, scaleSide, 0, 255); //println("->",defaultGreen);
          fill(defaultRed, 0, 0);
          noStroke();      
          triangle(width-reqLegend+position, colscaleBeg+scaleSide, width-reqLegend+position-2, colscaleBeg+scaleSide+10, 
            width-reqLegend+position+2, colscaleBeg+scaleSide+10);
          text(VarRed+" ", width-reqLegend+position+2, colscaleBeg+scaleSide);
        } 
      scaleGreenBlue(width-reqLegend, colscaleBeg, scaleSide, scaleSide, 
        badge, 
        defaultRed, BUBBLE_OPACITY, color(255));
      fill(0);
      textAlign(LEFT, BOTTOM);
      text(nf(minGreen()), width-reqLegend, colscaleBeg+scaleSide);
      textAlign(RIGHT, BOTTOM);
      text(nf(maxGreen()), width-reqLegend+scaleSide, colscaleBeg+scaleSide);   
      fill(128, 0, 0);
      textAlign(CENTER, TOP);
      text(nf(minBlue()), width-reqLegend+scaleSide/2, colscaleBeg);
      textAlign(CENTER, BOTTOM);
      text(nf(maxBlue()), width-reqLegend+scaleSide/2, colscaleBeg+scaleSide);
      colscaleBeg+=scaleSide+TEXTSIZE;
    }

    //Block for RED x GREEN
    if (usedRed && usedGreen && !VarGreen.equals(VarRed) )
    {
      fill(255);     
      textAlign(CENTER, TOP);
      text(" x ", width-reqLegend+scaleSide/2, colscaleBeg);
      fill(0, 255, 0); 
      textAlign(RIGHT, TOP); 
      text(VarGreen, width-reqLegend+scaleSide, colscaleBeg);
      fill(255, 0, 0); 
      textAlign(LEFT, TOP);  
      text(VarRed, width-reqLegend, colscaleBeg);
      colscaleBeg+=TEXTSIZE;
      float defaultBlue=0;
      if (usedBlue)
        if (VarBlue.equals(VarRed)) defaultBlue=-1;
        else if (VarBlue.equals(VarGreen)) defaultBlue=-2;
        else
        {
          int position=frameCount % (int)scaleSide; //print(position);
          defaultBlue=map(position, 0, scaleSide, 0, 255); //println("->",defaultGreen);
          fill(0, 0, defaultBlue);
          noStroke();
          triangle(width-reqLegend+position, colscaleBeg+scaleSide, width-reqLegend+position-2, colscaleBeg+scaleSide+10, 
            width-reqLegend+position+2, colscaleBeg+scaleSide+10);
          text(VarBlue+" ", width-reqLegend+position+2, colscaleBeg+scaleSide);
        } 

      scaleRedGreen(width-reqLegend, colscaleBeg, scaleSide, scaleSide, 
        badge, 
        defaultBlue, BUBBLE_OPACITY, color(255));  
      fill(0);
      textAlign(LEFT, BOTTOM);
      text(nf(minRed()), width-reqLegend, colscaleBeg+scaleSide);
      textAlign(RIGHT, BOTTOM);
      text(nf(maxRed()), width-reqLegend+scaleSide, colscaleBeg+scaleSide);   
      fill(0, 0, 128);
      textAlign(CENTER, TOP);
      text(nf(minGreen()), width-reqLegend+scaleSide/2, colscaleBeg);
      textAlign(CENTER, BOTTOM);
      text(nf(maxGreen()), width-reqLegend+scaleSide/2, colscaleBeg+scaleSide);
      colscaleBeg+=scaleSide+TEXTSIZE;
    }
  }
}

/// Adhoc helper.
public int notDivRest(int dividend, int  dividerMin, int dividerMax) ///< Najwiekszy dzielnik bez reszty w danym zakresie
{
  int i=dividerMax;
  for (; i>dividerMin; i--)
  {
    if (dividend % i == 0 ) return i;
  }
  return dividerMin;
}


/// An Array of Bubble objects.
Bubble[] bubbles; ///< Must be globaly visible for C++.

//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// "Bubble Visalisation" - Program do wizualizacji bąbelkowej - HumanAI,GuestXR, ISS University of Warsaw.
/// 2022 see: "https://www.researchgate.net/profile/WOJCIECH_BORKOWSKI" @author Wojciech Borkowski
//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// @brief Reading model parameters from command line.
//*////////////////////////////////////////////////////////////
public void checkCommandLine() ///< Parsing command line if available
{ 
    //extern int debug_level;
    
    //Is passing parameters possible?
    if(args==null)
    {
       //if(debug_level>0) 
       println("Command line parameters not available");
       return; //Not available!!!
    }

    if(debug_level>0)
    {
      println("args length is " + args.length);
      for(int a=0;a<args.length;a++)
          print(args[a]," ");
      println();
    }
    
    //... UTILISE PARAMETERS BELOW ...
    int Ecount=0;//Number of erros in parameters utilising
    
    for(int a=0;a<args.length;a++) // TODO - wydzielić iterpretacje komend w osobną funkcję żeby można było pisać "skrypty"
    {
      windowTitle+=args[0];//windowTitle+=args[a];
      
      String[] list = split(args[a], ':');
      if(debug_level>1)
      { 
        for(String s:list) 
          print("'"+s+"'"+' ');
        println();
      }
      

      if(list[0].equals("DRIVER") || list[0].equals("driver")) // vname as boolean set into true
      {
        //String driver=SVG; ///< Possible are: P2D, P3D, FX2D, PDF, SVG
        driver=list[1];
        println("DRIVER:",driver);
        println("Standards are as follow:", P2D, P3D, FX2D, PDF, SVG);
        if(driver.equals(SVG)) output="output.svg"; ///< for SVG
        if(driver.equals(PDF)) output="output.pdf"; ///< for PDF
      }
      else
      if(list[0].equals("output") || list[0].equals("OUTPUT")) // vname:string
      {
        //String output=""; ///< for others than PDF or SVG empty is expected
        output=list[1];
        println("OUTPUT:",output);
        println("It can be a absolute path or sole name of a file which will be located in the directory of the script!");
        println("(WARNING: Relative paths may behave very strange!");
      }
      else
      if(list[0].equals("shape") || list[0].equals("SHAPE")) // vname:string
      {
        // BubbShape DEF_BUBBLE_SHAPE=BubbShape.Rect; ///< Use string2BubbShape(String name) for conversion
        DEF_BUBBLE_SHAPE=string2BubbShape(list[1]);
        println("SHAPE of points:",DEF_BUBBLE_SHAPE);
      }
      else
      if(list[0].equals("PICTWIDTH") || list[0].equals("pictwidth")) // vname:string
      {
        //int   reqWidth=1000; ///< required width of scatter graph
        reqWidth=Integer.parseInt(list[1]);
        println("PICTWIDTH:",reqWidth);
      }
      else
      if(list[0].equals("LEGEND") || list[0].equals("legend")) // vname:string
      {
        //int   reqLegend=300; ///< required width of legend
        reqLegend=Integer.parseInt(list[1]);
        println("LEGEND width:",reqLegend);
      }
      else
      if(list[0].equals("PICTHEIGTH") || list[0].equals("pictheigth")) // vname:string
      {
        //int   reqHeight=560; ///< required hight of scatter graph
        reqHeight=Integer.parseInt(list[1]);
        println("PICTHEIGTH:",reqHeight);
      }
      else
      if(list[0].equals("STATUS") || list[0].equals("status")) // vname:string
      {
        //int   reqStatus=int(BUBBLE_VERDIAM+TEXTSIZE*7); ///< required hight of scatter bar
        reqStatus=Integer.parseInt(list[1]);
        println("STATUS height:",reqStatus);
      }
      else
      if(list[0].equals("STATUS height") || list[0].equals("maxdiam")) // vname:A
      {
         BUBBLE_OPACITY=Float.parseFloat(list[1]); //float BUBBLE_OPACITY=128;                         
         println("OPACITY of bubbles (0..255):",BUBBLE_OPACITY);                             
      }                                
      else
      if(list[0].equals("MAXDIAM") || list[0].equals("maxdiam")) // vname:A
      {
         BUBBLE_MAXDIAM=Float.parseFloat(list[1]);   //float BUBBLE_MAXDIAM=17;
         BUBBLE_VERDIAM=BUBBLE_MAXDIAM;
         println("MAXDIAM of bubbles:",BUBBLE_MAXDIAM);
      }                                
      else
      if(list[0].equals("VERDIAM") || list[0].equals("verdiam")) // vname:A
      {
         BUBBLE_VERDIAM=Float.parseFloat(list[1]);   //float BUBBLE_VERDIAM=48;
         BUBBLE_VERDIAM=BUBBLE_MAXDIAM;
         println("MAXDIAM/VERDIAM of bubbles:",BUBBLE_MAXDIAM,BUBBLE_VERDIAM);
      }                                
      else
      if(list[0].equals("x") || list[0].equals("X")) // vname:string
      {
        VarX=list[1]; //String VarX="H9";//" ambientTemp";//" meanTemp";//" StepCounter";//" ambientTemp";
        println("X:",VarX);
      }
      else
      if(list[0].equals("y") || list[0].equals("Y")) // vname:string
      {
        VarY=list[1]; //String VarY=" StepCounter";//"meanOpinion";
        println("Y:",VarY);
      }
      else
      if(list[0].equals("d") || list[0].equals("D") || list[0].equals("diameter") || list[0].equals("Diameter") ) // vname:string
      {
        VarDiam=list[1]; //String VarDiam="providersCount";//" liveCount";
        println("D:",VarDiam);
      }
      else
      if(list[0].equals("r") || list[0].equals("R") || list[0].equals("red") || list[0].equals("RED") ) // vname:string
      {
        VarRed=list[1]; //String VarRed="$REPET";
        //boolean RedLog10=true;
        if(list.length>2) RedLog10=list[2].equals("log");
        println("R:",(RedLog10?"log10(x+1) ":""),VarRed);
      }
      else
      if(list[0].equals("g") || list[0].equals("G") || list[0].equals("green") || list[0].equals("GREEN") ) // vname:string
      {
        VarGreen=list[1]; //String VarGreen="$REPET";//" StepCounter";//" meanTemp";//"$REPET";// ambientTemp";//" StepCounter";//
        //boolean GreLog10=false;
        if(list.length>2) GreLog10=list[2].equals("log");
        println("G:",(GreLog10?"log10(x+1) ":""),VarGreen);
      }
      else
      if(list[0].equals("b") || list[0].equals("B") || list[0].equals("blue") || list[0].equals("BLUE") ) // vname:string
      {
        VarBlue=list[1]; //String VarBlue="";//" StepCounter";//" meanTemp";//$REPET";//" StepCounter";
        //boolean BluLog10=true;
        if(list.length>2) BluLog10=list[2].equals("log");
        println("B:",(BluLog10?"log10(x+1) ":""),VarBlue);
      }
      else
      if(list[0].equals("CASE") || list[0].equals("case") || list[0].equals("CASENAME") || list[0].equals("casename")) // vname:string
      {
        CaseName=list[1]; //String CaseName="$REPET";
        println("CASENAME:",CaseName);
      }
      else
      //dictName
      if(list[0].equals("dictname") || list[0].equals("DICTNAME") || list[0].equals("input") || list[0].equals("INPUT")) // vname:string
      {
        dictName=list[1]; // dictName
        println("dictname:",dictName);
        println("It can be a absolute path or name of a file located in the \"data\" directory of the script!");
        println("(WARNING: Relative paths may behave very strange!");
      }
      else
      if(list[0].equals("filename") || list[0].equals("FILENAME") || list[0].equals("input") || list[0].equals("INPUT")) // vname:string
      {
        fileName=list[1]; // fileName
        println("filename:",fileName);
        println("It can be a absolute path or name of a file located in the \"data\" directory of the script!");
        println("(WARNING: Relative paths may behave very strange!");
      }
      else
      if(list[0].equals("fileoptions") || list[0].equals("FILEOPTIONS")) // vname:string
      {
        options=list[1]; //options
        println("fileoptions:",options);
        println("may contain \"header\", \"tsv\", \"csv\", or \"bin\" separated by commas");
        println("NOTE: Proper file extension work instaed of option tsv,csv option"); 
        println("EXAMPLE: \"header\" \"header,tsv\"");
      }
      else
      if(list[0].equals("HELP"))
      {
        Ecount=100;
      }
      else
      if(list[0].equals(""))
      {
        println("Empty parameter at position",a,"detected");
      }
      else
      {
        println("Unknown parameter '"+args[a]+"' detected");
        Ecount++;//OK
      }
    }
        
    if(Ecount!=0 )
    {
      if(Ecount!=100)
        println("Failed to understand",Ecount,"parameters");
      println("Need HELP???");
      println("OPTIONS:\n","\tX:s,","Y:s,","D:s,","R:s[:log],","G:s[:log],","B:s[:log],","CASENAME:s,",
                         "\n\tFILENAME:s,","or INPUT:s,","FILEOPTIONS:s,","OUTPUT:s,","DRIVER:s",
                         "\n\tPICTWIDTH:n","PICTHEIGTH:n","STATUS:n","LEGEND:n"
                         );
      exit();
    }
    
    //AKTALIZACJA FLAG
    usedName=CaseName.length()>0;
    usedX=VarX.length()>0;
    usedY=VarY.length()>0;
    usedDiam=VarDiam.length()>0;
    usedRed=VarRed.length()>0;
    usedGreen=VarGreen.length()>0;
    usedBlue=VarBlue.length()>0;
    windowTitle=CaseName+"_X:"+VarX+"_Y:"+VarY+"_D:"+VarDiam+"_R:"+VarRed+"_G:"+VarGreen+"_B:"+VarBlue;
}

String windowTitle="X:"+VarX+" Y:"+VarY+" D:"+VarDiam+" R:"+VarRed+" G:"+VarGreen+" B:"+VarBlue;  ///< Window title, optionally changed within parameters

//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// "Bubble Visalisation" - Program do wizualizacji bąbelkowej - HumanAI,GuestXR, ISS University of Warsaw.
/// 2022 see: "https://www.researchgate.net/profile/WOJCIECH_BORKOWSKI" @author Wojciech Borkowski
//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// Create a new row, so new bubble with random diameter
public void mousePressed()
{
  for (Bubble b : bubbles) 
  {
    if(b==null) continue;
    b.rollover(mouseX, mouseY,true);
  }
  /*
  TableRow row = table.addRow();
  // Set the values of that row
  row.setInt(" id",(int)random(9999));
  row.setFloat("x", mouseX);
  row.setFloat("y", mouseY);
  row.setFloat("diameter", random(40, 80));
  row.setString("name", "Blah");

  // If the table has more than 10 rows
  if (table.getRowCount() > 10) {
    // Delete the oldest row
    table.removeRow(0);
  }

  // Writing the CSV back to the same file
  saveTable(table, "data/testNew.csv");
  filename="testNew.csv";
  // And reloading it
  loadData();
  makeBubbles();
  */
}

public void keyPressed()
{
  if(key==' ')
  {
    if(output.length()==0)
      output="X_"+VarX+"_Y_"+VarY+"_D_"+VarDiam+"_R_"+(RedLog10?"log":"")+VarRed+"_G_"+(GreLog10?"log":"")+VarGreen+"_B_"+(BluLog10?"log":"")+VarBlue+"_cs_"+CaseName;
    save(output+forceOutputExt);
    println("Saved output file:",output);
  }
}

//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// "Bubble Visalisation" - Program do wizualizacji bąbelkowej - HumanAI,GuestXR, ISS University of Warsaw.
/// 2022 see: "https://www.researchgate.net/profile/WOJCIECH_BORKOWSKI" @author Wojciech Borkowski
//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Color scales visualisations.
//*////////////////////////////

/// 1D scale from black to color maintained from 1 to 3 equal color components.
public void scaleSimple(float startX,float startY,float swidth,float sheight,float step,boolean useRed,boolean useGreen,boolean useBlue,float alpha,int background) ///< Red x Blue scale
{
  if(background!=0) 
  {
    fill(background);noStroke();
    rect(startX,startY,swidth,sheight);
  }

  for(int x=(int)startX;x<startX+swidth;x+=step)
  {
    float col=map(x,startX,startX+swidth,0,255);
    fill(useRed?col:0,useGreen?col:0,useBlue?col:0,alpha);rect(x,startY,step,sheight);
  }
}

/// 2D scale maintained from 2 color components.
public void scaleRedBlue(float startX,float startY,float swidth,float sheight,float step,float defaultGreen,float alpha,int background) ///< Red x Blue scale
{
  if(background!=0) 
  {
    fill(background);noStroke();
    rect(startX,startY,swidth,sheight);
  }
  
  float def=defaultGreen;
  for(int x=(int)startX;x<startX+swidth;x+=step)
  {
    float red=map(x,startX,startX+swidth,0,255);
    
    if(defaultGreen==-1) def=red;
    
    for(int y=(int)startY;y<startY+sheight;y+=step)
    {
      float blue=map(y,startY,startY+sheight,0,255);
      
      if(defaultGreen==-2) def=blue;
      
      //stroke(red,def,blue,alpha);point(x,y);
      fill(red,def,blue,alpha);rect(x,y,step,step);
    }
  }
}

/// 2D scale maintained from 2 color components.
public void scaleGreenBlue(float startX,float startY,float swidth,float sheight,float step,float defaultRed,float alpha,int background) ///< Green x Blue scale
{
  if(background!=0) 
  {
    fill(background);noStroke();
    rect(startX,startY,swidth,sheight);
  }
  
  float def=defaultRed;
  for(int x=(int)startX;x<startX+swidth;x+=step)
  {
    float green=map(x,startX,startX+swidth,0,255);
    
    if(defaultRed==-1) def=green;
    
    for(int y=(int)startY;y<startY+sheight;y+=step)
    {
      float blue=map(y,startY,startY+sheight,0,255);
  
      if(defaultRed==-2) def=blue;
          
      //stroke(def,green,blue,alpha);point(x,y);
      fill(def,green,blue,alpha);rect(x,y,step,step);
    }
  }
}

/// 2D scale maintained from 2 color components.
public void scaleRedGreen(float startX,float startY,float swidth,float sheight,float step,float defaultBlue,float alpha,int background) ///< Red x Green scale
{
  if(background!=0) 
  {
    fill(background);noStroke();
    rect(startX,startY,swidth,sheight);
  }
  
  float def=defaultBlue;
  for(int x=(int)startX;x<startX+swidth;x+=step)
  {
    float red=map(x,startX,startX+swidth,0,255);
    
    if(defaultBlue==-1) def=red;
    
    for(int y=(int)startY;y<startY+sheight;y+=step)
    {
      float green=map(y,startY,startY+sheight,0,255);
      
      if(defaultBlue==-2) def=green;
      
      //stroke(red,green,def,alpha);point(x,y);
      fill(red,green,def,alpha);rect(x,y,step,step);
    }
  }
}

//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// "Bubble Visalisation" - Program do wizualizacji bąbelkowej - HumanAI,GuestXR, ISS University of Warsaw.
/// 2022 see: "https://www.researchgate.net/profile/WOJCIECH_BORKOWSKI" @author Wojciech Borkowski
//*////////////////////////////////////////////////////////////////////////////////////////////////////////////
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "BubbleVisual" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
