package Poi.upgrade;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类属性个数 >= Excel表头单元格的个数
 * 就是说你可以类中又这个属性，但是Excel表格中不需要填写这个属性（比如id）
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
