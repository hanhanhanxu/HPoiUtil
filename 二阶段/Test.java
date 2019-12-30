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
