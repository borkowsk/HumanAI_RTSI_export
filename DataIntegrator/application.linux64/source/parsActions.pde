/// job for integrating *.par files from RTSI.
//*///////////////////////////////////////////

void forParFileAction(String filename,File theFile) ///< Must be globally visible.
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
void moveData(TableRow target,TableRow source) ///< Must be globally visible for C++.
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
