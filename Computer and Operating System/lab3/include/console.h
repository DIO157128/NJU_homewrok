
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			      console.h
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#ifndef _ORANGES_CONSOLE_H_
#define _ORANGES_CONSOLE_H_
#define SCREEN_SIZE		(80 * 25)

typedef struct cursor_pos{
    int ptr;
    int pos[SCREEN_SIZE];
    int search_ptr;
}POSSTACK;
typedef struct out_char_stack
{
    int ptr; //offset
    char ch[SCREEN_SIZE];
    int search_ptr;
}OUTCHARSTACK;
/* CONSOLE */
typedef struct s_console
{
	unsigned int	current_start_addr;	/* 当前显示到了什么位置	  */
	unsigned int	original_addr;		/* 当前控制台对应显存位置 */
	unsigned int	v_mem_limit;		/* 当前控制台占的显存大小 */
	unsigned int	cursor;			/* 当前光标位置 */
    unsigned int 	search_pos;
    OUTCHARSTACK out_char_stack;
    POSSTACK pos_stack;
}CONSOLE;
PRIVATE void pushStack(CONSOLE* p_con,int pos);
PRIVATE int popStack(CONSOLE* p_con);
PRIVATE void pushStack(CONSOLE* p_con,int pos){
    p_con->pos_stack.pos[p_con->pos_stack.ptr++] = pos;
}
PRIVATE int popStack(CONSOLE* p_con){
    if(p_con->pos_stack.ptr==0){
        return 0;
    }else{
        p_con->pos_stack.ptr-=1;
        return p_con->pos_stack.pos[p_con->pos_stack.ptr];
    }
}

#define SCR_UP	1	/* scroll forward */
#define SCR_DN	-1	/* scroll backward */


#define SCREEN_WIDTH		80
#define TAB_WIDTH 			4
#define DEFAULT_CHAR_COLOR	0x07	/* 0000 0111 黑底白字 */
#define TAB_CHAR_COLOR 0x2

#endif /* _ORANGES_CONSOLE_H_ */
