package riun.xyz.Demo5;

import com.google.gson.GsonBuilder;

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
    //static final File file = new File("E:\\1MYKNOW\\HanXu的源代码记录\\HPoi\\五阶段\\test.csv");
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
        //test1();
        try {
            test2();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取csv文件，大文件
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
            System.out.println( new GsonBuilder().setPrettyPrinting().create().toJson(o));
        }
        System.out.println(result.size());
    }
}
