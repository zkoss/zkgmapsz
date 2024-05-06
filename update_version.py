#!/usr/bin/env python3
# update old version string with newer one. This file keeps track of all places to change
import sys
import logging


def replace_string(string1, string2, source_file):
    content = read_content(source_file)
    content = content.replace(string1, string2)
    write_content(content, source_file)


def read_content(source_file):
    fin = open(source_file, "rt")
    content = fin.read()
    fin.close()
    return content


def write_content(content, source_file):
    output_file = open(source_file, "wt")
    output_file.write(content)
    output_file.close()
    print('replace in ' + source_file)


def show_help():
    print("""Replace version string.
    for example, to replace 3.3.1 with 4.0.0, specify 2 arguments:
    3.3.1 4.0.0
    """)

file_list = [
    "pom.xml",
    "gmapszTest/pom.xml",
    "gmapsz/pom.xml",
    "gmapsz/src/main/java/org/zkoss/gmaps/Version.java",
    "gmapsz/src/main/resources/META-INF/MANIFEST.MF",
    "gmapsz/src/main/resources/metainfo/zk/lang-addon.xml",
    "gmapsz/build.gradle"
    ]


def main():
    if len(sys.argv) == 1:
        show_help()
        exit(0)

    version1 = sys.argv[1]
    version2 = sys.argv[2]
    for file in file_list:
        replace_string(version1, version2, file)


if __name__== "__main__":
  main()