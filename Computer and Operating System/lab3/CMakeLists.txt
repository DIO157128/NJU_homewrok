cmake_minimum_required(VERSION 3.21)
project(lab3)

set(CMAKE_CXX_STANDARD 14)

include_directories(boot/include)
include_directories(include)

add_executable(lab3
        boot/include/fat12hdr.inc
        boot/include/load.inc
        boot/include/pm.inc
        boot/boot.asm
        boot/boot.bin
        boot/loader.asm
        boot/loader.bin
        include/console.h
        include/const.h
        include/global.h
        include/keyboard.h
        include/keymap.h
        include/proc.h
        include/protect.h
        include/proto.h
        include/string.h
        include/tty.h
        include/type.h
        kernel/clock.c
        kernel/console.c
        kernel/global.c
        kernel/i8259.c
        kernel/keyboard.c
        kernel/main.c
        kernel/proc.c
        kernel/protect.c
        kernel/start.c
        kernel/tty.c
        lib/klib.c)
