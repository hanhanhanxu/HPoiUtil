package HExcelUtils.Demo1;

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
