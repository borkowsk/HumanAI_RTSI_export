/// A Bubble class
//*//////////////////////////////////////

enum  BubbShape { Circle,Rect,Diamond,Triangle }
BubbShape DEF_BUBBLE_SHAPE=BubbShape.Rect; ///< Use string2BubbShape(String name) for conversion

BubbShape string2BubbShape(String name)
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

float minRed() { return RedLog10? 0 :_minRed; }
float maxRed() { return RedLog10? (float)Math.log10(_maxRed+1):_maxRed; }

float minBlue() { return BluLog10? 0 :_minBlue; }
float maxBlue() { return BluLog10? (float)Math.log10(_maxBlue+1):_maxBlue; }

float minGreen() { return GreLog10? 0 :_minGreen; }
float maxGreen() { return GreLog10? (float)Math.log10(_maxGreen+1):_maxGreen; }
  
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
  void rollover(float px, float py,boolean withPrint) 
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
  void display() 
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
void makeBubbles() 
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
