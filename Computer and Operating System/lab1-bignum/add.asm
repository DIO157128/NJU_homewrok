section .bss
    res  resb 21
    num1 resb 21
    num2 resb 21
    over resb 4
    len1 resb 4
    len2 resb 4
    cmd  resb 200
section .data
    newline db "",0ah
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

startadd:
    push eax
    mov edx ,eax
    push edx

    
    
    ;get length of num1
    mov ebx ,eax
nextcharofnum1:
    cmp byte [eax],"+"
    jz num1finished
    inc eax
    jmp nextcharofnum1
num1finished:
    mov ecx ,eax
    sub ecx ,ebx
    mov [len1], ecx

    ;get length of num2

    add eax ,1
    mov ebx ,eax
nextcharofnum2:
    cmp byte [eax], 10
    jz num2finished
    inc eax
    jmp nextcharofnum2
num2finished:
    sub eax ,ebx
    mov [len2], al
    
    xor eax, eax
    xor ebx, ebx
    xor ecx, ecx
    xor edx, edx
    
    ;initialize num1
    mov eax ,num1
    mov ebx ,15h
initializenum10:
    mov byte[eax] ,0
    dec ebx
    inc eax
    cmp ebx ,0
    jnz initializenum10
    
    xor eax, eax
    xor ebx, ebx

    pop eax
    
    push eax
    mov bl ,byte [len1]
    mov ecx ,num1
    add ecx ,15h
    sub ecx ,ebx
initializenextcharofnum1:
    mov dl , byte [eax]
    sub dl ,48
    mov byte[ecx] , dl
    dec ebx
    inc eax
    inc ecx
    cmp ebx ,0
    jnz initializenextcharofnum1
    
    
    xor eax, eax
    xor ebx, ebx
    xor ecx, ecx
    xor edx, edx
    ;initialize num2
    mov eax ,num2
    mov ebx ,15h
initializenum20:
    mov byte[eax] ,0
    dec ebx
    inc eax
    cmp ebx ,0
    jnz initializenum20
    
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
initializenextcharofnum2:
    mov dl , byte [eax]
    sub dl ,48
    mov byte[ecx] , dl
    dec ebx
    inc eax
    inc ecx
    cmp ebx ,0
    jnz initializenextcharofnum2
    
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

    ;print
    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    mov eax ,15h
    mov ecx ,res
printresabit:
    push eax
    mov eax ,4
    mov ebx ,1
    mov edx ,1
    int 80h
    pop eax
    inc ecx
    dec eax
    cmp eax ,0
    jnz printresabit

    xor eax ,eax
    xor ebx ,ebx
    xor ecx ,ecx
    xor edx ,edx
    mov eax ,4
    mov ebx ,1
    mov ecx ,newline
    mov edx ,1
    int 80h

    pop eax
movetonextnewline:
    inc eax
    cmp byte[eax] ,0ah
    jnz movetonextnewline
    inc eax
    cmp byte[eax] ,"q"
    jz quit
    jmp startadd
quit:   
    ;quit
    mov eax ,1
    mov ebx ,0
    int 80h
    
