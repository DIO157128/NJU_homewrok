section .bss
    res  resb 21
    resmul resb 42
    num1 resb 21
    num2 resb 21
    over resb 4
    len1 resb 4
    len2 resb 4
    cmd  resb 200
    lenres resb 4
    lenresmul resb 4
section .data
    newline db "",0ah
    zero db "0",0ah
    error db "Invalid" ,0ah
section .text
global main
main:
    mov ebp, esp; for correct debugging
    ;read cmd
    mov eax ,3
    mov ebx ,0
    mov ecx ,cmd
    mov edx ,50
    int 80h
    
    xor eax ,eax
    mov eax ,cmd

start:
    push eax;in order to move the pointer to cmd to the next valid command
    mov edx ,eax
    push edx;in order to get the start pointer of current command to initialize num1&2


    mov ecx ,eax
operjud:
    cmp byte[ecx] ,0ah
    jz outputerror;if the operator were not valid
    cmp byte[ecx] ,"+"
    jz startadd
    cmp byte[ecx] ,"*"
    jz startmul
    inc ecx
    jmp operjud
    
    
startadd:  
    ;get length of num1
    xor ecx ,ecx
    mov ebx ,eax
addnextcharofnum1:
    cmp byte [eax],"+"
    jz addnum1finished
    inc eax
    jmp addnextcharofnum1
addnum1finished:
    mov ecx ,eax
    sub ecx ,ebx
    mov [len1], ecx

    ;get length of num2

    add eax ,1
    mov ebx ,eax
addnextcharofnum2:
    cmp byte [eax], 10
    jz addnum2finished
    inc eax
    jmp addnextcharofnum2
addnum2finished:
    sub eax ,ebx
    mov [len2], al
    
    xor eax, eax
    xor ebx, ebx
    xor ecx, ecx
    xor edx, edx
    
    ;initialize num1
    mov eax ,num1
    mov ebx ,15h
addinitializenum1:
    mov byte[eax] ,0
    dec ebx
    inc eax
    cmp ebx ,0
    jnz addinitializenum1
    
    xor eax, eax
    xor ebx, ebx

    pop eax;get the start position of command to save num1
    
    push eax
    mov bl ,byte [len1]
    mov ecx ,num1
    add ecx ,15h
    sub ecx ,ebx
addinitializenextcharofnum1:
    mov dl , byte [eax]
    sub dl ,48
    mov byte[ecx] , dl
    dec ebx
    inc eax
    inc ecx
    cmp ebx ,0
    jnz addinitializenextcharofnum1
    
    
    xor eax, eax
    xor ebx, ebx
    xor ecx, ecx
    xor edx, edx
    ;initialize num2
    mov eax ,num2
    mov ebx ,15h
addinitializenum2:
    mov byte[eax] ,0
    dec ebx
    inc eax
    cmp ebx ,0
    jnz addinitializenum2
    
    xor eax, eax
    xor ebx, ebx
    
    pop eax
    ;get the start position of command to save num2
    mov bl ,byte [len1]
    add eax ,ebx
    add eax ,1
    xor ebx ,ebx
    mov bl ,byte[len2]
    mov ecx ,num2
    add ecx ,15h
    sub ecx ,ebx
addinitializenextcharofnum2:
    mov dl , byte [eax]
    sub dl ,48
    mov byte[ecx] , dl
    dec ebx
    inc eax
    inc ecx
    cmp ebx ,0
    jnz addinitializenextcharofnum2
    
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    ;start adding
    
    mov eax ,15h
    mov ebx ,num1
    add ebx ,14h
    mov ecx ,num2
    add ecx ,14h
    
    
oneadd:
    xor edx ,edx
    mov dl ,byte[over]
    add dl ,byte[ebx]
    add dl ,byte[ecx]
    cmp edx ,10
    jge  overflip
    jl notover
overflip:
    push edx
    mov edx ,1h
    mov [over] ,edx
    pop edx
    sub edx ,0ah
    jmp overif1 
notover:
    push edx
    mov edx ,0h
    mov [over] ,edx
    pop edx
    jmp overif1 
overif1:
    push ebx
    mov ebx ,res
    add ebx ,eax
    sub ebx ,1
    add dl ,48
    mov [ebx] ,dl
    pop ebx
    dec eax
    dec ebx
    dec ecx
    cmp eax ,0h
    jnz oneadd

    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    ;calculate the length of res
    mov eax ,res
    add eax ,21
    mov ebx ,res
addcallen:
    cmp ebx ,eax
    jz addprint0
    cmp byte[ebx] ,"0"
    jnz quitaddcallen
    inc ebx
    jmp addcallen
 
addprint0:
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    mov eax ,4
    mov ebx ,1
    mov ecx ,zero
    mov edx ,1
    int 80h 
    mov eax ,4
    mov ebx ,1
    mov ecx ,newline
    mov edx ,1
    int 80h  
    jmp addmovetonextnewlinestart
    
    
quitaddcallen:
    sub eax ,ebx
    mov ecx ,lenres
    mov byte[ecx] ,al   
    

    ;print
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    mov eax ,res
    add eax ,21
    mov ecx ,res
    add ecx ,21
    sub ecx ,[lenres]
addprintresabit:
    push eax
    mov eax ,4
    mov ebx ,1
    mov edx ,1
    int 80h
    pop eax
    inc ecx
    cmp eax ,ecx
    jnz addprintresabit

    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    mov eax ,4
    mov ebx ,1
    mov ecx ,newline
    mov edx ,1
    int 80h

addmovetonextnewlinestart:   
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    pop eax
addmovetonextnewline:
    inc eax
    cmp byte[eax] ,0ah
    jnz addmovetonextnewline
    inc eax
    cmp byte[eax] ,"q"
    jz addquit
    jmp start
addquit:   
    ;quit
    mov eax ,1
    mov ebx ,0
    int 80h
    
    
startmul:  
    ;get length of num1
    xor ecx ,ecx
    mov ebx ,eax
mulnextcharofnum1:
    cmp byte [eax],"*"
    jz mulnum1finished
    inc eax
    jmp mulnextcharofnum1
mulnum1finished:
    mov ecx ,eax
    sub ecx ,ebx
    mov [len1], ecx

    ;get length of num2

    add eax ,1
    mov ebx ,eax
mulnextcharofnum2:
    cmp byte [eax], 10
    jz mulnum2finished
    inc eax
    jmp mulnextcharofnum2
mulnum2finished:
    sub eax ,ebx
    mov [len2], al
    
    xor eax, eax
    xor ebx, ebx
    xor ecx, ecx
    xor edx, edx
    
    ;initialize num1
    mov eax ,num1
    mov ebx ,15h
mulinitializenum1:
    mov byte[eax] ,0
    dec ebx
    inc eax
    cmp ebx ,0
    jnz mulinitializenum1
    
    xor eax, eax
    xor ebx, ebx

    pop eax
    
    push eax
    mov bl ,byte [len1]
    mov ecx ,num1
    add ecx ,15h
    sub ecx ,ebx
mulinitializenextcharofnum1:
    mov dl , byte [eax]
    sub dl ,48
    mov byte[ecx] , dl
    dec ebx
    inc eax
    inc ecx
    cmp ebx ,0
    jnz mulinitializenextcharofnum1
    
    
    xor eax, eax
    xor ebx, ebx
    xor ecx, ecx
    xor edx, edx
    ;initialize num2
    mov eax ,num2
    mov ebx ,15h
mulinitializenum2:
    mov byte[eax] ,0
    dec ebx
    inc eax
    cmp ebx ,0
    jnz mulinitializenum2
    
    xor eax, eax
    xor ebx, ebx
    
    pop eax
    mov bl ,byte [len1]
    add eax ,ebx
    add eax ,1
    xor ebx ,ebx
    mov bl ,byte[len2]
    mov ecx ,num2
    add ecx ,15h
    sub ecx ,ebx
mulinitializenextcharofnum2:
    mov dl , byte [eax]
    sub dl ,48
    mov byte[ecx] , dl
    dec ebx
    inc eax
    inc ecx
    cmp ebx ,0
    jnz mulinitializenextcharofnum2
    
    ;initialize resmul
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx    
    mov ebx ,0
mulinitializeresmul:
    mov eax ,resmul
    add eax ,ebx
    mov ecx ,0
    mov byte[eax] ,cl
    inc ebx
    cmp ebx ,42
    jnz mulinitializeresmul
    
            
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    ;start multiplying
    
    mov ecx ,14h
mulloop1:    
    mov edx ,14h
mulloop2:
    push ecx
    push edx
    mov eax ,num1
    mov ebx ,num2
    add eax ,ecx
    add ebx ,edx
    
    push edx
    xor edx ,edx
    mov dl ,byte[eax]
    mov eax ,edx
    xor edx ,edx
    mov dl ,byte[ebx]
    mov ebx ,edx
    xor edx ,edx
    ;move the pointer to resmul[i+j+1]
    pop edx
    add ecx ,resmul
    add ecx ,edx
    inc ecx
    
    mul ebx

    
    xor ebx,ebx
    mov ebx ,10
    add al ,byte[ecx]
    div ebx
    
    ;remainder is in edx and quotient is in eax 
    
    mov byte[ecx], dl
    dec ecx
    mov dl ,byte[ecx]
    add dl ,al
    mov byte[ecx] ,dl
    pop edx
    pop ecx
    dec edx
    cmp edx ,0
    jge mulloop2
    dec ecx
    cmp ecx ,0
    jge mulloop1
    
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    mov eax,0
muladd0:
    mov ebx ,resmul
    add ebx ,eax
    mov cl ,byte[ebx]
    add ecx ,48
    mov byte[ebx] ,cl
    inc eax
    cmp eax ,42
    jnz muladd0
    
  
    ;calculate the length of resmul
    mov eax ,resmul
    add eax ,42
    mov ebx ,resmul
mulcallen:
    cmp ebx ,eax
    jz mulprint0
    cmp byte[ebx] ,"0"
    jnz quitmulcallen
    inc ebx
    jmp mulcallen
  
mulprint0:
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    mov eax ,4
    mov ebx ,1
    mov ecx ,zero
    mov edx ,1
    int 80h 
    mov eax ,4
    mov ebx ,1
    mov ecx ,newline
    mov edx ,1
    int 80h  
    jmp mulmovetonextnewlinestart
          
quitmulcallen:
    sub eax ,ebx
    mov ecx ,lenresmul
    mov byte[ecx] ,al     
    ;print
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    mov eax ,resmul
    add eax ,42
    mov ecx ,resmul
    add ecx ,42
    sub ecx ,[lenresmul]
mulprintresabit:
    push eax
    mov eax ,4
    mov ebx ,1
    mov edx ,1
    int 80h
    pop eax
    inc ecx
    cmp eax ,ecx
    jnz mulprintresabit

    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    mov eax ,4
    mov ebx ,1
    mov ecx ,newline
    mov edx ,1
    int 80h    
    

    
    
    
mulmovetonextnewlinestart:    
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    pop eax
mulmovetonextnewline:
    inc eax
    cmp byte[eax] ,0ah
    jnz mulmovetonextnewline
    inc eax
    cmp byte[eax] ,"q"
    jz mulquit
    jmp start
mulquit:   
    ;quit
    mov eax ,1
    mov ebx ,0
    int 80h
  
outputerror:
    mov eax ,4
    mov ebx ,1
    mov ecx ,error
    mov edx ,8
    int 80h  
    
    pop eax
errormovetonextnewline:
    inc eax
    cmp byte[eax] ,0ah
    jnz errormovetonextnewline
    inc eax
    cmp byte[eax] ,"q"
    jz quit
    jmp start



quit:   
    ;quit
    mov eax ,1
    mov ebx ,0
    int 80h
    
