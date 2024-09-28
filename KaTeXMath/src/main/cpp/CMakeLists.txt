cmake_minimum_required(VERSION 3.28)
project(katexmath)

enable_language(CXX C)

add_library( # Sets the name of the library.
        katexmath

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             freetype_jni.cpp
             )
#set(PC_HARFBUZZ_INCLUDE_DIRS "harfbuzz/src")

add_subdirectory(harfbuzz harfbuzz)
add_subdirectory(freetype freetype)

target_link_libraries(katexmath log freetype android)
