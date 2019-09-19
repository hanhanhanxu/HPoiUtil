package Poi.upgrade.two;

import Poi.upgrade.Key;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        HPoiUtil hPoiUtil = new HPoiUtil();

        File file = new File("F:\\工作簿2.xlsx");
        List<String> lsitEx = new ArrayList<>(11);
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
            List listS = hPoiUtil.doExcel();
            for (Object o : listS) {
                System.out.println(o);
            }

            List listO = hPoiUtil.s2Obj();
            for (Object o : listO) {
                System.out.println(o);
            }
        }
    }
}
