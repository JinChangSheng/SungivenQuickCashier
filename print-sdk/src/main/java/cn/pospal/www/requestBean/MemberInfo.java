package cn.pospal.www.requestBean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by jinchangsheng on 18/7/27.
 * 会员信息
 */

public class MemberInfo implements Serializable{
    String memberCode;
    String memberName;
    String memberAccountUuid;
    String memberType;
    String memberGrade;
    String memberTypeName;
    String memberCardType;
    String cardNumberType;
    String sex;
    boolean isBirthdate;
    String birthday;
    boolean isIcCard;
    String cardFunction;
    BigDecimal discountRate;
    BigDecimal balance;
    BigDecimal memberTypeDiscountRate;
    String memberScore;
    String memberTel;
    String memGradeValidate;
    String memberId;
    boolean usepwd;
    String cardVersion;
    String cardRandomNum;
    String cardHolder;
    String cardNum;

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberAccountUuid() {
        return memberAccountUuid;
    }

    public void setMemberAccountUuid(String memberAccountUuid) {
        this.memberAccountUuid = memberAccountUuid;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMemberGrade() {
        return memberGrade;
    }

    public void setMemberGrade(String memberGrade) {
        this.memberGrade = memberGrade;
    }

    public String getMemberTypeName() {
        return memberTypeName;
    }

    public void setMemberTypeName(String memberTypeName) {
        this.memberTypeName = memberTypeName;
    }

    public String getMemberCardType() {
        return memberCardType;
    }

    public void setMemberCardType(String memberCardType) {
        this.memberCardType = memberCardType;
    }

    public String getCardNumberType() {
        return cardNumberType;
    }

    public void setCardNumberType(String cardNumberType) {
        this.cardNumberType = cardNumberType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isBirthdate() {
        return isBirthdate;
    }

    public void setBirthdate(boolean birthdate) {
        isBirthdate = birthdate;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isIcCard() {
        return isIcCard;
    }

    public void setIcCard(boolean icCard) {
        isIcCard = icCard;
    }

    public String getCardFunction() {
        return cardFunction;
    }

    public void setCardFunction(String cardFunction) {
        this.cardFunction = cardFunction;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getMemberTypeDiscountRate() {
        return memberTypeDiscountRate;
    }

    public void setMemberTypeDiscountRate(BigDecimal memberTypeDiscountRate) {
        this.memberTypeDiscountRate = memberTypeDiscountRate;
    }

    public String getMemberScore() {
        return memberScore;
    }

    public void setMemberScore(String memberScore) {
        this.memberScore = memberScore;
    }

    public String getMemberTel() {
        return memberTel;
    }

    public void setMemberTel(String memberTel) {
        this.memberTel = memberTel;
    }

    public String getMemGradeValidate() {
        return memGradeValidate;
    }

    public void setMemGradeValidate(String memGradeValidate) {
        this.memGradeValidate = memGradeValidate;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public boolean isUsepwd() {
        return usepwd;
    }

    public void setUsepwd(boolean usepwd) {
        this.usepwd = usepwd;
    }

    public String getCardVersion() {
        return cardVersion;
    }

    public void setCardVersion(String cardVersion) {
        this.cardVersion = cardVersion;
    }

    public String getCardRandomNum() {
        return cardRandomNum;
    }

    public void setCardRandomNum(String cardRandomNum) {
        this.cardRandomNum = cardRandomNum;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}
