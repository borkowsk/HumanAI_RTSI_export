/// @file DataIntegrator.java
/// @brief Parameter space integrator for RTSI
/// @details
///       Integrator for parameter space exploration provided in many data/log/out/par files
///       based on example from: 
///       https://forum.processing.org/two/discussion/1747/reading-filesnames-from-a-folder
//*////////////////////////////////////////////////////////////////////////////////////////

import java.io.File;

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
void setup() 
{
  size(300,50);
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
void draw() 
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
void exit()
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
