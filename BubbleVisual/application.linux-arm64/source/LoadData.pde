/// @brief Load CSV file into a Table object.
/// "header" option indicates the file has a header row
/// WARNING! First name have allways leading space!
void loadData(String filename,String options)
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
