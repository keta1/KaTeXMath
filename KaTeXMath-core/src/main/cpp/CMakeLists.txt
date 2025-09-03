cmake_minimum_required(VERSION 3.31.0)
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

set(FT_DISABLE_HARFBUZZ ON)
add_subdirectory(freetype freetype)

target_link_libraries(katexmath freetype)
