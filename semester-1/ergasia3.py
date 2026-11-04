from pathlib import Path
txt = Path('readme.txt').read_text() #to arxeio readme einai apla tyxaio mporei na einai otidipote


#replace olous tous ASCII xaraktires ektos twn grammatwn 
txt = txt.replace('1', '')
txt = txt.replace('2', '')
txt = txt.replace('3', '')
txt = txt.replace('4', '')
txt = txt.replace('5', '')
txt = txt.replace('6', '')
txt = txt.replace('7', '')
txt = txt.replace('8', '')
txt = txt.replace('9', '')
txt = txt.replace('0', '')
txt = txt.replace('!', '')
txt = txt.replace('@', '')
txt = txt.replace('#', '')
txt = txt.replace('$', '')
txt = txt.replace('%', '')
txt = txt.replace('^', '')
txt = txt.replace('&', '')
txt = txt.replace('*', '')
txt = txt.replace('(', '')
txt = txt.replace(')', '')
txt = txt.replace('-', ' ') #
txt = txt.replace('_', '')
txt = txt.replace('=', '')
txt = txt.replace('+', '')
txt = txt.replace('[', '')
txt = txt.replace(']', '')
txt = txt.replace('{', '')
txt = txt.replace('}', '')
txt = txt.replace('|', '')
txt = txt.replace(';', '')
txt = txt.replace(':', '')
txt = txt.replace('"', '')
txt = txt.replace(',', '')
txt = txt.replace('<', '')
txt = txt.replace('.', '')
txt = txt.replace('>', '')
txt = txt.replace('?', '')
txt = txt.replace('/', '')
txt = txt.replace('~', '')

txt = txt.replace('\n', ' ') # auto bgazei tis grammes 

txt = txt.replace('  ',' ') #kanei replace ta dipla space pou mporei na prokypsoun


# kano to string lista  
lista = txt.split()


#afairo ta zevgaria pou exoun athroisma 20 

i = 1
j = 0

while i < len(lista) :
    j = i - 1
    if len(lista[i]) + len(lista[j]) == 20 :
        lista.pop(i)
        lista.pop(j)
    i = i + 1 
    

#statistika gia to mhkos twn leksewn pou menoun 

i = 0
maxlength = 0

while i < len(lista) :
    if len(lista[i]) > maxlength :
        maxlength = len(lista[i])
    i = i + 1 
       
i = 1

while i <= maxlength :
    j = 0
    plithos = 0
    while j < len(lista) :
        if len(lista[j]) == i :
            plithos = plithos + 1 
        j = j + 1 
        
    print("yparxoun",plithos,"lekseis me",i,"grammata")
    i = i + 1

