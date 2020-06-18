package riun.xyz.Demo5;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
