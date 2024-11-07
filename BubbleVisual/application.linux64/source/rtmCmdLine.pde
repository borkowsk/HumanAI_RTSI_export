/// @brief Reading model parameters from command line.
//*////////////////////////////////////////////////////////////
void checkCommandLine() ///< Parsing command line if available
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
