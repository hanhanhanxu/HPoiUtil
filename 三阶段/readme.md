### 本次更新：

1、优化了部分内容。

2、增加了不需要传入类的方式。在测试中，test1()为原版需要传入类，test2()为新增的不许传入类的方式。



### 一、下载

将HPoiUtils.java下载

### 二、准备一个Excel文件

例如：`E:\\软件设计欠费名单.xlsx`

数据格式见截图：（学号已做处理，非真实学号）

![lQbb0f.png](https://s2.ax1x.com/2019/12/30/lQbb0f.png)

### 三、准备你的po

```java
package Poi.twostage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: HanXu
 * on 2019/12/27
 * Class description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer id;
    private Integer depId;
    private String className;
    private String sno;
    private String name;
    private String year;
    private Float allAmountArrears;
    private Float amountArrears;
}
```

### 四、编写简单的测试程序

```java
package HExcelUtils.Demo3;

import HExcelUtils.pojo.Student;
import com.google.gson.GsonBuilder;
import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.commons.beanutils.BeanUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: HanXu
 * on 2020/6/16
 * Class description:
 */
public class Test {

    //准备数据
    static final File file = new File("E:\\1MYKNOW\\HanXu的源代码记录\\HPoi\\三阶段\\软件设计欠费名单.xlsx");
    //准备字段集合
    private static List<String> genListField() {
        List<String> listField = new ArrayList<>(14);//建议使用ArrayList，get(i)操作更快
        listField.add("depId");
        listField.add("className");
        listField.add("sno");
        listField.add("name");
        listField.add("year");
        listField.add("allAmountArrears");
        listField.add("amountArrears");
        return listField;
    }


    //测试
    public static void main(String[] args) throws NotFoundException, CannotCompileException, InvocationTargetException, IllegalAccessException {
        //test1();
        test2();
    }

    /**
     * 需要传入Class
     */
    public static void test1() {
        List<String> listField = genListField();
        //执行
        List result = HPoiUtils.exe(file, listField, Student.class, true);
        for (Object o : result) {
            System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson(o));
            System.out.println();
        }
    }



    /**
     * 不需要传入Class
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static void test2() throws NotFoundException, CannotCompileException, InvocationTargetException, IllegalAccessException {
        List<String> listField = genListField();
        //执行
        List result = HPoiUtils.exe(file, listField, true);
        for (Object o : result) {
            //类输出
            Student student = new Student();
            BeanUtils.copyProperties(student, o);
            System.out.println(student);
            
            //json输出
            /*System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson(o));
            System.out.println();*/
        }
    }
}
```

### 五、测试结果

![NkoX01.jpg](https://s1.ax1x.com/2020/06/16/NkoX01.jpg)

拿到数据的对象集合后方便进行数据库持久化操作。

### 六、注意事项

本工具旨在简化项目中对Excel文件的导入导出（尚未做）功能操作，要明白一个东西的意图是干什么，而不是随意扩展，这样会失去方向。

（po类的属性类型必须为对象类型，不可用int等基本类型）

### 七、依赖

```xml
<!--poi-->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.17</version>
</dependency>
<!--以下是用到的一些工具类-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.5</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>5.2.2.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.12.6</version>
</dependency>
<!--不传入类时，类拷贝-->
<dependency>
    <groupId>commons-beanutils</groupId>
    <artifactId>commons-beanutils</artifactId>
    <version>1.9.3</version>
</dependency>
```



