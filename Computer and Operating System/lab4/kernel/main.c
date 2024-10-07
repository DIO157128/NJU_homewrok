
/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                            main.c
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                                    Forrest Yu, 2005
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

#include "type.h"
#include "const.h"
#include "protect.h"
#include "string.h"
#include "proc.h"
#include "tty.h"
#include "console.h"
#include "global.h"
#include "proto.h"


int strategy;//0是读写公平，1是读优先，2是写优先
int pro_state[5];//0是休息，1是等待，2是正在


PRIVATE void init_tasks()
{
	init_screen(tty_table);
	clean(console_table);

	// 表驱动，对应进程0, 1, 2, 3, 4, 5, 6
	int prior[7] = {1, 1, 1, 1, 1, 1, 1};
	for (int i = 0; i < 7; ++i) {
        proc_table[i].ticks    = prior[i];
        proc_table[i].priority = prior[i];
	}

	// initialization
	k_reenter = 0;
	ticks = 0;
	readers = 0;
	writers = 0;
    pro_state[0] = 0;
    pro_state[1] = 0;
    pro_state[2] = 0;
    pro_state[3] = 0;
    pro_state[4] = 0;


	tr = 0;

	strategy = 0; // 切换策略

	p_proc_ready = proc_table;
}
/*======================================================================*
                            kernel_main
 *======================================================================*/
PUBLIC int kernel_main()
{
	disp_str("-----\"kernel_main\" begins-----\n");

	TASK*		p_task		= task_table;
	PROCESS*	p_proc		= proc_table;
	char*		p_task_stack	= task_stack + STACK_SIZE_TOTAL;
	u16		selector_ldt	= SELECTOR_LDT_FIRST;
	int i;
    u8              privilege;
    u8              rpl;
    int             eflags;
	for (i = 0; i < NR_TASKS + NR_PROCS; i++) {
        if (i < NR_TASKS) {     /* 任务 */
                        p_task    = task_table + i;
                        privilege = PRIVILEGE_TASK;
                        rpl       = RPL_TASK;
                        eflags    = 0x1202; /* IF=1, IOPL=1, bit 2 is always 1 */
                }
                else {                  /* 用户进程 */
                        p_task    = user_proc_table + (i - NR_TASKS);
                        privilege = PRIVILEGE_USER;
                        rpl       = RPL_USER;
                        eflags    = 0x202; /* IF=1, bit 2 is always 1 */
                }
                
		strcpy(p_proc->p_name, p_task->name);	// name of the process
		p_proc->pid = i;			// pid
		p_proc->sleeping = 0; // 初始化结构体新增成员
		p_proc->blocked = 0;
		
		p_proc->ldt_sel = selector_ldt;

		memcpy(&p_proc->ldts[0], &gdt[SELECTOR_KERNEL_CS >> 3],
		       sizeof(DESCRIPTOR));
		p_proc->ldts[0].attr1 = DA_C | privilege << 5;
		memcpy(&p_proc->ldts[1], &gdt[SELECTOR_KERNEL_DS >> 3],
		       sizeof(DESCRIPTOR));
		p_proc->ldts[1].attr1 = DA_DRW | privilege << 5;
		p_proc->regs.cs	= (0 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.ds	= (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.es	= (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.fs	= (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.ss	= (8 & SA_RPL_MASK & SA_TI_MASK) | SA_TIL | rpl;
		p_proc->regs.gs	= (SELECTOR_KERNEL_GS & SA_RPL_MASK) | rpl;

		p_proc->regs.eip = (u32)p_task->initial_eip;
		p_proc->regs.esp = (u32)p_task_stack;
		p_proc->regs.eflags = eflags;

		p_proc->nr_tty = 0;

		p_task_stack -= p_task->stacksize;
		p_proc++;
		p_task++;
		selector_ldt += 1 << 3;
	}

	init_tasks();

	init_clock();
    init_keyboard();

	restart();

	while(1){}
}

PRIVATE read_proc(int proc, int slices){
    pro_state[proc] = 2;
	sleep_ms(slices * TIME_SLICE); // 读耗时slices个时间片
    pro_state[proc] = 0;
}

PRIVATE	write_proc(int proc, int slices){
    pro_state[proc] = 2;
	sleep_ms(slices * TIME_SLICE); // 写耗时slices个时间片
    pro_state[proc] = 0;
}

//读写公平方案
void read_gp(int proc, int slices){

	P(&queue);
    P(&n_r_mutex);
	P(&r_mutex);
	if (readers==0)
		P(&rw_mutex); // 有读者，禁止写
	readers++;
	V(&r_mutex);
	V(&queue);
	read_proc(proc, slices);
	P(&r_mutex);
	readers--;
	if (readers==0)
		V(&rw_mutex); // 没有读者，可以开始写了
	V(&r_mutex);
    V(&n_r_mutex);
}

void write_gp(int proc, int slices){

	P(&queue);
	P(&rw_mutex);

	V(&queue);
	// 写过程
	write_proc(proc, slices);

	V(&rw_mutex);
}

// 读者优先
void read_rf(int proc, int slices){


    P(&r_mutex);
    if (readers==0)
        P(&rw_mutex);
    readers++;
    V(&r_mutex);

    read_proc(proc, slices);


    V(&n_r_mutex);

    P(&r_mutex);
    readers--;
    if (readers==0)
        V(&rw_mutex); // 没有读者，可以开始写了
    V(&r_mutex);

}

void write_rf(int proc, int slices){

    P(&rw_mutex);

    // 写过程
    write_proc(proc, slices);

    V(&rw_mutex);
}

// 写者优先
void read_wf(int proc, int slices){
    P(&n_r_mutex);

    P(&queue);
    P(&r_mutex);
    if (readers==0)
        P(&rw_mutex);
    readers++;
    V(&r_mutex);
    V(&queue);

    //读过程开始
    read_proc(proc, slices);

    P(&r_mutex);
    readers--;
    if (readers==0)
        V(&rw_mutex); // 没有读者，可以开始写了
    V(&r_mutex);

    V(&n_r_mutex);
}

void write_wf(int proc, int slices){

    P(&w_mutex);
    // 写过程
    if (writers==0)
        P(&queue);
    writers++;
    V(&w_mutex);

    P(&rw_mutex);
    write_proc(proc, slices);

    V(&rw_mutex);

    P(&w_mutex);
    writers--;
    if (writers==0)
        V(&queue);
    V(&w_mutex);
}

read_f read_funcs[3] = {read_gp, read_rf, read_wf};
write_f write_funcs[3] = {write_gp, write_rf, write_wf};

/*======================================================================*
                               A
 *======================================================================*/
void A()
{
    sleep_ms(TIME_SLICE);
    int time = 1;
    while (1) {
        if(time<21){
            printf("\06%d ", time);
            time+=1;
            for(int i =0 ;i<5;i++){
                if(pro_state[i]==0){
                    printf(" \03Z");
                } else if(pro_state[i]==1){
                    printf(" \01X");
                } else{
                    printf(" \02O");
                }
            }
            printf("\n");
        }

        sleep_ms(TIME_SLICE);

    }

}

/*======================================================================*
                               B
 *======================================================================*/
void B()
{
	sleep_ms(TIME_SLICE);
	while(1){
        pro_state[0]=1;
		read_funcs[strategy](0,2);
		sleep_ms(REST*TIME_SLICE);
	}
}

/*======================================================================*
                               C
 *======================================================================*/
void C()
{
	sleep_ms(TIME_SLICE);
	while(1){
        pro_state[1]=1;
		read_funcs[strategy](1, 3);
		sleep_ms(REST*TIME_SLICE);
	}
}

/*======================================================================*
                               D
 *======================================================================*/
void D()
{
	sleep_ms(TIME_SLICE);
	while(1){
        pro_state[2]=1;
		read_funcs[strategy](2, 3);
		sleep_ms(REST*TIME_SLICE);
	}
}

/*======================================================================*
                               E
 *======================================================================*/
void E()
{
    sleep_ms(TIME_SLICE);
    while(1){
        pro_state[3]=1;
        write_funcs[strategy](3, 3);
        sleep_ms(REST*TIME_SLICE);
    }
}

/*======================================================================*
                               F
 *======================================================================*/
void F()
{
    sleep_ms(TIME_SLICE);
    while(1){
        pro_state[4]=1;
        write_funcs[strategy](4, 4);
        sleep_ms(REST*TIME_SLICE);
    }
}