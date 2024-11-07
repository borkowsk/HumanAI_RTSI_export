/// Gets the float value following the marker in the text string. 
/// @note Dot allowed, 'e'/'E' not allowed.
float getValueAfterMarker(String source,String marker) ///< Must be globally visible for C++.
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
float getValueAfterRegex(String source,String regex) ///< Must be globally visible.
{
  String[] matches = match(source,regex); //<>//
  
  if(matches==null) return -9999;
  
  String marker=matches[0]; print("FOUND",regex,"as",marker); //DEBUG
  
  float ret=getValueAfterMarker(source,marker); println(' ',ret);
  
  return ret; //<>//
}

//*//////////////////////////////////////
// ISS University of Warsaw.
/// @author Wojciech Borkowski
//*//////////////////////////////////////
