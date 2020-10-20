# Java 字节码



## ByteCode 

​	max: 256

​	real: 200~

字节码 -> 指令



分为4类:

	- 操作栈(真正的计算的部分) 包括与局部变量交互指令
	- 程序流程控制
	- 对象操作
	- 算术运算及转换



首先看一个Hello.java的例子

```java
/**
 * @Author nan
 */
public class Hello{

    public static void main(String[] args) {
        System.out.printf("HelloWord");
    }
}
```

先编译class: `javac Hello.java`

使用命令: `javap -c Hello.class ` 查看字节码

> ```sh
> nan@nan:~/Desktop/jk/1$ javap -c Hello.class 
> Compiled from "Hello.java"
> public class Hello {
> public Hello();
>  Code:
>     0: aload_0
>     1: invokespecial #1                  // Method java/lang/Object."<init>":()V
>     4: return
> 
> public static void main(java.lang.String[]);
>  Code:
>     0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
>     3: ldc           #3                  // String HelloWord
>     5: iconst_0
>     6: anewarray     #4                  // class java/lang/Object
>     9: invokevirtual #5                  // Method java/io/PrintStream.printf:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream;
>    12: pop
>    13: return
> }
> nan@nan:~/Desktop/jk/1$ 
> ```
>
> 我们可以看到打印的信息.

也可以存成文本看: `javap -c Hello.java > Hello_ByteCode.txt`



其实: 字节码和16进制,就是很简单的映射,主要就是根据表来进行查表,了解byte代表的含义就是啦.



机器执行class字节码文件本质就是无脑执行class里面的指令即可.