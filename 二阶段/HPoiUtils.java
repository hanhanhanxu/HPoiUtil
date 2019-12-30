package Poi.twostage;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: HanXu
 * on 2019/12/27
 * Class description: 读取Excel得到对象集合
 */
public class HPoiUtils {

    /**
     * 供外部调用的方法
     * @param filePrm 待处理Excel文件
     * @param listExPrm 表头属性集合
     * @param cPrm 对应类.Class
     * @param signHeader Excel文件中是否有表头 true 是 false 否
     * @return
     */
    public static List exe(File filePrm, List<String> listExPrm, Class cPrm, boolean signHeader){
        //准备工作
        Boolean ready = ready(filePrm, listExPrm, cPrm);
        if (!ready) return null;

        Integer len = listExPrm.size();
        //得到字符串数据集合
        List listS = getStr(filePrm, len);
        //得到对象数据集合
        List listO = getObj(listExPrm, listS, cPrm, signHeader);

        return listO;
    }


    /**
     * 准备工作
     * @param filePrm Excel文件
     * @param listExPrm 表头对应在类中的属性名称
     * @param cPrm 对应类
     * @return 是否初始化成功
     */
    private static Boolean ready(File filePrm, List<String> listExPrm, Class cPrm){
        try {
            //参数校验
            //1、listExPrm集合校验
            if (CollectionUtils.isEmpty(listExPrm)) {
                System.out.println("listExPrm集合为空, 表头元素对应属性名字的集合是必须的");
                return false;
            }
            for (String ex : listExPrm) {
                if(StringUtils.isEmpty(ex)){
                    System.out.println("listExPrm中不允许出现空字符串");
                    return false;
                }
            }

            //2、文件校验
            String path = filePrm.getPath();
            InputStream inTemp = null;
            try {
                inTemp = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                System.out.println("文件路径错误或在这个路径下找不到此文件");
                return false;
            }
            if (inTemp ==null) {
                System.out.println("无法得到文件流");
                return false;
            }
            inTemp.close();

            if (cPrm == null) {
                System.out.println("cPrm为空，Class是必须的");
                return false;
            }
            return true;
        }catch (Exception e){
            System.out.println("HPoiUtil - - - 未知的初始化错误...");
        }
        return false;
    }


    /**
     * 数据对象化
     * 我需要的东西：结果集合List<String>（非对象化） ,  表头字段集合List<Stirng>  ， 要转换的类
     * @param signHeader true代表有表头，false代表无表头
     * @return 解析Excel得到数据集合 List<Class>
     * 其实是将List<String> 转换为 List<Class>
     */
    private static List getObj(List<String> listEx, List<String> listS, Class c, boolean signHeader){
        //字符串集合对象化
        int lenListEx = listEx.size();//表头长度  11
        int sizeListS = listS.size();//Excel中的数据总量
        List listO = new ArrayList(sizeListS / lenListEx);//结果对象, 设置的容量可能会多一个 signHeader : true

        int sLIndex = signHeader ? lenListEx : 0;//i代表从第哪个下标开始读取listS中的数据
        //循环读取ListS, 每一个for循环封装一个对象
        for ( ; sLIndex < sizeListS; sLIndex += lenListEx) {
            try {
                Object o = c.newInstance();//准备实例
                //循环set对象的lenListEx个属性值。一个循环后则一个对象包装完成
                for (int j = 0; j < lenListEx; j++) {
                    if (sLIndex + j < sizeListS) {//大步长 + 小步长 确定了数据在ListS中的索引值
                        String sData = listS.get(sLIndex + j);//拿到一个数据
                        if(StringUtils.isNotBlank(sData)){//如果数据为nul或者""那么直接跳过，最后对象中此属性为null
                            //1、通过listEx得到的属性名组装出setter()方法全名 sSetterName
                            String sSetterName = getSetterName(j, listEx);
                            //2、通过方法全名sA得到对应的方法method；通过得到的参数类型转换参数数据。最后执行setter()方法
                            invokeSetterByName(c, o, sData, sSetterName);
                        }//if
                    }//if
                }
                listO.add(o);//装入集合之后该做什么了呢
            }catch (Exception e){
                e.printStackTrace();
            }
        }//for
        return listO;
    }

    private static void invokeSetterByName(Class c, Object o, String sData, String sSetterName) throws IllegalAccessException, InvocationTargetException {
        Method[] methods = c.getDeclaredMethods();//拿到所有方法
        for (Method method : methods) {
            if(sSetterName.equals(method.getName())){//必须得到同名的方法，然后获取到setter()参数类型，
                //要先判断method的参数类型
                Class<?>[] types = method.getParameterTypes();
                String nameType = types[0].getName();//类型名称 java.lang.Float
                Object data = regression(nameType, sData);//根据类型名称将String数据转换成需要的类型的数据  sD已经判断过不为空了
                method.invoke(o,data);//执行set方法
                break;//跳出for循环
                //应该加一个标识，sign = 1 如果sign == 0 那么说明没有进入for循环，那么就说明没有setXxx的方法，就说明这个表头字段错了
            }
        }//for
    }

    private static String getSetterName(int j, List<String> listEx) {
        String sSetterName = listEx.get(j);//拿到一个Excel表头字段 name pass
        StringBuilder sb = new StringBuilder();
        sb.append("set");
        sSetterName = sb.append(Tran(sSetterName)).toString();
        return sSetterName;
    }


    /**
     * 读取Excel数据到List<String>集合中
     * 我需要的东西：被读取excel文件路径path ， excel文件中的表头长度n
     * @return 解析Excel得到数据集合 List<String>
     *
     *     读取总是从首行开始读取，不管有无表头
     */
    private static List getStr(File file, Integer len){
        //分情况处理Excel
        String path = file.getPath();
        InputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误或在这个路径下找不到此文件");
            return null;
        }
        List listS = new ArrayList<>();
        try {
            String s = StringUtils.substringAfterLast(path, ".");//文件名，分割
            if("xls".equals(s)){
                doXls(in,len,listS);
            }else if("xlsx".equals(s)){
                doXlsx(in,len,listS);
            }else{
                System.out.println("文件格式不支持，请选择xls或xlsx格式的Excel文件");
            }
        } catch (IOException e) {
            System.out.println("通过文件流获取Excel对象失败");
        } finally {
            try {
                in.close();//关闭文件/流
            } catch (IOException e) {
                System.out.println("关闭文件流失败");
            }
        }
        return listS;
    }

    private static void doXls(InputStream in, Integer len, List listS) throws IOException {
        //获取Excel文件对象
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        //获取sheet表对象
        HSSFSheet sheet = workbook.getSheetAt(0);//只允许有一个sheet表
        int nRow = sheet.getLastRowNum();//若excel表中有3行，那么nRow=2 : 0,1,2
        for(int i=0;i<=nRow;i++){
            HSSFRow row = sheet.getRow(i);//row:第i+1行
            if(row!=null)
                for(int j=0;j<len;j++){
                    HSSFCell cell = row.getCell(j);//cell:第i+1行第j列单元格
                    if(cell==null){
                        listS.add("");
                    }else{
                        String str = cell.toString();
                        listS.add(str);
                    }
                }
        }
    }

    private static void doXlsx(InputStream in, Integer len, List listS) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int nRow = sheet.getLastRowNum();
        for(int i=0;i<=nRow;i++){
            XSSFRow row = sheet.getRow(i);
            for(int j=0;j<len;j++){
                XSSFCell cell = row.getCell(j);
                if(cell==null){
                    listS.add("");
                }else {
                    String str = cell.toString();
                    listS.add(str);
                }
            }
        }
    }


    /**
     * 根据nameType是什么类型，就把数据sD转化为什么类型的数据
     * @param nameType 类型名字nameType
     * @param data 数据sD
     * @return 对应类型的数据。
     *
     * 常用类型有java.lang.Float java.lang.String
     */
    private static Object regression(String nameType,String data){
        //如果是java.lang.Integer类型，但是数据data又存在小数点，那么就只取前面的部分
        if (nameType.contains("Integer")) {
            int n = data.indexOf(".");
            if(n > 0) data = data.substring(0, n);
        }
        try {
            Class<?> c = Class.forName(nameType);
            Constructor<?> constructor = c.getConstructor(String.class);//Float有一个传入String字符串的构造方法
            Object o = constructor.newInstance(data);
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串首字母转大写。（反射时通过属性名得到对应setter方法处使用）
     * @param s 被转换的字符串
     * @return 转换后的字符串
     */
    private static String Tran(String s){
        char[] chars = s.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }

}
