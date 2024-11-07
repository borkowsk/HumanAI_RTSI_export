/// Program do wizualizacji bąbelkowej
/// Rozwinięty z przykładu do Processingu:
///   "Loading Tabular Data * by Daniel Shiffman." 
///   https://processing.org/examples/loadsavetable.html
import processing.svg.*; ///< https://processing.org/reference/libraries/svg/index.html
import processing.pdf.*; ///< https://processing.org/reference/libraries/pdf/index.html

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
int   reqStatus=int(BUBBLE_VERDIAM+TEXTSIZE*7); ///< required hight of scatter bar
color LEGEND_BACKGROUND=color( 144,111,144 );

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

void setup()
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

void draw() 
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

void exit()
{
  println("ALL DONE");
  super.exit();
}

void fillStatus()
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
