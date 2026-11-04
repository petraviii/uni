import random

#gia to 8x8 paixnidi

paixnidia = 0      #metraei ta paixnidia 
score_aspros = 0   #paixndia pou exei nikisei o aspros 
score_mavros = 0   #paixndia pou exei nikisei o mavros 

while paixnidia < 100  : #loop paixnidion
    
    #midenizo ton pinaka stin arxi 
    col0 = [0,0,0,0,0,0,0,0]
    col1 = [0,0,0,0,0,0,0,0]
    col2 = [0,0,0,0,0,0,0,0]
    col3 = [0,0,0,0,0,0,0,0]
    col4 = [0,0,0,0,0,0,0,0]
    col5 = [0,0,0,0,0,0,0,0]
    col6 = [0,0,0,0,0,0,0,0]
    col7 = [0,0,0,0,0,0,0,0]

    matrix = [col0,col1,col2,col3,col4,col5,col6,col7]
    
    gameend = False  #vlepei an to kathe paixnidi exei teliosei
    turn = 0         #turns to xrisimopoio wste na vlepo poios paizei kathe fora 

    #kano generate tis arxikes theseis 
    x1 = random.randint(0,7)
    y1 = random.randint(0,7)
    x2 = random.randint(0,7)
    y2 = random.randint(0,7)

    while x1 == x2 and y1 == y2 : #elegxw an einai tyxon stin idia thesi tote apla  
        x1 = random.randint(0,7)  #kano pali generate random to ena pioni 
        y1 = random.randint(0,7)

    matrix[x1][y1] = 1 # 1 = aspros pyrgos 
    matrix[x2][y2] = 2 # 2 = mavros aksiomatikos 


    while gameend == False and paixnidia <= 100 : #loop gyrwn
        

        if turn % 2 == 1 :  #an turn perittos tote paizei o aspros 
             
            for i in [0,1,2,3,4,5,6,7] : #vrisko tis theseis twn pioniwn
                for j in [0,1,2,3,4,5,6,7] :
                    if matrix[i][j] == 1 :
                        seira1 = i
                        stili1 = j
            
                    if matrix[i][j] == 2 :
                        seira2 = i
                        stili2 = j
            
            if seira1 == seira2 or stili1 == stili2 : #an vriskete o aksiomatikos se idia seira h idia 
                gameend = True                        #stili me ton pyrgo tote to paixnidi teleionei 
                score_aspros = score_aspros + 1 
            else:
                x = random.randint(1,2)
                if x == 1 : #allazo thesi stin idia stili 
                    y = random.randint(0,7)

                    while y == seira1 : ##oste na min meinei stin idia thesi
                        y = random.randint(0,7)  

                    matrix[y][stili1] = 1
                    matrix[seira1][stili1] = 0 #kano tin palia thesi 0 diladi keni 
                    
                else: #allazo thesi stin idia seira
                    y = random.randint(0,7)

                    while y == stili1 : #oste na min meinei stin idia thesi
                        y = random.randint(0,7) 

                    matrix[seira1][y] = 1
                    matrix[seira1][stili1] = 0
        
        else: #an turns artios tote paizei o mavros 
            
            for i in [0,1,2,3,4,5,6,7] : #vrisko tis theseis twn pioniwn
                for j in [0,1,2,3,4,5,6,7] :
                    if matrix[i][j] == 1 :
                        seira1 = i
                        stili1 = j 
                    
                    if matrix[i][j] == 2 :
                        seira2 = i
                        stili2 = j
                        
            if ( stili2 + seira2 == stili1 + seira1 ) or ( seira2 - stili2 == seira1 - stili1 ) : #koitaw an o aksiomatikos mporei na nikisei 
                score_mavros = score_mavros + 1                                                   
                gameend = True
            else : # an den nikaei tote ton kounaw se mia tyxaia thesi 
                
                moved = False
                while moved == False :
                    
                    x = random.randint(0,7)
                    y = random.randint(0,7)
                    
                    while x == seira2 and y == stili2 : #oste na min meinei stin idia thesi 
                        x = random.randint(0,7)
                        y = random.randint(0,7)
                        
                    if ( stili2 + seira2 == y + x ) or ( seira2 - stili2 == x - y ) :
                        matrix[x][y] = 2
                        matrix[seira2][stili2] = 0 
                        moved = True   
        
        #for i in [0,1,2,3,4,5,6,7] : #print ton pinaka gia na mporo na do thn katastasi 
            #print ( matrix[i][0],matrix[i][1],matrix[i][2],matrix[i][3],matrix[i][4],matrix[i][5],matrix[i][6],matrix[i][7] ) 
            #print( "\n" , "arithmos turn: " , turn , "\n" )
        
        turn = turn + 1
        
        if gameend == True :
            paixnidia = paixnidia + 1 

print("paixtikan tosa paixnidia 8x8 : " , paixnidia )
print("to score tou asproy pyrgou htan : " , score_aspros )
print("to score tou mavrou aksiomatikou htan : " , score_mavros )

#gia to 8x7 paixnidi

paixnidia2 = 0      #metraei ta paixnidia 
score_aspros2 = 0   #paixndia pou exei nikisei o aspros 
score_mavros2 = 0   #paixndia pou exei nikisei o mavros 

while paixnidia2 < 100  : #loop paixnidion
    
    #midenizo ton pinaka stin arxi 
    col02 = [0,0,0,0,0,0,0]
    col12 = [0,0,0,0,0,0,0]
    col22 = [0,0,0,0,0,0,0]
    col32 = [0,0,0,0,0,0,0]
    col42 = [0,0,0,0,0,0,0]
    col52 = [0,0,0,0,0,0,0]
    col62 = [0,0,0,0,0,0,0]
    col72 = [0,0,0,0,0,0,0]

    matrix2 = [col02,col12,col22,col32,col42,col52,col62,col72]
    
    gameend2 = False  #vlepei an to kathe paixnidi exei teliosei
    turn2 = 0         #turns to xrisimopoio wste na vlepo poios paizei kathe fora 

    #kano generate tis arxikes theseis 
    x12 = random.randint(0,7)
    y12 = random.randint(0,6)
    x22 = random.randint(0,7)
    y22 = random.randint(0,6)

    while x12 == x22 and y12 == y22 : #elegxw an einai tyxon stin idia thesi tote apla  
        x12 = random.randint(0,7)  #kano pali generate random to ena pioni 
        y12 = random.randint(0,6)

    matrix2[x12][y12] = 1 # 1 = aspros pyrgos 
    matrix2[x22][y22] = 2 # 2 = mavros aksiomatikos 


    while gameend2 == False and paixnidia2 <= 100 : #loop gyrwn
        

        if turn2 % 2 == 1 :  #an turn perittos tote paizei o aspros 
             
            for i in [0,1,2,3,4,5,6,7] : #vrisko tis theseis twn pioniwn
                for j in [0,1,2,3,4,5,6] :
                    if matrix2[i][j] == 1 :
                        seira12 = i
                        stili12 = j
            
                    if matrix2[i][j] == 2 :
                        seira22 = i
                        stili22 = j
            
            if seira12 == seira22 or stili12 == stili22 : #an vriskete o aksiomatikos se idia seira h idia 
                gameend2 = True                        #stili me ton pyrgo tote to paixnidi teleionei 
                score_aspros2 = score_aspros2 + 1 
            else:
                x2 = random.randint(1,2)
                if x2 == 1 : #allazo thesi stin idia stili 
                    y2 = random.randint(0,7)

                    while y2 == seira12 : ##oste na min meinei stin idia thesi
                        y2 = random.randint(0,7)  

                    matrix2[y2][stili12] = 1
                    matrix2[seira12][stili12] = 0 #kano tin palia thesi 0 diladi keni 
                    
                else: #allazo thesi stin idia seira
                    y2 = random.randint(0,6)

                    while y2 == stili12 : #oste na min meinei stin idia thesi
                        y2 = random.randint(0,6) 

                    matrix2[seira12][y2] = 1
                    matrix2[seira12][stili12] = 0
        
        else: #an turns artios tote paizei o mavros 
            
            for i in [0,1,2,3,4,5,6,7] : #vrisko tis theseis twn pioniwn
                for j in [0,1,2,3,4,5,6] :
                    if matrix2[i][j] == 1 :
                        seira12 = i
                        stili12 = j 
                    
                    if matrix2[i][j] == 2 :
                        seira22 = i
                        stili22 = j
                        
            if ( stili22 + seira22 == stili12 + seira12 ) or ( seira22 - stili22 == seira12 - stili12 ) : #koitaw an o aksiomatikos mporei na nikisei 
                score_mavros2 = score_mavros2 + 1                                                   
                gameend2 = True
            else : # an den nikaei tote ton kounaw se mia tyxaia thesi 
                
                moved2 = False
                while moved2 == False :
                    
                    x2 = random.randint(0,7)
                    y2 = random.randint(0,6)
                    
                    while x2 == seira22 and y2 == stili22 : #oste na min meinei stin idia thesi 
                        x2 = random.randint(0,7)
                        y2 = random.randint(0,6)
                        
                    if ( stili22 + seira22 == y2 + x2 ) or ( seira22 - stili22 == x2 - y2 ) :
                        matrix2[x2][y2] = 2
                        matrix2[seira22][stili22] = 0 
                        moved2 = True   
        
        
        turn2 = turn2 + 1
        
        if gameend2 == True :
            paixnidia2 = paixnidia2 + 1 

print("paixtikan tosa paixnidia 8x7 : " , paixnidia2 )
print("to score tou asproy pyrgou htan : " , score_aspros2 )
print("to score tou mavrou aksiomatikou htan : " , score_mavros2 )

#gia to 7x7 paixnidi

paixnidia3 = 0      #metraei ta paixnidia 
score_aspros3 = 0   #paixndia pou exei nikisei o aspros 
score_mavros3 = 0   #paixndia pou exei nikisei o mavros 

while paixnidia3 < 100  : #loop paixnidion
    
    #midenizo ton pinaka stin arxi 
    col03 = [0,0,0,0,0,0,0]
    col13 = [0,0,0,0,0,0,0]
    col23 = [0,0,0,0,0,0,0]
    col33 = [0,0,0,0,0,0,0]
    col43 = [0,0,0,0,0,0,0]
    col53 = [0,0,0,0,0,0,0]
    col63 = [0,0,0,0,0,0,0]

    matrix3 = [col03,col13,col23,col33,col43,col53,col63]
    
    gameend3 = False  #vlepei an to kathe paixnidi exei teliosei
    turn3 = 0         #turns to xrisimopoio wste na vlepo poios paizei kathe fora 

    #kano generate tis arxikes theseis 
    x13 = random.randint(0,6)
    y13 = random.randint(0,6)
    x23 = random.randint(0,6)
    y23 = random.randint(0,6)

    while x13 == x23 and y13 == y23 : #elegxw an einai tyxon stin idia thesi tote apla  
        x13 = random.randint(0,6)  #kano pali generate random to ena pioni 
        y13 = random.randint(0,6)

    matrix3[x13][y13] = 1 # 1 = aspros pyrgos 
    matrix3[x23][y23] = 2 # 2 = mavros aksiomatikos 


    while gameend3 == False and paixnidia3 <= 100 : #loop gyrwn
        

        if turn3 % 2 == 1 :  #an turn perittos tote paizei o aspros 
             
            for i in [0,1,2,3,4,5,6] : #vrisko tis theseis twn pioniwn
                for j in [0,1,2,3,4,5,6] :
                    if matrix3[i][j] == 1 :
                        seira13 = i
                        stili13 = j
            
                    if matrix3[i][j] == 2 :
                        seira23 = i
                        stili23 = j
            
            if seira13 == seira23 or stili13 == stili23 : #an vriskete o aksiomatikos se idia seira h idia 
                gameend3 = True                        #stili me ton pyrgo tote to paixnidi teleionei 
                score_aspros3 = score_aspros3 + 1 
            else:
                x3 = random.randint(1,2)
                if x3 == 1 : #allazo thesi stin idia stili 
                    y3 = random.randint(0,6)

                    while y3 == seira13 : ##oste na min meinei stin idia thesi
                        y3 = random.randint(0,6)  

                    matrix3[y3][stili13] = 1
                    matrix3[seira13][stili13] = 0 #kano tin palia thesi 0 diladi keni 
                    
                else: #allazo thesi stin idia seira
                    y3 = random.randint(0,6)

                    while y3 == stili13 : #oste na min meinei stin idia thesi
                        y3 = random.randint(0,6) 

                    matrix3[seira13][y3] = 1
                    matrix3[seira13][stili13] = 0
        
        else: #an turns artios tote paizei o mavros 
            
            for i in [0,1,2,3,4,5,6] : #vrisko tis theseis twn pioniwn
                for j in [0,1,2,3,4,5,6] :
                    if matrix3[i][j] == 1 :
                        seira13 = i
                        stili13 = j 
                    
                    if matrix3[i][j] == 2 :
                        seira23 = i
                        stili23 = j
                        
            if ( stili23 + seira23 == stili13 + seira13 ) or ( seira23 - stili23 == seira13 - stili13 ) : #koitaw an o aksiomatikos mporei na nikisei 
                score_mavros3 = score_mavros3 + 1                                                   
                gameend3 = True
            else : # an den nikaei tote ton kounaw se mia tyxaia thesi 
                
                moved3 = False
                while moved3 == False :
                    
                    x3 = random.randint(0,6)
                    y3 = random.randint(0,6)
                    
                    while x3 == seira23 and y3 == stili23 : #oste na min meinei stin idia thesi 
                        x3 = random.randint(0,6)
                        y3 = random.randint(0,6)
                        
                    if ( stili23 + seira23 == y3 + x3 ) or ( seira23 - stili23 == x3 - y3 ) :
                        matrix3[x3][y3] = 2
                        matrix3[seira23][stili23] = 0 
                        moved3 = True   
        
        
        turn3 = turn3 + 1
        
        if gameend3 == True :
            paixnidia3 = paixnidia3 + 1 

print("paixtikan tosa paixnidia 7x7 : " , paixnidia3 )
print("to score tou asproy pyrgou htan : " , score_aspros3 )
print("to score tou mavrou aksiomatikou htan : " , score_mavros3 )