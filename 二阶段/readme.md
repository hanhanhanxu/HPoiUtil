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
package Poi.twostage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        File file = new File("E:\\软件设计欠费名单.xlsx");
        List<String> listEx = new ArrayList<>(7);//建议使用ArrayList，get(i)操作更快
        listEx.add("depId");
        listEx.add("className");
        listEx.add("sno");
        listEx.add("name");
        listEx.add("year");
        listEx.add("allAmountArrears");
        listEx.add("amountArrears");

        //所需参数：被读取文件，Excel表中各列代表的属性名字集合，和数据库中的表对应的javabean，Excel文件是否含有表头
        List list = HPoiUtils.exe(file, listEx, Student.class, true);
        if (list==null) {
            System.out.println("读取内容为空，主方法结束。");
            return;
        }
        for (Object o : list) {
            System.out.println(o);
        }
        System.out.println("共有数据条数：" + list.size());
    }
}
```

### 五、测试结果

![lQLsZ6.png](https://s2.ax1x.com/2019/12/30/lQLsZ6.png)

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
```



