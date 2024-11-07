//import java.io.FileReader;
//import java.io.BufferedReader;
import java.text.*; //SimpleTextFormat;

void initDictionary()
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

String  dictionaryInfo(String keyStr)
{
  String out=variablesInformation.get(keyStr);
  if(out!=null)
      return out;
  else
      return "???";
}

void loadDictionary(String filename)
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

void saveDictionary(String corename)
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
