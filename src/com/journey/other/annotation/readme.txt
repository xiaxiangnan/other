Java为我们提供的元注解和相关定义注解的语法:

元注解：元注解的作用就是负责注解其他注解

1.@Target: 用于描述注解的使用范围（即：被描述的注解可以用在什么地方）
@Target说明了Annotation所修饰的对象范围,在Annotation类型的声明中使用了target可更加明晰其修饰的目标。
取值(ElementType)有：
  1.CONSTRUCTOR:用于描述构造器
  2.FIELD:用于描述域
  3.LOCAL_VARIABLE:用于描述局部变量
  4.METHOD:用于描述方法
  5.PACKAGE:用于描述包
  6.PARAMETER:用于描述参数
  7.TYPE:用于描述类、接口(包括注解类型) 或enum声明

2.@Retention: 表示需要在什么级别保存该注释信息，用于描述注解的生命周期（即：被描述的注解在什么范围内有效）
@Retention定义了该Annotation被保留的时间长短：某些Annotation仅出现在源代码中，而被编译器丢弃；
而另一些却被编译在class文件中；编译在class文件中的Annotation可能会被虚拟机忽略，
而另一些在class被装载时将被读取（请注意并不影响class的执行，因为Annotation与class在使用上是被分离的）
取值（RetentionPoicy）有：
  1.SOURCE:在源文件中有效（即源文件保留）
  2.CLASS:在class文件中有效（即class保留）
  3.RUNTIME:在运行时有效（即运行时保留）

3.@Documented: 用于描述其它类型的annotation应该被作为被标注的程序成员的公共API.
可以被例如javadoc此类的工具文档化。Documented是一个标记注解，没有成员。

4.@Inherited: 一个标记注解，阐述了某个被标注的类型是被继承的。
如果一个使用了@Inherited修饰的annotation类型被用于一个class，则这个annotation将被用于该class的子类。
注意：@Inherited annotation类型是被标注过的class的子类所继承。类并不从它所实现的接口继承annotation，方法并不从它所重载的方法继承annotation。
当@Inherited annotation类型标注的annotation的Retention是RetentionPolicy.RUNTIME，则反射API增强了这种继承性。
如果我们使用java.lang.reflect去查询一个@Inherited annotation类型的annotation时，
反射代码检查将展开工作：检查class和其父类，直到发现指定的annotation类型被发现，或者到达类继承结构的顶层。


自定义注解：使用@interface自定义注解时，自动继承了java.lang.annotation.Annotation接口，不能继承其他的注解或接口。
@interface用来声明一个注解，其中的每一个方法实际上是声明了一个配置参数.
方法的名称就是参数的名称，返回值类型就是参数的类型（返回值类型只能是基本类型、Class、String、enum）。可以通过default来声明参数的默认值。
注解参数的可支持数据类型：
  1.所有基本数据类型（int,float,boolean,byte,double,char,long,short)
  2.String类型
  3.Class类型
  4.enum类型
  5.Annotation类型
  6.以上所有类型的数组