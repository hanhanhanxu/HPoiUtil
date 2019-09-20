package Poi.upgrade.two;

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

 /****************test_fork_request********************************
 * 导入 将excel文件中的数据导入到数据库中
 *
 * 但是我们这个只做读取，
 * 读取Excel文件数据到List集合中，
 * 然后将这些散装数据集合封装成对象化集合List<String> -> List<Class>
 * 要求类属性的类型全部写成包装类型，比如int->Integer float->Float
 ******************************************************************/

public class HPoiUtil {

    private List<String> listS;
    private List<String> listEx;
    private List<Class> listO;
    private Class c;
    private File file;
    private Integer len;


    /**
     * 准备工作
     * @param file Excel文件
     * @param listEx 表头对应在类中的属性名称
     * @param c 类
     * @return 是否初始化成功
     */
    public Boolean ready(File file,List listEx,Class c){
        try {
            this.file = file;
            this.listEx = listEx;
            this.c = c;
            this.len = listEx.size();

            listS = new ArrayList<>();
            listO = new ArrayList<>();
            return true;
        }catch (Exception e){
            System.out.println("HPoiUtil - - - 初始化错误...");
        }
        return false;
    }


    /**
     * 数据对象化
     * 我需要的东西：结果集合List<String>（非对象化） ,  表头字段集合List<Stirng>  ， 要转换的类
     * @param listS
     * @param listEx
     * @param c
     * @return 对象化的集合
     * 将List<String> 转换为 List<Class>
     */
    public List s2Obj(){
        //两个集合非空校验
        if(CollectionUtils.isEmpty(listS) || CollectionUtils.isEmpty(listEx)){
            System.out.println("listS或listEx集合为空");
            return null;
        }
        //对ListEx数据判断
        for (String ex : listEx) {
            if(StringUtils.isEmpty(ex)){
                System.out.println("ListEx中每一个数据必须全不为空");
                return null;
            }
        }
        //获取表头长度和数据集合长度
        int lenListEx = listEx.size();//表头长度
        int sizeListS = listS.size();

        List listO = new ArrayList();
        for (int i = lenListEx; i < sizeListS; i+=lenListEx) {
            //每一个for循环封装一个对象
            int j=0;
            try {
                //拿到实例
                Object o = c.newInstance();
                while(j < lenListEx){
                    //每一个while循环内部对对象属性进行set
                    if(i+j<sizeListS){
                        String sD = listS.get(i + j);//拿到一个数据
                        if(StringUtils.isNotBlank(sD)){//如果数据为nul或者""那么直接跳过，最后对象中此属性为null
                            String sA = listEx.get(j);//拿到一个Excel表头字段 name pass
                            //将表头字段字符串封装为其set方法 setNam setPass
                            StringBuilder sb = new StringBuilder();
                            sb.append("set");
                            sA = sb.append(Tran(sA)).toString();

                            Method[] methods = c.getDeclaredMethods();//拿到所有方法
                            for (Method method : methods) {
                                //如果此方法为表头字段的set方法 setName setPass等
                                if(sA.equals(method.getName())){
                                    //要先判断method的参数类型
                                    Class<?>[] types = method.getParameterTypes();
                                    String nameType = types[0].getName();//类型名称 java.lang.Float
                                    Object data = regression(nameType, sD);//根据类型名称将String数据转换成需要的类型的数据  sD已经判断过不为空了
                                    method.invoke(o,data);//执行set方法
                                    break;//跳出for循环
                                    //应该加一个标识，sign = 1 如果sign == 0 那么说明没有进入for循环，那么就说明没有setXxx的方法，就说明这个表头字段错了
                                }
                            }//for
                        }//if
                    }//if
                    j++;
                }//while
                listO.add(o);//装入集合之后该做什么了呢
            }catch (Exception e){
                e.printStackTrace();
            }
        }//for
        return listO;
    }



    /**
     * 读取Excel数据到List<String>集合中
     * 我需要的东西：被读取excel文件路径path ， excel文件中的表头长度n
     * @param path 文件路径
     * @return 数据集合 List<String>
     */
    public List doExcel(){
        String path = file.getPath();
        InputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误或在这个路径下找不到此文件");
            return null;
        }

        try {
            String s = StringUtils.substringAfterLast(path, ".");//文件名，分割
            if("xls".equals(s)){
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
            }else if("xlsx".equals(s)){
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
            }else{
                System.out.println("文件格式不支持，请选择xls或xlsx格式的Excel文件");
            }
        } catch (IOException e) {
            System.out.println("文件流转换为Excel失败");
        } finally {
            try {
                in.close();//关闭文件/流
            } catch (IOException e) {
                System.out.println("关闭文件流失败");
            }
        }

        return listS;
    }


    /**
     * 根据nameType是什么类型，就把数据sD转化为什么类型的数据
     * @param nameType 类型名字nameType
     * @param data 数据sD
     * @return 对应类型的数据。
     *
     * 常用类型有java.lang.Float java.lang.String
     */
    public Object regression(String nameType,String data){
        //如果是java.lang.Integer类型，但是数据data又存在小数点，那么就只取前面的部分
        if(nameType.contains("Integer")){
            int n = data.indexOf(".");
            if(n > 0){
                data = data.substring(0, n);
            }
        }
        try {
            Class<?> c = Class.forName(nameType);
            Constructor<?> constructor = c.getConstructor(String.class);//Float有一个传入String字符串的构造方法
            Object o = constructor.newInstance(data);
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    //字符串首字母转大写
    public String Tran(String s){
        char[] chars = s.toCharArray();
        chars[0]-=32;
        return String.valueOf(chars);
    }

}
