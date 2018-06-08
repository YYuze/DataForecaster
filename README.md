# DataProcessorWithContour
Generator a contour with some data      
There are four steps you need to do:      
step1 Put some data you need process in a .csv file,default:polar coordinates(angle,length,value)     
step2 Use DataSpreader to spread data full of your coordinate system.       
step3 use ConrecMesher to mesh your coordinate.           
step4 use ContourRender to get your contour!      

NOTICE:               
1.DataSpreader: 
Each data on the map is consist of two WEIGHTs,one SHAKE,and one ORIGINAL DATA  
Consider [0.97^distance] as position WEIGHT,[0.999^spreadRadium] as spread WEIGHT 
SHAKE is a random Integer between -5~0  
ORIGINAL DATA is read from .csv 

2.ConrecMesher  
Use CONREC algorithm Written by Paul Bourke 
http://paulbourke.net/papers/conrec/  


by the way:           
This is the bachelor thesis of me!            
I almost fail in it and that means I may not graduate from the BULL SHIT college!!!         
But now ! I made it! I m sooooooo happy!!!        
