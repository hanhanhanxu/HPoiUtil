package riun.xyz.nice.Demo1.shorthx;

import javassist.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author: HanXu
 * on 2020/6/18
 * Class description: 读取Excel得到对象集合，读取CSV得到对象集合
 */
public class HPoiUtils {

    private static final String CLASSNAME = "genClass";

    /**
     * 供外部调用的参数最少的方法：根据文件生成对象集合
     * @param file 待处理Excel文件
     * @param listField 表头属性集合
     * @return 读取到的对象集合
     */
    //file listField
    public static List exe(File file, List<String> listField) {
        return exe(file, 0, listField, true);
    }

    //file listField hasHeader
    public static List exe(File file, List<String> listField, boolean hasHeader) {
        return exe(file, 0, listField, hasHeader);
    }

    //file sheet listField
    public static List exe(File file, Integer sheetIndex, List<String> listField) {
        return exe(file, sheetIndex, listField, true);
    }

    public static List exe(File file, String sheetName, List<String> listField) {
        return exe(file, sheetName, listField, true);
    }

    //file sheet listField hasHeader
    public static List exe(File file, Integer sheetIndex, List<String> listField, boolean hasHeader) {
        //生成类
        Class c = generateClass(listField);
        return exe(file, sheetIndex, listField, c, hasHeader);
    }

    public static List exe(File file, String sheetName, List<String> listField, boolean hasHeader) {
        //生成类
        Class c = generateClass(listField);
        return exe(file, sheetName, listField, c, hasHeader);
    }

    //file listField class
    public static List exe(File file, List<String> listField, Class c){
        return exe(file, 0, listField, c, true);
    }

    //file listField class hasHeader
    public static List exe(File file, List<String> listField, Class c, boolean hasHeader){
        return exe(file, 0, listField, c, hasHeader);
    }

    //file sheet listField class
    public static List exe(File file, Integer sheetIndex, List<String> listField, Class c){
        return exe(file, sheetIndex, listField, c, true);
    }

    public static List exe(File file, String sheetName, List<String> listField, Class c){
        return exe(file, sheetName, listField, c, true);//sheetName不区分大小写
    }

    /**
     * 供外部调用的全部参数的方法
     * @param file 文件
     * @param sheetIndex 读取的sheet索引值 从0开始
     * @param listField Excel表中每列对应的属性集合
     * @param c 类
     * @param hasHeader Excel是否有头部
     * @return 读取到的对象集合
     */
    //file sheet listField class hasHeader
    public static List exe(File file, Integer sheetIndex, List<String> listField, Class c, boolean hasHeader){
        //参数检查
        Boolean check = check(file, sheetIndex, listField, c);
        if(!check){
            return null;
        }

        //得到结果
        List listResultObj = generateResult(file, sheetIndex, listField, c, hasHeader);
        return listResultObj;
    }

    /**
     * 供外部调用的全部参数的方法
     * @param file 文件
     * @param sheetName 读取的sheet名字 不区分大小写
     * @param listField Excel表中每列对应的属性集合
     * @param c 类
     * @param hasHeader Excel是否有头部
     * @return 读取到的对象集合
     */
    public static List exe(File file, String sheetName, List<String> listField, Class c, boolean hasHeader){
        //参数检查
        Boolean check = check(file, sheetName, listField, c);
        if(!check){
            return null;
        }

        //得到结果
        List listResultObj = generateResult(file, sheetName, listField, c, hasHeader);
        return listResultObj;
    }


    /*******************************************************下面是私有方法*******************************************************************/

    private static List generateResult(File file, Integer sheetIndex, List<String> listField, Class c, boolean hasHeader) {
        Integer len = listField.size();
        List listResultStr = getStr(file, sheetIndex, len); //得到字符串数据集合
        return getObj(listField, listResultStr, c, hasHeader); //得到对象数据集合
    }

    private static List generateResult(File file, String sheetName, List<String> listField, Class c, boolean hasHeader) {
        Integer len = listField.size();
        List listResultStr = getStr(file, sheetName, len); //得到字符串数据集合
        return getObj(listField, listResultStr, c, hasHeader); //得到对象数据集合
    }


    /**
     * 根据属性集合生成类
     * @param listField 存放字段名
     * @return 动态生成的类
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    private static Class generateClass(List<String> listField) {
        // 得到一个池
        ClassPool pool = ClassPool.getDefault();
        // 根据名称在运行期间动态生成一个类
        String className = HPoiUtils.class.getPackage().getName() + "." + CLASSNAME + ".java";
        // 根据类名 动态生成一个CtClass
        CtClass ctClass = pool.makeClass(className);

        // 制作字段
        List<FieldDeclare> fieldList = new ArrayList<>();
        for (String name : listField) {
            fieldList.add(new FieldDeclare(name));
        }

        // 向CtClass中添加字段和get set方法
        for (FieldDeclare filed : fieldList) {
            try {
                addFieldProperty(ctClass, filed.getName(), pool.getCtClass(filed.getClair()));
            } catch (CannotCompileException e) {
                System.out.println("添加字段失败");
                return null;
            } catch (NotFoundException e) {
                System.out.println("添加字段失败");
                return null;
            }
        }

        try {
            return ctClass.toClass();
        } catch (CannotCompileException e) {
            System.out.println("无法转化类");
            return null;
        }
    }

    /**
     * 向类中添加字段和getter setter方法
     * @param ctClass
     * @param fieldName
     * @param fieldClass
     */
    private static void addFieldProperty(CtClass ctClass, String fieldName, CtClass fieldClass) throws CannotCompileException {
        //生成私有属性
        CtField ctField = new CtField(fieldClass, fieldName, ctClass);
        ctField.setModifiers(Modifier.PRIVATE);
        //为类添加属性
        ctClass.addField(ctField);

        char[] cs = fieldName.toCharArray();
        if (cs[0] >= 97 && cs[0] <= 122) {
            cs[0] -= 32;
        }
        String getName = "get" + String.valueOf(cs);
        String setName = "set" + String.valueOf(cs);
        //添加get set方法
        ctClass.addMethod(CtNewMethod.getter(getName, ctField));
        ctClass.addMethod(CtNewMethod.setter(setName, ctField));
    }

    // 字段属性类
    static class FieldDeclare{
        private String clair; // 字段类型  可以通过字段类型名字得到字段类型的类  String -> Class
        private String name; // 字段名

        public FieldDeclare(String name) {
            this.clair = "java.lang.String";
            this.name = name;
        }

        public FieldDeclare(String clair, String name) {
            this.clair = clair;
            this.name = name;
        }

        public String getClair() {
            return clair;
        }

        public void setClair(String clair) {
            this.clair = clair;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    /**
     * 参数检查
     * @param file Excel文件
     * @param sheetIndex sheet索引值
     * @param listField 属性名称集合
     * @param c 对应类
     * @return 是否初始化成功
     * check存在的意义是做检查，检查到什么程度呢？检查到后面所有参数可以直接执行？为了不让最后走到哪了却没执行消耗太大？
     */
    private static Boolean check(File file, Integer sheetIndex, List<String> listField, Class c){
        try {
            //sheetIndex检查
            if (sheetIndex < 0) {
                System.out.println("sheet索引值不能为负数");
                return false;
            }
            //1、listField集合校验
            if (listField == null || listField.size() == 0) {
                System.out.println("listField集合为空, 表头元素对应属性名字的集合是必须的");
                return false;
            }
            for (String ex : listField) {
                if(StringUtils.isEmpty(ex)){
                    System.out.println("listField中不允许出现空字符串");
                    return false;
                }
            }

            //2、文件校验
            String path = file.getPath();

            try (  InputStream inTemp = new FileInputStream(path) ){
                if (inTemp == null) {//有必要吗
                    System.out.println("无法得到文件流");
                    return false;
                }
            } catch (FileNotFoundException e) {
                System.out.println("文件路径错误或在这个路径下找不到此文件");
                return false;
            }

            if (c == null) {
                System.out.println("c为空，Class是必须的");
                return false;
            }
            return true;
        }catch (Exception e){
            System.out.println("HPoiUtils - - - 未知的初始化错误...");
        }
        return false;
    }

    private static Boolean check(File file, String sheetName, List<String> listField, Class c){
        try {
            //sheetName检查
            if (sheetName == null || "".equals(sheetName)) {
                System.out.println("sheet名字不能为空");
                return false;
            }
            //1、listField集合校验
            if (listField == null || listField.size() == 0) {
                System.out.println("listField集合为空, 表头元素对应属性名字的集合是必须的");
                return false;
            }
            for (String ex : listField) {
                if(StringUtils.isEmpty(ex)){
                    System.out.println("listField中不允许出现空字符串");
                    return false;
                }
            }

            //2、文件校验
            String path = file.getPath();

            try (  InputStream inTemp = new FileInputStream(path) ){
                if (inTemp == null) {//有必要吗
                    System.out.println("无法得到文件流");
                    return false;
                }
            } catch (FileNotFoundException e) {
                System.out.println("文件路径错误或在这个路径下找不到此文件");
                return false;
            }

            if (c == null) {
                System.out.println("c为空，Class是必须的");
                return false;
            }
            return true;
        }catch (Exception e){
            System.out.println("HPoiUtils - - - 未知的初始化错误...");
        }
        return false;
    }


    /**
     * 字符串数据对象化
     * @param listField 字段集合
     * @param listResultStr 字符串结果集合
     * @param c 类
     * @param hasHeader excel中是否有表头
     * @return
     */
    private static List getObj(List<String> listField, List<String> listResultStr, Class c, boolean hasHeader){
        //字符串集合对象化
        int fieldNum = listField.size();//属性个数
        int lenResultStr = listResultStr.size();//Excel中的数据总量
        List listResultObj = new ArrayList(lenResultStr / fieldNum);//结果对象, 设置的容量可能会多一个 hasHeader : true

        int index = hasHeader ? fieldNum : 0;//从第哪个下标开始读取listResultStr中的数据
        //循环读取ListS, 每一个for循环封装一个对象
        for ( ; index < lenResultStr; index += fieldNum) {
            try {
                Object o = c.newInstance();//准备实例
                //循环set对象的fieldNum个属性值。一个循环后则一个对象包装完成
                for (int j = 0; j < fieldNum; j++) {
                    if (index + j < lenResultStr) {//大步长 + 小步长 确定了数据在ListS中的索引值
                        String dataStr = listResultStr.get(index + j);//拿到一个数据
                        if(StringUtils.isNotBlank(dataStr)){//如果数据为nul或者""那么直接跳过，最后对象中此属性为null
                            String filedName = listField.get(j);
                            invokeSetterByName(c, o, filedName, dataStr);
                        }//if
                    }//if
                }
                listResultObj.add(o);//装入集合之后该做什么了呢
            }catch (Exception e){
                e.printStackTrace();
            }
        }//for
        return listResultObj;
    }

    /**
     * 为对象执行属性的setter方法：1、生成setter方法，2、将数据转化为对应正确的类型，3、执行setter方法
     * @param c 目标类
     * @param o 目标对象
     * @param filedName 属性名字
     * @param dataStr 属性值
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static void invokeSetterByName(Class c, Object o, String filedName, String dataStr) throws IllegalAccessException, InvocationTargetException {
        String setterMethodName = getSetterMethodName(filedName);
        Method[] methods = c.getDeclaredMethods();//拿到所有方法
        for (Method method : methods) {
            if(setterMethodName.equals(method.getName())){//必须得到同名的方法，然后获取到setter()参数类型，
                //要先判断method的参数类型
                Class<?>[] types = method.getParameterTypes();
                String typeName = types[0].getName();//类型名称 java.lang.Float
                Object data = regression(typeName, dataStr);//根据类型名称将String数据转换成需要的类型的数据  sD已经判断过不为空了
                method.invoke(o,data);//执行set方法
                break;//跳出for循环
                //应该加一个标识，sign = 1 如果sign == 0 那么说明没有进入for循环，那么就说明没有setXxx的方法，就说明这个表头字段错了
            }
        }//for
    }

    /**
     * 获取setter方法名字
     * @param filedName 属性名
     * @return username -> setUsername
     */
    private static String getSetterMethodName(String filedName) {
        StringBuilder sb = new StringBuilder();
        sb.append("set");
        return sb.append(Tran(filedName)).toString();
    }

    /**
     * 从文件中得到String类型的数据
     * @param file 文件
     * @param len excel文件表头长度
     * @return string数据集合
     */
    private static List getStr(File file, Integer sheetIndex, Integer len){
        //分情况处理Excel
        String path = file.getPath();
        InputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误或在这个路径下找不到此文件");
            return null;
        }
        List listResultStr = null;
        try {
            String s = StringUtils.substringAfterLast(path, ".");//文件名，分割
            if("xls".equals(s)){
                listResultStr = doXls(in,sheetIndex,len);
            }else if("xlsx".equals(s)){
                listResultStr = doXlsx(in,sheetIndex,len);
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
        return listResultStr;
    }

    private static List getStr(File file, String sheetName, Integer len){
        //分情况处理Excel
        String path = file.getPath();
        InputStream in = null;
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误或在这个路径下找不到此文件");
            return null;
        }
        List listResultStr = null;
        try {
            String s = StringUtils.substringAfterLast(path, ".");//文件名，分割
            if("xls".equals(s)){
                listResultStr = doXls(in,sheetName,len);
            }else if("xlsx".equals(s)){
                listResultStr = doXlsx(in,sheetName,len);
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
        return listResultStr;
    }

    /**
     * 读取xls文件
     * @param in 文件输入流
     * @param len 最大列数
     * @throws IOException
     */
    private static List doXls(InputStream in, Integer sheetIndex, Integer len) throws IOException {
        List<String> listResultStr = new ArrayList();
        //获取Excel文件对象
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        //获取sheet表对象
        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);//只允许有一个sheet表
        int nRow = sheet.getLastRowNum();//若excel表中有3行，那么nRow=2 : 0,1,2
        for(int i=0; i <= nRow; i++){
            HSSFRow row = sheet.getRow(i);//row:第i+1行
            if(row != null)
                for(int j = 0; j < len; j++){
                    HSSFCell cell = row.getCell(j);//cell:第i+1行第j+1列单元格
                    if(cell == null){
                        listResultStr.add("");
                    }else{
                        String str = cell.toString();
                        listResultStr.add(str);
                    }
                }
        }
        return listResultStr;
    }

    private static List doXls(InputStream in, String sheetName, Integer len) throws IOException {
        List<String> listResultStr = new ArrayList();
        //获取Excel文件对象
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        //获取sheet表对象
        final HSSFSheet sheet = workbook.getSheet(sheetName);
        int nRow = sheet.getLastRowNum();//若excel表中有3行，那么nRow=2 : 0,1,2
        for(int i=0; i <= nRow; i++){
            HSSFRow row = sheet.getRow(i);//row:第i+1行
            if(row != null)
                for(int j = 0; j < len; j++){
                    HSSFCell cell = row.getCell(j);//cell:第i+1行第j+1列单元格
                    if(cell == null){
                        listResultStr.add("");
                    }else{
                        String str = cell.toString();
                        listResultStr.add(str);
                    }
                }
        }
        return listResultStr;
    }

    /**
     * 读取xlsx文件
     * @param in
     * @param len
     * @throws IOException
     */
    private static List doXlsx(InputStream in, Integer sheetIndex, Integer len) throws IOException {
        List<String> listResultStr = new ArrayList();
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        int nRow = sheet.getLastRowNum();
        for(int i = 0; i <= nRow; i++){
            XSSFRow row = sheet.getRow(i);
            if(row != null) //不会读取空行
                for(int j = 0; j < len; j++){
                    XSSFCell cell = row.getCell(j);
                    if(cell == null){
                        listResultStr.add("");
                    }else {
                        String str = cell.toString();
                        listResultStr.add(str);
                    }
                }
        }
        return listResultStr;
    }

    private static List doXlsx(InputStream in, String sheetName, Integer len) throws IOException {
        List<String> listResultStr = new ArrayList();
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        final XSSFSheet sheet = workbook.getSheet(sheetName);
        int nRow = sheet.getLastRowNum();
        for(int i = 0; i <= nRow; i++){
            XSSFRow row = sheet.getRow(i);
            if(row != null)
                for(int j = 0; j < len; j++){
                    XSSFCell cell = row.getCell(j);
                    if(cell == null){
                        listResultStr.add("");
                    }else {
                        String str = cell.toString();
                        listResultStr.add(str);
                    }
                }
        }
        return listResultStr;
    }

    /**
     * 类型回归
     * @param typeName 类型名
     * @param dataStr 字段名
     * @return 将String类型的字段转化为对应类型的字段
     * 目前好像只能转化为 java.lang.Integer java.lang.Float java.lang.String
     */
    private static Object regression(String typeName,String dataStr){
        if (typeName.contains("Integer")) {
            int n = dataStr.indexOf(".");
            if(n > 0) dataStr = dataStr.substring(0, n);
        }
        try {
            Class<?> c = Class.forName(typeName);
            Constructor<?> constructor = c.getConstructor(String.class);//Float有一个传入String字符串的构造方法
            Object o = constructor.newInstance(dataStr);
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 字符串首字母转大写。（反射时通过属性名得到对应setter方法处使用）
     * @param s 原字符串
     * @return 转换后的字符串
     */
    private static String Tran(String s){
        char[] chars = s.toCharArray();
        if (chars[0] >= 97 && chars[0] <= 122)
            chars[0] -= 32;
        return String.valueOf(chars);
    }


    /**
     * 读取csv文件数据
     * @param file 文件
     * @param listField 文件各列对应的属性集合
     * @param c 类
     * @param hasHeader 是否有头
     * @return 读取到的对象集合
     */
    public static List exeCsv(File file, List listField, Class c, boolean hasHeader) {
        //参数检查
        Boolean check = check(file, 0, listField, c);
        if(!check){
            return null;
        }
        //得到结果
        List listResultObj = generateResultCSV(file, listField, c, hasHeader);
        return listResultObj;
    }

    public static List exeCsv(File file, List listField, Class c) {
        return exeCsv(file, listField, c, true);
    }

    public static List exeCsv(File file, List listField) {
        final Class c = generateClass(listField);
        return exeCsv(file, listField, c, true);
    }

    private static List generateResultCSV(File file, List<String> listField, Class c, boolean hasHeader) {
        List listResultStr = readCSV(file); //得到字符串数据集合
        return getObj(listField, listResultStr, c, hasHeader); //得到对象数据集合
    }

    /**
     * 读取csv文件
     * @param file 文件
     * @return 读取到的字符串集合
     * 读取策略：读取每行的全部内容，空也会读取
     */
    private static List<String> readCSV(File file){
        List<String> listResultStr = new ArrayList<>(10);
        try ( BufferedReader bufferedReader = new BufferedReader(new FileReader(file)) ){
            String line = null;
            String[] items = null;
            while((line = bufferedReader.readLine()) != null){
                //数据行
                items = line.split(",", Integer.MAX_VALUE);
                listResultStr.addAll(Arrays.asList(items));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listResultStr;
    }
}
