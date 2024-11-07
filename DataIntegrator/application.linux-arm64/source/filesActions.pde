// Specific job implementation (RTSI)
//*//////////////////////////////////////
/// "RTSIsc1.49iRnd0000015151_Ne.4Si50_Cr.0Bi00.00-50SBi03.00-15nA400Am0.0Sm0.025mT0.001No0Al0.1Be0eT0eA800eS0eFS0eFa0.par"

/// Markers for variable spanning the parameter space
String firstMarker="Bi";        ///< Marker of the first variable.
String secondMarker="0Bi[-+1234567890.]+-";  ///< Marker of the second variable. May be regular expression
String thirdMarker="SBi";       ///< Marker of the third variable. Rarery used.

Table table; ///< A Table object

void initialiseAction(String path,File dir) ///< Must be globally visible for C++.
{
  println("\nStarting job for: ",path);
  table=new Table();
} 

void finaliseAction(String path) ///< Must be globally visible for C++.
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

void forDataFileAction(String filename,File theFile) ///< Must be globally visible for C++.
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
void makeMeanValuesFrom(TableRow destRow,Table srcTable)  ///< Must be globally visible for C++.
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
void makeHeaderFrom(Table destTable,Table srcTable)  ///< Must be globally visible.
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
