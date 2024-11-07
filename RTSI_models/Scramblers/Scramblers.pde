/*
---------- Forwarded message ---------
From: Pitt, Jeremy V <j.pitt@imperial.ac.uk>
Date: Tue, Sep 1, 2020 at 3:03 AM
Subject: Re: Algorithm
To: Andrzej Nowak <andrzejn232@gmail.com>


Dear Andrzej

Sorry for the lack of clarity in the Alife paper.

Here is how I did it. I can also send the Polog code if he “speaks” Prolog.

The basic idea is something I dragged out of astrophysics and surface temperatures of stars.

So each scrambler has three properties: surface temperature (ST), intensity (Ixy) and location (X,Y).
Both ST and Ixy are random numbers in [20,40].

Then the intensity Ix1y1 at another location (X1,Y1) is

Ix1y1 = Ixy *  (1 / sqrt( abs( (X,Y), (X1,Y1) ) ) )

The temperature variation (TV)  at Ix1y1  is then 

TV = (Ix1y1 / Ixy) * ST

The total TV at the location  is the sum of all TVs for all  scramblers.

There was a hack I needed because in fact I used an inverse quadratic rather than an inverse square
because otherwise it was producing intensities that would take the values out of my assumed rang
for the ambient temperature [-100,100] but I am fairly sure this is equivalent to just lowering the range
of the random number generated for Ixy, or doubling the grid size, so no big deal.

Please let me know if this helps.
Best wishes
Jeremy 
*/

final boolean Euclid=false;
final int N=8; //Number of scramblers 4-8
final int side=20;//Side of area

int mult=1;//Visualisation multiplier

class scrambler
{
  float ST;
  float Ixy;
  float X,Y;
  scrambler()//constructor
  {
     ST=random(20,40);
     if(random(1)<0.5) 
        ST=-ST;
     Ixy=random(20,40);
     X=random(0,side);
     Y=random(0,side);
  }
};


scrambler[] Scrambs;

float fn(float X1,float Y1,float X2,float Y2)
{
  float dX=abs(X1-X2);
  if(Euclid) dX*=dX;//use if Euclidean distance
  
  float dY=abs(Y1-Y2);
  if(Euclid) dY*=dY;//use if Euclidean distance
  
  if(dX+dY > 0 )
    return sqrt(dX+dY);
  else
    return 0;
}

float calculate(scrambler[] Scrs,float X,float Y)
{
  float val=0;
  for(scrambler s:Scrs)
  {
    //Ixy *  //(1 / sqrt( abs( (X,Y), (X1,Y1) ) ) );
    float d=fn(X,Y,s.X,s.Y);
    float Ix1y1 = s.Ixy * (d>0?1/d:1);
    float TV = (Ix1y1 / s.Ixy) * s.ST;  
    val+=TV;
  }
  return val;
}

void setup()
{
  size(500,500);
  mult=width/side;
  frameRate(10);
  Scrambs=new scrambler[N];
  for(int i=0;i<N;i++)
    Scrambs[i]=new scrambler();
}

void draw()
{
  noStroke();
  for(int i=0;i<side;i++)
    for(int j=0;j<side;j++)
    {
      float value=calculate(Scrambs,i,j);
      value=map(value,-100,100,0,255);

      if(mult==1)
      {
        //stroke(255-value,255-value,value);
        stroke(value);
        point(i,j);
      }
      else
      {
        fill(value);
        rect(i*mult,j*mult,mult,mult);
      }
    }
}
