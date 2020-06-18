

> 更新内容：新增读取csv文件



### 更新说明

新增读取csv文件



### 使用说明

你可以使用一句代码 `List<RepeaterDebtTermDto> result = HPoiUtils.exeCsv(file, listField, RepeaterDebtTermDto.class);` 读取你的csv文件，得到结果对象集合。

> csv文件是文本格式文件，虽然可以以Excel方式打开，然后会出现sheet1，可以新建sheet2。但是新增sheet2后再以文本格式打开会覆盖掉sheet1的内容，所以不建议使用excel进行sheet的新增。

### 参数说明

- file：csv文件。**必选**。
- listField：文件中每列代表的属性名字集合。**必选**。
- class：文件中每行对应的对象。**可选**，不传入时程序在运行期间会生成一个动态类。
- hasHeader：是否有表头信息。**可选**，默认为true，即有表头信息。

### 注意

由于csv文件中以逗号分隔，所以当存在这样的数据



【图片1】

![Nn8jvF.jpg](https://s1.ax1x.com/2020/06/18/Nn8jvF.jpg)



因为有逗号的存在，所以仍会读取到。类似这样的数据最后读取后的对象属性全为空。



【图片2】

![NnG9ER.jpg](https://s1.ax1x.com/2020/06/18/NnG9ER.jpg)



### 使用示例

1、将HPoiUtils.java下载，并放入你的项目

2、准备一个csv文件，例如 `E:\\1MYKNOW\\HanXu的源代码记录\\HPoi\\五阶段\\test.csv`



【图片3】

![NnGAgO.jpg](https://s1.ax1x.com/2020/06/18/NnGAgO.jpg)



第一行是表头，所以共有**1719**条数据记录。

【图片7】

![NnGeDH.jpg](https://s1.ax1x.com/2020/06/18/NnGeDH.jpg)

【图片6】

![NnGuVA.jpg](https://s1.ax1x.com/2020/06/18/NnGuVA.jpg)



3、编写测试案例：

#### 我们先使用传入类的方式

```java
package riun.xyz.Demo5;

import org.apache.commons.beanutils.BeanUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: HanXu
 * on 2020/6/17
 * Class description: 读取CSV文件的测试
 * 对应导入功能
 */
public class Test {

    //准备数据
    static final File file = new File("E:\\1MYKNOW\\HanXu的源代码记录\\HPoi\\五阶段\\tb_core_debt_term.csv");
    //准备字段集合
    private static List<String> genListField() {
        List<String> listField = new ArrayList(64);//建议使用ArrayList，get(i)操作更快
        listField.add("id");
        listField.add("debtNo");
        listField.add("merchantId");
        listField.add("createBy");
        listField.add("borrowerId");
        listField.add("outerBorrowerId");
        listField.add("listingId");
        listField.add("loanNo");
        listField.add("outerLoanNo");
        listField.add("loanAmt");
        listField.add("totalPeriod");
        listField.add("periodNo");
        listField.add("dueDate");
        listField.add("status");
        listField.add("initPrincipal");
        listField.add("initInterest");
        listField.add("initFeeAmt");
        listField.add("initAmount");
        listField.add("owingPrincipal");
        listField.add("owingInterest");
        listField.add("owingFeeAmt");
        listField.add("owingPenaltyFee");
        listField.add("owingAmount");
        listField.add("paidPrincipal");
        listField.add("paidInterest");
        listField.add("paidFeeAmt");
        listField.add("paidPenaltyFee");
        listField.add("paidNotifyFee");
        listField.add("paidAmount");
        listField.add("overdueDay");
        listField.add("lastRepayTime");
        listField.add("lastOverdueUpdatetime");
        listField.add("lastOverdueFmtTime");
        listField.add("peakPenaltyFee");
        listField.add("peakNotifyFee");
        listField.add("compensateFlag");
        listField.add("compensateTime");
        listField.add("version");
        listField.add("isactive");
        listField.add("temp1");
        listField.add("temp2");
        listField.add("temp3");
        listField.add("temp4");
        listField.add("temp5");
        listField.add("temp6");
        listField.add("temp7");
        listField.add("temp8");
        listField.add("temp9");
        listField.add("temp10");
        listField.add("inserttime");
        listField.add("updatetime");
        return listField;
    }


    //指定列数
    public static void main(String[] args) {
        test1();
        /*try {
            test2();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 读取csv文件，传入类
     */
    public static void test1() {
        List<String> listField = genListField();
        //执行
        List<RepeaterDebtTermDto> result = HPoiUtils.exeCsv(file, listField, RepeaterDebtTermDto.class);
        for (RepeaterDebtTermDto o : result) {
            System.out.println(o);
            //System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson(o));
        }
        System.out.println(result.size());
    }

    /**
     * 读取csv，不需要传入Class，拿到结果后要手动使用BeanUtils把属性值贴给对应的类
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void test2() throws InvocationTargetException, IllegalAccessException {
        List<String> listField = genListField();
        //执行
        List result = HPoiUtils.exeCsv(file, listField);
        for (Object o : result) {
            /*RepeaterDebtTermDto r = new RepeaterDebtTermDto();
            BeanUtils.copyProperties(r, o);
            System.out.println(r);*/
            System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson(o)); // 以json格式输出
        }
        System.out.println(result.size());
    }
}
```

需要用到的pojo：

```java
package riun.xyz.Demo5;

import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepeaterDebtTermDto {
    private Long id;
    private String debtNo;
    private String merchantId;
    private String createBy;
    private String borrowerId;
    private String outerBorrowerId;
    private String listingId;
    private String loanNo;
    private String outerLoanNo;
    private String loanAmt;
    private String totalPeriod;
    private String periodNo;
    private String dueDate;
    private String status;
    private String initPrincipal;
    private String initInterest;
    private String initFeeAmt;
    private String initAmount;
    private String owingPrincipal;
    private String owingInterest;
    private String owingFeeAmt;
    private String owingPenaltyFee;
    private String owingAmount;
    private String paidPrincipal;
    private String paidInterest;
    private String paidFeeAmt;
    private String paidPenaltyFee;
    private String paidNotifyFee;
    private String paidAmount;
    private String overdueDay;
    private String lastRepayTime;
    private String lastOverdueUpdatetime;
    private String lastOverdueFmtTime;
    private String peakPenaltyFee;
    private String peakNotifyFee;
    private String compensateFlag;
    private String compensateTime;
    private String version;
    private String isactive;
    private String temp1;
    private String temp2;
    private String temp3;
    private String temp4;
    private String temp5;
    private String temp6;
    private String temp7;
    private String temp8;
    private String temp9;
    private String temp10;
    private String inserttime;
    private String updatetime;

    private int taskVersion; // '默认1，每次重跑加1，后续任务使用最大的版本';
    private String batchDate;
    private String channelId;
    private String sourceId;
    private String sid;  // '数据的合同号';
}
```



测试结果：将文件中的数据读取到结果集合中，集合中储存的是对象RepeaterDebtTermDto，最后输出结果条数：



【图片4】

![NnG328.jpg](https://s1.ax1x.com/2020/06/18/NnG328.jpg)



#### 再测试不传入类

将上述 `Test.java`中 `main`方法中的 `test1()`注释，放开`test2()`。

测试结果：

【图片5】

![NnGJKg.jpg](https://s1.ax1x.com/2020/06/18/NnGJKg.jpg)



得到结果对象集合后，即可方便的进行入库或其他处理。

### 依赖：

> 与四阶段的依赖一样。

```xml
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
<!--工具类-->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.5</version>
</dependency>
<!--pojo类的setter getter等方法-->
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
```

