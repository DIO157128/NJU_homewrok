# CMAKE generated file: DO NOT EDIT!
# Generated by "MinGW Makefiles" Generator, CMake Version 3.21

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

SHELL = cmd.exe

# The CMake executable.
CMAKE_COMMAND = "C:\Program Files\JetBrains\CLion 2021.3\bin\cmake\win\bin\cmake.exe"

# The command to remove a file.
RM = "C:\Program Files\JetBrains\CLion 2021.3\bin\cmake\win\bin\cmake.exe" -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = C:\Users\dell\Desktop\C++\homework1

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = C:\Users\dell\Desktop\C++\homework1\cmake-build-debug

# Include any dependencies generated for this target.
include CMakeFiles/specialele.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/specialele.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/specialele.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/specialele.dir/flags.make

CMakeFiles/specialele.dir/specialele.cpp.obj: CMakeFiles/specialele.dir/flags.make
CMakeFiles/specialele.dir/specialele.cpp.obj: ../specialele.cpp
CMakeFiles/specialele.dir/specialele.cpp.obj: CMakeFiles/specialele.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\dell\Desktop\C++\homework1\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/specialele.dir/specialele.cpp.obj"
	C:\PROGRA~1\JETBRA~1\CLION2~1.3\bin\mingw\bin\G__~1.EXE $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/specialele.dir/specialele.cpp.obj -MF CMakeFiles\specialele.dir\specialele.cpp.obj.d -o CMakeFiles\specialele.dir\specialele.cpp.obj -c C:\Users\dell\Desktop\C++\homework1\specialele.cpp

CMakeFiles/specialele.dir/specialele.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/specialele.dir/specialele.cpp.i"
	C:\PROGRA~1\JETBRA~1\CLION2~1.3\bin\mingw\bin\G__~1.EXE $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\dell\Desktop\C++\homework1\specialele.cpp > CMakeFiles\specialele.dir\specialele.cpp.i

CMakeFiles/specialele.dir/specialele.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/specialele.dir/specialele.cpp.s"
	C:\PROGRA~1\JETBRA~1\CLION2~1.3\bin\mingw\bin\G__~1.EXE $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S C:\Users\dell\Desktop\C++\homework1\specialele.cpp -o CMakeFiles\specialele.dir\specialele.cpp.s

CMakeFiles/specialele.dir/base64.cpp.obj: CMakeFiles/specialele.dir/flags.make
CMakeFiles/specialele.dir/base64.cpp.obj: ../base64.cpp
CMakeFiles/specialele.dir/base64.cpp.obj: CMakeFiles/specialele.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=C:\Users\dell\Desktop\C++\homework1\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/specialele.dir/base64.cpp.obj"
	C:\PROGRA~1\JETBRA~1\CLION2~1.3\bin\mingw\bin\G__~1.EXE $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/specialele.dir/base64.cpp.obj -MF CMakeFiles\specialele.dir\base64.cpp.obj.d -o CMakeFiles\specialele.dir\base64.cpp.obj -c C:\Users\dell\Desktop\C++\homework1\base64.cpp

CMakeFiles/specialele.dir/base64.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/specialele.dir/base64.cpp.i"
	C:\PROGRA~1\JETBRA~1\CLION2~1.3\bin\mingw\bin\G__~1.EXE $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E C:\Users\dell\Desktop\C++\homework1\base64.cpp > CMakeFiles\specialele.dir\base64.cpp.i

CMakeFiles/specialele.dir/base64.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/specialele.dir/base64.cpp.s"
	C:\PROGRA~1\JETBRA~1\CLION2~1.3\bin\mingw\bin\G__~1.EXE $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S C:\Users\dell\Desktop\C++\homework1\base64.cpp -o CMakeFiles\specialele.dir\base64.cpp.s

# Object files for target specialele
specialele_OBJECTS = \
"CMakeFiles/specialele.dir/specialele.cpp.obj" \
"CMakeFiles/specialele.dir/base64.cpp.obj"

# External object files for target specialele
specialele_EXTERNAL_OBJECTS =

specialele.exe: CMakeFiles/specialele.dir/specialele.cpp.obj
specialele.exe: CMakeFiles/specialele.dir/base64.cpp.obj
specialele.exe: CMakeFiles/specialele.dir/build.make
specialele.exe: CMakeFiles/specialele.dir/linklibs.rsp
specialele.exe: CMakeFiles/specialele.dir/objects1.rsp
specialele.exe: CMakeFiles/specialele.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=C:\Users\dell\Desktop\C++\homework1\cmake-build-debug\CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Linking CXX executable specialele.exe"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles\specialele.dir\link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/specialele.dir/build: specialele.exe
.PHONY : CMakeFiles/specialele.dir/build

CMakeFiles/specialele.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles\specialele.dir\cmake_clean.cmake
.PHONY : CMakeFiles/specialele.dir/clean

CMakeFiles/specialele.dir/depend:
	$(CMAKE_COMMAND) -E cmake_depends "MinGW Makefiles" C:\Users\dell\Desktop\C++\homework1 C:\Users\dell\Desktop\C++\homework1 C:\Users\dell\Desktop\C++\homework1\cmake-build-debug C:\Users\dell\Desktop\C++\homework1\cmake-build-debug C:\Users\dell\Desktop\C++\homework1\cmake-build-debug\CMakeFiles\specialele.dir\DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/specialele.dir/depend

