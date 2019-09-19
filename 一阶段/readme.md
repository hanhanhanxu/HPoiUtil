### 一、下载

将HPoiUtil.java下载（推荐chorme插件 : https://chrome.google.com/webstore/detail/anlikcnbgdeidpacdbdljnabclhahhmd可使你从Github上面下载单个文件）



### 二、准备一个Excel文件

例如：`E:\\工作簿2.xlsx`

数据：

| ERP号  | 店面级别 | 业务类型 | 岗位类型 | 岗位名称   | 应配备人数 | 应认证人数 | 实际认证人数 | 能否空缺 | 能否兼职 | 差价   |
| ------ | -------- | -------- | -------- | ---------- | ---------- | ---------- | ------------ | -------- | -------- | ------ |
| 88888  | A        | 售前     | 关键岗位 | 二手车专员 | 8          | 7          | 6            | 否       | 能       | 2.3    |
| 234234 | B        | 阿斯     | 认证岗位 | 董经理     | 4          | 2          |              | 能       | 否       | 4.2    |
| 34     | 23       | 23       | 3        | 2          |            |            | 1            | 否       | 否       | 5      |
|        | ad       |          |          | asd        |            |            |              |          |          |        |
|        |          |          |          |            |            |            | 3            | 能       |          | 3.2323 |



[![nOKwTJ.png](https://s2.ax1x.com/2019/09/19/nOKwTJ.png)](https://imgchr.com/i/nOKwTJ)



### 三、准备你的po

```java
package hx.insist.com.Poi;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类属性个数 >= Excel表头单元格的个数
 * 就是说你可以类中又这个属性，但是Excel表格中不需要填写这个属性（比如id）
 *
 *@Data
 *@AllArgsConstructor
 *@NoArgsConstructor
 *为lombok注解，在这里相当于getter setter 和全参、无参构造函数
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Key {
    private String id;
    private Integer deptId;
    private String shopLevel;
    private String bussine;
    private String positionType;
    private String position;
    private Integer appion;
    private Integer real;
    private Integer realAll;//你的类的属性是Integer，我就将Excel中对应数据给你搞成Integer
    private String kong;
    private String jian;
    private Float chajia;//你的类的属性是Float，我就将Excel中对应数据给你搞成Float
    private String asd;
}
```



### 四、简单的编写程序测试类

```java
package Poi.upgrade.two;

import Poi.upgrade.Key;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        HPoiUtil hPoiUtil = new HPoiUtil();
        //需要：文件file 表头属性名的集合listEx
        
        File file = new File("F:\\工作簿2.xlsx");
        List<String> lsitEx = new ArrayList<>(11);
        //添加Excel中表头对应类属性的变量名
        lsitEx.add("deptId");
        lsitEx.add("shopLevel");
        lsitEx.add("bussine");
        lsitEx.add("positionType");
        lsitEx.add("position");
        lsitEx.add("appion");
        lsitEx.add("real");
        lsitEx.add("realAll");
        lsitEx.add("kong");
        lsitEx.add("jian");
        lsitEx.add("chajia");
        
        Boolean ready = hPoiUtil.ready(file, lsitEx, Key.class);
        if(ready){
            //得到的listS为数据集合
            List listS = hPoiUtil.doExcel();
            for (Object o : listS) {
                System.out.println(o);
            }

            //得到的listO为数据对象化的集合
            List listO = hPoiUtil.s2Obj();
            for (Object o : listO) {
                System.out.println(o);
            }
        }
    }
}
```



### 五、测试结果

[![nOM1BD.png](https://s2.ax1x.com/2019/09/19/nOM1BD.png)](https://imgchr.com/i/nOM1BD)

**拿到listO后即可方便的做持久化**





### 六、注意事项

本版本，只会读取Excel表格的第一个sheet。从Excel表格中读取的数据全为String类型，从listS到listO做**数据对象化**时，根据你类的属性，将相关数据设置为相关类型。可用类型有String，Integer，Long。

（po类的属性类型必须为对象类型，不可用int等基本类型）







### 七、名词解释

**持久化**：由内存中的对象到硬盘上的数据记录，得到listO后可使用mybatis等工具方便的将数据存放在数据库中。

**数据对象化**：将散装数据根据一定的队则封装为对象，即将各个值输出到类实例化对象的属性上。

