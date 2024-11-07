#!/bin/bash
#FOR filename: BixSBixSi.csv (Rows: 561 Colums: 45)
#Column names (case sensitive): 
#'filename' 'Bi' 'SBi' 'Si' '$REPET' 'StepCounter' 
#'ambientTemp' 'meanTemp' 'meanSensors' 
#'meanOpinion' 'sdevOpinion' 'minOpinion' 'maxOpinion' 'meanReliability' 
#'mnOpinErr!' 'mnSensErr!' 'mnOpSensDiff!' 'mnOpAmbDiff!' 
#'providersCount' 'cntProviders' 'newProviders' 'delProviders' 'self_net' 
#'meanDistSelf' 'meanDistNet' 
#'liveCount' 
#'link_up' 'link_dw' 'sensor_up' 'sensor_dw' 
#'howManyLinks' 'Ambient_waves_numb' 'Scrambs_numb' 'HistTooLow' 'H0' 'H1' 'H2' 'H3' 'H4' 'H5' 'H6' 'H7' 'H8' 'H9' 'HistTooHig'

#DRIVER="DRIVER:processing.svg.PGraphicsSVG"
DRIVER="DRIVER:processing.pdf.PGraphicsPDF"
#OUT_EXT=".svg"
OUT_EXT=".pdf"

argc=$#
required=2

if [ $argc -ne $required ] 
then
  echo "USAGE:"
  echo "$0 inputFileName outputSuffix"
  exit 1
fi


inInf="$2"

./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:ambientTemp  OUTPUT:00_ambientTemp${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:meanTemp     OUTPUT:01_meanTemp${inInf}${OUT_EXT}         >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:meanSensors  OUTPUT:02_meanSensors${inInf}${OUT_EXT}   >> outscr.txt
echo -e '\r03'

./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" B:meanOpinion  OUTPUT:03_meanOpinion${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" B:sdevOpinion  OUTPUT:04_sdevOpinion${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" G:minOpinion B:maxOpinion OUTPUT:05_minmaxOpinion${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" B:meanReliability         OUTPUT:06_meanReliability${inInf}${OUT_EXT}   >> outscr.txt
echo -e '\r06'

./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" 'G:mnOpinErr!'         OUTPUT:07_mnOpinErr${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" 'G:mnSensErr!'         OUTPUT:08_mnSensErr${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" 'G:mnOpSensDiff!'      OUTPUT:09_mnOpSensDiff${inInf}${OUT_EXT}   >> outscr.txt
echo -e '\r09'

./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER"                    'G:mnOpAmbDiff!'   OUTPUT:10AmnOpAmbDiff${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" 'R:mnSensAmbDiff!'                    OUTPUT:10BmnSensAmbDiff${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "B:" "B:" "$DRIVER" 'R:mnSensAmbDiff!' 'G:mnOpAmbDiff!'   OUTPUT:10CmnSensAmbDiff-mnOpAmbDiff${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" 'R:ratioRTSI2WoC'  'B:ratioRTSI2WoC'  OUTPUT:10DratioRTSI2WoC${inInf}${OUT_EXT}   >> outscr.txt 
echo -e '\r10'

./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:providersCount G:cntProviders        OUTPUT:11_providersCount${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:newProviders G:delProviders          OUTPUT:12_newdelProviders${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:self_net G:self_net B:self_net       OUTPUT:13_self_net${inInf}${OUT_EXT}   >> outscr.txt
echo -e '\r13'

./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" B:meanDistSelf         OUTPUT:14_meanDistSelf${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" B:meanDistNet          OUTPUT:15_meanDistNet${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" B:meanDistNet R:meanDistSelf OUTPUT:16_meanDistNetSelf${inInf}${OUT_EXT}   >> outscr.txt
echo -e '\r16'

./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:link_up      OUTPUT:17_link_up${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:link_dw      OUTPUT:18_link_dw${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:link_up B:link_dw OUTPUT:19_link_updw${inInf}${OUT_EXT}   >> outscr.txt
echo -e '\r19'

./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:sensor_up    OUTPUT:20_sensor_up${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:sensor_dw    OUTPUT:21_sensor_dw${inInf}${OUT_EXT}   >> outscr.txt
./Bubbler "filename:$1" "D:" "R:" "G:" "B:" "$DRIVER" R:sensor_up B:sensor_dw OUTPUT:22_sensor_updw${inInf}${OUT_EXT}   >> outscr.txt
echo -e '\r22'


