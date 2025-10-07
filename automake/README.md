# 1. Tutorial
## 1.1 create source file
vi helloworld.c
## 1.2autoscan

autoscan命令来帮助我们根据目录下的源代码生成一个configure.in的模板文件
文件configure.scan作为configure.in的蓝本
## 1.3 configure.in

mv configure.scan configure.in

将configure.scan改名为configure.in，并且编辑它，按下面的内容修改
```
============================configure.in内容开始=========================================
# -*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

AC_INIT(helloworld.c)
AM_INIT_AUTOMAKE(helloworld, 1.0)

# Checks for programs.
AC_PROG_CC

# Checks for libraries.

# Checks for header files.

# Checks for typedefs, structures, and compiler characteristics.

# Checks for library functions.
AC_OUTPUT(Makefile)
============================configure.in内容结束=========================================
```

执行命令aclocal和autoconf，分别会产生aclocal.m4及configure两个文件

```
$ aclocal 
$ls 
aclocal.m4 configure.in helloworld.c 
$ autoconf 
$ ls 
aclocal.m4 autom4te.cache configure configure.in helloworld.c
```
configure.in内容是一些宏定义，这些宏经autoconf处理后会变成检查系统特性、环境变量、软件必须的参数的shell脚本。  
autoconf 是用来生成自动配置软件源代码脚本（configure）的工具。configure脚本能独立于autoconf运行，且在运行的过程中，不需要用户的干预。  
要生成configure文件，你必须告诉autoconf如何找到你所用的宏。方式是使用aclocal程序来生成你的aclocal.m4。  
aclocal根据configure.in文件的内容，自动生成aclocal.m4文件。aclocal是一个perl 脚本程序，  
它的定义是：“aclocal - create aclocal.m4 by scanning configure.ac”。 
autoconf从configure.in这个列举编译软件时所需要各种参数的模板文件中创建configure。 
autoconf需要GNU m4宏处理器来处理aclocal.m4，生成configure脚本。  
m4是一个宏处理器。将输入拷贝到输出，同时将宏展开。宏可以是内嵌的，也可以是用户定义的。  
除了可以展开宏，m4还有一些内建的函数，用来引用文件，执行命令，整数运算，文本操作，循环等。m4既可以作为编译器的前端，也可以单独作为一个宏处理器。

## 1.4 create file Makefile.am

vi Makefile.am  

内容如下  
```
AUTOMAKE_OPTIONS=foreign
bin_PROGRAMS=helloworld
helloworld_SOURCES=helloworld
```
automake 会根据 Makefile.am 自动生成 Makefile.in  
Makefile.am 中定义的宏和目标,会指导 automake 生成指定的代码。  
例如，宏bin_PROGRAMS将导致编译和连接的目标被生成。

## 1.5 运行automake 

```
$ automake --add-missing
configure.in: installing `./install-sh'
configure.in: installing `./mkinstalldirs'
configure.in: installing `./missing'
Makefile.am: installing `./depcomp'
```
automake会根据Makefile.am文件产生一些文件，包含最重要的Makefile.in

## 1.6 执行 configure 生成 Makefile

```
$ ./configure 
checking for a BSD-compatible install... /usr/bin/install -c
checking whether build environment is sane... yes
checking for gawk... gawk
checking whether make sets $(MAKE)... yes
checking for gcc... gcc
checking for C compiler default output... a.out
checking whether the C compiler works... yes
checking whether we are cross compiling... no
checking for suffix of executables... 
checking for suffix of object files... o
checking whether we are using the GNU C compiler... yes
checking whether gcc accepts -g... yes
checking for gcc option to accept ANSI C... none needed
checking for style of include used by make... GNU
checking dependency style of gcc... gcc3
configure: creating ./config.status
config.status: creating Makefile
config.status: executing depfiles commands
$ ls -l Makefile
-rw-rw-r-- 1 yutao yutao 15035 Oct 15 10:40 Makefile
```

此时Makefile已经产生出来了

## 1.7 使用Makefile编译代码

```
$ make
if gcc -DPACKAGE_NAME="" -DPACKAGE_TARNAME="" -DPACKAGE_VERSION="" -

DPACKAGE_STRING="" -DPACKAGE_BUGREPORT="" -DPACKAGE="helloworld" -DVERSION="1.0" 

-I. -I. -g -O2 -MT helloworld.o -MD -MP -MF ".deps/helloworld.Tpo" \
-c -o helloworld.o `test -f 'helloworld.c' || echo './'`helloworld.c; \
then mv -f ".deps/helloworld.Tpo" ".deps/helloworld.Po"; \
else rm -f ".deps/helloworld.Tpo"; exit 1; \
fi
gcc -g -O2 -o helloworld helloworld.o
```

## 1.8 run hello world

```
$ ./helloworld 
Hello, Linux World!
```

# 2. Instruction

## 2.1 autoscan  
autoscan是用来扫描源代码目录生成configure.scan文件的。  
autoscan可以用目录名做为参数，若不使用参数的话，那么autoscan将使用当前目录。autoscan将扫描指定目录中的源文件，并创建configure.scan文件  

## 2.2 configure.scan 文件 

文件 configure.scan 包含系统配置的基本选项，里面都是一些宏定义。需要将它改名为configure.in

## 2.3 aclocal 

aclocal 是一个 perl 脚本程序，aclocal 根据 configure.in文件的内容，自动生成aclocal.m4文件。  
aclocal的定义是：“aclocal - create aclocal.m4 by scanning configure.ac” 

## 2.4 autoconf  

autoconf是用来产生configure文件的;  
configure是一个脚本，可通过该命令设置源程序来适应各种不同的操作系统平台，并根据不同的系统来产生合适的Makefile，  
从而可以使源代码能在不同的操作系统平台上被编译出来。  
configure.in文件的内容是一些宏，这些宏经过 autoconf 处理后会变成检查系统特性、环境变量、软件必须的参数的shell脚本。  
configure.in文件中的宏的顺序并没有规定，但是必须在所有宏的最前面和最后面分别加上AC_INIT宏和AC_OUTPUT宏。
在configure.ini中：

#号表示注释，这个宏后面的内容将被忽略。
`AC_INIT(FILE)`  这个宏用来检查源代码所在的路径

```
AM_INIT_AUTOMAKE(PACKAGE, VERSION)
```
这个宏是必须的，描述了将要生成的软件包的名字及其版本号：PACKAGE是软件包的名字，VERSION是版本号。  
使用make dist命令时，会生成一个类似helloworld-1.0.tar.gz的软件发行包，其中就有对应的软件包的名字和版本号  

`AC_PROG_CC` 这个宏将检查系统所用的C编译器  

`AC_OUTPUT(FILE)` 这个宏设定要输出的Makefile的名字。 
使用 automake 时，实际上还需要用到其他的一些宏，但可以用 aclocal 来帮助自动产生。  
执行aclocal后会得到aclocal.m4文件。
产生了configure.in和aclocal.m4 两个宏文件后，就可以使用 autoconf 来产生configure文件了。  

## 2.5 Makefile.am

Makefile.am是用来生成Makefile.in的，需要你手工书写。Makefile.am中定义了一些内容  

`AUTOMAKE_OPTIONS `是automake的选项。在执行automake时，会检查目录下是否存在标准GNU软件包中应具备的各种文件，  
例如AUTHORS、ChangeLog、NEWS等文件。将其设置成foreign时，automake会改用一般软件包的标准来检查。  

`bin_PROGRAMS` 指定所要产生的可执行文件的文件名。如果要产生多个可执行文件，那么在各个名字间用空格隔开。

`helloworld_SOURCES`指定产生“helloworld”时所需要的源代码。如果它用到了多个源文件，  
那么请使用空格符号将它们隔开。比如需要helloworld.h，helloworld.c 那么请写成helloworld_SOURCES= helloworld.h helloworld.c。

如果在 bin_PROGRAMS 定义了多个可执行文件，则对应每个可执行文件都要定义相对的filename_SOURCES  

## 2.6 automake

使用automake --add-missing来产生Makefile.in。 
选项--add-missing的定义是“add missing standard files to package”，它会让automake加入一个标准的软件包所必须的一些文件。  
用automake产生出来的Makefile.in文件是符合GNU Makefile惯例的，接下来只要执行configure这个shell 脚本就可以产生合适的 Makefile 文件了。  

## 2.7 Makefile

在符合GNU Makefiel惯例的Makefile中，包含了一些基本的预先定义的操作：  
`make` 根据Makefile编译源代码，连接，生成目标文件，可执行文件。  
`make clean` 清除上次的make命令所产生的object文件（后缀为“.o”的文件）及可执行文件。
`make install`  将编译成功的可执行文件安装到系统目录中，一般为/usr/local/bin目录。
`make dist` 产生发布软件包文件（即distribution package）。这个命令将会将可执行文件及相关文件打包成一个tar.gz压缩的文件用来作为发布软件的软件包。  
该命令会在当前目录下生成一个名字类似“PACKAGE-VERSION.tar.gz”的文件。  
PACKAGE和VERSION，是在configure.in中定义的`AM_INIT_AUTOMAKE(PACKAGE, VERSION)`。  

`make distcheck`  生成发布软件包并对其进行测试检查，以确定发布包的正确性。  
该命令将自动将压缩包解压，然后执行configure命令，并且执行make，来确认编译不出现错误，最后提示软件包已经准备好，可以发布了。

`make distclean` 类似make clean，但同时也将 configure 生成的文件全部删除掉，包括Makefile。  
