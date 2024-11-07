/// @brief ADDITIONAL VARIABLES ARE MADE HERE.
/// @details We add here two variables not calculated currently in the simulation program:
/// *   mnSensAmbDiff!  
///       `abs(meanSensors-ambientTemp)`: The difference between the agents' sensors and the ambient temperature (in step or its averaging from simulation)
/// *   ratioRTSI2WoC  
///       `mnOpAmbDiff/mnSensAmbDiff`: ratio of RTSI opinion error to Helbings like 'wisdom of crowd' error (in step or its averaging from simulation)
///
void  makeAdditionalVariables(Table srcTable)
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
  
  for(int r=0;r<rcout;r++) //<>//
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
  println("Additional Variables Ready\n"); //<>//
}

//*//////////////////////////////////////
// ISS University of Warsaw.
/// @author Wojciech Borkowski
//*//////////////////////////////////////
