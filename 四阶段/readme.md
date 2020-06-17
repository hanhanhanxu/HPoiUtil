> 本工具旨在优化提取优化读Excel文件的代码，让更多的人不再写重复代码。



### 更新说明

可以通过传入sheet索引或者名字去指定读取的sheet。默认值为0，即读取第一个sheet。



### 使用说明

您可以使用`List<Student> result = HPoiUtils.exe(file, sheet, listField, Class, false);` 一行代码去读取excel文件中的数据，返回结果存在result中。



### 参数说明

- file：一个Excel文件。**必选**
- sheetIndex或sheetName：sheetIndex代表sheet索引值，即第几个sheet，从0开始；sheetName代表sheet名字。**可选**，默认为0，即读取第一个sheet的内容
- list：属性集合，集合中的元素代表Excel中对应列的意义，一一对应。即List中第一个元素为id，那么代表Excel表中第一列代表id。**必选**
- c：表示想要转化成的对象，即将Excel表中的行记录转化为什么对象。**可选**，不传入时会根据传入list的内容动态生成一个类。大多数时候，建议手动传入
- hasHeader：Excel中是否有有表头，true，是，则读取时不会读取第一行数据；false，否。**可选**，默认为true



### 注意

当Excel存在整行没有数据时，则不会读取。

例如右侧蓝色框内整行无内容，则不会被读取，实际读取内容为红色框内。![NZ9kPU.jpg](https://s1.ax1x.com/2020/06/17/NZ9kPU.jpg)

所以实际读取的内容是这样：![NZ9VxJ.jpg](https://s1.ax1x.com/2020/06/17/NZ9VxJ.jpg)



### 使用示例

1、将HPoiUtils下载

2、准备一个Excel文件E:\1my|软件设计欠费名单.xlsx，Sheet1内容如图：![NZ9Ma6.jpg](https://s1.ax1x.com/2020/06/17/NZ9Ma6.jpg)

Sheet2内容如图：![NZ91PO.jpg](https://s1.ax1x.com/2020/06/17/NZ91PO.jpg)

3、编写测试案例：

#### 我们先得到sheet1中的数据

```java
package HExcelUtils.ReadDemo2;

import HExcelUtils.pojo.Student;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: HanXu
 * on 2020/6/16
 * Class description: 两种读取excel文件方式的测试
 */
public class Test {

    //准备数据
    static final File file = new File("E:\\1my\\软件设计欠费名单.xlsx");
    //准备字段集合
    private static List<String> genListField() {
        List<String> listField = new ArrayList(12);//建议使用ArrayList，get(i)操作更快
        listField.add("depId");
        listField.add("className");
        listField.add("sno");
        listField.add("name");
        listField.add("year");
        listField.add("allAmountArrears");
        listField.add("amountArrears");
        return listField;
    }


    public static void main(String[] args) {
        test1();
        //test2();
    }


    /**
     * 需要传入Class
     */
    public static void test1() {
        List<String> listField = genListField();
        //执行
        List<Student> result = HPoiUtils.exe(file, listField, Student.class);
        for (Student o : result) {
            System.out.println(o);
            //System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson(o));
            System.out.println();
        }
    }

    /**
     * 不需要传入Class
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void test2() throws InvocationTargetException, IllegalAccessException {
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



需要用到的pojo：

```java
package HExcelUtils.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date createTime;
}
```



测试结果：

![NZ9GxH.jpg](https://s1.ax1x.com/2020/06/17/NZ9GxH.jpg)



#### 再测试得到sheet2中的数据：

将上述 test1() 中 `List<Student> result = HPoiUtils.exe(file,  listField, Student.class);` 

替换为 `List<Student> result = HPoiUtils.exe(file, 1, listField, Student.class, false);`

测试结果：

![NZ9YMd.jpg](https://s1.ax1x.com/2020/06/17/NZ9YMd.jpg)



得到对象集合后，即可方便的入库或处理。



### 依赖

```xml
<dependencies>
    <!--poi-->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>3.17</version>
    </dependency>
    <!--javassist.*-->
    <dependency>
        <groupId>org.javassist</groupId>
        <artifactId>javassist</artifactId>
        <version>3.18.2-GA</version>
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
    <!--输出json样式-->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.5</version>
    </dependency>
</dependencies>
```

