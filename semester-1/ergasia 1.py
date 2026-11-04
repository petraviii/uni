
import random
sum_turns = 0
for i in range(100):
    turns=0
    col1 = [0,0,0]
    col2 = [0,0,0]
    col3 = [0,0,0]
    matrix = [col1,col2,col3]
    game_end = False
    while game_end == False:
        ring_placed = False
    
        while ring_placed == False:
         seira = random.randint(0,2)
         stili = random.randint(0,2)
         megethos = random.randint(1,3)
         if matrix[seira][stili] == 0 :
          matrix[seira][stili] = megethos
          ring_placed = True
         else :
            if matrix[seira][stili] != megethos :
              matrix[seira][stili] = megethos
              ring_placed = True
        
        
        
        
        if ( matrix[0][0] == matrix[0][1] == matrix[0][2] ) and matrix[0][0] != 0 :
            game_end = True
        elif ( matrix[1][0] == matrix[1][1] == matrix[1][2] ) and matrix[1][0] != 0 :
            game_end = True 
        elif ( matrix[2][0] == matrix[2][1] == matrix[2][1] ) and matrix[2][0] != 0 :
            game_end = True     
        elif ( matrix[0][0] == matrix[1][0] == matrix[2][0] ) and matrix[0][0] != 0 :
            game_end = True
        elif ( matrix[0][1] == matrix[1][1] == matrix[2][1] ) and matrix[0][1] != 0 :
            game_end = True
        elif ( matrix[0][2] == matrix[1][2] == matrix[2][2] ) and matrix[0][2] != 0 :
            game_end = True
        elif ( matrix[0][0] == matrix[1][1] == matrix[2][2] ) and matrix[0][0] != 0 :
            game_end = True
        elif ( matrix[0][2] == matrix[1][1] == matrix[2][0] ) and matrix[0][2] != 0 :
            game_end = True
        elif ( matrix[0][0] == 1 and matrix[0][1] == 2 and matrix[0][2] == 3 ) or ( matrix[0][0] == 3 and matrix[0][1] == 2 and matrix[0][2]== 1 ) :
            game_end = True
        elif ( matrix[1][0] == 1 and matrix[1][1] == 2 and matrix[1][2] == 3 ) or ( matrix[1][0] == 3 and matrix[1][1] == 2 and matrix[1][2] == 1 ) :
            game_end = True 
        elif ( matrix[2][0] == 1 and matrix[2][1] == 2 and matrix[2][1] == 3 ) or ( matrix[2][0] == 3 and matrix[2][1] == 2 and matrix[2][1] == 1 ) :
            game_end = True     
        elif ( matrix[0][0] == 1 and matrix[1][0] == 2 and matrix[2][0] == 3 ) or ( matrix[0][0] == 3 and matrix[1][0] == 2 and matrix[2][0] == 1 ) :
            game_end = True
        elif ( matrix[0][1] == 1 and matrix[1][1] == 2 and matrix[2][1] == 3 ) or ( matrix[0][1] == 3 and matrix[1][1] == 2 and matrix[2][1] == 1 ) :
            game_end = True
        elif ( matrix[0][2] == 1 and matrix[1][2] == 2 and matrix[2][2] == 3 ) or ( matrix[0][2] == 3 and matrix[1][2] == 2 and matrix[2][2] == 1 ) :
            game_end = True
        elif ( matrix[0][2] == 1 and matrix[1][2] == 2 and matrix[2][2] == 3 ) or ( matrix[0][2] == 3 and matrix[1][2] == 2 and matrix[2][2] == 1 ) :
            game_end = True
        elif ( matrix[0][2] == 1 and matrix[1][1] == 2 and matrix[2][0] == 3 ) or ( matrix[0][2] == 3 and matrix[1][1] == 2 and matrix[2][0] == 1 ) :
            game_end = True
        else :
            turns = turns + 1    
    sum_turns = sum_turns + turns
mesos = (sum_turns)/100
print (mesos)