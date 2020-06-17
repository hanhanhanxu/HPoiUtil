package riun.xyz.Test;

import org.apache.commons.beanutils.BeanUtils;
import riun.xyz.Utils.HPoiUtils;
import riun.xyz.pojo.Student;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: HanXu
 * on 2020/6/16
 * Class description: 两种读取excel文件方式的测试，除了Demo1中的文件，还需要用到HExcelUtils.pojo.Student
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
        //List<Student> result = HPoiUtils.exe(file, 0, listField, Student.class, true);
        List<Student> result = HPoiUtils.exe(file, listField, Student.class);
        for (Student o : result) {
            System.out.println(o);
            //System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson(o));
            System.out.println();
        }
        System.out.println("共" + result.size() + "条记录");
    }


    /**
     * 不需要传入Class
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void test2() {
        List<String> listField = genListField();
        //执行
        List result = HPoiUtils.exe(file, listField, true);
        for (Object o : result) {
            //类输出
            Student student = new Student();
            try {
                BeanUtils.copyProperties(student, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            System.out.println(student);
            
            //json输出
            /*System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson(o));
            System.out.println();*/
        }
        System.out.println("共" + result.size() + "条记录");
    }
}
