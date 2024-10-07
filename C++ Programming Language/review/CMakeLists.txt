cmake_minimum_required(VERSION 3.21)
project(review)

set(CMAKE_CXX_STANDARD 14)
add_compile_options(-fno-elide-constructors)
add_executable(review main.cpp)
